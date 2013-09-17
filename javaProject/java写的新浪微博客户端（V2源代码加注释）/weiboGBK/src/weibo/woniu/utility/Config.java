package weibo.woniu.utility;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
/**
 * ��ȡ�����ļ�
 * @version 2.0
 * @author ��ţ��֪��
 */
public class Config {
	private final static String filename = "Config.ini";
	private String username;
	private String password;
	public Config() throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(filename));
		read(reader);
		reader.close();
	}
	private void read(BufferedReader reader) throws IOException {
		String line;
		while ((line = reader.readLine()) != null) {
			parseLine(line);
		}
	}
	/*
	 * д��ini
	 */
	public void write(String content) throws IOException {
		FileWriter fileWriter = new FileWriter(filename); 
		fileWriter.write(content);
		fileWriter.flush();
		fileWriter.close();
	}
	private void parseLine(String line) {
		line = line.trim();//�����ַ����ĸ���������ǰ���հ׺�β���հס�
		if (line.matches(".*=.*")) {
			int i = line.indexOf('='); 
			String name = line.substring(0, i); 
			String value = line.substring(i + 1); 
			if(name.equals("�˺�")){
				username = value;
			}
			else if(name.equals("����")){
				password = value;
			}
		}
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
