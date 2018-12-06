<%@ page contentType="text/html;charset=UTF-8" %>
<script type="text/javascript">

var _obj = null;
var _cb = null;
var _listUrl = '${ctx}/auth/getStaffPage.action';

function initStaffSelection(obj, cb) {
	_obj = obj;
	_cb = cb;
}

$(function(){
	$('#staffSelectionTableList').datagrid({
		title: '',
		iconCls: 'icon-ok',
		url: _listUrl,
		nowrap: false,
		striped: true,
		collapsible: false,
		fitColumns: true,
		pagination: true,
		singleSelect: true,
		rownumbers: true,
		remoteSort: true,
		pageList: [10,15,30,50],
		idField: 'STAFF_ID',
		columns: [[
		    {field:'LOGIN_NAME',		title:'员工编号',	align:'center', sortable:true, width:50},
			{field:'STAFF_NAME',		title:'员工姓名',	align:'center', sortable:true, width:50},
			{field:'ORG_NAME',			title:'机构',		align:'center', sortable:true, width:50}
		]],
		onDblClickRow: function(row){
			staffSelectionOk();
		},
		toolbar: '#staffSelectionDlgToolBar'
	});
});

function staffSelectionSearch(){
	var queryParams = $('#staffSelectionTableList').datagrid('options').queryParams;	       
    queryParams.orgId      = $('#staffSelectionOrgId').val();
    queryParams.rangeType  = '3';
    queryParams.staffName  = $('#staffName').val();        
    $('#staffSelectionTableList').datagrid("load");
}

function staffSelectionOk() {
	
	var row = $('#staffSelectionTableList').datagrid('getSelected');
	if(row == null || row.length == 0) {
		alert('没有选择记录，请选择再点OK');
		return;
	}
	
	$('#staffSelectionTableList').datagrid('unselectRow', $('#staffSelectionTableList').datagrid('getRowIndex', row));
	
	$('#staffSelectionDlg').dialog('close');
	
	if(_cb) {
		_cb(_obj, row);
	}
	
	_obj = null;
	_cb = null;
}
</script>

<div id="staffSelectionDlg" class="easyui-dialog" title="员工选择" style="width: 800px; height: 450px; padding: 10px; top:00px;" closed=true
	data-options="iconCls:'icon-save',buttons:[{text:'OK',iconCls:'icon-ok',handler:function(){staffSelectionOk();}}]">
	
	
		<table id="staffSelectionTableList" stype="width:300px;"></table>
	
	
	
	<div id="staffSelectionDlgToolBar" style="padding:5px;height:auto">
		<div>
			营业厅: <input id="staffSelectionOrgId" name="staffSelectionOrgId" class="easyui-combotree" data-options="url:'${ctx}/auth/getTreeData.action?catalog=orgAsyncAll&type=1',method:'get',onChange:function(n,o){$(this).val(n);}" value="1" />
			营业员（姓名或编号）: <input id="staffName" name="staffName" class="easyui-text" data-options="" value="" />
			<a class="easyui-linkbutton" iconCls="icon-search" onclick="staffSelectionSearch()">Search</a>
		</div>
	</div>

</div>
