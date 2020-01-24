package Succession;
import Human.Human;
import Ancestry.Lineage;

public class Succession {

	public static int lineageI;
	public static Human[] lineageT;
	public static int lineageSecI;
	public static Human[] lineageSecT;
	public static boolean lineal;

	protected Human secondaryHeir;
	protected Lineage lineage;
	protected SucLaw law;

	public Succession(Lineage l, SucLaw s){
		this.lineage = l;
		this.law = s;
	}

	public int determine(){ return 0; }

	public static void clearLineage(){
		lineageI = 0;
		lineageT = new Human[15];
	}

	public static void clearSLineage(){
		lineageSecI = 0;
		lineageSecT = new Human[15];
	}

	public static void lockSecLineage(){
		clearSLineage();
		lineageSecT = lineageT.clone();
		lineageSecI = lineageI;
	}


	public Human getSecondaryHeir(){				return this.secondaryHeir;			}

	public boolean hasSecondaryHeir(){				return this.secondaryHeir != null;	}

	public void resetSecondaryHeir(){				this.secondaryHeir = null;			}

	public void setSecondaryHeir(Human h){			this.secondaryHeir = h;				}

	public static void addToLineage(Human h){
		lineageT[lineageI] = h;
		lineageI++;
	}

	public static void removeFromLineage(){
		lineageI--;
		lineageT[lineageI] = null;
	}

	public static void removeFromSecLineage(){
		lineageSecI--;
		lineageSecT[lineageSecI] = null;
	}

	public static void addToSecLineage(Human h){
		lineageSecT[lineageSecI] = h;
		lineageSecI++;
	}

	public static void promoteLineage(){
		lineageT = lineageSecT.clone();
		lineageI = lineageSecI;
	}

	public static void setLineal(boolean v){
		lineal = v;
	}

	public void setLineage(){
		Lineage.lineageZ = new Human[lineageI];
		System.arraycopy(lineageT, 0, Lineage.lineageZ, 0, lineageI);
	//	return lineageZ;
	}

	public void installSecondaryHeir(){
		promoteLineage();
		this.setHeir(this.getSecondaryHeir());
		this.setPriority(2);
	}

	public void setPriority(int v){
		this.lineage.setPriority(v);
	}

	public void lockAndSetHeir(Human h){
		lockSecLineage();
		this.setSecondaryHeir(h);
	}

	public void setHeir(Human h){
		this.lineage.setHeir(h);
		if (h == null || !h.isAlive() ){
			throw new RuntimeException();
		}
	}

	/*
 	public static Human heir;
	public static ArrayList<Human> lineage = new ArrayList<>();
	protected static boolean coverture;
	protected static Holder origin;
	protected static Human ancestor;
	protected static int blood;
	protected static int special;
	public static void inform(){
		Claim.temp = new Claim();
		Claim.temp.setBlood(blood);
//		Claim.temp.setLineage((ArrayList) lineage.clone());
		Claim.temp.setCoverture(coverture);
		Claim.temp.setSpecial(special);
		Claim.temp.setHolder(heir);
		Claim.temp.setOrigin(origin);
		lineage.clear();
	}

	public static void doMaintenance(){
		Succession.heir = 	null;
		lineage.clear();
		blood = 			0;
		ancestor = 			null;
		Claim.temp = 		null;
		coverture = 		false;
		special = 			0;
		origin =			null;
	}*/
}
