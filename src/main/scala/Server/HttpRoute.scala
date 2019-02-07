package Server

import java.util.concurrent.TimeUnit

import Server.Controller.HttpRequestHandler
import Server.entities._
import akka.actor.{ActorRef, ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpResponse, StatusCodes}
import akka.http.scaladsl.server.{Directives, Route}
import akka.http.scaladsl.settings.ServerSettings
import akka.pattern._
import akka.stream.{ActorMaterializer, Materializer}
import akka.util.Timeout
import com.typesafe.config.ConfigFactory
import io.circe.Decoder
import io.circe.generic.auto._
import io.circe.syntax._
import scala.concurrent.Future
import scala.reflect.runtime.universe.{TypeTag, typeOf}
import scala.util.{Failure, Success, Try}

final case class HttpRoute()(implicit val system: ActorSystem) extends Directives with SprayJsonSupport {

  protected val actorHandler: ActorRef = system.actorOf(Props(classOf[HttpRequestHandler]), "ActorHandler")
  protected implicit val ec = system.dispatcher
  private implicit val materializer: Materializer = ActorMaterializer()
  private implicit val timeout: Timeout = Timeout(5, TimeUnit.SECONDS)
  private val config = ConfigFactory.load()

  def start(): Future[Http.ServerBinding] = {
    Http().bindAndHandle(
      route,
      config.getString("server.bind-interface"),
      config.getInt("server.bind-port"),
      Http().defaultServerHttpContext,
      ServerSettings(system),
      system.log
    )
  }

  private def getMethodName[T](implicit m: TypeTag[T]): String = typeOf[T].typeSymbol.name.toString

  private def handleRoute[T <: Request](implicit decT: Decoder[T], m: TypeTag[T]): Route = {
    val methodName = getMethodName[T].toString
    ((post | get) & path(s"""(?i)$methodName""".r) & entity(as[T])) { request ⇒
      onComplete((actorHandler ? request)
        .mapTo[Option[SuccessResponse]])(response)
    }
  }


  private val route: Route = withSizeLimit(500000) {
    extractClientIP {
      ip ⇒
        pathPrefix("api") {
          handleRoute[AddProviderRequest] ~
            handleRoute[SearchByNameRequest] ~
            handleRoute[SearchByLocationRequest] ~
            handleRoute[AddServiceRequest] ~
            handleRoute[AddUserRequest] ~
            handleRoute[LoginRequest]
        }
    }
  }

  private def response(result: Try[Option[SuccessResponse]]): Route = {
    result match {
      case Success(value) => value match {
        case Some(item) => complete(
          HttpResponse(
            StatusCodes.OK,
            entity = HttpEntity(ContentTypes.`application/json`, item.asJson.toString))
        )
        case _ => complete(HttpResponse(
          StatusCodes.NotFound,
          entity = HttpEntity(ContentTypes.`application/json`, """{"message": "Not Found"}""")))
      }
      case Failure(exception) =>
        println(exception)
        complete(HttpResponse(
          StatusCodes.InternalServerError,
          entity = HttpEntity(ContentTypes.`application/json`, """{"message": "Internal Server Error ...."}""")))
    }
  }
}
