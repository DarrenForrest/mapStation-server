<%@ page contentType="text/html;charset=UTF-8" %>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>文件上传导入</title>
	<%@ include file="/common/meta.jsp" %>
	<%@ include file="/common/import.jsp" %>
<style type="text/css">
.extWidth {
width: 300px;
}
</style>
<script type="text/javascript">

function save() {	
	if(!$('#editForm').form('validate'))
		return;	
	showWaiting(true);
	$('#editForm').submit();
}

function importToTable()
{
	var fileTypeId = '${fileTypeId}';
	var id = '${id}';
	
	
	
	
	if(fileTypeId =='' || id == '' )
	{
		alert('请先上传文件后再导入');
		return;
	}
	
	if(fileTypeId == '5' ) // && id<>''
	{
		var bankAcctIdMy = $('#bankAcctIdMy').val();
		$.ajax({
			url: '${ctx}/income/importCheckFile.action?fileTypeId='+fileTypeId+'&id='+id + '&bankAcctIdMy=' +bankAcctIdMy,
			type: 'post',
			data: {id:id},
			dataType: 'json',
			success: function(data) {
				if(data.code != 0) {
					alert(data.message);
					return ;
				}
				if(data.cnt < 1)
				{
				  return;	
				}
				
				for(var i=0; i < data.list.length; i++)
				{
					var index = parent.addFieldC();	
					$('#3_acctItemType_' + index, window.parent.document).val('102');	
					parent.onchangePayType(index);	
					$('#3_amount_' + index, window.parent.document).val(data.list[i].AMOUNT);
					$('#3_bankId_' + index, window.parent.document).val(data.bankId);
					parent.onchangeBank(index);	
					$('#3_bankAcctId_' + index, window.parent.document).val(data.bankAcctId);
					$('#3_custName_' + index, window.parent.document).val(data.list[i].CUST_NAME);
					$('#3_chequeNbr_' + index, window.parent.document).val(data.list[i].CHECK_NBR);
					$('#3_saveTime_' + index, window.parent.document).val(data.list[i].SAVE_DATE);
				}
				alert('支票导入成功');
				parent.closeDialog();
								
			},
			error: function(data) { alert('ajax error'); }
		});
	}
}

$(function(){
	var fileTypeId = '${fileTypeId}';
	var code = '${code}';
	var message = '${message}';
	var index = '${index}';
	
	<c:if test="${fileTypeId == 9 && index != null }">		
		if(code != null && code == '0') {
			$('#3_fileNameShow_' + ${index}, window.parent.document).text('${file.fileName}');
			$('#3_fileNameShow_' + ${index}, window.parent.document).attr("target",'_blank');
			$('#3_fileNameShow_' + ${index}, window.parent.document).attr("href",'${ctx}/income/download.action?fileId=${file.id}');
			$('#3_fileId_' + ${index}, window.parent.document).val('${file.id}');
			$('#3_fileName_' + ${index}, window.parent.document).val('${file.fileName}');
			parent.closeDialog();
		}else if(code != null && code != '') {
			alert(message);
		}		
	</c:if>
	
	
	<c:if test="${fileTypeId != 5}">
	if(code != null && code == '0') {
		parent.closeDialog();
		alert(message);
	} else if(code != null && code != '') {
		alert(message);
	}
	</c:if>
	
	//必须选择银行账号才行		
	$('#bankAcctId').combotree( {  
	    //选择树节点触发事件  
	    onSelect: function(node) {  
	        //返回树对象  
	        var tree = $(this).tree;  
	        //选中的节点是否为叶子节点,如果不是叶子节点,清除选中  
	        var isLeaf = tree('isLeaf', node.target);  
	        if (!isLeaf) {  
	            //清除选中  
	            $('#bankAcctId').combotree('clear');  
	        }  
	    },
	    panelWidth:400
	});
	
	//$("#fileTypeId").combotree("readonly", "readonly");  
	
});

</script>


</head>
<body>
	<div class="easyui-panel">
		<form id="editForm" method="post"  action="${ctx}/auth/uploadFileAndImport.action" enctype="multipart/form-data">
		<input id="fileType" name="fileType" style="display:none;" value="${fileTypeId}" />
		<input id="isMonth" name="isMonth" style="display:none;" value="${isMonth}" />
		<input type="hidden" id="index" name="index" value="${index}">
		<table class="datagrid-body" width="100%" cellspacing="1" cellpadding="0" align="center">
			<tr>
				<th width="120px;" align="right" class="datagrid-header">
					文件路径: <span style="color: red;">&nbsp</span>
				</th>
				<td>
					<input id="filePath" name="filePath" type="file" class="extWidth"
							<c:if test="${fileTypeId == 9 || fileTypeId == 10}">
								accept="image/*"
							</c:if>
					  />
					<c:if test="${fileTypeId == 1}">
						&nbsp;<a href="${ctx}/tpl/arrivedFileTpl.xlsx" target="_blank" >模板下载(新版Office)</a>
						&nbsp;<a href="${ctx}/tpl/arrivedFileTpl_2003.xls" target="_blank" >模板下载(旧版Office)</a>
					</c:if>
					<c:if test="${fileTypeId == 5}">
						&nbsp;<a href="${ctx}/tpl/checkFileTpl.xlsx" target="_blank" >模板下载(新版Office)</a>
						&nbsp;<a href="${ctx}/tpl/checkFileTpl_2003.xls" target="_blank" >模板下载(旧版Office)</a>
					</c:if>
					<c:if test="${fileTypeId == 8}">
						&nbsp;<a href="${ctx}/tpl/voucherArrivedFileTpl.xlsx" target="_blank" >模板下载(新版Office)</a>
						&nbsp;<a href="${ctx}/tpl/voucherArrivedFileTpl_2003.xls" target="_blank" >模板下载(旧版Office)</a>
					</c:if>
				</td>
			</tr>			
			<c:choose>
			   <c:when test="${fileTypeId == 5}">
			   		<input type="hidden" id="fileTypeId" name="fileTypeId" value="${fileTypeId}"/>
			   </c:when>  
			   <c:when test="${fileTypeId == 8}">
			   		<input type="hidden" id="fileTypeId" name="fileTypeId" value="${fileTypeId}"/>
			   </c:when>
			   <c:when test="${fileTypeId == 9 || fileTypeId == 10}">
			   		<input type="hidden" id="fileTypeId" name="fileTypeId" value="${fileTypeId}"/>
			   		<input type="hidden" id="handOverDetailId" name="handOverDetailId" value="${handOverDetailId}"/>
			   </c:when>     
			   <c:otherwise>
			   	<tr>
				<th width="120px;" align="right" class="datagrid-header">
					文件格式: <span style="color: red;">*</span>
				</th>
				<td width="300px;">
					<input id="fileTypeId" name="fileTypeId" value="${fileTypeId}"
						class="easyui-combotree extWidth" data-options="url:'${ctx}/auth/getTreeData.action?catalog=enumCfg&type=tb_money_received.file_type_id',method:'get',onChange:function(n,o){$(this).val(n);},required:true" />
				</td>
				</tr>
			   </c:otherwise>
			</c:choose>
			

			<tr>
				<th width="120px;" align="right" class="datagrid-header">
					备注: <span style="color: red;">&nbsp</span>
				</th>
				<td>
					<input id="remark" name="remark" value=""
							class="easyui-validatebox extWidth" data-options="" />
				</td>
			</tr>
			<c:if test="${fileTypeId == 1}">
			<tr>
				<th width="120px;" align="right" class="datagrid-header">
					银行账号: <span style="color: red;">*</span>
				</th>
				<td>
					<input id="bankAcctId" name="bankAcctId" 
							class="easyui-combotree extWidth" data-options="url:'${ctx}/auth/getTreeData.action?catalog=bankAcctInfo&type=',method:'get',onChange:function(n,o){$(this).val(n);},required:true" value="${bankAcctId}" />
				</td>
			</tr>
			<tr>
				<th width="120px;" align="right" class="datagrid-header">
					结算起始时间: <span style="color: red;">*</span>
				</th>
				<td>
					<input id="settleBeginDate" name="settleBeginDate" value="${settleBeginDate}"
							class="easyui-datebox extWidth" data-options="required:true" />
				</td>
			</tr>
			<tr>
				<th width="120px;" align="right" class="datagrid-header">
					结算截止时间: <span style="color: red;">*</span>
				</th>
				<td>
					<input id="settleEndDate" name="settleEndDate" value="${settleEndDate}"
							class="easyui-datebox extWidth" data-options="required:true" />
				</td>
			</tr>
			</c:if>
			<c:if test="${fileTypeId == 5}">
			<tr>
				<th width="120px;" align="right" class="datagrid-header">
					银行账号: <span style="color: red;">*</span>
				</th>
				<td>
					<input id="bankAcctId" name="bankAcctId" value=""
							class="easyui-combotree extWidth" data-options="url:'${ctx}/auth/getTreeData.action?catalog=bankAcctInfo&type=',method:'get',onChange:function(n,o){$(this).val(n);},required:true" />
				</td>
			</tr>
			</c:if>
			<c:if test="${fileTypeId == 8}">
			<tr>
				<th width="120px;" align="right" class="datagrid-header">
					营业厅: <span style="color: red;">*</span>
				</th>
				<td>
					<input id="orgId" name="orgId" class="easyui-combotree extWidth" data-options="url:'${ctx}/auth/getTreeData.action?catalog=orgAsync&type=<%=optionOrgId%>',method:'get',onChange:function(n,o){$(this).val(n);}" value="${orgId}" />
				</td>
			</tr>
			<tr>
			    <th width="120px;" align="right" class="datagrid-header">
			               数据范围: <span style="color: red;">*</span>
			    </th>
				<td> 
					<select id="rangeType" name="rangeType" value="${rangeType}" class="extWidth">						
						<option value="3" >自身及子部门</option>
						<option value="2" >仅子部门</option>
						<option value="1" >仅自身</option>
					</select> 
			    </td>
			</tr>
				<tr>
				<th width="120px;" align="right" class="datagrid-header">
					营收起始时间: <span style="color: red;">*</span>
				</th>
				<td>
					<input id="settleBeginDate" name="settleBeginDate" value="${settleBeginDate}"
							class="easyui-datebox extWidth" data-options="required:true" />
				</td>
			</tr>
			<tr>
				<th width="120px;" align="right" class="datagrid-header">
					营收截止时间: <span style="color: red;">*</span>
				</th>
				<td>
					<input id="settleEndDate" name="settleEndDate" value="${settleEndDate}"
							class="easyui-datebox extWidth" data-options="required:true" />
				</td>
			</tr>
			
			<c:if test="${auth.staffName == '蔡咏梅' || auth.staffName == '欧阳雯' || auth.staffName == 'root' }">
			<tr>
				<th width="120px;" align="right" class="datagrid-header">
					文件类型: <span style="color: red;">*</span>
				</th>
				<td>
					<input id="arrivedFlag" name="arrivedFlag" class="easyui-combotree extWidth" data-options="url:'${ctx}/auth/getTreeData.action?catalog=enumCfg&type=page_fileUploadAndImport.arrivedFlag',method:'get',onChange:function(n,o){$(this).val(n);}" value="0" />		
				</td>
			</tr>
			</c:if>
			<c:if test="${auth.staffName != '蔡咏梅' &&  auth.staffName != '欧阳雯' && auth.staffName != 'root'}">
				<input type="hidden" id="arrivedFlag" name="arrivedFlag" value="0"/>
			</c:if>
			
			
			</c:if>
		</table>
		</form>
		<center>
			<a href="#" class="easyui-linkbutton" iconCls="icon-save" onClick="return save();">上传</a>
			<c:if test="${fileTypeId == 5}">
			<a href="#" class="easyui-linkbutton" iconCls="icon-save" onClick="return importToTable();">导入</a>
			</c:if>
			<a href="#" class="easyui-linkbutton" iconCls="icon-back" id="btn-back" onclick="parent.closeDialog();">返回</a>
		</center>
		
	</div>
	<br>
	
	<c:if test="${fileTypeId == 1}">
	<div>
		文件上传格式：第一行必须是如下标题头（顺序一致，共10列）
		<br>
		<br>
		银行账号	交易日期(YYYY-MM-DD)	交易类型	对方户名	对方账号	摘要	借方发生额(必填)	贷方发生额(必填)	账户余额(必填)	营业厅编码	退票标识
	</div>
	</c:if>
	
	<c:if test="${fileTypeId == 5}">
	<div>			
		<h3> <FONT color=#ff0000>${message}</FONT></h3>  
		<input type="hidden" id="id" name="id" value="${id}"/>
		<input type="hidden" id="bankAcctIdMy" name="bankAcctIdMy" value="${bankAcctId}"/>
	</div>
	</c:if>
	<c:if test="${fileTypeId == 8}">
	<div>
		文件上传格式：第一行必须是如下标题头（顺序一致，共5列）科目代码为银行时，需要填写缴存日期
		<br>
		<br>
		摘要 科目代码 借方 贷方 缴存日期(YYYY-MM-DD)
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
