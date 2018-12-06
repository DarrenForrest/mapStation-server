//datagrid编辑框使用datebox时必须使用如下扩展
$.extend($.fn.datagrid.defaults.editors, {
	datebox : {
		init : function(container, options) {
			var input = $('<input type="text">').appendTo(container);
			input.datebox(options);
			return input;
		},
		destroy : function(target) {
			$(target).datebox('destroy');
		},
		getValue : function(target) {
			var value = $(target).datebox('getValue');//获得旧值
			if(value != null && value != '') {
				if((value + '').indexOf('-') >= 0) {
					value = new Date(value.replace(/-/g,"/"));//字符串类型为2008-04-02 10:08:44,必须转化为2008/04/02 10:08:44格式才能实例化Date对象
				}
			}
			return value;
		},
		setValue : function(target, value) {
			$(target).datebox('setValue', (value == null || value == '' ? value : new Date(value).format('yyyy-MM-dd')));//设置新值的日期格式
		},
		resize : function(target, width) {
			$(target).datebox('resize', width);
		}
	},
	datetimebox: {//datetimebox就是你要自定义editor的名称
		init: function(container, options){
			var editor = $('<input />').appendTo(container);
			editor.enableEdit = false;
			editor.datetimebox(options);
			return editor;
		},
		getValue: function(target){
			return $(target).datetimebox('getValue');
		},
		setValue: function(target, value){
			if(value)
			$(target).datetimebox('setValue',new Date(value).format("yyyy-MM-dd hh:mm:ss"));
			else
			$(target).datetimebox('setValue', null);
		},
		resize: function(target, width){
			$(target).datetimebox('resize',width);
		},
		destroy: function(target){
			$(target).datetimebox('destroy');
		}
	},
	combogrid: {
		init: function(container, options){
			var input = $('<input type="text" class="datagrid-editable-input">').appendTo(container); 
			input.combogrid(options);
			return input;
		},
		destroy: function(target){
			$(target).combogrid('destroy');
		},
		getValue: function(target){
			return $(target).combogrid('getValue');
		},
		setValue: function(target, value){
			$(target).combogrid('setValue', value);
		},
		resize: function(target, width){
			$(target).combogrid('resize',width);
		}
	}
	
});

//datagrid逐行编辑时使用下列方法比较简单，datagrid初始化后调用$('#dg').datagrid("xyInitEdit", customEditHandlerFunction);
$.extend($.fn.datagrid.methods, {
	xyBeginEdit:function(dg, index) {
		if(dg.datagrid("options").editIndex != index){
			if(dg.datagrid("xyEndEdit")){
				dg.datagrid('beginEdit', index);
				dg.datagrid("options").editIndex = index;
			} else {
				//dg.datagrid('selectRow', dg.datagrid("options").editIndex);
			}
		}
	},
	xyEndEdit: function(dg) {
		var editIndex = dg.datagrid("options").editIndex;
		if(editIndex == undefined){return true}
		if(dg.datagrid('validateRow', editIndex)){
			if(dg.datagrid('getRows')[editIndex].CHANGE_FLAG == undefined) {
				dg.datagrid('getRows')[editIndex].CHANGE_FLAG = '3';
			}
			if(dg.datagrid("options").xyEditHandler != undefined) {
				dg.datagrid("options").xyEditHandler(dg, editIndex);
			}
			dg.datagrid('endEdit', editIndex);
			dg.datagrid("options").editIndex = undefined;
			return true;
		} else {
			return false;
		}
	},
	xyInitEdit:function(dg, handler) {
		var oldOnClick = dg.datagrid("options").onClickRow;
		dg.datagrid("options").onClickRow = function(index) {
			oldOnClick(index);
			dg.datagrid("xyBeginEdit", index);
		}
		dg.datagrid("options").xyEditHandler = handler;
	}
});