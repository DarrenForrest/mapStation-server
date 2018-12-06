<%@ page contentType="text/html;charset=UTF-8" %>

<html>
<head>
	<title>营业厅管理</title>	
	<%@ include file="/common/meta.jsp" %>
    <%@ include file="/common/import.jsp" %>
    <%@ include file="/common/showDialog/showDialog_js.jsp" %>
    
<script type="text/javascript">
	//var listUrl = '${ctx}/auth/getOrgPage.action';
	var listUrl = '${ctx}/auth/getTreeGridData.action?catalog=orgAsyncForTreeGrid&type=<%=optionOrgId%>';
	
	$(function(){
		$('#tableList').treegrid({
			title:'营业厅管理',
			iconCls:'icon-ok',
			url:listUrl,
			nowrap: false,
			striped: true,
			collapsible:false,
			fitColumns: true,
			pagination:false,
			singleSelect:true,
			rownumbers:true,
			pageList:[10,15,30,50],
			idField:'ORG_ID',
			treeField:'ORG_NAME',
			columns:[[
			    {field:'ORG_NAME',			title:'营业厅名称',		align:'left',   sortable:false, width:150},
			    {field:'ORG_ID',			title:'营业厅编号',		align:'center', sortable:false, width:50},
			    {field:'ORG_MARK',			title:'营业厅标识',		align:'center', sortable:false, width:50, editor:'text'},
			    {field:'ARRIVED_SPEED',		title:'到账速度（天）',	align:'center', sortable:false, width:50, editor:'numberbox'},
				{field:'CASH_SAVE_RULE',	title:'现金缴存规律',	align:'center', sortable:false, width:50, editor:'text'},
			//	{field:'CASH_ZERO_DATE',	title:'现金缴存起算日',	align:'center', sortable:false, width:50},
				{field:'CHEQUE_SAVE_RULE',	title:'支票缴存规律',	align:'center', sortable:false, width:50, editor:'text'},
			//	{field:'CHEQUE_ZERO_DATE',	title:'支票缴存起算日',	align:'center', sortable:false, width:50},
				{field:'NO_ALARM',			title:'未填报不预警',	align:'center', sortable:false, width:50,
					formatter:function(value, rowdata){ return rowdata.NO_ALARM != 0 ? rowdata.NO_ALARM_NAME : ''; }
				/*	,editor:{
						type:'combotree',
						options:{
							valueField:'id',
							textField:'text',
							method:'get',
							url:'${ctx}/auth/getTreeData.action?catalog=enumCfg&type=tb_org_extend.no_alarm',
							required:false
						}
					} */
				},
				
			 // {field:'STATE_NAME',		title:'状态',			align:'center', sortable:false, width:50},
				{field:'STATE_DATE',		title:'状态日期',		align:'center', sortable:false, width:50, formatter:function(value, rowdata){return new Date(value).format('yyyy-MM-dd hh:mm:ss');}},
				{field:'CREATE_DATE',		title:'创建日期',		align:'center', sortable:false, width:50, formatter:function(value, rowdata){return new Date(value).format('yyyy-MM-dd hh:mm:ss');}},
			]],
			onDblClickRow:function(row){
				edit();
			},
			onClickRow:function(row) {
				save();
			},
			toolbar : [{
				text:'保存',
				iconCls:'icon-save',
				handler:function(){
					saveInfo();
				}
			},{
				text:'撤消',
				iconCls:'icon-redo',
				handler:function(){
					window.location.reload();
				}
			}]
		});
	});
	
	function searchList(){
    	var queryParams = $('#tableList').treegrid('options').queryParams;	       
    	queryParams.orgName = $('#orgName').val();
    	queryParams.orgMark = $('#orgMark').val();
        $('#tableList').treegrid("reload");
   	}
	function clearSearch(){
		$('#orgName').val("");
		$('#orgMark').val("");
		searchList();
	}

	function saveInfo() {
		save();
		var rows = $('#tableList').treegrid('getChanges', 'updated');
		//alert(rows.length);
		if(rows.length == 0) {
			alert('没有修改过的数据');
			return;
		}
		if(!confirm('确定要保存修改过的' + rows.length + '条数据吗？')) {
			return;
		}
		
		$.ajax({
			url: '${ctx}/auth/saveOrg.action',
			type: 'post',
			data: {dataList: JSON.stringify(rows)},
			dataType: 'json',
			success: function(data){
				if(data.code == 0) {
					$('#tableList').treegrid('acceptChanges');
				}
				alert(data.message);
			},
			error: function(data){ alert("ajax error"); }
		});
		
	}
	function editInfo(id) {
		var url = 'toOrgEdit.action';
		if(url != null) {
			url += ('?orgId=' + id);
		}
		showDialog('INFO', url, 'middle', true);
	}
	function delInfo(id) {
		if(!confirm('确定要删除选择的记录吗？')) {
			return;
		}
		$.ajax({
			url: '${ctx}/auth/delOrg.action',
			type: 'post',
			data: {orgId:id},
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
		$('#tableList').treegrid('reload');
	}
</script>
	<script type="text/javascript">
		var editingId;
		function edit(){
			if (editingId != undefined){
				$('#tableList').treegrid('select', editingId);
				return;
			}
			var row = $('#tableList').treegrid('getSelected');
			if (row){
				editingId = row.ORG_ID
				$('#tableList').treegrid('beginEdit', editingId);
			}
		}
		function save(){
			if (editingId != undefined){
				var ed = $('#tableList').treegrid('getEditor', {index:editingId, field:'NO_ALARM'});
				if(ed == null) {
					$('#tableList').treegrid('endEdit', editingId);
					editingId = undefined;
					return true;
				}
				var NO_ALARM_NAME = $(ed.target).combotree('getText');
				$('#tableList').treegrid('find', editingId)['NO_ALARM_NAME'] = NO_ALARM_NAME;
				
				$('#tableList').treegrid('endEdit', editingId);
				
				editingId = undefined;
			}
		}
		function cancel(){
			if (editingId != undefined){
				$('#tableList').treegrid('cancelEdit', editingId);
				editingId = undefined;
			}
		}

	</script>
<body>

<div title="" class="easyui-panel" iconCls="icon-redo" collapsible="true">
	<table id="tableList"></table>
</div>

<%@include file="/common/showDialog/showDialog_middle.jsp"%>
<%@include file="/common/showDialog/showDialog_small.jsp"%>
<%@include file="/common/showDialog/showDialog_big.jsp"%>

</body>
</html>