<%@ page contentType="text/html;charset=UTF-8" %>

<html>
<head>
	<title>员工所辖机构管理</title>	
	<%@ include file="/common/meta.jsp" %>
    <%@ include file="/common/import.jsp" %>
    <%@ include file="/common/showDialog/showDialog_js.jsp" %>
        
<script type="text/javascript">
	var listUrl = '${ctx}/auth/getStaffOrgPage.action';
	
	$(function(){
		$('#tableList').datagrid({
			title:'员工所辖机构管理',
			iconCls:'icon-ok',
			url:listUrl,
			queryParams: getQueryParams(),
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
			    {field:'LOGIN_NAME',		title:'员工编号',	align:'left', sortable:true, width:50},
				{field:'STAFF_NAME',		title:'员工姓名',	align:'left', sortable:true, width:50},
				{field:'TYPE_NAME',			title:'类型',		align:'left', sortable:true, width:50},
				{field:'ORG_LIST',			title:'机构',		align:'left', sortable:true, width:250}
			]],
			onDblClickRow:function(row){
				//viewProject();
			},
			toolbar : [{
				text:'新增',
				iconCls:'icon-add',
				handler:function(){editInfo(null);}
			},{
				text:'编辑',
				iconCls:'icon-edit',
				handler:function(){
					var rowdata = $('#tableList').datagrid('getSelected');
					if(rowdata == null)
						alert('请选择要修改的记录！')
					else
						editInfo(rowdata.STAFF_ID, rowdata.TYPE);
				}
			}]
		});
	});
	
	function getQueryParams() {
		var queryParams = new Object();	       
        queryParams.staffName  = $('#staffName').val();
        return queryParams;
	}
	function searchList(){
		$('#tableList').datagrid('options').queryParams = getQueryParams();
        $('#tableList').datagrid("load");
   	}
	function clearSearch(){
		$('#staffName').val("");
		searchList();
	}

	function editInfo(id,type) {
		var url = 'toStaffOrgEdit.action';
		if(id != null) {
			url += ('?staffId=' + id + "&type=" + type);
		}
		showDialog('INFO (加载营业厅机构比较费时，请耐心等待...)', url, 'moremiddle', true);
	}
	function reload() {
		$('#tableList').datagrid('reload');
	}
	
</script>
</head>
<body>

<div title="员工查询" id="tab_search" class="easyui-panel" 
	 iconCls="icon-search"	collapsible="true" collapsed="false" title="查询搜索">
	<form id="formadd" name="formadd" method="post">
		<table class="datagrid-body" width="100%" >
			<tr>
				<td class="datagrid-header">营业员（姓名或编号）</td>
				<td colspan="5"> 
					<input id="staffName" name="staffName" class="easyui-text" data-options="" value="" />
				</td>
			</tr>
	
			<tr style="height: 35px;">
				<td colspan="6" align="center">
					<a class="easyui-linkbutton" iconCls="icon-search"	onclick="searchList()">查 询</a> 
						&nbsp;&nbsp;&nbsp;
					<a class="easyui-linkbutton" iconCls="icon-reload"	onclick="getChecked();">取值</a>
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