// Updated using Prototype
var LoginJS = Class.create({ 
	initialize: function (params) {
		this.paramObject = params; 
		if (getCookie("username") != null) {
			$("j_username").value = getCookie("username");
			$("j_password").focus();
		} else {
				$("j_username").focus();
		}
	},
	saveUsername: function(theForm) {
	    var expires = new Date();
	    expires.setTime(expires.getTime() + 24 * 30 * 60 * 60 * 1000); // sets it for approx 30 days.        
	    setCookie("username",theForm.j_username.value,expires, this.paramObject.url);
	},
	validateForm: function (form) {                                                               
	    return validateRequired(form); 
	}, 
	passwordHint: function () {
		if ($("j_username").value.length == 0) {
		  alert(this.paramObject.requiredUsername);
		  $("j_username").focus();
		} else {
		    location.href=this.paramObject.passwordHintLink + "/" + $("j_username").value;      
		}
	},
	required: function () {   
	    this.aa = new Array("j_username", this.paramObject.requiredUsername, new Function ("varName", " return this[varName];"));
	    this.ab = new Array("j_password", this.paramObjectrequiredPassword, new Function ("varName", " return this[varName];"));
	} 

});

