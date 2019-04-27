//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.Map;

public class InitGap {
	final int id = 0;//航班id
	final int a = 1;//航班到达时间
	final int d = 2;//航班离开时间
	public static int[][] initW(Flight []flight) {
		int [][]w = new int[flight.length][flight.length];
		for(int i = 0; i<flight.length; i++)
			for(int j = 0; j<flight.length; j++)
				if(compare(flight[i].getA(), flight[j].getD()) == true) {
					w[i][j] = 1;
					w[j][i] = 1;
				}
		return w;
	}
	/*public Map<Integer, Integer> initGate(int n) {
		Map<Integer, Integer> gate = new HashMap<Integer, Integer>();
		for(int i = 0; i<n; i++)
			gate.put(i+1, -1);
		return gate;
	}*/
	public static Flight[] initFlight(Flight []flight) {
		flight = sort(flight);
		return flight;
	}
	public static int[] initGene(int n, int i, Gate[] gate, Flight[] flight) { 
		//n表示航班数，i表示第一个航班选择第i个登机门, gate为登机门信息
		int[] gene = new int[n];
		gene[0] = i;
		gate[i].setGk(timeToInt(flight[0].getD()));
		for(int j = 1; j<n; j++) {
			gene[j] = -1;
			for(int k = 0; k < gate.length; k++) {
				if(timeToInt(flight[j].getA()) > gate[k].getGk())
				{
					gate[k].setGk(timeToInt(flight[j].getD()));
					gene[j] = k;
					break;
				}
			}
		}
		
		for(int j = 0; j<n; j++) {
			if(gene[j] < 0) {
				return null;
			}
		}
		
		return gene;	
	}
	private static Flight[] sort(Flight []flight) {
		Flight tmp = null;
		for(int i=0; i<flight.length-1; i++) {
			for(int j=0; j < flight.length-i-1; j++) {
				if(compare(flight[j].getA(),flight[j+1].getA()) == true) {
					tmp = clone(flight[j]);
					flight[j] = flight[j+1];
					flight[j+1] = tmp;
				}
			}
		}
		return flight;
	}
	private static Flight clone(Flight a) {
		Flight b = new Flight(a);
		return b;
	}
	private static boolean compare(String a, String b) {
		boolean result = false;
		if(timeToInt(a)>=timeToInt(b))
			result = true;
		return result;
	}
	public static int timeToInt(String a) {
		int result = 0;
		String[] s = a.split(":");
		result = Integer.parseInt(s[0]) * 60 + Integer.parseInt(s[1]);
		return result;
	}
}
