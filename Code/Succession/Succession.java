package Code.Succession;
import Code.Human.Human;
import Code.Politics.Office;
import Code.Ancestry.Lineage;

public class Succession {

	public static int lineageI;
	public static Human[] lineageT;
	public static int lineageSecI;
	public static Human[] lineageSecT;
	public static boolean lineal;
	private static int blood;
	static boolean coverture;

	protected Human secondaryHeir;
	protected Lineage lineage;
	protected SucLaw law;
	protected Office office;

	public Succession(Lineage l){
		this.office = l.getOffice();
		this.lineage = l;
		this.law = l.getLaw();
	}

	public int determine(){ return 0; }

	public static void clearLineage(){
		lineageI = 0;
		lineageT = new Human[21];
		coverture = false;
	}

	public static void clearSLineage(){
		lineageSecI = 0;
		lineageSecT = new Human[21];
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
		if (!lineageT[0].isRegnant()){
	//		System.out.println(lineageT[0].getFormalName());
	//		throw new RuntimeException();
		}
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
		this.determineBlood();
	}

	public void setLineage(Human[] a){
		Lineage.lineageZ = new Human[a.length];
		System.arraycopy(a, 0, Lineage.lineageZ, 0, a.length);
		this.determineBlood();
	}

	public void installSecondaryHeir(){
		promoteLineage();
		this.setHeir(this.getSecondaryHeir());
		this.setPriority(2);
		this.setBlood(1);
	}

	public void determineBlood(){

		//If priority is 3 it means there is no lineage and the blood hardly matters
		if (lineageI > 1){
			int n = this.countNumOfWomen();
			if (n == 0){
				this.setBlood(0);			//Agnatic
			} else if (n == 1){
				this.setBlood(1);			//Quasi-agnatic
			} else {
				this.setBlood(2);			//Cognatic
			}
		} else {
			this.setBlood(0);
		}
	}

	//Count the number of women in the lineage for the sake determining blood type
	public int countNumOfWomen(){
		int i = 0;
		Human[] a = this.getHumanLineage();
		for(int x = 0; x < lineageI;x++){
			if (a[x].isFemale()){
				i++;
			}
		}
		return i;
	}

	public void setPriority(int v){
		if (this.lineage.getPriority() == -1){
			this.lineage.setPriority(v);
		}
	}

	public void resetPriority(){
		this.lineage.setPriority(-1);
	}

	public int getPriority(){
		return this.lineage.getPriority();
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

	public static void resetBlood(){
		blood = 0;
	}

	public static void setCoverture(){
		coverture = true;
	}

	public static void setBlood(int i){
		blood = i;
	}

	public static int getBlood(){
		return blood;
	}

	public Human[] getHumanLineage(){
		return this.lineageT;
	}

	public Lineage getLineage(){
		return this.lineage;
	}

	public Office getOffice(){
		return this.office;
	}

}
