package Succession;

import Ancestry.*;
import Human.Human;
import java.util.ArrayList;
import java.util.List;

/*Succession law for lineage*/
public class SucLaw{

	private Lineage lineage;
	/*Recipes
		sex 1 + preference 1 + tracing 1 + purity 2 = 		agnatic primogeniture
		sex 0 + preference 1 + tracing 0 + purity 2 = 		agnatic-cognatic primogeniture
		sex 1 + preference -1 + tracing 0 + purity 1 =		female-exclusion agnatic-preference cognatic primogeniture
		sex 0 + preference 1 + tracing 0 + purity 1 = 		male-preference cognatic primogeniture

		sex 1 + preference -1 + tracing 0 + purity 1 =		agnatic uterine primogeniture

		sex 0 + preference 0 + tracing 0 + purity 0 = 		absolute cognatic primogeniture
		sex -1 + preference -1 + tracing -1 + purity 0 = 	enatic primogeniture

	*/

	private int sex; 			/*0 = cognatic, 1 = agnatic, -1 = enatic	*/
	private int preference;		/*0 = none, 1 = male, -1 = female*/
	private int tracing;		/*0 = cognatic 1 = agnatic, -1 = -enatic 	*/
	private int bastardy;		/*0 = banned, 1 = ultimate, 2 = ulterior, 3 = proximate, 4 = absolute*/
	/*
		ulmate: 	succeeds only upon complete extinction
		ulterior: 	succeed before ultimate
		proximate: 	only second to legimates
		absolute: 	equal to legimates
	*/
	private int purity;
	/*
		0:	core principle matters more than anything, i.e in primogeniture, daughter's son goes before her younger brother, unless tracing is agnatic
		1:	core principle is respected, but takes second place, i.e in primogeniture, holder's all sons (and their sons) will be put before the holder's daughters (and their children)
		2:	core principle is only consulted when obsecure case presents itself
	*/


	private static List<Human> transmitters = new ArrayList<>(10);


	public SucLaw(Lineage l){
		this.lineage =		l;
		this.sex = 			1;
		this.preference = 	0;
		this.tracing =		1;
		this.purity = 		2;
		this.bastardy = 	0;
	}

	public List<Human> getHeirGroup(Human h){
		List<Human> l;
		switch(this.sex){
			case 1:
				l = h.getSons();
				break;
			case 0:
				l = this.getPreferenceGroup(h);
				break;
			default:
				l =  h.getDaughters();
		}
		l.remove(this.getIncumbent());
		return l;
	}

/*both sexes can inherit, hence find preference*/
	public List<Human> getPreferenceGroup(Human h){
		List<Human> l;
		switch(this.preference){
			case 1:
				l = new ArrayList<>(h.getSons());
				l.addAll(h.getDaughters());
				return l;
			case 0:
				return h.getChildren();
			default:
				l = new ArrayList<>(h.getDaughters());
				l.addAll(h.getSons());
				return l;
		}
	}

	public boolean isNaturallyDead(Human h){
		return !h.isLegimate();
	/*	switch(this.bastardy){
			case 0:
				return !h.isLegimate();
			default:
				return false;
		}*/
	}


	//If the person themselves is suited to inherit
	public boolean canInherit(Human h){
		return h.isAlive() && isRightSex(h);
	}


	public boolean shouldInherit(Human h){
		return this.fitsPreference(h);
	}

	public boolean canPass(Human h){
		if (canBeTraced(h)){
			/*If the could immediately succedd, otherwise they are stored for later tracing*/
			if (shouldBeTraced(h)){
				return true;
			} else {
				this.addTransmitter(h);
			}
		}
		return false;
	}

	/*If lineage should be traced through them*/
	public boolean shouldBeTraced(Human h){
		switch(this.purity){
			case 2:
				return this.fitsPreference(h);
			case 1:
				return this.fitsPreference(h);
			default:
				return true;
		}
	}

	public boolean fitsPreference(Human h){
		switch(this.preference){
			case 1:
				return h.isMale();
			case 0:
				return true;
			default:
				return h.isFemale();
		}
	}

	/*If lineage can be traced through them*/
	public boolean canBeTraced(Human h){
		switch(this.tracing){
			case 1:
				return h.isMale();		//paternal lineage
			case 0:
				return true;
			default:
				return h.isFemale();	//maternal lineage
		}
	}

	public boolean isRightSex(Human h){
		switch(this.sex){
			case 1:
				return h.isMale();
			case 0:
				return true;
			default:
				return h.isFemale();
		}
	}



	public boolean hasMalePreference(){	return this.preference == 1;	}

	public boolean isCognatic(){		return this.sex == 0;			}

	public boolean isAgnatic(){			return this.sex == 1;			}

	public boolean isEnatic(){			return this.sex == -1;			}



	public void addTransmitter(Human h){
		this.transmitters.add(h);
	}



	public Human getTransmitter(){					return this.transmitters.get(0);	}


	public List<Human> getTransmitters(){			return new ArrayList<>(this.transmitters); }


	public boolean toleratesSecondaryHeir(){		return this.purity < 2;				}

	public boolean toleratesUltimateHeir(){			return this.purity == 2;			}



	public static void clearTransmitters(){			transmitters.clear();				}

	public static void removeTransmitter(){			transmitters.remove(0);				}

	public Human getIncumbent(){
		return this.lineage.getIncumbent().getPerson();
	}

}
