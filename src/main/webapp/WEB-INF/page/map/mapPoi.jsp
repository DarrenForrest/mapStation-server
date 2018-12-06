<%@ page contentType="text/html;charset=UTF-8" %>

<html>
<head>
	<title>基站管理</title>	
	<%@ include file="/common/meta.jsp" %>
    <%@ include file="/common/import.jsp" %>
    <%@ include file="/common/showDialog/showDialog_js.jsp" %>
    
<script type="text/javascript">

</script>    
<body>

<div title="" class="easyui-panel" iconCls="icon-redo" collapsible="true">
	<form action="${ pageContext.request.contextPath }/mapPoi/addMapStation.action">
		<table class="datagrid-body" width="100%" >
			<tr>
				<td class="datagrid-header">POI类型：</td>
				<td colspan="1"> 
					<input id="name" name="name" class="easyui-text" data-options="" value="" />
				</td>
				<td class="datagrid-header">半径 / TOPn:</td>
				<td colspan="1"> 
					<select id="timechouse"  name="state"  >
						    <option value="0" >&nbsp;&nbsp;&nbsp;半径</option>
							<option value="1" >&nbsp;&nbsp;&nbsp;TopN</option>
					</select>
				</td>
				<td class="datagrid-header">距离：</td>
				<td colspan="1"> 
					<input id="val" name="val" class="easyui-text" data-options="" value="" />
				</td>
			</tr>
	
			<tr style="height: 35px;">
				<td colspan="6" align="center">
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