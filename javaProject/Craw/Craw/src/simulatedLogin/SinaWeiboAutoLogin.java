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
	 * ʹ��HttpClient4ʵ���Զ�΢����½
	 * 
	 * @param username
	 *            ��¼�˺�
	 * @param password
	 *            ��¼����
	 * @return cookie ��½�󷵻ص�cookie
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
			System.out.println("��¼ʧ�ܣ�");
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

	private String rsaCrypt(String modeHex, String exponentHex,
			String password, long serverTime, String nonce)
			throws IllegalBlockSizeException, BadPaddingException,
			NoSuchAlgorithmException, InvalidKeySpecException,
			NoSuchPaddingException, InvalidKeyException,
			UnsupportedEncodingException {
		KeyFactory factory = KeyFactory.getInstance("RSA"); //����ת��ָ���㷨�� public/private �ؼ��ֵ� KeyFactory ����
		BigInteger m = new BigInteger(modeHex, 16); /* public exponent */   //��16����ת��Ϊ10����
		BigInteger e = new BigInteger(exponentHex, 16); /* modulus */
		RSAPublicKeySpec spec = new RSAPublicKeySpec(m, e);  //����ָ�� RSA ������Կ,����һ���µ� RSAPublicKeySpec��
		RSAPublicKey pub = (RSAPublicKey) factory.generatePublic(spec);  //�����ṩ����Կ�淶����Կ���ϣ����ɹ�Կ����
		Cipher enc = Cipher.getInstance("RSA");  //����Ϊ���ܺͽ����ṩ���빦��
		enc.init(Cipher.ENCRYPT_MODE, pub); // ����Կ��ʼ���� Cipher
		//Cipher.ENCRYPT_MODE ���ڽ� Cipher ��ʼ��Ϊ����ģʽ�ĳ�����
		String confusrPassword = serverTime + "\t" + nonce + "\n" + password;
		byte[] encryptedContentKey = enc.doFinal(confusrPassword
				.getBytes("GB2312")); //��������
		String encodeString = new String(Hex.encodeHex(encryptedContentKey)); //�Ѽ��ܺ������ת��Ϊ16����
		return encodeString;
	}

	private String encodeUserName(String username) {
		username = username.replaceFirst("@", "%40");
		return Base64.encodeBase64String(username.getBytes());
	}

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
