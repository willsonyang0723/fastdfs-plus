package com.jinhuhang.fastdfs.plugins;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.SystemPropertyUtils;

import com.jinhuhang.fastdfs.common.Constant;


/**
 * @project risk_limit
 * @author yy
 * @date 2017年12月21日 下午1:01:52
 * @description TODO spring相关设置  初始化 bean到普通类
 * @tag 
 * @company 上海金互行金融信息服务有限公司
 */
@Component
public class SpringUtil implements ApplicationContextAware {

	private static ApplicationContext context;
	
	
	@Override
	public void setApplicationContext(ApplicationContext context)
			throws BeansException {
		SpringUtil.context=context;
		//读取配置
		new Constant().loadPorperties("application.properties");
		if(context.getEnvironment().getActiveProfiles().length>0)
			new Constant().loadPorperties("application-"+context.getEnvironment().getActiveProfiles()+".properties");
			
		
	}


	public static ApplicationContext getContext() {
		return context;
	}


	
}
