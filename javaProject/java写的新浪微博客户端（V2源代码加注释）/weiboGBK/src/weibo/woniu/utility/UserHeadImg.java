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
 * 每一个头像都是这个类的一个对象
 * @version 2.0
 * @author 蜗牛都知道
 */
public class UserHeadImg extends JLabel{

	private static final long serialVersionUID = -856655269996837618L;

	public UserHeadImg(final User user){
		ImageIcon userImg = new ImageIcon(user
				.getProfileImageURL());
		setIcon(userImg);
		setCursor(new Cursor(Cursor.HAND_CURSOR));//手形鼠标
		addMouseListener(new MouseAdapter(){
			public void mouseEntered(MouseEvent e) {
				setToolTipText("简介："+user.getDescription());
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
