package Server.entities

import spray.json.DefaultJsonProtocol._

object Marshaller {

  implicit val getServiceProvider = jsonFormat1(GetServiceProvider)
  implicit val getServiceProviders = jsonFormat1(GetServiceProviders)

  implicit val addService = jsonFormat2(AddServiceRequest)
  implicit val addServiceResponse = jsonFormat2(AddServiceResponse)

  implicit val addProvider = jsonFormat10(AddProviderRequest)
  implicit val addUser = jsonFormat3(AddUserRequest)
  implicit val addProviderResponse = jsonFormat2(AddProviderResponse)
//  implicit val addUserResponse = jsonFormat2(AddUserResponse)

  implicit val loginRequest = jsonFormat2(LoginRequest)

  implicit val searchByName = jsonFormat1(SearchByNameRequest)

  implicit val searchByLocation = jsonFormat2(SearchByLocationRequest)


  implicit val resultResponse = jsonFormat1(ResultResponse)

}