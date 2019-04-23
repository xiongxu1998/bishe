import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;
//import java.util.Map;
//import java.util.HashMap;
import java.util.ArrayList;

public class GenerateDate {
	final static int n = 20;// 航班数
	final static int g = 5;//登机门数
	final static int count = 5;//转机的对数
	public static void main(String[] args) {
		File flight = new File("flight.txt");
		File trans = new File("trans.txt");
		File gate = new File("gate.txt");
		try {
			OutputStream f = new FileOutputStream(flight);
			for(int i = 0; i<n; i++) {
				Random r1 = new Random();
				Random r2 = new Random();
				int aHour = r1.nextInt(23);
				int aMin = r1.nextInt(60);
				int wait = r2.nextInt(30) + 30;
				int dMin = ((aMin + wait) >= 60)? (aMin + wait - 60) : (aMin + wait);
				int dHour = ((aMin + wait) >= 60) ? (aHour+1) : aHour;
				String s = i + " " + aHour + ":" + aMin + " " +
							dHour + ":" + dMin + "\n";
				f.write(s.getBytes());
			}
			f.close();
		} catch (IOException e) {
			System.out.println("Exception: flight");
		}
		try {
			OutputStream f = new FileOutputStream(trans);
			ArrayList<Integer> list = new ArrayList<Integer>();
			Random r3 = new Random();
			boolean[] bool = new boolean[n];
			int num = -1;
			for(int i = 0; i<count*2; i++) {
				do {
					num = r3.nextInt(n);
				}while(bool[num]);
				bool[num] = true;
				list.add(num);
			}
			ArrayList<Integer> transNum = new ArrayList<Integer>();
			for(int i = 0; i<count; i++) {
				transNum.add(r3.nextInt(100)+100);
			}
			String s = new String();
			for(int i = 0; i < count; i++) {
				s = s + list.get(i*2) + " " + list.get(2*i+1)
					+ " " + transNum.get(i) + "\n";
			}
			f.write(s.getBytes());
			f.close();
		} catch (IOException e) {
			System.out.println("Exception: trans");
		}
		
		try {
			OutputStream f = new FileOutputStream(gate);
			Random r = new Random();
			String s = new String();
			int[][] dis = new int[g][g];
			for(int i = 0; i<g; i++) {
				for(int j = i+1; j<g; j++) {
					dis[i][j] = r.nextInt(800) + 200;
				}
			}
			for(int i = 0; i<g; i++) {
				for(int j = 0; j<g; j++) {
					if(i>j)
						dis[i][j] = dis[j][i];
					else if(i == j)
						dis[i][j] = 0;
					else
						dis[i][j] = dis[i][j];
					s = s + dis[i][j] + "\t";
				}
				s = s + '\n';
			}
			f.write(s.getBytes());
			f.close();
		} catch(IOException e) {
			System.out.println("Exception: gate");
		}
	}
}
