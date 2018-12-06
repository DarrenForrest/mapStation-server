<%@ page contentType="text/html;charset=UTF-8" %>

<html>
<head>
	<title>授权查询</title>	
	<%@ include file="/common/meta.jsp" %>
    <%@ include file="/common/import.jsp" %>
    <%@ include file="/common/showDialog/showDialog_js.jsp" %>
    
<script type="text/javascript">
	var listUrl = '${ctx}/auth/getGrantQueryPage.action';
	
	$(function(){
		$('#tableList').datagrid({
			title:'授权查询',
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
			idField:'STAFF_ID',
			columns:[[
			    {field:'LOGIN_NAME',	title:'员工编号',	align:'center', sortable:true, width:50},
				{field:'STAFF_NAME',	title:'员工姓名',	align:'center', sortable:true, width:50},
				{field:'ROLE_NAME',		title:'角色名称',	align:'center', sortable:true, width:50},
				{field:'ORG_RANGE',		title:'管理范围',	align:'center', sortable:true, width:150},
			]],
			onDblClickRow:function(row){
				//viewProject();
			}
		});
		
		searchList();
	});
	
	function searchList(){
    	var queryParams = $('#tableList').datagrid('options').queryParams;	       
        queryParams.orgId      = $('#orgId').val();
        queryParams.rangeType  = $('#rangeType').val();
        queryParams.staffName  = $('#staffName').val();       
        queryParams.roleId     = $('#roleId').val();    
        $('#tableList').datagrid("load");
   	}
	function clearSearch(){
		$('#orgId').combotree('setValue', '${orgId}');
		$('#roleId').combotree('setValue', '');
		$('#rangeType').combotree('setValue', '3');
		$('#staffName').val("");		
		searchList();
	}
	
	function reload() {
		$('#tableList').datagrid('reload');
	}
</script>    
<body>

<div title="授权查询" id="tab_search" class="easyui-panel" 
	 iconCls="icon-search"	collapsible="true" collapsed="false" title="查询搜索">
	<form id="formadd" name="formadd" method="post">
		<table class="datagrid-body" width="100%" >
			<tr>
				<td class="datagrid-header">营业厅</td>
				<td colspan="1"> 
					<input id="orgId" name="orgId" class="easyui-combotree" data-options="url:'${ctx}/auth/getTreeData.action?catalog=orgAsync&type=<%=optionOrgId%>',method:'get',onChange:function(n,o){$(this).val(n);}" value="${orgId}" />
				</td>
				<td class="datagrid-header">数据范围</td>
				<td colspan="1"> 
					<input id="rangeType" name="rangeType" class="easyui-combotree" data-options="url:'${ctx}/auth/getTreeData.action?catalog=enumCfg&type=tb_audit_object.range_type',method:'get',onChange:function(n,o){$(this).val(n);}" value="3" />
				</td>
				<td class="datagrid-header">角色</td>
				<td colspan="1"> 
					<input id="roleId" name="roleId" class="easyui-combotree" data-options="url:'${ctx}/auth/getTreeData.action?catalog=role',method:'get',onChange:function(n,o){$(this).val(n);}" value="" />
				</td>
				<td class="datagrid-header">营业员（姓名或编号）</td>
				<td colspan="1"> 
					<input id="staffName" name="staffName" class="easyui-text" data-options="" value="" />
				</td>
			</tr>
	
			<tr style="height: 35px;">
				<td colspan="8" align="center">
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