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
 * 显示图片
 * @version 2.0
 * @author 蜗牛都知道
 */
public class ShowImage extends JDialog {

	private static final long serialVersionUID = 5339185340276572409L;
	private int imgWidth, imgHeight;// 图片的宽，高
	private String imgSite;// 图片的地址
	private BufferedImage bi;// 缓冲区的图片
	private JLabel label;
	private FileDialog save_Dialog = new FileDialog(this, "保存图片：",
			FileDialog.SAVE);// 保存对话框
	/**
	 * @param String 图片地址
	 * @version 2.0
	 * @author 蜗牛都知道
	 */
	public ShowImage(String imgSite) {
		this.imgSite = imgSite;
		label = new JLabel("正在载入图片...退出请按 Esc");
		label.setFont(new Font("微软雅黑", Font.BOLD, 14));
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
	 * 显示图片的线程
	 * @return JPopupMenu
	 * @version 2.0
	 * @author 蜗牛都知道
	 */
	private class ShowImageThread extends Thread {
		public void run() {
			try {
				bi = javax.imageio.ImageIO.read(new URL(imgSite));
			} catch (IOException e1) {
				label.setText("载入失败！请稍后再试。");
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

			addMouseListener(new MouseAdapter() { // 窗口的鼠标事件处理
				public void mousePressed(MouseEvent event) { // 点击鼠标
					triggerEvent(event); // 调用triggerEvent方法处理事件
				}

				public void mouseReleased(MouseEvent event) { // 释放鼠标
					triggerEvent(event);
				}

				private void triggerEvent(MouseEvent event) { // 处理事件
					if (event.isPopupTrigger()) { // 如果是弹出菜单事件(根据平台不同可能不同)
						JPopupMenu popupMenu = createMenu();
						popupMenu.show(event.getComponent(), event.getX(), event
								.getY()); // 显示菜单
					}
				}
			});
		}
	}
	/**
	 * 创建右键菜单
	 * @return JPopupMenu
	 * @version 2.0
	 * @author 蜗牛都知道
	 */
	private JPopupMenu createMenu(){ 
		JPopupMenu popupMenu = new JPopupMenu();
		popupMenu.setFont(new Font("微软雅黑", Font.BOLD, 12));
		JMenuItem saveItem = new JMenuItem("保存");
		saveItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setIconImage(getToolkit().getImage("image/logo.jpg"));
				save_Dialog.setDirectory("D:");
				save_Dialog.setFile("未命名"
						+ imgSite.substring(imgSite.lastIndexOf(".")));
				save_Dialog.setVisible(true);
				try {
					ImageIO.write(bi, imgSite.substring(imgSite
							.lastIndexOf(".") + 1), new File(save_Dialog
							.getDirectory(), save_Dialog.getFile()));
				} catch (IOException e1) {
					System.out.println("保存图片失败！");
					e1.printStackTrace();
				}
			}
		});
		JMenuItem originalItem = new JMenuItem("原图");
		originalItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CommonMethod.browse(imgSite);
				dispose();
			}
		});
		JMenuItem exitItem = new JMenuItem("关闭");
		exitItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				LoginThread.mainDialog.requestFocus();
			}
		});
		popupMenu.add(saveItem);
		popupMenu.addSeparator();// 分割线
		popupMenu.add(originalItem);
		popupMenu.addSeparator();// 分割线
		popupMenu.add(exitItem);
		return popupMenu;
	}
	/**
	 * 图片拖动的监听器
	 * @version 2.0
	 * @author 蜗牛都知道
	 */
	private class DragPicListener implements MouseInputListener{// 鼠标事件处理
		private JDialog dialog;
		Point point = new Point(0, 0); // 坐标点
		public DragPicListener(JDialog dialog) {
			this.dialog = dialog;
		}
		public void mouseDragged(MouseEvent e) {
			Point newPoint = SwingUtilities.convertPoint(dialog, e.getPoint(),
					getParent());
			setLocation(getX() + (newPoint.x - point.x), getY()
					+ (newPoint.y - point.y));
			point = newPoint; // 更改坐标点
		}
		public void mousePressed(MouseEvent e) {
			point = SwingUtilities.convertPoint(dialog, e.getPoint(), dialog
					.getParent()); // 得到当前坐标点
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
