package weibo.woniu.widget;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;

import weibo.woniu.thread.LoginThread;
import weibo.woniu.utility.Config;
import weibo.woniu.utility.MyLabel;



public class LoginFrame extends JFrame{
	/**
	 * 登录框  
	 * @version 2.0
	 * @author 蜗牛都知道
	 */
	private static final long serialVersionUID = -4115869573010360842L;
	private JTextField userName;//账号输入框
	private JPasswordField password;//密码输入框
	private JLabel accountLabel, pswLabel;//账号，密码的标签
	private MyLabel registerLabel, findBackLabel;//注册，找回的标签
	private JButton loginButton;//登录与按钮
	private Image logo, background;//设置窗口图标、背景图片
	private  LoginFrame loginFrame;//用与参数传递
	private boolean isRemember = true;//是否记住密码
	private Config config;//简单读写配置ini的类
	public LoginFrame(){
		super("新浪微博");
		try {
		    UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");   
		} catch (Exception e) {
			e.printStackTrace();
		}
		setLayout(null);
		setBounds(500, 240, 340, 260);
		loginFrame = this;
		logo = getToolkit().getImage("image/logo.jpg");
		setIconImage(logo);//设置窗口图标
		background = new ImageIcon("image/background.jpg").getImage();
		setBackground(background);
		
		
		try {
			config = new Config();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		accountLabel = new JLabel("账号:");
		accountLabel.setFont(new Font("微软雅黑",Font.PLAIN,14));
		accountLabel.setBounds(40, 60, 40, 15);
		pswLabel = new JLabel("密码:");
		pswLabel.setFont(new Font("微软雅黑",Font.PLAIN,14));
		pswLabel.setBounds(40, 98, 40, 15);
		
		userName = new JTextField(config.getUsername());
		userName.addKeyListener(new ForKeyListener());
		userName.setBounds(88, 53, 155, 33);
		userName.addFocusListener(new FocusAdapter(){
			public void focusGained(FocusEvent e) {
				userName.selectAll();
			}
		});
		password = new JPasswordField(config.getPassword());
		password.addKeyListener(new ForKeyListener());
		password.setBounds(88, 90, 155, 33);

		
		registerLabel = new MyLabel("注册");
		registerLabel.setLocation(260, 60);
		findBackLabel = new MyLabel("找回");
		findBackLabel.setLocation(260, 98);

		JCheckBox rmbCBox = new JCheckBox("记住密码");
		rmbCBox.setSelected(true);
		rmbCBox.setBounds(90, 140, 77, 14);
		rmbCBox.addItemListener(new ItemListener(){
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.DESELECTED){
					isRemember = false;
				}
			}
		});
		JCheckBox autoCBox = new JCheckBox("自动登录");
		autoCBox.setBounds(175, 140, 77, 14);
		
		JButton setButton = new JButton("设置");
		setButton.setBounds(50, 170, 75, 27);
		
		loginButton = new JButton("登录");
		loginButton.setBounds(170, 170, 75, 27);
		loginButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event) {
				LoginThread logining = new LoginThread(loginFrame);
				logining.start();
			}
		});
		
		add(userName);
		add(password);
		add(accountLabel);
		add(pswLabel);
		add(registerLabel);
		add(findBackLabel);
		add(rmbCBox);
		add(autoCBox);
		add(loginButton);
		setVisible(true);
		addKeyListener(new ForKeyListener());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	//重载方法setBackground()
	public void setBackground(final Image background){
		//背景图片放于一个JLabel中
		JLabel imgLabel = new JLabel(){
			private static final long serialVersionUID = 3639945919293420809L;
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				int x = this.getWidth();
				int y = this.getHeight();
				g.drawImage(background, 0, 0, x, y, null);
			}
		};
		//将背景标签添加到JFrame的LayeredPane(分层)面板里。
		getLayeredPane().add(imgLabel, new Integer(Integer.MIN_VALUE));
		//将内容面板设为透明。这样LayeredPane面板中的背景才能显示出来。
		((JPanel)getContentPane()).setOpaque(false);
		imgLabel.setBounds(0, 0, getWidth(), getHeight());
	}
	public String getPassword(){
		return String.valueOf(password.getPassword());
	}
	public String getUserName(){
		return userName.getText();
	}
	public void setLoginButtonStatus(String text, boolean enable){
		loginButton.setText(text);
		loginButton.setEnabled(enable);
	}
	public static void main(String[] args) {
		 new LoginFrame();
	}
	private class ForKeyListener extends KeyAdapter{
		public void keyPressed(KeyEvent e) {
			if(e.getKeyCode() == KeyEvent.VK_ENTER){
				LoginThread logining = new LoginThread(loginFrame);
				logining.start();
			}
		}
	}
	public boolean isRemember() {
		return isRemember;
	}
	public Config getConfig() {
		return config;
	}
}
