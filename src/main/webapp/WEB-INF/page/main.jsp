<%@ page contentType="text/html;charset=UTF-8"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>

<%
	String p2p_server = "";// SystemTools.getInstance().getValue("p2p_server");
	String p2p_web_server = "";// SystemTools.getInstance().getValue("p2p_web_server");
	String balance_server = "";//SystemTools.getInstance().getValue("balance_server");
	String uct_server = "";//Common.getValueByProperty("uct_server");
	String login_url = "";//Common.getValueByProperty("login_url");
	String login_out_url = "";//Common.getValueByProperty("login_out_url");

/*	Auth auth = CommonUtil.getLoginUser(request);
	LoginUser loginUser =null;
	
	if(auth == null){ 
		response.sendRedirect(login_out_url);
	}
	Map<String, String> cookieMap = CookieTools.getInfoFromCookie(request);
	if(cookieMap != null){
		String token = cookieMap.get("sessionId");
		Map<String,String> map=new HashMap<String,String>();
		map.put("user.userEname", cookieMap.get("userEname").trim());
		map.put("token", token);
		Root root=UctCoreClient.getLoginUserInfo(map);
		if(WsConstant.RESULT_SUCCESS.equals(root.getResult())){
			loginUser=(LoginUser)root.getEntity();
		}
	}
*/	
%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>百度地图服务系统</title>
<%@ include file="/common/meta.jsp"%>
<%@ include file="/common/import.jsp"%>



<script>

$.extend($.fn.layout.methods, {
	full : function (jq) {
		return jq.each(function () {
			var layout = $(this);
			var center = layout.layout('panel', 'center');
			center.panel('maximize');
			center.parent().css('z-index', 10);

			$(window).on('resize.full', function () {
				layout.layout('unFull').layout('resize');
			});
		});
	},
	unFull : function (jq) {
		return jq.each(function () {
			var center = $(this).layout('panel', 'center');
			center.parent().css('z-index', 'inherit');
			center.panel('restore');
			$(window).off('resize.full');
		});
	}
});
function full() {
	$("body").layout("full");
}
function unFull() {
	$("body").layout("unFull");
}
function refresh(){
	var pp = $('#main').tabs('getSelected');//选中的选项卡对象 
	var content = pp.panel('options').content;//获取面板内容  
	//console.log(tab); 
	$("#main").tabs('update',{//更新
		tab:pp,
		options:content
	})
}


function addTab2(title,url) {
	addTabBase(title,url);
}
function addTab(myobj,url) {
	var title = myobj.innerHTML;
	addTabBase(title,url);
}

function addTabBase(title,url) {
	if ($('#main').tabs('exists', title)) {
		$('#main').tabs('close', title);
	}
	//console.log(url);
	var content = '<iframe scrolling="auto" frameborder="0"  src="' + url + '" style="width:100%;height:99%;"></iframe>';
	$('#main').tabs('add', {
		title : title,
		iconCls : '',
		content : content,
		closable : true
	});
	  //禁止windows自带的右键点击事件
    document.oncontextmenu = function(){return false;};
    //右键点击事件
    $(".tabs-inner").bind('contextmenu',function(e){
		$('#mm').menu('show', {
			left: e.pageX,
			top: e.pageY
		});
		var subtitle =$(this).children("span").text();
      $('#main').data("currtab",subtitle);
		return false;
	});
}


$(document).ready(function() {
	//addTab('项目管理','./pro-list.html','icon-user');		//初始化第一个面板
	//$('#main').load("pro-list.html");//初始化中央区域
	if (self != top) {
		top.location = self.location;
	}
	$("#left h3").click(function(){
		
		$(this).addClass("ll").siblings().removeClass("ll");
	})
	$("h3").attr("style","cursor:hand");
	tabCloseEven();
	$('#subnav li').click(function(){ 
		$(this).addClass("ll").siblings().removeClass("ll"); 
	});
	
});
 
function logout(){
	$.messager.confirm('提示', '您确定工作已经保存，并退出该系统?', function(r){
				if (r){					
					 toLogout();
				}
			});
}

function toLogout(){
	var url = '${ctx}/logout.action'; 
	$.ajax({
		type : "post",
		async : false,
		url : url,
		data : '',
		dataType: "json",
	    success:function(data){
	    	if(data.code == '0')
	    	{ 
				window.location.href = data.url;
			} else {
				$.messager.alert('提交结果', result.message, 'info');
			}
		},
		error : function(data) {
			$.messager.alert('提交结果', 'error', 'error');
		}
	});
}

	//修改密码
	function updatePwd() {
		addTab2('修改密码', '${ctx}/common/beingDeveloped.jsp')
	}

	function tabCloseEven() {

		var defaultTitle = "系统公告";
		//关闭当前
		$('#mm-tabclose').click(function() {
			var tt = $('#main').data("currtab");
			$('#main').tabs('select', tt);
			if (tt == defaultTitle) {
				$.messager.alert('信息提示', "不能关闭默认tab页", 'info', function() {
					return false;
				});
			} else {
				$('#main').tabs('close', tt);
			}
		})
		//全部关闭
		$('#mm-tabcloseall').click(function() {
			var tt = $('#main').data("currtab");
			$('#main').tabs('select', tt);

			$('.tabs-inner span').each(function(i, n) {
				var t = $(n).text();
				if (t != defaultTitle) {
					$('#main').tabs('close', t);
				}
			});
		});
		//关闭除当前之外的TAB
		$('#mm-tabcloseother').click(function() {
			var tt = $('#main').data("currtab");
			$('#main').tabs('select', tt);

			$('.tabs-inner span').each(function(i, n) {
				var t = $(n).text();
				if (t != tt && t != defaultTitle) {
					$('#main').tabs('close', t);
				}
			});
		});
		//关闭当前右侧的TAB
		$('#mm-tabcloseright').click(function() {
			var tt = $('#main').data("currtab");
			$('#main').tabs('select', tt);

			var nextall = $('.tabs-selected').nextAll();
			if (nextall.length == 0) {
				$.messager.alert('信息提示', "右侧没有可以关闭的tab页", 'info', function() {
					return false;
				});
			}
			nextall.each(function(i, n) {
				var t = $('a:eq(0) span', $(n)).text();
				if (t != defaultTitle) {
					$('#main').tabs('close', t);
				}
			});
			return false;
		});
		//关闭当前左侧的TAB
		$('#mm-tabcloseleft').click(function() {
			var tt = $('#main').data("currtab");
			$('#main').tabs('select', tt);

			var prevall = $('.tabs-selected').prevAll();
			if (prevall.length <= 0) {
				$.messager.alert('信息提示', "左侧没有可以关闭的tab页", 'info', function() {
					return false;
				});
			}
			prevall.each(function(i, n) {
				var t = $('a:eq(0) span', $(n)).text();
				if (t != defaultTitle) {
					$('#main').tabs('close', t);
				}
			});
			return false;
		});
		//全屏
		$('#mm-full').click(function() {
			full();
		});
		//取消全屏
		$('#mm-unfull').click(function() {
			unFull();
		});
	}
</script>
</head>

<body class="easyui-layout">
	<div id="top" region="north" border="false"
		style="overflow-x: hidden; overflow-y: hidden; margin-left: 0px; height: 65px; margin-top: 0px; color: #000000;">
		<div
			style="float: left; text-align: center; overflow: hidden; height: 65px; width: 200px; background: #fff;">
			<div>
				<img src="${ctx}/images/regulator/logo.png">
			</div>
		</div>
		<div style="margin-left: 200px;height: 65px;background: url('${ctx}/images/regulator/top_banner.png') repeat-x center center ">
<!-- 
			<div
				style="float: left; margin: 28px 0px 0px 20px; height: 31px;width:346px; background:url('${ctx}/images/regulator/top_title.png') no-repeat center center;">
			</div>
-->			
			<div
				style="float: right; height: 57px; margin-top: 3px; text-align: center; margin-right: 20px; line-height: 39px; overflow: hidden;">
				<span style="font-size:25px; color:#f0f0f0" >欢迎您 ${staffName} </span>
				<span class="exit"><a href="javascript:void(0);"
					onclick="javascript:logout()" style="cursor: pointer"><img
						src="${ctx}/images/regulator/exit.png"></a></span>
			</div>
<!-- 			
			<div
				style="position: relative; right: 0px; top: 50px; width: 450px; height: 30px;">
				<iframe src="${ctx}/common/beingDeveloped.jsp" scrolling="no"
					width="100%" frameborder="0" height="30" marginwidth="0"
					marginheight="0" hspace="0" vspace="0" frameborder="0"
					scrolling="no" class="tianqi" allowtransparency="true">
				</iframe>
			</div>
 -->
		</div>
	</div>

	<div region="west" split="true" style="width: 200px; overflow: auto;">
		<div class="easyui-accordion" id="left" fit="true" border="true">
			
			<c:forEach items="${menuList}" var="i" varStatus="iStatus">
				<div title="${i.data.RULE_NAME}" style="overflow: auto;" iconCls="icon-system">
					<c:forEach items="${i.children}" var="j" varStatus="jStatus">
					<c:choose>
						<c:when test="${fn:contains(j.data.URL, '?')}">
							<h3 onClick="addTab(this,'${j.data.URL}&ruleId=${j.data.RULE_ID}');">${j.data.RULE_NAME}</h3>
						</c:when>
						<c:otherwise>
							<h3 onClick="addTab(this,'${j.data.URL}?ruleId=${j.data.RULE_ID}');">${j.data.RULE_NAME}</h3>
						</c:otherwise>
					</c:choose>
					</c:forEach>
				</div>
			</c:forEach>
			
		</div>

	</div>


	<div region="center" title="" style="overflow: hidden;">

		<div id="main" class="easyui-tabs" data-options="tools:'#tab-tools'" fit="true" border="true"
			style="background: #fff;">
			<div class="main" title="公告&消息"
				style="position: relative; background: #fff;">
				<iframe src="${ctx}/cfg/noticeList.action" width="100%" height="100%"></iframe>

			</div>
		</div>

<div id="tab-tools">
<a href="javascript:void(0)" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-full'" onclick="full();"></a>
<a href="javascript:void(0)" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-unfull'" onclick="unFull();"></a>
</div>

	</div>

	<div region="south" border="false" class="bottom"
		style="background: #EEE;"></div>

	<div id="ccc" region="center" title="">
		<div id="main"></div>
	</div>

	<div id="mm" class="easyui-menu" style="width: 150px;">
		<div id="mm-tabclose">关闭</div>
		<div id="mm-tabcloseall">全部关闭</div>
		<div id="mm-tabcloseother">除此之外全部关闭</div>
		<div class="menu-sep"></div>
		<div id="mm-tabcloseright">当前页右侧全部关闭</div>
		<div id="mm-tabcloseleft">当前页左侧全部关闭</div>
		<div id="mm-full">全屏</div>
		<div id="mm-unfull">取消全屏</div>
	</div>
	<%
		if(false){
	%>
<style type="text/css">
.div_overlay {
	position: absolute;
	top: 0%;
	left: 0%;
	background-color: #000;
	opacity: 0.6;
	z-index: 999;
	display: none;
}

.changepwd_tip {
	height: 44px;
}

.changepwd_tip .worn_info {
	margin: 0px;
	height: 24px;
	line-height: 24px;
	padding-top: 10px;
	text-align: center;
	color: #FF0000;
}


</style>
	<script type="text/javascript" src="${ctx}/js/Toast.js"></script>
	<div id="screen" class="div_overlay"></div>
	<div id="change_pwd" class="window" style="z-index: 9999">
		<div class="panel-header panel-header-noborder window-header" >
			<div class="panel-title">修改登录密码</div><div class="panel-tool"><a class="panel-tool-close" href="javascript:void(0)"
				onclick="javascript:window.parent.toLogout()"></a></div>
		</div>
		<div class="panel-body  dialog-content">
		<div class="changepwd_tip">
			<p class="worn_info">为确保账户安全，首次登录时必须修改初始登录密码</p>
		</div>
		<div>
			<iframe id="changepwdiframe" width="600" height="260" marginwidth="0"
				marginheight="0" hspace="0" vspace="0" frameborder="0"
				scrolling="no" class="tianqi" allowtransparency="true"></iframe>
		</div>
	</div>
	</div>
	<script type="text/javascript">
		$(function() {
			var iframe = document.getElementById("changepwdiframe");
			var src = "${ctx}/admin/user/toUserPwsdUpdate.action?r="
					+ Math.random();
			iframe.src = src;
			toolTipDialog('change_pwd', 600, 304);
		});
	</script>
	<%
		}
	%>
</body>
</html>
