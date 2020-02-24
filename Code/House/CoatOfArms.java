
package Code.House;
import Code.Common.Basic;
import Code.House.Ordinary;
import Code.Common.HTML;

public class CoatOfArms {
	private int metal;
	private int color;
	private String ordinary;
	private boolean onMetal;
	private static final String[] metals = {"argent", "or"};
	private static final String[] colors = {"gules", "sable", "azure", "purpure", "vert"};


	public CoatOfArms(){
		this.metal = Basic.randint(2);
		this.color = Basic.randint(5);
		this.ordinary = Ordinary.getOrdinary();
		this.onMetal = Basic.coinFlip();
	}


	public String getHTML(){
		String c = "../Input/CoAs/"+this.getOrdinary()+".svg";
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
	private int getMetal(){						return this.metal;		}
	private int getColor(){						return this.color;		}
	private String getOrdinary(){			return this.ordinary;	}
	private String fetchMetal(){			return metals[this.getMetal()];}
	private String fethColor(){				return colors[this.getColor()];}
//	private String fethCharge(){			return ordinaries[this.getOrdinary()];}

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
