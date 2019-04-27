import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
//import java.util.ArrayList;

public class Tabu {
	final static int n = 100; //航班数
	final static int g = 10; //登机门数
	final static int count = 15;//转机对数
	final static int ite = 100; //迭代次数
	final static int tabulength = 10; //禁忌表的长度
	public static void main(String[] args) {
		//读取航班文件信息
		Flight[] flight = new Flight[n];
		try {
			BufferedReader f = new 
					BufferedReader(new FileReader("flight.txt"));
			String reader = new String();
			String[] sList = null;
			for(int i = 0; i<n; i++) {
				flight[i] = new Flight();
				if((reader = f.readLine()) != null) {
					sList = reader.split(" ");
				}
				flight[i].setId(Integer.parseInt(sList[0]));
				flight[i].setA(sList[1]);
				flight[i].setD(sList[2]);
			}
			f.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Exception: flight!");
		}
		//初始化登机门数据   登机门id简化为自然数
		Gate[] gate = new Gate[g];
		for(int i = 0; i<g; i++) {
			gate[i] = new Gate();
			gate[i].setId(i);
			gate[i].setGk(-1);
		}
		//读取登机门间间距数据
		int[][] dis = new int[g][g];
		try {
			BufferedReader f = new BufferedReader(new FileReader("gate.txt"));
			String reader = new String();
			String[] sList = null;
			for(int i = 0; i<g; i++) {
				if((reader = f.readLine()) != null)
					sList = reader.split("\t");
				for(int j = 0; j<g; j++) {
					//System.out.print(sList[j]);
					dis[i][j] = Integer.parseInt(sList[j]);
				}
			}
			f.close();
		} catch(IOException e){
			e.printStackTrace();
			System.out.println("Exception: gate!");
		}
 		//读取转机人数数据
		Trans[] trans = new Trans[count];
		try {
			BufferedReader f = new BufferedReader(new FileReader("trans.txt"));
			String reader = new String();
			String[] sList = null;
			for(int i=0; i<count; ++i) {
				reader = f.readLine();
				if(reader != null)
					sList = reader.split(" ");
				trans[i] = new Trans();
				trans[i].setFrom(Integer.parseInt(sList[0]));
				trans[i].setTo(Integer.parseInt(sList[1]));
				trans[i].setNum(Integer.parseInt(sList[2]));
			}
			f.close();
		} catch(IOException e) {
			e.printStackTrace();
			System.out.println("Exception: trans!");
		}
		int[][] f = new int[n][n];
		for(int i = 0; i<n; i++)
			for(int j = 0; j<n; j++)
				f[i][j] = 0;
		for(int i = 0; i<count; i++) {
			f[trans[i].getFrom()][trans[i].getTo()] = trans[i].getNum();
			f[trans[i].getTo()][trans[i].getFrom()] = trans[i].getNum();
		}
		flight = InitGap.initFlight(flight);//对飞机按起飞时间进行排序
		int[] gene = InitGap.initGene(n, 0, gate, flight);
		int[] result = tabu(gene, flight, trans,dis);
		System.out.println("最终转机距离为" + Genetic.transCost(result, trans, dis));
	}
	
	public static int[] tabu(int[] gene, Flight[] flight, Trans[] trans, int[][] dis) {
		ArrayList<TabuList> tabuList = new ArrayList<TabuList>();
		for(int i = 0; i<ite; i++) {
			Random r = new Random();
			int chgFli = r.nextInt(gene.length-1);
			ArrayList<Integer> L = new ArrayList<Integer>();
			L = search(gene, chgFli, flight);
			int[][] tabuGene = new int[L.size()][gene.length] ;
			for(int j = 0; j<100 && j<L.size(); j++) {
				for(int k = 0; k<gene.length; k++)
					tabuGene[j][k] = gene[k];
				int beChgFli = L.get(j);
				TabuList tl = new TabuList();
				if(beChgFli < 0) {
					tl.setFlight1(chgFli);
					tl.setGate1(-1-beChgFli);
					tl.setFlight2(-1);
					tl.setGate2(gene[chgFli]);
				}
				else {
					tl.setFlight1(chgFli);
					tl.setGate1(gene[beChgFli]);
					tl.setFlight2(beChgFli);
					tl.setGate2(gene[chgFli]);
				}
				int indexOfTabulist;
				if(beChgFli < 0) { //直接移动
					if((indexOfTabulist = searchTabuList(tabuList, tl)) == -1) { //不在禁忌表中
						tabuGene[j][chgFli] = -1 - beChgFli;
						if(tabuList.size() < tabulength) //禁忌表未满
							tabuList.add(tl);
						else { // 长度已满
							tabuList.remove(0);
							tabuList.add(tl);
						}
					} // 在禁忌表中  判断是否解禁
					else {
						int[] tempGene = new int[gene.length];
						for(int m = 0; m<gene.length; m++)
							tempGene[m] = gene[m];
						tempGene[chgFli] = -1 - beChgFli;
						if(Genetic.transCost(tempGene, trans, dis) < Genetic.transCost(gene, trans, dis))
						{
							tabuList.get(indexOfTabulist).setFlight1(-1);
							tabuList.get(indexOfTabulist).setFlight2(-1);
							tabuList.get(indexOfTabulist).setGate1(-1);
							tabuList.get(indexOfTabulist).setGate2(-1);
							tabuGene[j][chgFli] = -1 - beChgFli;
						}
					}
				} // 交换位置
				else {
					if((indexOfTabulist = searchTabuList(tabuList,tl)) == -1) { //交换不在禁忌表中
						int temp = tabuGene[j][chgFli];
						tabuGene[j][chgFli] = tabuGene[j][beChgFli];
						tabuGene[j][beChgFli] = temp;
						if(tabuList.size() < tabulength)
							tabuList.add(tl);
						else {
							tabuList.remove(0);
							tabuList.add(tl);
						}
					} // 在禁忌表中
					else {
						int[] tempGene = new int[gene.length];
						for(int m = 0; m<gene.length; m++)
							tempGene[m] = gene[m];
						tempGene[chgFli] = -1 - beChgFli;
						if(Genetic.transCost(tempGene, trans, dis) < Genetic.transCost(gene, trans, dis))
						{
							tabuList.get(indexOfTabulist).setFlight1(-1);
							tabuList.get(indexOfTabulist).setFlight2(-1);
							tabuList.get(indexOfTabulist).setGate1(-1);
							tabuList.get(indexOfTabulist).setGate2(-1);
							int temp = tabuGene[j][chgFli];
							tabuGene[j][chgFli] = tabuGene[j][beChgFli];
							tabuGene[j][beChgFli] = temp;
						}
					}
				}
			}
			gene = Genetic.choose(tabuGene, 1, trans, dis)[0];
		}
		return gene;
	}
	private static int searchTabuList(ArrayList<TabuList> tabuList, TabuList tabu) {
		for(int i = 0; i<tabuList.size(); i++) {
			if(tabuList.get(i).equals(tabu) == true)
				return i;
		}
		return -1;
	}
	private static ArrayList<Integer> search(int[] gene, int k, Flight[] flight) {
		ArrayList<Integer> L = new ArrayList<Integer>();
		boolean[] bool = new boolean[g]; 
		for(int i = 0; i<gene.length; i++) {
			if(gene[i] != gene[k]) {
				if(whatSi(gene,i,k,flight) == 0) {
					L.add(i);
				}
				else if(whatSi(gene,i,k,flight) == 1) {
					bool[gene[i]] = true;
				}
				else {
				}
			}
		}
		for(int i = 0; i<g; i++) {
			if(bool[i] == false)
				L.add(-1-gene[i]);
		}
		return L;
	}
	private static int whatSi(int[] gene,int i, int k, Flight[] flight) {
		int iPreTime = InitGap.timeToInt(getPreTime(gene, i, flight));
		int iATime = InitGap.timeToInt(flight[i].getA());
		int iNextTime = InitGap.timeToInt(getNextTime(gene,i,flight));
		int iDTime = InitGap.timeToInt(flight[i].getD());
		int kPreTime = InitGap.timeToInt(getPreTime(gene,k,flight));
		int kATime = InitGap.timeToInt(flight[k].getA());
		int kNextTime = InitGap.timeToInt(getNextTime(gene,k,flight));
		int kDTime = InitGap.timeToInt(flight[k].getD());
		if(kPreTime <= iATime && kNextTime >= iDTime 
				&& kATime >= iPreTime && kDTime <= iNextTime)
			return 0;
		else if( (iATime > kATime && iATime < kDTime && iDTime > kDTime) 
				|| (iDTime >kATime && iDTime <kDTime && iATime < kATime))
			return 1;
		else
			 return 0;
	}
	private static String getPreTime(int[] gene, int k, Flight[] flight) {
		String result = "0:0";
		int i;
		for(i = k - 1; i >= 0; i--) {
			if(gene[i] == gene[k]) {
				result = flight[i].getD();
				break;
			}
		}
		if(i == -1)
			result = "0:0";
		return result;
	}
	
	private static String getNextTime(int[] gene, int k, Flight[] flight) {
		String result = "24:0";
		int i;
		for(i = k + 1; i < gene.length; i++) {
			if(gene[i] == gene[k]) {
				result = flight[i].getA();
				break;
			}
		}
		if(i == gene.length)
			result = "24:0";
		return result;
	}
}
