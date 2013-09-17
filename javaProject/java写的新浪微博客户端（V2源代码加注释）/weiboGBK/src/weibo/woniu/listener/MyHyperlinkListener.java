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
 * 处理微博客户端中的超链接
 * @version 2.0
 * @author 蜗牛都知道
 */
public class MyHyperlinkListener implements HyperlinkListener{
	private Status status;//一条微博
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
					//使用指定的编码机制将字符串转换为 application/x-www-form-urlencoded 格式。
					//该方法使用提供的编码机制获取不安全字符的字节。
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
				// 返回一个新字符串，它是此字符串的一个子字符串
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
