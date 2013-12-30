var animateStart = function() {
	animateUpAndOff("#welcome", function() {});
	$(".list-pane").css("display", "block");
	$(".list-pane").animate({
	  top: "0%"
	}, 800, function () {
	});
	$("body").css("overflow", "visible");
};

var animateRotate = function(offSelector, on, onSelector) {
	animateOff(offSelector, function () {
		addOffScreen(on, onSelector);
		animateOn(onSelector);
	});
};

var animateOff = function(selector, onComplete) {
	$(selector).animate({
	  marginLeft: "-100%",
	  marginRight: "100%"
	}, 400, function () {
      $(selector).remove();
	  onComplete();
	})
};

var addOffScreen = function(nodeString, selectorForNode) {
	$(".list-pane").append(nodeString);
	$(selectorForNode).css("margin-left", "200%");
	$(selectorForNode).css("margin-right", "-200%");
};

var animateOn = function(selector) {
	$( 'body' ).css("overflow", "hidden") 
	$(selector).animate({
		marginLeft: "0%",
		marginRight: "0%"
	}, 400, function () {
		$( 'body' ).css("overflow", "visible")
	});
};

var animateUpAndOff = function(selector, onComplete) {
	$(selector).animate({
		marginTop: "-100%"
	}, 600, function () {
		$(selector).remove();
		onComplete();
	});
}

var addGlyphicon = function (reference) {
	if($( reference ).children(".glyphicon-remove").height() === 0) {
	  $( reference ).children(".glyphicon-remove").animate({
	    height: "20px"
	  });
	} else {
	  $( reference ).children(".glyphicon-remove").animate({
	    height: "0px"
	  });
	}
};

var shrink  = function (selector) {
	$(selector).css("overflow", "hidden");
	$(selector).animate({
		height: "0px"
	}, 400, function () {
		$(selector).remove();
	});
};