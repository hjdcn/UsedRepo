package test;
import java.io.IOException;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

public class PostSample {

	public static void main(String[] args) {
		// ����HttpClient��ʵ��
		HttpClient httpClient = new HttpClient();
		String url = "http://weibo.com/?c=spr_web_sq_baidub_weibo_t001";
		// ����POST������ʵ��
		PostMethod postMethod = new PostMethod(url);
		// ʹ��ϵͳ�ṩ��Ĭ�ϵĻָ�����
		postMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler());
		// ��������?���ֵ
		NameValuePair[] data = { new NameValuePair("id", "1107402232@qq.com"),
				new NameValuePair("passwd", "8505283043") };
		// ���?��ֵ����postMethod��
		postMethod.setRequestBody(data);
		try {
			// ִ��postMethod
			int statusCode = httpClient.executeMethod(postMethod);
			// HttpClient����Ҫ����ܺ�̷����������POST��PUT�Ȳ����Զ�����ת��
			// 301����302
			if (statusCode == HttpStatus.SC_MOVED_PERMANENTLY
					|| statusCode == HttpStatus.SC_MOVED_TEMPORARILY) {
				// ��ͷ��ȡ��ת��ĵ�ַ
				Header locationHeader = postMethod
						.getResponseHeader("location");
				String location = null;
				if (locationHeader != null) {
					location = locationHeader.getValue();
					System.out
							.println("The page was redirected to:" + location);

				} else {
					System.err.println("Location field value is null.");
				}
				return;
			}
			if (statusCode != HttpStatus.SC_OK) {
				System.err.println("Method failed: "
						+ postMethod.getStatusLine());
			}
			// ��ȡ����
			byte[] responseBody = postMethod.getResponseBody();
			// ��������
			System.out.println(new String(responseBody));

		} catch (HttpException e) {
			// ����������쳣��������Э�鲻�Ի��߷��ص�����������
			System.out.println("Please check your provided http address!");
			e.printStackTrace();
		} catch (IOException e) {
			// ���������쳣
			e.printStackTrace();
		} finally {
			// �ͷ�����
			postMethod.releaseConnection();
		}
	}
}
