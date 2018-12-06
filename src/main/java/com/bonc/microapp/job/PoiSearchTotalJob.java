package com.bonc.microapp.job;

import com.bonc.common.CrontabTask;
import com.bonc.db.TxException;
import com.bonc.microapp.entity.JobDefine;
import com.bonc.microapp.service.MapPoiService;
import com.bonc.tools.ParamVo;

public class PoiSearchTotalJob extends CommonJob{
	private MapPoiService mapPoiService;
	
	@Override
	protected String ownerJob(JobDefine jobDefine, long triggerType, Long exeFlowId) throws TxException {
		if(mapPoiService == null) {
			mapPoiService = (MapPoiService)CrontabTask.getBean("mapPoiService");
		}
		String ret = mapPoiService.getPoiSearchTotal();
		return ret;
	}
	
}
