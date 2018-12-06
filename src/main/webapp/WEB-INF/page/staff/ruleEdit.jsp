<%@ page contentType="text/html;charset=UTF-8" %>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>规则信息编辑</title>
	<%@ include file="/common/meta.jsp" %>
	<%@ include file="/common/import.jsp" %>
<script type="text/javascript">

function save() {
	
	if(!$('#editForm').form('validate'))
		return;
	
	$.ajax({
		url: '${ctx}/auth/saveRule.action',
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
					规则编号: <span style="color: red;">&nbsp</span>
				</th>
				<td>
					<input id="ruleId" name="ruleId" value="${rule.ruleId}" readonly="readonly"
							class="easyui-validatebox" data-options="" />
				</td>
			</tr>
			<tr>
				<th width="120px;" align="right" class="datagrid-header">
					上级编号: <span style="color: red;">&nbsp</span>
				</th>
				<td>
					<input id="parentId" name="parentId" value="${rule.parentId == null ? 0 : rule.parentId}"
							class="easyui-combotree" data-options="panelWidth:300,panelHeight:600,url:'${ctx}/auth/getTreeData.action?catalog=rule',method:'get',onChange:function(n,o){$(this).val(n);}" />
				</td>
			</tr>
			<tr>
				<th width="120px;" align="right" class="datagrid-header">
					规则名称: <span style="color: red;">*</span>
				</th>
				<td>
					<input id="ruleName" name="ruleName" value="${rule.ruleName}"
							class="easyui-validatebox" data-options="required:true,novalidate:true" />
				</td>
			</tr>
			<tr>
				<th width="120px;" align="right" class="datagrid-header">
					访问路径: <span style="color: red;">&nbsp</span>
				</th>
				<td>
					<input id="url" name="url" value="${rule.url}"
							class="easyui-validatebox" data-options="" />
				</td>
			</tr>
			<tr>
				<th width="120px;" align="right" class="datagrid-header">
					图标: <span style="color: red;">&nbsp</span>
				</th>
				<td>
					<input id="icon" name="icon" value="${rule.icon}"
							class="easyui-validatebox" data-options="" />
				</td>
			</tr>
			<tr>
				<th width="120px;" align="right" class="datagrid-header">
					序号: <span style="color: red;">*</span>
				</th>
				<td>
					<input id="orderNum" name="orderNum" value="${rule.orderNum == null ? 20 : rule.orderNum}"
							class="easyui-validatebox" data-options="required:true,novalidate:true" />
				</td>
			</tr>
			<tr>
				<th width="120px;" align="right" class="datagrid-header">
					可见性: <span style="color: red;">*</span>
				</th>
				<td>
					<input id="visible" name="visible" value="${rule.visible == null ? 0 : rule.visible}"
							class="easyui-combotree" data-options="url:'${ctx}/auth/getTreeData.action?catalog=enumCfg&type=tb_rule.visible',method:'get',onChange:function(n,o){$(this).val(n);},required:true" />
				</td>
			</tr>
			<tr>
				<th width="120px;" align="right" class="datagrid-header">
					节点类型: <span style="color: red;">*</span>
				</th>
				<td>
					<input id="nodeType" name="nodeType" value="${rule.nodeType == null ? 0: rule.nodeType}"
							class="easyui-combotree" data-options="url:'${ctx}/auth/getTreeData.action?catalog=enumCfg&type=tb_rule.node_type',method:'get',onChange:function(n,o){$(this).val(n);},required:true" />
				</td>
			</tr>
			<tr>
				<th width="120px;" align="right" class="datagrid-header">
					节点编码: <span style="color: red;">*</span>
				</th>
				<td>
					<input id="nodeCode" name="nodeCode" value="${rule.nodeCode}"
							class="easyui-validatebox" data-options="required:true,novalidate:true" />
				</td>
			</tr>
		</table>
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
