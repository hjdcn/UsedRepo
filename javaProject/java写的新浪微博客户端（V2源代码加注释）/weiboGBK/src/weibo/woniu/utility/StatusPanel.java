package weibo.woniu.utility;

import java.awt.Dimension;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JTextPane;

import weibo.woniu.listener.MyHyperlinkListener;
import weibo.woniu.thread.LoginThread;
import weibo.woniu.widget.AnotherUser;
import weibo.woniu.widget.MainDialog;
import weibo4j.Status;
import weibo4j.WeiboException;
/**
 * �̳���JPanel��ʵ����Runnable�ӿڣ����ڷ���΢�������
 * @version 2.0
 * @author ��ţ
 */
public class StatusPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2693246827482720961L;
	private int locationY = 0;//����ÿ���ؼ���Y����λ��
	private List<Status> statuses;//һ�����΢����list
	private long id = 999999999999999999L;//ˢ�������ڹ����Ѿ����ֵ�΢��
	private final int statusWidth = 350;//΢���������õĿ��
	private final int retweetWidth = 390;//ת����΢�����õĿ��
	public Boolean loading = true;//�жϵ�ǰ20��΢���Ƿ�����꣬�����жϹ��������������ʱ�Ƿ�Ҫ������һҳ
	private MyLabel favLabel, retweetLabel, commentLabel,
		directMsgLabel,metionLabel,deleteLabel, cancelFavLabel;//�ղأ�ת��������,˽��,@,ɾ��,ȡ���ղ�
	private JTextPane statusTextPane;//����΢��
	private int locationX = 170;//����JLabel��ˮλ��;
	private boolean isTimeline = false;//�ж��Ƿ�����ҳ
	private boolean isOtherUser = false;//�����ж�Ӧ��repaint()�ĸ�tabbedPane
	private boolean isUserTimeLine = false;
	private boolean isFavourite  = false;
	public StatusPanel(List<Status> statuses){
		this.statuses = statuses;
		setLayout(null);
		addStatus(statuses);
	}
	public void addStatus(List<Status> statuses) {
		this.statuses = statuses;
		AddStatusThread thread = new AddStatusThread();
		thread.start();
	}
	private class AddStatusThread extends Thread{
		public void run() {
			for (final Status status : statuses) {
				if(isTimeline){
					if(id == status.getId() || id < status.getId()){
						System.err.println(status.getId()+"�Ѿ����֡�StatusPanelThread 52��");
						continue;
					}
				}
				id = status.getId();
			
				//ͷ��
				UserHeadImg userHead = new UserHeadImg(status.getUser());
				userHead.setBounds(2, locationY, 54, 54);
				add(userHead);
				
				addStatustoPanel(status,statusWidth);
				//΢���Ƿ���ת����
				if (status.isRetweet()) {
					locationX = 205;
					addStatustoPanel(status.getRetweetedStatus(),retweetWidth);
				}
			}
			loading = false;
		}
	}
	/**
	 *����һ��΢���������
     *@author ��ţ��֪��
     */
	private void addStatustoPanel(final Status status, int width){
		int textPaneHeight = 0;
		int statusImgHeight = 0;//һ��΢��ͼƬ�ĸ߶�+23
		//TODO ѡ��JTextPane�����  ԭ��
		statusTextPane = CommonMethod.initTextPanel();
		//statusTextPane.addMouseListener(new MyMouseListener());
		
		initLabel(status);
		//��΢�����û����� +΢������
		String statusContent = "<font face=΢���ź� size=4><a href=\"@"
			+ status.getUser().getName() + "\">";
		if(width == retweetWidth){
			statusContent += "@";
		}
		statusContent += status.getUser().getName() + "</a>"+":"+"<br/>"
				+ status.getText();
		
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
		String dateAndSource = "<br/>" +CommonMethod.getCreateDate(status.getCreatedAt()) + "  ͨ��"
				+ status.getSource() + "</font></b>";
		
		statusTextPane.setText(statusContent + dateAndSource);
		statusTextPane.setSize(width, statusTextPane.getPreferredSize().height+ statusImgHeight);
		//TODO ��ʱstatusTextPanel.getPreferredSize().heightֻ����status + lastText
		textPaneHeight = statusTextPane.getPreferredSize().height + statusImgHeight;
		//TODO ͨ��setText�Ͱ�imgURLҲ����JTextPane����
		statusTextPane.setText(CommonMethod.setForms(statusContent + imgURL + dateAndSource));
		statusTextPane.setBounds(410-width, locationY, width, textPaneHeight);
		statusTextPane.addHyperlinkListener(new MyHyperlinkListener(status));
		add(statusTextPane);
		//TODO ���ܹؼ���ȥ��֮���û�˹�����
		setPreferredSize(new Dimension(450, locationY));
		locationY = locationY + textPaneHeight;
		repaint();
		if(isOtherUser)//����ǲ鿴����û��Ļ���repaint NewWeibo���tabbedPane
			AnotherUser.tabbedPane.repaint();
		else
			MainDialog.tabbedPane.repaint();
	}
	
	/**
	 *��ʼ��ת�����ղأ�@�����۵ȱ�ǩ
     *@author ��ţ��֪��
	 * @param Status 
     */
	private void initLabel(final Status status){
		directMsgLabel = new MyLabel("˽��",status.getUser());
		retweetLabel= new MyLabel("ת��",status);
		commentLabel= new MyLabel("����",status);
		metionLabel = new MyLabel("@",status.getUser());
		favLabel= new MyLabel("�ղ�",status);
		cancelFavLabel = new MyLabel("ȡ���ղ�",status);
		deleteLabel = new MyLabel("ɾ��", status);
		

		favLabel.setLocation(locationX, 2);
		cancelFavLabel.setLocation(locationX-25, 2);
		retweetLabel.setLocation(locationX+40, 2);
		commentLabel.setLocation(locationX+80, 2);
		metionLabel.setLocation(locationX+120, 2);
		deleteLabel.setLocation(locationX+140, 2);
		directMsgLabel.setLocation(locationX+140, 2);
		statusTextPane.add(retweetLabel);
		statusTextPane.add(commentLabel);
		statusTextPane.add(metionLabel);
		if(isFavourite && locationX == 170){
			statusTextPane.add(cancelFavLabel);
		}
		else
			statusTextPane.add(favLabel);
		if(!isUserTimeLine){
			try {
				String name = status.getUser().getName();
				String userName = LoginThread.weibo.verifyCredentials().getName(); 
				if(!name.equals(userName)){
					statusTextPane.add(directMsgLabel);
				}
			} catch (WeiboException e) {
				e.printStackTrace();
			}
		}
		else {
			if(locationX == 205)//��ת�����ĸ�΢��
				statusTextPane.add(directMsgLabel);
			else
				statusTextPane.add(deleteLabel);
		}
		locationX = 170;
	}
	/**
	 *�Ƿ�����ҳ�ı�־
     *@author ��ţ��֪��
     */
	public void setIsTimeline(Boolean isTimeline){
		this.isTimeline = isTimeline;
	}
	/**
	 *�Ƿ�����AnotherUser�ϵı�־
     *@author ��ţ��֪��
     */
	public void setOtherUser(boolean isOtherUser) {
		this.isOtherUser = isOtherUser;
	}
	/**
	 *�Ƿ�����������΢��ѡ��ϵı�־
     *@author ��ţ��֪��
     */
	public void setIsUserTimeLine(boolean isUserTimeLine) {
		this.isUserTimeLine = isUserTimeLine;
	}
	/**
	 *�Ƿ������������ղ�ѡ��ϵı�־
     *@author ��ţ��֪��
     */
	public void setFavourite(boolean isFavourite) {
		this.isFavourite = isFavourite;
	}
}
