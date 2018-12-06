function oc(){
	var s =[];
	var mapName =document.getElementById("mapName").value;
	var cityCode=document.getElementById("cityCode").value;
	var map = new BMap.Map("l-map");
	if(cityCode=="140100"){
		map.centerAndZoom(new BMap.Point(112.550864,37.890277),13);
	}else if(cityCode=="140200"){
		map.centerAndZoom(new BMap.Point(113.290509,40.113744),12);
	}
	else if(cityCode=="140300"){
		map.centerAndZoom(new BMap.Point(113.569238,37.869529),13);
	}
	else if(cityCode=="140400"){
		map.centerAndZoom(new BMap.Point(113.120292,36.201664),13);
	}
	else if(cityCode=="140500"){
		map.centerAndZoom(new BMap.Point(112.867333,35.499834),13);
	}
	else if(cityCode=="140600"){
		map.centerAndZoom(new BMap.Point(112.479928,39.337672),13);
	}
	else if(cityCode=="140700"){
		map.centerAndZoom(new BMap.Point(112.738514,37.693362),13);
	}else if(cityCode=="140800"){
		map.centerAndZoom(new BMap.Point(111.006854,35.038859),13);
	}else if(cityCode=="140900"){
		map.centerAndZoom(new BMap.Point(112.727939,38.461031),13);
	}else if(cityCode=="141000"){
		map.centerAndZoom(new BMap.Point(111.538788,36.099745),13);
	}else if(cityCode=="141100"){
		map.centerAndZoom(new BMap.Point(111.143157,37.527316),13);
	}
	map.enableScrollWheelZoom(true); 
	var options = {
		onSearchComplete: function(results){
			 if (results.getPageIndex() <= results.getNumPages() - 1) { 
				 local.gotoPage(results.getPageIndex() + 1); 
			// 判断状态是否正确
			if (local.getStatus() == BMAP_STATUS_SUCCESS){		
				for (var i = 0; i < results.getCurrentNumPois(); i ++){
					var marker = new BMap.Marker(results.getPoi(i).point);  // 创建标注
					map.addOverlay(marker);               // 将标注添加到地图中
					s.push(results.getPoi(i).title + ", " + results.getPoi(i).address+","+results.getPoi(i).point.lng+","+results.getPoi(i).point.lat+","+cityCode);
					document.getElementById("r-result").innerHTML = s.join("<br/>");
					document.getElementById("mapList").value = document.getElementById("r-result").innerHTML;
				}
			}
		}
		}
	};
	var local = new BMap.LocalSearch(map, options);
	local.search(mapName);
}