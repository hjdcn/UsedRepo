package weibo.woniu.utility;

import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import weibo.woniu.thread.LoginThread;
import weibo.woniu.widget.TipDialog;
import weibo.woniu.widget.UpdateStatus;
import weibo4j.Comment;
import weibo4j.DirectMessage;
import weibo4j.Status;
import weibo4j.User;
import weibo4j.Weibo;
import weibo4j.WeiboException;
/**
 * �̳���JLabel��һ���ࡣ���ͻ��˴󲿷ֱ�ǩ����ʹ��������
 * @version 2.0
 * @author ��ţ
 */
public class MyLabel extends JLabel implements MouseListener {
	private static final long serialVersionUID = -1307086623157384054L;
	private boolean isEntered = false;
	private Status status;
	private Weibo weibo = LoginThread.weibo;
	private User user;
	private Comment comment;
	private DirectMessage directMessage;
	public MyLabel(String str) {
		super(str);
		setFont(new Font("΢���ź�",Font.PLAIN,14));
		setSize(getPreferredSize());
		addMouseListener(this);
	}
	public MyLabel(ImageIcon icon) {
		super(icon);
		addMouseListener(this);
	}
	public MyLabel(String string, ImageIcon icon, int loaction) {
		super(string,icon,loaction);
		setFont(new Font("΢���ź�",Font.PLAIN,14));
		addMouseListener(this);
	}

	public MyLabel(String str, Status status) {
		super(str);
		setFont(new Font("΢���ź�",Font.PLAIN,14));
		setSize(getPreferredSize());
		this.status = status;
		addMouseListener(this);
	}

	public MyLabel(String str, User user) {
		super(str);
		setFont(new Font("΢���ź�",Font.PLAIN,14));
		setSize(getPreferredSize());
		this.user = user;
		addMouseListener(this);
	}
	protected void paintBorder(Graphics g) {
		int w = this.getSize().width;
		int h = this.getSize().height;
		if (isEntered) {
			g.drawLine(0, h - 1, w - 1, h - 1);
		}
	}

	public void mouseClicked(MouseEvent mouseEvent) {
		Thread thread = new Thread(new ClickedThread());
		thread.start();
	}

	public void mousePressed(MouseEvent mouseEvent) {
	}

	public void mouseReleased(MouseEvent mouseEvent) {
	}

	public void mouseEntered(MouseEvent mouseEvent) {
		isEntered = true;
		repaint();
		setCursor(new Cursor(Cursor.HAND_CURSOR));//�������
	}

	public void mouseExited(MouseEvent mouseEvent) {
		isEntered = false;
		this.repaint();
	}
	private class ClickedThread implements Runnable
	{
		public void run() {
			if(getText() == "��ͼ"){
				new UpdateStatus(UpdateStatus.WITH_PICURE);
			}
			else if(getText() == "��΢��"){
				new UpdateStatus(UpdateStatus.JUST_STATUS);
			}
			else if(getText() == "ת��"){
				new UpdateStatus(UpdateStatus.REPOST_STATUS,status);
			}
			else if(getText() == "����"){
				new UpdateStatus(UpdateStatus.COMMENT_STATUS,status);
			}
			else if(getText() == "˽��"){
				TipDialog dialog = new TipDialog("Ӧ�ó����Ȩ�޲���!");
				dialog.setLocation(LoginThread.mainDialog.getX()+15, 559);
				//new UpdateStatus(UpdateStatus.DIRECT_MESSAGE,user);
			}
			else if(getText() == "@"){	
				new UpdateStatus(UpdateStatus.METION_HIM,user);
			}
			else if(getText() == "ɾ��"){
				try {
					if(comment != null){
						weibo.destroyComment(comment.getId());
					}
					else if(directMessage != null){
						weibo.destroyDirectMessage(directMessage.getId());
					}
					else if (status != null){
						weibo.destroyStatus(status.getId());
					}
					TipDialog dialog = new TipDialog("�ɹ�ɾ������ˢ�²鿴!");
					dialog.setLocation(LoginThread.mainDialog.getX()+15, LoginThread.mainDialog.getY()+559);
				} catch (WeiboException e) {
					System.out.println("ɾ��ʧ�ܣ�");
					e.printStackTrace();
				}
			}
			else if(getText() == "�ظ�"){
				if(comment != null){
					UpdateStatus.comment = comment;
					new UpdateStatus(UpdateStatus.REPLY,status);
				}
				else if(directMessage != null){
				}
			}
			else if(getText() == "�ղ�"){
					try {
						weibo.createFavorite(status.getId());
					} catch (WeiboException e) {
						System.out.println("�ղ�ʧ�ܣ�MyLabel(139)");
						e.printStackTrace();
					}
					TipDialog dialog = new TipDialog("���ղ�!");
					dialog.setLocation(LoginThread.mainDialog.getX()+15, LoginThread.mainDialog.getY()+559);
			}
			else if(getText() == "ȡ���ղ�"){
				try {
					weibo.destroyFavorite(status.getId());
					TipDialog dialog = new TipDialog("���ղ��ѱ�ȡ������ˢ�²鿴��");
					dialog.setLocation(LoginThread.mainDialog.getX()+15, 559);
				} catch (WeiboException e) {
					e.printStackTrace();
				}
			}
			else if(getText() == "+��ע"){
				try {
					weibo.createFriendship(String.valueOf(user.getId()));
				} catch (WeiboException e) {
					e.printStackTrace();
				}
				TipDialog dialog = new TipDialog("�ѹ�ע!");
				dialog.setLocation(LoginThread.mainDialog.getX()+15, 559);
			}
			else if(getText() == "ȡ����ע"){
				try {
					weibo.destroyFriendship(String.valueOf(user.getId()));
				} catch (WeiboException e) {
					e.printStackTrace();
				}
				TipDialog dialog = new TipDialog("��ȡ����ע����ˢ�²鿴!");
				dialog.setLocation(LoginThread.mainDialog.getX()+15, 559);
			}
			else if(getText() == "ע��"){
				CommonMethod.browse("http://weibo.com/signup/signup.php?ps=u3&lang=zh");
			}
			else if(getText() == "�һ�"){
				CommonMethod.browse("http://account.weibo.com/forgot/password?from=index");
			}
		}
	}
	public void setComment(Comment comment) {
		this.comment = comment;
	}
	public void setDirectMessage(DirectMessage directMessage) {
		this.directMessage = directMessage;
	}
}
