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
 * ���ڵ�¼���߳�
 * 
 * @param loginFrame ��¼��JFrame
 * @author ��ţ��֪��
 */
public class LoginThread extends Thread{

	
	private LoginFrame loginFrame;//��¼��
	private JLabel errLabel = new JLabel();//��ʾ��¼ʧ�ܱ�ǩ
	public static MainDialog mainDialog;//������
	public static Weibo weibo;//�û���Ψһ��һ��weibo�����
	public LoginThread(LoginFrame loginFrame){
		this.loginFrame = loginFrame;
		errLabel.setText("");
		loginFrame.setLoginButtonStatus("��¼��...",false);
	}
	public void run() {
		try{
			System.setProperty("weibo4j.oauth.consumerKey", Weibo.CONSUMER_KEY);//����ָ����ָʾ��ϵͳ���ԡ�
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
            String content = "�˺�="+loginFrame.getUserName();
            if(loginFrame.isRemember()){
				content += "\r\n����="+loginFrame.getPassword();	
			}
            try {
				loginFrame.getConfig().write(content);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
            loginFrame.dispose();
            mainDialog = new MainDialog();
		}catch(WeiboException te){
			errLabel.setText("������˺Ŵ��� !");
			errLabel.setFont(new Font("΢���ź�",Font.PLAIN,14));
			errLabel.setBounds(50,170,200,25);
			loginFrame.add(errLabel);
			loginFrame.repaint();
			loginFrame.setLoginButtonStatus("��¼",true);
		}
	}
}
