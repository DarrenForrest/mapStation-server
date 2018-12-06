<%@ page contentType="text/html;charset=UTF-8" %>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>节假日编辑</title>
	<%@ include file="/common/meta.jsp" %>
	<%@ include file="/common/import.jsp" %>
<script type="text/javascript">

function save() {
	
	if(!$('#editForm').form('validate'))
		return;
	
	$.ajax({
		url: '${ctx}/cfg/saveHoliday.action',
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
					节假日编号: <span style="color: red;">&nbsp</span>
				</th>
				<td>
					<input id="id" name="id" value="${holiday.id}" readonly="readonly"
							class="easyui-validatebox" data-options="" />
				</td>
			</tr>
			<tr>
				<th width="120px;" align="right" class="datagrid-header">
					节假日类型: <span style="color: red;">*</span>
				</th>
				<td>
					<input id="holidayType" name="holidayType" value="${holiday.holidayType}"
							class="easyui-combotree" data-options="url:'${ctx}/auth/getTreeData.action?catalog=enumCfg&type=tb_holiday.holiday_type',method:'get',onChange:function(n,o){$(this).val(n);},required:true" />
				</td>
			</tr>
			<tr>
				<th width="120px;" align="right" class="datagrid-header">
					日期: <span style="color: red;">*</span>
				</th>
				<td>
					<input type="text" id="holiday" name="holiday" class="Wdate" value="<fmt:formatDate value='${holiday.holiday}' type="date" pattern="yyyy-MM-dd"/>"
						   onclick="WdatePicker({isShowClear:true,readOnly:true,dateFmt:'yyyy-MM-dd'})"	 />
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
