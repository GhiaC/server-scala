package Server.Controller

import Server.Models._
import Server.entities
import Server.entities._
import akka.actor.Actor
import akka.pattern._

import scala.concurrent.{ExecutionContextExecutor, Future}

class HttpRequestHandler extends Actor {
  implicit val hdbExt = DBExtension(context.system).hdb
  implicit val ec: ExecutionContextExecutor = context.dispatcher

  def receive: Receive = {
    case r: AddServiceRequest => addService(r) pipeTo sender()
    case r: AddProviderRequest => addProvider(r) pipeTo sender()
    case r: AddUserRequest => addUser(r) pipeTo sender()
    case r: GetServiceProviders => getServiceProviders(r) pipeTo sender()
    case r: GetServiceProvider => getServiceProvider(r) pipeTo sender()
    case r: SearchByLocationRequest => searchByLocation(r) pipeTo sender()
    case r: SearchByNameRequest => searchByName(r) pipeTo sender()
    case r: LoginRequest => login(r) pipeTo sender()
    case GetServices => getServices pipeTo sender()
  }

  def searchByLocation(req: SearchByLocationRequest) = for (result <- hdbExt.run(ProviderRepo.searchByLocation(req.lat, req.long))) yield if (result.nonEmpty) Option(SearchByLocationResponse(result)) else None

  def searchByName(req: SearchByNameRequest) = for (result <- hdbExt.run(ProviderRepo.searchByName(req.name))) yield if (result.nonEmpty) Option(SearchByNameResponse(result)) else None

  def login(req: LoginRequest) = for {
    isExist <- hdbExt.run(UserRepo.login(req.username, req.password))
    result <- if (isExist.nonEmpty) Future(Some(LoginResponse(result = true, Some(UserInfo(isExist.head, req.username))))) else Future(Some(LoginResponse(result = false, None)))
  } yield result

  def addService(addServiceRequest: AddServiceRequest) =
    for {
      isExist <- hdbExt.run(ServicesRepo exists addServiceRequest.title)
      id <- ServicesRepo.getUniqueId
      result <- if (!isExist) {
        val newObject = ServiceModel(id, addServiceRequest.title, addServiceRequest.picture, None)
        hdbExt.run(ServicesRepo create newObject)
        Future(Option(AddServiceResponse(!isExist, id)))
      } else Future(Option(AddServiceResponse(!isExist, -1)))
    } yield result


  def addUser(addUserRequest: AddUserRequest) =
    for {
      isExist <- hdbExt.run(UserRepo exists addUserRequest.username)
      id <- UserRepo.getUniqueId
      result <- if (!isExist) {
        val newObject = UserModel(id, addUserRequest.username, addUserRequest.password, "now")
        hdbExt.run(UserRepo create newObject)
        Future(Some(AddUserResponse(result = !isExist, id)))
      } else Future(Some(AddUserResponse(result = !isExist, -1)))
    } yield result


  def addProvider(req: AddProviderRequest) =
    for {
      isExist <- hdbExt.run(ProviderRepo exists req.title)
      id <- ProviderRepo.getUniqueId
      result <- if (!isExist) {
        val newObject = ProviderModel(id, req.service_id, req.title, req.picture, req.start_time, req.end_time, req.description, req.phonenumber, req.stars, req.lat, req.long, None)
        hdbExt.run(ProviderRepo create newObject)
        Future(Some(AddProviderResponse(!isExist, id)))
      } else Future(Some(AddProviderResponse(!isExist, -1)))
    } yield result


  def getServices = for (result <- hdbExt.run(ServicesRepo.getAll)) yield Option(GetServicesResponse(result))

  def getServiceProvider(req: GetServiceProvider) = for (result <- hdbExt.run(ProviderRepo.get(req.serviceProviderId))) yield if (result.nonEmpty) Option(GetServiceProviderResponse(result.head)) else None

  def getServiceProviders(message: GetServiceProviders) =
    for (providers <- hdbExt.run(ProviderRepo getProviders message.serviceId)) yield {
      if (providers.nonEmpty) Option(GetServiceProvidersResponse(providers)) else None
    }


}
