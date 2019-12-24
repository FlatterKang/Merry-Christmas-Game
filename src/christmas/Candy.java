package christmas;

import java.util.Random;

/** �ǹ��ࣨ������ */
public class Candy extends FlyingObject implements Award {
	private int xSpeed = 1; // x�����ƶ��ٶ�
	private int ySpeed; // y�����ƶ��ٶ�
	private int awardType; // ��������

	/** ��ʼ������ */
	public Candy() {
		Random rand = new Random();
		this.image = MerryChristmas.candy[rand.nextInt(4)];
		
		this.ySpeed = rand.nextInt(5)+1;
		width = image.getWidth();
		height = image.getHeight();
		y = -height;
		x = rand.nextInt(MerryChristmas.WIDTH - width + 1);
		awardType = rand.nextInt(2); // ��ʼ��ʱȷ����������
	}

	/** ��ý������� */
	public int getType() {
		return awardType;
	}

	/** Խ�紦�� */
	@Override
	public boolean outOfBounds() {
		return y > MerryChristmas.HEIGHT;
	}

	/** �ǹ��ƶ� */
	@Override
	public void step() {
		x += xSpeed;
		y += ySpeed;
		if (x > MerryChristmas.WIDTH - width) {
			xSpeed = -1;
		}
		if (x < 0) {
			xSpeed = 1;
		}
	}
}