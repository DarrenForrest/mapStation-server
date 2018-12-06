<%@ page contentType="text/html;charset=UTF-8" %>

<html>
<head>
	<title>系统公告</title>	
	<%@ include file="/common/meta.jsp" %>
    <%@ include file="/common/import.jsp" %>
    <%@ include file="/common/showDialog/showDialog_js.jsp" %>
    
<script type="text/javascript">
	function getParamUrl()
	{
		
		return "";
	}
	
	
	var listUrl = '${ctx}/cfg/noticeListData.action';
	
	function formatDate(value,rec)
	 {
		if(value == null)
			return value;
		else
		   //return value;
		   return $.myTime.UnixToDate(value/1000, true, 8);
	 }
	
	$(function(){
		$('#tableList').datagrid({
			title:'公告管理',
			iconCls:'icon-ok',
			url: listUrl + getParamUrl() ,
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
				{field:'ENUM_VALUE'		,title:'分类'	,align : 'center', sortable:true, width:50},
				{field:'TITLE'			,title:'标题'		,align : 'left', sortable:true, width:350,
					formatter:function(value,rowdata){
						var txt  = '<a href="javascript:void(0);" onclick="viewInfo(\'' + rowdata.TITLE + '\',' + rowdata.ID + ')">' + rowdata.TITLE  + '</a>';
						
						if(rowdata.TOP_FLAG == 1)
						{
							txt ='<img src="${ctx}/images/regulator/top.png" />' + txt ;	
						}
						
						return txt;
					}
				},
				
				{field:'STAFF_NAME'		,title:'发布人'	,align : 'center', sortable:true, width:50},
				{field:'CREATE_DATE'	,title:'发布日期'	,align : 'center', sortable:true, width:100, formatter: formatDate}
				
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
			},
			{
				text:'编辑',
				iconCls:'icon-edit',
				handler:function(){
					var rowdata = $('#tableList').datagrid('getSelected');
					if(rowdata == null)
						alert('请选择要修改的记录！')
					else
						editInfo(rowdata.ID);
				}
			},
			{
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
		$('#tableList').datagrid('options').url = listUrl;
		
		var queryParams = $('#tableList').datagrid('options').queryParams;	       
        queryParams.orgId      = $('#orgId').val();
        queryParams.rangeType  = $('#rangeType').val();
        queryParams.beginDate  = $('#beginDate').val();
        queryParams.endDate  = $('#endDate').val();
        $('#tableList').datagrid("load");
   	}
	
	function reload() {
		$('#tableList').datagrid('reload');
	}
	
	function viewInfo(title, id) {
		var url = '${ctx}/cfg/noticeView.action?id=' + id ;
		showDialog(title, url, 'middle', true);
	}
	
	function editInfo(id) {
		var url = 'noticeEdit.action';
		if(url != null) {
			url += ('?id=' + id);
		}
				
		showDialog('公告管理', url, 'middle', true);
	}
	function delInfo(id) {
		if(!confirm('确定要删除选择的记录吗？')) {
			return;
		}
		$.ajax({
			url: '${ctx}/cfg/delNotice.action',
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

</script>
    
<body>


<br />
<div title="" id="showList" class="easyui-panel" iconCls="icon-redo"
	collapsible="true">
	<table id="tableList"></table>
</div>

<%@include file="/common/showDialog/showDialog_middle.jsp"%>
<%@include file="/common/showDialog/showDialog_small.jsp"%>
<%@include file="/common/showDialog/showDialog_big.jsp"%>

</body>
</html>