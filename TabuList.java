
public class TabuList {
	private int flight1;
	private int gate1;
	private int flight2;
	private int gate2;
	public int getFlight1() {
		return flight1;
	}
	public void setFlight1(int flight1) {
		this.flight1 = flight1;
	}
	public int getGate1() {
		return gate1;
	}
	public void setGate1(int gate1) {
		this.gate1 = gate1;
	}
	public int getFlight2() {
		return flight2;
	}
	public void setFlight2(int flight2) {
		this.flight2 = flight2;
	}
	public int getGate2() {
		return gate2;
	}
	public void setGate2(int gate2) {
		this.gate2 = gate2;
	}
	public TabuList(int flight1, int gate1, int flight2, int gate2) {
		this.flight1 = flight1;
		this.gate1 = gate1;
		this.flight2 = flight2;
		this.gate2 = gate2;
	}
	public TabuList() {
		this.flight1 = -1;
		this.gate1 = -1;
		this.flight2 = -1;
		this.gate2 = -1;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + flight1;
		result = prime * result + flight2;
		result = prime * result + gate1;
		result = prime * result + gate2;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TabuList other = (TabuList) obj;
		if (flight1 != other.flight1)
			return false;
		if (flight2 != other.flight2)
			return false;
		if (gate1 != other.gate1)
			return false;
		if (gate2 != other.gate2)
			return false;
		return true;
	}
}
