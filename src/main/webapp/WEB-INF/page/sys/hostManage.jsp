<%@ page contentType="text/html;charset=UTF-8" %>

<html>
<head>
	<title>主机管理</title>	
	<%@ include file="/common/meta.jsp" %>
    <%@ include file="/common/import.jsp" %>
    <%@ include file="/common/showDialog/showDialog_js.jsp" %>
    
<script type="text/javascript">
	var listUrl = '${ctx}/hostManage/getHostManagePage.action';
	
	$(function(){
		$('#tableList').datagrid({
			title:'主机管理',
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
			    {field:'ID',		title:'主机编号',	align:'center', sortable:true, width:50},
				{field:'IP',		title:'主机IP',	align:'center', sortable:true, width:50},
				{field:'NAME',	title:'主机名称',		align:'center', sortable:true, width:50},
				{field:'USER_NAME',	title:'用户名',		align:'center', sortable:true, width:50},
				{field:'PASS_WORD',	title:'密码',		align:'center', sortable:true, width:50},
				{field:'BACKUP_HOST',	title:'是否备份主机',		align:'center', sortable:true, width:50,formatter:function(value,rowdata){ if(value == 0){return "否"};if(value ==1){return "是"}}},	
				{field:'CREATE_DATE',	title:'创建日期',	align:'center', sortable:true, width:50, formatter:function(value, rowdata){return new Date(value).format('yyyy-MM-dd hh:mm:ss');}},
			]],
			onDblClickRow:function(row){
				//viewProject();
			},
			toolbar : [{
				text:'新增',
				iconCls:'icon-add',
				handler:function(){editInfo(null);}
			},{
				text:'删除',
				iconCls:'icon-cut',
				handler:function(){
					var rowdata = $('#tableList').datagrid('getSelected');
					if(rowdata == null)
						alert('请选择要删除的记录！')
					else
						delInfo(rowdata.ID);
				}
			},{
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
	});
	
	function searchList(){
    	var queryParams = $('#tableList').datagrid('options').queryParams;	       
        queryParams.id = $('#id').val();
        queryParams.ip = $('#ip').val();
        $('#tableList').datagrid("load");
   	}
	function clearSearch(){
		$('#id').val("");
		$('#ip').val("");
		searchList();
	}

	function editInfo(id) {
		var url = 'toHostManageEdit.action';
		if(url != null) {
			url += ('?id=' + id);
		}
		showDialog('INFO', url, 'middle', true);
	}
	function delInfo(id) {
		if(!confirm('确定要删除选择的记录吗？')) {
			return;
		}
		$.ajax({
			url: '${ctx}/hostManage/delHostManage.action',
			type: 'post',
			data: {id:id},
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
</script>    
<body>

<div title="主机查询" id="tab_search" class="easyui-panel" 
	 iconCls="icon-search"	collapsible="true" collapsed="false" title="查询搜索">
	<form id="formadd" name="formadd" method="post">
		<table class="datagrid-body" width="100%" >
			<tr>
				<td class="datagrid-header">主机编号</td>
				<td colspan="1"> 
					<input id="id" name="id" class="easyui-text" data-options="" value="" />
				</td>
				<td class="datagrid-header">主机IP</td>
				<td colspan="1"> 
					<input id="ip" name="ip" class="easyui-text" data-options="" value="" />
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