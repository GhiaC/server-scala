//package Server.entities
//
////used from this site
////https://dzone.com/articles/akka-http-another-one-validation-direction
//import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
//import akka.http.scaladsl.server.Directives._
//import akka.http.scaladsl.server.{Directive1, Rejection}
//import spray.json.DefaultJsonProtocol
//
//final case class FieldErrorInfo(name: String, error: String)
//
//final case class ModelValidationRejection(invalidFields: Seq[FieldErrorInfo]) extends Rejection
//
//trait Validator[T] extends (T => Seq[FieldErrorInfo]) {
//  protected def validationStage(rule: Boolean, fieldName: String, errorText: String): Option[FieldErrorInfo] =
//    if (rule) Some(FieldErrorInfo(fieldName, errorText)) else None
//}
//
//object ValidationDirective extends SprayJsonSupport with DefaultJsonProtocol {
//  def validateModel[T](model: T)(implicit validator: Validator[T]): Directive1[T] = {
//    validator(model) match {
//      case Nil => provide(model)
//      case errors: Seq[FieldErrorInfo] => reject(ModelValidationRejection(errors))
//    }
//  }
//}
//
//object RegisterRequestValidator extends Validator[RegisterRequest] {
//  private def nameRule(name: String) = name.length < 2 || name.length > 100
//
//  private def addressRule(name: String) = name.length < 2 || name.length > 100
//
//  private def ageRule(age: Option[Int]) = age.isDefined && (age.get < 16 || age.get > 150)
//
//  override def apply(model: RegisterRequest): Seq[FieldErrorInfo] = {
//
//    val nameErrorOpt: Option[FieldErrorInfo] = validationStage(nameRule(model.name), "name",
//      "name length must be between 2 and 100 characters")
//
//    val ageErrorOpt: Option[FieldErrorInfo] = validationStage(ageRule(Some(model.age)), "age",
//      "age must be between 16 and 150")
//
//    val addressErrorOpt: Option[FieldErrorInfo] = validationStage(addressRule(model.address), "address",
//      "address length must be between 5 and 250 characters")
//
//    (nameErrorOpt :: addressErrorOpt :: ageErrorOpt :: Nil).flatten
//  }
//
//}