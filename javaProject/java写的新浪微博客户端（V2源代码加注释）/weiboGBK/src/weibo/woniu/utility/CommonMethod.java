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
 * ����ͨ�õķ�����
 * @version 2.0
 * @author ��ţ��֪��
 */
public class CommonMethod {
	/**
	 * ��ȡͼƬ�Ŀ��
	 * ͨ�÷���
     * @param ͼƬ�ĵ�ַ(String����)
     * @return ͼƬ�Ŀ��(int����)
	 * @version 2.0
     * @author ��ţ��֪��
     */
	public static String getGender(String gender){
		String temp = null;
		if (gender.equals("f")) {
			temp = "Ů ";
		} else if (gender.equals("m")) {
			temp = "�� ";
		}
		return temp;
	}
	
	
	/**
	 * ��ȡͼƬ�Ŀ��
	 * ͨ�÷���
     *@param ͼƬ�ĵ�ַ(String����)
     *@return ͼƬ�Ŀ��(int����)
     *@version 2.0
     *@author ��ţ��֪��
     */
	public static int getImgHeight(String imgURL) throws NullPointerException{
		java.net.URL url = null;
		try {
			url = new URL(imgURL);
		} catch (MalformedURLException e) {
			System.out.print(" ������ͼƬʧ��");
		}
		//TODO BufferedImage �����������пɷ���ͼ�����ݻ������� Image
		//ʹ��BufferedImage ������δ����ͼƬʱ֪��ͼƬ�Ĵ�С
		BufferedImage bi = null;
		try {
			bi = javax.imageio.ImageIO.read(url);
		} catch (IOException e) {
			System.out.print(" ������ͼƬʧ��");
		} catch (java.lang.IllegalArgumentException e) {
			System.out.print(" ������ͼƬʧ��");
		}
		return bi.getHeight();
	}

	
	/**
	 *��ʼ��ÿ��JTextPane
	 *Ϊ��̬���ͣ����������CommentPanel��Ҳʹ�õ���
     *@return JTextPane
	 *@version 2.0
     *@author ��ţ��֪��
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
	 * ��΢��/���۵ȵȷ��͵�ʱ��(Date����)"��ʽ��"����ת����"String����"��
	 * Ϊ��̬���ͣ����������CommentPanel��Ҳʹ�õ���
     *@param ���͵�ʱ��(date����)
     *@return ���͵�ʱ��(String����)
     *@version 2.0
     *@author ��ţ��֪��
     */
	public static String getCreateDate(Date createDate){
		SimpleDateFormat formatter = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		return formatter.format(createDate);
	}
	
	/**
	 *������ʽ������ 
	 *Ϊ��̬���ͣ����������CommentPanel��Ҳʹ�õ���
     *@param String����
     *@return String����
     *@version 2.0
     *@author ��ţ��֪��
     */
	public static String setForms(String html) {
		html = "<font face=\"΢���ź�\" size=\"4\">" + html + "</font>";
		html = html.replaceAll(
				"@([^<^>^,^ ^��^��^:^��^/^&^@^#^\"^\']{1,})([��, :/<])",
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
	 * ��ʼ��ѡ���ÿ����� ����ҳ @ ���� ˽�� �ղ� ��˿ ��ע ΢����:ѡ�������
	 * @param String���ͣ�һ��ѡ������֣�
	 * @return JPanel
	 * @version 2.0
	 * @author ��ţ��֪��
	 */
	public static JPanel initTab (JTabbedPane tabbedPane, String tabName, User user) {
		JPanel tabPanel = new JPanel();
		tabPanel.setLayout(null);
		tabbedPane.addTab(tabName, null, tabPanel, null);
		HeadPanel headPanel = null;// ÿ��ѡ����ϰ벿��
		if(user != null){
			headPanel = new HeadPanel(user);
		}
		else
			headPanel = new HeadPanel();
		tabPanel.add(headPanel);
		return tabPanel;
	}
	/**
	 * �����������һ����ַ
     *@author ��ţ��֪��
     *@param String���͵ĵ�ַ
     */
	 public static void browse(String url){
		 try{
		 	//��ȡ��ָ����������ϵͳ���ԣ�������ϵͳ����
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
			 System.out.println("�Ҳ����������");
		 }
	 }
}
