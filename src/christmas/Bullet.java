package christmas;

/**
 * �ӵ���:�Ƿ�����
 */
public class Bullet extends FlyingObject {
	private int speed = 3; // �ƶ����ٶ�

	/** ����Ĳ������ӵ������ĵ�����
	 * @param x the center of bullet's x
	 * @param y the center of bullet's y */
	public Bullet(int x, int y) {
		// �ӵ���С�����ӵ�������λ��Ϊ�ӵ�����
		this.image = MerryChristmas.bullet;
		this.width = image.getWidth();
		this.height = image.getHeight();
		this.x = x - this.getWidth() / 2;
		this.y = y - this.getHeight() / 2;
	}

	/** �ƶ� */
	@Override
	public void step() {
		y -= speed;
	}

	/** Խ�紦�� */
	@Override
	public boolean outOfBounds() {
		return y < -14;
	}

}
