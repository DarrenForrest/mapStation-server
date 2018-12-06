<%@ page contentType="text/html;charset=UTF-8" %>

<html>
<head>
	<title>权限管理</title>	
	<%@ include file="/common/meta.jsp" %>
    <%@ include file="/common/import.jsp" %>
    <%@ include file="/common/showDialog/showDialog_js.jsp" %>
    
<script type="text/javascript">
	var listUrl = '${ctx}/auth/getRulePage.action';
	
	$(function(){
		$('#tableList').datagrid({
			title:'权限管理',
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
			idField:'RULE_ID',
			columns:[[
			    {field:'RULE_ID',		title:'规则编号',	align:'center', sortable:true, width:50},
				{field:'RULE_NAME',		title:'规则名称',	align:'center', sortable:true, width:50},
				{field:'URL',			title:'访问路径',	align:'center', sortable:true, width:50},
				{field:'ICON',			title:'图标',		align:'center', sortable:true, width:50},
				{field:'ORDER_NUM',		title:'序号',		align:'center', sortable:true, width:50},
				{field:'VISIBLE_NAME',	title:'可见性',		align:'center', sortable:true, width:50},
				{field:'NODE_TYPE_NAME',title:'节点类型',	align:'center', sortable:true, width:50},
				{field:'NODE_CODE',		title:'节点编码',	align:'center', sortable:true, width:50},
				{field:'PARENT_ID',		title:'上级编号',	align:'center', sortable:true, width:50},
				{field:'STATE_NAME',	title:'状态',		align:'center', sortable:true, width:50},
				{field:'STATE_DATE',	title:'状态日期',	align:'center', sortable:true, width:50, formatter:function(value, rowdata){return new Date(value).format('yyyy-MM-dd hh:mm:ss');}},
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
						delInfo(rowdata.RULE_ID);
				}
			},{
				text:'编辑',
				iconCls:'icon-edit',
				handler:function(){
					var rowdata = $('#tableList').datagrid('getSelected');
					if(rowdata == null)
						alert('请选择要修改的记录！')
					else
						editInfo(rowdata.RULE_ID);
				}
			}]
		});
	});
	
	function searchList(){
    	var queryParams = $('#tableList').datagrid('options').queryParams;	       
    	queryParams.ruleId = $('#ruleId').val();
        queryParams.ruleName = $('#ruleName').val();
        $('#tableList').datagrid("load");
   	}
	function clearSearch(){
		$('#ruleId').val("");
		$('#ruleName').val("");
		searchList();
	}

	function editInfo(id) {
		var url = 'toRuleEdit.action';
		if(url != null) {
			url += ('?ruleId=' + id);
		}
		showDialog('INFO', url, 'middle', true);
	}
	function delInfo(id) {
		if(!confirm('确定要删除选择的记录吗？')) {
			return;
		}
		$.ajax({
			url: '${ctx}/auth/delRule.action',
			type: 'post',
			data: {ruleId:id},
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

<div title="权限查询" id="tab_search" class="easyui-panel" 
	 iconCls="icon-search"	collapsible="true" collapsed="false" title="查询搜索">
	<form id="formadd" name="formadd" method="post">
		<table class="datagrid-body" width="100%" >
			<tr>
				<td class="datagrid-header">规则编号</td>
				<td colspan="1"> 
					<input id="ruleId" name="ruleId" class="easyui-text" data-options="" value="" />
				</td>
				<td class="datagrid-header">规则名称</td>
				<td colspan="1"> 
					<input id="ruleName" name="ruleName" class="easyui-text" data-options="" value="" />
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