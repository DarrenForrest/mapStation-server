<%@ page contentType="text/html;charset=UTF-8" %>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>通知查看</title>
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
				<th width="120px;" height="25px" align="right" class="datagrid-header">
					标题: 
				</th>
				<td>					
				   <div>
				   		${notice.title}
				   </div>
				</td>
			</tr>
			<tr>
				<th width="120px;" align="right" class="datagrid-header">
					分类: 
				</th>
				<td>
					<input id="noticeType" name="noticeType" value="${notice.noticeType}"
							class="easyui-combotree" data-options="url:'${ctx}/auth/getTreeData.action?catalog=enumCfg&type=tb_notice.notice_type',method:'get',onChange:function(n,o){$(this).val(n);},required:true" />
				</td>
			</tr>
			<tr>
				<th width="120px;" align="right" class="datagrid-header">
					内容: 
				</th>
				<td height="200px">				
					<div>
						<textarea id="content" name="content" readonly="readonly"				   		  
				   		  style="width:500px;height:200px;"				   		   
						  class="easyui-validatebox" 
						  data-options=""/>${notice.content}</textarea>	
					</div>
					
				</td>
			</tr>
		</table>
		</form>
		<center>
			<a href="#"
				class="easyui-linkbutton" iconCls="icon-back" id="btn-back"
				onclick="parent.closeDialog();">返回</a>
		</center>
		
	</div>
</body>
</html>
