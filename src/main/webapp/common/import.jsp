<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix='fmt' uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@page import="com.bonc.common.Auth"  %>

<%
	Auth auth = new Auth();
	auth = (Auth)request.getSession().getAttribute("auth");
	
	String optionOrgId = "0";
	if( auth != null)
	{
		optionOrgId = auth.getOptionOrgId();
	}
	
%>

<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="MaxExcelExportCnt" value="5000"/>
<c:set var="optionOrgId" value="${optionOrgId}"/>

<!-- css import -->
<link rel="shortcut icon" type="image/x-icon" href="${ctx}/favicon.ico" />
<link rel="stylesheet" type="text/css" href="${ctx}/js/easyui/themes/default/easyui.css" />
<link rel="stylesheet" type="text/css" href="${ctx}/js/easyui/themes/icon.css" />
<link href="${ctx}/css/gray.css" type="text/css" rel="stylesheet" />


<!-- js import -->
<script type="text/javascript" src="${ctx}/js/easyui/jquery.min.js"></script>
<script type="text/javascript" src="${ctx}/js/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${ctx}/js/easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="${ctx}/plugins/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="${ctx}/js/accounting.js"></script>
<script type="text/javascript" src="${ctx}/js/jQuery.formatMoney.js?v=1"></script>
<script type="text/javascript" src="${ctx}/js/jQuery.formatDate.js?v=2"></script>

<script type="text/javascript" src="${ctx}/js/tools/common.js"></script>
<script type="text/javascript" src="${ctx}/js/tools/dateutils.js"></script>
<script type="text/javascript" src="${ctx}/js/tools/easyui-custom.js"></script>
<script type="text/javascript" src="${ctx}/js/tools/easyui-datagrid.js"></script>

<script type="text/javascript" src="${ctx}/plugins/artDialog4.1.7/artDialog.source.js?skin=default"></script>
<script type="text/javascript" src="${ctx}/plugins/artDialog4.1.7/plugins/iframeTools.source.js"></script>

<!--[if lt IE 9]>
    <script src="${ctx}/js/json2.js"></script>
<![endif]-->

<script type="text/javascript">    
	$(function(){
		$('#orgId').combotree({   
			panelHeight:400,
			panelWidth:400
		});
	});
	
	function cpInput(inID, outID){
		$('#' + outID).val( $('#' + inID).val() );
	}
</script> 	


