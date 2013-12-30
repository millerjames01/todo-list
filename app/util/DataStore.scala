package util

import play.api.mvc._
import play.api.mvc.BodyParsers.parse
import play.api.templates.Html
import views.html._
import scalatags._
import structures._

/**
 * Running the project local purposes, using cookies to store data is fine. But for production,
 * using a database would probably be better. The DataStore trait should allow
 * separate implementations to be swapped out with few maintenance troubles.
 */
trait DataStore {
  def addList(listName: String, newListManagerData: String, initValue: String = "")(toResult: => Result): Result
  def removeList(listName: String, newListManagerData: String)(toResult: => Result): Result
  def modifyList(listName: String, newValue: String)(toResult: => Result): Result
  def addListManager(toResult: => Result): Result
  
  def getList(listName: String): Option[ToDoList]
  def getData(listName: String): Option[String]
  def getAllLists: Option[ListManager]
}

class CookieStore(implicit val session: Session) extends DataStore {
  
   def addList(listName: String, newListManagerData: String, initValue: String = "")
     (toResult:  => Result): Result = 
       toResult.withSession(session + (listName, initValue) + ("\\lists\\", newListManagerData))
   
   def removeList(listName: String, newListManagerData: String)(toResult: => Result): Result = 
     toResult.withSession(session - listName + ("\\lists\\", newListManagerData))
   
   def modifyList(listName: String, newValue: String)(toResult: => Result): Result = 
     toResult.withSession(session + (listName, newValue))
   
   def addListManager(toResult: => Result): Result = 
     toResult.withSession(session + ("\\lists\\", ""))
     
   def getAllLists: Option[ListManager] = 
     session.get("\\lists\\") map (ListManager(_))
   
   def getList(listName: String): Option[ToDoList] = 
     session.get(listName) map (ToDoList(listName, _))
     
   def getData(listName: String): Option[String] = 
     getList(listName) map (_.data)
}