package thread;


//class HJ implements Runnable{
//	public void run(){
//		System.out.println("���߳̿�ʼִ�У�");
//		try {
//			Thread.sleep(10000);
//		} catch (InterruptedException e) {
//			System.out.println("���̱߳����߳���ֹ��");
//			return ;
//		}
//		    System.out.println("���߳�������ֹ��");
//	}
//	/*
//	 * �����߳�ͬʱ��ʼִ��
//	 */
//	public static void main(String []args)
//	{
//		HJ test = new HJ();
//		Thread t= new Thread(test,"���߳�");
//		t.start();
//		try{
//			Thread.sleep(2000);//����߳��Ǹ��̣߳�ÿ�ζ�Ҫ��Thread��ʾ�߳�
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//		t.interrupt();
//		
//	}
//}


//class HJ implements Runnable {
//    public void run() {
//        while (true) {
//            System.out.println(Thread.currentThread().getName() + "������");
//        }
//    }
// 
//    public static void main(String[] args) {
//        HJ he = new HJ();
//        Thread demo = new Thread(he, "�߳�");
//       // demo.setDaemon(true);
//        demo.start();
//    }
//}


//import java.io.IOException;   
//
//public class TestThread extends Thread {   
//       
//    public TestThread() {   
//    }   
//    /** *//**  
//     * �̵߳�run�����������������߳�ͬʱ����  
//     */  
//    public void run(){   
//        for(int i = 1; i <= 100; i++){   
//            try{   
//                Thread.sleep(100);   
//                   
//            } catch (InterruptedException ex){   
//                ex.printStackTrace();   
//            }   
//            System.out.println(i);   
//        }   
//    }   
//    public static void main(String [] args){   
//        TestThread test = new TestThread();   
//        // ���������daemon����ô�߳̽����100��Ž���   
//        test.setDaemon(true);   
//        test.start();   
//        System.out.println("isDaemon = " + test.isDaemon());   
//        try {   
//            System.in.read(); // �������룬ʹ�����ڴ�ͣ�٣�һ�����յ��û����룬main�߳̽������ػ��߳��Զ�����   .����Ҳ����������100�����룬���̾����Ͻ���
//        } catch (IOException ex) {   
//            ex.printStackTrace();   
//        }   
//    }   
//} 

//
//class TestThread implements Runnable{
//	public void run(){
//		for(int i=1;i<=10;i++)
//		{
//			System.out.println(Thread.currentThread().getName()+"�������У�");
//			
////				try {
////					Thread.sleep(1000);
////				} catch (InterruptedException e) {
////					 //TODO Auto-generated catch block
////					e.printStackTrace();
////				}
//			}
//		}
//	
//	
//	public static void main(String []args){
//		TestThread t= new TestThread();
//		Thread tt= new Thread(t);
//		
//		Thread tt2= new Thread(t);
//		tt.start();
//		tt2.start();
//		new Thread(t).start();
//	}
//}


// class MyThread implements Runnable{
// 
//    private int ticket = 5;  //5��Ʊ
// 
//    public void run() {
//        for (int i=0; i<=20; i++) {
//            if (this.ticket > 0) {
//                System.out.println(Thread.currentThread().getName()+ "������Ʊ"+this.ticket--);
//            }
//        }
//    }
//}
//public class TestThread {
//     
//    public static void main(String [] args) {
//        MyThread my = new MyThread();
//        new Thread(my, "1�Ŵ���").start();
//        new Thread(my, "2�Ŵ���").start();
//        new Thread(my, "3�Ŵ���").start();
//    }
//} 

//class TestThread implements Runnable {
//	
//	private int  count =5;
//	public void run(){
//		for(int i=1;i<=20;i++){
//			
//			sale();
//		}
//	}
//	synchronized void  sale(){
//		
//			if(count>0)
//			{
//				
//					try {
//						Thread.sleep(0);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//					System.out.println(Thread.currentThread().getName()+"������Ʊ"+count);
//				count--;
//				
//			}
//	
//	}
//	public static void main(String [] a){
//		TestThread t=new TestThread();
//	     new Thread(t,"A").start();
//	     new Thread(t,"B").start();
//	     new Thread(t,"C").start();
//	     new Thread(t,"D").start();
//	     /*
//	      * �����Ϊ����ķ�ʽ�Ǵ���
//	      * ���Ƕ�����̣�����ͬ��
//	      * ������ִ�й�����ӵ�ж������ڴ浥Ԫ��������̹߳����ڴ棬�Ӷ����������˳��������Ч�ʡ�
//	      * �����Ǿ���һ���������ܵĳ������ĳ�����ݼ����ϵ�һ�����л,������ϵͳ������Դ����͵��ȵ�һ��������λ.
//	      */
////	     new Thread(new TestThread(),"A").start();
////	     new Thread(new TestThread(),"B").start();
////	     new Thread(new TestThread(),"C").start();
////	     new Thread(new TestThread(),"D").start();
//	}
//}
/*
 * ���һ�����������洢�ͽ�����Ϣ
 */
class Info {
	private String name="hj";
	private int age=20;
	private String [][] input=new String[10][];
	/*
	 * ��ʼ����ά����
	 * String [][] input = {{},{},{},{},{},{},{}};
	 */
	
//	public static void main(String []a ){
//		
//		Info info = new Info();
//		for(int i=0;i<10;i++)
//		{
//			info.input[i]=new String[(int) (Math.random()*100)];
//			System.out.println(info.input[i].length);
//		}
//	}
	void setName(String name)
	{
		this.name=name;
	}
	String getName ()
	{
		return this.name;
	}
	int  getAge()
	{
		return this.age;
	}
	void setAge(int age)
	{
		this.age=age;
	}
	static class producer implements Runnable{
		Info info = new Info();
		producer (Info info)
		{
			this.info=info;
		}
		boolean flag=true;
		public void run(){
			for(int i=1;i<=20;i++)
			{
				if(flag){
					flag=false;
					this.info.setAge(100);
					try{
	                    Thread.sleep(100);
	                }catch (Exception e) {
	                    e.printStackTrace();
	                }
					this.info.setName("jy");
				}else
				{
					flag=true;
					this.info.setAge(20);
					try{
	                    Thread.sleep(100);
	                }catch (Exception e) {
	                    e.printStackTrace();
	                }
					this.info.setName("hj");
				}
			}
		}
	}
	static class customer implements Runnable{
		Info info =new Info ();
		customer(Info info )
		{
			this.info=info;
		}
		public void run(){
			for(int i=1;i<=20;i++)
			{
				try{
                    Thread.sleep(100);
                }catch (Exception e) {
                    e.printStackTrace();
                }
				System.out.println(this.info.age+"<-->"+this.info.name);
			}
		}
	}
	
	public static void main(String [] a)
	{
		Info info =new Info();
		producer p= new producer(info);
		customer c = new customer(info);
		new Thread(p).start();
		new Thread(c).start();
	}
}










