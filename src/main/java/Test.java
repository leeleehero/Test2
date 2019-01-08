import com.baidu.aip.http.AipHttpClient;
import com.baidu.aip.util.Base64Util;
import com.baidu.aip.util.Util;
import org.json.HTTP;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;



import java.io.*;
import java.net.URLEncoder;

/**
 * @author LITAO
 * @version V1.0
 * @Package PACKAGE_NAME
 * @Title:
 * @Description:
 * @date 2019/1/8 13:36
 */
public class Test {
    public static void main(String[] args) {
        String url = "https://aip.baidubce.com/rest/2.0/ocr/v1/license_plate";
        String fileUrl = "C:\\Users\\LITAO\\Desktop\\)]T5H83O9F943_ZPO}]Y@[B.png";
        String parms = null;
        String img = null;
        try {
            byte[] file = Util.readFileByBytes(fileUrl);
            img = Base64Util.encode(file);
  //          System.out.println(img);
            parms= URLEncoder.encode(img, "utf-8");
//            System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++");
//            System.out.println(parms);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String access_token = AuthService.getAuth();
        String newUrl = url + "?access_token=" + access_token;
        Map<String, Object> header = new HashMap<String, Object>();
        header.put("Content-Type", "application/x-www-form-urlencoded");
        Map<String, String> body = new HashMap<String, String>();
        body.put("group_id", "group001");
        body.put("user_id", "001");
        body.put("image", img);
        String res = postMap(newUrl,header,body);
        System.out.println(res);
    }



    public static String postMap(String url, Map<String, Object> headerMap, Map<String, String> contentMap) {
        String result = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost post = new HttpPost(url);
        List<NameValuePair> content = new ArrayList<NameValuePair>();
        Iterator iterator = contentMap.entrySet().iterator();           //将content生成entity
        while(iterator.hasNext()){
            Entry<String,String> elem = (Entry<String, String>) iterator.next();
            content.add(new BasicNameValuePair(elem.getKey(),elem.getValue()));
        }
        CloseableHttpResponse response = null;
        try {
            Iterator headerIterator = headerMap.entrySet().iterator();          //循环增加header
            while(headerIterator.hasNext()){
                Entry<String,String> elem = (Entry<String, String>) headerIterator.next();
                post.addHeader(elem.getKey(),elem.getValue());
            }
            if(content.size() > 0){
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(content,"UTF-8");
                post.setEntity(entity);
            }
            response = httpClient.execute(post);//发送请求并接收返回数据
            if(response != null && response.getStatusLine().getStatusCode() == 200)
            {
                HttpEntity entity = response.getEntity();       //获取response的body部分
                result = EntityUtils.toString(entity,"utf-8");          //读取reponse的body部分并转化成字符串
            }
            return result;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                httpClient.close();
                if(response != null)
                {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return null;
    }


}


    