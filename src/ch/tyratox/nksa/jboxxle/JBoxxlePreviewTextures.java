package ch.tyratox.nksa.jboxxle;

import java.awt.Button;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Panel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class JBoxxlePreviewTextures extends Panel{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2236046363711436598L;
	private int textureCounter = 0;
	private JBoxxle jb;
	private int[][] data;
	
	private Graphics dbGraphics;
	private Image dbImage;


	
	public JBoxxlePreviewTextures(final JBoxxle jb, final ArrayList<String> list, int[][] data, final boolean isEclipse){
		this.jb = jb;
		setLayout(null);
		setSize(600, 600);
		setBackground(Color.LIGHT_GRAY);
		setLocation(20, 40);

		setData(data);
		
		jb.prevt = this;
		
		Button[] btns;
		
		Button load = new Button("Load this Texture Pack");
		load.setBounds(230, 580, 150, 20);
		load.setVisible(true);
		load.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				jb.bc.repaint();
				jb.inGame = true;
				jb.add(jb.bc);
				jb.remove(jb.prevt);
			}
		});
		add(load);
		Button next = new Button("Next Texture Pack");
		next.setBounds(450, 580, 150, 20);
		next.setVisible(true);
		next.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if(textureCounter < list.size()-1){
					textureCounter++;
				}else{
					textureCounter = 0;
				}
				if(isEclipse){
					jb.loadImages(list.get(textureCounter));
				}else{
					jb.loadInternalImages(list.get(textureCounter));
				}
				repaint();
			}
		});
		add(next);
		Button previous = new Button("Previous Texture Pack");
		previous.setBounds(0, 580, 150, 20);
		previous.setVisible(true);
		previous.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if(textureCounter > 0){
					textureCounter--;
				}else{
					textureCounter = list.size()-1;
				}
				if(isEclipse){
					jb.loadImages(list.get(textureCounter));
				}else{
					jb.loadInternalImages(list.get(textureCounter));
				}
				repaint();
			}
		});
		add(previous);
		
		if(jb.btns == null){
			btns = new Button[3];
			btns[0] = load;
			btns[1] = next;
			btns[2] = previous;
		}else{
			btns = new Button[6];
			for(int i = 0;i<3;i++){
				jb.btns[i] = btns[i];
			}
			btns[3] = load;
			btns[4] = next;
			btns[5] = previous;
		}
		
	}
	
	public void paint(Graphics g){
		this.requestFocus();
		if(data != null){
			for(int i = 0;i<20;i++){
				for(int j = 0;j<20;j++){
					g.drawImage(jb.images[data[i][j]], i*30, j*30, this);
				}
			}
		}
	}
	public void update(Graphics g){
		if(dbImage == null){
			dbImage = createImage(this.getSize().width, this.getSize().height);
			dbGraphics = dbImage.getGraphics();
		}
		dbGraphics.setColor(getBackground());
		dbGraphics.fillRect(0, 0, this.getSize().width, this.getSize().height);
		dbGraphics.setColor(getForeground());
		paint(dbGraphics);
		g.drawImage(dbImage, 0, 0, this);
	}
	public void setData(int[][] data){
		this.data = data;
		repaint();
	}
	
}
