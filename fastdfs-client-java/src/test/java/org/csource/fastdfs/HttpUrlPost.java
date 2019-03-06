package org.csource.fastdfs;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.Map;


public class HttpUrlPost {

    /** 发送http请求超时时间 */
    private static int timeout = Integer.valueOf(60000);
	/** 
     * 向指定URL发送POST请求 
     * @param url 
     * @param paramMap 
     * @return 响应结果 
     */  
    public static String sendPost(String url, Map<String, String> paramMap) {  
        PrintWriter out = null;  
        BufferedReader in = null;  
        String result = "";  
        try {  
            URL realUrl = new URL(url);  
            // 打开和URL之间的连接  
            URLConnection conn = realUrl.openConnection();  
            // 设置通用的请求属性  
            conn.setRequestProperty("accept", "*/*");  
            conn.setRequestProperty("connection", "Keep-Alive");  
            conn.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");  
            conn.setRequestProperty("charset", "UTF-8");  
            // 发送POST请求必须设置如下两行  
            conn.setDoOutput(true);  
            conn.setDoInput(true);  
            // 获取URLConnection对象对应的输出流  
            out = new PrintWriter(conn.getOutputStream());  
  
            // 设置请求属性  
            String param = "";  
            if (paramMap != null && paramMap.size() > 0) {  
                Iterator<String> ite = paramMap.keySet().iterator();  
                while (ite.hasNext()) {  
                    String key = ite.next();// key  
                    String value = paramMap.get(key);  
                    param += key + "=" + value + "&";  
                }  
                param = param.substring(0, param.length() - 1);  
            }  
//            String temp = new String(param.getBytes("utf-8"),"ISO-8859-1");
//            param = URLDecoder.decode(temp, "utf-8");
//            param = URLDecoder.decode(param,"utf-8");
            // 发送请求参数  
            out.print(param);  
            // flush输出流的缓冲  
            out.flush();  
            // 定义BufferedReader输入流来读取URL的响应  
            in = new BufferedReader(  
                    new InputStreamReader(conn.getInputStream()));  
            String line;  
            while ((line = in.readLine()) != null) {  
                result += line;  
            }  
        } catch (Exception e) {  
            System.err.println("发送 POST 请求出现异常！" + e);  
            e.printStackTrace();  
        }  
        // 使用finally块来关闭输出流、输入流  
        finally {  
            try {  
                if (out != null) {  
                    out.close();  
                }  
                if (in != null) {  
                    in.close();  
                }  
            } catch (IOException ex) {  
                ex.printStackTrace();  
            }  
        }  
        return result;  
    }


    public static String sendPost(String url, String data) throws Exception {
        return sendPost(url, data, "", "UTF-8");
    }

    public static String sendPost(String url, String data,String encodeing) throws Exception {
        return sendPost(url, data, "", encodeing);
    }

    /**
     * 发送http post请求，编码方式UTF-8
     *
     * @param data
     * @return
     * @throws Exception
     */
    public static String sendPost(String url, String data, String contentType,String encodeing) throws Exception {
        OutputStream outputStream = null;
        HttpURLConnection conn = null;
        try {
            URL remoteUrl = new URL(url);
            conn = (HttpURLConnection) remoteUrl.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");
            conn.setUseCaches(false);
            conn.setConnectTimeout(timeout);
            conn.setReadTimeout(timeout);
            //if(Detect.notEmpty(contentType)){
                conn.setRequestProperty("Content-type", contentType);
           // }

            // 发送数据
            byte[] datas = data.getBytes("UTF-8");
            //if(Detect.notEmpty(encodeing)){
                datas = data.getBytes(encodeing);
           // }
            outputStream = conn.getOutputStream();
            outputStream.write(datas, 0, datas.length);
            outputStream.flush();
            //log.info("http发送报文:" + data);
            // 读取返回数据
            InputStream inputStream = conn.getInputStream();

            String res = null;
            try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
                byte[] buffer = new byte[256];
                int len = 0;
                while ((len = inputStream.read(buffer)) != -1) {
                    bos.write(buffer, 0, len);
                }
                byte[] readBytes = bos.toByteArray();
                res = new String(readBytes, "UTF-8");
            }

            //log.info("http post 收到响应数据:" + res);
            return res;
        } catch (MalformedURLException e) {
            throw new Exception(e.getMessage());
        } catch (IOException e) {
            throw new Exception(e.getMessage());
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                }
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
    }


    /**
     * @title sendPostBody
     * @description 发送post请求体
     * @author yy
     * @date 2017年7月21日 上午9:56:26
     * @param url
     * @param json
     * @return
     * @return String
     */
    public static String sendPostBody(String url,String json,String code) {
        HttpPost post = new HttpPost(url);
        post.addHeader("Content-Type", "application/json;charset=UTF-8");

        HttpEntity entity = null;
        try {
            entity = new ByteArrayEntity(json.getBytes(code));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        post.setEntity(entity);
        CloseableHttpClient httpClient = null;
        try {
            httpClient = HttpClients.createDefault();
            CloseableHttpResponse res = httpClient.execute(post);
            String rjson = EntityUtils.toString(res.getEntity());
            return rjson;

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (null != httpClient) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * @title sendPostBody 
     * @description 发送post请求体
     * @author yy
     * @date 2017年7月21日 上午9:56:26
     * @param url
     * @param json
     * @return
     * @return String
     */
    public static String sendPostBody(String url,String json) {  
    	  HttpPost post = new HttpPost(url);
          post.addHeader("Content-Type", "application/json;charset=UTF-8");

        HttpEntity entity = null;
        try {
            entity = new ByteArrayEntity(json.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        post.setEntity(entity);
          CloseableHttpClient httpClient = null;
          try {
              httpClient = HttpClients.createDefault();
              CloseableHttpResponse res = httpClient.execute(post);
              String rjson = EntityUtils.toString(res.getEntity());
              return rjson;

          } catch (IOException e) {
        	  throw new RuntimeException(e);
          } finally {
              if (null != httpClient) {
                  try {
                      httpClient.close();
                  } catch (IOException e) {
                	  throw new RuntimeException(e);
                  }
              }
          }
    }  
    
    public static String sendGet(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            /*
             * Map<String, List<String>> map = connection.getHeaderFields(); //
             * 遍历所有的响应头字段 for (String key : map.keySet()) {
             * System.out.println(key + "--->" + map.get(key)); }
             */
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

    public static String toParam(Map<String, Object> map){
        StringBuilder sb = new StringBuilder();
        Iterator<Map.Entry<String, Object>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Object> entry = it.next();
            if(entry.getValue() != null ){
                sb.append(entry.getKey() + "=" + entry.getValue() + "&");
            }
        }
        return sb.toString();
    }
    
    // public static String postFile(String url, String params,File file){
    // //实例化httpClient
    // CloseableHttpClient httpclient = HttpClients.createDefault();
    // //实例化post方法
    // HttpPost httpPost = new HttpPost(url);
    // MultipartEntityBuilder mEntityBuilder = MultipartEntityBuilder.create();
    //
    // RequestConfig requestConfig =
    // RequestConfig.custom().setSocketTimeout(200000).setConnectTimeout(200000).build();//设置请求和传输超时时间
    // httpPost.setConfig(requestConfig);
    //
    // //处理参数
    //
    // // JSONObject jsonParam =JSONObject.parseObject(params);
    //
    // //结果
    // CloseableHttpResponse response = null;
    // String content="";
    // try {
    // String paramsUrl = URLEncoder.encode(params, "utf-8");
    // //提交的参数
    // StringEntity entity = new StringEntity(paramsUrl,"utf-8");
    // entity.setContentEncoding("utf-8");
    // entity.setContentType("application/json");
    //
    // //将参数给post方法
    // httpPost.setEntity(entity);
    // //执行post方法
    // response = httpclient.execute(httpPost);
    // if(response.getStatusLine().getStatusCode()==200){
    // content = EntityUtils.toString(response.getEntity(),"utf-8");
    // //System.out.println(content);
    // }
    // } catch (ClientProtocolException e) {
    // e.printStackTrace();
    // } catch (IOException e) {
    // e.printStackTrace();
    // } finally{
    // // 关闭连接,释放资源
    // try {
    // httpclient.close();
    // } catch (IOException e) {
    // e.printStackTrace();
    // }
    // }
    // return content;
    // }
      
    /** 
     * 测试主方法 
     * @param args 
     * @throws UnsupportedEncodingException 
     */  
    public static void main(String[] args) throws UnsupportedEncodingException {
        String str = "{\"basic_info\":{\"cell_phone_num\":\"15882000127\",\"id_card_num\":\"510104199508024890\",\"name\":\"刘国强\"},\"selected_website\":[],\"skip_mobile\":\"false\",\"uid\":\"APP_ID\"}";
        System.out.println(str.getBytes().length);
    /*	String message = "";
		message = URLDecoder.decode(message, "gb2312");
		
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("message", message);*/
//		String result = HttpUrlPost.sendGet(url, param);
//		String result = HttpUrlPost.sendPost(url, paramMap);
//		System.out.println(result);
    }  
}
