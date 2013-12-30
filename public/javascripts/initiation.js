$( document ).ready(function () {
	
  $('#add-input').keyup( function (e) {
	  if(e.which == 13) {
		postToDoItem($('#add-input').val(), $('.active').attr("list-name"));
		$('#add-input').val("");
	  }
  });
  
  $("#addListButton").click ( function ()
    { addList($('#list-name').val()); }
  );
  
  $('#delete-list').click( function ()
	{ deleteList($('.active').attr("list-name"), $('.active').attr("list-num")); }	  
  );
  
  $('.item').click( function () 
	{ addGlyphicon( this ); } 
  );
  
  $('#start').click( function () 
	{  animateStart(); }	  
  );
  
  $('.glyphicon-remove').click( function ()
	{  removeToDoItem( $(this).parent().attr("text"), $('.active').attr("list-name") ); }	  
  );
  
  $('#list-form').submit( function (event) {
	  event.preventDefault();
  });
  
  $('#go-to-add-list').click( function ()
    { goToAddList(); }	  
  );
});

$(document).bind('DOMSubtreeModified', function () {
	$('.item').unbind().click( function () 
			{ addGlyphicon( this ); } 
    );
	$('#list-form').unbind().submit( function (event) {
		  event.preventDefault();
	});
	$('#add-input').unbind().keyup( function (e) {
		  if(e.which == 13) {
			postToDoItem($('#add-input').val(), $('.active').attr("list-name"));
			$('#add-input').val("");
		  }
	});
	$('.glyphicon-remove').unbind().click( function ()
		{  removeToDoItem( $(this).parent().attr("text"), $('.active').attr("list-name") ); }	  
	);
	
	$("#addListButton").unbind().click ( function ()
	  { addList($('#list-name').val()); }
	);
	
	$('#go-to-add-list').unbind().click( function ()
	    { goToAddList($('.active').attr("list-num")); }	  
	);
	
	$('#delete-list').unbind().click( function ()
			{ deleteList($('.active').attr("list-name"), $('.active').attr("list-num")); }	  
	);
	
	$( document ).keyup(function (e) {
	  if(e.which == 39) {
	    nextData($(".active").attr("list-num"));  
	  } 
	});
});