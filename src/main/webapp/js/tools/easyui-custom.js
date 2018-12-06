//datagrid加载数据发生错误时弹出提示
$.fn.datagrid.defaults.onLoadError = function() {
	alert('加载数据时出错！');
};

//设置easyui面板和表格的默认宽度为上级的100%
$.extend($.fn.panel.defaults, {width:'100%'});
$.extend($.fn.datagrid.defaults, {width:'100%'});

//设置AJAX默认函数，在发送前弹出遮罩，完成请求后关闭
$.ajaxSetup({
	beforeSend:function(){showWaiting(true);},
	complete:function(data){showWaiting(false);}
});
$.fn.datagrid.defaults.loadMsg = false; //上面设置了AJAX请求时显示等待消息，则把datagrid中默认的loadMsg关闭

function showWaiting(show) {
	if(show) {
		$("<div class=\"datagrid-mask\"></div>").css({display:"block",width:"100%",height:$(window).height()}).appendTo("body"); 
		$("<div class=\"datagrid-mask-msg\"></div>").html("正在处理，请稍候。。。").appendTo("body").css({display:"block",left:($(document.body).outerWidth(true) - 190) / 2,top:($(window).height() - 45) / 2});
	} else {
		$("body").children("div.datagrid-mask").remove();
		$("body").children("div.datagrid-mask-msg").remove();
	}
}

//导出EXCEL时需要用到列名及字段名，导出前调用此函数自动计算出并追加到参数选项
function setExportExcelParams(dg, _exportType, _fileName) {
	var _titles = '';
	var _fields = '';
	var _format = '';
	var _columns = dg.datagrid('options').columns[0].concat();
	for(var i in _columns) {
		_titles += ((_titles.length > 0 ? ',' : '') + _columns[i].title);
		_fields += ((_fields.length > 0 ? ',' : '') + _columns[i].field);
		if(_columns[i].formatter != null) {
			_format += ((_format.length > 0 ? ',' : '') + _columns[i].field);
		}
	}
	
	var queryParams = dg.datagrid('options').queryParams;
	queryParams._titles = _titles;
	queryParams._fields = _fields;
	queryParams._format = _format;
	queryParams._exportType = _exportType;
	queryParams._fileName = _fileName;
	queryParams._showFooter = dg.datagrid('options').showFooter;
}
