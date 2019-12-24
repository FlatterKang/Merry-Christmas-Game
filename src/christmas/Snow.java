package christmas;
/**
 * @author Kang Boy
 * @version ����ʱ�䣺2019��12��21�� ����12:41:33 
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
