package weibo.woniu.widget;

import java.awt.Font;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import weibo.woniu.thread.ClickOrRefresh;
import weibo.woniu.thread.LoginThread;
import weibo.woniu.utility.CommonMethod;
import weibo.woniu.utility.MyScrollPane;
import weibo4j.Weibo;
import weibo4j.WeiboException;
import weibo4j.org.json.JSONException;
/**
 * ������
 * @version 2.0
 * @author ��ţ��֪��
 */
public class MainDialog extends JDialog {

	private static final long serialVersionUID = -1673890947357460141L;
	public static JTabbedPane tabbedPane;
	private JPanel homePage;// ��ҳ 
	private static Weibo weibo = LoginThread.weibo;
	public MainDialog() {
		setTitle("����΢��");
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
			//UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");   
		} catch (Exception e) {
			e.printStackTrace();
		}
		addWindowListener(new WindowAdapter()// ϵͳ�ر��¼�
		{
			public void windowClosing(WindowEvent e) {
				setVisible(false);
			}
		});
		setFocusable(true);
		addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {//��ESC����
					setVisible(false);
				}
			}
		});
		SystemTrayInitial();// ϵͳ����

		Image logo = getToolkit().getImage("image/logo.jpg");
		setIconImage(logo);
		setBounds(700, 0, 500, 600);
		setVisible(true);
		// setLayout(null);//�ӵĻ���û��ѡ����
		createTipDialog();//��ʾδ����Ϣ
		// ʵ����ѡ�
		tabbedPane = new JTabbedPane(JTabbedPane.RIGHT);
		tabbedPane.setFont(new Font("΢���ź�", Font.PLAIN, 13));
		add(tabbedPane);

		//��ʼ������ѡ�
		homePage = CommonMethod.initTab(tabbedPane,"��ҳ",null);
		String[] tabNames = {"@��", "����", "˽��", "�ղ�", "��˿", "��ע", "΢��"};
		for(String tabName : tabNames){
			CommonMethod.initTab(tabbedPane,tabName,null);
		}
		try {
			ClickOrRefresh firstOfAll = new ClickOrRefresh(homePage,MyScrollPane.HOMEPAGE);
			firstOfAll.initStatusTabPanel(weibo.getFriendsTimeline(),
					MyScrollPane.HOMEPAGE);
			repaint();
		} catch (WeiboException e) {
			System.out.println("��ú������͵�΢��ʱ���ֳ���MainDialog 112�У�");
		}	
		tabbedPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				JTabbedPane tabbedPane = (JTabbedPane)e.getSource(); 
				JPanel selectedPanel = (JPanel) tabbedPane.getSelectedComponent();
				ClickOrRefresh thread = new ClickOrRefresh(selectedPanel,
						tabbedPane.getSelectedIndex());
				thread.setcurrentStyle(ClickOrRefresh.CLICK);
				thread.start();
			}	
		});
	}
	/**
	 * ��ʾδ����Ϣ
	 * @version 2.0
	 * @author ��ţ��֪��
	 */
	private void createTipDialog(){
		long mentionCounts = 0;
		long commentCounts = 0;
		long followerCounts = 0;
		try {
			commentCounts = weibo.getUnread().getComments();
			followerCounts = weibo.getUnread().getFollowers();
			mentionCounts = weibo.getUnread().getMentions();
		} catch (WeiboException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String tipText = "";
		boolean falg = false;
		if(mentionCounts != 0){
			tipText += mentionCounts+"��΢��@��δ��.";
			System.out.println(tipText);
			falg = true;
		}
		if(commentCounts != 0){
			tipText += commentCounts+"������δ��.";
			falg = true;
		}
		if(followerCounts != 0){
			tipText += followerCounts+"���·�˿.";
			falg = true;
		}
		if(falg){
			TipDialog tipDialog = new TipDialog(tipText);
			tipDialog.setLocation(715, 559);
		}
	}
	
	/**
	 * ϵͳ����
	 * @version 2.0
	 * @author ��ţ��֪��
	 */
	private void SystemTrayInitial() {
		if (!SystemTray.isSupported()) // �жϵ�ǰϵͳ�Ƿ�֧��ϵͳ��
			return;
		try {
			String title = "����΢��JAVA�ͻ��� \nǣ����ţȥɢ��";// ������ʾ�ı���Ϣ
			SystemTray sysTray = SystemTray.getSystemTray();
			Image image = Toolkit.getDefaultToolkit()
					.getImage("image/logo.jpg");
			//(MyDemo.class.getResource("logo.jpg"));// ��ȡMyDemo���class�ļ�·���µ�ͼ��
			TrayIcon trayicon = new TrayIcon(image, title, createMenu());// ��������ͼ�꣺��ͼ�ꡢ�ı����һ��˵����
			trayicon.setImageAutoSize(true);// �����Ƿ��Զ�����ͼ��Ĵ�С
			trayicon.addActionListener(new ActionListener()// ˫��ͼ��ʱ��ʾ����
					{
						public void actionPerformed(ActionEvent e) {
							setVisible(true);
							toFront();// ����˴����ǿɼ��ģ��򽫴˴�������ǰ�ˣ������Խ�����Ϊ���� Window��
						}
					});
			sysTray.add(trayicon);
			trayicon.displayMessage("����΢��JAVA�ͻ���", "ǣ����ţȥɢ��", MessageType.INFO);// ������ͼ�긽����ʾ������Ϣ��
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * �Ҽ��˵� ��Ҫ�Ľ�
	 * @version 2.0
	 * @author ��ţ��֪��
	 */
	private PopupMenu createMenu() { // ����ϵͳ���˵��ķ���
		PopupMenu menu = new PopupMenu();
		// menu.setFont(new Font("΢���ź�",Font.BOLD,12));
		MenuItem openItem = new MenuItem("��");
		openItem.addActionListener(new ActionListener() {// ϵͳ���򿪲˵����¼�
					public void actionPerformed(ActionEvent e) {
						if (!isVisible()) {
							setVisible(true);
							toFront();
						} else
							toFront();
					}
				});
		MenuItem logoffItem = new MenuItem("ע��");
		logoffItem.addActionListener(new ActionListener() {// ϵͳ��ע���˵����¼�
					public void actionPerformed(ActionEvent e) {
						//TODo  �������⣡��������ܲ�����dispose()֮��ԭ����MainDialog��Ȼ����
						dispose();
						new LoginFrame();
					}
				});
		MenuItem aboutItem = new MenuItem("����");
		aboutItem.addActionListener(new ActionListener() {// ϵͳ��ע���˵����¼�
					public void actionPerformed(ActionEvent e) {
						JOptionPane.showMessageDialog(null, "��л���ǵ�֧���������"+"\n"+
								"�޸������2011/12/22 17:04. ����"+"\n"+
								"������Ҫæ����ĩ�����ˡ�����ҿơ��ᡣ"+"\n"+
								"�н�����Ȥ�����ѣ�cannel_2020@163.com"+"\n"+
								"                         ��ţ��֪��");
					}
				});
		MenuItem exitItem = new MenuItem("�˳�");
		exitItem.addActionListener(new ActionListener() { // ϵͳ���˳��¼�
					public void actionPerformed(ActionEvent e) {
						System.exit(0);
					}
				});

		menu.add(openItem);
		menu.addSeparator();// �ָ���
		menu.add(logoffItem);
		menu.addSeparator();// �ָ���
		menu.add(aboutItem);
		menu.addSeparator();// �ָ���
		menu.add(exitItem);

		return menu;
	}
}
