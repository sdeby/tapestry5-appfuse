var paramObject = {};
 
function 	initialize(params) {
		this.paramObject = params; 
		if (getCookie("username") != null) {
			$("j_username").value = getCookie("username");
			$("j_password").focus();
		} else {
				$("j_username").focus();
		}
}
	
function saveUsername(theForm) {
	    var expires = new Date();
	    expires.setTime(expires.getTime() + 24 * 30 * 60 * 60 * 1000); // sets it for approx 30 days.        
	    setCookie("username",$("j_username").value,expires, paramObject.url);
}
	
function validateForm(form) {                                                               
	    return validateRequired(form); 
} 
	
function passwordHint() {
		if ($("j_username").value.length == 0) {
		  alert(this.paramObject.requiredUsername);
		  $("j_username").focus();
		} else {
		    location.href=this.paramObject.passwordHintLink + "/" + $("j_username").value;      
		}
}

function required() {   
	    this.aa = new Array("j_username", paramObject.requiredUsername, new Function ("varName", " return this[varName];"));
	    this.ab = new Array("j_password", paramObject.requiredPassword, new Function ("varName", " return this[varName];"));
} 



