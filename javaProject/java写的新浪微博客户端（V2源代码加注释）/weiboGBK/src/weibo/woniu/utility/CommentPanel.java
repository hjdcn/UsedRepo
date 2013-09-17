package weibo.woniu.utility;

import java.awt.Dimension;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JTextPane;

import weibo.woniu.listener.MyHyperlinkListener;
import weibo.woniu.thread.LoginThread;
import weibo.woniu.widget.MainDialog;
import weibo4j.Comment;
import weibo4j.Status;
import weibo4j.WeiboException;
/**
 * 继承自JPanel，会于放置评论的面板
 * @version 2.0
 * @author 蜗牛都知道
 */
public class CommentPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7888159334820651609L;
	private int locationX = 200;//用于各个MyLabel的X坐标位置
	private int locationY = 0;//用于每个控件的Y坐标位置
	public boolean loading = true;//判断当前20条评论是否加载完，用于判断滚动条滚动到最底时是否要加载下一页
	private MyLabel directMsgLabel, metionLabel, replyLabel, deleteLabel,
		favLabel, retweetLabel, commentLabel;//私信,@,回复,删除,收藏,转发,评论
	private final int commentWidth = 350;//评论放置所用的宽度
	private final int statusWidth = 395;//微博所用的宽度
	private final int retweetWidth = 375;//转发的微博所用的宽度
	private JTextPane statusTextPane;
	private List<Comment> comments;
	public CommentPanel(List<Comment> comments) {
		this.comments = comments;
		setLayout(null);
		addComment(comments);
	}
	public void addComment(List<Comment> comments) {
		this.comments = comments;
		AddCommentThread thread = new AddCommentThread();
		thread.start();
		
	}
	private class AddCommentThread extends Thread{
		public void run() {
			for (final Comment comment : comments) {
				//头像
				UserHeadImg userHead = new UserHeadImg(comment.getUser());
				userHead.setBounds(2, locationY, 54, 54);
				add(userHead);
				
				directMsgLabel = new MyLabel("私信",comment.getUser());
				metionLabel = new MyLabel("@",comment.getUser());
				deleteLabel= new MyLabel("删除",comment.getStatus());
				deleteLabel.setComment(comment);
				replyLabel= new MyLabel("回复",comment.getStatus());
				replyLabel.setComment(comment);
				directMsgLabel.setLocation(270, locationY + 2);
				metionLabel.setLocation(310, locationY + 2);
				replyLabel.setLocation(330, locationY + 2);
				deleteLabel.setLocation(370, locationY + 2);
				try {
					String name = comment.getUser().getName();
					String userName = LoginThread.weibo.verifyCredentials().getName(); 
					if(!name.equals(userName)){
						add(directMsgLabel);;
					}
				} catch (WeiboException e) {
					e.printStackTrace();
				}
				add(metionLabel);
				add(deleteLabel);
				add(replyLabel);
				int textPaneHeight = 0;
				//TODO 选择JTextPane来存放  原因：
				JTextPane commentTextPane = CommonMethod.initTextPanel();
				//发评论的用户名字 +评论内容
				String commentContent = "<font face=微软雅黑 size=4><a href=\"@"
					+ comment.getUser().getName() + "\">"+comment.getUser().getName() 
					+ "</a>"+":"+"<br/>" + comment.getText();;
					//TODO 发微博的时间和来源
				String dateAndSource = "<br/>" + CommonMethod.getCreateDate(comment.getCreatedAt()) + "  通过"
						+ comment.getSource() + "</font></b>";
				commentTextPane.setText(commentContent + dateAndSource);
				commentTextPane.setSize(commentWidth, commentTextPane.getPreferredSize().height);
				textPaneHeight = (int)commentTextPane.getPreferredSize().height;
				commentTextPane.setBounds(410-commentWidth, locationY, commentWidth, textPaneHeight);
				commentTextPane.setText(CommonMethod.setForms(commentContent + dateAndSource));
				commentTextPane.addHyperlinkListener(new MyHyperlinkListener());
				add(commentTextPane);
				locationY = locationY + textPaneHeight;
				repaint();
				
				addStatustoPanel(comment.getStatus(), statusWidth);	
				if(comment.getStatus().isRetweet()){
					locationX = 190;
					addStatustoPanel(comment.getStatus().getRetweetedStatus(), retweetWidth);	
				}
				//TODO 这句很关键：去掉之后会没了滚动条
				setPreferredSize(new Dimension(270, locationY));
			}
			loading = false;
		}
	}
	private void addStatustoPanel(final Status status, int width){
		int textPaneHeight = 0;//处理图片外所有文本的高度
		int statusImgHeight = 0;//一条微博图片的高度+23
		//TODO 选择JTextPane来存放  原因：
		statusTextPane = CommonMethod.initTextPanel();
		//初始化JLabel
		initLabel(status);
		//发微薄的用户名字 +微博内容
		String statusContent = "";
		
		if(width == statusWidth){
			statusContent = "<font face=微软雅黑  size=4>评" ;
		}
		else{
			statusContent = "<font face=微软雅黑  size=4>转" ;
		}
		statusContent+= "<a href=\"@"
			+ status.getUser().getName() + "\">" + "@"
			+ status.getUser().getName()
			+ "<a/>的微博: <br/>" 
			+ status.getText()+"</font>";
		
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
		String dateAndSource = "<br/>" + CommonMethod.getCreateDate(status.getCreatedAt()) + "  通过"
				+ status.getSource() + "</font>";
		
		statusTextPane.setText(statusContent + dateAndSource);
		statusTextPane.setSize(width, statusTextPane.getPreferredSize().height+ statusImgHeight);
		//TODO 此时statusTextPanel.getPreferredSize().height只含有status + lastText
		textPaneHeight = statusTextPane.getPreferredSize().height + statusImgHeight;
		
		//TODO 图片竟然是String?????
		statusTextPane.setText(CommonMethod.setForms(statusContent + imgURL + dateAndSource));
		statusTextPane.setBounds(410-width, locationY, width, textPaneHeight);
		statusTextPane.addHyperlinkListener(new MyHyperlinkListener(status));
		add(statusTextPane);
		locationY = locationY + textPaneHeight;
		repaint();
		MainDialog.tabbedPane.repaint();
	}
	private void initLabel(final Status status){
		favLabel= new MyLabel("收藏",status);
		retweetLabel= new MyLabel("转发",status);
		commentLabel= new MyLabel("评论",status);
		metionLabel = new MyLabel("@",status.getUser());
		deleteLabel = new MyLabel("删除", status);
		retweetLabel.setLocation(locationX+40, 2);
		commentLabel.setLocation(locationX+80, 2);
		metionLabel.setLocation(locationX+120, 2);
		favLabel.setLocation(locationX+140, 2);
		deleteLabel.setLocation(locationX+140, 2);
		statusTextPane.add(favLabel);
		statusTextPane.add(retweetLabel);
		statusTextPane.add(commentLabel);
		statusTextPane.add(metionLabel);
		
		locationX = 200;
	}
}
