package christmas;

/**
 * 子弹类:是飞行物
 */
public class Bullet extends FlyingObject {
	private int speed = 3; // 移动的速度

	/** 传入的参数是子弹正中心的坐标
	 * @param x the center of bullet's x
	 * @param y the center of bullet's y */
	public Bullet(int x, int y) {
		// 子弹较小，以子弹的中央位置为子弹坐标
		this.image = MerryChristmas.bullet;
		this.width = image.getWidth();
		this.height = image.getHeight();
		this.x = x - this.getWidth() / 2;
		this.y = y - this.getHeight() / 2;
	}

	/** 移动 */
	@Override
	public void step() {
		y -= speed;
	}

	/** 越界处理 */
	@Override
	public boolean outOfBounds() {
		return y < -14;
	}

}
