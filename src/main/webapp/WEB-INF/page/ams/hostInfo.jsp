<%@ page contentType="text/html;charset=UTF-8" %>

<html>
<head>
	<title>主机管理</title>	
	<%@ include file="/common/meta.jsp" %>
    <%@ include file="/common/import.jsp" %>
    <%@ include file="/common/showDialog/showDialog_js.jsp" %>
    
<script type="text/javascript">
	var listUrl = '${ctx}/hostInfo/getHostInfoPage.action';
	
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
			idField:'id',
			columns:[[
			    {field:'id',		title:'主机编号',	align:'center', sortable:true, width:50},
				{field:'ip',		title:'主机IP',	align:'center', sortable:true, width:50},
				{field:'projectName',	title:'主机',		align:'center', sortable:true, width:50},
				{field:'address',	title:'地址',		align:'center', sortable:true, width:50},
				{field:'fileName',	title:'文件名',		align:'center', sortable:true, width:50},
				{field:'fileType',	title:'文件类型',		align:'center', sortable:true, width:50},	
				{field:'time',	title:'定时器',		align:'center', sortable:true, width:50},
				{field:'createTime',	title:'创建日期',	align:'center', sortable:true, width:50, formatter:function(value, rowdata){return new Date(value).format('yyyy-MM-dd hh:mm:ss');}},
			]],
			onDblClickRow:function(row){
				//viewProject();
			},
			toolbar : [{
				text:'删除',
				iconCls:'icon-cut',
				handler:function(){
					var rowdata = $('#tableList').datagrid('getSelected');
					if(rowdata == null)
						alert('请选择要删除的记录！')
					else
						delInfo(rowdata.id);
				}
			},{
				text:'恢复',
				iconCls:'icon-undo',
				handler:function(){
					var rowdata = $('#tableList').datagrid('getSelected');
					if(rowdata == null)
						alert('请选择要恢复的记录！')
					else
						recBackFile(rowdata.id);
				}
			}
			]
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
		var url = 'toHostInfoEdit.action';
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
			url: '${ctx}/hostInfo/delHostInfo.action',
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
	//
	function recBackFile(id) {
		if(!confirm('确定要恢复选择的记录吗？')) {
			return;
		}
		$.ajax({
			url: '${ctx}/hostInfo/recoverFile.action',
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
				<td class="datagrid-header">POI类型：</td>
				<td colspan="1"> 
					<input id="tag" name="tag" class="easyui-text" data-options="" value="" />
				</td>
				<td class="datagrid-header">主机</td>
				<td colspan="1"> 
					<input id="ip" name="ip" class="easyui-text" data-options="" value="" />
				</td>
				<td class="datagrid-header">主机</td>
				<td colspan="1"> 
					<input id="ip" name="ip" class="easyui-text" data-options="" value="" />
				</td>
			</tr>
	
			<tr style="height: 35px;">
				<td colspan="6" align="center">
					<input type="submit" class="easyui-linkbutton" iconCls="icon-search" value="关联" >
						&nbsp;&nbsp;&nbsp;
					<input type="reset" class="easyui-linkbutton" iconCls="icon-reload"  value="清空" >
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