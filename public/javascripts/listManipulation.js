var postToDoItem = function(item, list) {
	$.post("/addItem", { newItem : item, listName: list })
	  .done(function( returnedItem ) {
		  $('.to-do-list').append(returnedItem);
		  $("[text='" + item + "']").height(0);
		  $("[text='" + item + "']").animate({
			  height: "45px"
		  }, 400);
	  });
};

var removeToDoItem = function(item, list) {
	$.post("/removeItem", { toDelete : item, listName : list })
	  .done(function( text ) {
		  shrink("[text='" + text + "']");
	  });
};

var addList = function(listName) {
	$.post("/addList", { name : listName })
	  .done(function (list) {
		 animateRotate("#add-list-window", list, ".active");
		 $( document ).keyup(function (e) {
		   if(e.which == 39) {
			  nextData($(".active").attr("list-num"));  
		   } 
		 });	
	  });	
};

var deleteList = function (listName, indexString) {
	$.post("/removeList", {name : listName, index : indexString})
	  .done(function (list) {
		 animateUpAndOff(".active", function () { 
		   addOffScreen(list, ".active");
		   animateOn(".active");
		 });
	  });
}

var nextData = function (currIndex) {
	$.post("/next", { index : currIndex })
	.done( function (nextList) {
	  var jqueryList = $(nextList)
	  animateRotate("[list-num=" + currIndex + "]",
				    nextList,
					"[list-name='" + jqueryList.attr("list-name") + "']");
	});
};

var goToAddList = function (currIndex) {
   $.post("/goToAddList")
   .done( function (menu) {
	  animateRotate("[list-num=" + currIndex + "]",
			  		menu,
			  		"#add-list-window"); 
   });
};