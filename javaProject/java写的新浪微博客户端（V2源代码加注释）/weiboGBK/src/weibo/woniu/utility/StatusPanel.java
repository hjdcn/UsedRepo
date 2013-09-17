package weibo.woniu.utility;

import java.awt.Dimension;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JTextPane;

import weibo.woniu.listener.MyHyperlinkListener;
import weibo.woniu.thread.LoginThread;
import weibo.woniu.widget.AnotherUser;
import weibo.woniu.widget.MainDialog;
import weibo4j.Status;
import weibo4j.WeiboException;
/**
 * 继承自JPanel并实现了Runnable接口，会于放置微博的面板
 * @version 2.0
 * @author 蜗牛
 */
public class StatusPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2693246827482720961L;
	private int locationY = 0;//用于每个控件的Y坐标位置
	private List<Status> statuses;//一个存放微博的list
	private long id = 999999999999999999L;//刷新是用于过滤已经显现的微博
	private final int statusWidth = 350;//微博放置所用的宽度
	private final int retweetWidth = 390;//转发的微博所用的宽度
	public Boolean loading = true;//判断当前20条微博是否加载完，用于判断滚动条滚动到最底时是否要加载下一页
	private MyLabel favLabel, retweetLabel, commentLabel,
		directMsgLabel,metionLabel,deleteLabel, cancelFavLabel;//收藏，转发，评论,私信,@,删除,取消收藏
	private JTextPane statusTextPane;//放置微博
	private int locationX = 170;//控制JLabel的水位置;
	private boolean isTimeline = false;//判断是否是首页
	private boolean isOtherUser = false;//用于判断应当repaint()哪个tabbedPane
	private boolean isUserTimeLine = false;
	private boolean isFavourite  = false;
	public StatusPanel(List<Status> statuses){
		this.statuses = statuses;
		setLayout(null);
		addStatus(statuses);
	}
	public void addStatus(List<Status> statuses) {
		this.statuses = statuses;
		AddStatusThread thread = new AddStatusThread();
		thread.start();
	}
	private class AddStatusThread extends Thread{
		public void run() {
			for (final Status status : statuses) {
				if(isTimeline){
					if(id == status.getId() || id < status.getId()){
						System.err.println(status.getId()+"已经显现。StatusPanelThread 52行");
						continue;
					}
				}
				id = status.getId();
			
				//头像
				UserHeadImg userHead = new UserHeadImg(status.getUser());
				userHead.setBounds(2, locationY, 54, 54);
				add(userHead);
				
				addStatustoPanel(status,statusWidth);
				//微博是否是转发的
				if (status.isRetweet()) {
					locationX = 205;
					addStatustoPanel(status.getRetweetedStatus(),retweetWidth);
				}
			}
			loading = false;
		}
	}
	/**
	 *增加一条微博到面板中
     *@author 蜗牛都知道
     */
	private void addStatustoPanel(final Status status, int width){
		int textPaneHeight = 0;
		int statusImgHeight = 0;//一条微博图片的高度+23
		//TODO 选择JTextPane来存放  原因：
		statusTextPane = CommonMethod.initTextPanel();
		//statusTextPane.addMouseListener(new MyMouseListener());
		
		initLabel(status);
		//发微薄的用户名字 +微博内容
		String statusContent = "<font face=微软雅黑 size=4><a href=\"@"
			+ status.getUser().getName() + "\">";
		if(width == retweetWidth){
			statusContent += "@";
		}
		statusContent += status.getUser().getName() + "</a>"+":"+"<br/>"
				+ status.getText();
		
		//TODO 用于存放微博中含有的图片地址
		String imgURL = "";
		if (!"".equals(status.getThumbnail_pic())) {
			imgURL = "<br /><br /><a href=\"*"
					+ status.getBmiddle_pic()
					+ "\"><img border=\"0\" src="
					+ status.getThumbnail_pic() + " /></a>";
			statusImgHeight = CommonMethod.getImgHeight(status.getThumbnail_pic()) + 23;
		}
		//TODO 发微博的时间和来源
		String dateAndSource = "<br/>" +CommonMethod.getCreateDate(status.getCreatedAt()) + "  通过"
				+ status.getSource() + "</font></b>";
		
		statusTextPane.setText(statusContent + dateAndSource);
		statusTextPane.setSize(width, statusTextPane.getPreferredSize().height+ statusImgHeight);
		//TODO 此时statusTextPanel.getPreferredSize().height只含有status + lastText
		textPaneHeight = statusTextPane.getPreferredSize().height + statusImgHeight;
		//TODO 通过setText就把imgURL也放上JTextPane上了
		statusTextPane.setText(CommonMethod.setForms(statusContent + imgURL + dateAndSource));
		statusTextPane.setBounds(410-width, locationY, width, textPaneHeight);
		statusTextPane.addHyperlinkListener(new MyHyperlinkListener(status));
		add(statusTextPane);
		//TODO 这句很关键：去掉之后会没了滚动条
		setPreferredSize(new Dimension(450, locationY));
		locationY = locationY + textPaneHeight;
		repaint();
		if(isOtherUser)//如果是查看别的用户的话，repaint NewWeibo里的tabbedPane
			AnotherUser.tabbedPane.repaint();
		else
			MainDialog.tabbedPane.repaint();
	}
	
	/**
	 *初始化转发，收藏，@，评论等标签
     *@author 蜗牛都知道
	 * @param Status 
     */
	private void initLabel(final Status status){
		directMsgLabel = new MyLabel("私信",status.getUser());
		retweetLabel= new MyLabel("转发",status);
		commentLabel= new MyLabel("评论",status);
		metionLabel = new MyLabel("@",status.getUser());
		favLabel= new MyLabel("收藏",status);
		cancelFavLabel = new MyLabel("取消收藏",status);
		deleteLabel = new MyLabel("删除", status);
		

		favLabel.setLocation(locationX, 2);
		cancelFavLabel.setLocation(locationX-25, 2);
		retweetLabel.setLocation(locationX+40, 2);
		commentLabel.setLocation(locationX+80, 2);
		metionLabel.setLocation(locationX+120, 2);
		deleteLabel.setLocation(locationX+140, 2);
		directMsgLabel.setLocation(locationX+140, 2);
		statusTextPane.add(retweetLabel);
		statusTextPane.add(commentLabel);
		statusTextPane.add(metionLabel);
		if(isFavourite && locationX == 170){
			statusTextPane.add(cancelFavLabel);
		}
		else
			statusTextPane.add(favLabel);
		if(!isUserTimeLine){
			try {
				String name = status.getUser().getName();
				String userName = LoginThread.weibo.verifyCredentials().getName(); 
				if(!name.equals(userName)){
					statusTextPane.add(directMsgLabel);
				}
			} catch (WeiboException e) {
				e.printStackTrace();
			}
		}
		else {
			if(locationX == 205)//是转发的哪个微博
				statusTextPane.add(directMsgLabel);
			else
				statusTextPane.add(deleteLabel);
		}
		locationX = 170;
	}
	/**
	 *是否是首页的标志
     *@author 蜗牛都知道
     */
	public void setIsTimeline(Boolean isTimeline){
		this.isTimeline = isTimeline;
	}
	/**
	 *是否是在AnotherUser上的标志
     *@author 蜗牛都知道
     */
	public void setOtherUser(boolean isOtherUser) {
		this.isOtherUser = isOtherUser;
	}
	/**
	 *是否是在主界面微博选项卡上的标志
     *@author 蜗牛都知道
     */
	public void setIsUserTimeLine(boolean isUserTimeLine) {
		this.isUserTimeLine = isUserTimeLine;
	}
	/**
	 *是否是在主界面收藏选项卡上的标志
     *@author 蜗牛都知道
     */
	public void setFavourite(boolean isFavourite) {
		this.isFavourite = isFavourite;
	}
}
