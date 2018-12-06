<%@ page contentType="text/html;charset=UTF-8" %>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>员工所辖机构信息编辑</title>
	<%@ include file="/common/meta.jsp" %>
	<%@ include file="/common/import.jsp" %>
<script type="text/javascript">

function save() {
	
	if(!$('#editForm').form('validate'))
		return;
	
	var orgList = getChecked();
	if(orgList == ''){
		alert('请选择所辖的营业厅');
		return ;
	}
	
	$.ajax({
		url: '${ctx}/auth/saveStaffOrg.action',
		type: 'post',
		data: {staffId: $('#staffId').val(),orgList:orgList, type: $('#type').val()},
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

function getChecked(){
	var nodes = $('#orgTree').tree('getChecked');
	var sText = '';
	for(var i=0; i<nodes.length; i++){
		if (sText != '') sText += ',';
		sText += nodes[i].text;
	}
	
	var sID = '';
	for(var i=0; i<nodes.length; i++){
		if (sID != '') sID += ',';
		sID += nodes[i].id;
	}
	
	return sID;

}

function Travel(treeID){//参数为树的ID，注意不要添加#
	var roots=$('#'+treeID).tree('getRoots'),children,i,j;
	var sText = '';
	for(i=0;i<roots.length;i++){
		sText = sText + roots[i].text ;
     	children=$('#'+treeID).tree('getChildren', roots[i].target);
     	for(j=0;j<children.length;j++)alert(children[j].text);
   }
} 
 

$(function(){
	
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
					<input id="staffId" name="staffId" value="${staff.staffId}" readonly="readonly"
							class="easyui-validatebox" data-options="" />
				</td>
			</tr>
			<tr>
				<th width="120px;" align="right" class="datagrid-header">
					员工编号: <span style="color: red;">&nbsp</span>
				</th>
				<td>
					<input id="loginNameA" name="loginNameA" value="${staff.loginName}" readonly="readonly"
							class="easyui-validatebox" data-options="required:true,novalidate:true" />
				</td>
			</tr>
			<tr>
				<th width="120px;" align="right" class="datagrid-header">
					员工姓名: <span style="color: red;">*</span>
				</th>
				<td>
					<input id="staffNameA" name="staffNameA" value="${staff.staffName}"
						<c:if test="${fn:length(staff.staffName)=='0'}">
							onfocus="openStaffSelection($(this));"
						</c:if>
						<c:if test="${fn:length(staff.staffName)>'0'}">
							readonly="readonly"
						</c:if>
							class="easyui-validatebox" data-options="required:true,novalidate:true" />
				</td>
			</tr>
			<tr>
				<th width="120px;" align="right" class="datagrid-header">
					类型: <span style="color: red;">*</span>
				</th>
				<td>
					<input id="type" name="type" class="easyui-combotree" data-options="url:'${ctx}/auth/getTreeData.action?catalog=enumCfg&type=tb_staff_org.type',method:'get',onChange:function(n,o){$(this).val(n);}" 
						value="${type == null ? '1' : type}" />
	
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
	
	<div title="" class="easyui-panel" iconCls="icon-redo" collapsible="true">
		<ul id="orgTree" name="orgTree" class="easyui-tree" data-options="checkbox:true,cascadeCheck:false,url:'${ctx}/auth/getTreeData.action?catalog=orgAll&staffId=${staff.staffId}&type=${type == null ? '1' : type}',method:'get',onChange:function(n,o){$(this).val(n);}" value="1" >
		</ul>
	</div>
	
<%@include file="/common/staffSelection.jsp" %>

<script type="text/javascript">

	function openStaffSelection(obj) {
		initStaffSelection(obj, setBroker);
		$('#staffSelectionDlg').dialog('open');
	}
	function setBroker(obj, data) {
		if(data) {
			obj.val(data.STAFF_NAME);
			$("#loginNameA").val(data.LOGIN_NAME);
			$("#staffId").val(data.STAFF_ID);
		}
	}

</script>	
</body>
</html>
