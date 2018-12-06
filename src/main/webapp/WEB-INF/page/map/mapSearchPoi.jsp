<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.net.*" %>
<html>
<head>
	<title>百度地图POI检索</title>	
	<%@ include file="/common/meta.jsp" %>
    <%@ include file="/common/import.jsp" %>
    <%@ include file="/common/showDialog/showDialog_js.jsp" %>
    
<script type="text/javascript">
$(function(){
	$.ajax({
		url : "${ctx}/auth/getTreeData.action?catalog=allCityCode",
		type : "POST",
		data : { },
		dataType : "json",
		async : false,
		success : function(data) {
			$("#cityCode").empty();
			var html = "";
			$("#cityCode").append(html);
			if(data.length >0){
				if (typeof Array.prototype.forEach != 'function') {
					for (var i = 0; i < data.length; i++){
						$("#cityCode").append("<option value ='" + data[i].id + "'>" + data[i].text + "</option>");
				    }
				}else{
					data.forEach(function(value, index, array) {
						$("#cityCode").append("<option value ='" + value.id + "'>" + value.text + "</option>");
					});
				}
				getAreaCode();
			}
		}	
	});
	
	$.ajax({
		url : "${ctx}/auth/getTreeData.action?catalog=allFirstPoi",
		type : "POST",
		data : { },
		dataType : "json",
		async : false,
		success : function(data) {
			$("#firstPoi").empty();
			var html = "";
			$("#firstPoi").append(html);
			if(data.length >0){
				if (typeof Array.prototype.forEach != 'function') {
					for (var i = 0; i < data.length; i++){
						$("#firstPoi").append("<option value ='" + data[i].id + "'>" + data[i].text + "</option>");
				    }
				}else{
					data.forEach(function(value, index, array) {
						$("#firstPoi").append("<option value ='" + value.id + "'>" + value.text + "</option>");
					});
				}
				getSecondPoi();
			}
		}	
	});
	
	$("#cityCode").change(function(){
		getAreaCode();
	});
    $("#firstPoi").change(function(){
    	getSecondPoi();
	});
});

function getAreaCode(){
	$.ajax({
		url : "${ctx}/auth/getTreeData.action?catalog=getAreaCode&cityCode="+$("#cityCode").val(),
		type : "POST",
		data : {},
		dataType : "json",
		async : false,
		success : function(data) {
			$("#areaCode").empty();
			var html = "";
			$("#areaCode").append(html);
			$("#areaCode").append("<option value =''>---请选择区县---</option>");
			if(data.length >0){
				if (typeof Array.prototype.forEach != 'function') {
					for (var i = 0; i < data.length; i++){
						$("#areaCode").append("<option value ='" + data[i].id + "'>" + data[i].text + "</option>");
				    }
				}else{
					data.forEach(function(value, index, array) {
						$("#areaCode").append("<option value ='" + value.id + "'>" + value.text + "</option>");
					});
				}
			}
		}	
	});
}

function getSecondPoi(){
	var poi = $("#firstPoi").val();
	$.ajax({
		url : "${ctx}/auth/getTreeData.action?catalog=getSecondPoi&firstPoi="+poi,
		type : "POST",
		data : {},
		dataType : "json",
		async : false,
		success : function(data) {
			$("#secondPoi").empty();
			var html = "";
			$("#secondPoi").append(html);
			$("#secondPoi").append("<option value =''>---请选择二级POI---</option>");
			if(data.length >0){
				if (typeof Array.prototype.forEach != 'function') {
					for (var i = 0; i < data.length; i++){
						$("#secondPoi").append("<option value ='" + data[i].id + "'>" + data[i].text + "</option>");
				    }
				}else{
					data.forEach(function(value, index, array) {
						$("#secondPoi").append("<option value ='" + value.id + "'>" + value.text + "</option>");
					});
				}
			}
		}	
	});
}
</script>    
<body>

<div title="" class="easyui-panel" iconCls="icon-redo" collapsible="true">
	<form action="${ pageContext.request.contextPath }/mapPoi/searchPoiSave.action">
		<table class="datagrid-body" width="100%" >
			<tr>
				<td class="datagrid-header">地市：</td>
				<td> 
					<select id="cityCode"  name="cityCode" ></select>
				</td>
				<td class="datagrid-header">区县:</td>
				<td> 
					<select id="areaCode"  name="areaCode" ></select>
				</td>
			</tr>
			<tr>
				<td class="datagrid-header">一级POI：</td>
				<td> 
					<select id="firstPoi"  name="firstPoi" ></select>
				</td>
				<td class="datagrid-header">二级POI：</td>
				<td> 
					<select id="secondPoi"  name="secondPoi" ></select>
				</td>
			</tr>
			<tr>
				<td class="datagrid-header" title="百度地图POI检索，刷新则全量查询更新POI数据；否则只更新未检索的区域">是否刷新：</td>
				<td colspan="3"> 
					<select id="dealType"  name="dealType" >
						<option value="0">否</option>
						<option value="1">是</option>
					</select>
				</td>
			</tr>
	
			<tr style="height: 35px;">
				<td colspan="4" align="center">
					<input type="submit" iconCls="icon-add"   value="关联" > 
						&nbsp;&nbsp;&nbsp;
					<input  type="reset"  iconCls="icon-reload"  value="清空" >
				</td>
			</tr>
		</table>
	</form>
</div>

<%@include file="/common/showDialog/showDialog_middle.jsp"%>
<%@include file="/common/showDialog/showDialog_small.jsp"%>
<%@include file="/common/showDialog/showDialog_big.jsp"%>

</body>
</html>