<%@ page contentType="text/html;charset=UTF-8" %>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>文件信息编辑</title>
	<%@ include file="/common/meta.jsp" %>
	<%@ include file="/common/import.jsp" %>
<script type="text/javascript">

function save() {
	
	if(!$('#editForm').form('validate'))
		return;
	
	$('#editForm').submit();
	return;
	
	$.ajax({
		url: '${ctx}/auth/toUploadFile.action',
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

function importToTable() {
	if(!$('#editForm').form('validate'))
		return;
	
	$.ajax({
		url: '${ctx}/received/importReceivedFile.action',
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

$(function(){
	var type = '${type}';
	var code = '${code}';
	var message = '${message}';
	
	if(code != null && code == '0') {
		parent.closeDialog();
		alert(message);
	}
});

</script>

<style type="text/css">   
.mytable_xxx .datagrid-header { position: absolute; visibility: hidden; }
</style>
<script type="text/javascript">
	var listUrl = '${ctx}/received/getPreviewDataList.action?id=${file.id}';
	
	$(function(){
		$('#tableList').datagrid({
			title:'文件内容预览',
			iconCls:'icon-ok',
			url:listUrl,
			header:false,
			nowrap: false,
			striped: true,
			collapsible:false,
			fitColumns: true,
			pagination:true,
			singleSelect:true,
			rownumbers:false,
			remoteSort:true,
			pageList:[10,15,30,50],
			idField:'0',
			columns:[[
				{field:'0',			title:'<银行账号>'		,	align:'center', sortable:true, width:50},
				{field:'1',			title:'<交易日期>'		,	align:'center', sortable:true, width:50},
				{field:'2',			title:'<交易类型>'		,	align:'center', sortable:true, width:50},
				{field:'3',			title:'<对方户名>'		,	align:'center', sortable:true, width:50},
				{field:'4',			title:'<对方账号>'		,	align:'center', sortable:true, width:50},
				{field:'5',			title:'<摘要>'			,	align:'center', sortable:true, width:50},
				{field:'6',			title:'<借方发生额>'	,	align:'center', sortable:true, width:50},
				{field:'7',			title:'<贷方发生额>'	,	align:'center', sortable:true, width:50},
				{field:'8',			title:'<账户余额>'		,	align:'center', sortable:true, width:50},
				{field:'9',			title:'<营业厅编码>'	,	align:'center', sortable:true, width:50},
			]],
			onDblClickRow:function(row){
				//viewProject();
			},
		});
	});
	
	function searchList(){
    	var queryParams = $('#tableList').datagrid('options').queryParams;	       
        queryParams.settleName = $('#settleName').val();
        queryParams.startDate  = $('#startDate').val();
        queryParams.endDate    = $('#endDate').val();        
        $('#tableList').datagrid("load");
   	}
	function clearSearch(){
		$('#settleName').val("");
		$('#shortName').val("");
		$('#custName').val("");
		searchList();
	}

	function editInfo(id) {
		var url = 'toFileEdit.action';
		if(url != null) {
			url += ('?id=' + id);
		}
		showDialog('INFO', url, 'middle', true);
	}
	function delInfo(id) {
		if(!confirm('确定要删除选择的记录吗？')) {
			return;
		}
		$.ajax({
			url: '${ctx}/auth/delFile.action',
			type: 'post',
			data: {id:id},
			dataType: 'json',
			success: function(data) {
				alert(data.message);
				if(data.code == 0) {
					reload();
				}
			},
			error: function(data) { alert('ajax error'); }
		});
	}
	function reload() {
		$('#tableList').datagrid('reload');
	}
</script>
</head>
<body>
	<div class="easyui-panel">
		<form id="editForm" method="post" action="${ctx}/auth/toUploadFile.action" enctype="multipart/form-data">
		<input id="type" name="type" style="display:none;" value="${type}" />
		<table class="datagrid-body" width="100%" cellspacing="1" cellpadding="0" align="center">
			<tr style="display:none">
				<th width="120px;" align="right" class="datagrid-header">
					文件编号: <span style="color: red;">&nbsp</span>
				</th>
				<td>
					<input id="id" name="id" value="${file.id}" readonly="readonly"
							class="easyui-validatebox" data-options="" />
				</td>
			</tr>
			<tr style="display:none">
				<th width="120px;" align="right" class="datagrid-header">
					文件名: <span style="color: red;">&nbsp</span>
				</th>
				<td>
					<input id="fileName" name="fileName" value="${file.fileName}" readonly="readonly"
							class="easyui-validatebox" data-options="" />
				</td>
			</tr>
			<tr>
				<th width="120px;" align="right" class="datagrid-header">
					文件路径: <span style="color: red;">&nbsp</span>
				</th>
				<td>
					<input id="filePath" name="filePath" value="${file.filePath}" readonly="readonly"
					<c:if test="${file == null}">
						type="file" 
					</c:if>
					/>
				</td>
			</tr>
			<tr>
				<th width="120px;" align="right" class="datagrid-header">
					文件格式: <span style="color: red;">*</span>
				</th>
				<td>
					<input id="fileTypeId" name="fileTypeId" value="${file.fileTypeId == null ? 1: file.fileTypeId}"
							class="easyui-combotree" data-options="url:'${ctx}/auth/getTreeData.action?catalog=enumCfg&type=tb_file.file_type_id',method:'get',onChange:function(n,o){$(this).val(n);},required:true" />
				</td>
			</tr>
			<tr>
				<th width="120px;" align="right" class="datagrid-header">
					备注: <span style="color: red;">&nbsp</span>
				</th>
				<td>
					<input id="remark" name="remark" value="${file.remark}"
							class="easyui-validatebox" data-options="" />
				</td>
			</tr>
			<c:if test="${file != null || type == 'direct'}">
			<tr style="display:none">
				<th width="120px;" align="right" class="datagrid-header">
					到账类型: <span style="color: red;">*</span>
				</th>
				<td>
					<input id="fileTypeId" name="fileTypeId" value="1"
							class="easyui-combotree" data-options="url:'${ctx}/auth/getTreeData.action?catalog=enumCfg&type=tb_money_received.file_type_id',method:'get',onChange:function(n,o){$(this).val(n);},required:true" />
				</td>
			</tr>
			<tr>
				<th width="120px;" align="right" class="datagrid-header">
					银行账号: <span style="color: red;">*</span>
				</th>
				<td>
					<input id="bankAcctId" name="bankAcctId" value="${bankAcctId}"
							class="easyui-combotree" data-options="url:'${ctx}/auth/getTreeData.action?catalog=bankAcctInfo&type=',method:'get',onChange:function(n,o){$(this).val(n);},required:true" />
				</td>
			</tr>
			<tr>
				<th width="120px;" align="right" class="datagrid-header">
					结算起始时间: <span style="color: red;">*</span>
				</th>
				<td>
					<input id="settleBeginDate" name="settleBeginDate" value="${settleBeginDate}"
							class="easyui-datebox" data-options="required:true" />
				</td>
			</tr>
			<tr>
				<th width="120px;" align="right" class="datagrid-header">
					结算截止时间: <span style="color: red;">*</span>
				</th>
				<td>
					<input id="settleEndDate" name="settleEndDate" value="${settleEndDate}"
							class="easyui-datebox" data-options="required:true" />
				</td>
			</tr>
			</c:if>
		</table>
		</form>
		<center>
			<c:if test="${file == null}">
			<a href="#" class="easyui-linkbutton" iconCls="icon-save" onClick="return save();">提交</a>
			</c:if>
			<c:if test="${file != null}">
			<a href="#" class="easyui-linkbutton" iconCls="icon-save" onClick="return importToTable();">导入</a>
			</c:if>
			<a href="#" class="easyui-linkbutton" iconCls="icon-back" id="btn-back" onclick="parent.closeDialog();">返回</a>
		</center>
		
	</div>
	<br>
	<div class="easyui-panel mytable" <c:if test="${file == null}">style="display:none"</c:if> >
		<table id="tableList"></table>
	</div>
	
	<c:if test="${file == null}">
	<div>
		文件上传格式：第一行必须是如下标题头（顺序一致，共10列）
		<br>
		<br>
		银行账号	交易日期	交易类型	对方户名	对方账号	摘要	借方发生额	贷方发生额	账户余额	营业厅编码
	</div>
	</c:if>
	
	<style scoped="scoped">
		.textbox{
			height:20px;
			margin:0;
			padding:0 2px;
			box-sizing:content-box;
		}
	</style>
</body>
</html>
