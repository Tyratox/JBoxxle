package ch.tyratox.nksa.jboxxle;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class JBoxxlePanel extends Panel implements KeyListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7740927712815581431L;
	private JBoxxle jb;
	public int[][] data;
	private ArrayList<Integer[][]> cache = new ArrayList<Integer[][]>();
	private ArrayList<Integer[][]> cache_ = new ArrayList<Integer[][]>();
	
	private ArrayList<Integer[][]> lvls = null;
	public boolean iscache;
	
	private int dudeX, dudeY;
	private int targets = 0;
	private Graphics dbGraphics;
	private Image dbImage;
	@SuppressWarnings("unused")
	private boolean resized;
	private Label win = new Label("You Won");
	private boolean firstpaint = true;
	private boolean moveLock = false;
	
	public String levelname = "";

	
	public JBoxxlePanel(JBoxxle jb){
		win.setForeground(Color.white);
		win.setFont(new Font("Helvetica", Font.PLAIN, 50));
		win.setBounds(100, 100, 400, 90);
		win.setEnabled(false);
		addKeyListener(this);
		this.jb = jb;
		setLayout(null);
		setSize(600, 600);
		setBackground(Color.LIGHT_GRAY);
	}
	
	public void paint(Graphics g){
		if(levelname != ""){
			jb.setTitle("Level: " + levelname);
		}
		this.requestFocus();
		if(data != null){
			targets = 0;
			for(int i = 0;i<20;i++){
				for(int j = 0;j<20;j++){
					g.drawImage(jb.images[data[i][j]], i*30, j*30, this);
					if(data[i][j] == 6 || data[i][j] == 7){
						dudeX = i;
						dudeY = j;
					}
					if(data[i][j] == 3 || data[i][j] == 7){
						targets++;
					}
				}
			}
			if(iscache == false && firstpaint == false){
//				System.out.println("Saving to " + cache.size());
				cache.add(jb.intAToIntegerA(data));
//				System.out.println(cache.size() - 1);
			}else{
				iscache = false;
			}
			if(firstpaint){
				firstpaint = false;
			}
		}
//		System.out.println("This level has " + targets + " targets!");
		if(targets == 0){
			win();
		}
		moveLock = false;
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
	public void setDatas(ArrayList<Integer[][]> list){
		System.out.println("Load Set; Size: " + list.size());
		data = jb.integerATointA(list.get(0));
		list.remove(0);
		lvls = list;
		try{
			if(lvls.get(0) == null){
				System.out.println("Last Level");
				lvls = null;
			}
		}catch(Exception e){
			
		}
		repaint();
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.isControlDown()){
			if(e.getKeyCode() == 89){
				if(e.isShiftDown()){
					try{
						Integer[][] c = cache_.get(cache_.size() - 1);
						iscache = true;
						cache_.remove(cache_.size() - 1);
						cache.add(c);
						setData(jb.integerATointA(c));
						}catch(ArrayIndexOutOfBoundsException e1){
//							System.out.println("No More Saves");
						}
				}else{
					try{
						iscache = true;
						Integer[][] c = cache.get(cache.size() -2);
//						System.out.println("Loading Save # " + (cache.size()-2));
						Integer[][] q = cache.get(cache.size()-1);
						cache.remove(cache.size()-1);
						cache_.add(q);
//						System.out.println("Removed Save # " + (cache.size()-1));
						setData(jb.integerATointA(c));
					}catch(ArrayIndexOutOfBoundsException e1){
//						System.out.println("No More Saves");
					}
				}
			}
		}
		if(e.getKeyCode() == 87){
			//W
			Move(0, -1);
		}else if(e.getKeyCode() == 65){
			//A
			Move(-1, 0);
		}else if(e.getKeyCode() == 83){
			//S
			Move(0, 1);
		}else if(e.getKeyCode() == 68){
			//D
			Move(1, 0);
		}else if(e.getKeyCode() == 27){
			//ESC
			try {
				if(jb.inGame){
					jb.inGame = false;
					jb.jbm.previewPanel(jb);
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}else if(e.getKeyCode() == 32){
			win();
		}
		if(win.isEnabled()){
			jb.add(this);
			jb.remove(win);
			win.setEnabled(false);
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
	}
	
	public void Move(int x, int y){
		if(moveLock == false){
			moveLock = true;
			clearcache_();
			boolean changeposition = true;
			if(data[dudeX + x][dudeY + y] != 0 && data[dudeX + x][dudeY + y] != 1){
				if(data[dudeX + x][dudeY + y] == 2 || data[dudeX + x][dudeY + y] == 3){
					if(data[dudeX + x][dudeY + y] == 2){
						data[dudeX + x][dudeY + y] = 6;
					}else if(data[dudeX + x][dudeY + y] == 3){
						data[dudeX + x][dudeY + y] = 7;
					}
				}else if(data[dudeX + x][dudeY + y] == 4 || data[dudeX + x][dudeY + y] == 5){
					if(data[dudeX + (2*x)][dudeY + (2*y)] == 3){
						data[dudeX + (2*x)][dudeY + (2*y)] = 5;
						if(data[dudeX + x][dudeY + y] == 4){
							data[dudeX + x][dudeY + y] = 6;
						}else{
							data[dudeX + x][dudeY + y] = 7;
						}
					}else if(data[dudeX + (2*x)][dudeY + (2*y)] == 2){
						data[dudeX + (2*x)][dudeY + (2*y)] = 4;
						if(data[dudeX + x][dudeY + y] == 4){
							data[dudeX + x][dudeY + y] = 6;
						}else{
							data[dudeX + x][dudeY + y] = 7;
						}
					}else{
						changeposition = false;
					}
				}
				if(changeposition){
					if(data[dudeX][dudeY] == 6){
						data[dudeX][dudeY] = 2;
					}else if(data[dudeX][dudeY] == 7){
						data[dudeX][dudeY] = 3;
					}
					repaint();
				}else{
					moveLock = false;
				}
			}else{
				moveLock = false;
			}
			}
	}
	
	public void win(){
		if(levelname != "" && levelname != null){
			jb.levelsDone.add(levelname);
			System.out.println("Solved Level: " + levelname);
			jb.saveDoneLevels();
			jb.getSolvedLevels();
		}
		boolean err = false;
		if(lvls != null){
			System.out.println("Win, next Level");
			try{
				data = jb.integerATointA(lvls.get(0));
			}catch(Exception e){
				err = true;
				try {
					jb.jbm.previewPanel(jb);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
			if(err==false){
				lvls.remove(0);
			}
			repaint();
		}else{
			System.out.println("Win");
			try {
				jb.jbm.previewPanel(jb);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public int[][] copyIntegerArray(int[][] data){
		int[][] copy = new int[20][20];
		for(int i = 0;i<20;i++){
			for(int j = 0;j<20;j++){
				copy[i][j] = data[i][j];
			}
		}
		return copy;
	}
	
	public void clearcache_ (){
		cache_ = new ArrayList<Integer[][]>();
	}
}
