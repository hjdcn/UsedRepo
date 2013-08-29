package test;

import java.io.IOException;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.protocol.Protocol;

public class NoCertificationHttpsGetSample {
	public static void main(String[] args) {
		Protocol myhttps = new Protocol("https",
				new MySecureProtocolSocketFactory(), 443);
		Protocol.registerProtocol("https", myhttps);
		// ����HttpClient��ʵ��
		HttpClient httpClient = new HttpClient();
		// ����GET������ʵ��
		GetMethod getMethod = new GetMethod("https://yourhttps.com/");
		// ʹ��ϵͳ�ṩ��Ĭ�ϵĻָ�����
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler());
		try {
			// ִ��getMethod
			int statusCode = httpClient.executeMethod(getMethod);
			if (statusCode != HttpStatus.SC_OK) {
				System.err.println("Method failed: "
						+ getMethod.getStatusLine());
			}
			// ��ȡ����
			byte[] responseBody = getMethod.getResponseBody();
			// ��������
			System.out.println(new String(responseBody));
		} catch (HttpException e) {
			System.out.println("Please check your provided http address!");
			// �����������쳣��������Э�鲻�Ի��߷��ص�����������
			e.printStackTrace();
		} catch (IOException e) {
			// ���������쳣
			e.printStackTrace();
		} finally {
			// �ͷ�����
			getMethod.releaseConnection();
		}

	}
}
