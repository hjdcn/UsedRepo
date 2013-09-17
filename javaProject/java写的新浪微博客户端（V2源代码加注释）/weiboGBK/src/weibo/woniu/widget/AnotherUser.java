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
 * 查看其他用户的类，继承自JDialog，包括微博，粉丝，关注三个选项卡
 */
public class AnotherUser extends JDialog {

	private static final long serialVersionUID = -7686579399040120784L;
	public static User user;//一个用户
	public static JTabbedPane tabbedPane;
	public static String userId;//用户的UID 
	public AnotherUser(User user) throws WeiboException {
		AnotherUser.user = user;
		setTitle(user.getName());
		setBounds(200, 0, 500, 600);
		//setLayout(null);//加了选项卡了没了。
		setVisible(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		Image logo = getToolkit().getImage("image/logo.jpg");
		setIconImage(logo);
		
		
		tabbedPane=  new JTabbedPane(JTabbedPane.RIGHT);
		tabbedPane.setFont(new Font("微软雅黑", Font.PLAIN, 13));
		add(tabbedPane);
		
		JPanel userTimeline = CommonMethod.initTab(tabbedPane,"微博",user);//这里把User穿进去了
		JPanel userFriends = CommonMethod.initTab(tabbedPane,"关注",user);
		JPanel userFollowers = CommonMethod.initTab(tabbedPane,"粉丝",user);
		
		userId = String.valueOf(user.getId());
		ClickOrRefresh firstOfAll = new ClickOrRefresh(userTimeline, 
				MyScrollPane.OTHER_USERTIMELINE);
		firstOfAll.initStatusTabPanel(LoginThread.weibo.getUserTimeline(userId),
				MyScrollPane.OTHER_USERTIMELINE);
		firstOfAll.setTabPanel(userFriends);//设置ClickOrRefresh中的tabbedPanel为userFriends
		firstOfAll.initUserTabPanel(LoginThread.weibo.getFriendsStatuses(AnotherUser.userId),
				MyScrollPane.OTHER_FRIENDSHIPS);
		firstOfAll.setTabPanel(userFollowers);//设置ClickOrRefresh中的tabbedPanel为userFollowers
		firstOfAll.initUserTabPanel(LoginThread.weibo.getFollowersStatuses(AnotherUser.userId),
				MyScrollPane.OTHER_FOLLOWERS);
	}
}