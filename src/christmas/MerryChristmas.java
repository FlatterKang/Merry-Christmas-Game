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
	public static final int WIDTH = 400; // 面板宽
	public static final int HEIGHT = 654; // 面板高

	/** 游戏的四种状态: START RUNNING PAUSE GAME_OVER */
	private int state;
	private static final int START = 0;
	private static final int RUNNING = 1;
	private static final int PAUSE = 2;
	private static final int GAME_OVER = 3;

	private int score = 0; // 得分
	private Timer timer; // 定时器
	private int intervel = 1000 / 100; // 时间间隔(毫秒)
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

	private ArrayList<Shadow> shadowEggs = new ArrayList<>(); // 记录当前有阴影的彩蛋下标
	private FlyingObject[] flyings = {}; // 敌机数组
	private Bullet[] bullets = {}; // 子弹数组
	private Hero hero = new Hero(); // 英雄机
	private Snow[] snows = {}; // 画出雪景

	static { // 初始化图片资源
		try {
			background = ImageIO.read(MerryChristmas.class.getResource("background.png"));
			snow = ImageIO.read(MerryChristmas.class.getResource("snow.png"));
			start = ImageIO.read(MerryChristmas.class.getResource("start.png"));

			// 炸弹从大到小排列
			for (int i = 0; i < bomb.length; i++) {
				bomb[i] = ImageIO.read(MerryChristmas.class.getResource("bomb_" + i + ".png"));
			}
			// 糖果图片
			for (int i = 0; i < candy.length; i++) {
//				System.out.println("candy_" + (i + 1) + ".png");
				candy[i] = ImageIO.read(MerryChristmas.class.getResource("candy_" + (i + 1) + ".png"));
			}
			// 彩蛋阴影图
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

	/** 画 */
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.drawImage(background, 0, 0, null); // 画背景图
		paintShadow(g); // 画出彩蛋效果
		paintSnow(g); // 画出雪景
		paintHero(g); // 画英雄机
		paintBullets(g); // 画子弹
		paintFlyingObjects(g); // 画飞行物
		paintScore(g); // 画分数
		paintState(g); // 画游戏状态
	}

	public void paintShadow(Graphics g) {
		int tmp_index;
		for (int i = 0; i < shadowEggs.size(); i++) { // 遍历每一个彩蛋阴影
			// 对于该彩蛋，得到下一个要放映的图片下标并下标+1
			tmp_index = shadowEggs.get(i).nextIndex();

			if (tmp_index < 15) {
				g.drawImage(shadows[tmp_index], shadowEggs.get(i).getPaintX(), shadowEggs.get(i).getPaintY(), null);
			}
//			System.out.println("Center：x = "+shadowEggs.get(i).getCenterX()+", y = "+shadowEggs.get(i).getCenterY());
//			System.out.println("Paint：x = "+shadowEggs.get(i).getPaintX()+", y = "+shadowEggs.get(i).getPaintY());
		}
	}

	/** 画出雪景 */
	public void paintSnow(Graphics g) {
		for (int i = 0; i < snows.length; i++) {
			Snow s = snows[i];
			g.drawImage(s.getImage(), s.getX(), s.getY(), null);
//			System.out.println("画出雪景" + (i + 1) + "：x=" + s.getX() + ", y=" + s.getY());
		}

	}

	/** 画英雄机 */
	public void paintHero(Graphics g) {
		if (state == MerryChristmas.START)
			return;
		g.drawImage(hero.getImage(), hero.getX(), hero.getY(), null);
//		System.out.println("主机轮廓："+hero.getX()+", "+(hero.getX()+hero.getWidth()/2)+", "+ (hero.getX()+hero.getWidth()));
	}

	/** 画子弹 */
	public void paintBullets(Graphics g) {
		for (int i = 0; i < bullets.length; i++) {
			Bullet b = bullets[i];
			g.drawImage(b.getImage(), b.getX(), b.getY(), null);
//			System.out.println("子弹轮廓："+b.getX()+", "+(b.getX()+b.getWidth()/2)+", "+(b.getX()+b.getWidth()));
		}
	}

	/** 画飞行物 */
	public void paintFlyingObjects(Graphics g) {
		for (int i = 0; i < flyings.length; i++) {
			FlyingObject f = flyings[i];
			g.drawImage(f.getImage(), f.getX(), f.getY(), null);
		}
	}

	/** 画分数 */
	public void paintScore(Graphics g) {
		if (state == MerryChristmas.START)
			return;

		if (state == MerryChristmas.GAME_OVER) {
			Font font = new Font(Font.SANS_SERIF, Font.BOLD, 36); // 字体
			g.setColor(new Color(0xFF0000));
			g.setFont(font); // 设置字体
			g.drawString("最终得分：" + score, 85, 375);

			font = new Font(Font.SANS_SERIF, Font.BOLD, 24); // 字体
			g.setFont(font); // 设置字体
			g.drawString("单击左键再试一次", 100, 420);
			return;
		}

		int x = 10; // x坐标
		int y = 25; // y坐标
		Font font = new Font(Font.SANS_SERIF, Font.BOLD, 22); // 字体
		g.setColor(new Color(0xFF0000));
		g.setFont(font); // 设置字体
		g.drawString("SCORE:" + score, x, y); // 画分数
		y = y + 20; // y坐标增20
		g.drawString("LIFE:" + hero.getLife(), x, y); // 画命
	}

	/** 画游戏状态 */
	public void paintState(Graphics g) {
		switch (state) {
		case START: // 启动状态
			g.drawImage(start, 0, 0, null);
			break;
		case PAUSE: // 暂停状态
			g.drawImage(pause, 0, 0, null);
			break;
		case GAME_OVER: // 游戏终止状态
			g.drawImage(gameover, 0, 0, null);
			break;
		}
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame("Christmas Fly");
		MerryChristmas game = new MerryChristmas(); // 面板对象
		frame.add(game); // 将面板添加到JFrame中
		frame.setSize(WIDTH + 3 + 3, HEIGHT + 37 + 3); // 设置大小=内容面板+边框宽度
		frame.setAlwaysOnTop(true); // 设置其总在最上
		frame.setResizable(false); // 窗口大小不得改变
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 默认关闭操作
		frame.setIconImage(new ImageIcon("images/icon.jpg").getImage()); // 设置窗体的图标
		frame.setLocationRelativeTo(null); // 设置窗体初始位置

		frame.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(new ImageIcon("").getImage(), new Point(0, 0),
				"hiddenCursor"));
		frame.setVisible(true); // 尽快调用paint
		// frame.setFocusable(true);
		// frame.requestFocus();
		// frame.setAutoRequestFocus(true);

		game.action(); // 启动执行
	}

	/** 启动执行代码 */
	public void action() {
		// 鼠标监听事件
		MouseAdapter ml = new MouseAdapter() {

			@Override
			public void mouseMoved(MouseEvent e) { // 鼠标移动
				if (state == RUNNING) { // 运行状态下移动英雄机--随鼠标位置
					int x = e.getX();
					int y = e.getY();
					hero.moveTo(x, y);
				}
			}

			@Override
			public void mouseEntered(MouseEvent e) { // 鼠标进入
				if (state == PAUSE) { // 暂停状态下运行
					state = RUNNING;
				}

			}

			@Override
			public void mouseExited(MouseEvent e) { // 鼠标退出
				if (state == RUNNING) { // 游戏未结束，则设置其为暂停
					state = PAUSE;
				}
			}

			@Override
			public void mouseClicked(MouseEvent e) { // 鼠标点击
				switch (state) {
				case START:
					state = RUNNING; // 启动状态下运行
					break;
				case GAME_OVER: // 游戏结束，清理现场
					flyings = new FlyingObject[0]; // 清空飞行物
					snows = new Snow[0]; // 清理积雪
					bullets = new Bullet[0]; // 清空子弹
					hero = new Hero(); // 重新创建英雄机
					score = 0; // 清空成绩
					state = START; // 状态设置为启动
					break;
				}
			}
		};

		KeyListener kl = new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
//				System.out.println("敲击了:"+e.getKeyCode());
			}

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyChar() == ' ') {
					isShooting = true;
//					System.out.println("开始发射！");
				}
//				System.out.println("按下了:"+e.getKeyCode());
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyChar() == ' ') {
					isShooting = false;
					shootIndex = 0;
//					System.out.println("结束发射！");
				}
//				System.out.println("按下了:"+e.getKeyCode());
			}
		};

		this.addMouseListener(ml); // 处理鼠标点击操作
		this.addMouseMotionListener(ml); // 处理鼠标滑动操作

		this.setFocusable(true); // 设置可聚焦
		this.requestFocus(); // 初始对焦
		this.addKeyListener(kl); // 添加键盘监听

		timer = new Timer(); // 主流程控制
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				if (state == RUNNING) { // 运行状态
					requestFocus(); // 获得聚焦
					enterAction(); // 飞行物入场
					stepAction(); // 走一步
					shootAction(); // 英雄机射击
					bangAction(); // 子弹打飞行物
					outOfBoundsAction(); // 删除越界飞行物及子弹
					checkGameOverAction(); // 检查游戏结束
				}
				repaint(); // 重绘，调用paint()方法
			}

		}, intervel, intervel);
	}

	int flyEnteredIndex = 0; // 飞行物入场计数

	/** 飞行物入场 */
	public void enterAction() {
		flyEnteredIndex++;
		if (flyEnteredIndex % 60 == 0) { // 600毫秒生成一个飞行物
			FlyingObject obj = nextOne(); // 随机生成一个飞行物
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

	/** 雪下一帧，飞行物移动一帧，子弹前进一帧，英雄机前进一帧 */
	public void stepAction() {
		for (int i = 0; i < snows.length; i++) {
			Snow s = snows[i];
			s.step();
		}

		for (int i = 0; i < flyings.length; i++) { // 飞行物走一步
			FlyingObject f = flyings[i];
			f.step();
		}

		for (int i = 0; i < bullets.length; i++) { // 子弹走一步
			Bullet b = bullets[i];
			b.step();
		}
		hero.step(); // 英雄机走一步
	}

	/** 飞行物走一步 */
	public void flyingStepAction() {
		for (int i = 0; i < flyings.length; i++) {
			FlyingObject f = flyings[i];
			f.step();
		}
	}

	int shootIndex = 0; // 射击计数

	/** 射击 */
	public void shootAction() {
		if (!isShooting)
			return;
		if (shootIndex % 20 == 0) { // 200毫秒发一颗
			Bullet[] bs = hero.shoot(); // 英雄打出子弹
			bullets = Arrays.copyOf(bullets, bullets.length + bs.length); // 扩容
			System.arraycopy(bs, 0, bullets, bullets.length - bs.length, bs.length); // 追加数组
		}
		shootIndex++;
	}

	/** 子弹与飞行物碰撞检测 */
	public void bangAction() {
		for (int i = 0; i < bullets.length; i++) { // 遍历所有子弹
			Bullet b = bullets[i];
			bang(b); // 子弹和飞行物之间的碰撞检查
		}
	}

	/** 删除超出画板的飞行物及子弹 */
	public void outOfBoundsAction() {
		int index = 0; // 索引
		Snow[] snowsLive = new Snow[snows.length]; // 雪景
		for (int i = 0; i < snows.length; i++) {
			Snow s = snows[i];
			if (!s.outOfBounds()) {
				snowsLive[index++] = s;
			}
		}
		snows = Arrays.copyOf(snowsLive, index);

		index = 0;
		FlyingObject[] flyingLives = new FlyingObject[flyings.length]; // 活着的飞行物
		for (int i = 0; i < flyings.length; i++) {
			FlyingObject f = flyings[i];
			if (!f.outOfBounds()) {
				flyingLives[index++] = f; // 不越界的留着
			}
		}
		flyings = Arrays.copyOf(flyingLives, index); // 将不越界的飞行物都留着

		index = 0; // 索引重置为0
		Bullet[] bulletLives = new Bullet[bullets.length];
		for (int i = 0; i < bullets.length; i++) {
			Bullet b = bullets[i];
			if (!b.outOfBounds()) {
				bulletLives[index++] = b;
			}
		}
		bullets = Arrays.copyOf(bulletLives, index); // 将不越界的子弹留着

		// 删除播放完毕的彩蛋
		index = 0;
		for (int i = 0; i < shadowEggs.size(); i++) {
			if (shadowEggs.get(i).getIndex() > 14) {
				shadowEggs.remove(i);
//				System.out.println("删除一个彩蛋阴影！");
			}
		}
	}

	/** 检查游戏结束 */
	public void checkGameOverAction() {
		if (isGameOver() == true) {
			state = GAME_OVER; // 改变状态
			shootIndex = 0;
		}

	}

	/** 检查主机是否发生碰撞，游戏是否结束 */
	public boolean isGameOver() {

		for (int i = 0; i < flyings.length; i++) {
			int index = -1;
			FlyingObject obj = flyings[i];
			if (hero.hit(obj)) { // 检查英雄机与飞行物是否碰撞
//				System.out.println("发生了一次碰撞！");

				// 检查one的类型(敌人加分，打到糖果不加分)
				if (obj instanceof Enemy) { // 检查类型，是敌人，则扣血
					hero.subtractLife(); // 减命
					hero.setDoubleFire(0); // 双倍火力解除

				} else if (obj instanceof Award) { // 若为奖励，设置奖励
					Award a = (Award) obj;
					int type = a.getType(); // 获取奖励类型
					switch (type) {
					case Award.DOUBLE_FIRE:
						hero.addDoubleFire(20); // 设置双倍火力
						break;
					case Award.LIFE:
						hero.addLife(); // 设置加命
						break;
					}
				}
				index = i; // 记录碰上的飞行物索引
			}

			// 删除碰上的飞行物
			if (index != -1) {
				FlyingObject t = flyings[index];
				flyings[index] = flyings[flyings.length - 1];
				flyings[flyings.length - 1] = t; // 碰上的与最后一个飞行物交换

				flyings = Arrays.copyOf(flyings, flyings.length - 1);
			}
		}

		return hero.getLife() <= 0;
	}

	/** 子弹和飞行物之间的碰撞检查 */
	public void bang(Bullet bullet) {
		int index = -1; // 击中的飞行物索引
		for (int i = 0; i < flyings.length; i++) {
			FlyingObject obj = flyings[i];
			if (obj.shootBy(bullet)) { // 判断是否击中
				index = i; // 记录被击中的飞行物的索引
				break;
			}
		}
		if (index != -1) { // 有击中的飞行物
			// 删除该炮弹
			int index_bullet = 0; // 遍历炮弹
			Bullet[] bulletLives = new Bullet[bullets.length];
			for (int i = 0; i < bullets.length; i++) {
				Bullet b = bullets[i];
				if (b != bullet) {
					bulletLives[index_bullet++] = b;
				}
			}
			bullets = Arrays.copyOf(bulletLives, index_bullet); // 将没击中的炮弹留着

			FlyingObject one = flyings[index]; // 被击中的飞行物
			one.subtractLife();
//			if(one.getImage()==bomb[0]) {
//				System.out.println("彩蛋还有"+one.getLife()+"条命");
//			}
//			System.out.println("飞行物还有"+one.getLife()+"条命");
			if (one.getLife() > 0)
				return;
			if (one.getImage() == bomb[0]) { // 彩蛋特效
				Shadow s = new Shadow(shadows, one.getX(), one.getY());
				shadowEggs.add(s);
			}
			flyings[index] = null;// 丢弃被击中的飞行物，
			flyings[index] = flyings[flyings.length - 1]; // 用数组末尾飞行物移动填补空白
			flyings = Arrays.copyOf(flyings, flyings.length - 1); // 删除数组最后一个索引

			// 检查one的类型(敌人加分，打到糖果不加分)
			if (one instanceof Enemy) { // 检查类型，是敌人，则加分
				Enemy e = (Enemy) one; // 强制类型转换
				score += e.getScore(); // 加分
			} else { // 若打到奖励，则扣一分
				score--;
			}
		}
	}

	/**
	 * 随机生成掉落物
	 * 
	 * @return 飞行物对象
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
