import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class GamePlay extends JPanel implements ActionListener, KeyListener{
	private final int B_WIDTH = 600;
	private final int B_HEIGHT = 600;
	private final int STEP = 20;
	private final int EYE = 8;
	private final int SHIFT = 12;
	private final int SPEED = 200;
	private Timer timer;
	
	private int[] snakeX;
	private int[] snakeY;
	private int tail;
	private int score;
	private int xPos;
	private int yPos;
	private int appleX;
	private int appleY;
	private int pause;
	
	private boolean left;
	private boolean right;
	private boolean up;
	private boolean down;	
	private boolean gameStart;
	private boolean gameOver;
		
	//private Timer stoneTimer;
	private int stoneDelay;
	private int stoneX;
	private int stoneY;
	
	public GamePlay() {
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		timer = new Timer(SPEED, this);
		gameInit();
		generateApple();
		generatestone();
	}
	
	public void paint(Graphics g){
		super.paintComponent(g);
		//Score board	
		drawScoreBoard(g);		
		
		//Game screen of 60X60 tiles
		drawGameBoard(g);
							
		// Apple placement
		drawApple(g);
		
		// stone placement
		drawStone(g);
		
		// Snake trail
		drawSnakeTail(g);
		
		if(pause==1){
			timer.stop();
			drawMessage(g, "GAME PAUSE");
		}	
			
		if(gameOver){
			timer.stop();
			drawMessage(g, "GAME OVER");
		}
		g.dispose();
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(right){
			xPos++;
			if(xPos>(B_WIDTH/STEP)-1)
				xPos = 0;
		}
		if(left){
			xPos--;
			if(xPos<0)
				xPos = (B_WIDTH/STEP)-1;
		}
		if(up){
			yPos--;
			if(yPos<0)
				yPos = (B_HEIGHT/STEP)-1;
		}
		if(down){
			yPos++;
			if(yPos>(B_HEIGHT/STEP)-1)
				yPos = 0;
		}
		
		if(snakeX[0]==appleX/STEP && snakeY[0]==appleY/STEP){
			tail++;
			score = score+10;
			generateApple();
		}
		
		if(gameStart){
			for(int i=tail-1; i>0; i--){			
				if(snakeX[i]==xPos && snakeY[i]==yPos){
					gameOver = true;
				}
				snakeX[i] = snakeX[i-1];
				snakeY[i] = snakeY[i-1];
			}
			snakeX[0] = xPos;
			snakeY[0] = yPos;
			if(snakeX[0]==stoneX/STEP && snakeY[0]==stoneY/STEP)
				gameOver=true;
			
			stoneDelay++;
			if(stoneDelay>100){
				generatestone();
			}
		}
			
		repaint();
	}

	@Override
	public void keyPressed(KeyEvent arg0) {		
		gameStart = true;
		switch(arg0.getKeyCode()){
		case KeyEvent.VK_RIGHT:
			right = true;
			left = up = down = false;
			break;
		case KeyEvent.VK_LEFT:
			left = true;
			right = up = down = false;
			break;
		case KeyEvent.VK_UP:
			up = true;
			left = right = down = false;
			break;
		case KeyEvent.VK_DOWN:
			down = true;
			left = right = up = false;
			break;
		case KeyEvent.VK_SPACE:
			gameInit();
			break;
		case KeyEvent.VK_P:
			pause++;
			if (pause==2) {
				timer.start();
				pause = 0;
			}
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		//Not used
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		//Not used
		
	}

	public void gameInit(){
		snakeX = new int[B_WIDTH];
		snakeY = new int[B_HEIGHT];
		tail = 1;
		score = 0;
		xPos = 10;
		yPos = 10;
		pause = 0;
		
		left = false;
		right = false;
		up = false;
		down = false;	
		gameStart = false;
		gameOver = false;
				
		snakeX[0] = xPos;
		snakeY[0] = yPos;
		stoneDelay=0;
		timer.start();
	}
	
	public void generateApple(){
		appleX = (int)(Math.random()*(B_WIDTH/STEP))*STEP;
		appleY = (int)(Math.random()*(B_HEIGHT/STEP))*STEP;
	}
	
	public void drawScoreBoard(Graphics g){
		if(!gameStart){
			g.setColor(Color.BLACK);
			g.drawString("Press ARROW keys to start game and P to pause.", 5, 615);
		} else {
			g.setColor(Color.BLACK);
			g.drawString("SCORE:" +score, 5, 615);
		}
	}
	
	public void drawGameBoard(Graphics g){
		g.setColor(Color.GREEN);
		g.fillRect(0, 0, B_WIDTH, B_HEIGHT);
		//Stencil 
		/*
		 * for(int i=0; i<=B_WIDTH; i=i+10){ for(int j=0; j<=B_HEIGHT; j=j+10){
		 * g.setColor(Color.LIGHT_GRAY); g.drawLine(i, j, i, B_HEIGHT);
		 * g.setColor(Color.LIGHT_GRAY); g.drawLine(i, j, B_WIDTH, j); } }
		 */
	}

	public void drawApple(Graphics g){
		g.setColor(Color.RED);
		g.fillOval(appleX, appleY, STEP, STEP);
		g.setColor(Color.ORANGE);
		g.fillOval(appleX, appleY, STEP-1, STEP-1);
		g.setColor(Color.RED);
		g.fillOval(appleX, appleY, EYE, EYE);
	}

	public void drawSnakeTail(Graphics g){
		for (int i=0; i<tail; i++) {
			if (i==0) {
				g.setColor(Color.RED);
				g.fillOval(snakeX[i] * STEP, snakeY[i] * STEP, STEP, STEP);
				g.setColor(Color.BLACK);
				if(!gameStart){
					g.fillOval(snakeX[i] * STEP + SHIFT, snakeY[i] * STEP + SHIFT, EYE, EYE);
					g.fillOval(snakeX[i] * STEP + SHIFT, (snakeY[i] * STEP), EYE, EYE);
				}
				if (left) {
					g.fillOval(snakeX[i] * STEP, snakeY[i] * STEP, EYE, EYE);
					g.fillOval(snakeX[i] * STEP, snakeY[i] * STEP + SHIFT, EYE, EYE);
				}
				if (right) {
					g.fillOval(snakeX[i] * STEP + SHIFT, snakeY[i] * STEP + SHIFT, EYE, EYE);
					g.fillOval(snakeX[i] * STEP + SHIFT, (snakeY[i] * STEP), EYE, EYE);
				}
				if (up) {
					g.fillOval(snakeX[i] * STEP + SHIFT, snakeY[i] * STEP, EYE, EYE);
					g.fillOval(snakeX[i] * STEP, (snakeY[i] * STEP), EYE, EYE);
				}
				if (down) {
					g.fillOval(snakeX[i] * STEP, snakeY[i] * STEP + SHIFT, EYE, EYE);
					g.fillOval(snakeX[i] * STEP + SHIFT, snakeY[i] * STEP + SHIFT, EYE, EYE);
				}
			} else {
				g.setColor(Color.RED);
				g.fillOval(snakeX[i] * STEP, snakeY[i] * STEP, STEP, STEP);
				g.setColor(Color.YELLOW);
				g.fillOval(snakeX[i] * STEP, snakeY[i] * STEP, EYE, EYE);
			}
		}
	}
	
	public void drawMessage(Graphics g, String gameStatus){
		switch(gameStatus){
		case "GAME OVER":
			g.setColor(Color.BLACK);
			g.drawString("GAME OVER", 250, 300);
			g.drawString("GAME OVER", 251, 300);
			g.drawString("Press SPACEBAR to restart.", 220, 320);
			break;
		case "GAME PAUSE":
			g.setColor(Color.BLACK);
			g.drawString("GAME PAUSE", 250, 300);
			g.drawString("GAME PAUSE", 251, 300);
			break;
		}
	}
	
	public void generatestone(){
		do{
			stoneX = (int)(Math.random()*(B_WIDTH/STEP))*STEP;
			stoneY = (int)(Math.random()*(B_HEIGHT/STEP))*STEP;
		}while((stoneX == appleX) && (stoneY == appleY));
		stoneDelay=0;
	}
	
	public void drawStone(Graphics g){
		g.setColor(Color.DARK_GRAY);
		g.fillRoundRect(stoneX, stoneY, 15, 15, 15, 15);
	}
}