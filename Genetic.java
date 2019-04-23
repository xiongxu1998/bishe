import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class Genetic {
	final static double pc = 0.1;//(杂交概率)
	final static double pm = 0.1;//(变异概率)
	final static int popsize = 10;//(种群大小)
	final static int gateNum = 5; //登机门数
	final static int n = 20;
	public static int[] Hybridization(int[] geneA, int[] geneB, int[][] f, int[][] w) {
		int[] child = new int[geneA.length];
		for(int i = 0; i < child.length; i++)
			child[i] = geneA[i];
		Random r = new Random();
		double p = r.nextDouble();
		int k = r.nextInt(geneA.length-1);
		if(left(geneA, k, geneB[k], f, w) == true && 
				right(geneA,k,geneB[k],f, w) == true && p < pc)
			child[k] = geneB[k];
		else
			child[k] = geneA[k]; 
		return child;
	}
	public static int[] variation(int[][] gene, int t, int[][] f, int[][] w) {
		if(gene[t] == null)
		{
			System.out.println("基因为空！");
			return null;
		}
		Random r = new Random();
		int[] child = new int[gene[t].length];
		for(int i = 0 ; i<gene[t].length; i++)
			child[i] = gene[t][i];
		ArrayList<Integer> K = new ArrayList<Integer>();
		int i = r.nextInt(gene[t].length-1);
		for(int j = 0; j<gateNum; j++) {
			if(j != gene[t][i]) {
				if(right(gene[t], i, j, f, w) == true && 
						left(gene[t], i, j, f, w) == true)
					K.add(j);
			}
		}
		double p = r.nextDouble();
		if(p<pm && K.isEmpty() == false) {
			int index = r.nextInt(K.size());
			child[i] = K.get(index);
		}
		else
			return null;
		return child;
	}
	public static int[][] choose(int[][] gene, int leftNum, Trans[] trans, int[][] dis){
		if(gene == null)
			return null;
		int[][] leftGene = new int[leftNum][n];
		int count = 0, min = 0;
		boolean[] bool = new boolean[gene.length];
		int[] tranCost = new int[gene.length];
		for(int i = 0; i<tranCost.length; i++) {
			if(gene[i] != null&& transCost(gene[i],trans,dis) !=0)
				tranCost[i] = transCost(gene[i],trans,dis);
			else
				tranCost[i] = 9999999;
			//System.out.println(tranCost[i]);
		}
		for(count = 0; count<leftNum; count++) {
			for(min = 0; min<gene.length; min++)
				if(bool[min] == false)
					break;
			for(int i = 0; i<gene.length; i++) {
				if(tranCost[i] <= tranCost[min] && bool[i] == false) {
					min = i;
				}
			}
			bool[min] = true;
			//System.out.println(min);
			leftGene[count] = gene[min];
		}
		return leftGene;
	}
	public static int transCost(int[] gene, Trans[] trans, int[][] dis) {
		int result = 0;
		for(int i = 0; i<trans.length; i++) {
			int from = trans[i].getFrom();
			int to = trans[i].getTo();
			result = result + trans[i].getNum() * dis[gene[from]][gene[to]];
		}
		return result;
	}
	private static boolean left(int[] gene, int i, int l,int[][] f, int[][] w) {
		boolean result = true;
		ArrayList<Integer> L = new ArrayList<Integer>();
		for(int j = 0; j<i; j++) {
			if(gene[j] == l)
				L.add(j);
		}
		int flag = 0;
		if(L.isEmpty())
			result = true;
		else {
			Iterator<Integer> it = L.iterator();
			while(it.hasNext()) {
				int j = it.next();
				if(f[j][i] == 0) {
					flag = 1;
					result = (w[i][j] == 1 ? true:false);
					break;
				}
			}
		}
		if(flag != 1)
			result = false;
		return result;
	}
	private static boolean right(int[] gene, int i, int l,int[][] f, int[][] w) {
		boolean result = true;
		ArrayList<Integer> R = new ArrayList<Integer>();
		for(int j = gene.length-1; j>i; j--) {
			if(gene[j] == l)
				R.add(j);
		}
		int flag = 0;
		if(R.isEmpty())
			result = true;
		else {
			Iterator<Integer> it = R.iterator();
			while(it.hasNext()) {
				int j = it.next();
				if(f[j][i] == 0) {
					flag = 1;
					result = (w[i][j] == 1 ? true:false);
					break;
				}
			}
		}
		if(flag != 1)
			result = false;
		return result;
	}
}
