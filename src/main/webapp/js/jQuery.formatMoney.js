/**
 * 金额按千位逗号分割
 * @character_set UTF-8
 * 依赖函数 accounting.js
 *  Example
 * 	<code>
 *      alert($.formatMoney(1234.345, 2)); //=>1,234.35
 *      alert($.formatMoney(-1234.345, 2)); //=>-1,234.35
 *      alert($.unformatMoney(1,234.345)); //=>1234.35
 *      alert($.unformatMoney(-1,234.345)); //=>-1234.35
 * 	</code>
 */
;(function($)
{
    $.extend({
        /**
         * 金额按千位逗号分割
         * @public
         * @param mixed mVal 数值
         * @return string
         */
        formatMoney:function(mVal){
        	return accounting.formatMoney(mVal, "", 2, ",", "."); 
        },
        /**
         * 将千分位格式的字符串转换为浮点数
         * @public
         * @param string sVal 数值
         * @return float
         */
        unformatMoney:function(sVal){
            var fTmp = parseFloat(sVal.replace(/,/g, ''));
            return (isNaN(fTmp) ? 0 : fTmp);
        }
    });
})(jQuery);