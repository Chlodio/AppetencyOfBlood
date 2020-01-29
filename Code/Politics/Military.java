package Code.Politics;
import Code.Economy.Treasury;

public class Military{
	private Office owner;
	private int military; 				//Full time professionals
	private int paramilitary; 			//Part time, includes retinues
	private int irregulars;				//Ordinary citizens drafted
	private float qyality;				//How well the army is equiped and trained
	private float prestige;				//How much people value the army
	private float experience;			//How experianced the army
	private double cost;				//The annual cost of the army
	private Treasury funds;				//Funds for war
	Military(Office off){
		this.owner = off;
		this.military = 0;
		this.paramilitary = 1;
		this.irregulars = 0;
		this.qyality = 1.0f;			//0.0–2.0
		this.prestige = 0.0f;			//-1.0–1.0
		this.experience = 0.0f;			//0–1.0				|experiance decays faster than prestige
		this.cost = 0.0;
		this.funds = new Treasury(off, off.getDebt());
	}
	public Office getOwner(){ return this.owner; }
	public int getMilitary(){ return this.military; }
	public int getParamilitary(){ return this.paramilitary; }
	public int getIrregulars(){ return this.irregulars; }
	public float getQyality(){ return this.qyality; }
	public float getPrestige(){ return this.prestige; }
	public float getExperience(){ return this.experience; }
	public double getCost(){  return this.cost;  }
	public Treasury getFunds(){ return this.funds; }
	public double calcCost(){
		double c = Math.round((((this.military*5.0)+(this.paramilitary*2.5))*this.qyality)*100.0)/100.0;
		return c;
	}

}
