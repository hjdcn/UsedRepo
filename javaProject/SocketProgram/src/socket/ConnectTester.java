package socket;
import java.net.*;
import java.io.*;

public class ConnectTester {
	public static void main(String []args){
		String host="localhost";
		int port =135;
		if(args.length>0)
		{
			host =args[0];
			port = Integer.parseInt(args[1]);
		}
		new ConnectTester().connect(host, port);
	}
	
	public void connect(String host, int port){
		SocketAddress  remoteAddr = new InetSocketAddress(host,port);
		Socket socket =null;
		String result="";
		try{
			long begin =System.currentTimeMillis();
			socket =new Socket();
			socket.connect(remoteAddr,10000);
			long end = System.currentTimeMillis();
			result = (end-begin)+"ms";
			
		}catch(BindException e){
			result ="Local address and port can not binded";
		}catch(UnknownHostException e)
		{
			result = "unknow host";
		}catch(ConnectException e){
			result ="Connection refused";
		}catch(SocketTimeoutException e){
			result="Timeout";
		}catch(IOException e){
			result = "failure";
		}finally {
			if(socket!=null)
				try {
					socket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		System.out.println(remoteAddr+":"+result);
	}

}
