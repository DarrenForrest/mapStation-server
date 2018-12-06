
package com.show.api.test;

import com.bonc.microapp.dao.F;
import com.show.api.ShowApiRequest;

public class ShowapiTest {
    public ShowapiTest() {

    }

    /***
     * 测试接口猜一猜
     */
    public static void main(String[] args) {
       // String s = (new ShowApiRequest("http://route.showapi.com/632-1", "appid", "appSecret")).post();
       // System.out.println(s);
    	
    	
    	String res=new ShowApiRequest("http://route.showapi.com/184-5",F.SHOWAPI_APP_ID,F.SHOWAPI_APP_KEY)
    			.addTextPara("img_base64","")
    			.addTextPara("typeId","34")
    			.addTextPara("convert_to_jpg","0")
    			.addTextPara("needMorePrecise","0")
    		  .post();
    		System.out.println(res);
    }

}
