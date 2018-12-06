<%@ page contentType="text/html;charset=UTF-8" %>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>目录编辑</title>
	<%@ include file="/common/meta.jsp" %>
	<%@ include file="/common/import.jsp" %>
<script type="text/javascript">
function inputOnBlur() {
	var ip=$("#name").val();
	var path=$("path").val();
	$.ajax({
		url: '${ctx}/hostManage/regPath.action',
		type: 'post',
		data: $('#editForm').serialize(),
		dataType: 'json',
		success: function(data){
			if(data.code == 0) {
				alert(data.message);
			}else{
				alert(data.message);
			}	
		},
		error: function(data){ alert("ajax error"); }
	});	
}

function save() {
	
	if(!$('#editForm').form('validate'))
		return;
	
	$.ajax({
		url: '${ctx}/catalogManage/saveCatalogManage.action',
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

function chouseIP(){
	$.ajax({
		url: '${ctx}/catalogManage/selectIP.action',
		type: 'post',
		dataType: 'json',
		success: function(data){
			var listIp = data.data;
			$("#ip").empty();
			$("#ip").html("");
			var html="";
			html+="<option value='' >&nbsp;&nbsp;&nbsp;---请选择---</option>";
			if(listIp.length>0){
				for(var i=0;i<listIp.length;i++){
					html+="<option value='"+listIp[i].ip+"' >&nbsp;&nbsp;&nbsp;"+listIp[i].ip+"</option>";
				}
			}else{
				html="<option>未查找到主机IP信息</option>"
			}
			$("#ip").append(html);
		},
		error: function(data){ alert("获取主机IP失败，请重试"); }
	});

}

</script>
</head>
<body onload="chouseIP();" >
	<div class="easyui-panel">
		<form id="editForm" method="post">
		<table class="datagrid-body" width="100%" cellspacing="1" cellpadding="0" align="center">
			<tr>
				<th width="120px;" align="right" class="datagrid-header">
					目录编号: <span style="color: red;">&nbsp</span>
				</th>
				<td>
					<input id="id" name="id" value="${catalogManage.id}" readonly="readonly"
							class="easyui-validatebox" data-options="" />
				</td>
			</tr>				
			<tr>
				<th width="120px;" align="right" class="datagrid-header">
					所属主机IP: <span style="color: red;">*</span>
				</th>
				<td>
					<select id="ip" name="ip" style="width: 160px;" class="easyui-validatebox" data-rule="required;" data-options="required:true"  >
					</select>
				</td>
			</tr>
			<tr>
				<th width="120px;" align="right" class="datagrid-header">
					路径: <span style="color: red;">*</span>
				</th>
				<td>
					<input id="path" name="path" value="${catalogManage.path}"
							class="easyui-validatebox" data-rule="required;" data-options="required:true"  />
				</td>
			</tr>
			<tr>
				<th width="120px;" align="right" class="datagrid-header">
					目录名称: <span style="color: red;">*</span>
				</th>
				<td>
					<input id="name" name="name" value="${catalogManage.name}"
							class="easyui-validatebox" data-rule="required;" data-options="required:true"  />
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
