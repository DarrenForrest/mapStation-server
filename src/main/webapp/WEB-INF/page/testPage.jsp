<%@ page contentType="text/html;charset=UTF-8" %>

<html>
<head>
	<title>测试页面</title>	
	<%@ include file="/common/meta.jsp" %>
    <%@ include file="/common/import.jsp" %>
    <%@ include file="/common/showDialog/showDialog_js.jsp" %>
    
<script type="text/javascript">
$.extend($.fn.datagrid.defaults.editors, {
	datebox : {
		init : function(container, options) {
			var input = $('<input type="text">').appendTo(container);
			input.datebox(options);
			return input;
		},
		destroy : function(target) {
			$(target).datebox('destroy');
		},
		getValue : function(target) {
			return $(target).datebox('getValue');//获得旧值
		},
		setValue : function(target, value) {
			$(target).datebox('setValue', (value == null || value == '' ? '' : new Date(value).format('yyyy-MM-dd')));//设置新值的日期格式
		},
		resize : function(target, width) {
			$(target).datebox('resize', width);
		}
	},
	datetimebox: {//datetimebox就是你要自定义editor的名称
		init: function(container, options){
			var editor = $('<input />').appendTo(container);
			editor.enableEdit = false;
			editor.datetimebox(options);
			return editor;
		},
		getValue: function(target){
			return $(target).datetimebox('getValue');
		},
		setValue: function(target, value){
			if(value)
			$(target).datetimebox('setValue',new Date(value).format("yyyy-MM-dd hh:mm:ss"));
			else
			$(target).datetimebox('setValue', null);
		},
		resize: function(target, width){
			$(target).datetimebox('resize',width);
		},
		destroy: function(target){
			$(target).datetimebox('destroy');
		}
	}
});
	
function formatDatebox111(value) {
	if(value == null || value == '') {
		return '';
	}
	var dt;
	if(value instanceof Date) {
		dt = value;
	} else {
		dt = new Date(value);
	}
	return dt.format("yyyy-MM-dd"); //扩展的Date的format方法(上述插件实现)
}
</script>
<script type="text/javascript">
	var listUrl = '${ctx}/auth/getStaffPage.action';
	
	$(function(){
		$('#orgId').combotree({
			url:'${ctx}/auth/getTreeData.action?catalog=orgAsync&type=${orgId}',
			valueFiled: 'id',
			textField: 'text',
			method:'get',
			onChange:function(n,o){$(this).val(n);},
		});
	});
	
	$(function(){
		$('#tableList').datagrid({
			title:'TEST',
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
			    {field:'STAFF_ID',			title:'员工编号',	align:'center', sortable:true, width:50},
				{field:'STAFF_NAME',		title:'员工姓名',	align:'center', sortable:true, width:50, editor:'text'},
				{field:'LOGIN_NAME',		title:'登录名',		align:'center', sortable:true, width:50},
				{field:'ORG_ID',			title:'机构',		align:'center', sortable:true, width:50,
					formatter: function(value, rowdata, index){ return rowdata.ORG_NAME; },
					editor:{
						type:'combotree',
						options:{
							valueField:'id',
							textField:'name',
							method:'get',
							url:'${ctx}/auth/getTreeData.action?catalog=orgAsync&type=1',
							required:true
						}
					}
				},
				{field:'STATION_ID',		title:'职务',		align:'center', sortable:true, width:50, editor:'numberbox'},
				{field:'PASSWORD_EXP_DATE',	title:'密码有效期',	align:'center', sortable:true, width:50, editor:'datebox', formatter:function(value, rowdata){if(value != null && value != '') return new Date(value).format('yyyy-MM-dd');}},
				{field:'STATE_NAME',		title:'状态',		align:'center', sortable:true, width:50},
				{field:'STATE_DATE',		title:'状态日期',	align:'center', sortable:true, width:50, formatter:function(value, rowdata){if(value != null) return new Date(value).format('yyyy-MM-dd hh:mm:ss');}},
				{field:'CREATE_DATE',		title:'创建日期',	align:'center', sortable:true, width:50, formatter:function(value, rowdata){if(value != null) return new Date(value).format('yyyy-MM-dd hh:mm:ss');}},
			]],
			onClickRow: onClickRow,
			onDblClickRow:function(row){
				//viewProject();
			},
			toolbar : [{
				text:'新增',
				iconCls:'icon-add',
				handler:function(){append()}
			},{
				text:'删除',
				iconCls:'icon-cut',
				handler:function(){remove()}
			},{
				text:'保存',
				iconCls:'icon-save',
				handler:function(){accept()}
			},{
				text:'撤销',
				iconCls:'icon-undo',
				handler:function(){reject()}
			}],
		});
	});
	
	function searchList(){
    	var queryParams = $('#tableList').datagrid('options').queryParams;	       
        queryParams.orgId = $('#orgId').val();
        queryParams.startDate  = $('#startDate').val();
        queryParams.endDate    = $('#endDate').val();        
        $('#tableList').datagrid("load");
   	}
	function clearSearch(){
		$('#orgId').val("");
		$('#shortName').val("");
		$('#custName').val("");
		searchList();
	}

	function saveInfo(rowdata) {
		
		$.ajax({
			url: '${ctx}/testSaveStaff.action',
			type: 'post',
			data: {list: JSON.stringify(rowdata)},
			dataType: 'json',
			//traditional: true,
			success: function(data){
				alert(data.message);
				if(data.code == 0) {
					$('#tableList').datagrid('acceptChanges');
				}
			},
			error: function(data){ alert("ajax error"); }
		});
	}
	function reload() {
		$('#tableList').datagrid('reload');
	}
</script>
	<script type="text/javascript">
		var editIndex = undefined;
		function endEditing(){
			if (editIndex == undefined){return true}
			if ($('#tableList').datagrid('validateRow', editIndex)){
				//var ed = $('#tableList').datagrid('getEditor', {index:editIndex,field:'STAFF_ID'});
				//var productname = $(ed.target).combobox('getText');
				//$('#tableList').datagrid('getRows')[editIndex]['productname'] = productname;
				
				
				//改写ORG_NAME
				var ed = $('#tableList').datagrid('getEditor', {index:editIndex, field:'ORG_ID'});
				var ORG_NAME = $(ed.target).combotree('getText');
				$('#tableList').datagrid('getRows')[editIndex]['ORG_NAME'] = ORG_NAME;
				
				
				
				
				$('#tableList').datagrid('endEdit', editIndex);
				editIndex = undefined;
				return true;
			} else {
				return false;
			}
		}
		function onClickRow(index){
			if (editIndex != index){
				if (endEditing()){
					$('#tableList').datagrid('selectRow', index)
							.datagrid('beginEdit', index);
					editIndex = index;
				} else {
					$('#tableList').datagrid('selectRow', editIndex);
				}
			}
		}
		function append(){
			if (endEditing()){
				$('#tableList').datagrid('appendRow',{status:'P'});
				editIndex = $('#tableList').datagrid('getRows').length-1;
				$('#tableList').datagrid('selectRow', editIndex)
						.datagrid('beginEdit', editIndex);
			}
		}
		function removeit(){
			if (editIndex == undefined){return}
			$('#tableList').datagrid('cancelEdit', editIndex)
					.datagrid('deleteRow', editIndex);
			editIndex = undefined;
		}
		function accept(){
			
			endEditing();
			
			var rows = $('#tableList').datagrid('getChanges', 'updated');
			
			saveInfo(rows);
			
		}
		function reject(){
			$('#tableList').datagrid('rejectChanges');
			editIndex = undefined;
		}
		function getChanges(){
			var rows = $('#tableList').datagrid('getChanges');
			alert(rows.length+' rows are changed!');
		}
	</script>
<body>

<div title="交接表明细查询" id="tab_search" class="easyui-panel" 
	 iconCls="icon-search"	collapsible="true" collapsed="false" title="查询搜索">
	<form id="formadd" name="formadd" method="post">
		<table class="datagrid-body" width="100%" >
			<tr>
				<td class="datagrid-header">营业厅</td>
				<td colspan="1"> 
					<input id="orgId" name="orgId" class="easyui-combotree" data-options="" value="${orgId}" />
				</td>
				<td class="datagrid-header" >营收日期</td>
				<td colspan="1">
					<input type="text" id="startDate" name="startDate" class="Wdate" value="${startDate}"
						   onclick="WdatePicker({isShowClear:true,readOnly:true,dateFmt:'yyyy-MM-dd'})"	 />
						 至
					<input type="text" id="endDate" name="endDate" class="Wdate" value="${endDate}"
						   onclick="WdatePicker({isShowClear:true,readOnly:true,dateFmt:'yyyy-MM-dd'})"	/>
				</td>
				<td><input class="easyui-datebox" value="1434770111000" /></td>
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