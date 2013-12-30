package structures

import play.api.mvc.Session
import play.api.templates.Html
import scalatags._

class ListManager(val data: String)(implicit session: Session) {
  import ListManager._
  import ToDoHelpers._
  
  
  val lists = if(data == "") Nil else data.split("\\\\").toList
  
  def add(newList: String): Either[ValidationError, ListManager] = {
    val check = isValidToAdd(newList, this)
    check match {
      case Some(error) => Left(Some(error))
      case None => Right(new ListManager((lists :+ newList).mkString("\\")))
    }
  }
  
  def remove(listName: String): Either[ValidationError, ListManager] = {
    val check = isValidToDelete(listName, this)
    check match {
      case Some(error) => Left(Some(error))
      case None => Right(new ListManager((lists.filter(_ != listName).mkString("\\"))))
    }
  }
  
}

object ListManager {
  type ValidationError = Option[String]
  
  def isValidToAdd(name: String, listManager: ListManager): ValidationError = {
    if(listManager.lists.contains(name)) Some("A list with this name already exists.")
    else if (name.contains("\\")) Some("Item contains illegal character \\.")
    else None
  }
  
  def isValidToDelete(name: String, listManager: ListManager): ValidationError = {
    if(!listManager.lists.contains(name)) Some("List does not contain this item.")
    else None
  }
  
  def selectorFor(item: String): String = "[]"
  
  def apply(data: String)(implicit session: Session): ListManager = {
    new ListManager(data)
  }
}