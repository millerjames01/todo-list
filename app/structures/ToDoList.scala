package structures

import play.api.mvc.Session
import play.api.templates.Html
import scalatags._
import util.AssetHandler._

class ToDoList(val name: String, val data: String)(implicit session: Session) {
  import ToDoList._
  import ToDoHelpers._
  
  val list = if(data == "") Nil else data.split("\\\\").toList
  val length = list.length
  
  def +(newItem: String): Either[ValidationError, ToDoList] = {
    val check = isValidToAdd(newItem, this)
    check match {
      case Some(error) => Left(check)
      case None => Right(ToDoList(name, ( list :+ newItem ).mkString("\\")))
    }
  }
  
  def -(item: String): Either[ValidationError, ToDoList] = {
    val check = isValidToDelete(item, this)
    check match {
      case Some(error) => Left(check)
      case None => Right(ToDoList(name, list.filter(_ != item).mkString("\\")))
    }
  }
  
  def render(num: Int): Html = 
    div(
      div(
        h1(name).cls("list-title"),
        ul(
          for(item <- list) yield {
            li(item, span().cls("glyphicon glyphicon-remove pull-right")).cls("item").attr("text" -> item)
          }
        ).cls("to-do-list list-unstyled"),
        div(
          input().attr("type" -> "text").id("add-input").placeholder("Press Enter to add this item").cls("form-control"),
          div.cls("btn-group")(
              button("Add another list").cls("btn btn-default").id("go-to-add-list"),
              button("Delete this list").cls("btn btn-default").id("delete-list")
          )
        ).cls("input-group")
      ).cls(s"jumbotron to-do-window"),
      p.cls("help-info")("Click a list item to be able to delete it, and then press the right arrow key to change lists.")
    ).attr(("list-num" -> num), ("list-name" -> name)).cls("active")
}

object ToDoList {
  import ToDoHelpers._
  
  def isValidList(todo: ToDoList)(implicit session: Session): ValidationError = {
    if(session.get(todo.name).isDefined) Some("A list with this name already exists.")
    else None
  }
  
  def isValidToAdd(item: String, todo: ToDoList): ValidationError = {
    if (item.length > 50) Some("Item length is greater than 50.")
    else if (item.contains("\\")) Some("Item contains illegal character \\.")
    else if (todo.list.contains(item)) Some("Identical item in list.")
    else if (todo.length >= 24) Some("Too many items in list, cannot add another.")
    else None
  }
  
  def isValidToDelete(item: String, todo: ToDoList): ValidationError = {
    if(!todo.list.contains(item)) Some("List does not contain this item.")
    else None
  }
  
  def selectorFor(item: String): String = "[]"
  
  def apply(name: String, data: String)(implicit session: Session): ToDoList = {
    new ToDoList(name, data)
  }
}

object ToDoHelpers {
  type ValidationError = Option[String]
}