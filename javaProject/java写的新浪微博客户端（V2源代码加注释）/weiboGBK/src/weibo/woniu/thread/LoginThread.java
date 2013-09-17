package weibo.woniu.thread;

import java.awt.Font;
import java.io.IOException;

import javax.swing.JLabel;

import weibo.woniu.widget.LoginFrame;
import weibo.woniu.widget.MainDialog;
import weibo4j.OAuthVerifier;
import weibo4j.Weibo;
import weibo4j.WeiboException;
import weibo4j.http.AccessToken;
import weibo4j.http.RequestToken;
/**
 * 用于登录的线程
 * 
 * @param loginFrame 登录的JFrame
 * @author 蜗牛都知道
 */
public class LoginThread extends Thread{

	
	private LoginFrame loginFrame;//登录框
	private JLabel errLabel = new JLabel();//提示登录失败标签
	public static MainDialog mainDialog;//主界面
	public static Weibo weibo;//用户的唯一的一个weibo类对象
	public LoginThread(LoginFrame loginFrame){
		this.loginFrame = loginFrame;
		errLabel.setText("");
		loginFrame.setLoginButtonStatus("登录中...",false);
	}
	public void run() {
		try{
			System.setProperty("weibo4j.oauth.consumerKey", Weibo.CONSUMER_KEY);//设置指定键指示的系统属性。
	    	System.setProperty("weibo4j.oauth.consumerSecret", Weibo.CONSUMER_SECRET);
			weibo = new Weibo(loginFrame.getUserName(),loginFrame.getPassword());
            RequestToken requestToken = weibo.getOAuthRequestToken();
            System.out.println("Got request token.");
            System.out.println("Request token: "+ requestToken.getToken());
            System.out.println("Request token secret: "+ requestToken.getTokenSecret());
            
            OAuthVerifier verifier = weibo.getOAuthVerifier(requestToken);
            
            AccessToken accessToken = weibo.getOAuthAccessToken(requestToken,verifier.getVerifier());
            System.out.println("Got access token:" + verifier.getVerifier());
            System.out.println("Access token: "+ accessToken.getToken());
            System.out.println("Access token secret: "+ accessToken.getTokenSecret());
            weibo.setToken(accessToken.getToken(),accessToken.getTokenSecret());
            String content = "账号="+loginFrame.getUserName();
            if(loginFrame.isRemember()){
				content += "\r\n密码="+loginFrame.getPassword();	
			}
            try {
				loginFrame.getConfig().write(content);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
            loginFrame.dispose();
            mainDialog = new MainDialog();
		}catch(WeiboException te){
			errLabel.setText("密码或账号错误 !");
			errLabel.setFont(new Font("微软雅黑",Font.PLAIN,14));
			errLabel.setBounds(50,170,200,25);
			loginFrame.add(errLabel);
			loginFrame.repaint();
			loginFrame.setLoginButtonStatus("登录",true);
		}
	}
}
