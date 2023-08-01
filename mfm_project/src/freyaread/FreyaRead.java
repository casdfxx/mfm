package freyaread;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class FreyaRead {
	String currentPath;
	
	public FreyaRead() throws IOException{
		Path path=Paths.get("e:","新建文本文档.txt");
		List<String> l=Files.readAllLines(path,Charset.forName("UTF8"));
//		while(true){
//			if(l.get(0).length()<=0){
//				l.remove(0);
//				break;
//			}else{
//				l.remove(0);
//				
//			}
//		}
		for(String s:l){
			System.out.println(s);
		}
	}
	public static void main(String[] args) throws IOException {
		new FreyaRead();
	}
}
