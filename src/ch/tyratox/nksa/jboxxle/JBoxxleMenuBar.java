package ch.tyratox.nksa.jboxxle;

import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.net.URI;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import ch.tyratox.nksa.jboxxle.builder.JBoxxleBuilder;

public class JBoxxleMenuBar extends MenuBar{

	/**
	 * 
	 */
	private static final long serialVersionUID = -9198578836516817426L;
	
	private boolean isEclipse = false;

	/**
	 * 
	 */
	
	public JBoxxleMenuBar(final JBoxxle jb){
		//Init
		
		//File
		Menu menuLevel = new Menu("File");
		MenuItem createLevel = new MenuItem("Create your own Level");
		MenuItem eraseData = new MenuItem("Remove Saved Data");
		MenuItem menuLoadIntern = new MenuItem("Load internal Level");
		Menu menuLoad = new Menu("Load external Level");
		MenuItem menuLoadLevel = new MenuItem("Load Single Level");
		MenuItem menuLoadSet = new MenuItem("Load Level Set");
		MenuItem menuImportLevel = new MenuItem("Import David W. Skinner Level Set");
		MenuItem menuExit = new MenuItem("Exit");
		
		//Textures
		Menu menuTextures = new Menu("Textures");
		MenuItem menuLoadTextures = new MenuItem("Load external Texture Pack");
		MenuItem menuLoadInternTextures = new MenuItem("Load internal Texture Pack");
		MenuItem menuResetTextures = new MenuItem("Reset Textures");
		
		//Creating
		
		//File
		this.add(menuLevel);
		menuLevel.add(menuLoadIntern);
		menuLevel.add(menuLoad);
		menuLoad.add(menuLoadLevel);
		menuLoad.add(menuLoadSet);
		menuLoad.add(menuImportLevel);
		menuLevel.add(eraseData);
		menuLevel.add(createLevel);
		menuLevel.add(menuExit);
		
		//Textures
		this.add(menuTextures);
		menuTextures.add(menuLoadInternTextures);
		menuTextures.add(menuLoadTextures);
		menuTextures.add(menuResetTextures);
		
		//Listeners
		
		//File
		
		//Exit
		menuExit.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(JOptionPane.showConfirmDialog(null, "Do you really want to quit?") == JOptionPane.OK_OPTION){
					System.exit(0);
				}
				
			}
		});
		//Load internal Level
		menuLoadIntern.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e1) {
				try{
					if(jb.bc == null && jb.inGame){
						jb.inGame = false;
						previewPanel(jb);
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});
		//Load Level
		menuLoad.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = fc("Choose a Map", "Map", "txt", JFileChooser.FILES_ONLY);
				if(fc.showDialog(null, "Load") == JFileChooser.APPROVE_OPTION){
					File f = fc.getSelectedFile();
					
					jb.loadLevel(f.getAbsolutePath(), "");
				}
			}
		});
		//Load Level Set
		menuLoadSet.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = fc("Choose a Map", "Map", "txt", JFileChooser.FILES_ONLY);
				if(fc.showDialog(null, "Load") == JFileChooser.APPROVE_OPTION){
					File f = fc.getSelectedFile();
					ArrayList<Integer[][]> l = jb.getLevelPack(f.getAbsolutePath());
					jb.centerDatas(l);
					jb.bc.levelname = f.getAbsolutePath().split("/")[f.getAbsolutePath().split("/").length -1].replaceAll(".txt", "");
				}
			}
		});
		//Import Level
		menuImportLevel.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = fc("Choose a Map", "Map", "txt", JFileChooser.FILES_ONLY);
				if(fc.showDialog(null, "Load") == JFileChooser.APPROVE_OPTION){
					File f = fc.getSelectedFile();
					ArrayList<String> list = new ArrayList<String>();
					list.add("");
					list.add("#");
					list.add(" ");
					list.add(".");
					list.add("$");
					list.add("*");
					list.add("@");
					list.add("");
					
					ArrayList<Integer[][]> l = jb.getSkinnerLevelPack(f.getAbsolutePath(), list);
					jb.centerDatas(l);
				}
			}
		});
		
		//Erase Data
		
		eraseData.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				File f = new File("." + JBoxxle.username + ".save");
				f.delete();
				f = new File(".user.name");
				f.delete();
				try {
					JBoxxle.deleteFTPFile("server.tyratox.ch", "jboxxle", "jboxxle", "." + JBoxxle.username + ".save",JBoxxle.username + ".save", "/saves/");
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				
			}
		});
		
		//Launch JBoxxle Builder
		
		createLevel.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JFrame s = new JBoxxleBuilder();
				s.setVisible(true);
				
			}
		});
		
		//Textures
		
		//Load Textures
		menuLoadTextures.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = fc("Choose the Texture Folder", "", "", JFileChooser.DIRECTORIES_ONLY);
				if(fc.showDialog(null, "Load") == JFileChooser.APPROVE_OPTION){
					jb.loadImages(fc.getCurrentDirectory().toString());
				}
			}
		});
		menuLoadInternTextures.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					if(jb.inGame){
						jb.inGame = false;
						previewTexture(jb);
					}else{
						JOptionPane.showMessageDialog(jb, "Please load a level first!");
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		
		//Reset Textures
		menuResetTextures.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				jb.loadImages();
				jb.bc.repaint();
				
			}
		});
		
		
	}
	public JFileChooser fc(String title, final String fileType, final String fileExt, int chooseMode){
		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(chooseMode);
		fc.setDialogTitle(title +  " [." + fileExt + "]");
		if(fc.getFileSelectionMode() == JFileChooser.FILES_ONLY){
		fc.setFileFilter(new FileFilter() {
			
			@Override
			public String getDescription() {
				 return fileType + " (*." + fileExt + ")";
			}
			
			@Override
			public boolean accept(File f) {
				 return f.isDirectory() || f.getName().toLowerCase().endsWith("." + fileExt);
			}
		});
		}
		return fc;
	}
	public void previewPanel(JBoxxle jb) throws Exception{
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
			JBoxxlePreview prev = new JBoxxlePreview(jb, list, isEclipse);
		    jb.add(prev);
		    jb.prev = prev;
		    if(jb.bc != null){
		    	jb.remove(jb.bc);
		    }
		}
	}
	public void previewTexture(final JBoxxle jb) throws Exception{
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
				File[] li = new File(pa + "images").listFiles();
				for(int i = 0;i<li.length;i++){
					if(li[i].getName().endsWith(".png") == false){
						list.add(li[i].getAbsolutePath());
						System.out.println("Available Texture Pack: " + li[i]);
					}
				}
			}else{
				if(path != null) {
				    ZipInputStream zip = new ZipInputStream(new FileInputStream(new File(path)));
				    ZipEntry ze = null;

				    while((ze = zip.getNextEntry()) != null){
				        String entryName = ze.getName();
				        if( entryName.startsWith("images") &&  entryName.endsWith(".png") == false) {
				            list.add(entryName);
				            System.out.println("Available Texture Pack: " + entryName);
				        }
				    }
				    zip.close();
				 }
			}

			jb.add(new JBoxxlePreviewTextures(jb, list, jb.bc.data, isEclipse));
			jb.remove(jb.bc);
		}
	}

}
