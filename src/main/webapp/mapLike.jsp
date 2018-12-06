<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
	<style type="text/css">
		body, html,#allmap {width: 100%;height: 100%; margin:0;font-family:"微软雅黑";}
		#l-map{height:300px;width:100%;}
		#r-result{width:100%; font-size: 14px; line-height: 20px;}
	</style>
	<script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=jWK9pF6AOfNER0SbZStYjyO0"></script>
<title>单个地点场景基站映射</title>
<link rel="stylesheet" href="http://cdn.static.runoob.com/libs/bootstrap/3.3.7/css/bootstrap.min.css">
	<script src="http://cdn.static.runoob.com/libs/jquery/2.1.1/jquery.min.js"></script>
	<script src="http://cdn.static.runoob.com/libs/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath }/getMapPoi.js"></script>
</head>
<body onload="loaded();" >
<form class="bs-example bs-example-form" role="form" action="${ pageContext.request.contextPath }/mapPoi/addPoiData.do" method="post" >
        <div class="input-group">
<span class="input-group-addon">地&nbsp;&nbsp;点</span>
<input type="text" class="form-control" placeholder="地点" name="mapName" id="mapName" style="width: 200px;" >
<span class="input-group-addon">场景类型:</span>
<input type="text" class="form-control" placeholder="一级类型" name="firstType" id="firstType" style="width: 200px;" >
<span class="input-group-addon">二级行业类型:</span>
<input type="text" class="form-control" placeholder="二级类型" name="secondType" id="secondType" style="width: 200px;" >
<span class="input-group-addon">地区选择:</span>
<input type="text" class="form-control" placeholder="区域" name="cityCode" id="cityCode" style="width: 200px;" >
 </div>
 <button type="submit"  id="sub"  class="btn btn-success">提交</button>
<div id="l-map" style="float: left;" ></div>
		<input type="hidden" name="mapList" id="mapList"  value="" >	
		<div id="r-result"  style="float: left;"></div>
		 
    </form>
</body>
<script type="text/javascript">
function loaded(){
	$.ajax({
		url:'${pageContext.request.contextPatj}/mapPoi/getResource.do',
		type:'post',
		success: function(data){
			$("#firstType").val(data.firstType);
			$("#secondType").val(data.secondType);
			$("#mapName").val(data.mapName);
			$("#cityCode").val(data.cityCode);
			getPoiinfo();
		}
	})
}

function getPoiinfo(){
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

</script>
</html>