package weibo.woniu.utility;

import java.awt.Dimension;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JTextPane;

import weibo.woniu.listener.MyHyperlinkListener;
import weibo.woniu.widget.AnotherUser;
import weibo.woniu.widget.MainDialog;
import weibo4j.User;
/**
 * 继承自JPanel，用于放置关注的人，和粉丝
 * @version 2.0
 * @author 蜗牛都知道
 */
public class FriendsPanel extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8309396416114844244L;
	private List<User> users;
	private int locationY = 0;//用于每个控件的Y坐标位置
	public boolean loading = true;//判断当前20条是否加载完，用于判断滚动条滚动到最底时是否要加载下一页
	private boolean otherUser = false;//用于判断应当repaint()哪个tabbedPane
	private MyLabel followHimLabel, cancelLabel,metionLabel,
		directMsgLabel;//+关注，取消关注,@,私信
	private JTextPane nameAndTextPane;
	private boolean isFriendships;
	public FriendsPanel(List<User> users){
		this.users = users;
		setLayout(null);
		addUser(users);
	}
	public void addUser(List<User> users) {
		this.users = users;
		AddUserThread thread = new AddUserThread();
		thread.start();
	}
	private class AddUserThread extends Thread{
		public void run() {
			for (User user : users) {
				//头像
				UserHeadImg userHead = new UserHeadImg(user);
				userHead.setBounds(2, locationY, 54, 54);
				add(userHead);
				
				String nameAnd = "<font face=微软雅黑 size=3><a href=\"@"
					+ user.getName() + "\">" + user.getName() + "</font>"
					+ "</a><br>";
				String gender = CommonMethod.getGender(user.getGender());
				nameAnd += "<font face=微软雅黑 size=3>" + gender + user.getLocation()
					+ "<br/>" + "关注" + user.getFriendsCount() + "   粉丝"
					+ user.getFollowersCount() + "   微博"
					+ user.getStatusesCount() + "</font>";
				nameAndTextPane = CommonMethod.initTextPanel();
				initLabel(user);
				nameAndTextPane.addHyperlinkListener(new MyHyperlinkListener());
				nameAndTextPane.setText(CommonMethod.setForms(nameAnd));
				nameAndTextPane.setBounds(58, locationY, 350, 54);
				add(nameAndTextPane);
				
				String description = "<font face=微软雅黑 size=3>" + "简介："
						+ user.getDescription() + "</font>";
				JTextPane introTextPane = CommonMethod.initTextPanel();
				introTextPane.setText(description);
				introTextPane.addHyperlinkListener(new MyHyperlinkListener());
				introTextPane.setSize(400, introTextPane.getPreferredSize().height);
				int descrptionHeight = introTextPane.getPreferredSize().height;
				introTextPane.setText(CommonMethod.setForms(description));
				introTextPane.setBounds(2, locationY + 54, 400, descrptionHeight);
				add(introTextPane);
				
				locationY = locationY + 54 + descrptionHeight;
				setPreferredSize(new Dimension(270, locationY));
				if(isOtherUser())//如果是查看别的用户的话，repaint NewWeibo里的tabbedPane
					AnotherUser.tabbedPane.repaint();
				else
					MainDialog.tabbedPane.repaint();
			}
			loading = false;
		}
	}
	public boolean isOtherUser() {
		return otherUser;
	}
	public void setOtherUser(boolean otherUser) {
		this.otherUser = otherUser;
	}
	/**
	 * 初始化+关注，互相关注，取消关注，@，私信
     * @author 蜗牛都知道
	 * @param Status 
     */
	private void initLabel(User user){
		cancelLabel= new MyLabel("取消关注",user);
		followHimLabel= new MyLabel("+关注",user);
		metionLabel = new MyLabel("@",user);
		directMsgLabel = new MyLabel("私信",user);
		cancelLabel.setLocation(225, 2);
		followHimLabel.setLocation(240, 2);
		metionLabel.setLocation(290, 2);
		directMsgLabel.setLocation(310, 2);
		if(isFriendships){
			nameAndTextPane.add(cancelLabel);
		}
		else
			nameAndTextPane.add(followHimLabel);
		nameAndTextPane.add(metionLabel);
		nameAndTextPane.add(directMsgLabel);
	}
	public void setFriendships(boolean isFriendships) {
		this.isFriendships = isFriendships;
	}
}
