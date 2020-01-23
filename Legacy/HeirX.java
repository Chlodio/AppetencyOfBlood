import java.util.List;
import java.util.ArrayList;

public class Heir{
	private static Human heir;

	public static String getAgnaticPrimo(Human i){
		int v = AgnaticPrimo(i);
		switch(v){
			case 2:
				return "Heir apparent "+getHeir().getFormalName();
			case 1:
				return "Heir presumptive "+getHeir().getFormalName();
			default:
				return "No heir";
		}
	}

	public static int AgnaticPrimo(Human i){
		if (AgnaticPrimoA(i)){
			return 2;
		} else if (AgnaticPrimoP(i)){
		 	return 1;
		}
		return 0;
	}

	public static boolean AgnaticPrimoA(Human i){
		if (i.isAdult()){
			List<Human> l = i.getSons();
			for(Human x: l){
				if (x.isLegimate()){
					if (x.isAlive()){
						heir = x;
						return true;
					}
					else if (AgnaticPrimoA(x)){
						return true;
					}
				}
			}
		}
		return false;
	}

	public static boolean AgnaticPrimoP(Human i){
		Human p;
		while(i.hadFather()){
			p = i;
			i = i.getFather();
			List<Human> l = i.getSons();
			l.remove(p);
			for (Human x: l){
				if (AgnaticPrimoA(x)){
					return true;
				}
			}
		}
		return false;
	}


	public static Human getHeir(){
		return heir;
	}
}
