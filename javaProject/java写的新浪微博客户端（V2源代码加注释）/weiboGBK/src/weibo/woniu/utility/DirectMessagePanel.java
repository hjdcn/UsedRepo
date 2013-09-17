package weibo.woniu.utility;

import java.util.List;

import javax.swing.JPanel;
import javax.swing.JTextPane;

import weibo.woniu.listener.MyHyperlinkListener;
import weibo.woniu.thread.LoginThread;
import weibo.woniu.widget.MainDialog;
import weibo4j.DirectMessage;
import weibo4j.User;
import weibo4j.WeiboException;
/**
 * 继承自JPanel，会于放置私信的面板
 * @version 2.0
 * @author 蜗牛
 */
public class DirectMessagePanel extends JPanel {
	
	private static final long serialVersionUID = -477060567259337835L;
	private List<DirectMessage> directMessages;
	private MyLabel metionLabel, replyLabel, deleteLabel;//@,回复,删除,收藏,转发,评论
	private int locationY = 0;
	private final int dirMsgWidth = 350;//评论放置所用的宽度
	public boolean loading = true;
	/**
	 * List<DirectMessage>
	 * @version 2.0
	 * @author 蜗牛
	 */
	public DirectMessagePanel(List<DirectMessage> directMessages) {
		this.directMessages = directMessages;
		setLayout(null);
		addDirectMessage(directMessages);
	}
	public void addDirectMessage(List<DirectMessage> directMessages) {
		this.directMessages = directMessages;
		AddDirectMessageThread thread = new AddDirectMessageThread();
		thread.start();
	}
	private class AddDirectMessageThread extends Thread{

		public void run() {
			for (final DirectMessage directMessage : directMessages) {
				User sender = directMessage.getSender();
				UserHeadImg userHead = new UserHeadImg(sender);
				userHead.setBounds(2, locationY, 54, 54);
				add(userHead);
				
				metionLabel = new MyLabel("@",sender);
				deleteLabel= new MyLabel("删除");
				deleteLabel.setDirectMessage(directMessage);
				replyLabel= new MyLabel("回复");
				deleteLabel.setDirectMessage(directMessage);
				metionLabel.setLocation(310, locationY + 2);
				replyLabel.setLocation(330, locationY + 2);
				deleteLabel.setLocation(370, locationY + 2);
				try {
					String name = sender.getName();
					String userName = LoginThread.weibo.verifyCredentials().getName(); 
					if(!name.equals(userName)){
						add(replyLabel);;
					}
				} catch (WeiboException e) {
					e.printStackTrace();
				}
				add(metionLabel);
				add(deleteLabel);
				
				
				int textPaneHeight = 0;
				//TODO 选择JTextPane来存放  原因：
				JTextPane dirMsgTextPane = CommonMethod.initTextPanel();
				//发评论的用户名字 +评论内容
				String commentContent = "<font face=微软雅黑 size=4><a href=\"@"
					+ sender.getName() + "\">"+sender.getName() 
					+ "</a>"+":"+"<br/>" + directMessage.getText();;
					//TODO 发微博的时间和来源
				String dateAndSource = "<br/>" + CommonMethod.getCreateDate(directMessage.getCreatedAt()) + "  通过"
						+ directMessage.getSender().getStatusSource() + "</font></b>";
				dirMsgTextPane.setText(commentContent + dateAndSource);
				dirMsgTextPane.setSize(dirMsgWidth, dirMsgTextPane.getPreferredSize().height);
				textPaneHeight = (int)dirMsgTextPane.getPreferredSize().height;
				dirMsgTextPane.setBounds(410-dirMsgWidth, locationY, dirMsgWidth, textPaneHeight);
				dirMsgTextPane.setText(CommonMethod.setForms(commentContent + dateAndSource));
				dirMsgTextPane.addHyperlinkListener(new MyHyperlinkListener());
				add(dirMsgTextPane);
				locationY = locationY + textPaneHeight;
				repaint();
				MainDialog.tabbedPane.repaint();
			}
			loading = false;
		}
	}
}
