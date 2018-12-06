function appendParam(url, params) {
	var has = url.indexOf("?") === -1;
	
	for (var i in params) {
		if (has) {
			url += "?"+i+"="+params[i];
			has = false;
		} else {
			url += "&"+i+"="+params[i];
		}
	}
	
	return url;
}