<%@ page contentType="text/html;charset=UTF-8" %>
<script type="text/javascript">
	var _dg = null;
	var _cb = null;
	var _cols = null;
	var _colsDefault = null;
	var _rows = null;
	
	//注意：datagrid不能使用reload来查询，改用重新赋值url的方式，否则动态增减列有BUG
	//使用说明：
	//  1. 在页面内导入groupby-selection.jsp
	//  2. 在初始化datagrid之后调用initGroupByEnv($('#datagrid'), searchFunction)
	//  3. 查询时传递两个参数：aggregateColumn和partitionColumn
	
	//对外API：
	//initGroupByEnv      初始化
	//getAggregateString  获取聚合子句字符串
	//getPartitionString  获取分区子句字符串
	
	//col.columnType: count sum min max group express
	//col.columnExpress: custom express
	//col.columnSql: new column's aggregate function sql
	//col.title: new column's title
	//col.field: new column's field
	
	var columnType = [
	                  {type:'group', text:'分组'},
	                  {type:'count', text:'计数'},
	                  {type:'sum'  , text:'合计'},
	                  {type:'min'  , text:'最小'},
	                  {type:'max'  , text:'最大'}
	                  ];
	var unselectColumns = null;
	
	function initGroupByEnv(dg, func) {
		_dg = dg;
		_cb = func;
		_cols = null;
		_colsDefault = dg.datagrid('options').columns[0].concat();
		unselectColumns = dg.datagrid('options').columns[0].concat();
		
		initGroupbySettingTable();
	}
	function getAggregateString() {
		var str = "";
		for(var i in _cols) {
			if(_cols[i].columnType == null) {
				
			} else if(
					_cols[i].columnType == 'count' ||
					_cols[i].columnType == 'sum' ||
					_cols[i].columnType == 'min' ||
					_cols[i].columnType == 'max' ||
					_cols[i].columnType == 'express') {
				if(str.length > 0)
					str += ', ';
				str += (_cols[i].columnSql + ' as ' + _cols[i].field);
			}
		}
		return str;
	}
	function getPartitionString() {
		var str = "";
		for(var i in _cols) {
			if(_cols[i].columnType == 'group') {
				if(str.length > 0)
					str += ', ';
				str = str + _cols[i].field;
			}
		}
		return str;
	}
	function setColumns() {
		if(_cols == null || _cols.length == 0) {
			_dg.datagrid('options').columns[0] = _colsDefault;
		} else {
			_dg.datagrid('options').columns[0] = _cols;
		}
		
		var queryParams = _dg.datagrid('options').queryParams;
		queryParams.aggregateColumn = getAggregateString();
        queryParams.partitionColumn = getPartitionString();
        _dg.datagrid({colums: _dg.datagrid('options').columns[0], queryParams: queryParams});
		//$('#groupbySqlPreview').html('select ' + getAggregateString() + '<br>&nbsp&nbspfrom table_set<br>&nbspgroup by ' + getPartitionString());
	}
	function groupbyCommit() {
		
		endEditing();
		
		_rows = $("#groupbySettingTable").datagrid('getData').rows;
		_cols = new Array();
		for(var i in _rows) {
			
			_cols[i] = jQuery.extend(true, {}, _rows[i]); //深层复制
			if(_rows[i].columnType == 'group') {
				_cols[i].field = _rows[i].field;
				_cols[i].title = _rows[i].title;
				_cols[i].columnType = _rows[i].columnType;
				_cols[i].columnExpress = _rows[i].columnExpress;
				_cols[i].columnSql = _rows[i].columnSql;
				_cols[i].formatter = getDefaultFormatter(_rows[i].field);
				_cols[i].align = getDefaultAlign(_rows[i].field);
			} else {
				_cols[i].field = _rows[i].columnType.toUpperCase() + i + '_' + _rows[i].field;
				_cols[i].title = _rows[i].title;
				_cols[i].columnType = _rows[i].columnType;
				_cols[i].columnExpress = _rows[i].columnExpress;
				if(_rows[i].columnType == 'count') {
					_cols[i].columnSql = 'count(*)';
				} else {
					_cols[i].columnSql = _rows[i].columnType + '(' + _rows[i].field + ')';
					_cols[i].formatter = getDefaultFormatter(_rows[i].field);
					_cols[i].align = getDefaultAlign(_rows[i].field);
				}
			}
		}
		
		setColumns();
	}
	function groupbyReset() {
		
		_cols = null;
		_rows = null;
		
		reject();
		initGroupbySettingTable();
		
		setColumns();
	}
	function getDefaultFormatter(field) {
		for(var j in _colsDefault) {
			if(_colsDefault[j].field == field) {
				return _colsDefault[j].formatter;
			}
		}
		return null;
	}
	function getDefaultAlign(field) {
		for(var j in _colsDefault) {
			if(_colsDefault[j].field == field) {
				return _colsDefault[j].align;
			}
		}
		return null;
	}
</script>

<script type="text/javascript">
	function initGroupbySettingTable() {
		$('#groupbySettingTable').datagrid({
			title:'配置统计规则',
			iconCls:'icon-ok',
			data:_rows,
			nowrap: false,
			striped: true,
			collapsible:false,
			fitColumns:true,
			pagination:false,
			singleSelect:true,
			rownumbers:true,
			remoteSort:true,
			pageList:[10,15,30,50],
			idField:null,
			columns:[[
			    {field:'field',			title:'字段名',	align:'left', sortable:true, width:50,
					formatter: function(value, rowdata, index) {return rowdata.fieldName;},
					editor: {
			    		type: 'combobox',
			    		options: {
			    			valueField: 'field',
							textField: 'title',
							data: unselectColumns,
							editable:false,
							onSelect:onSettingSelect
			    		}
			    	}
			    },
			    {field:'columnType',	title:'类型',	align:'left', sortable:true, width:50,
			    	formatter: function(value, rowdata, index) {return rowdata.columnTypeName;},
			    	editor: {
			    		type: 'combobox',
			    		options: {
			    			valueField: 'type',
							textField: 'text',
							data: columnType,
							editable:false,
							onSelect:onSettingSelect
			    		}
			    	}
				},
			    {field:'title',			title:'列标题',	align:'left', sortable:true, width:50,
			    	editor: 'text'
			    }
			]],
			onClickRow:onClickRow,
			onDblClickRow:function(row){
				//
			},
			toolbar : [{
				text:'新增',
				iconCls:'icon-add',
				handler:function(){append()}
			},{
				text:'删除',
				iconCls:'icon-cut',
				handler:function(){remove()}
			},{
				text:'上移',
				iconCls:'icon-up',
				handler:function(){moveup()}
			},{
				text:'下移',
				iconCls:'icon-down',
				handler:function(){movedown()}
			}]
		});
	}
	
	//////////////////////////////////////
	function getUnselectedField() {
		var fields = new Array();
		for(var i in _colsDefault) {
			var flag = false;
			var rows = $("#groupbySettingTable").datagrid('getData').rows;
			for(var j in rows) {
				if(_colsDefault[i].field == rows[j].field) {
					flag = true;
					break;
				}
			}
			if(!flag) {
				fields[array.length] = _colsDefault.slice(i);
			}
		}
		alert(fields[0]);
		return fields;
	}
	function onSettingSelect() {
		var ed1 = $('#groupbySettingTable').datagrid('getEditor', {index:editIndex, field:'columnType'});
		var ed2 = $('#groupbySettingTable').datagrid('getEditor', {index:editIndex, field:'field'});
		var ed3 = $('#groupbySettingTable').datagrid('getEditor', {index:editIndex, field:'title'});
		
		var title = "";
		if($(ed1.target).combotree('getValue') == 'group') {
			title = $(ed2.target).combotree('getText');
		} else if($(ed1.target).combotree('getValue') == 'count') {
			title = $(ed1.target).combotree('getText');
		} else {
			title = $(ed1.target).combotree('getText') + $(ed2.target).combotree('getText');
		}
		$(ed3.target).val(title);
	}
	
	var editIndex = undefined;
	function endEditing(){
		if (editIndex == undefined){return true}
		if ($('#groupbySettingTable').datagrid('validateRow', editIndex)){
			
			//改写columnTypeName
			var ed = $('#groupbySettingTable').datagrid('getEditor', {index:editIndex, field:'columnType'});
			if(ed == null) {
				$('#groupbySettingTable').datagrid('endEdit', editIndex);
				editIndex = undefined;
				return true;
			}
			var columnTypeName = $(ed.target).combotree('getText');
			$('#groupbySettingTable').datagrid('getRows')[editIndex]['columnTypeName'] = columnTypeName;
			
			//改写fieldName
			var ed = $('#groupbySettingTable').datagrid('getEditor', {index:editIndex, field:'field'});
			if(ed == null) {
				$('#groupbySettingTable').datagrid('endEdit', editIndex);
				editIndex = undefined;
				return true;
			}
			var fieldName = $(ed.target).combotree('getText');
			$('#groupbySettingTable').datagrid('getRows')[editIndex]['fieldName'] = fieldName;
			
			
			$('#groupbySettingTable').datagrid('endEdit', editIndex);
			editIndex = undefined;
			return true;
		} else {
			return false;
		}
	}
	function onClickRow(index){
		if (editIndex != index){
			if (endEditing()){
				$('#groupbySettingTable').datagrid('selectRow', index)
						.datagrid('beginEdit', index);
				editIndex = index;
			} else {
				$('#groupbySettingTable').datagrid('selectRow', editIndex);
			}
		}
	}
	function append(){
		if (endEditing()){
			$('#groupbySettingTable').datagrid('appendRow',{columnType:'group'});
			editIndex = $('#groupbySettingTable').datagrid('getRows').length-1;
			$('#groupbySettingTable').datagrid('selectRow', editIndex)
					.datagrid('beginEdit', editIndex);
		}
	}
	function remove(){
		if (editIndex == undefined){return}
		$('#groupbySettingTable').datagrid('cancelEdit', editIndex)
				.datagrid('deleteRow', editIndex);
		editIndex = undefined;
	}
	function reject(){
		$('#groupbySettingTable').datagrid('rejectChanges');
		editIndex = undefined;
	}
	function getChanges(){
		var rows = $('#groupbySettingTable').datagrid('getChanges');
		alert(rows.length+' rows are changed!');
	}
	function moveup(){
		endEditing();
		var row = $('#groupbySettingTable').datagrid('getSelected');
		var index = $("#groupbySettingTable").datagrid('getRowIndex', row);
		if(swaprow(index, index - 1)) {
			$("#groupbySettingTable").datagrid('selectRow', index - 1);
		}
	}
	function movedown(){
		endEditing();
		var row = $('#groupbySettingTable').datagrid('getSelected');
		var index = $("#groupbySettingTable").datagrid('getRowIndex', row);
		if(swaprow(index, index + 1)) {
			$("#groupbySettingTable").datagrid('selectRow', index + 1);
		}
	}
	function swaprow(index1, index2){
		if(index1 < 0 || index2 < 0 || index1 == index2) {
			return false;
		}
		var rows = $("#groupbySettingTable").datagrid('getData').rows;
		if(index1 >= rows.length || index2 >= rows.length) {
			return false;
		}
		var row1 = $("#groupbySettingTable").datagrid('getData').rows[index1];
		var row2 = $("#groupbySettingTable").datagrid('getData').rows[index2];
		$("#groupbySettingTable").datagrid('getData').rows[index1] = row2;
		$("#groupbySettingTable").datagrid('getData').rows[index2] = row1;
		$("#groupbySettingTable").datagrid('refreshRow', index1);
		$("#groupbySettingTable").datagrid('refreshRow', index2);
		return true;
	}
</script>

<div id="dlg" class="easyui-dialog" title="Basic Dialog" style="width: 800px; height: 400px; padding: 10px; top:20px;" closed=true
	data-options="iconCls:'icon-save',buttons:[{text:'重置',iconCls:'icon-undo',handler:function(){groupbyReset(); if(_cb != null) _cb();}},{text:'OK',iconCls:'icon-ok',handler:function(){$('#dlg').dialog('close'); groupbyCommit(); if(_cb != null) _cb();}}]">

	<table id="groupbySettingTable"></table>
	<!--
	--------------------------------<br>
	结果语句预览：
	<div id="groupbySqlPreview" style="display:none;"></div>
	-->

</div>
