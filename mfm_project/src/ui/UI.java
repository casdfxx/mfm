package ui;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import freyaread.MemoryFregment;
import freyaread.MemoryFregmentManagement;

public class UI {
	
	
	String savePath;
	int fileCount;
	JFrame f=new JFrame();
	JPanel jp=new JPanel();
	JPanel jp_north=new JPanel(new GridLayout(2,1)); 
	JTextField jtf=new JTextField(20);
	JButton jb_new=new JButton("new");
	MemoryFregmentManagement mfm;
	DefaultListModel l=new DefaultListModel();//new MyDL(null);//new DefaultListModel();
	JList jlist=new JList(l);
	
	public class MyDL extends DefaultListModel{
		public List<MemoryFregment> mf;
		public MyDL(List<MemoryFregment> mf){
			this.mf=mf;
		}
		
		public int getSize(){
			return this.size();
		}
		public Object getElementAt(int index){
			return mf.get(index).content;
		}
	}
	
	public UI(){
		f.setTitle("mfm V1.4");
		try {
			String strCount = cmdExec("echo %mfm_count%");
			fileCount=Integer.parseInt(strCount);
			savePath=cmdExec("echo %mfm%");
			
			File check_f = new File(savePath);
			if(!check_f.isDirectory()) {
				init();
			}
		}catch(Exception e) {
			init();
			
			return;
		}
		
		
		mfm=new MemoryFregmentManagement(savePath);
		f.add(jp);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jp.setLayout(new BorderLayout());
		jp.add(jtf, BorderLayout.NORTH);
		JScrollPane jsp=new JScrollPane(jlist);
		
		jp.add(jsp, BorderLayout.CENTER);
		
		
		jtf.addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent k){
				if(k.getKeyCode()==k.VK_ENTER){
					if(jtf.getText().equals("#new#")){
						fileCount++;
						String fileName=cmdExec("echo %mfm%/"+fileCount+".txt");
						System.out.println(fileName);
						File f=new File(fileName);
						if(!f.exists())
							try {
								f.createNewFile();
							} catch (IOException e) {
								e.printStackTrace();
							}
						cmdExec("start "+fileName);
						cmdExec("setx mfm_count \""+fileCount+"\"");
					}
					if(jtf.getText().equals("#directory#")){
						cmdExec("start %mfm%");
					}
					
					l.removeAllElements();
					String[] c=jtf.getText().split(" ");
					List<MemoryFregment> mf=mfm.select(c);
//					if(mf.size()>=1){
//						Clipboard cp=Toolkit.getDefaultToolkit().getSystemClipboard();
//						StringSelection ss=new StringSelection(mf.get(0).content);
//						cp.setContents(ss, null);
//					}
					for(int i=0;i<mf.size();i++){
						l.addElement(mf.get(i));
					}
					jlist.setModel(l);
				}
			}
		});
		jlist.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				if(e.getClickCount()==3&&e.getButton()==e.BUTTON3){
					System.out.println("mutton3");
					String f=((MemoryFregment)jlist.getSelectedValue()).path;
					try {
						Files.delete(Paths.get(f));
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					l.removeElement(jlist.getSelectedValue());
					mfm.load();
					//mfm.map.remove(jlist.getSelectedValue());
					
				}else if(e.getClickCount()==1&&e.getButton()==e.BUTTON1){
					Clipboard cp=Toolkit.getDefaultToolkit().getSystemClipboard();
					StringSelection ss=new StringSelection((String)jlist.getSelectedValue().toString());
					cp.setContents(ss, null);
				}else if(e.getClickCount()==2&&e.getButton()==e.BUTTON1){
					//cmd_exec("start "+((MemoryFregment)jlist.getSelectedValue()).path);
					System.out.println(cmdExec("start "+((MemoryFregment)jlist.getSelectedValue()).path));
				}
				
			}
		});
		f.pack();
		f.setVisible(true);
		
		FileChangeListener();
	}
	public String cmdExec(String cmd){
		InputStreamReader isr;
		String result="";
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
		if(result.endsWith("\r\n")){
			int len=result.length();
			result=result.substring(0, len-2);
		}
		
		return result;//ȥ����β\r\n������integerת��ʱ���ִ���
	}
	public void FileChangeListener(){
		WatchService watcher;
		try {
			watcher = FileSystems.getDefault().newWatchService();
			Paths.get(savePath).register(watcher, StandardWatchEventKinds.ENTRY_CREATE,StandardWatchEventKinds.ENTRY_DELETE,StandardWatchEventKinds.ENTRY_MODIFY);
			while(true){
				WatchKey key=watcher.take();
				mfm.load();
				
				for(WatchEvent<?> event:key.pollEvents()){
//					System.out.println(event.context()+"�ļ�������"+event.kind()+"�¼�");
					//�Լ��Ĵ��롣
				}
				if(!key.reset()){
					break;
				}
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
//			e1.printStackTrace();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
//			e1.printStackTrace();
		}
	}
	public void init(){
		
		JFileChooser jfc		= new JFileChooser();
		
		jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		jfc.showDialog(null, "初次使用，请选择保存目录");
		File file				= jfc.getSelectedFile();
		
		int maxNum = 0;
		if(null!=file) {
			System.out.println(file);
			for(String str:file.list()) {
				System.out.println(str);
				String[] strs = str.split("\\.");
				String numStr  = strs[0];
				int T_num = Integer.parseInt(numStr);
				maxNum = maxNum>T_num?maxNum:T_num;
			}
			cmdExec("setx mfm \""+file+"\"");
			cmdExec("setx mfm_count \""+ maxNum+ "\"");
			
			JOptionPane.showMessageDialog(f, "配置完成，重启后生效。");
			f.dispose();
		}
		
		return;
	}
	public static void main(String[] args) {
		new UI();
//		JFrame jf    = new JFrame();
//		jf.setSize(100,100);
//		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		
//		//JDialog jd   = new JDialog(jf, "asdf", true);
////		jf.add(jd);
//		jf.setVisible(true);
//		//jd.show();
		
		
	}
}
