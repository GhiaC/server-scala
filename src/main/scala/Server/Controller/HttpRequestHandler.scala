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
    case r: GetServiceProviders => getServiceProviders(r) pipeTo sender()
    case r: GetServiceProvider => getServiceProvider(r) pipeTo sender()
    case r: SearchByLocationRequest => searchByLocation(r) pipeTo sender()
    case r: SearchByNameRequest => searchByName(r) pipeTo sender()
    case GetServices => getServices pipeTo sender()
  }

  def searchByLocation(req: SearchByLocationRequest) = for (result <- hdbExt.run(ProviderRepo.searchByLocation(req.lat, req.long))) yield if (result.nonEmpty) Option(GetServiceProviderResponse(result.head)) else None

  def searchByName(req: SearchByNameRequest) = for (result <- hdbExt.run(ProviderRepo.searchByName(req.name))) yield if (result.nonEmpty) Option(GetServiceProviderResponse(result.head)) else None

  def addService(addServiceRequest: AddServiceRequest) =
    for {
      id <- ServicesRepo.getUniqueId
      newObject = ServiceModel(id, addServiceRequest.title, addServiceRequest.picture, None)
      _ <- hdbExt.run(ServicesRepo create newObject)
    } yield Option(AddServiceResponse(id))

  def addProvider(req: AddProviderRequest) =
    for {
      id <- ProviderRepo.getUniqueId
      newObject = ProviderModel(id, req.service_id, req.title, req.picture, req.start_time, req.end_time, req.description, req.phonenumber, req.stars, req.lat, req.long, None)
      r <- hdbExt.run(ProviderRepo create newObject)
    } yield Option(AddProviderResponse(id))


  def getServices = for (result <- hdbExt.run(ServicesRepo.getAll)) yield Option(GetServicesResponse(result))

  def getServiceProvider(req: GetServiceProvider) = for (result <- hdbExt.run(ProviderRepo.get(req.serviceProviderId))) yield if (result.nonEmpty) Option(GetServiceProviderResponse(result.head)) else None

  def getServiceProviders(message: GetServiceProviders) =
    for (providers <- hdbExt.run(ProviderRepo getProviders message.serviceId)) yield {
      if (providers.nonEmpty) Option(GetServiceProvidersResponse(providers)) else None
    }


}
