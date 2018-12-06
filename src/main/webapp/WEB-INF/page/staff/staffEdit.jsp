<%@ page contentType="text/html;charset=UTF-8" %>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>员工信息编辑</title>
	<%@ include file="/common/meta.jsp" %>
	<%@ include file="/common/import.jsp" %>
<script type="text/javascript">

function processTreeSelected() {
	var nodes = $('#tt').tree('getChecked');
	var s = '';
	for(var i=0; i<nodes.length; i++){
		if (s != '') s += ',';
		s += nodes[i].id;
	}
	$('#roles').val(s);
}

function processDataGrid() {
	endEditing();
	var rows = $('#tableList').datagrid('getRows');
	$('#roleList').val(JSON.stringify(rows));
}

function save() {
	
	if(!$('#editForm').form('validate'))
		return;
	
	//processTreeSelected();
	
	processDataGrid();
	
	$.ajax({
		url: '${ctx}/auth/saveStaff.action',
		type: 'post',
		data: $('#editForm').serialize(),
		dataType: 'json',
		success: function(data){
			if(data.code == 0) {
				parent.closeDialog();
			}
			alert(data.message);
		},
		error: function(data){ alert("ajax error"); }
	});
}

function itemSelect(i) {
	//在onCheck方法中调用select事件
	$('#tt').tree('select', i.target);
	
}

$(function(){
	
	var listUrl = '${ctx}/auth/getStaffRolePage.action?staffId=${staff.staffId}';
	
	$('#tableList').datagrid({
		title:'员工角色配置',
		iconCls:'icon-ok',
		url:listUrl,
		nowrap: false,
		striped: true,
		collapsible:false,
		fitColumns: true,
		pagination:true,
		singleSelect:true,
		rownumbers:true,
		remoteSort:false,
		pageList:[50],
		pageSize:50,
		idField:'ID',
		columns:[[
		    {field:'ROLE_ID',		title:'角色编号',	align:'center', sortable:false, width:50,
		    	formatter: function(value, rowdata, index){ return rowdata.ROLE_NAME; },
		    	editor:{
					type:'combotree',
					options:{
						valueField:'id',
						textField:'name',
						method:'get',
						url:'${ctx}/auth/getTreeData.action?catalog=role',
						required:false
					}
				}
		    },
			{field:'ORG_ID',		title:'机构',		align:'center', sortable:false, width:50,
				formatter: function(value, rowdata, index){ return rowdata.ORG_NAME; },
				editor:{
					type:'combotree',
					options:{
						valueField:'id',
						textField:'name',
						method:'get',
						url:'${ctx}/auth/getTreeData.action?catalog=orgAsync&type=${orgId}',
						required:false
					}
				}
			},
			{field:'RANGE_TYPE',		title:'数据范围',	align:'center', sortable:false, width:50,
				formatter: function(value, rowdata, index){ return rowdata.RANGE_TYPE_NAME; },
				editor:{
					type:'combotree',
					options:{
						valueField:'id',
						textField:'name',
						method:'get',
						url:'${ctx}/auth/getTreeData.action?catalog=enumCfg&type=tb_audit_object.range_type',
						required:false
					}
				}
			}
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
			text:'撤销',
			iconCls:'icon-undo',
			handler:function(){reject()}
		}]
	});
});

</script>

	<script type="text/javascript">
		var editIndex = undefined;
		function endEditing(){
			if (editIndex == undefined){return true}
			if ($('#tableList').datagrid('validateRow', editIndex)){
				
				//改写ORG_NAME
				var ed = $('#tableList').datagrid('getEditor', {index:editIndex, field:'ORG_ID'});
				if(ed == null) {
					$('#tableList').datagrid('endEdit', editIndex);
					editIndex = undefined;
					return true;
				}
				var ORG_NAME = $(ed.target).combotree('getText');
				$('#tableList').datagrid('getRows')[editIndex]['ORG_NAME'] = ORG_NAME;
				
				//改写ROLE_NAME
				var ed = $('#tableList').datagrid('getEditor', {index:editIndex, field:'ROLE_ID'});
				if(ed == null) {
					$('#tableList').datagrid('endEdit', editIndex);
					editIndex = undefined;
					return true;
				}
				var ROLE_NAME = $(ed.target).combotree('getText');
				$('#tableList').datagrid('getRows')[editIndex]['ROLE_NAME'] = ROLE_NAME;
				
				//改写RANGE_TYPE_NAME
				var ed = $('#tableList').datagrid('getEditor', {index:editIndex, field:'RANGE_TYPE'});
				if(ed == null) {
					$('#tableList').datagrid('endEdit', editIndex);
					editIndex = undefined;
					return true;
				}
				var RANGE_TYPE_NAME = $(ed.target).combotree('getText');
				$('#tableList').datagrid('getRows')[editIndex]['RANGE_TYPE_NAME'] = RANGE_TYPE_NAME;
				
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
		function remove(){
			if (editIndex == undefined){return}
			$('#tableList').datagrid('cancelEdit', editIndex)
					.datagrid('deleteRow', editIndex);
			editIndex = undefined;
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
</head>
<body>
	<div class="easyui-panel">
		<form id="editForm" method="post">
		<table class="datagrid-body" width="100%" cellspacing="1" cellpadding="0" align="center">
			<tr style="display:none">
				<th width="120px;" align="right" class="datagrid-header">
					员工ID: <span style="color: red;">&nbsp</span>
				</th>
				<td>
					<input id="staffId" name="staffId" value="${staff.staffId}" readonly="readonly"
							class="easyui-validatebox" data-options="" />
				</td>
			</tr>
			<tr>
				<th width="120px;" align="right" class="datagrid-header">
					员工编号: <span style="color: red;">*</span>
				</th>
				<td>
					<input id="loginName" name="loginName" value="${staff.loginName}" readonly="readonly"
							class="easyui-validatebox" data-options="required:true,novalidate:true" />
				</td>
			</tr>
			<tr>
				<th width="120px;" align="right" class="datagrid-header">
					员工姓名: <span style="color: red;">*</span>
				</th>
				<td>
					<input id="staffName" name="staffName" value="${staff.staffName}"
							class="easyui-validatebox" data-options="required:true,novalidate:true" />
				</td>
			</tr>
			<tr>
				<th width="120px;" align="right" class="datagrid-header">
					密码: <span style="color: red;"></span>
				</th>
				<td>
					<input id="password" name="password" value=""
							class="easyui-validatebox" data-options="required:false,novalidate:true" />
				</td>
			</tr>
			<tr>
				<th width="120px;" align="right" class="datagrid-header">
					机构: <span style="color: red;">*</span>
				</th>
				<td>
					<input id="orgId" name="orgId" value="${staff.orgId}"
							class="easyui-combotree" data-options="url:'${ctx}/auth/getTreeData.action?catalog=orgAsync&type=${staff.orgId}',method:'get',onChange:function(n,o){$(this).val(n);},required:true" />
				</td>
			</tr>
		</table>
		
		<div style="display:none;">
			<div class="easyui-panel" title="角色选择" style="height:280px;padding:10px;">
				<input id="roles" name="roles" style="display:none" value=""/>
				<ul id="tt" class="easyui-tree" data-options="url:'${ctx}/auth/getRoleByStaffId.action?staffId=${staff.staffId}',method:'get',animate:true,cascadeCheck:false,checkbox:true,onCheck:itemSelect"></ul>
			</div>
		</div>
		<div>
			<input id="roleList" name="roleList" style="display:none" value=""/>
			<table id="tableList"></table>
		</div>
		
		</form>
		<center>
			<a href="#" class="easyui-linkbutton" iconCls="icon-save"
				onClick="return save();">提交</a> <a href="#"
				class="easyui-linkbutton" iconCls="icon-back" id="btn-back"
				onclick="parent.closeDialog();">返回</a>
		</center>
		
	</div>
</body>
</html>
