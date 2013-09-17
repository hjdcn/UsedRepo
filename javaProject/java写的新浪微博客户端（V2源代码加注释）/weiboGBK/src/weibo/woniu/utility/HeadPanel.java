package weibo.woniu.utility;

import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;

import weibo.woniu.thread.ClickOrRefresh;
import weibo.woniu.thread.LoginThread;
import weibo.woniu.widget.AnotherUser;
import weibo.woniu.widget.MainDialog;
import weibo.woniu.widget.SearchDialog;
import weibo4j.User;
import weibo4j.WeiboException;

/**
 * 每个选项卡的头部，是一个继承自JPanel的类,包括用户的信息和一些标签(搜索，找人，发图等)
 * @version 2.0
 * @author 蜗牛都知道
 */
public class HeadPanel extends JPanel implements Runnable{	
	
	private static final long serialVersionUID = 7107973899361508580L;
	private final MyLabel searchLabel = new  MyLabel("搜索");
	private final MyLabel findLabel = new MyLabel("找人");
	private final MyLabel sendPicLabel = new MyLabel("发图");
	private final MyLabel updateStatusLabel = new MyLabel("发微博");
	private final MyLabel refurbishLabel = new MyLabel("刷新");
	private boolean isAnotherUser = false;//是否是AnotherUser类生成的一个对象
	private User user;
	public HeadPanel() {
		setSize(500, 90);
		setLayout(null);
		try {
			user = LoginThread.weibo.verifyCredentials();
		} catch (WeiboException e1) {
			//TODO 出现过这一个一次过，是不是因为静态变量的原因呢。
			System.out.println("获得该户用（User）时发生异常");;
		}
		
		searchLabel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				new SearchDialog(searchLabel);
			}
		});
		findLabel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				new SearchDialog(findLabel);
			}
		});
		refurbishLabel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent arg0) {
				JTabbedPane tabbedPane = MainDialog.tabbedPane;
				ClickOrRefresh refresh = new ClickOrRefresh((JPanel) tabbedPane.getSelectedComponent(),
						tabbedPane.getSelectedIndex());
				refresh.setcurrentStyle(ClickOrRefresh.REFRESH);
				refresh.start();
			}
		});
		searchLabel.setBounds(180, 5, 30, 22);
		findLabel.setBounds(225, 5, 30, 22);
		sendPicLabel.setBounds(270, 5, 30, 22);
		updateStatusLabel.setBounds(315, 5, 45, 22);
		refurbishLabel.setBounds(375, 5, 30, 22);
		add(searchLabel);
		add(findLabel);
		add(sendPicLabel);
		add(updateStatusLabel);
		add(refurbishLabel);
		Thread thread = new Thread(this);
		thread.start();
	}
	public HeadPanel(final User user){
		setSize(500, 90);
		setLayout(null);
		this.user = user;
		JLabel createFriendship = new MyLabel("+关注", user);
		JLabel destroyFriendship = new MyLabel("取消关注",user);
		JLabel directMsg = new MyLabel("私信",user);
		JLabel refresh = new MyLabel("刷新");
		refresh.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent arg0) {
				JTabbedPane tabbedPane = AnotherUser.tabbedPane;
				ClickOrRefresh refresh = new ClickOrRefresh((JPanel) tabbedPane.getSelectedComponent(),
						tabbedPane.getSelectedIndex()+8);//设置了偏移量
				refresh.setcurrentStyle(ClickOrRefresh.REFRESH);
				refresh.start();
			}
		});
		createFriendship.setLocation(225, 5);
		destroyFriendship.setLocation(275, 5);
		directMsg.setLocation(340, 5);
		refresh.setLocation(375, 5);
		add(createFriendship);
		add(destroyFriendship);
		add(directMsg);
		add(refresh);
		Thread thread = new Thread(this);
		thread.start();
	}
	public void run() {
		UserHeadImg userHead = new UserHeadImg(user);
		userHead.setBounds(8, 8, 60, 60);
		userHead.setHorizontalAlignment(SwingConstants.LEFT);//设置标签内容沿 X轴的对齐方式。
		add(userHead);
		
		JLabel nameLabel = new MyLabel(user.getName());
		nameLabel.setFont(new Font("微软雅黑", Font.PLAIN,16));
		nameLabel.setSize(nameLabel.getPreferredSize());
		nameLabel.setLocation(70, 8);
		add(nameLabel);
		
		JLabel locationLabel = new JLabel(user.getLocation());
		locationLabel.setBounds(72, 30, 100, 24);
		locationLabel.setFont(new Font("微软雅黑", Font.PLAIN,16));
		add(locationLabel);
		
		String gender = CommonMethod.getGender(user.getGender());
		JLabel userGender = new JLabel(gender);
		userGender.setBounds(165, 30, 26, 26);
		userGender.setFont(new Font("微软雅黑", Font.PLAIN,16));
		add(userGender);
		
		int labelX = 8;//控制三个标签的位置（关注，粉丝，微博）
		JLabel friendsLabel = new MyLabel("关注 "+Integer.toString(user.getFriendsCount()));
		friendsLabel.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				if(isAnotherUser){
					AnotherUser.tabbedPane.setSelectedIndex(1);
				}
				else MainDialog.tabbedPane.setSelectedIndex(6);
			}
		});
		friendsLabel.setLocation(labelX, 70);
		add(friendsLabel);
		

		labelX += friendsLabel.getPreferredSize().getWidth()+45;
		JLabel followersLabel = new MyLabel("粉丝 "+Integer.toString(user.getFollowersCount()));
		followersLabel.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				if(isAnotherUser){
					AnotherUser.tabbedPane.setSelectedIndex(2);
				}
				else MainDialog.tabbedPane.setSelectedIndex(5);
			}
		});
		followersLabel.setLocation(labelX, 70);
		add(followersLabel);
		

		labelX += followersLabel.getPreferredSize().getWidth()+45;
		JLabel statusLabel = new MyLabel("微博 "+String.valueOf(user.getStatusesCount()));
		statusLabel.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				if(isAnotherUser){
					AnotherUser.tabbedPane.setSelectedIndex(0);
				}
				else MainDialog.tabbedPane.setSelectedIndex(7);
			}
		});
		statusLabel.setLocation(labelX, 70);
		add(statusLabel);
		repaint();
	}
}
