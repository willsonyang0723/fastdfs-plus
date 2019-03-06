package org.csource.common;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeoutException;

import org.apache.commons.lang3.ArrayUtils;
import org.csource.fastdfs.ClientGlobal;

import net.rubyeye.xmemcached.XMemcachedClient;
import net.rubyeye.xmemcached.exception.MemcachedException;

/**
 * @project fastdfs-client-java
 * @author yy
 * @date 2018年5月29日 下午6:31:27
 * @description TODO Mecache工具类
 * @tag 
 * @company 上海金互行金融信息服务有限公司
 */
public class MemcacheUtil {
	private static XMemcachedClient memcache ;

	  private void initMemcacheClient(){
		try {
			memcache = new XMemcachedClient(new InetSocketAddress(ClientGlobal.g_memcache_server.split(":")[0].toString(), Integer.valueOf(ClientGlobal.g_memcache_server.split(":")[1])));
		} catch (Exception e) {
			throw new RuntimeException("memcache初始化失败");
		}
	  }

	public static void set(String key,int expSeconds,byte[] value){
		if(memcache==null)
			new MemcacheUtil().initMemcacheClient();
		if(value.length>10240000)
			throw new RuntimeException("上传文件大于10M");
		List<byte[]> storeByteList = new ArrayList();
		for(int i=0;i*1024000 < value.length;i++){
			if(i*1024000+1024000>value.length)
				storeByteList.add(Arrays.copyOfRange(value, i*1024000, value.length));
			else
				storeByteList.add(Arrays.copyOfRange(value, i*1024000, i*1024000+1024000));
		}
		long b = System.currentTimeMillis();
		for (int i = 0; i < storeByteList.size(); i++) {
			try {
				memcache.set(key+"."+i, expSeconds,storeByteList.get(i), 3000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		long a =System.currentTimeMillis();
		System.out.println(key+" memcache存储花费"+(a-b)+"毫秒");
		
	}
	
	/**
	 * @title get 
	 * @description 获取memcache
	 * @author yy
	 * @date 2018年5月29日 下午6:35:40
	 * @param key
	 * @return void
	 */
	public static byte[] get(String key){
		if(memcache==null)
			new MemcacheUtil().initMemcacheClient();
		byte[] tvalue = null;
		try {
			for (int i = 0; i < 10; i++) {
				byte[] value= memcache.get(key+"."+i);
				if(value==null || value.length==0)
					break;
				tvalue=ArrayUtils.addAll(tvalue, value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return tvalue;
	}
}
