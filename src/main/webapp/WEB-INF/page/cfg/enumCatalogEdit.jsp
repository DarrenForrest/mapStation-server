<%@ page contentType="text/html;charset=UTF-8" %>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>枚举值类型编辑</title>
	<%@ include file="/common/meta.jsp" %>
	<%@ include file="/common/import.jsp" %>
<script type="text/javascript">

$(function(){
	$('#state').combobox('setValue', '${enumCatalog.state}');
});

function save() {
	
	if(!$('#editForm').form('enableValidation').form('validate'))
		return;
	
	$.ajax({
		url: '${ctx}/cfg/saveEnumCatalog.action',
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
					参数编码: <span style="color: red;"></span>
				</th>
				<td>
					<input type="hidden" id="id" name="id" value="${id}" />
					<input id="catalogCode" name="catalogCode" value="${enumCatalog.catalogCode}" 
							style="width:250px;"
							class="easyui-validatebox" data-options="required:true,novalidate:false" />
					<span style="color: red;">*</span>
				</td>
			</tr>
			<tr>
				<th width="120px;" align="right" class="datagrid-header">
					参数名称: <span style="color: red;"></span>
				</th>
				<td>
					<input id="catalogName" name="catalogName" value="${enumCatalog.catalogName}"
							class="easyui-validatebox" data-options="required:true,novalidate:false" />
					<span style="color: red;">*</span>		
				</td>
			</tr>
			<tr>
				<th width="120px;" align="right" class="datagrid-header">
					状态: <span style="color: red;"></span>
				</th>
				<td>
					<select class="easyui-combobox" id="state" name="state" value="${enumCatalog.state}"
							data-options="editable:false;required:true" style="width:200px;">
						<option value="1">在用</option>
						<option value="0">失效</option>
					</select>
					<span style="color: red;">*</span>
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
