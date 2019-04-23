
public class Flight {
	private int id;
	private String a;
	private String d;
	public Flight() {
		this.id = -1;
		this.a = null;
		this.d = null;
	}
	public Flight(Flight f) {
		this.id = f.id;
		this.a = f.a;
		this.d = f.d;
	}
	public int getId() {
		return this.id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getA() {
		return this.a;
	}
	public void setA(String a) {
		this.a = a;
	}
	public String getD() {
		return this.d;
	}
	public void setD(String d) {
		this.d = d;
	}
}
