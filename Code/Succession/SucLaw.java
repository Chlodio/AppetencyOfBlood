package Code.Succession;

import Code.Ancestry.*;
import Code.Human.Human;
import java.util.ArrayList;
import java.util.List;

/*Succession law for lineage*/
public class SucLaw{


	public static SucLaw global = new SucLaw();		//Container for non-static object

	private Lineage lineage;
	/*Recipes
		sex 1 + preference 1 + tracing 1  = 		agnatic primogeniture
		sex 0 + preference 1 + tracing 0  = 		agnatic-cognatic primogeniture
		sex 1 + preference -1 + tracing 0 =		female-exclusion agnatic-preference cognatic primogeniture
		sex 0 + preference 1 + tracing 0  = 		male-preference cognatic primogeniture

		sex 1 + preference -1 + tracing 0 =		agnatic uterine primogeniture

		sex 0 + preference 0 + tracing 0 = 		absolute cognatic primogeniture
		sex -1 + preference -1 + tracing -1 = 	enatic primogeniture

	*/


	private byte sex; 			/*0 = cognatic, 1 = agnatic, -1 = enatic	*/
	private byte preference;	/*
		0 = all children will be validated within the order they were born in
		1 = sons will place before daughters despite the birth order
		-1 = daughters will place before sons despite the birth order
	*/

	private byte tracing;		/*0 = cognatic 1 = agnatic, -1 = -enatic 	*/
	private byte bastardy;		/*0 = banned, 1 = ultimate, 2 = ulterior, 3 = proximate, 4 = absolute*/
	/*
		ulmate: 	succeeds only upon complete extinction
		ulterior: 	succeed before ultimate
		proximate: 	only second to legimates
		absolute: 	equal to legimates
	*/
	private byte lastResort;
	/*
		When heir is not found with traditional way, the succession law apply an exception to allow people to succeed who otherwise couldn't
		0 = no last last resort
		1 = semi-last resort, sets sex and tracing to cognatic
		2 = quasi-last resort, sets tracing to cognatic
	*/

	private byte coverture;
	/*
	If the spouse of holder rules alongside them, only applied preference isn't 0
	0 = no coverture
	1 = coverture
	2 = conditional coverture
	*/
	private static List<Human> transmitters = new ArrayList<>(10);


	public SucLaw(Lineage l){
		this.lineage =		l;
		this.sex = 				0;
		this.preference =  1;
		this.tracing =		0;
		this.bastardy = 	0;
		this.lastResort = 0;
		this.coverture = 1;
	}

		public SucLaw(){}

	public static List<Human> getHeirGroup(Human h){
		List<Human> l;

		switch(global.getTracing()){
			case 1:
				l = h.getSons();
				break;
			case 0:
				l = global.getPreferenceGroup(h);
				break;
			default:
				l = h.getDaughters();
		}

		l.remove(global.getIncumbent());
		return l;
	}

/*Both sexes can inherit, hence find preference*/
	public static List<Human> getPreferenceGroup(Human h){
		List<Human> l;
		switch(global.getPreference()){
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

	public static boolean isNaturallyDead(Human h){
		return !h.isLegimate();
	}

	//If the person themselves is suited to inherit
	public static boolean canInherit(Human h){
		return h.isAlive() && global.isRightSex(h);
	}

	//Determines what group should be checked after checking eligiblity
	public static List<Human> getShouldGroupAgnatic(Human h){
		if (global.getTracing() == 1){
			return h.getSons();
		} else {
			return h.getChildren();
		}
	}

	public static List<Human> getShouldGroupEnatic(Human h){
		if (global.getTracing() == -1){
			return h.getDaughters();
		} else {
			return h.getChildren();
		}
	}

	public static boolean fitsPreference(Human h){
		switch(global.getPreference()){
			case 1:
				return h.isMale();
			case 0:
				return true;
			default:
				return h.isFemale();
		}
	}

	/*If lineage can be traced through them*/
	public static boolean canBeTraced(Human h){
		switch(global.getTracing()){
			case 1:
				return h.isMale();		//paternal lineage
			case 0:
				return true;
			default:
				return h.isFemale();	//maternal lineage
		}
	}

	public static boolean isRightSex(Human h){
		switch(global.getSex()){
			case 1:
				return h.isMale();
			case 0:
				return true;
			default:
				return h.isFemale();
		}
	}

	public static void applyLastResort(){
		switch(global.lastResort){
			case 1:
				global.sex = 				0;
				global.tracing = 		0;
				break;
			case 2:
				global.tracing = 		0;
				break;
		}
	}

	public void setAsGlobal(){
		global.lineage = 		this.lineage;
		global.sex = 				this.sex;
		global.preference = this.preference;
		global.tracing = 		this.tracing;
		global.bastardy = 	this.bastardy;
		global.lastResort = this.lastResort;
	}

	public boolean hasMalePreference(){	return this.preference == 1;	}

	public boolean isCognatic(){		return this.sex == 0;			}

	public boolean isAgnatic(){			return this.sex == 1;			}

	public boolean isEnatic(){			return this.sex == -1;			}

	public boolean hasLastResort(){	return this.lastResort != 0;	}

	public Human getIncumbent(){
		return this.lineage.getIncumbent().getPerson();
	}

	public byte getTracing(){						return this.tracing;				}
	public byte getPreference(){				return this.preference;			}
	public byte getSex(){								return this.sex;						}

	public static byte getGlobalTracing(){				return global.tracing;				}
	public static byte getGlobalPreference(){			return global.preference;			}
	public static byte getGlobalSex(){						return global.sex;						}

	public void setTracing(int b){			this.tracing = (byte) b;		}
	public void setPreference(int b){		this.preference = (byte) b;	}
	public void setSex(int b){					this.sex = (byte) b;	}

	public Lineage getLineage(){				return this.lineage;	}


}
