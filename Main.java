import java.io.*;
import java.util.*;


public class Main {
	static final int POPULATION_COUNT = 15;
	static final int GENERATION_COUNT = 75;
	static final int ELITE_COUNT = 2;
	static ArrayList<GeneticPlayer> p = new ArrayList<GeneticPlayer>();
	static final Player CONTROL = new GreedyPlayer(2, 2);
	static PrintWriter pr;
	static double maxS = 0.0;
	public static void main (String[] args) throws IOException {
		pr = new PrintWriter("Results.txt");
		pr.println("POPULATION COUNT: " + POPULATION_COUNT);
		pr.println("GENERATION COUNT: " + GENERATION_COUNT);
		pr.println("FITNESS COUNT: " + GeneticPlayer.FITNESS_COUNT);
		initializePop();
		for (int i = 1; i <= GENERATION_COUNT; i++) {
			System.out.println("GENERATION " + i);
			generate(i);
		}
		pr.close();
		System.out.println(maxS);
		
//		GeneticPlayer gp = new GeneticPlayer(1, new double[]{0.40430, 0.00960, 0.94638, 0.88833, 0.20999, 0.83762, 0.18462, 0.28337, 0.38851 ,0.30554});
//		for (int i = 0; i < 100; i++) {
//			gp.calcFitness(CONTROL);
//			System.out.println(gp.fitness);
//		}
		
//		Board b = new Board();
//		double cnt = 0;
//		for (int i = 0; i < 1000; i++) {
//			b.setInit();
//			int res = b.hostGame(new GeneticPlayer(1, new double[]{0.47186, 0.46765, 0.63046, 0.59636, 0.23386, 0.60098, 0.62882, 0.64553, 0.42779}), CONTROL);
//			if (res > 0)
//				cnt++;
//			if (res == 0)
//				cnt += 0.5;
//		}
//		System.out.println(cnt/1000);
	}
	private static void initializePop () throws IOException {
		for (int i = 0; i < POPULATION_COUNT; i++) {
			double[] values = new double[9];
			for (int j = 0; j < 9; j++)
				values[j] = Math.random();	
			p.add(new GeneticPlayer((int)(Math.random()*2+1), values));
			p.get(i).calcFitness(CONTROL);
		}
		pr.println("GENERATION 0");
		Collections.sort(p);
		for (int i = 0; i < POPULATION_COUNT; i++) {
			String weights = "";
			for (int j = 0; j < 9; j++)
				weights += String.format("%.5f", p.get(i).values[j]) + " ";
			pr.printf("%-60s %.10f\n", weights, p.get(i).fitness);
		}
	}
	private static int getRandom () {
		double sum = 0;
		for (GeneticPlayer gp : p)
			sum += gp.fitness;
		double ran = Math.random() * sum;
		double curr = 0.0;
		for (int i = 0; i < POPULATION_COUNT; i++) {
			curr += p.get(i).fitness;
			if (ran < curr)
				return i;
		}
		System.out.println("ERROR");
		return -1;
	}
	private static void generate (int gen) throws IOException {
		ArrayList<GeneticPlayer> np = new ArrayList<GeneticPlayer>();
		double ran = 0;
		double[] values = new double[9];
		for (int i = 0; i < ELITE_COUNT; i++)
			np.add(p.get(i));
		for (int i = 0; i < POPULATION_COUNT - ELITE_COUNT; i++) {
			int x = getRandom();
			int y = getRandom();
			
			for (int j = 0; j < 9; j++) {
				double lo = Math.min(p.get(x).values[j], p.get(y).values[j]);
				double hi = Math.max(p.get(x).values[j], p.get(y).values[j]);
				values[j] = lo + (hi - lo) * Math.random();
				ran = Math.random();
				if (ran < 0.1)
					values[j] = Math.random();
			}
			np.add(new GeneticPlayer((int)(Math.random()*2+1), values));
		}
		for (int i = 0; i < POPULATION_COUNT; i++) {
			np.get(i).calcFitness(CONTROL);
			maxS = Math.max(maxS, np.get(i).fitness);
		}
		p = np;
		pr.println("\nGENERATION "+gen+"\n");
		Collections.sort(p);
		for (int i = 0; i < POPULATION_COUNT; i++) {
			String weights = "";
			for (int j = 0; j < 9; j++)
				weights += String.format("%.5f", p.get(i).values[j]) + " ";
			pr.printf("%-60s %.10f\n", weights, p.get(i).fitness);
		}
	}
}
