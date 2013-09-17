package weibo.woniu.widget;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileFilter;

import weibo.woniu.thread.LoginThread;
import weibo.woniu.utility.MyLabel;
import weibo4j.Comment;
import weibo4j.Status;
import weibo4j.User;
import weibo4j.WeiboException;

public class UpdateStatus extends JDialog implements MouseListener {

	/**
	 * 发微博，发私信等等的窗口
	 * @version 2.0
	 * @author 蜗牛
	 */
	private static final long serialVersionUID = 2842572587931578187L;
	public static final int JUST_STATUS = 1;//只发微博
	public static final int WITH_PICURE = 2;//发带图片的微博
	public static final int REPOST_STATUS = 3;//转发
	public static final int COMMENT_STATUS = 4;//评论
	public static final int DIRECT_MESSAGE = 5;//私信
	public static final int METION_HIM = 6;//@
	public static final int REPLY = 7;//回复
	private JTextArea writePlace; //微博的文本
	private JButton empty,close,send;//清空，关闭，发送
	private MyLabel emotions;//表情
	private JLabel tip;//提示标签
	private JScrollPane scrollPane;//放置图片的JScrollPane
	private File file;//选择的图片文件
	private int style;
	private int locationY;//控制控件的垂直坐标
	private Status status;
	private User user;
	public static Comment comment;//用于回复时获得评论者的名字
	public UpdateStatus(int style) {
		this.style = style;
		setTitle("分享你的新鲜事！");
		common();
		writePlace.setText("天晴吗？");
	}
	public UpdateStatus(int style, Status status) {
		this.style = style;
		this.status = status;
		common();
		
		if(style == REPOST_STATUS){
			setTitle("转发"+ status.getUser().getName()+"的微博:");
			if(status.isRetweet()){
				writePlace.setText("//@"+status.getUser().getName()+":"+status.getText());
			}
			else
				writePlace.setText("转发微博.");
		}
		else if(style == COMMENT_STATUS){
			setTitle("评论"+ status.getUser().getName()+"的微博:");
			writePlace.setText("...");
		}
		else if(style == REPLY){
			if(comment != null){
				setTitle("评论"+ status.getUser().getName()+"的微博：");
				writePlace.setText("回复 @"+ comment.getUser().getName()+":");
			}
		}
	}
	public UpdateStatus(int style, User user) {
		this.style = style;
		this.user = user;
		common();
		if(style == DIRECT_MESSAGE){
			setTitle("给"+ user.getName()+"发私信：");
			writePlace.setText("Hello world!");
		}
		else if(style == METION_HIM){
			setTitle("@"+ user.getName()+"：");
			writePlace.setText("@"+ user.getName()+" ");
		}
	}
	public void mouseClicked(MouseEvent arg0) {
		JFileChooser fileChooser = new JFileChooser("E:\\image");
		// 设置图像文件过滤器
		fileChooser.setFileFilter(new FileFilter() {
			public boolean accept(File file) { // 可接受的文件类型
				String name = file.getName().toLowerCase(); // 获取文件名
				return name.endsWith(".gif") || name.endsWith(".jpg")
						|| name.endsWith(".jpeg") || file.isDirectory();
			}
			public String getDescription() {
				return "图像文件"; // 文件描述
			}
		});
		int result = fileChooser.showOpenDialog(this); // 显示文件选择对话框
		if(result == JFileChooser.CANCEL_OPTION){
			return;
		}
		file = fileChooser.getSelectedFile();
		ImageIcon icon = null;
		try {
			icon = new ImageIcon(ImageIO.read(file));
		} catch (IOException e) {
			System.out.println("图片读取失败！SendImageFrame 160。");
			e.printStackTrace();
		}
		JLabel imgLabel = new JLabel(icon);
		JPanel imgPanel = new JPanel();
		imgPanel.add(imgLabel);
		imgPanel.setPreferredSize(new Dimension(icon.getIconWidth(),icon.getIconHeight()));
		if(scrollPane != null){
			remove(scrollPane);
			validate();//别忘了这句
			repaint();

		}
		scrollPane = new JScrollPane(imgPanel);
		scrollPane.getVerticalScrollBar().setUnitIncrement(30);
		scrollPane.setBounds(5, 113, 475, 447);
		add(scrollPane);
		validate();
		repaint();
	}

	private void common(){
		setLayout(null);
		setVisible(true);
		
		writePlace = new JTextArea();
		writePlace.setLineWrap(true);
		writePlace.setWrapStyleWord(true);
		writePlace.setFont(new Font("微软雅黑", Font.PLAIN, 16));
		writePlace.setBackground(new Color(0xeeeeee));
		writePlace.setEditable(true);
		
		if(style == WITH_PICURE){
			locationY = 80;
			setBounds(200, 0, 500, 600);
			writePlace.setFont(new Font("微软雅黑", Font.PLAIN, 15));
			JLabel selectLabel = new JLabel("点击选择文件");
			selectLabel.addMouseListener(this);
			selectLabel.setBounds(0, 300, 500, 50);
			selectLabel.setHorizontalAlignment(SwingConstants.CENTER);
			selectLabel.setForeground(Color.green);
			selectLabel.setFont(new Font("微软雅黑", Font.BOLD, 24));
			add(selectLabel);
		}
		else {
			locationY = 112;
			setBounds(200, 80, 500, 180);
		}
		
		Image logo = getToolkit().getImage("image/logo.jpg");
		setIconImage(logo);
		

		ImageIcon icon = new ImageIcon("image/smile.gif");
		
		emotions = new MyLabel("表情", icon, SwingConstants.LEFT);
		emotions.setBounds(5, locationY+2, 58, 20);
		add(emotions);
		
		empty = new JButton("清空");
		empty.setFont(new Font("微软雅黑",Font.PLAIN,14));
		empty.setBounds(65, locationY, 80, 30);
		add(empty);
		empty.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				writePlace.setText("");
			}
		});
		close = new JButton("关闭");
		close.setBounds(310, locationY, 80, 30);
		close.setFont(new Font("微软雅黑",Font.PLAIN,14));
		add(close);
		close.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		send = new JButton("发微");
		send.setBounds(400, locationY, 80, 30);
		send.setFont(new Font("微软雅黑",Font.PLAIN,14));
		add(send);
		send.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Thread thread = new Thread(new SendThread());
				thread.start();
			}
		});
		
		int num = 140 - writePlace.getText().length();
		tip = new JLabel("(你还可输入" + num + "个字。)");// +status.getUser().getName()
		tip.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		tip.setBounds(150, locationY+2, 165, 20);
		add(tip);
		
		writePlace.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				int num = 140 - writePlace.getText().length();
				tip.setText("(你还可输入" + num + "个字。)");
			}
			public void insertUpdate(DocumentEvent arg0) {
				int num = 140 - writePlace.getText().length();
				tip.setText("(你还可输入" + num + "个字。)");
			}
			public void removeUpdate(DocumentEvent arg0) {
				int num = 140 - writePlace.getText().length();
				tip.setText("(你还可输入" + num + "个字。)");
			}
		});
		JScrollPane scrollPane = new JScrollPane(writePlace);
		scrollPane.setBounds(2, 2, 480, locationY);
		add(scrollPane);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		repaint();
	}
	public void mouseEntered(MouseEvent arg0) {

	}

	public void mouseExited(MouseEvent arg0) {

	}
	public void mousePressed(MouseEvent arg0) {

	}
	public void mouseReleased(MouseEvent arg0) {
		
	}
	
	private class SendThread implements Runnable
	{
		public void run() {
			String sendText = writePlace.getText();
			close.setEnabled(false);
			send.setEnabled(false);
			try {
				if(style == WITH_PICURE){
					LoginThread.weibo.uploadStatus(sendText, file);
					TipDialog dialog = new TipDialog("该发送！");
					dialog.setLocation(LoginThread.mainDialog.getX()+15, LoginThread.mainDialog.getY()+559);
				}
				else if(style == JUST_STATUS || style == METION_HIM){
					LoginThread.weibo.updateStatus(sendText);
					TipDialog dialog = new TipDialog("该发送！");
					dialog.setLocation(LoginThread.mainDialog.getX()+15, LoginThread.mainDialog.getY()+559);
				}
				else if(style == REPOST_STATUS){
					LoginThread.weibo.repost(String.valueOf(status.getId()),
							writePlace.getText());
					TipDialog dialog = new TipDialog("该微博已转发！");
					dialog.setLocation(LoginThread.mainDialog.getX()+15, LoginThread.mainDialog.getY()+559);
				}
				else if(style == COMMENT_STATUS){
					LoginThread.weibo.updateComment(writePlace.getText(), 
							String.valueOf(status.getId()));
					TipDialog dialog = new TipDialog("已评论！");
					dialog.setLocation(LoginThread.mainDialog.getX()+15, LoginThread.mainDialog.getY()+559);
				}
				else if(style == DIRECT_MESSAGE){
					LoginThread.weibo.sendDirectMessage(String.valueOf(user
							.getId()),writePlace.getText());
					TipDialog dialog = new TipDialog("私信已经发送！");
					dialog.setLocation(LoginThread.mainDialog.getX()+15, LoginThread.mainDialog.getY()+559);
				}
				else if(style == REPLY){
					if(comment != null){
						LoginThread.weibo.updateComment(writePlace.getText(), String.
								valueOf(status.getUser().getId()), 
								String.valueOf(comment.getId()));
					}
					TipDialog dialog = new TipDialog("已回复，请刷新查看!");
					dialog.setLocation(LoginThread.mainDialog.getX()+15, LoginThread.mainDialog.getY()+559);
				}
			} catch (WeiboException e) {
				e.printStackTrace();
			}
			dispose();
		}
	}
}


