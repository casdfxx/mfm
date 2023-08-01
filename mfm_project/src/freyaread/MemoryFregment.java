package freyaread;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;


public class MemoryFregment {
	public boolean isChange=false;
	/**
	 * 记忆碎片的路径
	 */
	public String path;
	/**
	 * 记忆碎片的名称，一般为一个唯一数字字符
	 */
	public String fileName;
	/**
	 * 记忆碎片的信息，以HashSet的方式存储，表示不能重复
	 */
	public Set<String> info;
	/**
	 * 记忆碎片的内容
	 */
	public String content;
	
	private BufferedReader br;
	public MemoryFregment(String path){
		this.path=path;
		File f=new File(path);
		fileName=f.getName();
		f=null;
		info=new HashSet<String>();
		
		try {
			br=new BufferedReader(new FileReader(path));
			//br=Files.newBufferedReader(Paths.get(path));不支持中文
			info=getInfo(path);
			content=getContent(path);
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
		
	};
	private Set<String> getInfo(String path){
		try{
			//1、获取文件的所有头信息,文件头和文件体以一个空行为分隔
			StringBuilder sb=new StringBuilder();
			String content;
			while((content=br.readLine())!=null){
				if(content.length()<=0){
					break;
				}
				info.add(content);
//				sb.append(content);
			}
			//2、将所获得的信息以','分成一个一个的String放入info中
//			content=sb.toString();
//			for(String t:content.split(",")){
//				info.add(t);
//			}
			//3、反回
			return info;
		}catch(IOException e){
			return null;
		}
	}
	private String getContent(String path){
		try{
			StringBuilder sb=new StringBuilder();
			char[] content=new char[1024];
			int len=0;
			while((len=br.read(content))!=-1){
				sb.append(new String(content,0,len));
			}
			return sb.toString();
		}catch(IOException e){
			return null;
		}
	}
	
	/**
	 * 增加记忆描述
	 * @param con String 要增加的内容，先在内存中添加，后调用
	 * MemoryFregmentManagment中的方法保存在磁盘，用','隔开。
	 * @return boolean值， 是否保存成功
	 */
	public boolean insert(String con){
		//1、先检查要插入的内容是否已经存在，若存在则退出
		if(info.contains(con)) return false;
		//2、改变内存，磁盘中的改变留给MemoryFregmentManagment
		isChange=true;
		info.add(con);
		return true;
	}

	/**
	 * 删指定除记忆描述
	 * @param con String 要删除的记忆描述
	 * @return boolean 是否删除成功
	 */
	public boolean delete(String con){
		if(!info.contains(con)) return false;
		isChange=true;
		info.remove(con);
		return true;
	}
	
	/**
	 * 
	 * @param old String 旧值
	 * @param n String 新值
	 * @return boolean 是否成功
	 */
	public boolean updata(String old,String n){
		if(!info.contains(old)) return false;
		isChange=true;
		info.remove(old);
		info.add(n);
		return true;
	}
	
	/**
	 * 删除当前整个记忆碎片
	 * @return 是否成功
	 */
	public boolean drop(){
		try {
			Files.delete(Paths.get(path));
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return false;
		}
	}
	/**
	 * 保存更改
	 * @return 成功 true,失败 false;
	 */
	public boolean save(){
		try {
			Files.delete(Paths.get(path));
			BufferedWriter bw=new BufferedWriter(new FileWriter(path));
					//Files.newBufferedWriter(Paths.get(path));
			for(String str:info){
				bw.write(str);bw.newLine();
			}
			bw.newLine();
			bw.write(content);
			bw.close();
			return true;
		} catch (IOException e) {
			return false;
		}
		
	}
	public String toString(){
		return content;
	}
	public static void main(String[] args) {
		MemoryFregment mf=new MemoryFregment("E:/la.txt");
		for(String t:mf.info){
			System.out.println(t);
		}
		System.out.println(mf.content);
	}
}
