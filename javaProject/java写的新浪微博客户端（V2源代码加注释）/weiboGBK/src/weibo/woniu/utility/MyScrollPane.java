package weibo.woniu.utility;

import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.util.List;

import javax.swing.JScrollPane;

import weibo.woniu.thread.LoginThread;
import weibo.woniu.widget.AnotherUser;
import weibo4j.Comment;
import weibo4j.DirectMessage;
import weibo4j.Paging;
import weibo4j.Status;
import weibo4j.User;
import weibo4j.Weibo;
import weibo4j.WeiboException;
/**
 * �̳���JScrollPane(�������)��һ���࣬�ͻ���ÿ��ѡ����еĶ�����
 * @author ��ţ��֪��
 */
public class MyScrollPane extends JScrollPane{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2316047352013532172L;
	public final static int HOMEPAGE  = 0;//��ҳ
	public final static int MENTION  = 1;//@��
	public final static int COMMENTS  = 2;//����
	public final static int DIRECT_MESSAGE  = 3;//˽��
	public final static int FAVORITES  = 4;//�ղ�
	public final static int FOLLOWERS  = 5;//��˿
	public final static int FRIENDSHIPS  = 6;//��ע
	public final static int USERTIMELINE  = 7;//΢��
	public final static int OTHER_USERTIMELINE  = 8;//��һ���û���΢��
	public final static int OTHER_FRIENDSHIPS  = 9;//��һ���û��Ĺ�ע����
	public final static int OTHER_FOLLOWERS = 10;//��һ���û��ķ�˿
	//���㵱ǰ�ǵڼ�ҳ
	private int homeCount = 2, mentionCount = 2, commentsCount = 2, 
		directMessageCount = 2, favoritescount = 2, followersCount = 2, 
		friendshipsCount = 2, userTimelineCount = 2, other_userTimelineCount = 2,
		other_friendships = 2, other_followers = 2;
	private int currentTabNum;//��ǰѡ�������
	private Weibo weibo = LoginThread.weibo;//��ǰ��΢��
	private StatusPanel statusPanel;//��ǰ���õ�΢������壬���ڹ��������
	private CommentPanel commentPanel;//��ǰ���õ����۵���壬���ڹ��������
	private FriendsPanel friendsPanel;//��ǰ���õķ�˿(��ע������壬���ڹ��������
	private DirectMessagePanel directMessagePanel;////��ǰ����˽�ŵ���壬���ڹ������
	/**
	 * @param StatusPanel StatusPanelThread: ������ѡ���������ϵ�΢�����
	 * @author ��ţ��֪��
	 */
	public MyScrollPane(StatusPanel statusPanel){
		super(statusPanel);
		this.statusPanel = statusPanel;
		common();
	}
	/**
	 * @param CommentPanel commentPanel: ������ѡ���������ϵ��������
	 * @author ��ţ��֪��
	 */
	public MyScrollPane(CommentPanel commentPanel) {
		super(commentPanel);
		this.commentPanel = commentPanel;
		common();
	}
	public MyScrollPane(FriendsPanel friendsPanel) {
		super(friendsPanel);
		this.friendsPanel = friendsPanel;
		common();
	}
	public MyScrollPane(DirectMessagePanel directMessagePanel) {
		super(directMessagePanel);
		this.directMessagePanel = directMessagePanel;
		common();
	}
	/**
	 * ������干�еĶ���
	 * @author ��ţ��֪��
	 */
	private void common(){
		setBounds(0,108,432,450);
		getVerticalScrollBar().setUnitIncrement(30);
		setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener(){
			public void adjustmentValueChanged(AdjustmentEvent e) {
				int currentValue = getVerticalScrollBar().getMaximum() - 
					getVerticalScrollBar().getVisibleAmount();
				if(e.getValue() == currentValue){
					if(statusPanel != null){
						if (!statusPanel.loading) {
							Thread thread = new Thread(new AddThread());
							thread.start();
							statusPanel.loading = true;
						}
					}
					else if(commentPanel != null){
						if (!commentPanel.loading) {
							Thread thread = new Thread(new AddThread());
							thread.start();
							commentPanel.loading = true;
						}
					}
					else if(friendsPanel != null){
						if (!friendsPanel.loading) {
							Thread thread = new Thread(new AddThread());
							thread.start();
							friendsPanel.loading = true;
						}
					}
					else if(directMessagePanel != null){
						if (!directMessagePanel.loading) {
							Thread thread = new Thread(new AddThread());
							thread.start();
							directMessagePanel.loading = true;
						}
					}
				}
			}
		});
	}
	/**
	 * ��ȡҪ�������һҳ��΢��
	 * @author ��ţ��֪��
	 */
	private List<Status> listOfStatus(){
		List<Status> statuses = null;
		try {
			if(currentTabNum == HOMEPAGE){
				statuses = weibo.getFriendsTimeline(new Paging(homeCount));	
				++homeCount;
			}
			else if(currentTabNum == MyScrollPane.MENTION){
				statuses = weibo.getMentions(new Paging(mentionCount));
				++mentionCount;
			}
			else if(currentTabNum == MyScrollPane.FAVORITES){
				statuses = weibo.getFavorites(favoritescount);
				++favoritescount;
			}
			else if(currentTabNum == MyScrollPane.USERTIMELINE){
				statuses = weibo.getUserTimeline(new Paging(userTimelineCount));
				++userTimelineCount;
			}
			else if(currentTabNum == MyScrollPane.OTHER_USERTIMELINE){
				statuses = weibo.getUserTimeline(AnotherUser.userId,
						new Paging(other_userTimelineCount));
				++other_userTimelineCount;
			}
		} catch (WeiboException e) {
			System.out.println("������һҳ΢��ʱ�����쳣��");
			e.printStackTrace();
		}
		return statuses;
	}
	/**
	 * ��ȡҪ�������һҳ������
	 * @author ��ţ��֪��
	 */
	private List<Comment> listOfComment() {
		List<Comment> comments = null;
		try {
			if(currentTabNum == COMMENTS){
				comments = weibo.getCommentsTimeline(commentsCount);	
				++commentsCount;
			}
		}catch (WeiboException e) {
			System.out.println("������һҳ����ʱ�����쳣��");
			e.printStackTrace();
		}
		return comments;
	}
	/**
	 * ��ȡҪ�������һҳ�ķ�˿���߹�ע���û�
	 * @author ��ţ��֪��
	 */
	private List<User> listOfUser() {
		List<User> users = null;
		try {
			if(currentTabNum == FOLLOWERS){
				users = weibo.getFollowersUsers((followersCount-1)*20);	
				++followersCount;
			}
			else if(currentTabNum == FRIENDSHIPS){
				users = weibo.getFriendsUsers((friendshipsCount-1)*20);	
				++friendshipsCount;
			}
			else if(currentTabNum == OTHER_FOLLOWERS){
				users = weibo.getFollowersUsers(AnotherUser.userId, (other_followers-1)*20);	
				++other_followers;
			}
			else if(currentTabNum == OTHER_FRIENDSHIPS){
				users = weibo.getFriendsUsers(AnotherUser.userId, (other_friendships-1)*20);	
				++other_friendships;
			}
		}catch (WeiboException e) {
			System.out.println("������һҳ��˿/����ע���û�ʱ�����쳣��");
			e.printStackTrace();
		}
		return users;
	}
	
	/**
	 * ��ȡҪ�������һҳ��˽��
	 * @author ��ţ��֪��
	 */
	private List<DirectMessage> listOfDirectMessge() {
		List<DirectMessage> directMessages = null;
		try {
			directMessages = weibo.getDirectMessages(new Paging(directMessageCount));	
			++directMessageCount;
		}catch (WeiboException e) {
			System.out.println("������һҳ˽��ʱ�����쳣��");
			e.printStackTrace();
		}
		return directMessages;
	}
	public void setCurrentTabNum(int currentTabNum){
		this.currentTabNum = currentTabNum;
	}
	/**
	 * ����һҳ�����ݣ�΢������˿����ע�����۵ȣ�����
	 * @author ��ţ��֪��
	 */
	private class AddThread implements Runnable
	{
		public void run() {
			if(statusPanel != null)
				statusPanel.addStatus(listOfStatus());
			else if(commentPanel != null){
				commentPanel.addComment(listOfComment());
			}
			else if(friendsPanel != null){
				friendsPanel.addUser(listOfUser());
			}
			else if(directMessagePanel != null){
				directMessagePanel.addDirectMessage(listOfDirectMessge());
			}
		}
	}

}
