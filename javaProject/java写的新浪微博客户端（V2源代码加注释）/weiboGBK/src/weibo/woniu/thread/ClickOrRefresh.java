package weibo.woniu.thread;

import java.util.List;

import javax.swing.JPanel;

import weibo.woniu.utility.CommentPanel;
import weibo.woniu.utility.DirectMessagePanel;
import weibo.woniu.utility.FriendsPanel;
import weibo.woniu.utility.HeadPanel;
import weibo.woniu.utility.MyScrollPane;
import weibo.woniu.utility.StatusPanel;
import weibo.woniu.widget.AnotherUser;
import weibo.woniu.widget.TipDialog;
import weibo4j.Comment;
import weibo4j.DirectMessage;
import weibo4j.Status;
import weibo4j.User;
import weibo4j.Weibo;
import weibo4j.WeiboException;
/**
 * �̳���Thread�����ڴ���"ˢ��"��"��һ�ε����ѡ�"�����ǩ
 * @version 2.0
 * @author ��ţ��֪��
 */
public class ClickOrRefresh extends Thread {
	public final static String CLICK = "CLICK";//��һ�ε��
	public final static String REFRESH = "REFRESH";//����
	private static int[] current_Array = {2,1,1,1,1,1,1,1,2,2,2};//�����ж��Ƿ��һ�ε����ѡ�
	private String currentstyle;//�ǵ��ѡ�������ˢ��
	private JPanel tabPanel;//ѡ����
	private int currentTabNum;//��ǰѡ�������
	private Weibo weibo = LoginThread.weibo;
	/**
	 * @param JPanel tabPanel ��ǰѡ������
	 * @param Weibo currentWeibo ��ǰ��΢��
	 * @param int currentTabNum ��ǰѡ�������
	 * @version 2.0
	 * @author ��ţ��֪��
	 */
	public ClickOrRefresh(JPanel tabPanel, int currentTabNum){
		this.tabPanel = tabPanel;
		this.currentTabNum = currentTabNum;
	}
	public void run() {
		//��һ�ε��
		if(currentstyle == CLICK){
			if(current_Array[currentTabNum] == 1){
				current_Array[currentTabNum] = 2;
				if(listOfStatus(currentTabNum) != null){
					initStatusTabPanel(listOfStatus(currentTabNum),
							currentTabNum);
				}else if(listOfComment(currentTabNum) != null){
					initCommentTabPanel(listOfComment(currentTabNum),
							currentTabNum);
				}
				else if(listOfUser(currentTabNum) != null){
					initUserTabPanel(listOfUser(currentTabNum),
							currentTabNum);
				}
				else if(listOfDirMsg(currentTabNum) != null){
					TipDialog dialog = new TipDialog("Ӧ�ó����Ȩ�޲���!");
					dialog.setLocation(LoginThread.mainDialog.getX()+15, 559);
					//initDirMsgTabPanel(listOfDirMsg(currentTabNum),currentTabNum);
				}
			}	
		}
		//ˢ��
		else if(currentstyle == REFRESH){
			if(tabPanel != null)
				tabPanel.removeAll();
			HeadPanel headPanel = null;
			if(currentTabNum < MyScrollPane.OTHER_USERTIMELINE)
				headPanel = new HeadPanel();
			else
				headPanel = new HeadPanel(AnotherUser.user);
			tabPanel.add(headPanel);
			if(listOfStatus(currentTabNum) != null){
				initStatusTabPanel(listOfStatus(currentTabNum),
						currentTabNum);
			}
			else if(listOfComment(currentTabNum) != null){
				initCommentTabPanel(listOfComment(currentTabNum),
						currentTabNum);
			}
			else if(listOfUser(currentTabNum) != null){
				initUserTabPanel(listOfUser(currentTabNum),
						currentTabNum);
			}
		}
	}

	/**
	 * ��ȡҪ�����΢��(���µ�΢��)
	 * @author ��ţ��֪��
	 */
	private List<Status> listOfStatus(int currentTabNum){
		List<Status> statuses = null;
		try {
			if(currentTabNum == MyScrollPane.HOMEPAGE){
				statuses = weibo.getFriendsTimeline();	
			}
			else if(currentTabNum == MyScrollPane.MENTION){
				statuses = weibo.getMentions();
			}
			else if(currentTabNum == MyScrollPane.FAVORITES){
				statuses = weibo.getFavorites();
			}
			else if(currentTabNum == MyScrollPane.USERTIMELINE){
				statuses = weibo.getUserTimeline();
			}
			else if(currentTabNum == MyScrollPane.OTHER_USERTIMELINE){
				statuses = LoginThread.weibo.getUserTimeline(
						AnotherUser.userId);
			}
		} catch (WeiboException e) {
			System.out.println("��������΢��ʱ���ִ���ClickOrRefresh 70�У�");
			e.printStackTrace();
		}
		return statuses;
	}
	/**
	 * ��ȡҪ���������(���µ�����)
	 * @version 2.0
	 * @author ��ţ��֪��
	 */
	private List<Comment> listOfComment(int currentTabNum){
		List<Comment> comments = null;
		try {
			if(currentTabNum == MyScrollPane.COMMENTS){
				comments = weibo.getCommentsTimeline();	
			}
		} catch (WeiboException e) {
			System.out.println("������������ʱ���ִ���ClickOrRefresh 87�У�");
			e.printStackTrace();
		}
		return comments;
	}
	/**
	 * ��ȡҪ����ķ�˿����������ע����
	 * @author ��ţ��֪��
	 */
	private List<User> listOfUser(int currentTabNum) {
		List<User> users = null;
		try {
			if(currentTabNum == MyScrollPane.FOLLOWERS){
				users = weibo.getFollowersStatuses();	
			}
			else if(currentTabNum == MyScrollPane.FRIENDSHIPS){
				users = weibo.getFriendsStatuses();	
			}
			else if(currentTabNum == MyScrollPane.OTHER_FOLLOWERS){
				users = LoginThread.weibo.getFollowersStatuses(AnotherUser.userId);	
			}
			else if(currentTabNum == MyScrollPane.OTHER_FRIENDSHIPS){
				users = LoginThread.weibo.getFriendsStatuses(AnotherUser.userId);	
			}
		} catch (WeiboException e) {
			System.out.println("������������ʱ���ִ���ClickOrRefresh 87�У�");
			e.printStackTrace();
		}
		return users;
	}
	
	private List<DirectMessage> listOfDirMsg(int currentTabNum) {
		List<DirectMessage> directMessages = null;
		/*try {
			directMessages = weibo.getDirectMessages();
		} catch (WeiboException e) {
			System.out.println("��������˽��ʱ���ִ���ClickOrRefresh 167�У�");
			e.printStackTrace();
		}*/
		TipDialog dialog = new TipDialog("Ӧ�ó����Ȩ�޲���!");
		dialog.setLocation(LoginThread.mainDialog.getX()+15, 559);
		return directMessages;
	}
	/**
	 * ��ʼ����ҳ��@�ң��ղأ�΢���ĸ�ѡ���������ݣ��°벿��
	 * @param List<Status>��currentTabNum��ǰѡ�������
	 * @version 2.0
	 * @author ��ţ
	 */
	public void initStatusTabPanel(List<Status> statuses,
			int currentTabNum) {
		//ÿ��ѡ����°벿��
		StatusPanel statusPane = new StatusPanel(statuses);
		if(currentTabNum == MyScrollPane.HOMEPAGE){//��ǰѡ�����ҳ
			statusPane.setIsTimeline(true);
		}
		else if(currentTabNum == MyScrollPane.USERTIMELINE){
			statusPane.setIsUserTimeLine(true);
		}
		else if(currentTabNum == MyScrollPane.FAVORITES){
			statusPane.setFavourite(true);
		}
		else if(currentTabNum == MyScrollPane.OTHER_USERTIMELINE){
			statusPane.setOtherUser(true);
		}
		MyScrollPane scrollPane = new MyScrollPane(statusPane);//�������
		scrollPane.setCurrentTabNum(currentTabNum);
		tabPanel.add(scrollPane);
		
	}
	/**
	 * ����ѡ���������ݣ��°벿��
	 * @param List<Comment>��currentTabNum��ǰѡ�������
	 * @version 2.0
	 * @author ��ţ
	 */
	private void initCommentTabPanel(List<Comment> comments, int currentTabNum) {
		//ÿ��ѡ����°벿��
		CommentPanel commentPanel = new CommentPanel(comments);
		MyScrollPane scrollPane = new MyScrollPane(commentPanel);//�������
		scrollPane.setCurrentTabNum(currentTabNum);
		tabPanel.add(scrollPane);
	}
	/**
	 * ��˿����עѡ���������ݣ��°벿��
	 * @param List<User>,currentTabNum��ǰѡ�������
	 * @version 2.0
	 * @author ��ţ
	 */

	public void initUserTabPanel(List<User> users, int currentTabNum) {
		//ÿ��ѡ����°벿��
		FriendsPanel friendsPanel = new FriendsPanel(users);
		if(currentTabNum == MyScrollPane.OTHER_FOLLOWERS ||
				currentTabNum == MyScrollPane.OTHER_FRIENDSHIPS){//��ǰ���ڲ鿴����û�
			friendsPanel.setOtherUser(true);
		}
		else if(currentTabNum == MyScrollPane.FRIENDSHIPS){
			friendsPanel.setFriendships(true);
		}
		MyScrollPane scrollPane = new MyScrollPane(friendsPanel);//�������
		scrollPane.setCurrentTabNum(currentTabNum);
		tabPanel.add(scrollPane);
	}
	/**
	 * ˽�ŵ�������ݣ��°벿��
	 * @param List<DirectMessage>,currentTabNum��ǰѡ�������
	 * @version 2.0
	 * @author ��ţ��֪��
	 */
	private void initDirMsgTabPanel(List<DirectMessage> directMessages,
			int currentTabNum) {
		DirectMessagePanel directMessagePanel = new DirectMessagePanel(directMessages);
		MyScrollPane scrollPane = new MyScrollPane(directMessagePanel);//�������
		scrollPane.setCurrentTabNum(currentTabNum);
		tabPanel.add(scrollPane);
	}
	
	/**
	 * �ǵ�һ�ε����ѡ�������ˢ��    ClickOrRefresh.CLICK ,ClickOrRefresh.REFRESH 
	 * @author ��ţ��֪��
	 */
	public void setcurrentStyle(String currentstyle){
		this.currentstyle = currentstyle;
	}
	public JPanel getTabPanel() {
		return tabPanel;
	}
	public void setTabPanel(JPanel tabPanel) {
		this.tabPanel = tabPanel;
	}
}
