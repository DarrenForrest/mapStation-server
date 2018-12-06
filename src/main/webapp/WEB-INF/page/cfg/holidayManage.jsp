<%@ page contentType="text/html;charset=UTF-8" %>

<html>
<head>
	<title>节假日设置</title>	
	<%@ include file="/common/meta.jsp" %>
    <%@ include file="/common/import.jsp" %>
    <%@ include file="/common/showDialog/showDialog_js.jsp" %>
    
<script type="text/javascript">
	
	var listUrl = '${ctx}/cfg/getHolidayPage.action';
	
	$(function(){
		$('#tableList').datagrid({
			title:'节假日设置',
			iconCls:'icon-ok',
			url:listUrl,
			nowrap: false,
			striped: true,
			collapsible:false,
			fitColumns:false,
			pagination:true,
			singleSelect:true,
			rownumbers:true,
			remoteSort:true,
			pageList:[10,15,30,50],
			idField:'ID',
			columns:[[
			    {field:'ID',				title:'节假日编号',	align:'center', sortable:true, width:150, hidden:true},
				{field:'HOLIDAY_TYPE_NAME',	title:'类型名称',	align:'center', sortable:true, width:150},
				{field:'HOLIDAY',			title:'日期',		align:'center', sortable:true, width:150, formatter:function(value, rowdata){if(value != null) return new Date(value).format('yyyy-MM-dd');}},
			]],
			onDblClickRow:function(row){
				var rowdata = $('#tableList').datagrid('getSelected');
				if(rowdata == null)
					alert('请选择要修改的记录！')
				else
					editInfo(rowdata.ID);
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
    	queryParams.holidayType = $('#holidayType').val();
    	queryParams.beginDate = $('#beginDate').val();
    	queryParams.endDate = $('#endDate').val();
        $('#tableList').datagrid("load");
   	}
	function clearSearch(){
		$('#holidayType').combotree('setValue', -1);
		$('#beginDate').val("");
		$('#endDate').val("");
		searchList();
	}

	function editInfo(id) {
		var url = 'toHolidayEdit.action';
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
			url: '${ctx}/cfg/delHoliday.action',
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

<div title="节假日查询" id="tab_search" class="easyui-panel" 
	 iconCls="icon-search"	collapsible="true" collapsed="false" title="查询搜索">
	<form id="formadd" name="formadd" method="post">
		<table class="datagrid-body" width="100%" >
			<tr>
				<td class="datagrid-header">节假日类型</td>
				<td colspan="1"> 
					<input id="holidayType" name="holidayType" class="easyui-combotree" data-options="url:'${ctx}/auth/getTreeData.action?catalog=enumCfg&type=tb_holiday.holiday_type',method:'get',onChange:function(n,o){$(this).val(n);}" value="-1" />
				</td>
				<td class="datagrid-header" >日期</td>
				<td colspan="1">
					<input type="text" id="beginDate" name="beginDate" class="Wdate" 
						   onclick="WdatePicker({isShowClear:true,readOnly:true,dateFmt:'yyyy-MM-dd'})"	 />
						 至
					<input type="text" id="endDate" name="endDate" class="Wdate" 
						   onclick="WdatePicker({isShowClear:true,readOnly:true,dateFmt:'yyyy-MM-dd'})"	/>
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