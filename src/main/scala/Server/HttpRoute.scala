package Server

import java.util.concurrent.TimeUnit

import akka.http.scaladsl.model.HttpHeader
import akka.http.scaladsl.server.Directive0
//import Server.Controller.CORSHandler
import Server.Controller.HttpRequestHandler
import akka.actor.{ActorRef, ActorSystem, Props}
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpResponse, StatusCodes}
import akka.http.scaladsl.server.{Directives, Route}
import akka.pattern._
import akka.util.Timeout
import io.circe.generic.auto._
import io.circe.syntax._
import io.circe.{Decoder, Encoder}

import scala.reflect.runtime.universe.{TypeTag, typeOf}
import scala.util.{Failure, Success, Try}


case class HttpRoute()(implicit val system: ActorSystem) extends Directives with SprayJsonSupport {

  import Server.entities.Marshaller._
  import Server.entities._

  private val actorHandler: ActorRef = system.actorOf(Props(classOf[HttpRequestHandler]), "ActorHandler")

  implicit val timeout: Timeout = Timeout(5, TimeUnit.SECONDS)

  def handleRequest(request: Request) = onComplete((actorHandler ? request).mapTo[Option[SuccessResponse]])(response)

  def getMethodName[T](implicit m: TypeTag[T]): String = typeOf[T].typeSymbol.name.toString

  import ch.megard.akka.http.cors.scaladsl.CorsDirectives._

  def handleRoute[R, T <: Request](implicit decT: Decoder[T], encR: Encoder[R], m: TypeTag[T]): Route = {
    val methodName = getMethodName[T].toString
    //    ((post | get) & path(s"""(?i)$methodName""".r / IntNumber)) { (_, id) ⇒
    //      val getServiceProviderName = getMethodName[GetServiceProvider]
    //      methodName match {
    //        case `getServiceProviderName` => handleRequest(GetServiceProvider(id))
    //        case _ => complete("not handled")
    //      }
    //    } ~
    ((post | get) & path(s"""(?i)$methodName""".r)) { _ ⇒
      val getUser = getMethodName[GetUser.type]
      methodName match {
        case `getUser` => handleRequest(GetUser)
        case _ => complete("not handled")
      }
    }
  }

  val route: Route =
    cors() {
      pathPrefix("api") {
        handleRoute[Boolean, GetUser.type] ~
          ((put | post) & path("register")) (entity(as[AddUserRequest])(handleRequest)) ~
          ((put | post) & path("login")) (entity(as[LoginRequest])(handleRequest)) ~
          headerValueByName("Authorization"){
            Authorization =>
              (post & path(Segment)) { seg =>
                println(seg)
                println(Authorization)

                complete("{" +
                  "\"Response\":{" +
                  "\"name\":\"masoud\"" +
                  "}" +
                  "}")

              }
          }
      }
    }


  def response(result: Try[Option[SuccessResponse]]): Route = {
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
