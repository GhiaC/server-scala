//package client
//
//import java.util.concurrent.TimeUnit
//
//import Server.entities.RegisterRequest
//import akka.actor.{ActorSystem, Props}
//import akka.event.Logging
//import akka.stream.ActorMaterializer
//import akka.util.Timeout
//
//import scala.concurrent.ExecutionContextExecutor
//
//class ExtendedString(s: String) {
//  def isNumber: Boolean = s.matches("[+-]?\\d+.?\\d+")
//}
//
//object ClientMain  {
//
//  implicit val system = ActorSystem("client")
//  implicit val materializer: ActorMaterializer = ActorMaterializer()
//  implicit val executionContext: ExecutionContextExecutor = system.dispatcher
//
//  implicit def String2ExtendedString(s: String): ExtendedString = new ExtendedString(s)
//
//  val http = system.actorOf(Props(classOf[RequestSenderActor]), "http")
//
//  val log = Logging(system.eventStream, "logger")
//
//  log.info("Started Client!")
//
//  implicit val timeout: Timeout = Timeout(5, TimeUnit.SECONDS)
//
//  import akka.pattern._
//
//  log.info("success mode")
//  val mat = ActorMaterializer()
//  (http ? RegisterRequest("asdfasdfasdf", 23, "asdfasdfasdfasdf")).map {success =>
//    system.log.info("## Success ##\n {}", success)
//  }
//
//  log.info("failure mode")
//  (http ? RegisterRequest("", 23, "asdfasdfasdfasdf")).map { failure =>
//    system.log.info("## Failure invalid name ##\n {}", failure)
//  }
//
//  (http ? RegisterRequest("adfasdfadf", 230, "asdfasdfasdfasdf")).map { failure =>
//    system.log.info("## Failure invalid age ##\n {}", failure)
//
//  }
//
//  (http ? RegisterRequest("asdfasdfadsf", 23, "")).map { failure =>
//    system.log.info("## Failure invalid address ##\n {}", failure)
//  }
//
//
//  def isAllDigits(x: String) = x forall Character.isDigit
//}
//
