package weibo.woniu.listener;

import java.io.UnsupportedEncodingException;

import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import weibo.woniu.thread.LoginThread;
import weibo.woniu.utility.CommonMethod;
import weibo.woniu.widget.AnotherUser;
import weibo.woniu.widget.ShowImage;
import weibo4j.Status;
import weibo4j.WeiboException;
/**
 * ����΢���ͻ����еĳ�����
 * @version 2.0
 * @author ��ţ��֪��
 */
public class MyHyperlinkListener implements HyperlinkListener{
	private Status status;//һ��΢��
	public MyHyperlinkListener(){
	}
	public MyHyperlinkListener(Status status) {
		this.status = status;
	}


	public void hyperlinkUpdate(HyperlinkEvent e) {
		if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
			String url = e.getDescription();
			if ("".equals(url)) {
				return;
			} 
			else if (url.substring(0, 1).equals("@")) {
				url = url.substring(1, url.length());
				String changeEncode;
				try {
					//ʹ��ָ���ı�����ƽ��ַ���ת��Ϊ application/x-www-form-urlencoded ��ʽ��
					//�÷���ʹ���ṩ�ı�����ƻ�ȡ����ȫ�ַ����ֽڡ�
					changeEncode = java.net.URLEncoder.encode(url,
							"utf-8");
					new AnotherUser(LoginThread.weibo.showUser(changeEncode));
				} catch (UnsupportedEncodingException e1) {
					e1.printStackTrace();
				} catch (WeiboException weiboe) {
					weiboe.printStackTrace();
				}
			} else if (url.substring(0, 1).equals("*")) {
				url = url.substring(1, url.length());
				String str = status.getBmiddle_pic();
				if (!"".equals(str)) {
					new ShowImage(str);
				}
			} else if (url.substring(0, 7).equals("http://")) {
				// substring(int beginIndex, int endIndex)
				// ����һ�����ַ��������Ǵ��ַ�����һ�����ַ���
				String lastOfUrl = url.substring(url.lastIndexOf(".") + 1);
				if ("|gif|jpg|jpeg|png|bmp|".indexOf("|" + lastOfUrl
						+ "|") > -1) {
					new ShowImage(url);
				} else {
					CommonMethod.browse(url);
				}
			}
		}
	}
	
}
