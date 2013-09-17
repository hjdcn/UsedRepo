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
 * �̳���JPanel�����ڷ���˽�ŵ����
 * @version 2.0
 * @author ��ţ
 */
public class DirectMessagePanel extends JPanel {
	
	private static final long serialVersionUID = -477060567259337835L;
	private List<DirectMessage> directMessages;
	private MyLabel metionLabel, replyLabel, deleteLabel;//@,�ظ�,ɾ��,�ղ�,ת��,����
	private int locationY = 0;
	private final int dirMsgWidth = 350;//���۷������õĿ��
	public boolean loading = true;
	/**
	 * List<DirectMessage>
	 * @version 2.0
	 * @author ��ţ
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
				deleteLabel= new MyLabel("ɾ��");
				deleteLabel.setDirectMessage(directMessage);
				replyLabel= new MyLabel("�ظ�");
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
				//TODO ѡ��JTextPane�����  ԭ��
				JTextPane dirMsgTextPane = CommonMethod.initTextPanel();
				//�����۵��û����� +��������
				String commentContent = "<font face=΢���ź� size=4><a href=\"@"
					+ sender.getName() + "\">"+sender.getName() 
					+ "</a>"+":"+"<br/>" + directMessage.getText();;
					//TODO ��΢����ʱ�����Դ
				String dateAndSource = "<br/>" + CommonMethod.getCreateDate(directMessage.getCreatedAt()) + "  ͨ��"
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
