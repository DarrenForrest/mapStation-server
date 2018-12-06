package com.bonc.microapp.job;

import com.bonc.common.CrontabTask;
import com.bonc.db.TxException;
import com.bonc.microapp.entity.JobDefine;
import com.bonc.microapp.service.MapPoiService;
import com.bonc.tools.ParamVo;

public class TyPoiJob extends CommonJob{

	private MapPoiService mapPoiService;
	
	
	@Override
	protected String ownerJob(JobDefine jobDefine, long triggerType, Long exeFlowId) throws TxException {
		
		if(mapPoiService == null) {
			mapPoiService = (MapPoiService)CrontabTask.getBean("mapPoiService");
		}
		
		ParamVo vo = new ParamVo();
		String cityCode="140100";
		String ret = mapPoiService.getPoiInfo(cityCode,"3k0KG0jADuc0GFziTgOWHamzH9KCSdLh");
		
		return ret;
	}
	
}
