<%@ page contentType="text/html;charset=UTF-8" %>

<html>
<head>
	<title>查看任务日志</title>	
	<%@ include file="/common/meta.jsp" %>
    <%@ include file="/common/import.jsp" %>
    <%@ include file="/common/showDialog/showDialog_js.jsp" %>
    
<script type="text/javascript">
	var listUrl = '${ctx}/task/getJobLogPage.action';
	
	$(function(){
		$('#tableList').datagrid({
			title:'任务日志列表',
			iconCls:'icon-ok',
			url:'',
			nowrap: false,
			striped: true,
			collapsible:false,
			fitColumns: true,
			pagination:true,
			singleSelect:true,
			rownumbers:true,
			remoteSort:true,
			pageList:[10,15,30,50],
			idField:'EXE_FLOW_ID',
			columns:[[
			    {field:'EXE_FLOW_ID',		title:'流水号',		align:'center', sortable:true, width:30},
			    {field:'JOB_ID',			title:'任务编号',	align:'center', sortable:true, width:35},
				{field:'JOB_NAME_MAIN',		title:'任务名称',	align:'center', sortable:true, width:50},
				{field:'START_DATE',		title:'开始时间',	align:'center', sortable:true, width:80, formatter:function(value){if(value) return new Date(value).format('yyyy-MM-dd hh:mm:ss');}},
				{field:'END_DATE',			title:'结束时间',	align:'center', sortable:true, width:80, formatter:function(value){if(value) return new Date(value).format('yyyy-MM-dd hh:mm:ss');}},
				{field:'STATE_NAME',		title:'状态',		align:'center', sortable:true, width:50},
				{field:'RET_MSG',			title:'返回信息',	align:'center', sortable:true, width:100},
				{field:'TRIGGER_TYPE_NAME',	title:'触发方式',	align:'center', sortable:true, width:50},
			]],
			onDblClickRow:function(row){
				//viewProject();
			}
		});
		
		searchList();
	});
	
	function searchList(){
    	//var queryParams = $('#tableList').datagrid('options').queryParams;	       
    	//queryParams.jobId = $('#jobId').val();
        //queryParams.jobName = $('#jobName').val();
        //$('#tableList').datagrid("load");
        var params = ('?jobId=' + $('#jobId').val());
        params += ('&jobName=' + $('#jobName').val());
        $('#tableList').datagrid({url: listUrl + params});
   	}
	function clearSearch(){
		$('#jobId').val("");
		$('#jobName').val("");
		searchList();
	}

	function reload() {
		$('#tableList').datagrid('reload');
	}
</script>    
<body>

<div title="任务日志查询" id="tab_search" class="easyui-panel" 
	 iconCls="icon-search"	collapsible="true" collapsed="false" title="查询搜索">
	<form id="formadd" name="formadd" method="post">
		<table class="datagrid-body" width="100%" >
			<tr>
				<td class="datagrid-header">任务编号</td>
				<td colspan="1"> 
					<input id="jobId" name="jobId" class="easyui-text" data-options="" value="${jobId}" />
				</td>
				<td class="datagrid-header">任务名称</td>
				<td colspan="1"> 
					<input id="jobName" name="jobName" class="easyui-text" data-options="" value="" />
				</td>
			</tr>
	
			<tr style="height: 35px;">
				<td colspan="6" align="center">
					<a class="easyui-linkbutton" iconCls="icon-search"	onclick="searchList()">查 询</a> 
						&nbsp;&nbsp;&nbsp;
					<a class="easyui-linkbutton" iconCls="icon-reload"	onclick="clearSearch();">清 空</a>
				</td>
			</tr>
		</table>
	</form>
</div>

<br/>
<div title="" class="easyui-panel" iconCls="icon-redo" collapsible="true">
	<table id="tableList"></table>
</div>

<%@include file="/common/showDialog/showDialog_middle.jsp"%>
<%@include file="/common/showDialog/showDialog_small.jsp"%>
<%@include file="/common/showDialog/showDialog_big.jsp"%>

</body>
</html>