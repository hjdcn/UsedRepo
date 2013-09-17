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
 * 继承自JLabel的一个类。本客户端大部分标签都是使用这个类的
 * @version 2.0
 * @author 蜗牛
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
		setFont(new Font("微软雅黑",Font.PLAIN,14));
		setSize(getPreferredSize());
		addMouseListener(this);
	}
	public MyLabel(ImageIcon icon) {
		super(icon);
		addMouseListener(this);
	}
	public MyLabel(String string, ImageIcon icon, int loaction) {
		super(string,icon,loaction);
		setFont(new Font("微软雅黑",Font.PLAIN,14));
		addMouseListener(this);
	}

	public MyLabel(String str, Status status) {
		super(str);
		setFont(new Font("微软雅黑",Font.PLAIN,14));
		setSize(getPreferredSize());
		this.status = status;
		addMouseListener(this);
	}

	public MyLabel(String str, User user) {
		super(str);
		setFont(new Font("微软雅黑",Font.PLAIN,14));
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
		setCursor(new Cursor(Cursor.HAND_CURSOR));//手形鼠标
	}

	public void mouseExited(MouseEvent mouseEvent) {
		isEntered = false;
		this.repaint();
	}
	private class ClickedThread implements Runnable
	{
		public void run() {
			if(getText() == "发图"){
				new UpdateStatus(UpdateStatus.WITH_PICURE);
			}
			else if(getText() == "发微博"){
				new UpdateStatus(UpdateStatus.JUST_STATUS);
			}
			else if(getText() == "转发"){
				new UpdateStatus(UpdateStatus.REPOST_STATUS,status);
			}
			else if(getText() == "评论"){
				new UpdateStatus(UpdateStatus.COMMENT_STATUS,status);
			}
			else if(getText() == "私信"){
				TipDialog dialog = new TipDialog("应用程序的权限不足!");
				dialog.setLocation(LoginThread.mainDialog.getX()+15, 559);
				//new UpdateStatus(UpdateStatus.DIRECT_MESSAGE,user);
			}
			else if(getText() == "@"){	
				new UpdateStatus(UpdateStatus.METION_HIM,user);
			}
			else if(getText() == "删除"){
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
					TipDialog dialog = new TipDialog("成功删除，请刷新查看!");
					dialog.setLocation(LoginThread.mainDialog.getX()+15, LoginThread.mainDialog.getY()+559);
				} catch (WeiboException e) {
					System.out.println("删除失败！");
					e.printStackTrace();
				}
			}
			else if(getText() == "回复"){
				if(comment != null){
					UpdateStatus.comment = comment;
					new UpdateStatus(UpdateStatus.REPLY,status);
				}
				else if(directMessage != null){
				}
			}
			else if(getText() == "收藏"){
					try {
						weibo.createFavorite(status.getId());
					} catch (WeiboException e) {
						System.out.println("收藏失败！MyLabel(139)");
						e.printStackTrace();
					}
					TipDialog dialog = new TipDialog("已收藏!");
					dialog.setLocation(LoginThread.mainDialog.getX()+15, LoginThread.mainDialog.getY()+559);
			}
			else if(getText() == "取消收藏"){
				try {
					weibo.destroyFavorite(status.getId());
					TipDialog dialog = new TipDialog("该收藏已被取消，请刷新查看！");
					dialog.setLocation(LoginThread.mainDialog.getX()+15, 559);
				} catch (WeiboException e) {
					e.printStackTrace();
				}
			}
			else if(getText() == "+关注"){
				try {
					weibo.createFriendship(String.valueOf(user.getId()));
				} catch (WeiboException e) {
					e.printStackTrace();
				}
				TipDialog dialog = new TipDialog("已关注!");
				dialog.setLocation(LoginThread.mainDialog.getX()+15, 559);
			}
			else if(getText() == "取消关注"){
				try {
					weibo.destroyFriendship(String.valueOf(user.getId()));
				} catch (WeiboException e) {
					e.printStackTrace();
				}
				TipDialog dialog = new TipDialog("已取消关注，请刷新查看!");
				dialog.setLocation(LoginThread.mainDialog.getX()+15, 559);
			}
			else if(getText() == "注册"){
				CommonMethod.browse("http://weibo.com/signup/signup.php?ps=u3&lang=zh");
			}
			else if(getText() == "找回"){
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
