package simulatedLogin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;

import simulatedLogin.SinaWeiboAutoLogin;

/**
 * 
 * @author Pok
 */
public class Crawler {

	private String htmlSource;
	private String host;
	private String cookie;
	private String chartset;

	/**
	 * 构造函数
	 * 
	 * @param host
	 *            主机地址，如：book.douban.com
	 * @param cookie
	 *            登录cookie
	 * @param chartset
	 *            编码
	 */
	public Crawler(String host, String cookie, String chartset) {
		this.host = host;
		this.cookie = cookie;
		this.chartset = chartset;
	}

	/**
	 * 爬取函数
	 * 
	 * @param url
	 *            待爬取的url
	 * @return content 爬取返回的源代码
	 */
	public String crawler(String url) throws IOException {
		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(url);
		get.setHeader("Accept",
				"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
//		get.setHeader("Accept-Charset", "GBK,utf-8;q=0.7,*;q=0.3");
		get.setHeader("Accept-Encoding", "deflate");
		get.setHeader("Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
//		get.setHeader("Cache-Control", "max-age=0");
		get.setHeader("Cookie", cookie);
		get.setHeader("Connection", "keep-alive");
		get.setHeader("Host", host);
//		get.setHeader(
//				"User-Agent",
//				"Mozilla/5.0 (Windows NT 5.1) AppleWebKit/535.7 (KHTML, like Gecko) Chrome/16.0.912.63 Safari/535.7");
		get.setHeader(
				"User-Agent",
				"Mozilla/5.0 (Windows NT 6.2; WOW64; rv:20.0) Gecko/20100101 Firefox/20.0");
		HttpResponse response = client.execute(get);
		HttpEntity entity = response.getEntity();
		InputStreamReader isr = new InputStreamReader(entity.getContent(),
				chartset);
		BufferedReader br = new BufferedReader(isr);
		StringBuilder sb = new StringBuilder();
		String temp = null;
		while ((temp = br.readLine()) != null) {
			sb.append(temp);
		}
		htmlSource = sb.toString();
		return htmlSource;
	}

	public static void main(String[] args) throws InvalidKeyException,
			IllegalBlockSizeException, BadPaddingException,
			NoSuchAlgorithmException, InvalidKeySpecException,
			NoSuchPaddingException, IOException, JSONException {
		SinaWeiboAutoLogin swa = new SinaWeiboAutoLogin();
		String cookie = swa.getLoginCookie("815192128@qq.com", "Lingy520");
		//String cookie = "U_TRS1=0000002d.269347fe.521a3298.d24d0ea7; SINAGLOBAL=202.116.208.45_1377448600.997984; sso_info=v02m6alo5qztYmVgpWFjbKUuY2ClYGOgpWFjZKVgpGilLiMspWFjYKVgpCSlYKQk4CxiaeVqZmDtLONs5CyjoOcs42zlLE=; U_TRS2=0000002c.959a6fd1.5220d2d3.53d6ddcb; tgc=TGT-Mzc0Mjg3Mzc1MQ==-1377882836-gz-31169C56F2B4A328C537A4FFCC78DA8C; SUS=SID-3742873751-1377882835-GZ-92pnb-04285b3c3686222df4fb6a2293d4d07a; SUE=es%3D96b3ac895d156bede13998130c383b24%26ev%3Dv1%26es2%3Dfcb22cb0f8549c3a44ad26f9d31fc16a%26rs0%3D0t64nhPQQMVIDoKPfr63ekgilEWEgJBbCGWH9Rv8zBTXQpnEV4pHKllOb%252FGI81PNeguywZbleXswwOEdebERTL07GHKtDe8SMMIBxk%252FPZ%252FMnPRHegWWWNAiYzsBnH65My5%252Fy%252B6x9bYuRqaJcCQ3En7NNOpNgrUBzY4%252FJ%252BjRlkgk%253D%26rv%3D0; SUP=cv%3D1%26bt%3D1377882836%26et%3D1377969236%26d%3D40c3%26i%3Dd07a%26us%3D1%26vf%3D0%26vt%3D0%26ac%3D6%26st%3D0%26lt%3D7%26uid%3D3742873751%26user%3Ddancongna%2540163.com%26ag%3D4%26name%3Ddancongna%2540163.com%26nick%3D%25E7%2594%25A8%25E6%2588%25B73742873751%26sex%3D%26ps%3D0%26email%3D%26dob%3D%26ln%3D%26os%3D%26fmp%3D%26lcp%3D; ALC=ac%3D6%26bt%3D1377882836%26cv%3D4.0%26et%3D1380474836%26uid%3D3742873751%26vf%3D0%26vt%3D0%26es%3D90b83ab86f64614a44ebf0a4de9f228c; ALF=1380474836; LT=1377882836";
		String host = "http://weibo.com";
		String charset = "UTF-8";
		Crawler craw = new Crawler(host, cookie, charset);
		System.out.println(craw.crawler("http://weibo.com/p/1005052309846073/info?from=page_100505&mod=TAB#place"));
	}

}