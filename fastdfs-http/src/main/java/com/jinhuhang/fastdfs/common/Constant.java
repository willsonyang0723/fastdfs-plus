package com.jinhuhang.fastdfs.common;

import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @project risk-common
 * @author yy
 * @date 2017年7月6日 上午11:48:13
 * @description TODO 常量
 * @tag 
 * @company 上海金互行金融信息服务有限公司
 */
public class Constant {
	/**通过返回 成功**/
	public static final int SUCCESS=1;
	/**通过返回 失败**/
	public static final int FAIL=0;
	
	/**
	 * 保存全局属性值
	 */
	private static Properties p = null;

	/**
	 * 获取配置
	 * 
	 */
	public static String getConfig(String key) {
		String value=p.getProperty(key);
		if(value==null)
			p.put(key, "");
		return value;
	}

	
	public void loadPorperties(String confpath)
	{
		try {
			if(Constant.p==null)
				Constant.p =PropertiesLoaderUtils.loadAllProperties(confpath);
			else
				Constant.p.putAll(PropertiesLoaderUtils.loadAllProperties(confpath));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
