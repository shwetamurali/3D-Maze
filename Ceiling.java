import java.awt.Polygon;
import java.awt.GradientPaint;

public class Ceiling
{
	private int[] x,y;

	public Ceiling(int[] x,int[] y)
	{
		this.x=x;
		this.y=y;
		/*this.r=r;
		this.g=g;
		this.b=b;*/
	}
	public int[] getX()
	{
		return x;
	}
	public int[] getY()
	{
		return y;
	}

	public Polygon getTrap()
	{
		return new Polygon(y,x,x.length);
	}
	/*public GradientPaint getPaint() {
		return new GradientPaint(x[0],y[0],new Color(r,g,b),x[1],y[1], new Color(r-30,g-30,b-30));
	}*/
}
