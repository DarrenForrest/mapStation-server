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
		url: '${ctx}/hostInfo/saveHostInfo.action',
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
					<input id="id" name="id" value="${hostInfo.id}" readonly="readonly"
							class="easyui-validatebox" data-options="" />
				</td>
			</tr>
			<tr>
				<th width="120px;" align="right" class="datagrid-header">
					IP: <span style="color: red;">*</span>
				</th>
				<td>
					<input id="ip" name="ip" value="${hostInfo.ip}"
							class="easyui-validatebox"  data-options="required:true" />
				</td>
			</tr>
			<tr>
				<th width="120px;" align="right" class="datagrid-header">
					主机: <span style="color: red;">*</span>
				</th>
				<td>
					<input id="projectName" name="projectName" value="${hostInfo.projectName}"
							class="easyui-validatebox"  data-options="required:true" />
				</td>
			</tr>
			<tr>
				<th width="120px;" align="right" class="datagrid-header">
					地址: <span style="color: red;">*</span>
				</th>
				<td>
					<input id="address" name="address" value="${hostInfo.address}"
							class="easyui-validatebox" data-options="required:true" />
				</td>
			</tr>
			<tr>
				<th width="120px;" align="right" class="datagrid-header">
					文件名: <span style="color: red;">*</span>
				</th>
				<td>
					<input id="fileName" name="fileName" value="${hostInfo.fileName}"
							class="easyui-validatebox" data-options="required:true" />
				</td>
			</tr>	
			<!-- 
			<tr>
				<th width="120px;" align="right" class="datagrid-header">
					内容: <span style="color: red;">*</span>
				</th>
				<td>
					<input id="info" name="info" value="${hostInfo.info}"
							class="easyui-validatebox" data-options="required:true" />
				</td>
				 -->	
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
