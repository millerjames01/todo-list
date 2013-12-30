package util

import play.api.mvc._
import play.api.templates.Html
import views.html._
import scalatags._
import structures._
import controllers.routes

object AssetHandler {
  implicit def stagToHtml(scalaTag: HtmlTag): Html = Html(scalaTag.toXML.toString)
  
  def asset(srcPath: String) = script().attr(("type" -> "text/javascript")).src(
    routes.Assets.at(srcPath).toString
  )
  
  val animScript = asset("javascripts/animations.js")
  val listScript = asset("javascripts/listManipulation.js")
  val initiationScript = asset("javascripts/initiation.js")
  
  val scripts: List[Html] = List(animScript, listScript, initiationScript)
  
  def errorPane: Html = 
    div.cls("jumbotron")(
      h1("Uh Oh"),
      h2("An error occurred processing your quote.")
    )
    
  def errorItem(error: String): Html =
    li.cls("error")(s"Uh oh! $error")
    
  def itemHtml(text: String): Html = 
    li.cls("item").attr("text" -> text)(
      text, 
      span.cls("glyphicon glyphicon-remove pull-right")
    )
    
  def addListHtml(index: Int): Html = 
    div.id("add-list-window").cls("jumbotron active").attr(("list-num" -> index))(
       h1("Build a New List"),
       div.cls("input-group span2")(
         input.id("list-name").cls("form-control").placeholder("Name your list...").attr(("type" -> "text")),
         span.cls("input-group-btn")(
           button.id("addListButton").cls("btn btn-default").attr("type" -> "button")("+")
         )
       )
     )
}