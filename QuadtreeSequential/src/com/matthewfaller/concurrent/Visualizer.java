package com.matthewfaller.concurrent;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

/**
 * @author Matthew Faller
 * A simple swing class to visualize the changes to a quadtree. 
 */
public class Visualizer extends JFrame {
	
	private static final long serialVersionUID = 1L;	
	static int POINT_RADIUS = 14;
	static int WIDTH = 800, HEIGHT = 800;
	private static JPanel contentPane;
	private static QuadtreeConcurrent root;
	private static URL url;
	private static Image img;
	
	public Visualizer(){
		WIDTH = getToolkit().getScreenSize().height - 100; HEIGHT = WIDTH;
		root = new QuadtreeConcurrent(new Box(new Vec2f(WIDTH/2, HEIGHT/2), WIDTH, HEIGHT), true, null);
		url = getClass().getResource("flower.png");
		BufferedImage image = null;
		try {
			image = ImageIO.read(url);
			img = image.getScaledInstance(POINT_RADIUS, POINT_RADIUS, 0);
			image = ImageIO.read(getClass().getResource("point.png"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}		
		contentPane = new JPanel();
		contentPane.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		contentPane.setLayout(null);
		contentPane.addMouseListener(new MouseListener(){//Anonymous Inner Type
			@Override
			public void mouseClicked(MouseEvent e) {
				
				root.insert( new Float2( e.getPoint().x, e.getPoint().y) );
				redrawTree(root);
			}
			@Override
			public void mousePressed(MouseEvent e) {

				
			}
			@Override
			public void mouseReleased(MouseEvent e) {}
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}			
		});		
		setTitle("Quadtree Visualizer");
		setContentPane(contentPane);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setIconImage(image);
		setResizable(false);
		setLocation(getToolkit().getScreenSize().width/6, 10);
		pack();
	}
	/**
	 * @param args
	 */
	public static void main(String... args) {	
		Visualizer vis = new Visualizer();
		vis.setVisible(true);
	}
	
	private static void redrawTree(QuadtreeConcurrent tree){
		
		contentPane.removeAll();
		ArrayList<Box> boxes = tree.getBoxes();
		ArrayList<Float2> points = tree.getPoints(new ArrayList<Float2>());
		
		for(Box b : boxes){
			
			JLabel label = new JLabel();
			label.setBounds((int)(b.pos.x - b.halfWidth()), (int)(b.pos.y - b.halfHeight()), (int)b.getHeight(), (int)b.getHeight());
			label.setBorder(new LineBorder(Color.BLACK, 1));
			contentPane.add(label);
		}		
		for(Vec2f point : points){			
			//icon.getImage().getScaledInstance(POINT_RADIUS, height, hints)			
			ImageComponent label = new ImageComponent(img);			
			label.setBounds((int)point.x - POINT_RADIUS/2, (int)point.y - POINT_RADIUS/2, POINT_RADIUS, POINT_RADIUS);
			//label.setBackground(Color.RED);
			//label.setOpaque(true);
			label.addMouseListener(new MouseListener() {				
				@Override
				public void mouseReleased(MouseEvent e) {}				
				@Override
				public void mousePressed(MouseEvent e) {}				
				@Override
				public void mouseExited(MouseEvent e) {}				
				@Override
				public void mouseEntered(MouseEvent e) {}				
				@Override
				public void mouseClicked(MouseEvent e) {
					
					boolean deleted = root.delete(new Float2(e.getComponent().getBounds().x + POINT_RADIUS/2,
										  e.getComponent().getBounds().y + POINT_RADIUS/2));					
					if(deleted){
						redrawTree(root);
					}						
					//System.out.println("Tree Size : " + root.count());
				}
			});
			contentPane.add(label);
		}		
		contentPane.repaint();
	}
}