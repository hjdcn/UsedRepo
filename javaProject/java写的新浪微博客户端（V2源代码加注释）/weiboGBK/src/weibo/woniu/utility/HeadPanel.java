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
 * ÿ��ѡ���ͷ������һ���̳���JPanel����,�����û�����Ϣ��һЩ��ǩ(���������ˣ���ͼ��)
 * @version 2.0
 * @author ��ţ��֪��
 */
public class HeadPanel extends JPanel implements Runnable{	
	
	private static final long serialVersionUID = 7107973899361508580L;
	private final MyLabel searchLabel = new  MyLabel("����");
	private final MyLabel findLabel = new MyLabel("����");
	private final MyLabel sendPicLabel = new MyLabel("��ͼ");
	private final MyLabel updateStatusLabel = new MyLabel("��΢��");
	private final MyLabel refurbishLabel = new MyLabel("ˢ��");
	private boolean isAnotherUser = false;//�Ƿ���AnotherUser�����ɵ�һ������
	private User user;
	public HeadPanel() {
		setSize(500, 90);
		setLayout(null);
		try {
			user = LoginThread.weibo.verifyCredentials();
		} catch (WeiboException e1) {
			//TODO ���ֹ���һ��һ�ι����ǲ�����Ϊ��̬������ԭ���ء�
			System.out.println("��øû��ã�User��ʱ�����쳣");;
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
		JLabel createFriendship = new MyLabel("+��ע", user);
		JLabel destroyFriendship = new MyLabel("ȡ����ע",user);
		JLabel directMsg = new MyLabel("˽��",user);
		JLabel refresh = new MyLabel("ˢ��");
		refresh.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent arg0) {
				JTabbedPane tabbedPane = AnotherUser.tabbedPane;
				ClickOrRefresh refresh = new ClickOrRefresh((JPanel) tabbedPane.getSelectedComponent(),
						tabbedPane.getSelectedIndex()+8);//������ƫ����
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
		userHead.setHorizontalAlignment(SwingConstants.LEFT);//���ñ�ǩ������ X��Ķ��뷽ʽ��
		add(userHead);
		
		JLabel nameLabel = new MyLabel(user.getName());
		nameLabel.setFont(new Font("΢���ź�", Font.PLAIN,16));
		nameLabel.setSize(nameLabel.getPreferredSize());
		nameLabel.setLocation(70, 8);
		add(nameLabel);
		
		JLabel locationLabel = new JLabel(user.getLocation());
		locationLabel.setBounds(72, 30, 100, 24);
		locationLabel.setFont(new Font("΢���ź�", Font.PLAIN,16));
		add(locationLabel);
		
		String gender = CommonMethod.getGender(user.getGender());
		JLabel userGender = new JLabel(gender);
		userGender.setBounds(165, 30, 26, 26);
		userGender.setFont(new Font("΢���ź�", Font.PLAIN,16));
		add(userGender);
		
		int labelX = 8;//����������ǩ��λ�ã���ע����˿��΢����
		JLabel friendsLabel = new MyLabel("��ע "+Integer.toString(user.getFriendsCount()));
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
		JLabel followersLabel = new MyLabel("��˿ "+Integer.toString(user.getFollowersCount()));
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
		JLabel statusLabel = new MyLabel("΢�� "+String.valueOf(user.getStatusesCount()));
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
