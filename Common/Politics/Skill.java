package Politics;
import Interest.Interest;
import Common.Basic;
public class Skill {
	private int martial;
	private int charisma;
	private int finance;
	private int stewardship;
	private int guile;

	public Skill(Ruler r){
		Interest i			=	r.getInterest();
		int max 			= 	11-i.getHedonsim();
		this.martial 		= 	(Basic.randint(max)+i.getImperialism())/2;
		this.charisma		= 	(Basic.randint(max)+i.getDiplomacy())/2;
		this.finance 		= 	(Basic.randint(max)+i.getEconomy())/2;
		this.stewardship 	=	(Basic.randint(max)+i.getJudiacry())/2;
		this.guile 			= 	(Basic.randint(max)+i.getStability())/2;
		//majesty			//qyality of being impressive and great
		//legalism			//knowledge of law
	}



//Micro methods

	public int getCharisma(){		return this.charisma;	}
	public int getFinance(){		return this.finance;	}
	public int getGuile(){			return this.guile;		}
	public int getMartial(){		return this.martial;	}
	public int getStewardship(){	return this.stewardship;}

}
