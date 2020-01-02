package ch.tyratox.nksa.jboxxle.builder;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class JBoxxleBuilderPanel extends JPanel implements KeyListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1809044467251540250L;
	static String ls = System.getProperty("line.separator");
	private boolean startup = true;
	
	private JBoxxleBuilder jbb;

	public JBoxxleBuilderPanel(JBoxxleBuilder jbb){
		addKeyListener(this);
		this.jbb = jbb;
		setLayout(null);
		setSize(600, 600);
		setBackground(Color.LIGHT_GRAY);

		
	}
	
	public void paint(Graphics g){
		this.requestFocus();
		for(int i = 0;i<20;i++){
			for(int j = 0;j<20;j++){
				if(startup){
					g.drawImage(jbb.images[0], i*30, j*30, this);
					jbb.data[i][j] = 0;
				}else{
					g.drawImage(jbb.images[jbb.data[i][j]], i*30, j*30, this);
				}
			}
		}
		startup = false;
	}
	
	public void changeImage(JButton btn){
		double x = btn.getBounds().getX();
		double y = btn.getBounds().getY();
		
		int d = jbb.data[(int) (x/30)][(int) (y/30)];
		
		int set = 0;
		
		if(d == 0){
			set = 1;
		}else if(d == 1){
			set = 2;
		}else if(d == 2){
			set = 3;
		}else if(d == 3){
			set = 4;
		}else if(d == 4){
			set = 5;
		}else if(d == 5){
			set = 6;
		}else if(d == 6){
			set = 7;
		}else if(d == 7){
			set = 0;
		}
		
		jbb.data[(int) (x/30)][(int) (y/30)] = set;
		
		repaint();
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.isControlDown()){
			if(e.getKeyCode() == 83){
				JFileChooser fc = new JFileChooser();
				fc.setDialogTitle("Where do you want to save the file?");
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				if(fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION){
					String path = fc.getSelectedFile().getAbsolutePath() + "/map.txt";
					File file = new File(path);
					String code = "";
					for(int i = 0;i<20;i++){
						for(int j = 0;j<20;j++){
							code = code + jbb.data[j][i];
						}
						code = code + ls;
					}
					try {
						if (!file.exists()) {
							InputStream fstream = new ByteArrayInputStream(code.getBytes());
							BufferedReader in = new BufferedReader(new InputStreamReader(fstream));
							String line = "";
							String c = "";
							while((line = in.readLine()) != null){
								if(line.contains("1") || line.contains("2") || line.contains("3") || line.contains("4") || line.contains("5") || line.contains("6") || line.contains("7")){
									if(c == ""){
										c = line;
									}else{
										c = c+ls+line;	
									}
								}
							}
							in.close();
							byte[] contentInBytes = c.getBytes();
							FileOutputStream fop = new FileOutputStream(file);
							fop.write(contentInBytes);
							fop.flush();
							fop.close();
						}else{
							if(JOptionPane.showConfirmDialog(null, "You you want to overwrite " + path + " ?") == JOptionPane.OK_OPTION){
								InputStream fstream = new ByteArrayInputStream(code.getBytes());
								BufferedReader in = new BufferedReader(new InputStreamReader(fstream));
								String line = "";
								String c = "";
								while((line = in.readLine()) != null){
									if(line.contains("1") || line.contains("2") || line.contains("3") || line.contains("4") || line.contains("5") || line.contains("6") || line.contains("7")){
										if(c == ""){
											c = line;
										}else{
											c = c+ls+line;	
										}
									}
								}
								in.close();
								byte[] contentInBytes = c.getBytes();
								FileOutputStream fop = new FileOutputStream(file);
								fop.write(contentInBytes);
								fop.flush();
								fop.close();
							}
						}
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
				}
			}
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

}
