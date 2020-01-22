package Ancestry;
import Human.Human;
import java.util.List;
import java.util.ArrayList;

public class Bastard {
	private static ArrayList<Human> bastards = new ArrayList<>();

	public static void add(Human b){						bastards.add(b); 					}
	public static void remove(Human b){						bastards.remove(b); 				}
	public static int getAmount(){							return bastards.size(); 			}

}
