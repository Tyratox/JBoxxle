package ch.tyratox.nksa.jboxxle;

import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URI;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

import javazoom.jl.player.Player;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

public class JBoxxle extends Frame{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -105248398166837957L;
	
	public static String username = "";
	public static final String PW = "OMG SUCH SECURE!";
	
	private static String OS = System.getProperty("os.name").toLowerCase();
	
	Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
	public Image[] images;
	JBoxxlePanel bc;
	public JBoxxleMenuBar jbm = new JBoxxleMenuBar(this);
	private static String ls = System.getProperty("line.separator");
	public boolean inGame = true;
	
	public ArrayList<String> levelsDone = new ArrayList<String>();
	
	//Music Player
	static Player player;
	static InputStream fis;
	static BufferedInputStream bis;
	
	//Preview Panels
	public JBoxxlePreview prev = null;
	public JBoxxlePreviewTextures prevt = null;
	
	//To Remove
	public Button[] btns = null;
	
	
	public static void main(String[] args) {
		getUserName();
		
		//Load Music File
		new Thread(){
			public void run(){
				try{
					 fis = getClass().getClassLoader().getResourceAsStream("sounds/main.mp3");
					 bis = new BufferedInputStream(fis);
					 player = new Player(bis);
					while(true){
						 player.play();
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}.start();
		
		new JBoxxle();
	}
	
	public JBoxxle(){
		getSolvedLevels();
		//Load Images
		loadImages();
		setMenuBar(jbm);
		setResizable(false);
		setSize(640, 660);
		setTitle("JBoxxel");
		setBackground(Color.black);
		setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
		setVisible(true);
		
		//Close On Exit
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e){
				System.exit(0);
			}
		});
		
		try {
			if(inGame){
				inGame = false;
				jbm.previewPanel(this);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
	}
	
	public void loadPanel(){
		this.bc = new JBoxxlePanel(this);
		bc.setLocation(20, 40);
		this.add(bc);
	}
	
	public void loadImages(){
		try{
			images = new Image[9];
					for(int i = 0;i<=8;i++){
						images[i] = ImageIO.read(getClass().getResourceAsStream("/images/main/" + i + ".png"));
					}

		}catch(IOException e){
			e.printStackTrace();
		}
	}
	public void loadImages(String path){
		try{
			images = new Image[9];
					for(int i = 0;i<=8;i++){
						System.out.println((path + "/" + i + ".png"));
						images[i] = ImageIO.read(new File(path + "/" + i + ".png"));
					}

		}catch(IOException e){
			e.printStackTrace();
		}
		bc.iscache = true;
		bc.repaint();
	}
	public void loadInternalImages(String path){
		try{
			images = new Image[9];
					for(int i = 0;i<=8;i++){
						images[i] = ImageIO.read(getClass().getResourceAsStream("/" + path + i + ".png"));
					}

		}catch(IOException e){
			e.printStackTrace();
		}
		bc.iscache = true;
		bc.repaint();
	}
	public int[][] loadLevel(String name, String lname){
		loadPanel();
		boolean err = false;
		int[][] data = new int[20][20];
		String line;
		int len = 0, lines = 0;
		try{
			FileReader fr = new FileReader(new File(name));
			BufferedReader in = new BufferedReader(fr);
			while((line = in.readLine()) != null){
				char[] chars = line.toCharArray();
				for(int i = 0; i<chars.length;i++){
					data[i][lines] = Integer.parseInt(Character.toString(chars[i]));
					len = Math.max(len, i);
				}
				lines++;
			}
			in.close();
			
		}catch(Exception e){
			JOptionPane.showMessageDialog(bc, "Not a valid Map");
			err = true;
		}
		if(err == false){
//			System.out.println(len);
//			System.out.println(lines);
			bc.levelname = lname;
			if((lines != 20 && len != 20) && (lines != 19 && len != 19)){
				centerData(data, len, lines);
			}else{
				bc.setData(data);
			}
		}
		return data;
	}
	public void loadInternalLevel(String name, String lname){
		if(name.charAt(0)+"" != "/"){
			name = "/"+name;
		}
		System.out.println("Loading: " + name);
		loadPanel();
		boolean err = false;
		int[][] data = new int[20][20];
		String line;
		int len = 0, lines = 0;
		try{
			InputStream fr = getClass().getResourceAsStream(name);
			BufferedReader in = new BufferedReader(new InputStreamReader(fr));
			while((line = in.readLine()) != null){
				char[] chars = line.toCharArray();
				for(int i = 0; i<chars.length;i++){
					data[i][lines] = Integer.parseInt(Character.toString(chars[i]));
					len = Math.max(len, i);
				}
				lines++;
			}
			in.close();
			
		}catch(Exception e){
			JOptionPane.showMessageDialog(bc, "Not a valid Map");
			e.printStackTrace();
			err = true;
		}
		if(err == false){
//			System.out.println(len);
//			System.out.println(lines);
			bc.levelname = lname;
			if((lines != 20 && len != 20) && (lines != 19 && len != 19)){
				centerData(data, len, lines);
			}else{
				bc.setData(data);
			}
		}
	}
	public void loadLevel(String name, ArrayList<String> replacement){
		loadPanel();
		boolean err = false;
		int[][] data = new int[20][20];
		String line;
		int len = 0, lines = 0;
		try{
			FileReader fr = new FileReader(new File(name));
			BufferedReader in = new BufferedReader(fr);
			while((line = in.readLine()) != null){
				char[] chars = line.toCharArray();
				for(int i = 0; i<chars.length;i++){
					String cc = Character.toString(chars[i]);
					int c = replacement.indexOf(cc);
					data[i][lines] = c;
					len = Math.max(len, i);
				}
				lines++;
			}
			in.close();
			
		}catch(Exception e){
			e.printStackTrace();
			JOptionPane.showMessageDialog(bc, "Not a valid Map");
			err = true;
		}
		if(err == false){
			if((lines != 20 && len != 20) && (lines != 19 && len != 19)){
				centerData(data, len, lines);
			}else{
				bc.setData(data);
			}
		}
	}
	public int[][] getLevel(String name){
		System.out.println("Loading: " + name);
		boolean err = false;
		int[][] data = new int[20][20];
		String line;
		int len = 0, lines = 0;
		try{
			FileReader fr = new FileReader(new File(name));
			BufferedReader in = new BufferedReader(fr);
			while((line = in.readLine()) != null){
				char[] chars = line.toCharArray();
				for(int i = 0; i<chars.length;i++){
					data[i][lines] = Integer.parseInt(Character.toString(chars[i]));
					len = Math.max(len, i);
				}
				lines++;
			}
			in.close();
			
		}catch(Exception e){
			JOptionPane.showMessageDialog(this, "Not a valid Map");
			e.printStackTrace();
			err = true;
		}
		if(err == false){
//			System.out.println(len);
//			System.out.println(lines);
			if((lines != 20 && len != 20) && (lines != 19 && len != 19)){
				return getCData(data, len, lines);
			}else{
				return data;
			}
		}
		return data;
	}
	public int[][] getInternalLevel(String name){
		if(name.charAt(0)+"" != "/"){
			name = "/"+name;
		}
		System.out.println("Loading: " + name);
		boolean err = false;
		int[][] data = new int[20][20];
		String line;
		int len = 0, lines = 0;
		try{
			InputStream fr = getClass().getResourceAsStream(name);
			BufferedReader in = new BufferedReader(new InputStreamReader(fr));
			while((line = in.readLine()) != null){
				char[] chars = line.toCharArray();
				for(int i = 0; i<chars.length;i++){
					data[i][lines] = Integer.parseInt(Character.toString(chars[i]));
					len = Math.max(len, i);
				}
				lines++;
			}
			in.close();
			
		}catch(Exception e){
			JOptionPane.showMessageDialog(this, "Not a valid internal Map");
			e.printStackTrace();
			err = true;
		}
		if(err == false){
//			System.out.println(len);
//			System.out.println(lines);
			if((lines != 20 && len != 20) && (lines != 19 && len != 19)){
				return getCData(data, len, lines);
			}else{
				return data;
			}
		}
		return data;
	}
	public ArrayList<Integer[][]> getLevelPack(String name){
		ArrayList<Integer[][]> list = new ArrayList<Integer[][]>();
		System.out.println("Loading: " + name);
		int[][] data = new int[20][20];
		String line;
//		int levelC = -1;
		boolean rLevel = false;
		int len = 0, lines = 0;
		try{
			FileReader fr = new FileReader(new File(name));
			BufferedReader in = new BufferedReader(fr);
			while((line = in.readLine()) != null){
				if(line.equalsIgnoreCase(ls) || line.equalsIgnoreCase(" ") || line.equalsIgnoreCase("	")){
					line = "";
				}else{
					char[] chars = line.toCharArray();
//					System.out.println("Chars: " + chars.length);
					for(int i = 0; i<chars.length;i++){
						if((chars[i]+"").equalsIgnoreCase(";")){
							lines = 0;
							len = 0;
							line = "";
							i = chars.length;
//							levelC++;
							if(rLevel){
								System.out.println("Saved New Level: " + i);
								list.add(intAToIntegerA(data));
							}
							rLevel = true;
						}else if(rLevel == true){
							String cc = Character.toString(chars[i]);
							int c = Integer.parseInt(cc);
							if(c == -1){
								System.out.println("OMG;" + cc);
							}
							data[i][lines] = c;
//							System.out.println("Saved " + c + " to " + i + " ; " + lines);
							len = Math.max(len, i);
						}
					}
				}
				lines++;
			}
			if(rLevel){
				System.out.println("Saved New Level: " + list.size());
				list.add(intAToIntegerA(data));
			}
			in.close();
			
		}catch(Exception e){
			JOptionPane.showMessageDialog(this, "Not a valid Map");
			e.printStackTrace();
		}
		return list;
	}
	public ArrayList<Integer[][]> getSkinnerLevelPack(String name, ArrayList<String> replacement){
		ArrayList<Integer[][]> list = new ArrayList<Integer[][]>();
		System.out.println("Loading: " + name);
		int[][] data = new int[20][20];
		String line;
//		int levelC = -1;
		boolean rLevel = false;
		int len = 0, lines = 0;
		try{
			FileReader fr = new FileReader(new File(name));
			BufferedReader in = new BufferedReader(fr);
			boolean error = false;
			while((line = in.readLine()) != null){
				if(line.equalsIgnoreCase(ls) || line.equalsIgnoreCase(" ") || line.equalsIgnoreCase("	")){
					line = "";
				}else{
					char[] chars = line.toCharArray();
//					System.out.println("Chars: " + chars.length);
					for(int i = 0; i<chars.length;i++){
						if((chars[i]+"").equalsIgnoreCase(";")){
							lines = 0;
							len = 0;
							line = "";
							i = chars.length;
//							levelC++;
							if(rLevel){
								System.out.println("Saved New Level:" + list.size());
								list.add(intAToIntegerA(data));
							}
							rLevel = true;
						}else if(rLevel == true){
							String cc = Character.toString(chars[i]);
							int c = replacement.indexOf(cc);
							if(c == -1){
								System.out.println("OMG;" + cc);
							}
							try{
							data[i][lines] = c;
							}catch(ArrayIndexOutOfBoundsException e){
								error = true;
							}
//							System.out.println("Saved " + c + " to " + i + " ; " + lines);
							len = Math.max(len, i);
						}
					}
				}
				lines++;
			}
			if(rLevel && error == false){
				System.out.println("Saved New Level: " + list.size());
				list.add(intAToIntegerA(data));
			}
			in.close();
			
		}catch(Exception e){
			JOptionPane.showMessageDialog(this, "Not a valid Map");
			e.printStackTrace();
		}
		return list;
	}
	
	public void centerData(int[][] data, int w, int h){
		//black squares horizontal counter
		int len = 0;
		int len_counter = 0;
		int len_zero = 0;
		
		int hei = 0;
		int hei_counter = 0;
		int hei_zero = 0;
		int[][] cache = data;
		for(int i = 0;i<20;i++){
			for(int j = 0;j<20;j++){
				if(data[i][j] == 0){
					if(i == len_counter){
						len++;
					}
				}
				if(data[j][i] == 0){
					if(i == hei_counter){
						hei++;
					}
				}
			}
			hei_counter++;
			if(hei_zero==0||hei_zero>hei){
				hei_zero=hei;
			}
			hei=0;
			
			len_counter++;
			if(len_zero==0||len_zero>len){
				len_zero=len;
			}
			len=0;
		}
//		System.out.println(hei_zero + " black squares horizontal");
//		System.out.println(len_zero + " black squares vertical");
		
		for(int i = 19;i>-1;i--){
			for(int j = 19;j>-1;j--){
				if(data[i][j] != 0){
//					System.out.println("Feld " + i + " | " + j + " ist nicht null!");
					cache[i+ (hei_zero/2)][j + (len_zero/2)] = data[i][j];
//					System.out.println("Feld " + (i+ (c/2)) + " | " + (j + (q/2)) + " ist jetzt " + data[i][j] + " !");
					cache[i][j] = 0;
//					System.out.println("Feld " + i + " | " + j + " ist jetzt null!");
				}else{
					//Test
//					cache[i][j] = 4;
				}
			}
		}
		if(prev != null){
			System.out.println("Removing Preview");
			if(btns != null){
				System.out.println("Removing Buttons");
				for(int i = 0;i<btns.length;i++){
					if(i<3){
						remove(btns[i]);
					}else{
						bc.remove(btns[i]);//TODO
					}
				}
				btns = null;
			}
			remove(prev);
		}
		
		if(bc == null){
			loadPanel();
			System.out.println("Created new Panel");
		}
		bc.setData(cache);
	}
	public void centerDatas(ArrayList<Integer[][]> list){
		for(int l = 0;l<list.size();l++){
			int[][] data = getCData(integerATointA(list.get(l)), 0, 0);
			list.set(l, intAToIntegerA(data));
		}
		if(bc == null){
			loadPanel();
		}
		if(prev != null){
			System.out.println("Removing Preview");
			if(btns != null){
				System.out.println("Removing Buttons");
				for(int i = 0;i<btns.length;i++){
					if(i<3){
						remove(btns[i]);
					}else{
						bc.remove(btns[i]);
					}
				}
				btns = null;
			}
			remove(prev);
		}
		bc.setDatas(list);
	}
	private int[][] getCData(int[][] data, int w, int h){
		//black squares horizontal counter
		int len = 0;
		int len_counter = 0;
		int len_zero = 0;
		
		int hei = 0;
		int hei_counter = 0;
		int hei_zero = 0;
		int[][] cache = data;
		for(int i = 0;i<20;i++){
			for(int j = 0;j<20;j++){
				if(data[i][j] == 0){
					if(i == len_counter){
						len++;
					}
				}
				if(data[j][i] == 0){
					if(i == hei_counter){
						hei++;
					}
				}
			}
			hei_counter++;
			if(hei_zero==0||hei_zero>hei){
				hei_zero=hei;
			}
			hei=0;
			
			len_counter++;
			if(len_zero==0||len_zero>len){
				len_zero=len;
			}
			len=0;
		}
//		System.out.println(hei_zero + " black squares horizontal");
//		System.out.println(len_zero + " black squares vertical");
		
		for(int i = 19;i>-1;i--){
			for(int j = 19;j>-1;j--){
				if(data[i][j] != 0){
//					System.out.println("Feld " + i + " | " + j + " ist nicht null!");
					cache[i+ (hei_zero/2)][j + (len_zero/2)] = data[i][j];
//					System.out.println("Feld " + (i+ (c/2)) + " | " + (j + (q/2)) + " ist jetzt " + data[i][j] + " !");
					cache[i][j] = 0;
//					System.out.println("Feld " + i + " | " + j + " ist jetzt null!");
				}else{
					//Test
//					cache[i][j] = 4;
				}
			}
		}
		
		
		return cache;
	}

	public int[][] integerATointA(Integer[][] data){
		
		int[][] save = new int[20][20];
		
		for(int i = 0; i<20;i++){
			for(int j = 0;j<20;j++){
				save[i][j] = data[i][j].intValue();
			}
		}
		return save;
	}
	
	public Integer[][] intAToIntegerA(int[][] data){
		Integer[][] save = new Integer[20][20];
		
		for(int i = 0; i<20;i++){
			for(int j = 0;j<20;j++){
				save[i][j] = Integer.valueOf(data[i][j]);
			}
		}
		return save;
	}
	public ArrayList<String> getAllInternalLevels() throws Exception{
		boolean isEclipse;
		String path = System.getProperty("java.class.path");
		ArrayList<String> list = new ArrayList<String>();
		CodeSource src = JBoxxle.class.getProtectionDomain().getCodeSource();
		URI ja = src.getLocation().toURI();
		String pa = ja.toString().replace("file:", "");
		if(src != null){
			System.out.println("Try to read from " + pa);
			if(pa.contains("rsrc")==false){
				//Is Eclipse
				isEclipse = true;
				System.out.println("isEclipse = " + isEclipse);
				File[] li = new File(pa + "maps").listFiles();
				for(int i = 0;i<li.length;i++){
					if(li[i].getName().endsWith(".txt")){
						list.add(li[i].getAbsolutePath());
						System.out.println("Available Map: " + li[i]);
					}
				}
			}else{
				if(path != null) {
				    ZipInputStream zip = new ZipInputStream(new FileInputStream(new File(path)));
				    ZipEntry ze = null;

				    while((ze = zip.getNextEntry()) != null){
				        String entryName = ze.getName();
				        if( entryName.startsWith("maps") &&  entryName.endsWith(".txt") ) {
				            list.add(entryName);
				            System.out.println("Available Map: " + entryName);
				        }
				    }
				    zip.close();
				 }
			}
		}
		return list;
	}
	
	
	public static void getUserName(){
		if(new File(".user.name").exists()){
			try {
				InputStream is = new FileInputStream(new File(".user.name"));
				BufferedReader br = new BufferedReader(new InputStreamReader(is));
				try {
					username = br.readLine();
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}else{
			username = JOptionPane.showInputDialog(null, "Please enter your Username");
			if(username != "" && username != null && username.replaceAll(" ", "") != ""){
				InputStream is = new ByteArrayInputStream(username.getBytes());
				File f = new File(".user.name");
				try {
					OutputStream os = new FileOutputStream(f);
					byte[] buffer = new byte[1024];
		            int bytesRead;
		            //read from is to buffer
		            while((bytesRead = is.read(buffer)) !=-1){
		                os.write(buffer, 0, bytesRead);
		            }
		            is.close();
		            //flush OutputStream to write any buffered data to file
		            os.flush();
		            os.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else{
				System.exit(0);
			}
		}
	}
	
	public void saveDoneLevels(){
		System.out.println("Saving solved levels to file...");
    	File f = new File("." + username + ".save");
    	String c = "";
    	for(int i = 0;i<levelsDone.size();i++){
    		System.out.println("Level Done: "+levelsDone.get(i));
    		if(c == ""){
    			c = levelsDone.get(i);
    		}else{
    			c = c + ls + levelsDone.get(i);
    		}
    	}
    	InputStream is = null;
		try {
			is = new ByteArrayInputStream(JBoxxleCrypt.encrypt(c, PW).getBytes());
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	try {
			OutputStream os = new FileOutputStream(f);
			byte[] buffer = new byte[1024];
            int bytesRead;
            //read from is to buffer
            while((bytesRead = is.read(buffer)) !=-1){
                os.write(buffer, 0, bytesRead);
            }
            is.close();
            //flush OutputStream to write any buffered data to file
            os.flush();
            os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void getSolvedLevels(){
		//Read solved levels
				try {
					if(new File("." + username + ".save").exists()){
						InputStream is = new FileInputStream(new File("." + username + ".save"));
						BufferedReader bf = new BufferedReader(new InputStreamReader(is));
						String line = "";
						String everything = "";
						while((line = bf.readLine()) != null){
							if(everything == ""){
								everything = line;
							}else{
								everything = everything + line;
							}
						}
						bf.close();
						String[] ss = JBoxxleCrypt.decrypt(everything, PW).split(ls);
						for(int i = 0;i<ss.length;i++){
							levelsDone.add(ss[i]);
							System.out.println("Solved Level: " + ss[i]);
						}
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
	}
	
	public static boolean isWindows() {
		 
		return (OS.indexOf("win") >= 0);
 
	}
 
	public static boolean isMac() {
 
		return (OS.indexOf("mac") >= 0);
 
	}
 
	public static boolean isUnix() {
 
		return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0 );
 
	}

}
