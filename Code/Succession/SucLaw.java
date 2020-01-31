package Code.Succession;

import Code.Ancestry.*;
import Code.Human.Human;
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

	private byte sex; 			/*0 = cognatic, 1 = agnatic, -1 = enatic	*/
	private byte preference;		/*0 = none, 1 = male, -1 = female*/
	private byte tracing;		/*0 = cognatic 1 = agnatic, -1 = -enatic 	*/
	private byte bastardy;		/*0 = banned, 1 = ultimate, 2 = ulterior, 3 = proximate, 4 = absolute*/
	/*
		ulmate: 	succeeds only upon complete extinction
		ulterior: 	succeed before ultimate
		proximate: 	only second to legimates
		absolute: 	equal to legimates
	*/
	private byte purity;
	/*
		0:	core principle matters more than anything, i.e in primogeniture, daughter's son goes before her younger brother, unless tracing is agnatic
		1:	core principle is respected, but takes second place, i.e in primogeniture, holder's all sons (and their sons) will be put before the holder's daughters (and their children)
		2:	core principle is only consulted when obsecure case presents itself
	*/


	private static List<Human> transmitters = new ArrayList<>(10);


	public SucLaw(Lineage l){
		this.lineage =		l;
		this.sex = 			1;
		this.preference = 	1;
		this.tracing =		0;
		this.purity = 		2;
		this.bastardy = 	0;
	}

	public List<Human> getHeirGroup(Human h){
		List<Human> l;
		switch(this.getSex()){
			case 1:
				l = this.getShouldGroupAgnatic(h);
				break;
			case 0:
				l = this.getPreferenceGroup(h);		//Possible method 'GroupCognatic', but not real need
				break;
			default:
				l = this.getShouldGroupEnatic(h);
		}
		l.remove(this.getIncumbent());
		return l;
	}

/*both sexes can inherit, hence find preference*/
	public List<Human> getPreferenceGroup(Human h){
		List<Human> l;
		switch(this.getPreference()){
			case 1:
				l = h.getSons();
				l.addAll(h.getDaughters());
				return l;
			case 0:
				return h.getChildren();
			default:
				l = h.getDaughters();
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


	//Determines what group should be checked after checking eligiblity
	public List<Human> getShouldGroupAgnatic(Human h){
		if (this.getTracing() == 1){
			return h.getSons();
		} else {
			return h.getChildren();
		}
	}

	public List<Human> getShouldGroupEnatic(Human h){
		if (this.getTracing() == -1){
			return h.getDaughters();
		} else {
			return h.getChildren();
		}
	}

	/*If lineage should be traced through them*/
	public boolean shouldBeTraced(Human h){
		switch(this.getPurity()){
			case 2:
				return this.fitsPreference(h);
			case 1:
				return this.fitsPreference(h);
			default:
				return true;
		}
	}

	public boolean fitsPreference(Human h){
		switch(this.getPreference()){
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
		switch(this.getTracing()){
			case 1:
				return h.isMale();		//paternal lineage
			case 0:
				return true;
			default:
				return h.isFemale();	//maternal lineage
		}
	}

	public boolean isRightSex(Human h){
		switch(this.getSex()){
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

	public byte getTracing(){						return this.tracing;				}
	public byte getPurity(){						return this.purity;					}
	public byte getPreference(){					return this.preference;				}
	public byte getSex(){							return this.sex;					}


}
