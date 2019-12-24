package christmas;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class MerryChristmas extends JPanel {
	public static final int WIDTH = 400; // ����
	public static final int HEIGHT = 654; // ����

	/** ��Ϸ������״̬: START RUNNING PAUSE GAME_OVER */
	private int state;
	private static final int START = 0;
	private static final int RUNNING = 1;
	private static final int PAUSE = 2;
	private static final int GAME_OVER = 3;

	private int score = 0; // �÷�
	private Timer timer; // ��ʱ��
	private int intervel = 1000 / 100; // ʱ����(����)
	private boolean isShooting = false;

	public static BufferedImage background;
	public static BufferedImage snow;
	public static BufferedImage start;

	public static BufferedImage[] bomb = new BufferedImage[4];
	public static BufferedImage[] candy = new BufferedImage[4];
	public static BufferedImage[] shadows = new BufferedImage[15];
	public static BufferedImage bullet;
	public static BufferedImage hero0;
	public static BufferedImage hero1;
	public static BufferedImage hero2;
	public static BufferedImage pause;
	public static BufferedImage gameover;

	private ArrayList<Shadow> shadowEggs = new ArrayList<>(); // ��¼��ǰ����Ӱ�Ĳʵ��±�
	private FlyingObject[] flyings = {}; // �л�����
	private Bullet[] bullets = {}; // �ӵ�����
	private Hero hero = new Hero(); // Ӣ�ۻ�
	private Snow[] snows = {}; // ����ѩ��

	static { // ��ʼ��ͼƬ��Դ
		try {
			background = ImageIO.read(MerryChristmas.class.getResource("background.png"));
			snow = ImageIO.read(MerryChristmas.class.getResource("snow.png"));
			start = ImageIO.read(MerryChristmas.class.getResource("start.png"));

			// ը���Ӵ�С����
			for (int i = 0; i < bomb.length; i++) {
				bomb[i] = ImageIO.read(MerryChristmas.class.getResource("bomb_" + i + ".png"));
			}
			// �ǹ�ͼƬ
			for (int i = 0; i < candy.length; i++) {
//				System.out.println("candy_" + (i + 1) + ".png");
				candy[i] = ImageIO.read(MerryChristmas.class.getResource("candy_" + (i + 1) + ".png"));
			}
			// �ʵ���Ӱͼ
			for (int i = 0; i < shadows.length; i++) {
				shadows[i] = ImageIO.read(MerryChristmas.class.getResource("shadow_" + i + ".png"));
			}
			bullet = ImageIO.read(MerryChristmas.class.getResource("bullet_comet.png"));
			hero0 = ImageIO.read(MerryChristmas.class.getResource("hero0.png"));
			hero1 = ImageIO.read(MerryChristmas.class.getResource("hero1.png"));
			hero2 = ImageIO.read(MerryChristmas.class.getResource("hero2.png"));
			pause = ImageIO.read(MerryChristmas.class.getResource("snow.png"));
			gameover = ImageIO.read(MerryChristmas.class.getResource("gameover.png"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** �� */
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.drawImage(background, 0, 0, null); // ������ͼ
		paintShadow(g); // �����ʵ�Ч��
		paintSnow(g); // ����ѩ��
		paintHero(g); // ��Ӣ�ۻ�
		paintBullets(g); // ���ӵ�
		paintFlyingObjects(g); // ��������
		paintScore(g); // ������
		paintState(g); // ����Ϸ״̬
	}

	public void paintShadow(Graphics g) {
		int tmp_index;
		for (int i = 0; i < shadowEggs.size(); i++) { // ����ÿһ���ʵ���Ӱ
			// ���ڸòʵ����õ���һ��Ҫ��ӳ��ͼƬ�±겢�±�+1
			tmp_index = shadowEggs.get(i).nextIndex();

			if (tmp_index < 15) {
				g.drawImage(shadows[tmp_index], shadowEggs.get(i).getPaintX(), shadowEggs.get(i).getPaintY(), null);
			}
//			System.out.println("Center��x = "+shadowEggs.get(i).getCenterX()+", y = "+shadowEggs.get(i).getCenterY());
//			System.out.println("Paint��x = "+shadowEggs.get(i).getPaintX()+", y = "+shadowEggs.get(i).getPaintY());
		}
	}

	/** ����ѩ�� */
	public void paintSnow(Graphics g) {
		for (int i = 0; i < snows.length; i++) {
			Snow s = snows[i];
			g.drawImage(s.getImage(), s.getX(), s.getY(), null);
//			System.out.println("����ѩ��" + (i + 1) + "��x=" + s.getX() + ", y=" + s.getY());
		}

	}

	/** ��Ӣ�ۻ� */
	public void paintHero(Graphics g) {
		if (state == MerryChristmas.START)
			return;
		g.drawImage(hero.getImage(), hero.getX(), hero.getY(), null);
//		System.out.println("����������"+hero.getX()+", "+(hero.getX()+hero.getWidth()/2)+", "+ (hero.getX()+hero.getWidth()));
	}

	/** ���ӵ� */
	public void paintBullets(Graphics g) {
		for (int i = 0; i < bullets.length; i++) {
			Bullet b = bullets[i];
			g.drawImage(b.getImage(), b.getX(), b.getY(), null);
//			System.out.println("�ӵ�������"+b.getX()+", "+(b.getX()+b.getWidth()/2)+", "+(b.getX()+b.getWidth()));
		}
	}

	/** �������� */
	public void paintFlyingObjects(Graphics g) {
		for (int i = 0; i < flyings.length; i++) {
			FlyingObject f = flyings[i];
			g.drawImage(f.getImage(), f.getX(), f.getY(), null);
		}
	}

	/** ������ */
	public void paintScore(Graphics g) {
		if (state == MerryChristmas.START)
			return;

		if (state == MerryChristmas.GAME_OVER) {
			Font font = new Font(Font.SANS_SERIF, Font.BOLD, 36); // ����
			g.setColor(new Color(0xFF0000));
			g.setFont(font); // ��������
			g.drawString("���յ÷֣�" + score, 85, 375);

			font = new Font(Font.SANS_SERIF, Font.BOLD, 24); // ����
			g.setFont(font); // ��������
			g.drawString("�����������һ��", 100, 420);
			return;
		}

		int x = 10; // x����
		int y = 25; // y����
		Font font = new Font(Font.SANS_SERIF, Font.BOLD, 22); // ����
		g.setColor(new Color(0xFF0000));
		g.setFont(font); // ��������
		g.drawString("SCORE:" + score, x, y); // ������
		y = y + 20; // y������20
		g.drawString("LIFE:" + hero.getLife(), x, y); // ����
	}

	/** ����Ϸ״̬ */
	public void paintState(Graphics g) {
		switch (state) {
		case START: // ����״̬
			g.drawImage(start, 0, 0, null);
			break;
		case PAUSE: // ��ͣ״̬
			g.drawImage(pause, 0, 0, null);
			break;
		case GAME_OVER: // ��Ϸ��ֹ״̬
			g.drawImage(gameover, 0, 0, null);
			break;
		}
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame("Christmas Fly");
		MerryChristmas game = new MerryChristmas(); // ������
		frame.add(game); // �������ӵ�JFrame��
		frame.setSize(WIDTH + 3 + 3, HEIGHT + 37 + 3); // ���ô�С=�������+�߿���
		frame.setAlwaysOnTop(true); // ��������������
		frame.setResizable(false); // ���ڴ�С���øı�
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Ĭ�Ϲرղ���
		frame.setIconImage(new ImageIcon("images/icon.jpg").getImage()); // ���ô����ͼ��
		frame.setLocationRelativeTo(null); // ���ô����ʼλ��

		frame.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(new ImageIcon("").getImage(), new Point(0, 0),
				"hiddenCursor"));
		frame.setVisible(true); // �������paint
		// frame.setFocusable(true);
		// frame.requestFocus();
		// frame.setAutoRequestFocus(true);

		game.action(); // ����ִ��
	}

	/** ����ִ�д��� */
	public void action() {
		// �������¼�
		MouseAdapter ml = new MouseAdapter() {

			@Override
			public void mouseMoved(MouseEvent e) { // ����ƶ�
				if (state == RUNNING) { // ����״̬���ƶ�Ӣ�ۻ�--�����λ��
					int x = e.getX();
					int y = e.getY();
					hero.moveTo(x, y);
				}
			}

			@Override
			public void mouseEntered(MouseEvent e) { // ������
				if (state == PAUSE) { // ��ͣ״̬������
					state = RUNNING;
				}

			}

			@Override
			public void mouseExited(MouseEvent e) { // ����˳�
				if (state == RUNNING) { // ��Ϸδ��������������Ϊ��ͣ
					state = PAUSE;
				}
			}

			@Override
			public void mouseClicked(MouseEvent e) { // �����
				switch (state) {
				case START:
					state = RUNNING; // ����״̬������
					break;
				case GAME_OVER: // ��Ϸ�����������ֳ�
					flyings = new FlyingObject[0]; // ��շ�����
					snows = new Snow[0]; // �����ѩ
					bullets = new Bullet[0]; // ����ӵ�
					hero = new Hero(); // ���´���Ӣ�ۻ�
					score = 0; // ��ճɼ�
					state = START; // ״̬����Ϊ����
					break;
				}
			}
		};

		KeyListener kl = new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
//				System.out.println("�û���:"+e.getKeyCode());
			}

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyChar() == ' ') {
					isShooting = true;
//					System.out.println("��ʼ���䣡");
				}
//				System.out.println("������:"+e.getKeyCode());
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyChar() == ' ') {
					isShooting = false;
					shootIndex = 0;
//					System.out.println("�������䣡");
				}
//				System.out.println("������:"+e.getKeyCode());
			}
		};

		this.addMouseListener(ml); // �������������
		this.addMouseMotionListener(ml); // ������껬������

		this.setFocusable(true); // ���ÿɾ۽�
		this.requestFocus(); // ��ʼ�Խ�
		this.addKeyListener(kl); // ��Ӽ��̼���

		timer = new Timer(); // �����̿���
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				if (state == RUNNING) { // ����״̬
					requestFocus(); // ��þ۽�
					enterAction(); // �������볡
					stepAction(); // ��һ��
					shootAction(); // Ӣ�ۻ����
					bangAction(); // �ӵ��������
					outOfBoundsAction(); // ɾ��Խ������Ｐ�ӵ�
					checkGameOverAction(); // �����Ϸ����
				}
				repaint(); // �ػ棬����paint()����
			}

		}, intervel, intervel);
	}

	int flyEnteredIndex = 0; // �������볡����

	/** �������볡 */
	public void enterAction() {
		flyEnteredIndex++;
		if (flyEnteredIndex % 60 == 0) { // 600��������һ��������
			FlyingObject obj = nextOne(); // �������һ��������
			flyings = Arrays.copyOf(flyings, flyings.length + 1);
			flyings[flyings.length - 1] = obj;
		}

		if (snows.length == 0) {
			Snow s = new Snow();
			snows = Arrays.copyOf(snows, snows.length + 1);
			snows[snows.length - 1] = s;
		} else if (snows.length < 2) {
			if (snows[0].getY() > 0) {
				Snow s = new Snow();
				snows = Arrays.copyOf(snows, snows.length + 1);
				snows[snows.length - 1] = s;
			}
		}
	}

	/** ѩ��һ֡���������ƶ�һ֡���ӵ�ǰ��һ֡��Ӣ�ۻ�ǰ��һ֡ */
	public void stepAction() {
		for (int i = 0; i < snows.length; i++) {
			Snow s = snows[i];
			s.step();
		}

		for (int i = 0; i < flyings.length; i++) { // ��������һ��
			FlyingObject f = flyings[i];
			f.step();
		}

		for (int i = 0; i < bullets.length; i++) { // �ӵ���һ��
			Bullet b = bullets[i];
			b.step();
		}
		hero.step(); // Ӣ�ۻ���һ��
	}

	/** ��������һ�� */
	public void flyingStepAction() {
		for (int i = 0; i < flyings.length; i++) {
			FlyingObject f = flyings[i];
			f.step();
		}
	}

	int shootIndex = 0; // �������

	/** ��� */
	public void shootAction() {
		if (!isShooting)
			return;
		if (shootIndex % 20 == 0) { // 200���뷢һ��
			Bullet[] bs = hero.shoot(); // Ӣ�۴���ӵ�
			bullets = Arrays.copyOf(bullets, bullets.length + bs.length); // ����
			System.arraycopy(bs, 0, bullets, bullets.length - bs.length, bs.length); // ׷������
		}
		shootIndex++;
	}

	/** �ӵ����������ײ��� */
	public void bangAction() {
		for (int i = 0; i < bullets.length; i++) { // ���������ӵ�
			Bullet b = bullets[i];
			bang(b); // �ӵ��ͷ�����֮�����ײ���
		}
	}

	/** ɾ����������ķ����Ｐ�ӵ� */
	public void outOfBoundsAction() {
		int index = 0; // ����
		Snow[] snowsLive = new Snow[snows.length]; // ѩ��
		for (int i = 0; i < snows.length; i++) {
			Snow s = snows[i];
			if (!s.outOfBounds()) {
				snowsLive[index++] = s;
			}
		}
		snows = Arrays.copyOf(snowsLive, index);

		index = 0;
		FlyingObject[] flyingLives = new FlyingObject[flyings.length]; // ���ŵķ�����
		for (int i = 0; i < flyings.length; i++) {
			FlyingObject f = flyings[i];
			if (!f.outOfBounds()) {
				flyingLives[index++] = f; // ��Խ�������
			}
		}
		flyings = Arrays.copyOf(flyingLives, index); // ����Խ��ķ����ﶼ����

		index = 0; // ��������Ϊ0
		Bullet[] bulletLives = new Bullet[bullets.length];
		for (int i = 0; i < bullets.length; i++) {
			Bullet b = bullets[i];
			if (!b.outOfBounds()) {
				bulletLives[index++] = b;
			}
		}
		bullets = Arrays.copyOf(bulletLives, index); // ����Խ����ӵ�����

		// ɾ��������ϵĲʵ�
		index = 0;
		for (int i = 0; i < shadowEggs.size(); i++) {
			if (shadowEggs.get(i).getIndex() > 14) {
				shadowEggs.remove(i);
//				System.out.println("ɾ��һ���ʵ���Ӱ��");
			}
		}
	}

	/** �����Ϸ���� */
	public void checkGameOverAction() {
		if (isGameOver() == true) {
			state = GAME_OVER; // �ı�״̬
			shootIndex = 0;
		}

	}

	/** ��������Ƿ�����ײ����Ϸ�Ƿ���� */
	public boolean isGameOver() {

		for (int i = 0; i < flyings.length; i++) {
			int index = -1;
			FlyingObject obj = flyings[i];
			if (hero.hit(obj)) { // ���Ӣ�ۻ���������Ƿ���ײ
//				System.out.println("������һ����ײ��");

				// ���one������(���˼ӷ֣����ǹ����ӷ�)
				if (obj instanceof Enemy) { // ������ͣ��ǵ��ˣ����Ѫ
					hero.subtractLife(); // ����
					hero.setDoubleFire(0); // ˫���������

				} else if (obj instanceof Award) { // ��Ϊ���������ý���
					Award a = (Award) obj;
					int type = a.getType(); // ��ȡ��������
					switch (type) {
					case Award.DOUBLE_FIRE:
						hero.addDoubleFire(20); // ����˫������
						break;
					case Award.LIFE:
						hero.addLife(); // ���ü���
						break;
					}
				}
				index = i; // ��¼���ϵķ���������
			}

			// ɾ�����ϵķ�����
			if (index != -1) {
				FlyingObject t = flyings[index];
				flyings[index] = flyings[flyings.length - 1];
				flyings[flyings.length - 1] = t; // ���ϵ������һ�������ｻ��

				flyings = Arrays.copyOf(flyings, flyings.length - 1);
			}
		}

		return hero.getLife() <= 0;
	}

	/** �ӵ��ͷ�����֮�����ײ��� */
	public void bang(Bullet bullet) {
		int index = -1; // ���еķ���������
		for (int i = 0; i < flyings.length; i++) {
			FlyingObject obj = flyings[i];
			if (obj.shootBy(bullet)) { // �ж��Ƿ����
				index = i; // ��¼�����еķ����������
				break;
			}
		}
		if (index != -1) { // �л��еķ�����
			// ɾ�����ڵ�
			int index_bullet = 0; // �����ڵ�
			Bullet[] bulletLives = new Bullet[bullets.length];
			for (int i = 0; i < bullets.length; i++) {
				Bullet b = bullets[i];
				if (b != bullet) {
					bulletLives[index_bullet++] = b;
				}
			}
			bullets = Arrays.copyOf(bulletLives, index_bullet); // ��û���е��ڵ�����

			FlyingObject one = flyings[index]; // �����еķ�����
			one.subtractLife();
//			if(one.getImage()==bomb[0]) {
//				System.out.println("�ʵ�����"+one.getLife()+"����");
//			}
//			System.out.println("�����ﻹ��"+one.getLife()+"����");
			if (one.getLife() > 0)
				return;
			if (one.getImage() == bomb[0]) { // �ʵ���Ч
				Shadow s = new Shadow(shadows, one.getX(), one.getY());
				shadowEggs.add(s);
			}
			flyings[index] = null;// ���������еķ����
			flyings[index] = flyings[flyings.length - 1]; // ������ĩβ�������ƶ���հ�
			flyings = Arrays.copyOf(flyings, flyings.length - 1); // ɾ���������һ������

			// ���one������(���˼ӷ֣����ǹ����ӷ�)
			if (one instanceof Enemy) { // ������ͣ��ǵ��ˣ���ӷ�
				Enemy e = (Enemy) one; // ǿ������ת��
				score += e.getScore(); // �ӷ�
			} else { // ���򵽽��������һ��
				score--;
			}
		}
	}

	/**
	 * ������ɵ�����
	 * 
	 * @return ���������
	 */
	public static FlyingObject nextOne() {
		Random random = new Random();
		int type = random.nextInt(20); // [0,20)
		if (type < 3) {
			return new Candy();
		} else {
			return new Bomb();
		}
	}

}

class Shadow {
	/**
	 * 
	 * @param x the dead egg's X
	 * @param y the dead egg's Y
	 */

	private static double playSpeed = 0.3;

	public Shadow(BufferedImage[] imgArr, int x, int y) {
		shadows = imgArr;
		this.shadowIndex = -0.5;
		this.centerX = x + (MerryChristmas.bomb[0].getWidth() / 2);
		this.centerY = y + (MerryChristmas.bomb[0].getHeight() / 2);
	}

	public int nextIndex() {
//		System.out.println("index="+shadowIndex+"---->"+(int)(shadowIndex+playSpeed));
		return (int) (shadowIndex += playSpeed);
	}

	public int getIndex() {
//		System.out.println("index="+shadowIndex+"---->"+(int)(shadowIndex+playSpeed));
		return (int) (shadowIndex);
	}

	public int getCenterX() {
		return centerX;
	}

	public int getCenterY() {
		return centerY;
	}

	public int getPaintX() {
		return centerX - shadows[getIndex()].getWidth() / 2;
	}

	public int getPaintY() {
		return centerY - shadows[getIndex()].getHeight() / 2;
	}

	private double shadowIndex;
	private BufferedImage[] shadows;
	private int centerX;
	private int centerY;
}
