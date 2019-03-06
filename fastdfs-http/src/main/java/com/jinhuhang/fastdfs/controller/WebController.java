package com.jinhuhang.fastdfs.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jinhuhang.fastdfs.common.FDFSUtil;
import com.jinhuhang.fastdfs.common.MemcacheUtil;

/**
 * @project FastdfsHttp
 * @author yy
 * @date 2018年2月24日 下午4:05:09
 * @description TODO 接收外部请求
 * @tag 
 * @company 上海金互行金融信息服务有限公司
 */
@Slf4j
@Controller
public class WebController {
	/**
	 * 硬编码验证
	 * @author yy 
	 * @date 2018年2月24日 下午4:10:25
	 */
	private final String contToken="jinhuhangpic123";
	
	@RequestMapping("/upload")
	public String upload(String suffix,String token,@RequestBody byte[] data){
		if(!token.equals(contToken))
			return "unkown token";
		log.info("上传文件大小："+data.length);
		try {
			return FDFSUtil.uploadFile(data, suffix);
		} catch (Exception e) {
			log.error("上传文件失败：",e);
			return "upload error";
		}
	}

	@RequestMapping("/")
	public String index(){
		return "图片服务器启动成功";
	}
	
	/**
	 * @title getFile 
	 * @description 根据key 从缓存中获取值
	 * @author yy
	 * @date 2018年5月29日 下午6:42:30
	 * @return void
	 */
	@RequestMapping("/getFile")
	public void getFile(HttpServletRequest request,HttpServletResponse response){
		
		String url =(String) request.getParameter("url");
		ServletOutputStream out =null;
		try {
			out= response.getOutputStream(); 
			byte[] n =MemcacheUtil.get(url);
			System.out.println(n.length);
			out.write(MemcacheUtil.get(url));
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
