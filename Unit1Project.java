import java.util.Scanner;
import java.math.*; 
public class Unit1Project {
	//bunch of variables
	static int f = 0;
	static String[][] phase= {{" ", " ", " "}, {" starting ", " final "},{"heating ","cooling "}};
	static double[] specificHeats = {0.002108, 0.004184, 0.001996};
	static double[][] tempBoundaries = {{-273.14, 0}, {0, 100}, {100, 700}};
	static double[] latentHeat = {0.333, 2.257};
	static double[] temp = new double[3];
	static double massOfWater = 0;
	static double energyInFunc = 0;
	static double energyAdded = 0;
	//all changes within phases
	public static double roundTo3(double value) {
	    BigDecimal bd = new BigDecimal(Double.toString(value));
	    bd = bd.setScale(3, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}
	static double TempChanges(double specificHeats[],double temp[],double massOfWater, double energyAdded, String phase[][], double energyInFunc) {
		double tempChange = 0;
		if (phase[0][2].equals("solid")) {f = 0;}
		if (phase[0][2].equals("liquid")) {f = 1;}
		if (phase[0][2].equals("gas")) {f = 2;}
		if (temp[0]<temp[1]) {tempChange=roundTo3((Math.min(temp[1], tempBoundaries[f][1])-Math.max(temp[0], tempBoundaries[f][0])));}
		if (temp[0]>temp[1]) {tempChange=roundTo3((Math.max(temp[1],tempBoundaries[f][0])-Math.min(temp[0], tempBoundaries[f][1])));}
		temp[2]+=tempChange;
		energyInFunc = roundTo3(specificHeats[f]*tempChange*massOfWater);
		return energyInFunc;}
	//solid-liquid, vice versa
	static double SolidPhaseChange(double latentHeat[], double temp[], double massOfWater, double energyAdded, String phase[][], double energyInFunc) {
		energyInFunc = roundTo3(latentHeat[0] * massOfWater);
		phase[0][2] = "liquid";
		if (temp[2]>temp[1]) { 
			energyInFunc *= -1;
			phase[0][2] = "solid";
		}
		return energyInFunc;}
	//liquid-gas, vice versa
	static double GasPhaseChange(double latentHeat[], double temp[], double massOfWater, double energyAdded, String phase[][], double energyInFunc) {
		energyInFunc = roundTo3(latentHeat[1] * massOfWater);
		phase[0][2] = "gas";
		if (temp[2]>temp[1]) { 
			energyInFunc *= -1;
			phase[0][2] = "liquid";
		}
		return energyInFunc;}
	public static void main(String[] args) {
		double Energy = 0;
		Scanner sc = new Scanner(System.in);
		System.out.print("Enter the mass(in grams) of the water: ");
		massOfWater = sc.nextDouble();
		for (int i = 0; i<2; i++) {
			System.out.print("Enter the"+phase[1][i]+"temperature(in Celsius) of the water: ");
			temp[i] = sc.nextDouble();
			if (temp[i] < -273.14) { temp[i] = -273.14;}}
		sc.close();
		temp[2]=temp[0];
		for (int i=0; i<2; i++) {
			if (temp[i] < 0) {phase[0][i] = "solid";}
			if (temp[i] >= 0 && temp[0] <= 100) {phase[0][i] = "liquid";}
			if (temp[i] > 100) {phase[0][i] = "gas";}}
		phase[0][2]=phase[0][0];
		String p1 = "We will be ";
		String p2 = phase[2][0];
		if (temp[0]>temp[1]) {p2 = phase[2][1];}
		String p3 = " grams of water from ";
		String p4 = " Celsius(";
		String p5 = ") to ";
		String p6 = ").";
		System.out.println(p1+p2+massOfWater+p3+temp[0]+p4+phase[0][0]+p5+temp[1]+p4+phase[0][1]+p6);
		while (true) {
			energyAdded = TempChanges(specificHeats,temp,massOfWater, energyAdded,phase, energyInFunc);
			if (energyAdded != 0) {
				Energy += energyAdded;
				System.out.println(p2+"("+phase[0][2]+"): " +energyAdded+" kJ");
			}
			if ((phase[0][2].equals("solid") && temp[2]<temp[1])||(phase[0][2].equals("liquid") && temp[1]<temp[2])) {
				energyAdded = SolidPhaseChange(latentHeat, temp, massOfWater, energyAdded, phase, energyInFunc);
				System.out.println("Changed phase to "+phase[0][2]+": "+energyAdded+" kJ.");
				Energy += energyAdded;
			}
			else if ((phase[0][2].equals("liquid") && temp[2]<temp[1])||(phase[0][2].equals("gas") && temp[1]<temp[2])) {
				energyAdded = GasPhaseChange(latentHeat, temp, massOfWater, energyAdded, phase, energyInFunc);
				System.out.println("Changed phase to "+phase[0][2]+": "+energyAdded+" kJ.");
				Energy += energyAdded;
			}
			if (temp[1]==temp[2]) {break;}
		}
		System.out.println("Total Heat Energy: "+Energy+" kJ");
	}
}