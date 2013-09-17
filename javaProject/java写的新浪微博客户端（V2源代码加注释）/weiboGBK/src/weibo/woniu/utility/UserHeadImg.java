package weibo.woniu.utility;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import weibo4j.User;

/**
 * ÿһ��ͷ����������һ������
 * @version 2.0
 * @author ��ţ��֪��
 */
public class UserHeadImg extends JLabel{

	private static final long serialVersionUID = -856655269996837618L;

	public UserHeadImg(final User user){
		ImageIcon userImg = new ImageIcon(user
				.getProfileImageURL());
		setIcon(userImg);
		setCursor(new Cursor(Cursor.HAND_CURSOR));//�������
		addMouseListener(new MouseAdapter(){
			public void mouseEntered(MouseEvent e) {
				setToolTipText("��飺"+user.getDescription());
				setBorder(BorderFactory.createLineBorder(Color.BLUE));
			}
			public void mouseExited(MouseEvent e) {
				setBorder(null);
			}
			public void mouseClicked(MouseEvent e) {
				String url = "http://weibo.com/"+user.getId();
				CommonMethod.browse(url);
			}
		});
	}
}
