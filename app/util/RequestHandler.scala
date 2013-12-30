package util

import play.api.mvc._
import play.api.templates.Html
import views.html._
import scalatags._
import structures._

/**
 * This is a controller so it can be able to use methods Ok and Redirect from Play!
 * There doesn't seem to be any other way around this.
 */
object RequestHandler extends Controller {
  type MappedRequest = Request[Map[String, Seq[String]]]
  
  implicit class Parameter(paramName: String) {
    def fromRequest(implicit request: MappedRequest): Option[String] = {
      val dataInSeq = request.body.get(paramName)
      dataInSeq flatMap (seq => seq.headOption)
    }
  }
  
  def get(paramName: String): Parameter = new Parameter(paramName)
  
  def handleInputErrors(a: Option[String], b: Option[String])
    (f: (String, String) => Result): Result = {
    val unsafeResult = 
      for(
        safeA <- a;
        safeB <- b
      ) yield f(safeA, safeB)
    unsafeResult getOrElse (Ok(AssetHandler.errorPane))
  }
  
  def handleInputErrors(a: Option[String], b: Option[String], c: Option[String])
    (f: (String, String, String) => Result): Result = {
    val unsafeResult =
      for(
        safeA <- a;
        safeB <- b;
        safeC <- c
      ) yield f(safeA, safeB, safeC)
    unsafeResult getOrElse(Ok(AssetHandler.errorPane))
  }
  
}