<%@ page contentType="text/html;charset=UTF-8" %>

<html>
<head>
	<title>营业厅交接录入</title>	
	<%@ include file="/common/meta.jsp" %>
    <%@ include file="/common/import.jsp" %>
    


<link rel="stylesheet" href="${ctx}/plugins/zTree_v3.5.17/css/demo.css" type="text/css">   
<link rel="stylesheet" href="${ctx}/plugins/zTree_v3.5.17/css/zTreeStyle/zTreeStyle.css" type="text/css">
<script type="text/javascript" src="${ctx}/plugins/zTree_v3.5.17/js/jquery.ztree.all-3.5.js"></script>

<script src="${ctx}/plugins/artDialog4.1.7/artDialog.source.js?skin=default"></script>
<script src="${ctx}/plugins/artDialog4.1.7/plugins/iframeTools.source.js"></script>

<script type="text/javascript">
	<!-- -->
	var zTreeObj; 
	
	var setting = {
		view: {
			showLine: true,
			selectedMulti: false,
			dblClickExpand: false
		},
		data: {
			key: {
				  name:"orgName",
				  title:"",
				  url:"url"
			},
			simpleData: {
				enable: true,
				idKey:"orgId",
				pIdKey:"parentId"	
			}
		},
		callback: {
			onNodeCreated: this.onNodeCreated,
			beforeClick: this.beforeClick,
			onClick: this.onClick
		}
	};
	
	
	var zNodes = ${orgList};
			
	function onClick(event, treeId, treeNode, clickFlag) {
		
		art.dialog.data('vID',  treeNode.orgId);// 存储数据
		art.dialog.data('vName', treeNode.orgName);
		art.dialog.close();
		
		//
	}		


	$(document).ready(function(){
		zTreeObj = $.fn.zTree.init($("#orgTree"), setting, zNodes);
		
		nodes = zTreeObj.getNodes();
    	zTreeObj.expandNode(nodes[0], true, null, null, null);

	});

</script>
    
    
<body>

	<div id="left_div">
		<div class="zTreeDemoBackground left" > 	
			<ul id="orgTree" class="ztree"></ul>
		</div>			
	</div>

</body>
</html>