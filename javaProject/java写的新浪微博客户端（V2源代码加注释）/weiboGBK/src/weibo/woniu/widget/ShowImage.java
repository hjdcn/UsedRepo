package weibo.woniu.widget;

import java.awt.FileDialog;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputListener;

import weibo.woniu.thread.LoginThread;
import weibo.woniu.utility.CommonMethod;
/**
 * ��ʾͼƬ
 * @version 2.0
 * @author ��ţ��֪��
 */
public class ShowImage extends JDialog {

	private static final long serialVersionUID = 5339185340276572409L;
	private int imgWidth, imgHeight;// ͼƬ�Ŀ���
	private String imgSite;// ͼƬ�ĵ�ַ
	private BufferedImage bi;// ��������ͼƬ
	private JLabel label;
	private FileDialog save_Dialog = new FileDialog(this, "����ͼƬ��",
			FileDialog.SAVE);// ����Ի���
	/**
	 * @param String ͼƬ��ַ
	 * @version 2.0
	 * @author ��ţ��֪��
	 */
	public ShowImage(String imgSite) {
		this.imgSite = imgSite;
		label = new JLabel("��������ͼƬ...�˳��밴 Esc");
		label.setFont(new Font("΢���ź�", Font.BOLD, 14));
		label.setBounds(35, 76, 180, 15);
		add(label);
		setLayout(null);
		setUndecorated(true);
		setSize(250, 150);
		setLocation(350, 200);
		setVisible(true);
		addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					dispose();
					// LoginThread.mainDialog.requestFocus();
				}
			}
		});
		DragPicListener listener = new DragPicListener(this);
		addMouseListener(listener);
		addMouseMotionListener(listener);
		new ShowImageThread().start();

	}
	/**
	 * ��ʾͼƬ���߳�
	 * @return JPopupMenu
	 * @version 2.0
	 * @author ��ţ��֪��
	 */
	private class ShowImageThread extends Thread {
		public void run() {
			try {
				bi = javax.imageio.ImageIO.read(new URL(imgSite));
			} catch (IOException e1) {
				label.setText("����ʧ�ܣ����Ժ����ԡ�");
				e1.printStackTrace();
			}
			imgHeight = bi.getHeight();
			imgWidth = bi.getWidth();
			JPanel imgPanel = new JPanel() {
				private static final long serialVersionUID = 6246862165441423926L;
				public void paintComponent(Graphics g) {
					super.paintComponent(g);
					Graphics2D g2d = (Graphics2D) g;
					if (bi != null) {
						g2d.drawImage(bi, 0, 0, this);
					}
				}
			};
			if (label != null) {
				remove(label);
			}
			setSize(imgWidth, imgHeight);
			imgPanel.setSize(imgWidth, imgHeight);
			add(imgPanel);

			addMouseListener(new MouseAdapter() { // ���ڵ�����¼�����
				public void mousePressed(MouseEvent event) { // ������
					triggerEvent(event); // ����triggerEvent���������¼�
				}

				public void mouseReleased(MouseEvent event) { // �ͷ����
					triggerEvent(event);
				}

				private void triggerEvent(MouseEvent event) { // �����¼�
					if (event.isPopupTrigger()) { // ����ǵ����˵��¼�(����ƽ̨��ͬ���ܲ�ͬ)
						JPopupMenu popupMenu = createMenu();
						popupMenu.show(event.getComponent(), event.getX(), event
								.getY()); // ��ʾ�˵�
					}
				}
			});
		}
	}
	/**
	 * �����Ҽ��˵�
	 * @return JPopupMenu
	 * @version 2.0
	 * @author ��ţ��֪��
	 */
	private JPopupMenu createMenu(){ 
		JPopupMenu popupMenu = new JPopupMenu();
		popupMenu.setFont(new Font("΢���ź�", Font.BOLD, 12));
		JMenuItem saveItem = new JMenuItem("����");
		saveItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setIconImage(getToolkit().getImage("image/logo.jpg"));
				save_Dialog.setDirectory("D:");
				save_Dialog.setFile("δ����"
						+ imgSite.substring(imgSite.lastIndexOf(".")));
				save_Dialog.setVisible(true);
				try {
					ImageIO.write(bi, imgSite.substring(imgSite
							.lastIndexOf(".") + 1), new File(save_Dialog
							.getDirectory(), save_Dialog.getFile()));
				} catch (IOException e1) {
					System.out.println("����ͼƬʧ�ܣ�");
					e1.printStackTrace();
				}
			}
		});
		JMenuItem originalItem = new JMenuItem("ԭͼ");
		originalItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CommonMethod.browse(imgSite);
				dispose();
			}
		});
		JMenuItem exitItem = new JMenuItem("�ر�");
		exitItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				LoginThread.mainDialog.requestFocus();
			}
		});
		popupMenu.add(saveItem);
		popupMenu.addSeparator();// �ָ���
		popupMenu.add(originalItem);
		popupMenu.addSeparator();// �ָ���
		popupMenu.add(exitItem);
		return popupMenu;
	}
	/**
	 * ͼƬ�϶��ļ�����
	 * @version 2.0
	 * @author ��ţ��֪��
	 */
	private class DragPicListener implements MouseInputListener{// ����¼�����
		private JDialog dialog;
		Point point = new Point(0, 0); // �����
		public DragPicListener(JDialog dialog) {
			this.dialog = dialog;
		}
		public void mouseDragged(MouseEvent e) {
			Point newPoint = SwingUtilities.convertPoint(dialog, e.getPoint(),
					getParent());
			setLocation(getX() + (newPoint.x - point.x), getY()
					+ (newPoint.y - point.y));
			point = newPoint; // ���������
		}
		public void mousePressed(MouseEvent e) {
			point = SwingUtilities.convertPoint(dialog, e.getPoint(), dialog
					.getParent()); // �õ���ǰ�����
		}
		public void mouseMoved(MouseEvent e) {
		}
		public void mouseReleased(MouseEvent e) {
		}

		public void mouseEntered(MouseEvent e) {
		}

		public void mouseExited(MouseEvent e) {
		}

		public void mouseClicked(MouseEvent e) {
		}
	}
	
}
