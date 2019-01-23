//package client
//
//import Server.entities.RegisterRequest
//import akka.actor.{Actor, ActorSystem}
//import akka.event.Logging
//import akka.http.scaladsl.Http
//import akka.http.scaladsl.model.{HttpEntity, _}
//import com.typesafe.config.{Config, ConfigFactory}
//
//import scala.concurrent.{ExecutionContextExecutor, Future}
//import scala.util.{Failure, Success}
//
//case class GetInfo(userId: Int)
//
//case class SuccessResponse(json: String)
//
//case class FailureResponse(response: String)
//
//class RequestSenderActor extends Actor {
//
//  import spray.json._
//
//  implicit val printer: PrettyPrinter.type = PrettyPrinter
//
//  implicit val system: ActorSystem = context.system
//  implicit val ec: ExecutionContextExecutor = context.dispatcher
//
//  val log = Logging(context.system.eventStream, "logger")
//
//  val conf: Config = ConfigFactory.load()
//  val baseUrl: String = conf.getString("base-url")
//
//
//  def getInfo(userId: Int)(implicit system: ActorSystem): Future[Either[FailureResponse, SuccessResponse]] = {
//    val request: HttpRequest = HttpRequest(HttpMethods.GET, uri = baseUrl + "user/salary/" + userId)
//    Http().singleRequest(request) transform {
//      case Success(value) => {
//        value match {
//          case HttpResponse(StatusCodes.OK, _, _, _) =>
//            Success(Right(SuccessResponse(value.entity.toString)))
//          case resp =>
//            Success(Left(FailureResponse(resp.toString())))
//        }
//      }
//      case Failure(_) => Success(Left(FailureResponse("Connection failed!")))
//    }
//  }
//
//  import Server.entities.RegisterRequestMarshaller._
//
//  def registerRequest(registerRequest: RegisterRequest)(implicit system: ActorSystem): Future[Either[FailureResponse, SuccessResponse]] = {
//    val request: HttpRequest = HttpRequest(HttpMethods.POST,
//      entity = HttpEntity(ContentTypes.`application/json`, registerRequest.toJson.toString()),
//      uri = baseUrl + "user/register"
//    )
//    Http().singleRequest(request) transform {
//      case Success(value) => {
//        value match {
//          case HttpResponse(StatusCodes.OK, _, _, _) => {
//            Success(Right(SuccessResponse(value.entity.toString)))
//          }
//          case resp =>
//            Success(Left(FailureResponse(resp.toString())))
//        }
//      }
//      case Failure(_) => Success(Left(FailureResponse("Connection failed!")))
//    }
//  }
//
//  import akka.pattern._
//
//
//  def receive: PartialFunction[Any, Unit] = {
//    case GetInfo(userId) =>
//      val replyTo = sender()
//      getInfo(userId)(context.system) pipeTo replyTo
//
//    case g: RegisterRequest =>
//      val replyTo = sender()
//      registerRequest(g) pipeTo replyTo
//  }
//
//}