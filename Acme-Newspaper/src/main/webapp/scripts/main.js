function initialize() {
	checkCookie();
	getBusinessName();
	widgInit();
}

// true->exportar a zip
// false-> exportar a war
function getMainDomain() {
	var development = false;
	var domainName = "Acme-Newspaper";
	if (development == true) {
		return "/" + domainName + "/";
	} else {
		return "/";
	}
}
function changeLanguage(language) {
	if (language === 1) {
		document.cookie = "language=es;path=/";
	}
	if (language === 0) {
		document.cookie = "language=en;path=/";
	}
	location.reload();
}
