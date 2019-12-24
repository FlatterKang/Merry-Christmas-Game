package christmas;

import java.awt.image.BufferedImage;

/**
 * 飞行物(敌机，蜜蜂，子弹，英雄机)
 */

public abstract class FlyingObject {
	protected int x; // 屏幕像素点x坐标
	protected int y; // 屏幕像素点y坐标
	protected double theoreticalX;  // x坐标精确值
	protected double theoreticalY;  // y坐标精确值
	protected int width; // 宽
	protected int height; // 高
	protected BufferedImage image; // 图片
	protected int life; // 命
	protected double speed; // 每次移动的步长

	public int getLife() {
		return life;
	}
	
	/** 减命 */
	public void subtractLife() { // 减命
		life--;
	}
	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public BufferedImage getImage() {
		return image;
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}

	/**
	 * 检查是否出界
	 * 
	 * @return true 出界与否
	 */
	public abstract boolean outOfBounds();

	/**
	 * 飞行物移动一步
	 */
	public abstract void step();

	/**
	 * 检查当前飞行物体是否被子弹(x,y)击(shoot)中
	 * 
	 * @param Bullet 子弹对象
	 * @return true表示被击中了
	 */
	public boolean shootBy(Bullet bullet) {
		int x = bullet.x; // x是子弹横坐标
		int y = bullet.y; // y是子弹纵坐标

		return x + bullet.getWidth() / 2 > this.x // 子弹中坐标要大于飞行物左边界横坐标
				&& x+ bullet.getWidth() / 2 < this.x + width // 子弹中坐标要小于飞行物右边界横坐标
				&& y > this.y && y < this.y + height;
	}

}
