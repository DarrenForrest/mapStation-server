package com.bonc.common;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger.TriggerState;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoader;

public class CrontabTask {
	
	private static boolean init = false;
	private static Scheduler scheduler;
	private static ApplicationContext applicationContext;
	
	static {
		CrontabTask.applicationContext = ContextLoader.getCurrentWebApplicationContext();
	}
	
	public static synchronized ApplicationContext getApplicationContext() {
		return CrontabTask.applicationContext;
	}
	
	public static synchronized void setApplicationContext(ApplicationContext applicationContext) {
		CrontabTask.applicationContext = applicationContext;
	}
	
	public static synchronized boolean init() {
		if(!init) {
			try {
				scheduler = StdSchedulerFactory.getDefaultScheduler();
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}

	public static synchronized boolean start() {
		
		if(!init) {
			init = true;
			
			try {
				scheduler.start();
			} catch(Exception e) {
				e.printStackTrace();
				init = false;
				return false;
			}
		}
		
		return true;
	}
	
	public static synchronized boolean stop() {
		if(init) {
			try {
				scheduler.clear();
				scheduler.shutdown(true);
			} catch (SchedulerException e) {
				e.printStackTrace();
				return false;
			}
		}
		init = false;
		return true;
	}
	
	public static Object getBean(String name) {
		System.out.println("applicationContext=" + applicationContext);
		
		return applicationContext == null ? null : applicationContext.getBean(name);
	}
	
	public static boolean isRunning() {
		return init;
	}
	
	public static String getJobState(String id) {
		try {
			if(isRunning()) {
				TriggerKey triggerKey = new TriggerKey("TRIGGER-" + id);
				TriggerState triggerState = scheduler.getTriggerState(triggerKey);
				if(triggerState == null) {
					return "not start";
				}
				return triggerState.name();
			}
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		return "not found";
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static synchronized boolean addToScheduler(String id, String clsName, String cronExpress, Object data) {
		try {
			CronTrigger cronTrigger = TriggerBuilder.newTrigger()
					.withIdentity("TRIGGER-" + id, "GROUP")
					.withSchedule(CronScheduleBuilder.cronSchedule(cronExpress))
					.build();
			
			Class cls = null;
			try {
				cls = Class.forName(clsName);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				return false;
			}
			JobDetail job = JobBuilder.newJob(cls)
				    .withIdentity("JOB-" + id, "GROUP")
				    .build();
			job.getJobDataMap().put("data", data);
			
			scheduler.scheduleJob(job, cronTrigger);
			
		} catch (SchedulerException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
}
