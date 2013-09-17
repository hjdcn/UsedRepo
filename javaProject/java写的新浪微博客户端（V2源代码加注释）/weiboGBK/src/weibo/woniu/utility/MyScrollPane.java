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
 * 继承自JScrollPane(滚动面板)的一个类，客户端每个选项卡共有的东西。
 * @author 蜗牛都知道
 */
public class MyScrollPane extends JScrollPane{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2316047352013532172L;
	public final static int HOMEPAGE  = 0;//主页
	public final static int MENTION  = 1;//@我
	public final static int COMMENTS  = 2;//评论
	public final static int DIRECT_MESSAGE  = 3;//私信
	public final static int FAVORITES  = 4;//收藏
	public final static int FOLLOWERS  = 5;//粉丝
	public final static int FRIENDSHIPS  = 6;//关注
	public final static int USERTIMELINE  = 7;//微博
	public final static int OTHER_USERTIMELINE  = 8;//另一个用户的微博
	public final static int OTHER_FRIENDSHIPS  = 9;//另一个用户的关注的人
	public final static int OTHER_FOLLOWERS = 10;//另一个用户的粉丝
	//计算当前是第几页
	private int homeCount = 2, mentionCount = 2, commentsCount = 2, 
		directMessageCount = 2, favoritescount = 2, followersCount = 2, 
		friendshipsCount = 2, userTimelineCount = 2, other_userTimelineCount = 2,
		other_friendships = 2, other_followers = 2;
	private int currentTabNum;//当前选项卡的索引
	private Weibo weibo = LoginThread.weibo;//当前的微博
	private StatusPanel statusPanel;//当前放置的微博的面板，放于滚动面板中
	private CommentPanel commentPanel;//当前放置的评论的面板，放于滚动面板中
	private FriendsPanel friendsPanel;//当前放置的粉丝(关注）的面板，放于滚动面板中
	private DirectMessagePanel directMessagePanel;////当前放置私信的面板，放于滚动面板
	/**
	 * @param StatusPanel StatusPanelThread: 放置在选项卡滚动面板上的微博面板
	 * @author 蜗牛都知道
	 */
	public MyScrollPane(StatusPanel statusPanel){
		super(statusPanel);
		this.statusPanel = statusPanel;
		common();
	}
	/**
	 * @param CommentPanel commentPanel: 放置在选项卡滚动面板上的评论面板
	 * @author 蜗牛都知道
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
	 * 滚动面板共有的东西
	 * @author 蜗牛都知道
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
	 * 获取要载入的下一页的微博
	 * @author 蜗牛都知道
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
			System.out.println("加载下一页微博时出现异常！");
			e.printStackTrace();
		}
		return statuses;
	}
	/**
	 * 获取要载入的下一页的评论
	 * @author 蜗牛都知道
	 */
	private List<Comment> listOfComment() {
		List<Comment> comments = null;
		try {
			if(currentTabNum == COMMENTS){
				comments = weibo.getCommentsTimeline(commentsCount);	
				++commentsCount;
			}
		}catch (WeiboException e) {
			System.out.println("加载下一页评论时出现异常！");
			e.printStackTrace();
		}
		return comments;
	}
	/**
	 * 获取要载入的下一页的粉丝或者关注的用户
	 * @author 蜗牛都知道
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
			System.out.println("加载下一页粉丝/所关注的用户时出现异常！");
			e.printStackTrace();
		}
		return users;
	}
	
	/**
	 * 获取要载入的下一页的私信
	 * @author 蜗牛都知道
	 */
	private List<DirectMessage> listOfDirectMessge() {
		List<DirectMessage> directMessages = null;
		try {
			directMessages = weibo.getDirectMessages(new Paging(directMessageCount));	
			++directMessageCount;
		}catch (WeiboException e) {
			System.out.println("加载下一页私信时出现异常！");
			e.printStackTrace();
		}
		return directMessages;
	}
	public void setCurrentTabNum(int currentTabNum){
		this.currentTabNum = currentTabNum;
	}
	/**
	 * 把下一页的数据（微博，粉丝，关注，评论等）载入
	 * @author 蜗牛都知道
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
