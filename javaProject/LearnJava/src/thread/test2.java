package thread;

public class test2 {
	public enum simpleEnum { not, hot, hj,jy,zheying}
	
	public static void main(String []args){
		for(simpleEnum s: simpleEnum.values()){
			System.out.println(s);
		}
	}

}
