<%@ page contentType="text/html;charset=UTF-8" %>

<html>
<head>
	<title>执行日志查询</title>	
	<%@ include file="/common/meta.jsp" %>
    <%@ include file="/common/import.jsp" %>
    <%@ include file="/common/showDialog/showDialog_js.jsp" %>
        
<script type="text/javascript">
	var listUrl = '${ctx}/ams/getActionLogPage.action';
	
	$(function(){
		$('#tableList').datagrid({
			title:'执行日志查询',
			iconCls:'icon-ok',
			url:listUrl,
			queryParams: getQueryParams(),
			nowrap: false,
			striped: true,
			collapsible:false,
			fitColumns: true,
			pagination:true,
			singleSelect:true,
			rownumbers:true,
			remoteSort:true,
			pageSize: 50,
			pageList:[50,150,300,500],
			idField:'STAFF_ID',
			columns:[[
				{field:'WORK_DATE',			title:'工作日',	align:'left', sortable:true, width:60},
				{field:'AMS_STAFF_NAME',	title:'登录账号',	align:'left', sortable:true, width:50},      
				{field:'ADMIN_NAME',		title:'负责人',	align:'left', sortable:true, width:50},
				{field:'ACTION',			title:'动作',		align:'left', sortable:true, width:60},
				{field:'PLAN_TIME',			title:'计划时间',	align:'left', sortable:true, width:80},
				{field:'EXE_TIME',			title:'执行时间',	align:'left', sortable:true, width:80},
				{field:'EXE_CODE_NAME',		title:'状态',		align:'center', sortable:true, width:40},
				{field:'EXE_MSG',			title:'返回结果',	align:'left', sortable:true, width:60},
				{field:'EXE_ERR_MSG',		title:'报错信息',	align:'left', sortable:true, width:30},
			    //{field:'STATE_NAME',		title:'状态',		align:'center', sortable:true, width:20, hidden:false},
			    //{field:'LOCK_STATE_NAME',	title:'帐号状态',	align:'center', sortable:true, width:20, hidden:false},
				//{field:'STATE_DATE',		title:'状态日期',	align:'center', sortable:true, width:50, hidden:false, formatter:function(value, rowdata){return new Date(value).format('yyyy-MM-dd hh:mm:ss');}},
				//{field:'CREATE_DATE',		title:'创建日期',	align:'center', sortable:true, width:50, formatter:function(value, rowdata){return new Date(value).format('yyyy-MM-dd hh:mm:ss');}},
				
				{field:'op',				title:'操作'	,	align:'center', sortable:true, width:80,
					formatter: funHandle}
			]],
			onDblClickRow:function(row){
				//viewProject();
			},
			toolbar : []
		});
	});
	
	function getQueryParams() {
		var queryParams = new Object();	       
        queryParams.loginName  = $('#loginName').val();
        queryParams.staffName  = $('#staffName').val();
        queryParams.adminName  = $('#adminName').val();
        queryParams.email  	   = $('#email').val();
        
        queryParams.action   = $('#action').val();
        queryParams.exeCode  = $('#exeCode').val();
        queryParams.beginDate = $('#beginDate').val();
        queryParams.endDate  = $('#endDate').val();
        return queryParams;
	}
	function searchList(){
		$('#tableList').datagrid('options').queryParams = getQueryParams();
        $('#tableList').datagrid("load");
   	}
	function clearSearch(){
		$('#loginName').val('');
		$('#staffName').val('');
		$('#adminName').val('');
		$('#email').val('');
		
		$('#action').combotree('setValue', '-1');// 
		$('#exeCode').combotree('setValue','-1');// 
		
		searchList();
	}

	function editInfo(id) {
		var url = '${ctx}/ams/toStaffEdit.action';
		if(url != null) {
			url += ('?id=' + id);
		}
		showDialog('帐号信息维护', url, 'moremiddle', true);
	}
	function reload() {
		$('#tableList').datagrid('reload');
	}
	
	function quickSearch(){
		$('#loginName').val('');
		$('#staffName').val('');
		$('#adminName').val('');
		$('#email').val('3944');
		searchList();
	}
	
	function filterSearch(action, exeCode){
		$('#action').combotree('setValue',  action );// 
		$('#exeCode').combotree('setValue', exeCode);// 
		searchList();
	}
	
	
	function funHandle(value,rowdata){
			if(rowdata.EXE_CODE !='2'){
				// return '';
			}
		
			var html= '<a href="javascript:void(0);" onclick="gotoAms(&#39;' + rowdata.ENCRYPT_LOGIN_NAME + '&#39;,&#39;' + rowdata.ENCRYPT_PASSWORD +'&#39;)">登录AMS系统</a>' +
			          '&nbsp;&nbsp;' +
			       //   '<a href="javascript:void(0);" onclick="handle(&#39;' + rowdata.LOGIN_NAME + '&#39;,&#39;' + rowdata.PASSWORD + '&#39;,&#39;staffLogin&#39;)">签入</a>' +
			       //   '&nbsp;&nbsp;' +
			       //   '<a href="javascript:void(0);" onclick="handle(&#39;' + rowdata.LOGIN_NAME + '&#39;,&#39;' + rowdata.PASSWORD + '&#39;,&#39;staffLoginout&#39;)">签出</a>' +
			       //   '&nbsp;&nbsp;' +
			          '<a href="javascript:void(0);" onclick="handle(&#39;' + rowdata.LOGIN_NAME + '&#39;,&#39;' + rowdata.PASSWORD + '&#39;,&#39;queryWorkTime&#39;)">补签</a>' +
			          '';
			return html;
	}
	
	function gotoAms(encryptUserName, encryptPassword){
		var url = "http://10.208.230.98:7001/ams/loginAction.do?method=login&optrid=" + encodeURIComponent(encryptUserName) + "&password=" + encodeURIComponent(encryptPassword) + "&imageField.x=32&imageField.y=13";
		showDialog('登录AMS系统', url, 'big', true);
	}
	
	function handle(name,password,action){
		var url = '${ctx}/ams/' + action + '.action';
		
		$.ajax({
			url: url,
			type: 'post',
			data: {'loginName':name,'password':password},
			dataType: 'json',
			success: function(data){
				if(data.code == 0) {
					//parent.closeDialog();
				}
				alert(data.message);
			},
			error: function(data){ alert("ajax error"); }
		});
	}
	
</script>
</head>
<body>

<div title="AMS工号查询" id="tab_search" class="easyui-panel" 
	 iconCls="icon-search"	collapsible="true" collapsed="false" title="查询搜索">
	<form id="formadd" name="formadd" method="post">
		<table class="datagrid-body" width="100%" >
			<tr>
				<td class="datagrid-header" onclick="quickSearch();">帐号</td>
				<td colspan="1"> 
					<input id="loginName" name="loginName" class="easyui-text" data-options="" value="" />
				</td>
				<td class="datagrid-header">姓名</td>
				<td colspan="1"> 
					<input id="staffName" name="staffName" class="easyui-text" data-options="" value="" />
				</td>
				<td class="datagrid-header">负责人</td>
				<td colspan="1"> 
					<input id="adminName" name="adminName" class="easyui-text" data-options="" value="" />
				</td>
				<td class="datagrid-header">邮箱</td>
				<td colspan="1"> 
					<input id="email" name="email" class="easyui-text" data-options="" value="" />					
				</td>
			</tr>
			
			<tr>
				<td class="datagrid-header" onclick="quickSearch();">动作</td>
				<td colspan="1"> 
					<input id="action" name="action" class="easyui-combotree" value="-1"
						data-options="url:'${ctx}/auth/getTreeData.action?catalog=enumCfg&type=tb_enum_catalog.action',method:'get',onChange:function(n,o){$(this).val(n);},required:false"  />
				</td>
				<td class="datagrid-header">状态</td>
				<td colspan="1"> 
							<input id="exeCode" name="exeCode" class="easyui-combotree" value="-1"
						data-options="url:'${ctx}/auth/getTreeData.action?catalog=enumCfg&type=tb_ams_action_log.exe_code',method:'get',onChange:function(n,o){$(this).val(n);},required:false"  />
				</td>
				<td class="datagrid-header">日期</td>
				<td colspan="3"> 
					<input type="text" id="beginDate" name="beginDate" class="Wdate" value="${beginDate}"
						   onclick="WdatePicker({isShowClear:true,readOnly:true,dateFmt:'yyyy-MM-dd'})"	 />
						 至
					<input type="text" id="endDate" name="endDate" class="Wdate" value="${endDate}"
						   onclick="WdatePicker({isShowClear:true,readOnly:true,dateFmt:'yyyy-MM-dd'})"	/>
				</td>
			</tr>
	
			<tr style="height: 35px;">
				<td colspan="8" align="center">
					<a class="easyui-linkbutton" iconCls="icon-search"	onclick="searchList()">查 询</a> 
						&nbsp;&nbsp;&nbsp;	
					<a class="easyui-linkbutton" iconCls="icon-reload"	onclick="clearSearch();">清 空</a>
				</td>
			</tr>
			
			<tr style="height: 35px;">
				<td colspan="8" align="center">
					<a class="easyui-linkbutton" iconCls="icon-search"	onclick="filterSearch('login','-1')">签入查询</a> 
						&nbsp;
					<a class="easyui-linkbutton" iconCls="icon-search"	onclick="filterSearch('login','2')">签入失败</a> 
						&nbsp;	
					<a class="easyui-linkbutton" iconCls="icon-search"	onclick="filterSearch('workloginout','-1');">签出查询</a>
						&nbsp;
					<a class="easyui-linkbutton" iconCls="icon-search"	onclick="filterSearch('workloginout','2')">签出失败</a> 

				</td>
			</tr>
		</table>
	</form>
</div>

<br/>
<div title="" class="easyui-panel" iconCls="icon-redo" collapsible="true">
	<table id="tableList"></table>
</div>

<%@include file="/common/showDialog/showDialog_middle.jsp"%>
<%@include file="/common/showDialog/showDialog_small.jsp"%>
<%@include file="/common/showDialog/showDialog_big.jsp"%>

</body>
</html>