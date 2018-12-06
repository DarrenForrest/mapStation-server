<%@ page contentType="text/html;charset=UTF-8" %>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>主机编辑</title>
	<%@ include file="/common/meta.jsp" %>
	<%@ include file="/common/import.jsp" %>
<script type="text/javascript">

function save() {
	
	if(!$('#editForm').form('validate'))
		return;
	  	
	$.ajax({
		url: '${ctx}/hostManage/saveHostManage.action',
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
					主机编号: <span style="color: red;">&nbsp</span>
				</th>
				<td>
					<input id="id" name="id" value="${hostManage.id}" readonly="readonly"
							class="easyui-validatebox" data-options="" />
				</td>
			</tr>
			<tr>
				<th width="120px;" align="right" class="datagrid-header">
					IP: <span style="color: red;">*</span>
				</th>
				<td>
					<input id="ip" name="ip" value="${hostManage.ip}"
							class="easyui-validatebox"  data-options="required:true" />
				</td>
			</tr>
			<tr>
				<th width="120px;" align="right" class="datagrid-header">
					主机名称: <span style="color: red;">*</span>
				</th>
				<td>
					<input id="name" name="name" value="${hostManage.name}"
							class="easyui-validatebox"  data-options="required:true" />
				</td>
			</tr>
			<tr>
				<th width="120px;" align="right" class="datagrid-header">
					用户名: <span style="color: red;">*</span>
				</th>
				<td>
					<input id="userName" name="userName" value="${hostManage.userName}"
							class="easyui-validatebox" data-options="required:true" />
				</td>
			</tr><tr>
				<th width="120px;" align="right" class="datagrid-header">
					密码: <span style="color: red;">*</span>
				</th>
				<td>
					<input id="passWord" name="passWord" value="${hostManage.passWord}"
							class="easyui-validatebox" data-options="required:true" />
				</td>
			</tr>
			<tr>
				<th width="120px;" align="right" class="datagrid-header">
					是否备份主机: <span style="color: red;">*</span>
				</th>
				<td>
					<select class="form-control input-sm" id="backupHost"  name="backupHost" class="easyui-validatebox" required="true" >
						<option value="">请选择</option>
						<option value="0" ${hostManage.backupHost=="0"?'selected':''}>否</option>
						<option value="1" ${hostManage.backupHost=="1"?'selected':''}>是</option>
					</select>
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
