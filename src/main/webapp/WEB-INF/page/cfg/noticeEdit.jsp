<%@ page contentType="text/html;charset=UTF-8" %>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>通知查看</title>
	<%@ include file="/common/meta.jsp" %>
	<%@ include file="/common/import.jsp" %>
<script type="text/javascript">

function save() {
	
	var title = $('#title').val();
	var content = $('#content').val();
	var noticeType = $('#noticeType').val();
	
	if(title == "")
	{
		alert('标题不能为空');
		$('#title').focus();
		return;
	}
	
	if(noticeType == "")
	{
		alert('请选择分类');
		$('#noticeType').focus();
		return;	
	}
	
	if(content =="")
	{
		alert('内容不能为空');
		$('#content').focus();
		return;
	}
	
	if(content.length > 1500)
	{
		alert('内容超长，当前内容字数长度为'+ content.length +  '，系统允许最多1500个字');
		return;
	}
	
	
	$.ajax({
		url: '${ctx}/cfg/saveNoticeJson.action',
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
					标题:  <span style="color: red;">*&nbsp</span>
				</th>
				<td>	
				   <input type="hidden" id="id" name="id" value="${notice.id}" 
						  class="easyui-validatebox" data-options="" /> 
				   <input type="hidden" id="hotFlag" name="hotFlag" value="0"  /> 
					<input type="hidden" id="topFlag" name="topFlag" value="0" /> 	  		  
				   <input type="text" id="title" name="title" value="${notice.title}" 
				   		  style="width:600px"
						  class="easyui-validatebox" data-options="" />				
				</td>
			</tr>
			<tr>
				<th width="120px;" align="right" class="datagrid-header">
					分类:  <span style="color: red;">*&nbsp</span>
				</th>
				<td>
					<input id="noticeType" name="noticeType" value="${notice.noticeType}"
							class="easyui-combotree" data-options="url:'${ctx}/auth/getTreeData.action?catalog=enumCfg&type=tb_notice.notice_type',method:'get',onChange:function(n,o){$(this).val(n);}" />
				</td>
			</tr>
			<tr>
				<th width="120px;" align="right" class="datagrid-header">
					内容:  <span style="color: red;">*&nbsp</span>
				</th>
				<td height="200px">	
					<div>
						<textarea id="content" name="content" 				   		  
				   		  style="width:600px;height:200px;"				   		   
						  class="easyui-validatebox" 
						  data-options=""/>${notice.content}</textarea>	  		
					</div>

				</td>
			</tr>
		</table>
		</form>
		<center>
			<a href="#" class="easyui-linkbutton" iconCls="icon-save"
				onClick="return save();">提交</a>
			<a href="#"	class="easyui-linkbutton" iconCls="icon-back" 
				id="btn-back"	onclick="parent.closeDialog();">返回</a>
		</center>
		
	</div>
</body>
</html>
