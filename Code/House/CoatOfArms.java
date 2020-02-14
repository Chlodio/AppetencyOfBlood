/*Currently unused intended to make CoA-system more detailed*/

package Code.House;
import Code.Common.Basic;
import Code.Common.HTML;

public class CoatOfArms {
	private int metal;
	private int color;
	private int ordinary;
	private boolean onMetal;
	private static final String[] metals = {"argent", "or"};
	private static final String[] colors = {"gules", "sable", "azure", "purpure", "vert"};
	private static final String[] ordinaries = {"cross_plain", "cross_annulety", "cross_bottony", "cross_calvatry", "cross_coptic", "cross_coptic", "cross_couped", "cross_crescenty", "cross_doubled", "cross_Jerusalem", "cross_latin", "cross_Lorraine", "cross_norse_sun", "cross_parted_fretted", "cross_patriarchal", "cross_pomelly", "cross_pommeled", "cross_potent", "cross_quarter-pierced", "cross_avellane", "cross_formerly", "cross_formerly_fitchy", "cross_clechy", "chief_plain", "chief_enarched", "chief_triangular", "chief_double-arched", "chief_pale", "base", "orle", "ford", "quarter", "semy_roundels", "tierce_plain", "tierce_sinister", "wall_plain", "flaunches", "saltire_couped", "saltire", "pall_plain", "pall_inverted", "shakefork", "cross_patonce"};

	public CoatOfArms(){
		this.metal = Basic.randint(2);
		this.color = Basic.randint(5);
		this.ordinary = Basic.randint(ordinaries.length);
		this.onMetal = Basic.coinFlip();
	}


	public String getHTML(){
		String c = "../Input/CoAs/"+this.fethCharge()+".svg";
		String s = HTML.getImgClass(this.getChargeColor()+"_o", c);
		s = HTML.getDivClass("escu "+this.getBackground()+"_e", s);
		s = HTML.getDivClass("CoA", s);
		return s;
	}

	/*private String getDesc(){
		if (this.isOnMetal()){
			return this.getMetal().capitalize()+", a "+this.getOrdinary()+" "+this.getColor();
		} else {
			return this.getColor().capitalize()+", a "+this.getOrdinary()+" "+this.getMetal();
		}
	}*/

	private boolean isOnMetal(){			return this.onMetal;	}
	private int getMetal(){					return this.metal;		}
	private int getColor(){					return this.color;		}
	private int getOrdinary(){				return this.ordinary;	}
	private String fetchMetal(){			return metals[this.getMetal()];}
	private String fethColor(){				return colors[this.getColor()];}
	private String fethCharge(){			return ordinaries[this.getOrdinary()];}

	//The reverse of getBackground
	private String getChargeColor(){
		if (!this.isOnMetal()){
			return this.fetchMetal();
		} else {
			return this.fethColor();
		}
	}

	private String getBackground(){
		if (this.isOnMetal()){
			return this.fetchMetal();
		} else {
			return this.fethColor();
		}
	}


}
