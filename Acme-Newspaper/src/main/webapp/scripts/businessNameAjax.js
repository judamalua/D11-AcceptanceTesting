function getBusinessNameAjax() {
	var xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			if (this.responseText != "null") {
				setCookieBusinessName(this.responseText.split(",")[0], this.responseText.split(",")[1]);
				getCookieBusinessName();
			}

		}
	};
	xhttp.open("GET", getMainDomain() + "configuration/getBusinessName.do", true);
	xhttp.send();

}

function setCookieBusinessName(firstName, secondName) {
	var d = new Date();
	d.setTime(d.getTime() + 30 * 60 * 1000);
	var res = {
		firstName : firstName,
		secondName : secondName
	};
	document.cookie = "businessName=" + JSON.stringify(res) + "; expires=" + d.toUTCString() + ";path=/";

};

function getCookieBusinessName() {
	var businessName = getCookie("businessName");
	var name = JSON.parse(businessName);
	document.getElementsByClassName("brand-logo")[0].innerHTML = '&#160;&#160;' + name.firstName + '&#160;<img width="24" src="images/people.png" />&#160;' + name.secondName;
};

function getBusinessName() {
	var businessName = getCookie("businessName");
	if (businessName != "" && businessName != null) {
		getCookieBusinessName();
	} else {
		getBusinessNameAjax();
	}
}
