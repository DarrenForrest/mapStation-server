<%@ page contentType="text/html;charset=UTF-8" %>

<html>
<head>
	<title>自动任务调度</title>	
	<%@ include file="/common/meta.jsp" %>
    <%@ include file="/common/import.jsp" %>
    <%@ include file="/common/showDialog/showDialog_js.jsp" %>
    
<script type="text/javascript">
	var listUrl = '${ctx}/task/getJobDefinePage.action';
	
	$(function(){
		$('#tableList').datagrid({
			title:'自动任务列表',
			iconCls:'icon-ok',
			url:listUrl,
			nowrap: false,
			striped: true,
			collapsible:false,
			fitColumns: true,
			pagination:true,
			singleSelect:true,
			rownumbers:true,
			remoteSort:true,
			pageList:[10,15,30,50],
			idField:'ID',
			columns:[[
			    {field:'ID',				title:'编号',		align:'center', sortable:true, width:20},
				{field:'JOB_NAME',			title:'任务名称',	align:'left', sortable:true, width:50},
				{field:'JOB_CLASS',			title:'JAVA类名',	align:'left', sortable:true, width:130},
				{field:'STORED_PROCEDURE',	title:'存储过程',	align:'left', sortable:true, width:30},
				{field:'CRON_EXPRESSION',	title:'定时器配置',	align:'left', sortable:true, width:40},
				{field:'JOB_DESC',			title:'描述',		align:'center', sortable:true, width:140},
				{field:'STATUS_NAME',		title:'状态',		align:'center', sortable:true, width:30},
				{field:'TRIGGER_STATE',		title:'运行状态',	align:'center', sortable:true, width:30},
				{field:'XX',				title:'操作',		align:'center', sortable:true, width:60,
					formatter: function(value, rowdata){
						return '<a href="javascript:void(0)" onclick="doJob(' + rowdata.ID + ');">手工执行</a>'
						       + '&nbsp|&nbsp' +
						       '<a href="javascript:void(0)" onclick="showLog(' + rowdata.ID + ');">查看日志</a>';
					}
				},
			]],
			onDblClickRow:function(row){
				//viewProject();
			},
			toolbar : [{
				text:'编辑',
				iconCls:'icon-edit',
				handler:function(){
					var rowdata = $('#tableList').datagrid('getSelected');
					if(rowdata == null)
						alert('请选择要修改的记录！')
					else
						editInfo(rowdata.ID);
				}
			}]
		});
		
		reloadFresh();
	});
	
	function searchList(){
    	var queryParams = $('#tableList').datagrid('options').queryParams;	       
    	queryParams.id = $('#id').val();
        queryParams.jobName = $('#jobName').val();
        $('#tableList').datagrid("load");
   	}
	function clearSearch(){
		$('#id').val("");
		$('#jobName').val("");
		searchList();
	}

	function editInfo(id) {
		var url = 'toJobDefineEdit.action';
		if(url != null) {
			url += ('?id=' + id);
		}
		showDialog('INFO', url, 'middle', true);
	}
	
	function showLog(jobId) {
		var url = 'toJobLogManage.action?jobId=' + jobId;
		showDialog('INFO', url, 'big', true);
	}
	
	function doJob(jobId) {
		
		if(!confirm('确定要立即执行编号为' + jobId + '的任务吗？')) {
			return;
		}
		$.ajax({
			url: '${ctx}/task/doJob.action',
			type: 'post',
			data: {jobId:jobId},
			dataType: 'json',
			success: function(data) {
				alert(data.message);
				if(data.code == 0) {
					reload();
				}
			},
			error: function(data) { alert('ajax error'); }
		});
	}
	
	function reload() {
		$('#tableList').datagrid('reload');
	}
	
	function startScheduler() {
		$.ajax({
			url: '${ctx}/task/startAutoTask.action',
			type: 'post',
			data: {},
			dataType: 'json',
			success: function(data){
				if(data.code != 0) alert(data.message);
				getSchedulerState();
				reload();
			},
			error: function(data){ alert("ajax error"); }
		});
	}
	
	function stopScheduler() {
		$.ajax({
			url: '${ctx}/task/stopAutoTask.action',
			type: 'post',
			data: {},
			dataType: 'json',
			success: function(data){
				if(data.code != 0) alert(data.message);
				getSchedulerState();
				reload();
			},
			error: function(data){ alert("ajax error"); }
		});
	}
	
	function getSchedulerState() {
		$.ajax({
			url: '${ctx}/task/getSchedulerState.action',
			type: 'post',
			data: {},
			dataType: 'json',
			success: function(data){
				//alert(data.message);
				$('#schedulerState').val(data.schedulerStateName);
				if(data.schedulerState == 1) {
					$('#schedulerState').css('color', 'green');
				} else {
					$('#schedulerState').css('color', 'red');
				}
			},
			error: function(data){  }
		});
	}
	
	//增加autoReload标识防止服务停止后重复提醒加载数据出错
	var autoReload = true;
	$.fn.datagrid.defaults.onLoadError = function() {
		if(autoReload) {
			//alert('加载数据出错，请刷新页面重试！');
			//autoReload = false;
		}
	};
	function reloadFresh(){
		getSchedulerState();
		reload();
		if(autoReload) {
			//setTimeout(function(){reloadFresh();}, 1000 * 5);
		}
	}
</script>    
<body>

<div title="自动任务查询" id="tab_search" class="easyui-panel" 
	 iconCls="icon-search"	collapsible="true" collapsed="false" title="查询搜索">
	<form id="formadd" name="formadd" method="post">
		<table class="datagrid-body" width="100%" >
			<tr>
				<td class="datagrid-header">任务编号</td>
				<td colspan="1"> 
					<input id="id" name="id" class="easyui-text" data-options="" value="" />
				</td>
				<td class="datagrid-header">任务名称</td>
				<td colspan="1"> 
					<input id="jobName" name="jobName" class="easyui-text" data-options="" value="" />
				</td>
				<td class="datagrid-header">定时器状态</td>
				<td colspan="1"> 
					<input id="schedulerState" name="schedulerState" class="easyui-text" data-options="" readonly value="未知" />
				</td>
			</tr>
	
			<tr style="height: 35px;">
				<td colspan="6" align="center">
					<a class="easyui-linkbutton" iconCls="icon-search"	onclick="searchList()">查 询</a> 
						&nbsp;&nbsp;&nbsp;
					<a class="easyui-linkbutton" iconCls="icon-reload"	onclick="clearSearch();">清 空</a>
						&nbsp;&nbsp;&nbsp;
					<a class="easyui-linkbutton" iconCls=""	onclick="startScheduler();">启动定时器</a>
						&nbsp;&nbsp;&nbsp;
					<a class="easyui-linkbutton" iconCls=""	onclick="stopScheduler();">停止定时器</a>
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