package freyaread;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;


public class MemoryFregment {
	public boolean isChange=false;
	/**
	 * ������Ƭ��·��
	 */
	public String path;
	/**
	 * ������Ƭ�����ƣ�һ��Ϊһ��Ψһ�����ַ�
	 */
	public String fileName;
	/**
	 * ������Ƭ����Ϣ����HashSet�ķ�ʽ�洢����ʾ�����ظ�
	 */
	public Set<String> info;
	/**
	 * ������Ƭ������
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
			//br=Files.newBufferedReader(Paths.get(path));��֧������
			info=getInfo(path);
			content=getContent(path);
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	};
	
	private Set<String> getInfo(String path){
		try{
			//1����ȡ�ļ�������ͷ��Ϣ,�ļ�ͷ���ļ�����һ������Ϊ�ָ�
			StringBuilder sb=new StringBuilder();
			String content;
			while((content=br.readLine())!=null){
				if(content.length()<=0){
					break;
				}
				info.add(content);
//				sb.append(content);
			}
			//2��������õ���Ϣ��','�ֳ�һ��һ����String����info��
//			content=sb.toString();
//			for(String t:content.split(",")){
//				info.add(t);
//			}
			//3������
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
	 * ���Ӽ�������
	 * @param con String Ҫ���ӵ����ݣ������ڴ�����ӣ������
	 * MemoryFregmentManagment�еķ��������ڴ��̣���','������
	 * @return booleanֵ�� �Ƿ񱣴�ɹ�
	 */
	public boolean insert(String con){
		//1���ȼ��Ҫ����������Ƿ��Ѿ����ڣ����������˳�
		if(info.contains(con)) return false;
		//2���ı��ڴ棬�����еĸı�����MemoryFregmentManagment
		isChange=true;
		info.add(con);
		return true;
	}

	/**
	 * ɾָ������������
	 * @param con String Ҫɾ���ļ�������
	 * @return boolean �Ƿ�ɾ���ɹ�
	 */
	public boolean delete(String con){
		if(!info.contains(con)) return false;
		isChange=true;
		info.remove(con);
		return true;
	}
	
	/**
	 * 
	 * @param old String ��ֵ
	 * @param n String ��ֵ
	 * @return boolean �Ƿ�ɹ�
	 */
	public boolean updata(String old,String n){
		if(!info.contains(old)) return false;
		isChange=true;
		info.remove(old);
		info.add(n);
		return true;
	}
	
	/**
	 * ɾ����ǰ����������Ƭ
	 * @return �Ƿ�ɹ�
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
	 * �������
	 * @return �ɹ� true,ʧ�� false;
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
		
		try {
			byte[] temp = content.getBytes("UTF-8");
			String ret  = new String(temp, "UTF-8");
			return ret;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	public static void main(String[] args) {
		MemoryFregment mf=new MemoryFregment("E:/la.txt");
		for(String t:mf.info){
			System.out.println(t);
		}
		System.out.println(mf.content);
	}
}
