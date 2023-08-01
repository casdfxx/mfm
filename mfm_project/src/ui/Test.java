package ui;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class Test {

public String cmdExec(String cmd){
		InputStreamReader isr;
		String result=new String();
		cmd="cmd /c "+cmd;
		String s[]=cmd.split(" ");
		try {
			Process p=Runtime.getRuntime().exec(s);
			isr=new InputStreamReader(p.getInputStream());
			int content=0;
			while((content=isr.read())!=-1){
				result+=((char)content)+"";
			}
			isr.close();
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return result;
	}
	public static void main(String[] args) {
		Test t=new Test();
//		String content=t.cmdExec("setx mfm_count \"109\"");
		Scanner scan=new Scanner(System.in);
			String con="";
		while((con=scan.nextLine())!=null){
			String content=t.cmdExec(con);
			System.out.println(content);
		}
			
//			content=t.cmdExec(scan.nextLine());
//			System.out.println(content);
//			
//			content=t.cmdExec(scan.nextLine());
//			System.out.println(content);
			
		
	}
}
