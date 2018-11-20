import java.awt.Polygon;
public class Wall3D
{
	int[] x;
	int[] y;
	public Wall3D(int[] x, int[] y)
	{
		this.x=x;
		this.y=y;
	}
	public Polygon getPoly()
	{
		return new Polygon(y,x,x.length);
	}



}