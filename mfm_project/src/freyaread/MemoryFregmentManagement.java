package freyaread;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class MemoryFregmentManagement {
	public File filePath;
	public Map<Set<String>,MemoryFregment> map=new HashMap<>();
	
	public MemoryFregmentManagement(String Path){
		filePath=new File(Path);
		load();
	}
	public MemoryFregmentManagement(File filePath){
		filePath=this.filePath;
		load();
	}
	/**
	 * 加载文件库到map
	 * @return 成功 true,失败 false;
	 */
	public boolean load(){
		try{
			map.clear();
			for(File file:filePath.listFiles(new MyFileFilter())){
				MemoryFregment mf=new MemoryFregment(file.toString());
				map.put(mf.info, mf);
			}
			return true;
		}catch(Exception e){
			return false;
		}
		
	}
	private class MyFileFilter implements FilenameFilter{

		@Override
		public boolean accept(File dir, String name) {
			// TODO Auto-generated method stub
			if(name.endsWith(".txt")) return true;
			else	return false;
		}

	}
	/**
	 * 根据str（info）查找map中哪个MemoryFregment与之相似
	 * @param str
	 * @return 找到则返回MemoryFregment，找不到则返回null
	 */
	public List<MemoryFregment> select(String str){
		List<MemoryFregment> mf=new ArrayList<>();
		for(Set<String> e:map.keySet()){
			if(e.contains(str)){
				mf.add(map.get(e)) ;
			}
		};
		return mf;
	}
	public List<MemoryFregment> select(String[] array){
		List<MemoryFregment> mf=new ArrayList<>();
		int size=array.length;
		map.keySet().forEach(e->{
			//mf.add(map.get(e));
			int mingzhong=0;
			for(String aim:array){
				if(e.contains(aim)){
					mingzhong++;
			//		mf.remove(map.get(e));
				}
			}
			if(mingzhong==array.length){
				mf.add(map.get(e));
			}
		});
		return mf;
	}
	
	/**
	 * 遍历map里所有的MemoryFregment，若其中的change==true时，则修改。
	 * @return
	 */
	public boolean saveAll(){
		map.keySet().forEach(e->{ 
			MemoryFregment mf=map.get(e);
			mf.save();
		});
		return false;
	}
	public static void main(String[] args) {
		Clipboard cb=Toolkit.getDefaultToolkit().getSystemClipboard();
		
		
		MemoryFregmentManagement mfm=new MemoryFregmentManagement("H:/MFM");
		Scanner scan=new Scanner(System.in);
		while(true){
			try{
				String content=scan.nextLine();
				String c[]=content.split(" ");
				System.out.println("数量： mfm.select(c): "+mfm.select(c).size());
				StringSelection ss=new StringSelection(mfm.select(c).get(0).content);
				cb.setContents(ss, null);
			}catch(Exception e){
				continue;
			}
		}
//		System.out.println(mfm.select("范仲淹").get(0).content);
	}
}
