<%@ page contentType="text/html;charset=UTF-8" %>

<html>
<head>
	<title>文件管理</title>	
	<%@ include file="/common/meta.jsp" %>
    <%@ include file="/common/import.jsp" %>
    <%@ include file="/common/showDialog/showDialog_js.jsp" %>
    
<script type="text/javascript">
	var listUrl = '${ctx}/auth/getFilePage.action';
	
	$(function(){
		$('#tableList').datagrid({
			title:'文件管理',
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
			    {field:'ID',				title:'文件编号',	align:'center', sortable:true, width:50},
				{field:'FILE_NAME',			title:'文件名称',	align:'center', sortable:true, width:50},
				{field:'FILE_PATH',			title:'文件路径',	align:'center', sortable:true, width:50},
				{field:'FILE_TYPE_ID_NAME',	title:'文件类型',	align:'center', sortable:true, width:50},
				{field:'REMARK',			title:'备注',		align:'center', sortable:true, width:50},
				{field:'STAFF_NAME',		title:'修改工号',	align:'center', sortable:true, width:50},
				{field:'STATE_NAME',		title:'状态',		align:'center', sortable:true, width:50},
				{field:'STATE_DATE',		title:'状态日期',	align:'center', sortable:true, width:50, formatter:function(value, rowdata){return new Date(value).format('yyyy-MM-dd hh:mm:ss');}},
				{field:'CREATE_DATE',		title:'创建日期',	align:'center', sortable:true, width:50, formatter:function(value, rowdata){return new Date(value).format('yyyy-MM-dd hh:mm:ss');}},
				{field:'op',				title:'操作'	,			align:'center', sortable:true, width:50,
					formatter:function(value,rowdata){
						if(rowdata.STATE == 0 || rowdata.STATE == 2) {
							return '<a href="javascript:void(0)" onclick="editInfo(' + rowdata.ID + ');">导入实收</a>';
						} else {
							return '<a href="javascript:void(0)" onclick="">查看详情</a>';
						}
					}
				},
			]],
			onDblClickRow:function(row){
				//viewProject();
			},
			toolbar : [{
				text:'上传新文件',
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
			}]
		});
	});
	
	function searchList(){
    	var queryParams = $('#tableList').datagrid('options').queryParams;	       
        queryParams.settleName = $('#settleName').val();
        queryParams.startDate  = $('#startDate').val();
        queryParams.endDate    = $('#endDate').val();        
        $('#tableList').datagrid("load");
   	}
	function clearSearch(){
		$('#settleName').val("");
		$('#shortName').val("");
		$('#custName').val("");
		searchList();
	}

	function editInfo(id) {
		var url = 'toFileEdit.action';
		if(url != null) {
			url += ('?id=' + id);
		}
		showDialog('INFO', url, 'big', true);
	}
	function delInfo(id) {
		if(!confirm('确定要删除选择的记录吗？')) {
			return;
		}
		$.ajax({
			url: '${ctx}/auth/delFile.action',
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

<div title="文件查询" id="tab_search" class="easyui-panel" 
	 iconCls="icon-search"	collapsible="true" collapsed="false" title="查询搜索">
	<form id="formadd" name="formadd" method="post">
		<table class="datagrid-body" width="100%" >
			<tr>
				<td class="datagrid-header">营业厅</td>
				<td colspan="1"> 
					<input id="orgId" name="orgId" class="easyui-combotree" data-options="url:'${ctx}/auth/getTreeData.action?catalog=orgAsync&type=<%=optionOrgId%>',method:'get',onChange:function(n,o){$(this).val(n);}" value="" />
				</td>
				<td class="datagrid-header" >营收日期</td>
				<td colspan="1">
					<input type="text" id="startDate" name="startDate" class="Wdate" value="${startDate}"
						   onclick="WdatePicker({isShowClear:true,readOnly:true,dateFmt:'yyyy-MM-dd'})"	 />
						 至
					<input type="text" id="endDate" name="endDate" class="Wdate" value="${endDate}"
						   onclick="WdatePicker({isShowClear:true,readOnly:true,dateFmt:'yyyy-MM-dd'})"	/>
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