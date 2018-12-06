<%@ page contentType="text/html;charset=UTF-8" %>

<html>
<head>
	<title>枚举值参数管理</title>	
	<%@ include file="/common/meta.jsp" %>
    <%@ include file="/common/import.jsp" %>
    <%@ include file="/common/showDialog/showDialog_js.jsp" %>
    
<script type="text/javascript">
var listUrl = '${ctx}/cfg/getEnumCatalogPage.action';
var listUrlCfg = '${ctx}/cfg/getEnumCfgPage.action';
var demoUrl = '${ctx}/auth/getTreeData.action?catalog=enumCfg&type=';

$(function(){
	$('#tableList').datagrid({
		title:'类型管理',
		iconCls:'icon-ok',
		url:listUrl,
		queryParams: getQueryParams(),
		nowrap: false,
		striped: true,
		collapsible:false,
		fitColumns: true,
		pagination:true,
		singleSelect:true,
		rownumbers:false,
		remoteSort:true,
		pageSize:10,
		pageList:[10,15,30,50],
		idField:'ID',
		columns:[[
		    {field:'CATALOG_CODE',	title:'参数编码',	align:'left', sortable:true, width:70},
			{field:'CATALOG_NAME',	title:'参数名称',	align:'left', sortable:true, width:40},
			{field:'STATE_NAME',	title:'状态',		align:'center', sortable:true, width:30}
		]],
		onClickRow:function(row){
			var rowdata = $('#tableList').datagrid('getSelected');
			$('#catalogId').val( rowdata.ID );
			$('#catalogCode').val( rowdata.CATALOG_CODE );
			$('#catalogName').val( rowdata.CATALOG_NAME );
			$("#tableListEnumCfg").datagrid("getPanel").panel("setTitle","参数值管理  " + rowdata.CATALOG_NAME);
			$('#demo').combotree('loadData', []); //清空列表
			$('#demo').combotree({url: demoUrl + rowdata.CATALOG_CODE });			
			searchListCfg();
		},
		onDblClickRow:function(row){
			//viewProject();
		},
		toolbar:'#tb'
	});
	
	$('#tableListEnumCfg').datagrid({
		title:'参数值管理',
		iconCls:'icon-ok',
		url:listUrlCfg,
		nowrap: false,
		striped: true,
		collapsible:false,
		fitColumns: true,
		pagination:true,
		singleSelect:true,
		rownumbers:true,
		remoteSort:true,
		pageSize:10,
		pageList:[10,15,30,50],
		idField:'ID',
		columns:[[
		    {field:'ENUM_KEY',		title:'主键',	align:'left', sortable:true, width:40},
			{field:'ENUM_VALUE',	title:'值',	align:'left', sortable:true, width:40},
			{field:'PID',			title:'父键',	align:'left', sortable:true, width:20},
			{field:'ORDER_NUM',		title:'排序',		align:'center', sortable:true, width:20},
			{field:'STATE_NAME',	title:'状态',		align:'center', sortable:true, width:30}
		]],
		onDblClickRow:function(row){
			//viewProject();
		},
		toolbar : [{
			text:'新增',
			iconCls:'icon-add',
			handler:function(){editInfoCfg(-1);}
		},{
			text:'编辑',
			iconCls:'icon-edit',
			handler:function(){
				var rowdata = $('#tableListEnumCfg').datagrid('getSelected');
				if(rowdata == null)
					alert('请选择要修改的记录！')
				else
					editInfoCfg(rowdata.ID);
			}
		},{
			text:'删除',
			iconCls:'icon-cut',
			handler:function(){
				var rowdata = $('#tableListEnumCfg').datagrid('getSelected');
				if(rowdata == null)
					alert('请选择要删除的记录！')
				else
					delInfoCfg(rowdata.ID);
			}
		}]
	});
	
	var pager = $('#tableListEnumCfg').datagrid().datagrid('getPager');	// get the pager of datagrid
	pager.pagination({buttons:'#divComboTree' });	
});

function getQueryParams() {
	var queryParams = new Object();	       
    queryParams.q  = $('#q').val();
    return queryParams;
}

function searchList(){
	$('#tableList').datagrid('options').queryParams = getQueryParams();
    $('#tableList').datagrid("load");
}
function clearSearch(){
	$('#q').val("");
	searchList();
}

function reload() {
	$('#tableList').datagrid('reload');
	$('#tableListEnumCfg').datagrid('reload');
}

function getQueryParamsCfg() {	
	var queryParams = new Object();	       
    queryParams.catalogCode  = $('#catalogCode').val();
    return queryParams;
}
function searchListCfg(){
	$('#tableListEnumCfg').datagrid('options').queryParams = getQueryParamsCfg();
    $('#tableListEnumCfg').datagrid("load");
}

function editInfo(id) {
	var url = 'toEnumCatalogEdit.action';
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
		url: '${ctx}/cfg/delEnumCatalog.action',
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

function editInfoCfg(id) {
	var url = 'toEnumCfgEdit.action';
	if(url != null) {
		url += ('?id=' + id);
	}
	
	var catalogId= $('#catalogId').val();
	if(catalogId == ''){
		alert('请选择左侧参数类型');
		return;
	}
	url = url + "&catalogId=" + catalogId;
	
	var catalogName = $('#catalogName').val();
	
	showDialog(catalogName, url, 'middle', true);
}

function delInfoCfg(id) {
	if(!confirm('确定要删除选择的记录吗？')) {
		return;
	}
	$.ajax({
		url: '${ctx}/cfg/delEnumCfg.action',
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

function btn_add(){
	editInfo(-1);
}

function btn_edit(){
	var rowdata = $('#tableList').datagrid('getSelected');
	if(rowdata == null)
		alert('请选择要修改的记录！')
	else
		editInfo(rowdata.ID);
}

function btn_del(){
	var rowdata = $('#tableList').datagrid('getSelected');
	if(rowdata == null)
		alert('请选择要删除的记录！')
	else
		delInfo(rowdata.ID);
}



</script>
<body>
<input type="hidden" id="catalogId" name="catalogId" >
<input type="hidden" id="catalogCode" name="catalogCode" >
<input type="hidden" id="catalogName" name="catalogName" >

<div id="divComboTree">
		效果预览<input id="demo" name="demo" value=""
				class="easyui-combotree" data-options="url:'',method:'get',onChange:function(n,o){$(this).val(n);}" />
</div>

<div class="easyui-panel" title="枚举值参数管理" style="width:100%;height:450;padding:2px;">
	<div class="easyui-layout" data-options="fit:true">
		<div data-options="region:'west',split:true" style="width:500px;padding:1px">
			<table id="tableList"></table>
			<div id="tb" style="padding:0px">  
		        <div style="margin-bottom:0px">  
		             <a href="#" onclick="btn_add();" 	class="easyui-linkbutton" iconCls="icon-add" plain="true">新增</a>  
		             <a href="#" onclick="btn_edit();" 	class="easyui-linkbutton" iconCls="icon-edit" plain="true">编辑</a>  
		             <a href="#" onclick="btn_del();" 	class="easyui-linkbutton" iconCls="icon-remove" plain="true">删除</a>
		             |
		             <input id="q" name="q" class="easyui-searchbox" data-options="prompt:'',searcher:searchList" ></input>  
		        </div>  
		    </div>
		</div>

		<div data-options="region:'center'" style="padding:1px">
			<table id="tableListEnumCfg"></table>
		</div>
	</div>
</div>


<%@include file="/common/showDialog/showDialog_middle.jsp"%>
<%@include file="/common/showDialog/showDialog_small.jsp"%>
<%@include file="/common/showDialog/showDialog_big.jsp"%>
</body>
</html>