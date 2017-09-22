import java.awt.Color;

import javax.swing.JFrame;

public class Snake {

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		GamePlay game = new GamePlay();
		frame.setTitle("Snake Game");
		//frame.setBounds(0, 0, 620, 620);
		frame.setSize(607, 650);
		frame.setBackground(Color.LIGHT_GRAY);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(game);
		frame.setVisible(true);
		

	}

}
