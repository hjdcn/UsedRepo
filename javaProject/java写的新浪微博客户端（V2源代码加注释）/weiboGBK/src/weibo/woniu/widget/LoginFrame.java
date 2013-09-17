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
	 * ��¼��  
	 * @version 2.0
	 * @author ��ţ��֪��
	 */
	private static final long serialVersionUID = -4115869573010360842L;
	private JTextField userName;//�˺������
	private JPasswordField password;//���������
	private JLabel accountLabel, pswLabel;//�˺ţ�����ı�ǩ
	private MyLabel registerLabel, findBackLabel;//ע�ᣬ�һصı�ǩ
	private JButton loginButton;//��¼�밴ť
	private Image logo, background;//���ô���ͼ�ꡢ����ͼƬ
	private  LoginFrame loginFrame;//�����������
	private boolean isRemember = true;//�Ƿ��ס����
	private Config config;//�򵥶�д����ini����
	public LoginFrame(){
		super("����΢��");
		try {
		    UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");   
		} catch (Exception e) {
			e.printStackTrace();
		}
		setLayout(null);
		setBounds(500, 240, 340, 260);
		loginFrame = this;
		logo = getToolkit().getImage("image/logo.jpg");
		setIconImage(logo);//���ô���ͼ��
		background = new ImageIcon("image/background.jpg").getImage();
		setBackground(background);
		
		
		try {
			config = new Config();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		accountLabel = new JLabel("�˺�:");
		accountLabel.setFont(new Font("΢���ź�",Font.PLAIN,14));
		accountLabel.setBounds(40, 60, 40, 15);
		pswLabel = new JLabel("����:");
		pswLabel.setFont(new Font("΢���ź�",Font.PLAIN,14));
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

		
		registerLabel = new MyLabel("ע��");
		registerLabel.setLocation(260, 60);
		findBackLabel = new MyLabel("�һ�");
		findBackLabel.setLocation(260, 98);

		JCheckBox rmbCBox = new JCheckBox("��ס����");
		rmbCBox.setSelected(true);
		rmbCBox.setBounds(90, 140, 77, 14);
		rmbCBox.addItemListener(new ItemListener(){
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.DESELECTED){
					isRemember = false;
				}
			}
		});
		JCheckBox autoCBox = new JCheckBox("�Զ���¼");
		autoCBox.setBounds(175, 140, 77, 14);
		
		JButton setButton = new JButton("����");
		setButton.setBounds(50, 170, 75, 27);
		
		loginButton = new JButton("��¼");
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
	//���ط���setBackground()
	public void setBackground(final Image background){
		//����ͼƬ����һ��JLabel��
		JLabel imgLabel = new JLabel(){
			private static final long serialVersionUID = 3639945919293420809L;
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				int x = this.getWidth();
				int y = this.getHeight();
				g.drawImage(background, 0, 0, x, y, null);
			}
		};
		//��������ǩ��ӵ�JFrame��LayeredPane(�ֲ�)����
		getLayeredPane().add(imgLabel, new Integer(Integer.MIN_VALUE));
		//�����������Ϊ͸��������LayeredPane����еı���������ʾ������
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
