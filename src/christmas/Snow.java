package christmas;
/**
 * @author Kang Boy
 * @version 创建时间：2019年12月21日 下午12:41:33 
 */
public class Snow extends FlyingObject {
	private int speed = 2;
	
	public Snow() {
		this.width = MerryChristmas.snow.getWidth();
		this.height = MerryChristmas.snow.getHeight();
		this.x = 0;
		this.y = -height;
		this.image = MerryChristmas.snow;
	}
	
	@Override
	public boolean outOfBounds() {
		return y > MerryChristmas.HEIGHT;
	}

	@Override
	public void step() {
		y += speed;
	}

}
