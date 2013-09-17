package simulatedLogin;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

//import org.apache.commons.codec.binary.Base64;
//import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 
 * @author Pok
 */
public class SinaWeiboAutoLogin {

	private String cookie = "";
	private String rsakv;
	private long servertime;
	private String nonce;
	private String pubkey;
	private String sp;
	private String su;

	/**
	 * 使用HttpClient4实现自动微博登陆
	 * 
	 * @param username
	 *            登录账号
	 * @param password
	 *            登录密码
	 * @return cookie 登陆后返回的cookie
	 */
	public String getLoginCookie(String username, String password)
			throws IOException, JSONException, IllegalBlockSizeException,
			BadPaddingException, NoSuchAlgorithmException,
			InvalidKeySpecException, NoSuchPaddingException,
			NoSuchPaddingException, InvalidKeyException {
		DefaultHttpClient client = new DefaultHttpClient();
		client.getParams().setParameter("http.protocol.cookie-policy",
				CookiePolicy.BROWSER_COMPATIBILITY);
		client.getParams().setParameter(
				HttpConnectionParams.CONNECTION_TIMEOUT, 5000);
		getParam(client);
		sp = rsaCrypt(pubkey, "10001", password, servertime, nonce);
		su = encodeUserName(username);
		String url = login(client);
		if (url.equals("-1")) {
			System.out.println("登录失败！");
			return "";
		} else {
			HttpGet getMethod = new HttpGet(url);
			HttpResponse response = client.execute(getMethod);
			for (Cookie c : client.getCookieStore().getCookies()) {
				String s = c.toString();
				String name = s.substring(s.indexOf("name:") + 5,
						s.indexOf("][value")).trim();
				String value = s.substring(s.indexOf("value:") + 6,
						s.indexOf("][domain"));
				s = name + "=" + value + ";";
				cookie += s;
			}
			//System.out.println("1:"+cookie);
		}
		return cookie;
	}

	/*
	 * 获取登录参数
	 */
	private void getParam(DefaultHttpClient client) throws IOException,
			JSONException {
		String preloginurl = "http://login.sina.com.cn/sso/prelogin.php?entry=sso&"
				+ "callback=sinaSSOController.preloginCallBack&su="
				+ "dW5kZWZpbmVk"
				+ "&rsakt=mod&client=ssologin.js(v1.4.5)"
				+ "&_=" + getCurrentTime();
		HttpGet get = new HttpGet(preloginurl);
		HttpResponse response = client.execute(get);
		String getResp = EntityUtils.toString(response.getEntity());
		int firstLeftBracket = getResp.indexOf("(");
		int lastRightBracket = getResp.lastIndexOf(")");
		String jsonStr = getResp.substring(firstLeftBracket + 1,
				lastRightBracket);
		JSONObject jsonInfo = new JSONObject(jsonStr);
		nonce = jsonInfo.getString("nonce");
		pubkey = jsonInfo.getString("pubkey");
		rsakv = jsonInfo.getString("rsakv");
		servertime = jsonInfo.getLong("servertime");
	}

	private String getCurrentTime() {
		return String.valueOf(new Date().getTime() / 1000);
	}

	/*
	 * 模拟加密过程
	 *   username 经过了BASE64 计算： username = base64.encodestring( urllib.quote(username) )[:-1];
         password 经过了三次SHA1 加密， 且其中加入了 servertime 和 nonce 的值来干扰。
                       即： 两次SHA1加密后， 将结果加上 servertime 和 nonce 的值， 再SHA1 算一次。
	 */
	private String rsaCrypt(String modeHex, String exponentHex,
			String password, long serverTime, String nonce)
			throws IllegalBlockSizeException, BadPaddingException,
			NoSuchAlgorithmException, InvalidKeySpecException,
			NoSuchPaddingException, InvalidKeyException,
			UnsupportedEncodingException {
		KeyFactory factory = KeyFactory.getInstance("RSA"); //返回转换指定算法的 public/private 关键字的 KeyFactory 对象
		BigInteger m = new BigInteger(modeHex, 16); /* public exponent */   //把16进制转换为10进制 pubkey
		BigInteger e = new BigInteger(exponentHex, 16); /* modulus */                       //password
		RSAPublicKeySpec spec = new RSAPublicKeySpec(m, e);  //此类指定 RSA 公用密钥,创建一个新的 RSAPublicKeySpec。
		RSAPublicKey pub = (RSAPublicKey) factory.generatePublic(spec);  //根据提供的密钥规范（密钥材料）生成公钥对象。
		//http://blog.csdn.net/wuwenlong527/article/details/2037931 介绍cipher
		Cipher enc = Cipher.getInstance("RSA");  //此类为加密和解密提供密码功能
		enc.init(Cipher.ENCRYPT_MODE, pub); // 用密钥初始化此 Cipher
		//Cipher.ENCRYPT_MODE 用于将 Cipher 初始化为加密模式的常量。
		String confusrPassword = serverTime + "\t" + nonce + "\n" + password;
		byte[] encryptedContentKey = enc.doFinal(confusrPassword.getBytes("GB2312")); //加密数据
		String encodeString = new String(Hex.encodeHex(encryptedContentKey)); //把加密后的数据转换为16进制
		return encodeString;
	}

	/*
	 * 将名字用basde64编码
	 */
	
	private String encodeUserName(String username) {
		username = username.replaceFirst("@", "%40");
		return Base64.encodeBase64String(username.getBytes());
	}

	/*
	 * post头部得到正式登录的URL
	 */
	private String login(DefaultHttpClient client)
			throws UnsupportedEncodingException, IOException {
		HttpPost post = new HttpPost(
				"http://login.sina.com.cn/sso/login.php?client=ssologin.js(v1.4.5)");
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("entry", "weibo"));
		nvps.add(new BasicNameValuePair("gateway", "1"));
		nvps.add(new BasicNameValuePair("from", ""));
		nvps.add(new BasicNameValuePair("savestate", "7"));
		nvps.add(new BasicNameValuePair("useticket", "1"));
		nvps.add(new BasicNameValuePair("ssosimplelogin", "1"));
		nvps.add(new BasicNameValuePair("vsnf", "1"));
		nvps.add(new BasicNameValuePair("su", su));
		nvps.add(new BasicNameValuePair("service", "miniblog"));
		nvps.add(new BasicNameValuePair("servertime", servertime + ""));
		nvps.add(new BasicNameValuePair("nonce", nonce));
		nvps.add(new BasicNameValuePair("pwencode", "rsa2"));
		nvps.add(new BasicNameValuePair("rsakv", rsakv));
		nvps.add(new BasicNameValuePair("sp", sp));
		nvps.add(new BasicNameValuePair("encoding", "UTF-8"));
		nvps.add(new BasicNameValuePair("prelt", "115"));
		nvps.add(new BasicNameValuePair("returntype", "META"));
		nvps.add(new BasicNameValuePair(
				"url",
				"http://weibo.com/ajaxlogin.php?framelogin=1&callback=parent.sinaSSOController.feedBackUrlCallBack"));
		post.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
		HttpResponse response = client.execute(post);
		String entity = EntityUtils.toString(response.getEntity());
		String url = "";
		try {
			url = entity.substring(
					entity.indexOf("http://weibo.com/ajaxlogin.php?"),
					entity.indexOf("code=0") + 6);
		} catch (Exception e) {
			return "-1";
		}
		return url;
	}

	public String getCookie() {
		return cookie;
	}

//	public static void main(String[] a) throws IOException, JSONException,
//			IllegalBlockSizeException, IllegalBlockSizeException,
//			IllegalBlockSizeException, BadPaddingException,
//			BadPaddingException, BadPaddingException, NoSuchAlgorithmException,
//			NoSuchAlgorithmException, InvalidKeySpecException,
//			InvalidKeyException, NoSuchPaddingException, NoSuchPaddingException {
//		SinaWeiboAutoLogin hj = new SinaWeiboAutoLogin();
//		String cookie = hj.getLoginCookie("gdufstui01@163.com", "201201");
//		System.out.println("hjhjhj  " + cookie);
//	}
}