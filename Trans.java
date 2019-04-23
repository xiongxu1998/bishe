
public class Trans {
	private int from;
	private int to;
	private int num;
	public Trans() {
		from = -1;
		to  = -1;
		num = -1;
	}
	public Trans(int from, int to, int num) {
		//super();
		this.from = from;
		this.to = to;
		this.num = num;
	}
	public int getFrom() {
		return from;
	}
	public void setFrom(int from) {
		this.from = from;
	}
	public int getTo() {
		return to;
	}
	public void setTo(int to) {
		this.to = to;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
}
