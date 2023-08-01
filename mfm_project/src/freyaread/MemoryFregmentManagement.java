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
	 * �����ļ��⵽map
	 * @return �ɹ� true,ʧ�� false;
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
	 * ����str��info������map���ĸ�MemoryFregment��֮����
	 * @param str
	 * @return �ҵ��򷵻�MemoryFregment���Ҳ����򷵻�null
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
	 * ����map�����е�MemoryFregment�������е�change==trueʱ�����޸ġ�
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
				System.out.println("������ mfm.select(c): "+mfm.select(c).size());
				StringSelection ss=new StringSelection(mfm.select(c).get(0).content);
				cb.setContents(ss, null);
			}catch(Exception e){
				continue;
			}
		}
//		System.out.println(mfm.select("������").get(0).content);
	}
}
