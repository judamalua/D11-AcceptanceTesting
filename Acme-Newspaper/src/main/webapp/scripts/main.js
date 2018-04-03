function initialize() {
	checkCookie();
	getBusinessName();
}

function getMainDomain() {
	var development = true;
	var domainName = "Acme-Newspaper";
	if (development == true) {
		return "/" + domainName + "/";
	} else {
		return "/";
	}
}
