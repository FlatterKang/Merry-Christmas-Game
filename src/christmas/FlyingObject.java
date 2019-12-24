package christmas;

import java.awt.image.BufferedImage;

/**
 * ������(�л����۷䣬�ӵ���Ӣ�ۻ�)
 */

public abstract class FlyingObject {
	protected int x; // ��Ļ���ص�x����
	protected int y; // ��Ļ���ص�y����
	protected double theoreticalX;  // x���꾫ȷֵ
	protected double theoreticalY;  // y���꾫ȷֵ
	protected int width; // ��
	protected int height; // ��
	protected BufferedImage image; // ͼƬ
	protected int life; // ��
	protected double speed; // ÿ���ƶ��Ĳ���

	public int getLife() {
		return life;
	}
	
	/** ���� */
	public void subtractLife() { // ����
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
	 * ����Ƿ����
	 * 
	 * @return true �������
	 */
	public abstract boolean outOfBounds();

	/**
	 * �������ƶ�һ��
	 */
	public abstract void step();

	/**
	 * ��鵱ǰ���������Ƿ��ӵ�(x,y)��(shoot)��
	 * 
	 * @param Bullet �ӵ�����
	 * @return true��ʾ��������
	 */
	public boolean shootBy(Bullet bullet) {
		int x = bullet.x; // x���ӵ�������
		int y = bullet.y; // y���ӵ�������

		return x + bullet.getWidth() / 2 > this.x // �ӵ�������Ҫ���ڷ�������߽������
				&& x+ bullet.getWidth() / 2 < this.x + width // �ӵ�������ҪС�ڷ������ұ߽������
				&& y > this.y && y < this.y + height;
	}

}
