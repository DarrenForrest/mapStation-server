<%@ page contentType="text/html;charset=UTF-8" %>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>AMS帐号信息编辑</title>
	<%@ include file="/common/meta.jsp" %>
	<%@ include file="/common/import.jsp" %>
<script type="text/javascript">

function save() {
	if(!$('#editForm').form('validate'))
		return;
	
	$.ajax({
		url: '${ctx}/ams/saveStaff.action',
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

function test(action){
	var userName = $('loginName').val();
	var password = $('loginName').val();
	
	if(userName == ''){
		alert('请输入登录帐号');
		return;
	}
	if(password == ''){
		alert('请输入密码');
		return;
	}
	
	if(!confirm('你确定要操作吗？\n密码输错4次，账号会被锁定！')){
		return;
	}
	
	var url = '${ctx}/ams/' + action + '.action';
	
	$.ajax({
		url: url,
		type: 'post',
		data: $('#editForm').serialize(),
		dataType: 'json',
		success: function(data){
			if(data.code == 0) {
				//parent.closeDialog();
			}
			alert(data.message);
		},
		error: function(data){ alert("ajax error"); }
	});
	
}

${announcementInfo.paymentDeadline == null ? '0' : paymentDeadlineC}

$(function(){
	$('#autoLoginFlag').combobox('select',"${staff.autoLoginFlag == null ? '1' : staff.autoLoginFlag}");
	$('#autoLoginoutFlag').combobox('select',"${staff.autoLoginoutFlag == null ? '1' : staff.autoLoginoutFlag}");
	$('#emailNoticeFlag').combobox('select',"${staff.emailNoticeFlag == null ? '1' : staff.emailNoticeFlag}");
	$('#lockState').combobox('select',"${staff.lockState == null ? '0' : staff.lockState}");
	$('#state').combobox('select',"${staff.state == null ? '1' : staff.state}");
});

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
					<input id="id" name="id" value="${staff.id}" 
							class="easyui-validatebox" data-options="" />
				</td>
			</tr>
			<tr>
				<th width="100px;" align="right" class="datagrid-header">
					登录帐号: 
				</th>
				<td>
					<input id="loginName" name="loginName" value="${staff.loginName}" 
							class="easyui-validatebox" data-options="required:true,novalidate:true" />
							<span style="color: red;">*</span>
				</td>

				<th width="100px;" align="right" class="datagrid-header">
					密码: 
				</th>
				<td>
					<input id="password" name="password" value="${staff.password}"
							class="easyui-validatebox" data-options="required:false,novalidate:true" />
							<span style="color: red;">*</span>
				</td>
			</tr>
			<tr>
				<th width="100px;" align="right" class="datagrid-header">
					姓名: 
				</th>
				<td>
					<input id="staffName" name="staffName" value="${staff.staffName}"
							class="easyui-validatebox" data-options="required:true,novalidate:true" />
							<span style="color: red;">*</span>
				</td>
				<th width="100px;" align="right" class="datagrid-header">
					备注: 
				</th>
				<td>
					<input id="remark" name="remark" value="${staff.remark}"
							class="easyui-validatebox" data-options="required:true,novalidate:true" />
							<span style="color: red;">&nbsp</span>
				</td>
			</tr>
			
			<tr>			
				<th width="100px;" align="right" class="datagrid-header">
					负责人: 
				</th>
				<td>
					<input id="adminName" name="adminName" value="${staff.adminName}"
							class="easyui-validatebox" data-options="required:true,novalidate:true" />
							<span style="color: red;">*</span>
				</td>
				<th width="100px;" align="right" class="datagrid-header">
					邮箱: 
				</th>
				<td>
					<input id="email" name="email" value="${staff.email}"
							class="easyui-validatebox" data-options="required:true,novalidate:true" />
							<span style="color: red;">* 用于接收邮件通知</span>
				</td>
			</tr>

			<tr>			
				<th width="100px;" align="right" class="datagrid-header">
					签入控制: 
				</th>
				<td>
					<select class="easyui-combobox" id="autoLoginFlag" name="autoLoginFlag" value="${staff.autoLoginFlag}" style="width:120px;">
							<option value="1">自动签入</option>
							<option value="0">人工签入</option>
					</select>
					<span style="color: red;">*</span>
				</td>
				<th width="100px;" align="right" class="datagrid-header">
					签出控制: 
				</th>
				<td>
					<select class="easyui-combobox" id="autoLoginoutFlag" name="autoLoginoutFlag" value="${staff.autoLoginFlag}" style="width:120px;">
							<option value="1">自动签出</option>
							<option value="0">人工签出</option>
					</select>
					<span style="color: red;">*</span>
				</td>
			</tr>
			
			<tr>
				<th width="100px;" align="right" class="datagrid-header">
					邮件通知: 
				</th>
				<td>
					<select class="easyui-combobox" id="emailNoticeFlag" name="emailNoticeFlag" value="${staff.emailNoticeFlag}" style="width:120px;">
							<option value="1">允许邮件通知</option>
							<option value="0">禁止通知</option>
					</select>
					<span style="color: red;">*</span>
				</td>

				<th width="100px;" align="right" class="datagrid-header">
					帐号锁定: 
				</th>
				<td>
					<select class="easyui-combobox" id="lockState" name="lockState"  value="${staff.lockState}" style="width:120px;">
							<option value="0">正常</option>
							<option value="1">锁定</option>
					</select>
					<span style="color: red;">* 锁定的账户不再自动签入和签出</span>
				</td>
			</tr>
			
			<tr>
				<th width="100px;" align="right" class="datagrid-header">
					状态: 
				</th>
				<td colspan="3">
					<select class="easyui-combobox" id="state" name="state" value="${staff.state}" style="width:120px;">
							<option value="1">正常</option>
							<option value="0">注销</option>
					</select>
					<span style="color: red;">*</span>
				</td>
			</tr>
			
			<tr>
				<td colspan="4">
					<center>
						<a href="#" class="easyui-linkbutton" iconCls="icon-ok" onClick="return test('staffLogin');">签入</a>
						&nbsp;&nbsp;
						<a href="#" class="easyui-linkbutton" iconCls="icon-ok" onClick="return test('staffLoginout');">签出</a>
						&nbsp;&nbsp;
						<a href="#" class="easyui-linkbutton" iconCls="icon-ok" onClick="return test('queryWorkTime');">查询工时</a>
					</center>
				</td>
			</tr>

		</table>
		
		</form>

		
		<center>
			<a href="#" class="easyui-linkbutton" iconCls="icon-save" onClick="return save();">提交</a>
			&nbsp;&nbsp;
			<a href="#" class="easyui-linkbutton" iconCls="icon-back" id="btn-back"	onclick="parent.closeDialog();">返回</a>
		</center>
		
	</div>
</body>
</html>
