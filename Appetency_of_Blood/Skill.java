public class Skill {
	private int martial;
	private int charisma;
	private int finance;
	private int stewardship;
	private int guile;

	public Skill(Ruler r){
		Interest i			=	r.getInterest();
		int max 			= 	11-i.getHedonsim();
		this.martial 		= 	(Main.randint(max)+i.getImperialism())/2;
		this.charisma		= 	(Main.randint(max)+i.getDiplomacy())/2;
		this.finance 		= 	(Main.randint(max)+i.getEconomy())/2;
		this.stewardship 	=	(Main.randint(max)+i.getJudiacry())/2;
		this.guile 			= 	(Main.randint(max)+i.getStability())/2;
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
