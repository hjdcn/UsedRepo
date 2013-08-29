package thread;


//class HJ implements Runnable{
//	public void run(){
//		System.out.println("子线程开始执行！");
//		try {
//			Thread.sleep(10000);
//		} catch (InterruptedException e) {
//			System.out.println("子线程被主线程终止！");
//			return ;
//		}
//		    System.out.println("子线程正常终止！");
//	}
//	/*
//	 * 两个线程同时开始执行
//	 */
//	public static void main(String []args)
//	{
//		HJ test = new HJ();
//		Thread t= new Thread(test,"子线程");
//		t.start();
//		try{
//			Thread.sleep(2000);//这个线程是父线程，每次都要用Thread表示线程
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
//            System.out.println(Thread.currentThread().getName() + "在运行");
//        }
//    }
// 
//    public static void main(String[] args) {
//        HJ he = new HJ();
//        Thread demo = new Thread(he, "线程");
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
//     * 线程的run方法，它将和其他线程同时运行  
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
//        // 如果不设置daemon，那么线程将输出100后才结束   
//        test.setDaemon(true);   
//        test.start();   
//        System.out.println("isDaemon = " + test.isDaemon());   
//        try {   
//            System.in.read(); // 接受输入，使程序在此停顿，一旦接收到用户输入，main线程结束，守护线程自动结束   .如果我不等他输出完100就输入，进程就马上结束
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
//			System.out.println(Thread.currentThread().getName()+"正在运行！");
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
//    private int ticket = 5;  //5张票
// 
//    public void run() {
//        for (int i=0; i<=20; i++) {
//            if (this.ticket > 0) {
//                System.out.println(Thread.currentThread().getName()+ "正在卖票"+this.ticket--);
//            }
//        }
//    }
//}
//public class TestThread {
//     
//    public static void main(String [] args) {
//        MyThread my = new MyThread();
//        new Thread(my, "1号窗口").start();
//        new Thread(my, "2号窗口").start();
//        new Thread(my, "3号窗口").start();
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
//					System.out.println(Thread.currentThread().getName()+"正在拿票"+count);
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
//	      * 如果改为下面的方式是错的
//	      * 这是多个进程，不能同步
//	      * 进程在执行过程中拥有独立的内存单元，而多个线程共享内存，从而极大地提高了程序的运行效率。
//	      * 进程是具有一定独立功能的程序关于某个数据集合上的一次运行活动,进程是系统进行资源分配和调度的一个独立单位.
//	      */
////	     new Thread(new TestThread(),"A").start();
////	     new Thread(new TestThread(),"B").start();
////	     new Thread(new TestThread(),"C").start();
////	     new Thread(new TestThread(),"D").start();
//	}
//}
/*
 * 设计一个公共类来存储和交换信息
 */
//class Info {
//	private String name="hj";
//	private int age=20;
//	private String [][] input=new String[10][];
//	/*
//	 * 初始化二维数组
//	 * String [][] input = {{},{},{},{},{},{},{}};
//	 */
//	
////	public static void main(String []a ){
////		
////		Info info = new Info();
////		for(int i=0;i<10;i++)
////		{
////			info.input[i]=new String[(int) (Math.random()*100)];
////			System.out.println(info.input[i].length);
////		}
////	}
//	void setName(String name)
//	{
//		this.name=name;
//	}
//	String getName ()
//	{
//		return this.name;
//	}
//	int  getAge()
//	{
//		return this.age;
//	}
//	void setAge(int age)
//	{
//		this.age=age;
//	}
//	static class producer implements Runnable{
//		Info info = new Info();
//		producer (Info info)
//		{
//			this.info=info;
//		}
//		boolean flag=true;
//		public void run(){
//			for(int i=1;i<=20;i++)
//			{
//				if(flag){
//					flag=false;
//					this.info.setAge(100);
//					try{
//	                    Thread.sleep(100);
//	                }catch (Exception e) {
//	                    e.printStackTrace();
//	                }
//					this.info.setName("jy");
//				}else
//				{
//					flag=true;
//					this.info.setAge(20);
//					try{
//	                    Thread.sleep(100);
//	                }catch (Exception e) {
//	                    e.printStackTrace();
//	                }
//					this.info.setName("hj");
//				}
//			}
//		}
//	}
//	static class customer implements Runnable{
//		Info info =new Info ();
//		customer(Info info )
//		{
//			this.info=info;
//		}
//		public void run(){
//			for(int i=1;i<=20;i++)
//			{
//				try{
//                    Thread.sleep(100);
//                }catch (Exception e) {
//                    e.printStackTrace();
//                }
//				System.out.println(this.info.age+"<-->"+this.info.name);
//			}
//		}
//	}
//	
//	public static void main(String [] a)
//	{
//		Info info =new Info();
//		producer p= new producer(info);
//		customer c = new customer(info);
//		new Thread(p).start();
//		new Thread(c).start();
//	}
//}

class Info {
	private String name="";
	private int age=0;
	private boolean flag=true;
	synchronized void set (String name,int age) {
		if (!flag){
			try {
				super.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		this.age=age;
		/*
		 * 这里的休眠是为了让=工作做一半，以后要测试同步就可以用这个方法
		 */
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.name=name;
		flag=false;
		super.notify();
	}
	String getName()
	{
		return this.name;
	}
	int getAge()
	{
		return this.age;
	}
	/*
	 * 这里的同步也是必不可少的，因为虽然只有一个人在生产，但是有可能生产到一半就有人来拿了，
	 * 拿了分别的一半！！
	 */
	synchronized void get() {
		if(flag){
			try {
				super.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(this.getAge()+"<--->"+this.getName());
		flag=true;
		super.notify();
	}
	static class producer implements Runnable{
		Info info=null;
		producer(Info info)
		{
			this.info=info;
		}
		public void run(){
			boolean flag=true;
	
				for(int i=1;i<=100;i++){
					if(flag){
						this.info.set("hj", 100);
						flag=false;
					}else
					{
						this.info.set("jy", 20);
						flag=true;
					}
					
				}	
		}
	}
	static class customer implements Runnable{
		Info info=null;
		customer(Info info)
		{
			this.info=info;
		}
		public void run(){
			for(int i=1;i<=100;i++){	
			this.info.get();
			}
		}
	}
	public static void main(String []a){
		Info t =new Info ();
		producer p= new producer(t);
		customer c= new customer(t);
		new Thread(p,"a").start();
		new Thread(c,"b").start();
	}
}










