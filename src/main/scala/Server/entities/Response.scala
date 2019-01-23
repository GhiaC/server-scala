package Server.entities

import Server.Models.{ProviderModel, ServiceModel}

trait Response

sealed trait SuccessResponse extends Response

sealed trait FailureResponse extends Response

case class ResultResponse(result: String) extends FailureResponse

case class AddProviderResponse(id: Int) extends SuccessResponse

case class AddServiceResponse(id: Int) extends SuccessResponse

case class GetServicesResponse(list: Seq[ServiceModel]) extends SuccessResponse

case class GetServiceProvidersResponse(list: Seq[ProviderModel]) extends SuccessResponse

case class GetServiceProviderResponse(provider: ProviderModel) extends SuccessResponse
