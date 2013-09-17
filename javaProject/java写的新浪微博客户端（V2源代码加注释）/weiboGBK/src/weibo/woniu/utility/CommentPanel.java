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
 * �̳���JPanel�����ڷ������۵����
 * @version 2.0
 * @author ��ţ��֪��
 */
public class CommentPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7888159334820651609L;
	private int locationX = 200;//���ڸ���MyLabel��X����λ��
	private int locationY = 0;//����ÿ���ؼ���Y����λ��
	public boolean loading = true;//�жϵ�ǰ20�������Ƿ�����꣬�����жϹ��������������ʱ�Ƿ�Ҫ������һҳ
	private MyLabel directMsgLabel, metionLabel, replyLabel, deleteLabel,
		favLabel, retweetLabel, commentLabel;//˽��,@,�ظ�,ɾ��,�ղ�,ת��,����
	private final int commentWidth = 350;//���۷������õĿ��
	private final int statusWidth = 395;//΢�����õĿ��
	private final int retweetWidth = 375;//ת����΢�����õĿ��
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
				//ͷ��
				UserHeadImg userHead = new UserHeadImg(comment.getUser());
				userHead.setBounds(2, locationY, 54, 54);
				add(userHead);
				
				directMsgLabel = new MyLabel("˽��",comment.getUser());
				metionLabel = new MyLabel("@",comment.getUser());
				deleteLabel= new MyLabel("ɾ��",comment.getStatus());
				deleteLabel.setComment(comment);
				replyLabel= new MyLabel("�ظ�",comment.getStatus());
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
				//TODO ѡ��JTextPane�����  ԭ��
				JTextPane commentTextPane = CommonMethod.initTextPanel();
				//�����۵��û����� +��������
				String commentContent = "<font face=΢���ź� size=4><a href=\"@"
					+ comment.getUser().getName() + "\">"+comment.getUser().getName() 
					+ "</a>"+":"+"<br/>" + comment.getText();;
					//TODO ��΢����ʱ�����Դ
				String dateAndSource = "<br/>" + CommonMethod.getCreateDate(comment.getCreatedAt()) + "  ͨ��"
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
				//TODO ���ܹؼ���ȥ��֮���û�˹�����
				setPreferredSize(new Dimension(270, locationY));
			}
			loading = false;
		}
	}
	private void addStatustoPanel(final Status status, int width){
		int textPaneHeight = 0;//����ͼƬ�������ı��ĸ߶�
		int statusImgHeight = 0;//һ��΢��ͼƬ�ĸ߶�+23
		//TODO ѡ��JTextPane�����  ԭ��
		statusTextPane = CommonMethod.initTextPanel();
		//��ʼ��JLabel
		initLabel(status);
		//��΢�����û����� +΢������
		String statusContent = "";
		
		if(width == statusWidth){
			statusContent = "<font face=΢���ź�  size=4>��" ;
		}
		else{
			statusContent = "<font face=΢���ź�  size=4>ת" ;
		}
		statusContent+= "<a href=\"@"
			+ status.getUser().getName() + "\">" + "@"
			+ status.getUser().getName()
			+ "<a/>��΢��: <br/>" 
			+ status.getText()+"</font>";
		
		//TODO ���ڴ��΢���к��е�ͼƬ��ַ
		String imgURL = "";
		if (!"".equals(status.getThumbnail_pic())) {
			imgURL = "<br /><br /><a href=\"*"
					+ status.getBmiddle_pic()
					+ "\"><img border=\"0\" src="
					+ status.getThumbnail_pic() + " /></a>";
			statusImgHeight = CommonMethod.getImgHeight(status.getThumbnail_pic()) + 23;
		}
		//TODO ��΢����ʱ�����Դ
		String dateAndSource = "<br/>" + CommonMethod.getCreateDate(status.getCreatedAt()) + "  ͨ��"
				+ status.getSource() + "</font>";
		
		statusTextPane.setText(statusContent + dateAndSource);
		statusTextPane.setSize(width, statusTextPane.getPreferredSize().height+ statusImgHeight);
		//TODO ��ʱstatusTextPanel.getPreferredSize().heightֻ����status + lastText
		textPaneHeight = statusTextPane.getPreferredSize().height + statusImgHeight;
		
		//TODO ͼƬ��Ȼ��String?????
		statusTextPane.setText(CommonMethod.setForms(statusContent + imgURL + dateAndSource));
		statusTextPane.setBounds(410-width, locationY, width, textPaneHeight);
		statusTextPane.addHyperlinkListener(new MyHyperlinkListener(status));
		add(statusTextPane);
		locationY = locationY + textPaneHeight;
		repaint();
		MainDialog.tabbedPane.repaint();
	}
	private void initLabel(final Status status){
		favLabel= new MyLabel("�ղ�",status);
		retweetLabel= new MyLabel("ת��",status);
		commentLabel= new MyLabel("����",status);
		metionLabel = new MyLabel("@",status.getUser());
		deleteLabel = new MyLabel("ɾ��", status);
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
