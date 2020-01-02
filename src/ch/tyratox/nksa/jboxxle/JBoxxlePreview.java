package ch.tyratox.nksa.jboxxle;

import java.awt.Button;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Panel;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class JBoxxlePreview extends Panel{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7740927712815581431L;
	private JBoxxle jb;
	private int[][] data;
	
	private Graphics dbGraphics;
	private Image dbImage;
	
	private int levelCounter = 0;


	
	public JBoxxlePreview(final JBoxxle jb, final ArrayList<String> list, final boolean isEclipse){
		jb.setTitle("JBoxxle");
		this.jb = jb;
		setLayout(null);
		setSize(600, 600);
		setBackground(Color.LIGHT_GRAY);
		setLocation(20, 40);
		
		if(isEclipse){
			setData(loadLevel(list.get(levelCounter)));
		}else{
			setData(loadInternalLevel(list.get(levelCounter)));
		}
		Choice dropdown = new Choice();
		String[] choice = {"Levels", "Level Packs"};
		for (int i = 0; i < choice.length; i++) {
			dropdown.insert(choice[i], i);
		}
		dropdown.select(0);
		dropdown.setBackground(Color.white);
		dropdown.setBounds(260, 560, 100, 20);
//		dropdown.setVisible(true);
		dropdown.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				System.out.println("Selected: " + e.getID());
				
			}
		});
//		jb.add(dropdown);
		Button load = new Button("Load this Level");
		load.setBounds(260, 600, 100, 20);
		load.setVisible(true);
		load.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				jb.inGame = true;
				String lname = list.get(levelCounter).split("/")[list.get(levelCounter).split("/").length - 1].replaceAll(".txt", "");
				jb.setTitle(lname);
				if(isEclipse){
					jb.loadLevel(list.get(levelCounter), lname);
				}else{
					jb.loadInternalLevel(list.get(levelCounter), lname);
				}
				
			}
		});
		jb.add(load);
		Button next = new Button("Next Level");
		next.setBounds(525, 600, 100, 20);
		next.setVisible(true);
		next.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if(levelCounter < list.size()-1){
					levelCounter++;
				}else{
					levelCounter = 0;
				}
				jb.setTitle(list.get(levelCounter).split("/")[list.get(levelCounter).split("/").length - 1].replaceAll(".txt", ""));
				if(isEclipse){
					setData(loadLevel(list.get(levelCounter)));
				}else{
					setData(loadInternalLevel(list.get(levelCounter)));
				}
			}
		});
		jb.add(next);
		Button previous = new Button("Previous Level");
		previous.setBounds(15, 600, 100, 20);
		previous.setVisible(true);
		previous.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if(levelCounter > 0){
					levelCounter--;
				}else{
					levelCounter = list.size()-1;
				}
				jb.setTitle(list.get(levelCounter).split("/")[list.get(levelCounter).split("/").length - 1].replaceAll(".txt", ""));
				if(isEclipse){
					setData(loadLevel(list.get(levelCounter)));
				}else{
					setData(loadInternalLevel(list.get(levelCounter)));
				}
			}
		});
		jb.add(previous);
		
		Button[] btns = new Button[3];
		btns[0] = load;
		btns[1] = next;
		btns[2] = previous;
		jb.btns = btns;
		setVisible(true);
	}
	
	public void paint(Graphics g){
		if(jb.levelsDone.contains(jb.getTitle())){
			jb.setTitle(jb.getTitle() + " [solved]");
		}
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
//		this.label TODO
		this.data = data;
		repaint();
	}
	
	private int[][] loadLevel(String name){
		return jb.getLevel(name);
	}
	private int[][] loadInternalLevel(String name){
		return jb.getInternalLevel(name);
	}
	
}
