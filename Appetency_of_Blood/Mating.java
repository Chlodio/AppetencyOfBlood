import java.util.List;
import java.util.ArrayList;

class Mating{
	private static List<Human> retiree = new ArrayList<>(); 	//people going to be retired
	final private static double[] aging = {0.5, 0.25, 0.5, 0.75, 1.0, 1.25, 1.5};
	//influences age of singles
	private static int virgins = 0;

	public static int revaluateM(Human single){
		int age = single.getAge();
		//Lower means higher chance
		if (!single.isMarriageable()){
			retiree.add(single);
			return 100;
		}
		int base = (int) (30.0*aging[age/10]);
		base += 150*single.getLivingSons().size();
		base += 100*single.cadency;
		return base;
	}

	public static int revaluateF(Human single){
		if (!single.isMarriageable()){
			retiree.add(single);
			if(single.isVirgin()){
				virgins++;
			}
			return 100;
		}
		//Higher means higher
		int v = 112-single.getAge();
		if (!single.isVirgin()){
			v /= 2;
		}
		return v;
	}

	public static void retire(){
		for(Human x: retiree){
			x.becomeTaken();
			x.addToElders();
			x.mating = 0;
			x.relSta = 3;
		}
		retiree.clear();
	}

	public static int getVirgings(){					return virgins; }

}
