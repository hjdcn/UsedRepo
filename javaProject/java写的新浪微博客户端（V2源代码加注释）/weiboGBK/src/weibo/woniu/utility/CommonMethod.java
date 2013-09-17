package weibo.woniu.utility;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;

import weibo4j.User;
/**
 * 公众通用的方法。
 * @version 2.0
 * @author 蜗牛都知道
 */
public class CommonMethod {
	/**
	 * 获取图片的宽度
	 * 通用方法
     * @param 图片的地址(String类型)
     * @return 图片的宽带(int类型)
	 * @version 2.0
     * @author 蜗牛都知道
     */
	public static String getGender(String gender){
		String temp = null;
		if (gender.equals("f")) {
			temp = "女 ";
		} else if (gender.equals("m")) {
			temp = "男 ";
		}
		return temp;
	}
	
	
	/**
	 * 获取图片的宽度
	 * 通用方法
     *@param 图片的地址(String类型)
     *@return 图片的宽带(int类型)
     *@version 2.0
     *@author 蜗牛都知道
     */
	public static int getImgHeight(String imgURL) throws NullPointerException{
		java.net.URL url = null;
		try {
			url = new URL(imgURL);
		} catch (MalformedURLException e) {
			System.out.print(" ：载入图片失败");
		}
		//TODO BufferedImage 子类描述具有可访问图像数据缓冲区的 Image
		//使用BufferedImage 才能再未显现图片时知道图片的大小
		BufferedImage bi = null;
		try {
			bi = javax.imageio.ImageIO.read(url);
		} catch (IOException e) {
			System.out.print(" ：载入图片失败");
		} catch (java.lang.IllegalArgumentException e) {
			System.out.print(" ：载入图片失败");
		}
		return bi.getHeight();
	}

	
	/**
	 *初始化每个JTextPane
	 *为静态类型，这个方法在CommentPanel等也使用到。
     *@return JTextPane
	 *@version 2.0
     *@author 蜗牛都知道
     */
	public static JTextPane initTextPanel() {
		// TODO Auto-generated method stub
		JTextPane textPanel = new JTextPane();
		textPanel.setEditable(false);
		textPanel.setContentType("text/html");
		textPanel.setBackground(new Color(0xeeeeee));
		return textPanel;
	}
	/**
	 * 把微博/评论等等发送的时间(Date类型)"格式化"，并转化成"String类型"。
	 * 为静态类型，这个方法在CommentPanel等也使用到。
     *@param 发送的时间(date类型)
     *@return 发送的时间(String类型)
     *@version 2.0
     *@author 蜗牛都知道
     */
	public static String getCreateDate(Date createDate){
		SimpleDateFormat formatter = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		return formatter.format(createDate);
	}
	
	/**
	 *正则表达式，还不 
	 *为静态类型，这个方法在CommentPanel等也使用到。
     *@param String类型
     *@return String类型
     *@version 2.0
     *@author 蜗牛都知道
     */
	public static String setForms(String html) {
		html = "<font face=\"微软雅黑\" size=\"4\">" + html + "</font>";
		html = html.replaceAll(
				"@([^<^>^,^ ^：^。^:^：^/^&^@^#^\"^\']{1,})([：, :/<])",
				"<a href=@$1>@$1</a>$2");
		html = html.replaceAll("#([^<^>^,^ ^:^/^&^@^#^\"^\']{1,})#",
				"<a href=#$1>#$1#</a>");
		html = html.replaceAll(
				"([^href=^href=\"])(http://[-a-zA-Z0-9:%_\\+.~#?&/=]{1,})",
				"$1<a href=$2>$2</a>");
		html = html
				.replaceAll("<a[^>]* href=\"\"[^>]*>([^<^>]*)</a>", "$1");
		html = html.replaceAll("<a[^>]* href=>([^<^>]*)</a>", "$1");
		return html;
	}
	/**
	 * 初始化选项卡的每个面块 （主页 @ 评论 私信 收藏 粉丝 关注 微博）:选项卡的名称
	 * @param String类型，一个选项卡的名字：
	 * @return JPanel
	 * @version 2.0
	 * @author 蜗牛都知道
	 */
	public static JPanel initTab (JTabbedPane tabbedPane, String tabName, User user) {
		JPanel tabPanel = new JPanel();
		tabPanel.setLayout(null);
		tabbedPane.addTab(tabName, null, tabPanel, null);
		HeadPanel headPanel = null;// 每个选项卡的上半部分
		if(user != null){
			headPanel = new HeadPanel(user);
		}
		else
			headPanel = new HeadPanel();
		tabPanel.add(headPanel);
		return tabPanel;
	}
	/**
	 * 调用浏览器打开一个网址
     *@author 蜗牛都知道
     *@param String类型的地址
     */
	 public static void browse(String url){
		 try{
		 	//获取用指定键描述的系统属性，这里是系统名字
		     String osName = System.getProperty("os.name", "");
		     if (osName.startsWith("Mac OS")) {
		         Class fileMgr = Class.forName("com.apple.eio.FileManager");
		         Method openURL = fileMgr.getDeclaredMethod("openURL", new Class[] { String.class });
		         openURL.invoke(null, new Object[] { url });
		     } else if (osName.startsWith("Windows")) {
		         Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
		     } else { // assume Unix or Linux
		         String[] browsers = { "firefox", "opera", "konqueror", "epiphany", "mozilla", "netscape" };
		         String browser = null;
		         for (int count = 0; count < browsers.length && browser == null; count++)
		             if (Runtime.getRuntime().exec(new String[] { "which", browsers[count] }).waitFor() == 0)
		                 browser = browsers[count];
		         if (browser == null)
		             throw new NoSuchMethodException("Could not find web browser");
		         else
		             Runtime.getRuntime().exec(new String[] { browser, url });
		     }
		 }catch(Exception e){
			 System.out.println("找不到浏览器！");
		 }
	 }
}
