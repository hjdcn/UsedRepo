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
		String host = "http://weibo.com";
		String charset = "UTF-8";
		Crawler craw = new Crawler(host, cookie, charset);
		System.out.println(craw.crawler("http://weibo.com/2149436624/info"));
	}

}
