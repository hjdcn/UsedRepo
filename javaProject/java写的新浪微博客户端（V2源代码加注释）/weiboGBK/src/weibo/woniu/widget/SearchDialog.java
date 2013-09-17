package weibo.woniu.widget;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

import weibo.woniu.thread.LoginThread;
import weibo.woniu.utility.MyLabel;

/**
 * ËÑË÷¿ò:ÕÒÈË£¬ËÑË÷
 * @version 2.0
 * @author ÎÏÅ£¶¼ÖªµÀ
 */
public class SearchDialog extends JDialog{
	private static final long serialVersionUID = 3882762622508812968L;
	public SearchDialog(JLabel la){
		setUndecorated(true);
		setLayout(null);
		if(la.getText() == "ÕÒÈË"){
			setBounds(LoginThread.mainDialog.getX()+230, 70, 162, 27);
		}
		else setBounds(LoginThread.mainDialog.getX()+190, 70, 162, 27);
		JTextField textArea = new JTextField();
		textArea.setFont(new Font("Î¢ÈíÑÅºÚ", Font.BOLD, 12));
		textArea.setBounds(2, 2,138,23);
		ImageIcon icon = new ImageIcon("image/search.jpg");
		JLabel iconLabel = new MyLabel(icon);
		iconLabel.setBounds(140, 3, 20, 20);
		add(iconLabel);
		add(textArea);
		addWindowListener(new WindowAdapter(){
			public void windowDeactivated(WindowEvent arg0) {
				dispose();
			}
		});
		setVisible(true);
	}
	public void paint(Graphics g) {
		super.paint(g);
		g.drawRoundRect(0, 0, 160, 25, 7, 7);
	}
}
