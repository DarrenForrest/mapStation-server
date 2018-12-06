<%@ page contentType="text/html;charset=UTF-8" %>
<script type="text/javascript">
	var _dg = null;
	var _cb = null;
	var _cols = null;
	
	function initGroupByEnv(dg, func) {
		_dg = dg;
		_cb = func;
		_cols = dg.datagrid('options').columns[0];
		for(var i in _cols) {
			_cols[i].columnTitle = _cols[i].title;
			addRow(_cols[i]);
		}
	}
	function setColumns() {
		var noShow = true;
		for(var i in _cols) {
			if(_cols[i].columnType == null || _cols[i].columnType.length == 0) {
				_cols[i].columnType = "express";
				if(_cols[i].columnExpress == null) {
					_cols[i].columnExpress = "";
				}
			}
			
			if(_cols[i].columnType == "express" && _cols[i].columnExpress.length == 0){
				_dg.datagrid('hideColumn', _cols[i].field);
			} else {
				noShow = false;
				_dg.datagrid('showColumn', _cols[i].field);
			}
			
			//显示合计字段名
			_cols[i].title =  _cols[i].columnTypeName;
		}
		if(noShow) {
			for(var i in _cols) {
				_dg.datagrid('showColumn', _cols[i].field);
				_cols[i].title = _cols[i].columnTitle;
			}
		}
		$('#resultSql').html('select ' + getAggregateString() + '<br>&nbsp&nbspfrom table_set<br>&nbspgroup by ' + getPartitionString());
	}
	function getAggregateString() {
		var str = "";
		for(var i in _cols) {
			if(
					_cols[i].columnType == 'count' ||
					_cols[i].columnType == 'sum' ||
					_cols[i].columnType == 'min' ||
					_cols[i].columnType == 'max'
					
				) {
				if(str.length > 0)
					str = str + ', ';
				str = str + _cols[i].columnType + '(' + _cols[i].field + ') as ' + _cols[i].field;
			} else if(_cols[i].columnType == 'express' && _cols[i].columnExpress.length > 0) {
				if(str.length > 0)
					str = str + ',';
				str = str + _cols[i].columnExpress + ' as ' + _cols[i].field;
			}
		}
		return str;
	}
	function getPartitionString() {
		var str = "";
		for(var i in _cols) {
			if(_cols[i].columnType == 'groupby') {
				if(str.length > 0)
					str = str + ', ';
				str = str + _cols[i].field;
			}
		}
		return str;
	}
	function addRow(col) {
		var table = document.getElementById('settingTable');
		var tr = table.insertRow();
		tr.insertCell().innerHTML = col.field;
		tr.insertCell().innerHTML = col.title;
		tr.insertCell().innerHTML = '<input name="RADIO_' + col.field + '" type="radio" style="margin-left:25px;" onclick="build(this, \'' + col.field + '\', \'groupby\');" />';
		tr.insertCell().innerHTML = '<input name="RADIO_' + col.field + '" type="radio" style="margin-left:25px;" onclick="build(this, \'' + col.field + '\', \'count\');" />';
		tr.insertCell().innerHTML = '<input name="RADIO_' + col.field + '" type="radio" style="margin-left:25px;" onclick="build(this, \'' + col.field + '\', \'sum\');" />';
		tr.insertCell().innerHTML = '<input name="RADIO_' + col.field + '" type="radio" style="margin-left:25px;" onclick="build(this, \'' + col.field + '\', \'min\');" />';
		tr.insertCell().innerHTML = '<input name="RADIO_' + col.field + '" type="radio" style="margin-left:25px;" onclick="build(this, \'' + col.field + '\', \'max\');" />';
		tr.insertCell().innerHTML = '<input id="RADIO_' + col.field + '" name="RADIO_' + col.field + '" type="radio" style="margin-left:25px;" checked="true" onclick="build(this, \'' + col.field + '\', \'express\');" /><input id="EXPRESS_' + col.field + '" type="text" onchange="build(document.getElementById(\'RADIO_' + col.field + '\'), \'' + col.field + '\', \'express\');" />';
	}
	function build(obj, field, type) {
		//alert(obj.checked + field + type);
		for(var i in _cols) {
			if(_cols[i].field == field) {
				if(obj.checked) {
					_cols[i].columnType = type;
					_cols[i].columnExpress = $('#EXPRESS_' + field).val();
					if(type == 'count'  ) _cols[i].columnTypeName = '计数';
					if(type == 'sum'    ) _cols[i].columnTypeName = '合计' + _cols[i].columnTitle;
					if(type == 'min'    ) _cols[i].columnTypeName = '最小' + _cols[i].columnTitle;
					if(type == 'max'    ) _cols[i].columnTypeName = '最大' + _cols[i].columnTitle;
					if(type == 'express') _cols[i].columnTypeName = '[new]' + _cols[i].columnTitle;
					if(type == 'groupby') _cols[i].columnTypeName = _cols[i].columnTitle;
				} else {
					//_cols[i].columnType = "";
				}
				break;
			}
		}
		setColumns();
	}
	function groupbyClear() {
		for(var i in _cols) {
			if(_cols[i].columnTitle != null) {
				_cols[i].title = _cols[i].columnTitle;
			}
			_cols[i].columnType = null;
			_cols[i].columnExpress = null;
			$('#EXPRESS_' + _cols[i].field).val('');
			$('#RADIO_' + _cols[i].field).attr('checked', true);
		}
		setColumns();
	}
</script>

<div id="dlg" class="easyui-dialog" title="Basic Dialog" style="width: 800px; height: 400px; padding: 10px; top:20px;" closed=true
	data-options="iconCls:'icon-save',buttons:[{text:'重置',iconCls:'icon-undo',handler:function(){groupbyClear(); if(_cb != null) _cb();}},{text:'OK',iconCls:'icon-ok',handler:function(){$('#dlg').dialog('close'); if(_cb != null) _cb();}}]">

	<table id="settingTable">
		<tr>
			<th style="width:60px;">字段名</th>
			<th style="width:60px;">列名</th>
			<th style="width:60px;">GROUP BY</th>
			<th style="width:60px;">COUNT</th>
			<th style="width:60px;">SUM</th>
			<th style="width:60px;">MIN</th>
			<th style="width:60px;">MAX</th>
			<th style="width:200px">EXPRESS</th>
		</tr>
	</table>
	--------------------------------<br>
	结果语句：
	<div id="resultSql"></div>

</div>
