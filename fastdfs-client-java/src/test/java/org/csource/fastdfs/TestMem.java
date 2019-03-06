package org.csource.fastdfs;

import java.io.File;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.rubyeye.xmemcached.XMemcachedClient;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.auth.AuthInfo;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;



public class TestMem {

	public static void main(String[] args) {
		try {
			byte[] value=FileUtils.readFileToByteArray(new File("D:/1.pdf"));
			
			System.out.println(value.length);
			if(value.length>10240000)
				throw new RuntimeException("上传文件大于10M");
			List<byte[]> storeByteList = new ArrayList();
			for(int i=0;i*1024000 < value.length;i++){
				if(i*1024000+1024000>value.length)
					storeByteList.add(Arrays.copyOfRange(value, i*1024000, value.length));
				else
					storeByteList.add(Arrays.copyOfRange(value, i*1024000, i*1024000+1024000));
			}
			
			InetSocketAddress ia = new InetSocketAddress("192.168.1.126", 12121);
			XMemcachedClient x = new XMemcachedClient(ia);
			System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXX");
			long b = System.currentTimeMillis();
			for (int i = 0; i < storeByteList.size(); i++) {
				System.out.println(storeByteList.get(i).length);
				x.set("http://192.168.1.126:80/1.pdf."+i, 6000,storeByteList.get(i), 5000);
			}
			long a = System.currentTimeMillis();
			System.out.println((a-b));
			System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXX");
			/*XMemcachedClientBuilder builder = new XMemcachedClientBuilder();
			builder.setAuthInfoMap();
			MemCachedClient mc = new MemCachedClient("192.168.1.126:12121", true, true);
			
			byte[] value=FileUtils.readFileToByteArray(new File("D:/test.png"));
			boolean r=mc.set("test1", value, 3000);*/
//			byte[] totalbyte= new byte[0];
//			for (int i = 0; i < 10; i++) {
//				byte[] s =x.get("test"+i); 
//				if(s!=null && s.length>0)
//					totalbyte =ArrayUtils.addAll(totalbyte, s);
//				System.out.println(totalbyte.length);
//			}
//			
//			FileUtils.writeByteArrayToFile(new File("D:/2.pdf"), totalbyte);
//			System.out.println(x.get("http://192.168.1.126:80/1.pdf.0"));
			byte[] vall = x.get("http://192.168.1.126:80/1.pdf.0");
			byte[] val11 = x.get("http://192.168.1.126:80/1.pdf.1");
			byte[] val=ArrayUtils.addAll(vall, val11);
			System.out.println(val.length);
			FileUtils.writeByteArrayToFile(new File("D:/2.pdf"), val);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		
		
		
		
	}
}
