package com.bonc.microapp.dao;

import org.springframework.stereotype.Repository;

import com.bonc.db.BatisDao;

@Repository
public class CoBaseStationInfoDao  extends BatisDao{

	public CoBaseStationInfoDao() {
		this.setNameSpace(F.NAMESPACE_MICROAPP + ".CoBaseStationInfo");
	}

}
