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



public class TestMem1 {

	public static void main(String[] args) {
		try {
			String bvalue = Base64.getMimeEncoder().encodeToString(FileUtils.readFileToByteArray(new File("D:/1.pdf")));
			System.out.println(bvalue.length());
			if(bvalue.length()>10240000)
				throw new RuntimeException("上传文件大于10M");
			List<String> storeByteList = new ArrayList();
			for(int i=0;i*1024000 < bvalue.length();i++){
				if(i*1024000+1024000>bvalue.length())
					storeByteList.add(bvalue.substring(i*1024000, bvalue.length()));
				else
					storeByteList.add(bvalue.substring(i*1024000, i*1024000+1024000));
			}
			
			InetSocketAddress ia = new InetSocketAddress("192.168.1.126", 12121);
			XMemcachedClient x = new XMemcachedClient(ia);
			System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXX");
			long b = System.currentTimeMillis();
			for (int i = 0; i < storeByteList.size(); i++) {
				System.out.println(storeByteList.get(i).length());
				x.set("http://192.168.1.126:80/1.pdf."+i, 6000,storeByteList.get(i), 5000);
			}
			long a = System.currentTimeMillis();
			System.out.println((a-b));
			System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXX");

			System.out.println("http://192.168.1.126:80/1.pdf.0");
			System.out.println(x.get("http://192.168.1.126:80/1.pdf.0").toString().length());
			System.out.println("http://192.168.1.126:80/1.pdf.1");
			System.out.println(x.get("http://192.168.1.126:80/1.pdf.1").toString().length());
			String f = x.get("http://192.168.1.126:80/1.pdf.0").toString()+x.get("http://192.168.1.126:80/1.pdf.1").toString();
			System.out.println(f.length());
			FileUtils.writeByteArrayToFile(new File("D:/2.pdf"), Base64.getMimeDecoder().decode(f));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		
		
		
		
	}
}
