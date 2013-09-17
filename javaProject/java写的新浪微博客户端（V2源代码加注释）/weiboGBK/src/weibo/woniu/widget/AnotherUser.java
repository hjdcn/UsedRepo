package weibo.woniu.widget;

import java.awt.Font;
import java.awt.Image;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import weibo.woniu.thread.ClickOrRefresh;
import weibo.woniu.thread.LoginThread;
import weibo.woniu.utility.CommonMethod;
import weibo.woniu.utility.MyScrollPane;
import weibo4j.User;
import weibo4j.WeiboException;


/**
 * �鿴�����û����࣬�̳���JDialog������΢������˿����ע����ѡ�
 */
public class AnotherUser extends JDialog {

	private static final long serialVersionUID = -7686579399040120784L;
	public static User user;//һ���û�
	public static JTabbedPane tabbedPane;
	public static String userId;//�û���UID 
	public AnotherUser(User user) throws WeiboException {
		AnotherUser.user = user;
		setTitle(user.getName());
		setBounds(200, 0, 500, 600);
		//setLayout(null);//����ѡ���û�ˡ�
		setVisible(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		Image logo = getToolkit().getImage("image/logo.jpg");
		setIconImage(logo);
		
		
		tabbedPane=  new JTabbedPane(JTabbedPane.RIGHT);
		tabbedPane.setFont(new Font("΢���ź�", Font.PLAIN, 13));
		add(tabbedPane);
		
		JPanel userTimeline = CommonMethod.initTab(tabbedPane,"΢��",user);//�����User����ȥ��
		JPanel userFriends = CommonMethod.initTab(tabbedPane,"��ע",user);
		JPanel userFollowers = CommonMethod.initTab(tabbedPane,"��˿",user);
		
		userId = String.valueOf(user.getId());
		ClickOrRefresh firstOfAll = new ClickOrRefresh(userTimeline, 
				MyScrollPane.OTHER_USERTIMELINE);
		firstOfAll.initStatusTabPanel(LoginThread.weibo.getUserTimeline(userId),
				MyScrollPane.OTHER_USERTIMELINE);
		firstOfAll.setTabPanel(userFriends);//����ClickOrRefresh�е�tabbedPanelΪuserFriends
		firstOfAll.initUserTabPanel(LoginThread.weibo.getFriendsStatuses(AnotherUser.userId),
				MyScrollPane.OTHER_FRIENDSHIPS);
		firstOfAll.setTabPanel(userFollowers);//����ClickOrRefresh�е�tabbedPanelΪuserFollowers
		firstOfAll.initUserTabPanel(LoginThread.weibo.getFollowersStatuses(AnotherUser.userId),
				MyScrollPane.OTHER_FOLLOWERS);
	}
}