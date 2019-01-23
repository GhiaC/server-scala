package Server.entities

import spray.json.DefaultJsonProtocol._
import spray.json.RootJsonFormat


object Marshaller {

  implicit val getServiceProvider: RootJsonFormat[GetServiceProvider] = jsonFormat1(GetServiceProvider)
  implicit val getServiceProviders: RootJsonFormat[GetServiceProviders] = jsonFormat1(GetServiceProviders)

  implicit val addService: RootJsonFormat[AddServiceRequest] = jsonFormat2(AddServiceRequest)
  implicit val addServiceResponse: RootJsonFormat[AddServiceResponse] = jsonFormat1(AddServiceResponse)

  implicit val addProvider: RootJsonFormat[AddProviderRequest] = jsonFormat10(AddProviderRequest)
  implicit val addProviderResponse: RootJsonFormat[AddProviderResponse] = jsonFormat1(AddProviderResponse)

  implicit val resultResponse: RootJsonFormat[ResultResponse] = jsonFormat1(ResultResponse)

}