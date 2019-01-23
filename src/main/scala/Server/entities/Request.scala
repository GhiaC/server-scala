package Server.entities

sealed trait Request


final case class AddProviderRequest(service_id: Int, title: String, picture: String, start_time: String, end_time: String, description: String, phonenumber: String, stars: Int, lat: String, long: String) extends Request

final case class AddServiceRequest(title: String, picture: String) extends Request

final case class AddUserRequest(username: String, password: String) extends Request

final case class SearchByNameRequest(name: String) extends Request

final case class SearchByLocationRequest(lat: String, long: String) extends Request

object GetServices extends Request

final case class GetServiceProviders(serviceId: Int) extends Request

final case class GetServiceProvider(serviceProviderId: Int) extends Request

