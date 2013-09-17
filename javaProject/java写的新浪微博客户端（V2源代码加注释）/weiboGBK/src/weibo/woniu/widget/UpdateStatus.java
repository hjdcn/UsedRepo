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
	 * ��΢������˽�ŵȵȵĴ���
	 * @version 2.0
	 * @author ��ţ
	 */
	private static final long serialVersionUID = 2842572587931578187L;
	public static final int JUST_STATUS = 1;//ֻ��΢��
	public static final int WITH_PICURE = 2;//����ͼƬ��΢��
	public static final int REPOST_STATUS = 3;//ת��
	public static final int COMMENT_STATUS = 4;//����
	public static final int DIRECT_MESSAGE = 5;//˽��
	public static final int METION_HIM = 6;//@
	public static final int REPLY = 7;//�ظ�
	private JTextArea writePlace; //΢�����ı�
	private JButton empty,close,send;//��գ��رգ�����
	private MyLabel emotions;//����
	private JLabel tip;//��ʾ��ǩ
	private JScrollPane scrollPane;//����ͼƬ��JScrollPane
	private File file;//ѡ���ͼƬ�ļ�
	private int style;
	private int locationY;//���ƿؼ��Ĵ�ֱ����
	private Status status;
	private User user;
	public static Comment comment;//���ڻظ�ʱ��������ߵ�����
	public UpdateStatus(int style) {
		this.style = style;
		setTitle("������������£�");
		common();
		writePlace.setText("������");
	}
	public UpdateStatus(int style, Status status) {
		this.style = style;
		this.status = status;
		common();
		
		if(style == REPOST_STATUS){
			setTitle("ת��"+ status.getUser().getName()+"��΢��:");
			if(status.isRetweet()){
				writePlace.setText("//@"+status.getUser().getName()+":"+status.getText());
			}
			else
				writePlace.setText("ת��΢��.");
		}
		else if(style == COMMENT_STATUS){
			setTitle("����"+ status.getUser().getName()+"��΢��:");
			writePlace.setText("...");
		}
		else if(style == REPLY){
			if(comment != null){
				setTitle("����"+ status.getUser().getName()+"��΢����");
				writePlace.setText("�ظ� @"+ comment.getUser().getName()+":");
			}
		}
	}
	public UpdateStatus(int style, User user) {
		this.style = style;
		this.user = user;
		common();
		if(style == DIRECT_MESSAGE){
			setTitle("��"+ user.getName()+"��˽�ţ�");
			writePlace.setText("Hello world!");
		}
		else if(style == METION_HIM){
			setTitle("@"+ user.getName()+"��");
			writePlace.setText("@"+ user.getName()+" ");
		}
	}
	public void mouseClicked(MouseEvent arg0) {
		JFileChooser fileChooser = new JFileChooser("E:\\image");
		// ����ͼ���ļ�������
		fileChooser.setFileFilter(new FileFilter() {
			public boolean accept(File file) { // �ɽ��ܵ��ļ�����
				String name = file.getName().toLowerCase(); // ��ȡ�ļ���
				return name.endsWith(".gif") || name.endsWith(".jpg")
						|| name.endsWith(".jpeg") || file.isDirectory();
			}
			public String getDescription() {
				return "ͼ���ļ�"; // �ļ�����
			}
		});
		int result = fileChooser.showOpenDialog(this); // ��ʾ�ļ�ѡ��Ի���
		if(result == JFileChooser.CANCEL_OPTION){
			return;
		}
		file = fileChooser.getSelectedFile();
		ImageIcon icon = null;
		try {
			icon = new ImageIcon(ImageIO.read(file));
		} catch (IOException e) {
			System.out.println("ͼƬ��ȡʧ�ܣ�SendImageFrame 160��");
			e.printStackTrace();
		}
		JLabel imgLabel = new JLabel(icon);
		JPanel imgPanel = new JPanel();
		imgPanel.add(imgLabel);
		imgPanel.setPreferredSize(new Dimension(icon.getIconWidth(),icon.getIconHeight()));
		if(scrollPane != null){
			remove(scrollPane);
			validate();//���������
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
		writePlace.setFont(new Font("΢���ź�", Font.PLAIN, 16));
		writePlace.setBackground(new Color(0xeeeeee));
		writePlace.setEditable(true);
		
		if(style == WITH_PICURE){
			locationY = 80;
			setBounds(200, 0, 500, 600);
			writePlace.setFont(new Font("΢���ź�", Font.PLAIN, 15));
			JLabel selectLabel = new JLabel("���ѡ���ļ�");
			selectLabel.addMouseListener(this);
			selectLabel.setBounds(0, 300, 500, 50);
			selectLabel.setHorizontalAlignment(SwingConstants.CENTER);
			selectLabel.setForeground(Color.green);
			selectLabel.setFont(new Font("΢���ź�", Font.BOLD, 24));
			add(selectLabel);
		}
		else {
			locationY = 112;
			setBounds(200, 80, 500, 180);
		}
		
		Image logo = getToolkit().getImage("image/logo.jpg");
		setIconImage(logo);
		

		ImageIcon icon = new ImageIcon("image/smile.gif");
		
		emotions = new MyLabel("����", icon, SwingConstants.LEFT);
		emotions.setBounds(5, locationY+2, 58, 20);
		add(emotions);
		
		empty = new JButton("���");
		empty.setFont(new Font("΢���ź�",Font.PLAIN,14));
		empty.setBounds(65, locationY, 80, 30);
		add(empty);
		empty.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				writePlace.setText("");
			}
		});
		close = new JButton("�ر�");
		close.setBounds(310, locationY, 80, 30);
		close.setFont(new Font("΢���ź�",Font.PLAIN,14));
		add(close);
		close.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		send = new JButton("��΢");
		send.setBounds(400, locationY, 80, 30);
		send.setFont(new Font("΢���ź�",Font.PLAIN,14));
		add(send);
		send.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Thread thread = new Thread(new SendThread());
				thread.start();
			}
		});
		
		int num = 140 - writePlace.getText().length();
		tip = new JLabel("(�㻹������" + num + "���֡�)");// +status.getUser().getName()
		tip.setFont(new Font("΢���ź�", Font.PLAIN, 15));
		tip.setBounds(150, locationY+2, 165, 20);
		add(tip);
		
		writePlace.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				int num = 140 - writePlace.getText().length();
				tip.setText("(�㻹������" + num + "���֡�)");
			}
			public void insertUpdate(DocumentEvent arg0) {
				int num = 140 - writePlace.getText().length();
				tip.setText("(�㻹������" + num + "���֡�)");
			}
			public void removeUpdate(DocumentEvent arg0) {
				int num = 140 - writePlace.getText().length();
				tip.setText("(�㻹������" + num + "���֡�)");
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
					TipDialog dialog = new TipDialog("�÷��ͣ�");
					dialog.setLocation(LoginThread.mainDialog.getX()+15, LoginThread.mainDialog.getY()+559);
				}
				else if(style == JUST_STATUS || style == METION_HIM){
					LoginThread.weibo.updateStatus(sendText);
					TipDialog dialog = new TipDialog("�÷��ͣ�");
					dialog.setLocation(LoginThread.mainDialog.getX()+15, LoginThread.mainDialog.getY()+559);
				}
				else if(style == REPOST_STATUS){
					LoginThread.weibo.repost(String.valueOf(status.getId()),
							writePlace.getText());
					TipDialog dialog = new TipDialog("��΢����ת����");
					dialog.setLocation(LoginThread.mainDialog.getX()+15, LoginThread.mainDialog.getY()+559);
				}
				else if(style == COMMENT_STATUS){
					LoginThread.weibo.updateComment(writePlace.getText(), 
							String.valueOf(status.getId()));
					TipDialog dialog = new TipDialog("�����ۣ�");
					dialog.setLocation(LoginThread.mainDialog.getX()+15, LoginThread.mainDialog.getY()+559);
				}
				else if(style == DIRECT_MESSAGE){
					LoginThread.weibo.sendDirectMessage(String.valueOf(user
							.getId()),writePlace.getText());
					TipDialog dialog = new TipDialog("˽���Ѿ����ͣ�");
					dialog.setLocation(LoginThread.mainDialog.getX()+15, LoginThread.mainDialog.getY()+559);
				}
				else if(style == REPLY){
					if(comment != null){
						LoginThread.weibo.updateComment(writePlace.getText(), String.
								valueOf(status.getUser().getId()), 
								String.valueOf(comment.getId()));
					}
					TipDialog dialog = new TipDialog("�ѻظ�����ˢ�²鿴!");
					dialog.setLocation(LoginThread.mainDialog.getX()+15, LoginThread.mainDialog.getY()+559);
				}
			} catch (WeiboException e) {
				e.printStackTrace();
			}
			dispose();
		}
	}
}


