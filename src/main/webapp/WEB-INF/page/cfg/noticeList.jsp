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
	var listUrl2 = '${ctx}/cfg/messageListData.action';
	
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
			title:'系统公告',
			iconCls:'icon-ok',
			url: listUrl,
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
				//viewProject();
			}
		});
		
		$('#tableList2').datagrid({
			title:'待办事宜',
			iconCls:'icon-ok',
			url: listUrl2,
			nowrap: false,
			striped: true,
			collapsible:false,				
			fitColumns: true,
			pagination:true,
			singleSelect:true,
			rownumbers:true,
			remoteSort:true,
			pageList:[10,15,30,50],
			pageSize:10,
			idField:'id',
			columns:[[
				{field:'description'		,title:'分类'	,align : 'center', sortable:true, width:50},
				{field:'content'			,title:'内容'		,align : 'left', sortable:true, width:350,
					formatter:function(value,rowdata){
						var txt  = '<a href="javascript:void(0);" onclick="showMessage(\''+rowdata.description+'\',\''+rowdata.url+'\');">' + rowdata.content  + '</a>';
						return txt;
					}	
				}
			]],
			onDblClickRow:function(row){
				//viewProject();
			}
		});
		
		reloadFresh();
	});
	
	function searchList(){
		$('#tableList').datagrid('reload');
		$('#tableList2').datagrid('reload');
   	}
	
	function reload() {
		$('#tableList').datagrid('reload');
		$('#tableList2').datagrid('reload');
	}
	
	function viewInfo(title, id) {
		var url = '${ctx}/cfg/noticeView.action?id=' + id ;
		showDialog(title, url, 'big', true);
	}
	
	function showMessage(title,url){
		parent.addTab2(title, url);
	}
	
	//增加autoReload标识防止服务停止后重复提醒加载数据出错
	var autoReload = false;
	$.fn.datagrid.defaults.onLoadError = function() {
		if(autoReload) {
			//alert('加载数据出错，请刷新页面重试！');
			autoReload = false;
		}
	};
	
	function reloadFresh(){
		if(autoReload) {
			setTimeout(function(){reload(); reloadFresh();}, 1000 * 60);
		}
	}

</script>
    
<body>


<br />
<div title=""  id="showList" class="easyui-panel" iconCls="icon-redo"
	collapsible="true" >
	<table id="tableList" style="margin 0 1px;"></table>
</div>
<br/><br/>
<div title=""  id="showList1" class="easyui-panel" iconCls="icon-redo"
	collapsible="true" >
	<table id="tableList2"></table>
</div>

<%@include file="/common/showDialog/showDialog_middle.jsp"%>
<%@include file="/common/showDialog/showDialog_small.jsp"%>
<%@include file="/common/showDialog/showDialog_big.jsp"%>

</body>
</html>