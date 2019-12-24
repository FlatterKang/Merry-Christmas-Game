package christmas;

import java.util.Random;

/** 糖果类（奖励） */
public class Candy extends FlyingObject implements Award {
	private int xSpeed = 1; // x坐标移动速度
	private int ySpeed; // y坐标移动速度
	private int awardType; // 奖励类型

	/** 初始化数据 */
	public Candy() {
		Random rand = new Random();
		this.image = MerryChristmas.candy[rand.nextInt(4)];
		
		this.ySpeed = rand.nextInt(5)+1;
		width = image.getWidth();
		height = image.getHeight();
		y = -height;
		x = rand.nextInt(MerryChristmas.WIDTH - width + 1);
		awardType = rand.nextInt(2); // 初始化时确定奖励类型
	}

	/** 获得奖励类型 */
	public int getType() {
		return awardType;
	}

	/** 越界处理 */
	@Override
	public boolean outOfBounds() {
		return y > MerryChristmas.HEIGHT;
	}

	/** 糖果移动 */
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