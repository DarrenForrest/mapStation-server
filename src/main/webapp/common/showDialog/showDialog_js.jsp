<%@ page contentType="text/html;charset=UTF-8"%>
<script type="text/javascript">
jQuery.browser={};(function(){jQuery.browser.msie=false; jQuery.browser.version=0;if(navigator.userAgent.match(/MSIE ([0-9]+)./)){ jQuery.browser.msie=true;jQuery.browser.version=RegExp.$1;}})();
	/**
	 *	展示窗口 (默认显示 type类型为：middle的窗口)
	 *	
	 *	适合 显示对话框的div独立，且div中是iframe的情况（只需引入本文件，不需另定义div）
	 *	
	 *	参数说明：
	 *		title：窗口标题
	 *		tmpurl:窗口中iframe的src		
	 *		type:窗口大小,值为{'big','middle'}之一（可以扩展）；big:1000*600 middle:500*300 ...
	 *		isReloadParent:关闭窗口后，是否刷新父窗口，true:是  false:否
	 *		isIe6Frame 是否IE6加iframe 
	 */
	var dialog_excend_id = 'middle';
	function showDialog(title,tmpurl,type,isReloadParent, width, height){
		var ie6Flag = false;
		if($.browser.msie){
			if($.browser.version == '6.0'){
				ie6Flag = true;
			}
		}
		var defaulttop=10;
		var defaultleft;
		var defaultWith = 500;
		var defaultHeight = 300;
		if(type==="big"){
			defaulttop=30;
			if(width == undefined || width == 0 || width == null){
				defaultWith = 1000;
			}else{
				defaultWith = width;
			}
			if(height == undefined || height == 0 || height == null){
				defaultHeight = 600;
			}else{
				defaultHeight = height;
			}
			dialog_excend_id = 'big'
		}else if(type==="morebig"){
			defaulttop=30;
			defaultWith = 1100;
			defaultHeight = 350;
			dialog_excend_id = 'morebig';
		}else if(type==="middle"){
			defaulttop=30;
			defaultWith = 800;
			defaultHeight = 350;
			dialog_excend_id = 'middle';
		}else if(type==="small"){
			defaulttop=30;
			defaultWith = 340;
			defaultHeight = 200;
			dialog_excend_id = 'middle';
		}else if(type==="moremiddle"){
			defaulttop=30;
			defaultWith = 800;
			defaultHeight = 600;
			dialog_excend_id = 'moremiddle';
		}else if(type==="moreheight"){
			defaulttop=30;
			defaultWith = 800;
			defaultHeight = 650;
			dialog_excend_id = 'middle';
		}else{
			defaulttop=30;
			defaultWith = 800;
			defaultHeight = 500;
			dialog_excend_id = 'middle';
		}
		
		var x = 0;
		try{
			x = $('#popup'+dialog_excend_id).offset().top;
		}catch(exception){
			x=0;
		}
		if(x<0) x=0;
		
		var y = 0;
		try{
			y = (document.body.clientWidth - defaultWith) / 2;
		}catch(exception){
			y=0;
		}
		if(y<0) y=0;
		$('#popup'+dialog_excend_id).dialog({
			top:x,
			left:y,
			title:title,
			width:defaultWith,
			height:defaultHeight,
			draggable:true,
			modal:true,
			onClose:function(){
				$('#framepopup'+dialog_excend_id).attr('src','about:blank');
			},
			onBeforeClose:function(){
				if(isReloadParent === true){
					reload();
				}
			}
		});
		
		if(tmpurl.indexOf('?')>-1){
			tmpurl = tmpurl + "&sdfdsf="+new Date().getTime();
		}else{
			tmpurl = tmpurl + "?sdfdsf="+new Date().getTime();
		}
		$('#framepopup'+dialog_excend_id).attr('src',tmpurl);
		
		$('#popup'+dialog_excend_id).dialog('open');
		if(ie6Flag){
			ie6bug.maskSelect();
		}
	}
	function closeDialog(){
		$('#popup'+dialog_excend_id).dialog('close');
	}
</script>