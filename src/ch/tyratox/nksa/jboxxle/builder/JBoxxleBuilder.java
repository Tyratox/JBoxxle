package ch.tyratox.nksa.jboxxle.builder;

import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;

public class JBoxxleBuilder extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2146343028644029700L;
	private JBoxxleBuilderPanel jbp;
	public Image images[];
	public int[][] data = new int[20][20];


	/**
	 * Opens JBoxxle Builder
	 */
	public JBoxxleBuilder() {
		loadImages();
		jbp = new JBoxxleBuilderPanel(this);
		addButtons(this, jbp);
		setBounds(100, 100, 600, 600);
		add(jbp);
	}
	
	private void addButtons(JBoxxleBuilder jb, final JBoxxleBuilderPanel jbp) {
		for(int i = 0;i<20;i++){
			for(int j = 0;j<20;j++){
				final JButton h = new JButton();
				h.setBounds(i*30, j*30, 30, 30);
				h.addMouseListener(new MouseAdapter() {
					@Override
					public void mousePressed(MouseEvent e) {
						jbp.changeImage(h);
					}
				});
				h.setOpaque(false);
				h.setContentAreaFilled(false);
				h.setBorderPainted(false);
				jb.add(h);
			}
		}
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

}
