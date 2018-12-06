<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<!DOCTYPE html>
<html>
<head>
<title>备份</title>	
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
            <div title="脚本备份" id="tab_search" class="easyui-panel" collapsible="true" collapsed="false" title="查询搜索">
            	<table border="1"  class="datagrid-body" >
                	<tbody>
                           <tr>
                               <th class="datagrid-header" style="width: 90px;" >源主机：</th>
                               <td>
                               	<div  style="width: auto;height: 40px;" >
                                  <input type="text" class="notice-txt" name="IP"  id="sendToUser"  onclick="showResourceIp(this);"  style="text-align: center;width: 100%;height: 40px;"  >
                               </div>
                               </td>
                           </tr>
                           <tr>
                               <th class="datagrid-header"  >项目：</th>
                               <td>
                               	<div  style="width: auto;height: 40px;" >
                                  <input type="text" class="notice-txt"  id="projectManage"  onclick="showProjectManage(this);" style="width: 100%;height: 40px;text-align: center;"  >
                               </div>
                               </td>
                           </tr>
                           <tr>
                               <th class="datagrid-header"  >目录：</th>
                               <td>
                               	<div  style="width: auto;height: 40px;" >
								<input type="text" class="notice-txt"  id="catalogManage"  onclick="showCatalogManage(this);" style="width: 100%;height: 40px;text-align: center;"  >
								  <input type="hidden" class="notice-txt"  name="userIds" id=""   >
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
									<option value="2" >&nbsp;&nbsp;&nbsp;&nbsp;每天</option>
								</select>
								</div>
                            </td>
                           </tr>
                           <tr>
                               <th class="datagrid-header" >选择文件：</th>
                               <td>
                            		<div  id="fileList" style="width:100%; height: 300px;overflow-y:auto"   class="panel-body common-content" >
                            		    <table id="fileListsTable" class="datagrid-body" width="100%" cellspacing="1" cellpadding="0" align="center" border="1" >
	    								</table>
                            		</div>
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
		title : '&nbsp;选择源主机',
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
function showProjectManage(obj){
	$.ajax({
		"url" : "${ctx}/host/getProjectManage.action",
		"type" : "POST",
		dataType : "json",
		success : function(data) {
			$("#userListTable").empty();
			$("#userListTable").html("");
			var list = data.data;
			var html="";
			html+="<tr>";
			html+="<th ></th>";
			html+="<th >项目名</th>";
			html+="<th >编码</th>";
			html+="</tr>";
			if(list.length>0){
				for(var i=0;i<list.length;i++){
					html+="<th class='datagrid-header' ><input name='one' type='radio'  value='"+list[i].CODE+"'  ></th>";
					html+="<th class='datagrid-header' name='id' style='width:150px;' >"+list[i].NAME+"</th>";
					html+="<th  class='datagrid-header' name='name' >"+list[i].CODE+"</th>";
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
		title : '&nbsp;所属项目',
		shadeClose: true,
		content : $("#addrDiv"),
		area : ['300px', '450px'],
		btn : [ '确定', '取消' ],
		btn1 : function(index, layero) {// 确定按钮回调
			var check=$('input[type="radio"]:checked').val();//选中的复选框
			$("#projectManage").attr("value",check);
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
function showCatalogManage(obj){
	var ip = $("#sendToUser").val();
	$.ajax({
		"url" : "${ctx}/host/getCatalogManageList.action",
		"type" : "POST",
		dataType : "json",
		data : {"ip" : ip},
		success : function(data) {
			$("#userListTable").empty();
			$("#userListTable").html("");
			var list = data.data;
			var html="";
			html+="<tr>";
			html+="<th ></th>";
			html+="<th >所属主机</th>";
			html+="<th >路径</th>";
			html+="</tr>";
			if(list.length>0){
				for(var i=0;i<list.length;i++){
					html+="<th class='datagrid-header' ><input name='one' type='radio'  value='"+list[i].path+"'  ></th>";
					html+="<th class='datagrid-header'  name='id' style='width:150px;' >"+list[i].ip+"</th>";
					html+="<th class='datagrid-header' name='name' >"+list[i].path+"</th>";
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
		title : '&nbsp;选择文件路径',
		shadeClose: true,
		content : $("#addrDiv"),
		area : ['300px', '450px'],
		btn : [ '确定', '取消' ],
		btn1 : function(index, layero) {// 确定按钮回调
			var check=$('input[type="radio"]:checked').val();//选中的复选框
			$.ajax({
				"url" : "${ctx}/hostInfo/catalogManageReg.action",
				"type" : "POST",
				dataType : "json",
				data : {"ip" : ip,"address" : check},
				success : function(data) {
					var list=data.data;
					if(list.length!=0){
						if(window.confirm('该目录中存在备份文件，是否继续选择此目录')){
			                 //alert("确定");
			                 $("#catalogManage").attr("value",check);
							 layer.close(index);
							 showManage();
			                 return true;
			              }else{
			                 //alert("取消");
			                 return false;
			             }
					}else{
						$("#catalogManage").attr("value",check);
						layer.close(index);
						showManage();
					}
				}
			});
		},
		btn2 : function(index, layero) {// 确定按钮回调
			layer.close(index);
		},
		end : function() {
		}
	
	});
}

function showManage(){
	var ip=$("#sendToUser").val();
	var project=$("#projectManage").val();
	var catalog=$("#catalogManage").val();
	$.ajax({
		"url" : "${ctx}/host/getList.action",
		"type" : "POST",
		dataType : "json",
		data : {ip : ip,project : project, catalog : catalog },
		success : function(data) {
			$("#fileListsTable").empty();
			$("#fileListsTable").html("");
			var list = data.data;
			var html="";
			html+="<caption><h4>文件目录</h4></caption><tr>";
			html+="<th><input type='checkbox' id='chElt' onclick='checkOrCancelAll();' ><span id='mySpan'>全选</span></th>";
			html+="<th>文件名</th>";
			html+="<th>所属用户</th>";
			html+="<th>所属组</th>";
			html+="<th>更改时间</th>";
			html+="<th>类型</th>";
			html+="</tr>";
			if(list.length>0){
				for(var i=0;i<list.length;i++){
					html+="<tr><td><input name='interest' type='checkbox' value="+list[i].fileName+','+list[i].fileType+" ></td><td><input type='text' class='datagrid-header' readonly='readonly' name='fileName' value='"+list[i].fileName+"' style='text-align: center;'  ></td>";
					html+="<td  ><input class='datagrid-header' name='Buser' readonly='readonly' type='text' value='"+list[i].buser+"' style='text-align: center;' ></td><td ><input name='bgroup' class='datagrid-header' readonly='readonly'  type='text' value='"+list[i].bgroup+"' style='text-align: center;' ></td>";
					html+="<td ><input name='createTime' class='datagrid-header' readonly='readonly'  type='text' value='"+list[i].createTime+"' style='text-align: center;' ></td>";
					html+="<td><input name='fileType' class='datagrid-header' readonly='readonly'  type='text' value='"+list[i].fileType+"' style='text-align: center;' ></td>";
					html+="</tr>";
				}
			}else{
				html="<tr><th colspans='8' align='center'>未查找到</th></tr>"
			}
			$("#fileListsTable").append(html);
		}
	});
}

//发布公告
var buttonPublish = function(){	
	var fileName="";
	var check=$("input[name='interest']:checked");//选中的复选框
	check.each(function(){
		fileName+=$(this).val()+";";
	});
	
	if($('#sendToUser').val() === ''){
		layer.msg('主机IP不能为空！', {icon:1});
	}else if($('#catalogManage').val() === ''){
		layer.msg('目录不能为空！', {icon:1});
	}
	var ip = $("#sendToUser").val();
	var projectManage =$("#projectManage").val();
	var catalogManage = $("#catalogManage").val();
	var fileType=$("#fileType").val();
	var options=$("#timechouse option:selected");
	var time=options.val();
	$.ajax({
		url:'${ctx}/host/pubMessage.action',
		"type":"POST",
		"dataType":"json",
		data : {
			ip:ip,
			projectManage : projectManage,
			catalogManage :catalogManage,
			fileName : fileName,
			fileType : fileType,
			time : time
		},
		success:function(data){
			window.location.reload();
		}
   });
};

//页面加载的时候,所有的复选框都是未选中的状态
function checkOrCancelAll(){
		//1.获取checkbo的元素对象
		var chElt=document.getElementById("chElt");
		//2.获取选中状态
		var checkedElt=chElt.checked;
		//3.若checked=true,将所有的复选框选中,checked=false,将所有的复选框取消
		var allCheck=document.getElementsByName("interest");
		//4.循环遍历取出每一个复选框中的元素
		var mySpan=document.getElementById("mySpan");
		if(checkedElt){
		//全选
		for(var i=0;i<allCheck.length;i++){
		//设置复选框的选中状态
		allCheck[i].checked=true;
		}
		mySpan.innerHTML="取消全选";
		}else{
		//取消全选
		for(var i=0;i<allCheck.length;i++){
			if(allCheck[i].checked=false){
				allCheck[i].checked=true;
			}else{
				allCheck[i].checked=false;
			}		
		}
		mySpan.innerHTML="全选";
		}
}

</script>
</html>