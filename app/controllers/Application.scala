package controllers

import play.api.mvc._
import play.api.mvc.BodyParsers.parse
import play.api.templates.Html
import views.html._
import scalatags._
import structures._
import util._
import util.AssetHandler._
import util.RequestHandler._

object Application extends Controller {
  val parseRequestAsMap = parse.tolerantFormUrlEncoded
  
  def dataStore(implicit session: Session): DataStore = new CookieStore
  
  def index = Action { implicit request =>
    dataStore.getAllLists match {
      case Some(listManager) => {
        val maybeName = listManager.lists.headOption
        val maybeData = maybeName flatMap (dataStore.getData(_))
        val maybeList = maybeData.map(ToDoList(maybeName.get, _))
        Ok(welcome(maybeList, AssetHandler.scripts))
      }
      case None => dataStore.addListManager { 
        Ok(welcome(None, scripts)) 
      }
    }
  }
  
  def add = Action (parseRequestAsMap) { implicit request =>
    val maybeItem = get ("newItem") fromRequest
    val maybeName = get ("listName") fromRequest
    val maybeData = maybeName flatMap (dataStore.getData(_))
    
    handleInputErrors(maybeItem, maybeName, maybeData) {
      (item: String, name: String, data: String) => {
        val oldToDoList = ToDoList(name, data)
        val newToDoList = oldToDoList + item
        newToDoList match {
          case Left(ve) => Ok(ve.get)
          case Right(validatedList) => {
            dataStore.modifyList(name, validatedList.data) {
              Ok(itemHtml(item))
            }
          } 
        }
      }
    }
  }
  
  def remove = Action (parseRequestAsMap) { implicit request =>
    val maybeItem = get("toDelete") fromRequest
    val maybeName = get("listName") fromRequest
    val maybeData = maybeName flatMap (dataStore.getData(_))
    
    handleInputErrors(maybeItem, maybeName, maybeData) {
      (item: String, name: String, data: String) => {
        val newToDoList = ToDoList(name, data) - item
        newToDoList match {
          case Left(ve) => Ok(ve.get)
          case Right(validatedList) => {
            dataStore.modifyList(name, validatedList.data) { Ok(item) }
          }
        }
      }
    }
  }
  
  def addList = Action (parseRequestAsMap) { implicit request =>
    val maybeName = get ("name") fromRequest
    val maybeLists = dataStore.getAllLists map (_.data)
    
    handleInputErrors(maybeName, maybeLists) {
      (name, lists) => {
        val newToDoList = ToDoList(name, "")
        val listManager = ListManager(lists)
        println(listManager.lists)
        println()
        listManager.add(name) match {
          case Left(error) => Ok(error.get)
          case Right(valListMngr) => dataStore.addList(name, valListMngr.data) {
              println(valListMngr.lists)
              println()
              Ok(newToDoList.render(valListMngr.lists.length - 1))
          }
        } 
      }
    }
  }
  
  def removeList = Action (parseRequestAsMap) { implicit request => 
    val maybeName = get ("name") fromRequest
    val maybeIndexString = get ("index") fromRequest
    val maybeListsData = dataStore.getAllLists map (_.data)
    
    handleInputErrors(maybeName, maybeIndexString, maybeListsData) {
      (name, indexString, listsData) => {
        val listManager = ListManager(listsData)
        listManager.remove(name) match {
          case Left(error) => Ok(error.get)
          case Right(valListMngr) => dataStore.removeList(name, valListMngr.data) {
            println(valListMngr.lists)
            println()
            val index = Integer.parseInt(indexString)
            val listsLength = valListMngr.lists.length
            if(listsLength == 0) Ok(addListHtml(0))
            else {
              val requestedList = getRequestedList(index, listsData, (_ + 1))
              Ok(requestedList.render((index + 1) % (listsLength + 1)))
            }
          }
        }
      }
    }
  }
  
  def newData(f: Int => Int) = Action (parseRequestAsMap) { implicit request =>
    val maybeIndexString = get ("index") fromRequest
    val maybeListsData = session get ("\\lists\\")
    
    handleInputErrors(maybeIndexString, maybeListsData) {
      (indexString, listsData) => {
        val index = Integer.parseInt(indexString)
        val requestedList = getRequestedList(index, listsData, f)
        Ok(requestedList.render(f(index)))
      }
    }
  }
  
  def getRequestedList(index: Int, listsData: String, f: Int => Int)
    (implicit request: MappedRequest): ToDoList = {
      val listManager = ListManager(listsData)
      val lists = listManager.lists
      val adjusted = Math.abs(f(index) % lists.length)
      val listName = lists(adjusted)
      val listData = session.get(listName) getOrElse "Error Loading Data"
      ToDoList(listName, listData)
  }
  
  def nextData = newData(_ + 1) // next index
  def prevData = newData(_ - 1) // prev index
  
  def goToAddList = Action (parse.tolerantFormUrlEncoded) {implicit request => 
    Ok(AssetHandler.addListHtml(0))
  }
}