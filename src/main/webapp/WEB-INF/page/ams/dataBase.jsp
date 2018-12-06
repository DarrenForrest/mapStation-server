<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<!DOCTYPE html>
<html>
<head>
<title>数据库备份</title>	
<meta charset="utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE8" />
	<%@ include file="/common/meta.jsp" %>
	<%@ include file="/common/import.jsp" %>
<script src="${ctx}/plugins/layui/layui.js"></script>
<script src="${ctx}/plugins/layer/layer.js"></script>
<script type="text/javascript">
	//定义全局使用的layer样式
	layer.config({
		skin : 'layui-layer-ext'
	});
</script>	
</head>
<body>
	<div class="panel-border panel-edit">
   		<div   >
            <div title="数据库备份" id="tab_search" class="easyui-panel" collapsible="true" collapsed="false" >
            	<table border="1"  class="datagrid-body" >
                	<tbody>
                           <tr>
                               <th class="datagrid-header" style="width: 90px;" >主机IP：</th>
                               <td>
                               	<div  style="width: auto;height: 40px;" >
                                  <input type="text" class="notice-txt" name="IP"  id="sendToUser"  onclick="showResourceIp(this);"  style="text-align: center;width: 100%;height: 40px;"  >
                               </div>
                               </td>
                           </tr>
                            <tr>
                               <th class="datagrid-header"  >数据库名称：</th>
                               <td>
                               	<div  style="width: auto;height: 40px;" >
                                  <input type="text" class="notice-txt"  id="dataName"  name="dataName" style="width: 100%;height: 40px;text-align: center;"  >
                               </div>
                               </td>
                           </tr>
                            <tr>
                               <th class="datagrid-header"  >数据库端口：</th>
                               <td>
                               	<div  style="width: auto;height: 40px;" >
                                  <input type="text" class="notice-txt"  id="port"  name="port" style="width: 100%;height: 40px;text-align: center;"  >
                               </div>
                               </td>
                           </tr>
                           <tr>
                               <th class="datagrid-header"  >数据库用户：</th>
                               <td>
                               	<div  style="width: auto;height: 40px;" >
                                  <input type="text" class="notice-txt"  id="dataUser"  name="dataUser" style="width: 100%;height: 40px;text-align: center;"  >
                               </div>
                               </td>
                           </tr>
                           <tr>
                               <th class="datagrid-header"  >数据库密码：</th>
                               <td>
                               	<div  style="width: auto;height: 40px;" >
								<input type="text" class="notice-txt"  id="dataPwd" name="dataPwd" style="width: 100%;height: 40px;text-align: center;"  >
								</div>
                               </td>
                           </tr>
                            <tr>
                               <th class="datagrid-header"  >数据库所在路径：</th>
                               <td>
                               	<div  style="width: auto;height: 40px;" >
								<input type="text" class="notice-txt"  id="dataAddr" name="dataAddr" style="width: 100%;height: 40px;text-align: center;"  >
								</div>
                               </td>
                           </tr>
                           <tr>
                            <th class="datagrid-header"  >定时：</th>
                            <td>
                            	<div  style="width: auto;height: 15px; " >
								<select id="timechouse"  name="time"  style="width: 150px;" >
								    <option value="0" >&nbsp;&nbsp;&nbsp;---请选择---</option>
									<option value="1" >&nbsp;&nbsp;&nbsp;&nbsp;每分钟</option>
									<option value="2" >&nbsp;&nbsp;&nbsp;&nbsp;每两分钟</option>
								</select>
								</div>
                            </td>
                           </tr>
                           <tr>
                               <th class="datagrid-header" ></th>
                               <td>
                                   <div class="edit-btn" style="width: 500px;margin-left: 150px;" >
                                       <button class="btn btn-red btn-md" onclick="buttonPublish()">备份</button>
                                       &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                       <input type="reset"  class="btn btn-gray btn-md"  value="取消" />
                                   </div>
                               </td>
                           </tr>
                       </tbody>
                </table>
               
            </div>               
        </div>
   	</div> 	
    <div id="addrDiv" style="overflow-y:auto" class="panel-body panel-content"  >
	    <table id="userListTable" class="datagrid-body" width="100%" cellspacing="1" cellpadding="0" align="center" >
	    </table>
   	</div>
</body>
<script type="text/javascript">
function showResourceIp(obj){
	$.ajax({
		"url" : "${ctx}/host/getHostList.action",
		"type" : "POST",
		dataType : "json",
		success : function(data) {
			$("#userListTable").empty();
			$("#userListTable").html("");
			var list = data.data;
			var html="";
			html+="<tr>";
			html+="<th ></th>";
			html+="<th >主机IP</th>";
			html+="<th >名称</th>";
			html+="</tr>";
			if(list.length>0){
				for(var i=0;i<list.length;i++){
					html+="<th class='datagrid-header' ><input name='one' type='radio'  value='"+list[i].IP+"'  ><input type='hidden' id='"+list[i].IP+"' value='"+list[i].BACKUP_HOST+"' ></th>";
					html+="<th class='datagrid-header'  name='id' style='width:150px;' >"+list[i].IP+"</th>";
					html+="<th name='name' class='datagrid-header' >"+list[i].NAME+"</th>";
					html+="</tr>";
				}
			}else{
				html="<tr><th colspans='8' align='center'>未查找到</th></tr>"
			}
			$("#userListTable").append(html);
		}
	});
	layer.open({
		type : 1,
		title : '&nbsp;选择主机IP',
		shadeClose: true,
		content : $("#addrDiv"),
		area : ['300px', '450px'],
		btn : [ '确定', '取消' ],
		btn1 : function(index, layero) {// 确定按钮回调
			var check=$('input[type="radio"]:checked').val();//选中的复选框
			$("#sendToUser").attr("value",check);
			var status=$('input[type="radio"]:checked').parent().find("input[type='hidden']").val();
			layer.close(index);
		},
		btn2 : function(index, layero) {// 确定按钮回调
			layer.close(index);
		},
		end : function() {
		}
	
	});
}

//
var buttonPublish = function(){	

	if($('#sendToUser').val() === ''){
		layer.msg('主机IP不能为空！', {icon:1});
	}else if($('#dataName').val() === ''){
		layer.msg('数据库名不能为空！', {icon:1});
	}else if($('#dataPwd').val() === ''){
		layer.msg('数据库密码不能为空！', {icon:1});
	}else if($('#dataUser').val() === ''){
		layer.msg('用户名不能为空！', {icon:1});
	}else if($('#dataAddr').val()==''){
		layer.msg('数据库路径不能为空！', {icon:1});
	}
	var ip = $("#sendToUser").val();
	var dataName =$("#dataName").val();
	var dataPwd = $("#dataPwd").val();
	var dataUser=$("#dataUser").val();
	var port =$("#port").val();
	var dataAddr =$("#dataAddr").val();
	var options=$("#timechouse option:selected");
	var time=options.val();
	$.ajax({
		url:'${ctx}/host/pubMessage.action',
		"type":"POST",
		"dataType":"json",
		data : {
			ip:ip,
			dataName : dataName,
			dataPwd :dataPwd,
			dataUser : dataUser,
			port : port,
			dataAddr : dataAddr,
			time : time
		},
		success:function(data){
			layer.msg('备份成功！', {time: 1000, icon:1});
			window.location.reload();
		}
   });
};


</script>
</html>