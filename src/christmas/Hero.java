package christmas;

import java.awt.image.BufferedImage;

/**
 * 英雄机:是飞行物
 */
public class Hero extends FlyingObject {

	private BufferedImage[] images = {}; // 英雄机图片
	private int index = 0; // 英雄机图片切换索引

	private int doubleFire; // 双倍火力
	

	/** 初始化数据 */
	public Hero() {
		life = 3; // 初始3条命
		doubleFire = 0; // 初始火力为0
		images = new BufferedImage[] { MerryChristmas.hero0, MerryChristmas.hero1 ,MerryChristmas.hero2}; // 英雄机图片数组
		image = MerryChristmas.hero0; // 初始为hero0图片
		width = image.getWidth();
		height = image.getHeight();
		x = 150;
		y = 400;
	}

	/** 获取双倍火力 */
	public int isDoubleFire() {
		return doubleFire;
	}

	/** 设置双倍火力 */
	public void setDoubleFire(int doubleFire) {
		this.doubleFire = doubleFire;
	}

	/** 增加火力 */
	public void addDoubleFire(int num) {
		doubleFire = num;
	}

	/** 增命 */
	public void addLife() { // 增命
		life++;
	}



	/** 获取命 */
	public int getLife() {
		return life;
	}

	/** 当前物体移动了一下，相对距离，x,y鼠标位置 */
	public void moveTo(int x, int y) {
		this.x = x - width / 2;
		this.y = y - height / 2;
	}

	/** 越界处理 */
	@Override
	public boolean outOfBounds() {
		return false;
	}

	/** 发射子弹 */
	public Bullet[] shoot() {
		int xStep = width / 4; // 4半
		int yStep = 20; // 炮弹初始位置与圣诞老人的距离
		if (doubleFire > 0) { // 双倍火力
			doubleFire--;
//			System.out.println("还剩"+doubleFire+"次双倍火力");
			Bullet[] bullets = new Bullet[2];
			bullets[0] = new Bullet(x + xStep+5, y - yStep); // y-yStep(子弹距飞机的位置)
			bullets[1] = new Bullet(x + 3 * xStep-5, y - yStep);
			return bullets;
		} else { // 单倍火力
			Bullet[] bullets = new Bullet[1];
			bullets[0] = new Bullet(x + 2 * xStep, y - yStep);
			
			return bullets;
		}
	}

	/** 50ms下一帧换图 */
	@Override
	public void step() {
		if (images.length > 0) {
			image = images[index++ / 5 % images.length]; // 切换图片hero0，hero1,hero2
		}
	}

	/** 碰撞算法 */
	public boolean hit(FlyingObject other) {

		
		int x1 = other.getX() - this.width / 2; // x坐标最小距离
		int x2 = other.getX() + this.width / 2 + other.width; // x坐标最大距离
		int y1 = other.getY() - this.height / 2; // y坐标最小距离
		int y2 = other.getY() + this.height / 2 + other.height; // y坐标最大距离
		int herox = this.x + this.width / 2; // 英雄机x坐标中心点距离
		int heroy = this.y + this.height / 2; // 英雄机y坐标中心点距离
		return x1 < herox && herox < x2 &&  y1 < heroy && heroy < y2; // 区间范围内为撞上了
	}

}
