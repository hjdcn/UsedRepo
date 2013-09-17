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
 * �̳���JPanel�����ڷ��ù�ע���ˣ��ͷ�˿
 * @version 2.0
 * @author ��ţ��֪��
 */
public class FriendsPanel extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8309396416114844244L;
	private List<User> users;
	private int locationY = 0;//����ÿ���ؼ���Y����λ��
	public boolean loading = true;//�жϵ�ǰ20���Ƿ�����꣬�����жϹ��������������ʱ�Ƿ�Ҫ������һҳ
	private boolean otherUser = false;//�����ж�Ӧ��repaint()�ĸ�tabbedPane
	private MyLabel followHimLabel, cancelLabel,metionLabel,
		directMsgLabel;//+��ע��ȡ����ע,@,˽��
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
				//ͷ��
				UserHeadImg userHead = new UserHeadImg(user);
				userHead.setBounds(2, locationY, 54, 54);
				add(userHead);
				
				String nameAnd = "<font face=΢���ź� size=3><a href=\"@"
					+ user.getName() + "\">" + user.getName() + "</font>"
					+ "</a><br>";
				String gender = CommonMethod.getGender(user.getGender());
				nameAnd += "<font face=΢���ź� size=3>" + gender + user.getLocation()
					+ "<br/>" + "��ע" + user.getFriendsCount() + "   ��˿"
					+ user.getFollowersCount() + "   ΢��"
					+ user.getStatusesCount() + "</font>";
				nameAndTextPane = CommonMethod.initTextPanel();
				initLabel(user);
				nameAndTextPane.addHyperlinkListener(new MyHyperlinkListener());
				nameAndTextPane.setText(CommonMethod.setForms(nameAnd));
				nameAndTextPane.setBounds(58, locationY, 350, 54);
				add(nameAndTextPane);
				
				String description = "<font face=΢���ź� size=3>" + "��飺"
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
				if(isOtherUser())//����ǲ鿴����û��Ļ���repaint NewWeibo���tabbedPane
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
	 * ��ʼ��+��ע�������ע��ȡ����ע��@��˽��
     * @author ��ţ��֪��
	 * @param Status 
     */
	private void initLabel(User user){
		cancelLabel= new MyLabel("ȡ����ע",user);
		followHimLabel= new MyLabel("+��ע",user);
		metionLabel = new MyLabel("@",user);
		directMsgLabel = new MyLabel("˽��",user);
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
