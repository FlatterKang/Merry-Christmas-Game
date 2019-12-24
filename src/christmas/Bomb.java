package christmas;

import java.util.Random;

/**
 * �зɻ�: �Ƿ����Ҳ�ǵ���
 */
public class Bomb extends FlyingObject implements Enemy {

	/** ��ʼ������ */
	public Bomb() {
		Random rand = new Random();
		int tmp = rand.nextInt(100);
		if(tmp<3) { // �ʵ� 180*221  16�ڴ�
			this.image = MerryChristmas.bomb[0];
			life = 16;
			speed = 0.5;
		}else if(tmp < 8) { // ��ը��108*113 5�ڴ�
			this.image = MerryChristmas.bomb[1];
			life = 5;
			speed = 1.5;
		}else if(tmp<26) {  // ��ը��75* 3�ڴ�
			this.image = MerryChristmas.bomb[2];
			life = 3;
			speed = 2;
		}else { // 49*36 Сը�� 1�ڴ�
			this.image = MerryChristmas.bomb[3];
			life = 1;
			speed = 3;
		}
		
		width = image.getWidth(); 
		height = image.getHeight();
		theoreticalY = -height; // ��ʼ�����꣺-36
		theoreticalX = rand.nextInt(MerryChristmas.WIDTH - width + 1); // ���ҵ����������-�ɻ���=400-49=351
//		System.out.println(theoreticalX);
	}

	/** ��ȡ���� */
	@Override
	public int getScore() {
		if(this.image == MerryChristmas.bomb[0])
			return 10;
		else if (this.image == MerryChristmas.bomb[1])
			return 5;
		else if ((this.image == MerryChristmas.bomb[2]))
			return 3;
		else
			return 1;
		
	}

	/** Խ�紦�� */
	@Override
	public boolean outOfBounds() {
		return theoreticalY > MerryChristmas.HEIGHT;
	}

	/** �ƶ� */
	@Override
	public void step() {
		theoreticalY += speed;
	}
	
	@Override
	public int getX() {
		return (int)theoreticalX;
	}
	
	@Override
	public void setX(int x) {
		this.theoreticalX = x;
	}
	
	@Override
	public int getY() {
		return (int)theoreticalY;
	}
	
	@Override
	public void setY(int y) {
		this.theoreticalY = y;
	}
	
	/**
	 * ��鵱ǰ���������Ƿ��ӵ�(x,y)��(shoot)��
	 * 
	 * @param Bullet �ӵ�����
	 * @return true��ʾ��������
	 */
	@Override
	public boolean shootBy(Bullet bullet) {
		int x = bullet.x; // x���ӵ�������
		int y = bullet.y; // y���ӵ�������

		return x + bullet.getWidth() / 2 > this.theoreticalX // �ӵ�������Ҫ���ڷ�������߽������
				&& x+ bullet.getWidth() / 2 < this.theoreticalX + width // �ӵ�������ҪС�ڷ������ұ߽������
				&& y > this.theoreticalY && y < this.theoreticalY + height;
	}
}
