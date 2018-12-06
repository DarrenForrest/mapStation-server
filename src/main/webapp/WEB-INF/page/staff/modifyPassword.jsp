<%@ page contentType="text/html;charset=UTF-8" %>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>修改密码</title>
	<%@ include file="/common/meta.jsp" %>
	<%@ include file="/common/import.jsp" %>
<script type="text/javascript">

function save() {
	
	if(!$('#editForm').form('validate'))
		return;
	
	$.ajax({
		url: '${ctx}/auth/modifyPassword.action',
		type: 'post',
		data: $('#editForm').serialize(),
		dataType: 'json',
		success: function(data){
			if(data.code == 0) {
				window.location.reload();
			}
			alert(data.message);
		},
		error: function(data){ alert("ajax error"); }
	});
}

function saveMobile() {
	
	if(!$('#mobileForm').form('validate')) return;
	
	var mobile = $('#mobile').val();
	//if(mobile == ''){alert('请填写手机号');$('#mobile').focus();return;}
	
	var myreg = /^(1)+\d{10}$/; 
    if(!myreg.test(mobile)) 
    { 
		alert('请输入有效的手机号码！'); 
		$('#mobile').focus(); 
		return false; 
     }
	
	
	$.ajax({
		url: '${ctx}/auth/modifyMobile.action',
		type: 'post',
		data: $('#mobileForm').serialize(),
		dataType: 'json',
		success: function(data){
			if(data.code == 0) {
				window.location.reload();
			}
			alert(data.message);
		},
		error: function(data){ alert("ajax error"); }
	});
}

</script>
</head>
<body>
	<div class="easyui-panel" title="修改密码">
		<form id="editForm" method="post">
		<input type="hidden" name="token" value="${token}" />
		<table class="datagrid-body" width="100%" cellspacing="1" cellpadding="0" align="center">
			<tr>
				<th width="120px;" align="right" class="datagrid-header">
					原密码: <span style="color: red;">*</span>
				</th>
				<td>
					<input id="oldPassword" name="oldPassword" value="" type="password"
							class="easyui-validatebox" data-options="required:true,novalidate:true" />
				</td>
			</tr>
			<tr>
				<th width="120px;" align="right" class="datagrid-header">
					新密码: <span style="color: red;">*</span>
				</th>
				<td>
					<input id="newPassword" name="newPassword" value="" type="password"
							class="easyui-validatebox" data-options="required:true,novalidate:true" />
				</td>
			</tr>
			<tr>
				<th width="120px;" align="right" class="datagrid-header">
					确认新密码: <span style="color: red;">*</span>
				</th>
				<td>
					<input id="confirmPassword" name="confirmPassword" value="" type="password"
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
	
	<div class="easyui-panel" title="修改手机号 (手机号用来接收短信通知)">
		<form id="mobileForm" method="post">
		<table class="datagrid-body" width="100%" cellspacing="1" cellpadding="0" align="center">
			<tr>
				<th width="120px;" align="right" class="datagrid-header">
					手机号: <span style="color: red;">*</span>
				</th>
				<td>
					<input id="mobile" name="mobile" value="${mobile}" type="text"
							class="easyui-validatebox" data-options="required:true,novalidate:true" />
				</td>
			</tr>
		</table>
		</form>
		<center>
			<a href="#" class="easyui-linkbutton" iconCls="icon-save"
				onClick="return saveMobile();">提交</a> <a href="#"
				class="easyui-linkbutton" iconCls="icon-back" id="btn-back"
				onclick="parent.closeDialog();">返回</a>
		</center>
		
	</div>
</body>
</html>
