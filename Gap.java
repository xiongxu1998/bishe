import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.io.BufferedReader;

public class Gap {
	final static int n = 20; //航班数
	final static int g = 5; //登机门数
	final static int count = 5;//转机对数
	final static int popsize = 12;//种群大小
	final static int iteNum = 500; //迭代代数
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
		int[][] w = InitGap.initW(flight); //航班起飞邻接表
		//生成初始解
		
		int[][] gene  = new int[popsize][n];
		for(int i = 0; i < g; i++) {
			gene[i] = InitGap.initGene(n, i, gate, flight);
			for(int j = 0; j<g; j++) {
				gate[j].setId(j);
				gate[j].setGk(-1);
			}
		}	
		for(int i = g; i<popsize; i++) {
			gene[i] = Genetic.variation(gene, 0, f, w);
			if(gene[i] == null)
				i--;
		}
		/*
		for(int i =0; i<gene.length; i++) {
			for(int j = 0; j < gene[0].length; j++)
				System.out.print(gene[i][j]);
			System.out.print("\n");
		}
		*/
		//迭代
		int countHyNum = 0;
		for(int i = 0; i<iteNum; i++) {
			int[][] leftGene = Genetic.choose(gene, popsize/2, trans, dis);
			/*
			for(int l =0; l<leftGene.length; l++) {
				for(int m = 0; m < leftGene[0].length; m++)
					System.out.print(leftGene[l][m]);
				System.out.print("\n");
			}
			*/
			//生成杂交基因
			int[][] hyGene = new int[popsize/2][gene[0].length];
			Random r = new Random();
			while(countHyNum < popsize/2) {
				int p = r.nextInt(leftGene.length-1);
				int q = r.nextInt(leftGene.length-1);
				hyGene[countHyNum] = Genetic.Hybridization(leftGene[p], leftGene[q], f, w);
				countHyNum++;
			}
			countHyNum = 0;
			
			for(int l =0; l<hyGene.length; l++) {
				for(int m = 0; m < hyGene[0].length; m++)
					System.out.print(hyGene[l][m]);
				System.out.print("\n");
			}
			System.out.println("!");
			//生成变异基因
			int[][] varGene = new int[popsize/2][gene[0].length];
			for(int p = 0; p<popsize/2; p++) {
				varGene[p] = Genetic.variation(leftGene, 0, f, w);
				if(varGene[p] == null) {
					p--;
				}
			}
			for(int l =0; l<varGene.length; l++) {
				for(int m = 0; m < varGene[0].length; m++)
					System.out.print(varGene[l][m]);
				System.out.print("\n");
			}
			System.out.println("@");
			//再次选择
			int allNum = leftGene.length + hyGene.length + varGene.length;
			int[][] allGene = new int[allNum][gene[0].length];
			int countAllNum = 0;
			for(int p = 0; p<leftGene.length; p++,countAllNum++)
				allGene[countAllNum] = leftGene[p];
			for(int p = 0; p<hyGene.length; p++,countAllNum++)
				allGene[countAllNum] = hyGene[p];
			for(int p = 0; p<varGene.length; p++,countAllNum++)
				allGene[countAllNum] = varGene[p];
			gene = Genetic.choose(allGene, popsize, trans, dis);
			for(int p =0; p<gene.length; p++) {
				for(int j = 0; j < gene[0].length; j++)
					System.out.print(gene[p][j]);
				System.out.print("\n");
			}
			System.out.println("?");
		}
		System.out.println("最终结果：");
		//选出最终结果
		int[] result = new int[n];
		result = Genetic.choose(gene, 1, trans, dis)[0];
		for(int p = 0; p<result.length; p++)
			System.out.print(result[p]);
		System.out.print("\n");
		System.out.println("最终转机距离为" + Genetic.transCost(result, trans, dis));
		
	}
}
