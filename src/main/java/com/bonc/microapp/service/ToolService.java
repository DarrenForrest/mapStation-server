package com.bonc.microapp.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import com.bonc.db.IDao;
import com.bonc.db.TxException;
import com.bonc.microapp.entity.MailList;
import com.bonc.tools.ConfigUtil;
import com.bonc.tools.ParamVo;

@Service
public class ToolService {
    private static final Logger log = LoggerFactory.getLogger(ToolService.class);
    
    @Autowired
    private IDao mailListDao;
    
    @Autowired
    private IDao holidayDao;
    
    @Autowired
    private MailService mailService;
    
    /**
     * 
     * @param day  YYYY-MM-DD
     * @return
     */
    public boolean isHoliday(String day){
    	ParamVo vo = new ParamVo();
    	boolean bFlag = false;
    	
    	vo.setMethod("selectDayCnt");
    	vo.put("day", day);
    	Long cnt = (Long)this.holidayDao.select(vo);
    	if(cnt.intValue() > 0){
    		return true;
    	}else{
    		return false;
    	}
    }
    
    public void txEmailNotice(String subject,String msg,String tag) {
    	MailList mailList = new MailList();
    	   	
    	String mailTo =  ConfigUtil.getInstance().getValueByProperty("config/microapp.properties", "admin_email");
    	
    	mailList.setId(-1L);
    	mailList.setMailTo(mailTo);
    	mailList.setMailCc("");
    	mailList.setMailSubject(subject);
    	mailList.setMailTxt(msg);
    	mailList.setSendCnt(0L);
    	mailList.setState(1L);
    	mailList.setTag(tag);
    	mailList.setCreateDate( new Date());
    	mailList.setUpdateDate(new Date());

    	try {
			this.mailService.tnInsert(mailList);
		} catch (TxException e) {
			e.printStackTrace();
		}
    }
    
    public String txSendMail()throws TxException{
    	
    	ParamVo vo = new ParamVo();
    	vo.setMethod("selectList4Send");
    	List list = this.mailListDao.selectList(vo);
    	
    	if(list == null){
    		throw new TxException("查询待发送邮件列表出错");
    	}
    	
    	for(int i =0; i< list.size(); i++){
    		Map map = (Map)list.get(i);
    		Long id = Long.valueOf( map.get("ID").toString() );
    		
    		vo = new ParamVo();
    		vo.put("id", id);
    		vo.setObjectClass(MailList.class);
    		
    		MailList mailList = (MailList)this.mailListDao.getInfoById(vo);
    		
    		try{
    			this.mailSend(mailList.getMailTo(), mailList.getMailSubject(), mailList.getMailTxt());
    			
    			mailList.setSendCnt(mailList.getSendCnt() + 1);
    			mailList.setUpdateDate(new Date());
    			mailList.setState(2L);
    		}catch(TxException e){
    			
    			mailList.setSendCnt(mailList.getSendCnt() + 1);
    			mailList.setUpdateDate(new Date());
    			this.mailService.tnUpdate(mailList);
    			
    			e.printStackTrace();
    			throw e;
    		}
    		
    		this.mailService.tnUpdate(mailList);
    	}
    	return "发送邮件" + list.size() + "封";
    }
    
	public void mailSend(String email,String subject,String msg) throws TxException {
    	String mail_host = ConfigUtil.getInstance().getValueByProperty("config/microapp.properties", "mail_host");
    	String mail_port = ConfigUtil.getInstance().getValueByProperty("config/microapp.properties", "mail_port");
    	String mail_username = ConfigUtil.getInstance().getValueByProperty("config/microapp.properties", "mail_username");
    	String mail_password = ConfigUtil.getInstance().getValueByProperty("config/microapp.properties", "mail_password");
 
		 //创建邮件发送服务器
	    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();  
        mailSender.setHost(mail_host);
        mailSender.setPort( Long.valueOf(mail_port).intValue());
        mailSender.setUsername(mail_username);
        mailSender.setPassword(mail_password);
        //加认证机制
        Properties javaMailProperties = new Properties();
    	javaMailProperties.put("mail.smtp.auth", true); 
    	javaMailProperties.put("mail.smtp.starttls.enable", true); 
    	javaMailProperties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
    	javaMailProperties.put("mail.smtp.timeout", 5000); 
        mailSender.setJavaMailProperties(javaMailProperties);
        
        String[] aEmail = email.split(";");
        
        //创建邮件内容
        SimpleMailMessage message=new SimpleMailMessage();
        message.setFrom(mail_username);
        message.setTo(aEmail);
        message.setSubject(subject);
        message.setText(msg);

        try{
        	mailSender.send(message);
        }catch (Exception e){
        	e.printStackTrace();
        	throw new TxException(e.getMessage());
        }
	       
	}
}
