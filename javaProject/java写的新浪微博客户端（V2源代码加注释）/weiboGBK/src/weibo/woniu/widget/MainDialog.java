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
 * 主界面
 * @version 2.0
 * @author 蜗牛都知道
 */
public class MainDialog extends JDialog {

	private static final long serialVersionUID = -1673890947357460141L;
	public static JTabbedPane tabbedPane;
	private JPanel homePage;// 主页 
	private static Weibo weibo = LoginThread.weibo;
	public MainDialog() {
		setTitle("新浪微博");
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
			//UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");   
		} catch (Exception e) {
			e.printStackTrace();
		}
		addWindowListener(new WindowAdapter()// 系统关闭事件
		{
			public void windowClosing(WindowEvent e) {
				setVisible(false);
			}
		});
		setFocusable(true);
		addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {//按ESC隐藏
					setVisible(false);
				}
			}
		});
		SystemTrayInitial();// 系统拖盘

		Image logo = getToolkit().getImage("image/logo.jpg");
		setIconImage(logo);
		setBounds(700, 0, 500, 600);
		setVisible(true);
		// setLayout(null);//加的话就没了选项卡面板
		createTipDialog();//提示未读信息
		// 实例化选项卡
		tabbedPane = new JTabbedPane(JTabbedPane.RIGHT);
		tabbedPane.setFont(new Font("微软雅黑", Font.PLAIN, 13));
		add(tabbedPane);

		//初始化各个选项卡
		homePage = CommonMethod.initTab(tabbedPane,"主页",null);
		String[] tabNames = {"@我", "评论", "私信", "收藏", "粉丝", "关注", "微博"};
		for(String tabName : tabNames){
			CommonMethod.initTab(tabbedPane,tabName,null);
		}
		try {
			ClickOrRefresh firstOfAll = new ClickOrRefresh(homePage,MyScrollPane.HOMEPAGE);
			firstOfAll.initStatusTabPanel(weibo.getFriendsTimeline(),
					MyScrollPane.HOMEPAGE);
			repaint();
		} catch (WeiboException e) {
			System.out.println("获得好友所送得微博时出现出错，MainDialog 112行！");
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
	 * 提示未读信息
	 * @version 2.0
	 * @author 蜗牛都知道
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
			tipText += mentionCounts+"条微博@我未读.";
			System.out.println(tipText);
			falg = true;
		}
		if(commentCounts != 0){
			tipText += commentCounts+"新评论未读.";
			falg = true;
		}
		if(followerCounts != 0){
			tipText += followerCounts+"个新粉丝.";
			falg = true;
		}
		if(falg){
			TipDialog tipDialog = new TipDialog(tipText);
			tipDialog.setLocation(715, 559);
		}
	}
	
	/**
	 * 系统托盘
	 * @version 2.0
	 * @author 蜗牛都知道
	 */
	private void SystemTrayInitial() {
		if (!SystemTray.isSupported()) // 判断当前系统是否支持系统栏
			return;
		try {
			String title = "新浪微博JAVA客户端 \n牵着蜗牛去散步";// 设置提示文本信息
			SystemTray sysTray = SystemTray.getSystemTray();
			Image image = Toolkit.getDefaultToolkit()
					.getImage("image/logo.jpg");
			//(MyDemo.class.getResource("logo.jpg"));// 获取MyDemo类的class文件路径下的图标
			TrayIcon trayicon = new TrayIcon(image, title, createMenu());// 创建托盘图标：由图标、文本、右击菜单组成
			trayicon.setImageAutoSize(true);// 设置是否自动调整图标的大小
			trayicon.addActionListener(new ActionListener()// 双击图标时显示窗体
					{
						public void actionPerformed(ActionEvent e) {
							setVisible(true);
							toFront();// 如果此窗口是可见的，则将此窗口置于前端，并可以将其设为焦点 Window。
						}
					});
			sysTray.add(trayicon);
			trayicon.displayMessage("新浪微博JAVA客户端", "牵着蜗牛去散步", MessageType.INFO);// 在托盘图标附近显示弹出消息。
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 右键菜单 还要改进
	 * @version 2.0
	 * @author 蜗牛都知道
	 */
	private PopupMenu createMenu() { // 创建系统栏菜单的方法
		PopupMenu menu = new PopupMenu();
		// menu.setFont(new Font("微软雅黑",Font.BOLD,12));
		MenuItem openItem = new MenuItem("打开");
		openItem.addActionListener(new ActionListener() {// 系统栏打开菜单项事件
					public void actionPerformed(ActionEvent e) {
						if (!isVisible()) {
							setVisible(true);
							toFront();
						} else
							toFront();
					}
				});
		MenuItem logoffItem = new MenuItem("注销");
		logoffItem.addActionListener(new ActionListener() {// 系统栏注销菜单项事件
					public void actionPerformed(ActionEvent e) {
						//TODo  遗留问题！！这个功能不正常dispose()之后原来的MainDialog任然存在
						dispose();
						new LoginFrame();
					}
				});
		MenuItem aboutItem = new MenuItem("关于");
		aboutItem.addActionListener(new ActionListener() {// 系统栏注销菜单项事件
					public void actionPerformed(ActionEvent e) {
						JOptionPane.showMessageDialog(null, "感谢您们的支持与鼓励。"+"\n"+
								"修改完结于2011/12/22 17:04. 天晴"+"\n"+
								"接下来要忙于期末考试了。不想挂科。泪。"+"\n"+
								"有交流兴趣的朋友：cannel_2020@163.com"+"\n"+
								"                         蜗牛都知道");
					}
				});
		MenuItem exitItem = new MenuItem("退出");
		exitItem.addActionListener(new ActionListener() { // 系统栏退出事件
					public void actionPerformed(ActionEvent e) {
						System.exit(0);
					}
				});

		menu.add(openItem);
		menu.addSeparator();// 分割线
		menu.add(logoffItem);
		menu.addSeparator();// 分割线
		menu.add(aboutItem);
		menu.addSeparator();// 分割线
		menu.add(exitItem);

		return menu;
	}
}
