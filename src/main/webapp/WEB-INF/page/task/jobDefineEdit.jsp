<%@ page contentType="text/html;charset=UTF-8" %>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>任务信息编辑</title>
	<%@ include file="/common/meta.jsp" %>
	<%@ include file="/common/import.jsp" %>
<script type="text/javascript">

function save() {
	
	if(!$('#editForm').form('validate'))
		return;
	
	$.ajax({
		url: '${ctx}/task/saveJobDefine.action',
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
					任务编号: <span style="color: red;">&nbsp</span>
				</th>
				<td>
					<input id="id" name="id" value="${jobDefine.id}" readonly="readonly"
							class="easyui-validatebox" data-options="" />
				</td>
			</tr>
			<tr>
				<th width="120px;" align="right" class="datagrid-header">
					任务名称: <span style="color: red;">*</span>
				</th>
				<td>
					<input id="jobName" name="jobName" value="${jobDefine.jobName}"
							class="easyui-validatebox" data-options="required:true,novalidate:true" />
				</td>
			</tr>
			<tr>
				<th width="120px;" align="right" class="datagrid-header">
					存储过程: <span style="color: red;">*</span>
				</th>
				<td>
					<input id="storedProcedure" name="storedProcedure" value="${jobDefine.storedProcedure}" readonly="readonly"
							class="easyui-validatebox" data-options="" />
				</td>
			</tr>
			<tr>
				<th width="120px;" align="right" class="datagrid-header">
					定时器配置: <span style="color: red;">*</span>
				</th>
				<td>
					<input id="cronExpression" name="cronExpression" value="${jobDefine.cronExpression}"
							class="easyui-validatebox" data-options="required:true,novalidate:true" />
				</td>
			</tr>
			<tr>
				<th width="120px;" align="right" class="datagrid-header">
					描述: <span style="color: red;">*</span>
				</th>
				<td>
					<input id="jobDesc" name="jobDesc" value="${jobDefine.jobDesc == null ? 0 : jobDefine.jobDesc}"
							class="easyui-validatebox" data-options="required:true,novalidate:true" />
				</td>
			</tr>
			<tr>
				<th width="120px;" align="right" class="datagrid-header">
					状态: <span style="color: red;">*</span>
				</th>
				<td>
					<input id="status" name="status" value="${jobDefine.status == null ? 1: jobDefine.status}"
							class="easyui-combotree" data-options="url:'${ctx}/auth/getTreeData.action?catalog=enumCfg&type=tb_job_define.status',method:'get',onChange:function(n,o){$(this).val(n);},required:true" />
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
