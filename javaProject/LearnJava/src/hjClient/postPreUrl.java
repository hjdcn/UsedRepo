package hjClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

import thread.test2;



public class postPreUrl {
	
	
	public static void main(String []a ){
		test2 te=new test2();
		//String url="http://login.sina.com.cn/sso/login.php?client=ssologin.js(v1.3.18)";
		String url = "http://login.sina.com.cn/sso/prelogin.php?entry=sso&"
				+ "callback=sinaSSOController.preloginCallBack&su="
				+ "dW5kZWZpbmVk"
				+ "&rsakt=mod&client=ssologin.js(v1.4.5)"
				+ "&_=";
		//String url="http://www.baidu.com/";
		HttpClient httpClient = new HttpClient();
		/*
		httpClient.getHostConfiguration().setProxy("130.136.254.21", 3124);   
		  �����ڲɼ����ݵ�ʱ����������Щ��̳�����ݣ�������Ҫͨ�����ô�������ɶ�ָ����վ��Դ�����У�
		  ���������������ڹ����Ĺ����������޷�֮�佨����Ŀ����վ��Http���Ӷ�������ԣ����ջ����޷�ȡ����ҳ��Դ��
        //��Ҫ��֤  
        UsernamePasswordCredentials creds = new UsernamePasswordCredentials("20111003637", "246952");  
 
        httpClient.getState().setProxyCredentials(AuthScope.ANY, creds);  
         
        */
        //����httpͷ   
//        List<Header> headers = new ArrayList<Header>();   
//        headers.add(new Header("User-Agent", "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1)"));   
//        httpClient.getHostConfiguration().getParams().setParameter("http.default-headers", headers);   

		GetMethod getmethod = new GetMethod(url);
		getmethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,new DefaultHttpMethodRetryHandler());
		try {
			httpClient.executeMethod(getmethod);
			String respone = getmethod.getResponseBodyAsString();
			System.out.println(respone);
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

//	public static void main(String [] a){
//		String url=""
//	}
}
