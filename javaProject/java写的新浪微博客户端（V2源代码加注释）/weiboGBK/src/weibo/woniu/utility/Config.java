package weibo.woniu.utility;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
/**
 * 读取配置文件
 * @version 2.0
 * @author 蜗牛都知道
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
	 * 写入ini
	 */
	public void write(String content) throws IOException {
		FileWriter fileWriter = new FileWriter(filename); 
		fileWriter.write(content);
		fileWriter.flush();
		fileWriter.close();
	}
	private void parseLine(String line) {
		line = line.trim();//返回字符串的副本，忽略前导空白和尾部空白。
		if (line.matches(".*=.*")) {
			int i = line.indexOf('='); 
			String name = line.substring(0, i); 
			String value = line.substring(i + 1); 
			if(name.equals("账号")){
				username = value;
			}
			else if(name.equals("密码")){
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
