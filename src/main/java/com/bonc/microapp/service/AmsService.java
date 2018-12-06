package com.bonc.microapp.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bonc.db.IDao;
import com.bonc.db.TxException;
import com.bonc.microapp.entity.AmsActionLog;
import com.bonc.microapp.entity.AmsStaff;
import com.bonc.tools.AmsTool;
import com.bonc.tools.DateUtils;
import com.bonc.tools.ParamVo;
import com.bonc.tools.StrUtil;

@Service
public class AmsService {

	@Autowired
	private IDao amsStaffDao;
	
	@Autowired
	private IDao amsActionLogDao;
	
	@Autowired
	private ToolService toolService;
	
	@Autowired
	private AmsLogService amsLogService;
	
	public boolean loginByAuthCode(AmsTool amsTool) throws TxException {
		//第一个使用验证码登录
		String userName="dfgx_liyg";
		String password= "p2p12345@";
		if(!amsTool.login(userName, password)) {			
			if(!amsTool.login(userName, password)) {
				if(!amsTool.login(userName, password)) {
					if(!amsTool.login(userName, password)) {
						throw new TxException("连续4次登录失败");
					}
				}
			}
		}
		return true;
	}
	
	public String txExeLoginoutPlanRedo(ParamVo vo) throws TxException {
		String str = "";
		int iRet = 0;
		
		int iCntSuccess = 0;
		int iCntError = 0;
		
		AmsTool amsTool = new AmsTool();
		
		//TODO:节假日应该跳过，不执行
		String workDate = DateUtils.getNow("yyyy-MM-dd");		
		
		ParamVo voQuery = new ParamVo();
		voQuery.setMethod("selectRedoList4Exe");
		voQuery.setObjectClass(AmsActionLog.class);
		voQuery.put("action", "workloginout");
		voQuery.put("workDate", workDate);
		
		List list = this.amsActionLogDao.selectList(voQuery);
		if(list == null){
			return "查询执行计划失败";
		}
		
		System.out.println("list.szie = " +list.size() );
		
		
		for(int i=0; i<list.size(); i++){			
			if(i==0) {
				this.loginByAuthCode(amsTool);
			}			
			
			AmsActionLog amsActionLog = (AmsActionLog)list.get(i);
			
			ParamVo voStaff = new ParamVo();
			voStaff.setObjectClass(AmsStaff.class);
			voStaff.put("id", amsActionLog.getAmsStaffId());
			
			AmsStaff amsStaff = (AmsStaff)this.amsStaffDao.getInfoById(voStaff);
			if(amsStaff == null){
				throw new TxException("查询AmsStaff错误");
			}
			
			//生成随机数，避免都是同一个秒数执行
			Random random =new Random() ;
			int iSleep = random.nextInt(10);
			try {
				Thread.sleep(1000*iSleep);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
						
			try{
				float workTime = amsTool.queryWorkTimeBySwitch(amsStaff.getLoginName(), amsStaff.getPassword());
				
				amsActionLog.setExeCode("3");//补签此功能
				amsActionLog.setExeMsg(amsActionLog.getExeMsg()  + "; 签出成功  本日工时:" + workTime + "小时");
				amsActionLog.setExeTime(new Date());
				iCntSuccess = iCntSuccess +1;
			}catch(Exception e){
				amsActionLog.setExeCode("2");//执行失败
				amsActionLog.setExeMsg(amsActionLog.getExeMsg() + "; 签出失败");
				amsActionLog.setExeErrMsg( amsActionLog.getExeErrMsg() + "; " + e.getMessage());
				amsActionLog.setExeTime(new Date());
				iCntError = iCntError + 1;
			}
			
			ParamVo voLog = new ParamVo();
			voLog.setObject(amsActionLog);
			voLog.setObjectClass(AmsActionLog.class);
			this.amsLogService.tnSaveAmsActionLog(voLog);
		}
		
		str = "redo 签出成功记录数 = " + iCntSuccess + ";签出失败记录数 =" + iCntError;
		return str;
	}
	
	public String txExeLoginPlanRedo(ParamVo vo) throws TxException {
		String str = "";
		int iRet = 0;
		
		int iCntSuccess = 0;
		int iCntError = 0;
		
		AmsTool amsTool = new AmsTool();
		
		String workDate = DateUtils.getNow("yyyy-MM-dd");
		
		ParamVo voQuery = new ParamVo();
		voQuery.setMethod("selectRedoList4Exe");
		voQuery.setObjectClass(AmsActionLog.class);
		voQuery.put("action", "login");
		voQuery.put("workDate", workDate);
		
		List list = this.amsActionLogDao.selectList(voQuery);
		if(list == null){
			return "查询执行计划失败";
		}
		
		System.out.println("list.szie = " +list.size() );		
		
		for(int i=0; i<list.size(); i++){
			if(i==0) {
				this.loginByAuthCode(amsTool);
			}
			
			AmsActionLog amsActionLog = (AmsActionLog)list.get(i);
			
			ParamVo voStaff = new ParamVo();
			voStaff.setObjectClass(AmsStaff.class);
			voStaff.put("id", amsActionLog.getAmsStaffId());
			
			AmsStaff amsStaff = (AmsStaff)this.amsStaffDao.getInfoById(voStaff);
			if(amsStaff == null){
				throw new TxException("查询AmsStaff错误");
			}

			try{
				float workTime = amsTool.queryWorkTime(amsStaff.getLoginName(), amsStaff.getPassword());
				
				amsActionLog.setExeCode("3");//修复成功
				amsActionLog.setExeMsg(amsActionLog.getExeMsg()  + "; redo 签入成功  工时:" + workTime + "小时");
				amsActionLog.setExeTime(new Date());
				iCntSuccess = iCntSuccess +1;
			}catch(Exception e){
				amsActionLog.setExeCode("2");//执行失败
				amsActionLog.setExeMsg(amsActionLog.getExeErrMsg() + ";签入失败");
				amsActionLog.setExeErrMsg(amsActionLog.getExeErrMsg() + ";" + e.getMessage());
				amsActionLog.setExeTime(new Date());
				iCntError = iCntError + 1;
			}
			
			ParamVo voLog = new ParamVo();
			voLog.setObject(amsActionLog);
			voLog.setObjectClass(AmsActionLog.class);
			this.amsLogService.tnSaveAmsActionLog(voLog);
			
			//生成随机数
			Random random =new Random() ;
			int iSleep = random.nextInt(10);
			try {
				Thread.sleep(1000*iSleep);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		str = "签入成功记录数 = " + iCntSuccess + ";签入失败记录数 =" + iCntError;
		return str;
	}
	
	public String txCreateMail4ActionReport(String action) throws TxException{
		String str="";		
		String workDate = DateUtils.getNow("yyyy-MM-dd");
		
		String subject = "AMS系统 " + workDate + " " + action + " 执行统计";

		ParamVo vo = new ParamVo();
		vo.setMethod("selectActionReport");
		vo.put("workDate", workDate);
		vo.put("action", action);
		
		List list = this.amsActionLogDao.selectList(vo);
		if(list == null){
			throw new TxException("查询签入签出日志出错");
		}
		if(list.size() == 0){
			toolService.txEmailNotice(subject, "没有记录", "autoAms");
			return "没有记录";
		}
		StringBuffer sb = new StringBuffer();
		sb.append(workDate + "  " + action + " 执行情况\n");
		sb.append("--------------------\n");
		
		for(int i=0; i< list.size(); i++){
			Map map = (Map)list.get(i);
			String stateName = map.get("STATE_NAME").toString();
			String cnt		 = map.get("CNT").toString();
			
			String line = StrUtil.rightPad(workDate, 15, " ") +
							StrUtil.rightPad(action, 14, " ") +	
							StrUtil.rightPad(stateName, 6, " ") +
							StrUtil.rightPad(cnt, 6, " ");
			sb.append(line + "\n");
		}
		
		//生成失败明细记录
		vo = new ParamVo();
		vo.setMethod("selectPage");
		// workDate action exeCode
		vo.put("beginDate", workDate);
		vo.put("endDate", workDate);
		vo.put("action", action);
		vo.put("exeCode", 2);//失败的就
		
		list = this.amsActionLogDao.selectList(vo);
		if(list == null){
			throw new TxException("查询签入签出日志出错");
		}
		if(list.size() > 0){
			
			String title = StrUtil.rightPadChEn("帐号", 26, " ") +
							StrUtil.rightPadChEn("负责人", 16, " ") +
							StrUtil.rightPadChEn("执行时间", 22, " ") +
							StrUtil.rightPadChEn("执行结果", 10, " ") +
							StrUtil.rightPadChEn("报错信息", 10, " ");
			
			sb.append("\n\n执行出错的记录\n");
			sb.append("--------------------\n");
			sb.append(title + "\n");
			for(int i=0; i< list.size(); i++){
				Map map = (Map)list.get(i);
				//LOGIN_NAME ADMIN_NAME PLAN_TIME EXE_MSG EXE_ERR_MSG

				String loginName = map.get("LOGIN_NAME") == null  ? "":map.get("LOGIN_NAME").toString();
				String adminName = map.get("ADMIN_NAME") == null  ? "":map.get("ADMIN_NAME").toString();
				String planTime  = map.get("PLAN_TIME") == null   ? "":map.get("PLAN_TIME").toString();
				String exeMsg    = map.get("EXE_MSG") == null     ? "":map.get("EXE_MSG").toString();
				String exeErrMsg = map.get("EXE_ERR_MSG") == null ? "":map.get("EXE_ERR_MSG").toString();
				
				String line = StrUtil.rightPad(loginName, 26, " ") +
						StrUtil.rightPadChEn(adminName, 16, " ") +
						StrUtil.rightPadChEn(planTime,  22, " ") +
						StrUtil.rightPadChEn(exeMsg,    10, " ") +
						StrUtil.rightPadChEn(exeErrMsg, 10, " ");
				
				sb.append(line + "\n");
			}
		}
		
 		toolService.txEmailNotice(subject, sb.toString(), "autoAms");
		
 		str  = "生成 " + workDate + " " + action + " 邮件通知";
		return str;
	}
	
	public String txExeLoginoutPlan(ParamVo vo) throws TxException {
		String str = "";
		int iRet = 0;
		
		int iCntSuccess = 0;
		int iCntError = 0;
		
		AmsTool amsTool = new AmsTool();
		
		//TODO:节假日应该跳过，不执行
		String workDate = DateUtils.getNow("yyyy-MM-dd");		
		
		ParamVo voQuery = new ParamVo();
		voQuery.setMethod("selectList4Exe");
		voQuery.setObjectClass(AmsActionLog.class);
		voQuery.put("action", "workloginout");
		voQuery.put("workDate", workDate);
		
		List list = this.amsActionLogDao.selectList(voQuery);
		if(list == null){
			return "查询执行计划失败";
		}
		
		System.out.println("list.szie = " +list.size() );
		
		
		for(int i=0; i<list.size(); i++){
			if(i==0) {
				this.loginByAuthCode(amsTool);
			}
			
			AmsActionLog amsActionLog = (AmsActionLog)list.get(i);
			
			ParamVo voStaff = new ParamVo();
			voStaff.setObjectClass(AmsStaff.class);
			voStaff.put("id", amsActionLog.getAmsStaffId());
			
			AmsStaff amsStaff = (AmsStaff)this.amsStaffDao.getInfoById(voStaff);
			if(amsStaff == null){
				throw new TxException("查询AmsStaff错误");
			}
			
			//生成随机数，避免都是同一个秒数执行
			Random random =new Random() ;
			int iSleep = random.nextInt(6);
			try {
				Thread.sleep(1000*iSleep);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
						
			try{
				float workTime = amsTool.queryWorkTimeBySwitch(amsStaff.getLoginName(), amsStaff.getPassword());
				
				amsActionLog.setExeCode("1");//执行成功
				amsActionLog.setExeMsg("签出成功  本日工时:" + workTime + "小时");
				amsActionLog.setExeTime(new Date());
				iCntSuccess = iCntSuccess +1;
			}catch(Exception e){
				amsActionLog.setExeCode("2");//执行失败
				amsActionLog.setExeMsg("签出失败");
				amsActionLog.setExeErrMsg(e.getMessage());
				amsActionLog.setExeTime(new Date());
				iCntError = iCntError + 1;
			}
			
			ParamVo voLog = new ParamVo();
			voLog.setObject(amsActionLog);
			voLog.setObjectClass(AmsActionLog.class);
			
			//独立事务保存
			this.amsLogService.tnSaveAmsActionLog(voLog);			
		}
		
		str = "签出成功记录数 = " + iCntSuccess + ";签出失败记录数 =" + iCntError;
		return str;
	}
	
	public String txExeLoginPlan(ParamVo vo) throws TxException {
		String str = "";
		int iRet = 0;
		
		int iCntSuccess = 0;
		int iCntError = 0;
		
		AmsTool amsTool = new AmsTool();
		
		//TODO:节假日应该跳过，不执行
		String workDate = DateUtils.getNow("yyyy-MM-dd");
		
		ParamVo voQuery = new ParamVo();
		voQuery.setMethod("selectList4Exe");
		voQuery.setObjectClass(AmsActionLog.class);
		voQuery.put("action", "login");
		voQuery.put("workDate", workDate);
		
		List list = this.amsActionLogDao.selectList(voQuery);
		if(list == null){
			return "查询执行计划失败";
		}
		
		System.out.println("list.szie = " +list.size() );
	
		
		for(int i=0; i<list.size(); i++){
			if(i==0) {
				this.loginByAuthCode(amsTool);
			}
			
			AmsActionLog amsActionLog = (AmsActionLog)list.get(i);
			
			ParamVo voStaff = new ParamVo();
			voStaff.setObjectClass(AmsStaff.class);
			voStaff.put("id", amsActionLog.getAmsStaffId());
			
			AmsStaff amsStaff = (AmsStaff)this.amsStaffDao.getInfoById(voStaff);
			if(amsStaff == null){
				throw new TxException("查询AmsStaff错误");
			}
			

			try{
				float workTime = amsTool.queryWorkTimeBySwitch(amsStaff.getLoginName(), amsStaff.getPassword());
				
				amsActionLog.setExeCode("1");//执行成功
				amsActionLog.setExeMsg("签入成功  工时:" + workTime + "小时");
				amsActionLog.setExeTime(new Date());
				iCntSuccess = iCntSuccess +1;
			}catch(Exception e){
				amsActionLog.setExeCode("2");//执行失败
				amsActionLog.setExeMsg("签入失败");
				amsActionLog.setExeErrMsg(e.getMessage());
				amsActionLog.setExeTime(new Date());
				iCntError = iCntError + 1;
			}
			
			ParamVo voLog = new ParamVo();
			voLog.setObject(amsActionLog);
			voLog.setObjectClass(AmsActionLog.class);
			
			//独立事务保存
			this.amsLogService.tnSaveAmsActionLog(voLog);
			
			//生成随机数
			Random random =new Random() ;
			int iSleep = random.nextInt(6);
			try {
				Thread.sleep(1000*iSleep);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		str = "签入成功记录数 = " + iCntSuccess + ";签入失败记录数 =" + iCntError;
		return str;
	}
	
	public String txCreateAmsPlan(ParamVo vo) throws TxException {
		String str ="";
		int iRet = 0;
		
		String workDate = DateUtils.getNow("yyyy-MM-dd");
		if(toolService.isHoliday(workDate)){
			return workDate+"是节假日，不生成计划";
		}
		
		iRet = this.amsActionLogDao.insert(this.amsActionLogDao.getNameSpace()+".insertAmsLoginPlan", vo.getParam());
		if(iRet <0){
			throw new TxException("生成签入计划错误");
		}
		str = "生成签入计划记录数：" + iRet + ";";
		
		
		iRet = this.amsActionLogDao.insert(this.amsActionLogDao.getNameSpace()+".insertAmsLoginoutPlan", vo.getParam());
		if(iRet <0){
			throw new TxException("生成签出计划错误");
		}
		
		str = str +"生成签出计划记录数：" + iRet + ";";
		
		return str;
	}
	
	
	public void txSaveStaff(ParamVo vo) throws TxException {
		if(vo.getObject() ==null){
			throw new TxException("保存的信息为空！");
		}
		
		vo.setObjectClass(AmsStaff.class);
		if(!this.amsStaffDao.save(vo)) {
			throw new TxException("保存帐号信息出错！");
		}
	}
	
	public Object getActionLogPage(ParamVo vo) {
		vo.setMethod("selectPage");
		return this.amsActionLogDao.selectPage(vo);
	}
	
	public Object getStaffPage(ParamVo vo) {
		vo.setMethod("selectPage");
		return this.amsStaffDao.selectPage(vo);
	}
	
	public Object getStaffInfo(ParamVo vo) {
		return this.amsStaffDao.getInfoById(vo);
	}
}
