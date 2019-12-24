package christmas;

import java.util.Random;

/**
 * 敌飞机: 是飞行物，也是敌人
 */
public class Bomb extends FlyingObject implements Enemy {

	/** 初始化数据 */
	public Bomb() {
		Random rand = new Random();
		int tmp = rand.nextInt(100);
		if(tmp<3) { // 彩蛋 180*221  16炮打开
			this.image = MerryChristmas.bomb[0];
			life = 16;
			speed = 0.5;
		}else if(tmp < 8) { // 大炸弹108*113 5炮打开
			this.image = MerryChristmas.bomb[1];
			life = 5;
			speed = 1.5;
		}else if(tmp<26) {  // 中炸弹75* 3炮打开
			this.image = MerryChristmas.bomb[2];
			life = 3;
			speed = 2;
		}else { // 49*36 小炸弹 1炮打开
			this.image = MerryChristmas.bomb[3];
			life = 1;
			speed = 3;
		}
		
		width = image.getWidth(); 
		height = image.getHeight();
		theoreticalY = -height; // 初始纵坐标：-36
		theoreticalX = rand.nextInt(MerryChristmas.WIDTH - width + 1); // 最右到达：内容面板宽-飞机宽=400-49=351
//		System.out.println(theoreticalX);
	}

	/** 获取分数 */
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

	/** 越界处理 */
	@Override
	public boolean outOfBounds() {
		return theoreticalY > MerryChristmas.HEIGHT;
	}

	/** 移动 */
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
	 * 检查当前飞行物体是否被子弹(x,y)击(shoot)中
	 * 
	 * @param Bullet 子弹对象
	 * @return true表示被击中了
	 */
	@Override
	public boolean shootBy(Bullet bullet) {
		int x = bullet.x; // x是子弹横坐标
		int y = bullet.y; // y是子弹纵坐标

		return x + bullet.getWidth() / 2 > this.theoreticalX // 子弹中坐标要大于飞行物左边界横坐标
				&& x+ bullet.getWidth() / 2 < this.theoreticalX + width // 子弹中坐标要小于飞行物右边界横坐标
				&& y > this.theoreticalY && y < this.theoreticalY + height;
	}
}
