package com.jinhuhang.fastdfs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

public class ThreadTest {
	private static int num=100;
	public static AtomicInteger c = new AtomicInteger(num);
	public static StringBuffer sb = new StringBuffer();
	
	
	public static void main(String[] args) throws Exception {
		
		ExecutorService pool=Executors.newFixedThreadPool(10);
		
//		List<Thread> ls=new ThreadTest().getThread(3);
		long before = System.currentTimeMillis();
		Set<Callable<Integer>> set = new HashSet();
		for (int i = 0; i < 10; i++) {
			set.add(getCallable(0));
		}
		
		append(DateFormatUtils.format(before, "yyyy-MM-dd HH:mm:ss")+":开始上传文件["+num+"]个");
		List<Future<Integer>> list=pool.invokeAll(set);
		
		while(true){
			boolean compelete = true;
			for (Future<Integer> future : list) {
				if(!future.isDone())
					compelete=false;
			}
			
			if(compelete)
				break;
		}
		
		
		
		long after = System.currentTimeMillis();
		append(DateFormatUtils.format(after, "yyyy-MM-dd HH:mm:ss")+":上传完毕，花了["+(after-before)/1000+"]秒");
		
		FileUtils.writeStringToFile(new File("D:/testReport/"+num+".txt"), sb.toString());
	}
	private static Callable<Integer> getCallable(int size){
		return new Callable<Integer>() {

			@Override
			public Integer call() throws Exception {
				while(true){
					int i=c.getAndDecrement();
					if(i<=0)
						break;
					// TODO Auto-generated method stub
					byte[] data = null;
					try {
						data = FileUtils.readFileToByteArray(new File("C:\\Users\\yangyong\\Desktop\\1-11.png"));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					String url=HttpUrlPost.sendPostBody("http://localhost:9200/upload?token=jinhuhangpic123&suffix=png",
							data);
					append(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss")+":"+i+"="+url);
				}
				return 0;
			}
			
		};
	}
	private List<Thread> getThread(int size){
		
		List<Thread> list = new ArrayList<Thread>();
		for (int i = 0; i < size; i++) {
			list.add(new Thread(new Runnable() {
				
				@Override
				public void run() {
					while(true){
						int i=c.getAndDecrement();
						if(i<=0)
							break;
						// TODO Auto-generated method stub
						byte[] data = null;
						try {
							data = FileUtils.readFileToByteArray(new File("C:\\Users\\yangyong\\Desktop\\1-11.png"));
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						String url=HttpUrlPost.sendPostBody("http://localhost:9200/upload?token=jinhuhangpic123&suffix=png",
								data);
						append(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss")+":"+i+"="+url);
					}
				}
			}));
		}
		
		return list;
	}
	
	
	private static synchronized void append(String str){
		sb.append(str+"\n");
	}
}
