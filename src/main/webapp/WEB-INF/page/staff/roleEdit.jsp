<%@ page contentType="text/html;charset=UTF-8" %>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>角色编辑</title>
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
	$('#rules').val(s);
}

function save() {
	
	if(!$('#editForm').form('validate'))
		return;
	
	processTreeSelected();
	
	$.ajax({
		url: '${ctx}/auth/saveRole.action',
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

</script>
</head>
<body>
	<div class="easyui-panel">
		<form id="editForm" method="post">
		<table class="datagrid-body" width="100%" cellspacing="1" cellpadding="0" align="center">
			<tr>
				<th width="120px;" align="right" class="datagrid-header">
					角色编号: <span style="color: red;">&nbsp</span>
				</th>
				<td>
					<input id="roleId" name="roleId" value="${role.roleId}" readonly="readonly"
							class="easyui-validatebox" data-options="" />
				</td>
			</tr>
			<tr>
				<th width="120px;" align="right" class="datagrid-header">
					角色名称: <span style="color: red;">*</span>
				</th>
				<td>
					<input id="roleName" name="roleName" value="${role.roleName}"
							class="easyui-validatebox" data-options="required:true,novalidate:true" />
				</td>
			</tr>
		</table>
		
		<div class="easyui-panel" style="padding:5px">
			<div>规则选择</div>
			<div>
				<input id="rules" name="rules" style="display:none" value="" />
				<ul id="tt" class="easyui-tree" data-options="url:'${ctx}/auth/getRuleByRoleId.action?roleId=${role.roleId}',method:'get',animate:true,cascadeCheck:false,checkbox:true"></ul>
			</div>
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
