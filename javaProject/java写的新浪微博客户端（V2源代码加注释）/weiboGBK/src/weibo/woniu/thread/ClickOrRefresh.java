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
 * 继承自Thread，用于处理"刷新"和"第一次点击该选项卡"这个标签
 * @version 2.0
 * @author 蜗牛都知道
 */
public class ClickOrRefresh extends Thread {
	public final static String CLICK = "CLICK";//第一次点击
	public final static String REFRESH = "REFRESH";//更新
	private static int[] current_Array = {2,1,1,1,1,1,1,1,2,2,2};//用于判断是否第一次点击该选项卡
	private String currentstyle;//是点击选项卡，还是刷新
	private JPanel tabPanel;//选项卡面板
	private int currentTabNum;//当前选项卡的索引
	private Weibo weibo = LoginThread.weibo;
	/**
	 * @param JPanel tabPanel 当前选项卡的面板
	 * @param Weibo currentWeibo 当前的微博
	 * @param int currentTabNum 当前选项卡的索引
	 * @version 2.0
	 * @author 蜗牛都知道
	 */
	public ClickOrRefresh(JPanel tabPanel, int currentTabNum){
		this.tabPanel = tabPanel;
		this.currentTabNum = currentTabNum;
	}
	public void run() {
		//第一次点击
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
					TipDialog dialog = new TipDialog("应用程序的权限不足!");
					dialog.setLocation(LoginThread.mainDialog.getX()+15, 559);
					//initDirMsgTabPanel(listOfDirMsg(currentTabNum),currentTabNum);
				}
			}	
		}
		//刷新
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
	 * 获取要载入的微博(最新的微博)
	 * @author 蜗牛都知道
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
			System.out.println("载入最新微博时出现错误！ClickOrRefresh 70行！");
			e.printStackTrace();
		}
		return statuses;
	}
	/**
	 * 获取要载入的评论(最新的评论)
	 * @version 2.0
	 * @author 蜗牛都知道
	 */
	private List<Comment> listOfComment(int currentTabNum){
		List<Comment> comments = null;
		try {
			if(currentTabNum == MyScrollPane.COMMENTS){
				comments = weibo.getCommentsTimeline();	
			}
		} catch (WeiboException e) {
			System.out.println("载入最新评论时出现错误！ClickOrRefresh 87行！");
			e.printStackTrace();
		}
		return comments;
	}
	/**
	 * 获取要载入的粉丝，或者所关注的人
	 * @author 蜗牛都知道
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
			System.out.println("载入最新评论时出现错误！ClickOrRefresh 87行！");
			e.printStackTrace();
		}
		return users;
	}
	
	private List<DirectMessage> listOfDirMsg(int currentTabNum) {
		List<DirectMessage> directMessages = null;
		/*try {
			directMessages = weibo.getDirectMessages();
		} catch (WeiboException e) {
			System.out.println("载入最新私信时出现错误！ClickOrRefresh 167行！");
			e.printStackTrace();
		}*/
		TipDialog dialog = new TipDialog("应用程序的权限不足!");
		dialog.setLocation(LoginThread.mainDialog.getX()+15, 559);
		return directMessages;
	}
	/**
	 * 初始化首页，@我，收藏，微博四个选项卡的面板内容，下半部分
	 * @param List<Status>；currentTabNum当前选项卡的索引
	 * @version 2.0
	 * @author 蜗牛
	 */
	public void initStatusTabPanel(List<Status> statuses,
			int currentTabNum) {
		//每个选项卡的下半部分
		StatusPanel statusPane = new StatusPanel(statuses);
		if(currentTabNum == MyScrollPane.HOMEPAGE){//当前选项卡是首页
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
		MyScrollPane scrollPane = new MyScrollPane(statusPane);//滚动面板
		scrollPane.setCurrentTabNum(currentTabNum);
		tabPanel.add(scrollPane);
		
	}
	/**
	 * 评论选项卡的面板内容，下半部分
	 * @param List<Comment>，currentTabNum当前选项卡的索引
	 * @version 2.0
	 * @author 蜗牛
	 */
	private void initCommentTabPanel(List<Comment> comments, int currentTabNum) {
		//每个选项卡的下半部分
		CommentPanel commentPanel = new CommentPanel(comments);
		MyScrollPane scrollPane = new MyScrollPane(commentPanel);//滚动面板
		scrollPane.setCurrentTabNum(currentTabNum);
		tabPanel.add(scrollPane);
	}
	/**
	 * 粉丝，关注选项卡的面板内容，下半部分
	 * @param List<User>,currentTabNum当前选项卡的索引
	 * @version 2.0
	 * @author 蜗牛
	 */

	public void initUserTabPanel(List<User> users, int currentTabNum) {
		//每个选项卡的下半部分
		FriendsPanel friendsPanel = new FriendsPanel(users);
		if(currentTabNum == MyScrollPane.OTHER_FOLLOWERS ||
				currentTabNum == MyScrollPane.OTHER_FRIENDSHIPS){//当前是在查看别的用户
			friendsPanel.setOtherUser(true);
		}
		else if(currentTabNum == MyScrollPane.FRIENDSHIPS){
			friendsPanel.setFriendships(true);
		}
		MyScrollPane scrollPane = new MyScrollPane(friendsPanel);//滚动面板
		scrollPane.setCurrentTabNum(currentTabNum);
		tabPanel.add(scrollPane);
	}
	/**
	 * 私信的面板内容，下半部分
	 * @param List<DirectMessage>,currentTabNum当前选项卡的索引
	 * @version 2.0
	 * @author 蜗牛都知道
	 */
	private void initDirMsgTabPanel(List<DirectMessage> directMessages,
			int currentTabNum) {
		DirectMessagePanel directMessagePanel = new DirectMessagePanel(directMessages);
		MyScrollPane scrollPane = new MyScrollPane(directMessagePanel);//滚动面板
		scrollPane.setCurrentTabNum(currentTabNum);
		tabPanel.add(scrollPane);
	}
	
	/**
	 * 是第一次点击该选项卡，还是刷新    ClickOrRefresh.CLICK ,ClickOrRefresh.REFRESH 
	 * @author 蜗牛都知道
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
