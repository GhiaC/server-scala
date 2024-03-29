package Server.entities

import Server.Models.{ProviderModel, ServiceModel, UserInfo, UserModel}

trait Response

sealed trait SuccessResponse extends Response

sealed trait FailureResponse extends Response

case class ResultResponse(result: String) extends FailureResponse

case class AddProviderResponse(result: Boolean, id: Int) extends SuccessResponse

case class AddServiceResponse(result: Boolean, id: Int) extends SuccessResponse

case class RegisterResponse(result: Boolean, id: Int, token: Option[String]) extends SuccessResponse

case class LoginResponse(result: Boolean, user: Option[UserInfo], token: Option[String]) extends SuccessResponse

case class GetServicesResponse(list: Seq[ServiceModel]) extends SuccessResponse

case class GetServiceProvidersResponse(list: Seq[ProviderModel]) extends SuccessResponse

case class SearchByNameResponse(list: Seq[ProviderModel]) extends SuccessResponse

case class SearchByLocationResponse(list: Seq[ProviderModel]) extends SuccessResponse

case class GetServiceProviderResponse(provider: ProviderModel) extends SuccessResponse
