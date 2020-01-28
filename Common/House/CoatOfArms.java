/*Currently unused intended to make CoA-system more detailed*/

package House;

public class CoatOfArms {
	private int metal;
	private int color;
	private int ordinary;
	private boolean onMetal;
	private static final String[] metals = {"argent", "or"};
	private static final String[] colors = {"gules", "sable", "azure", "purpure", "vert"};
	private static final String[] ordinary = {"cross", "cross annulety", "cross bottony", "cross calvaltry", "cross coptic"};

	private String getDesc(){
		if (this.isOnMetal()){
			return this.getMetal().capitalize()+", a "+this.getOrdinary()+" "+this.getColor();
		} else {
			return this.getColor().capitalize()+", a "+this.getOrdinary()+" "+this.getMetal();
		}
	}
	private boolean isOnMetal(){			return this.onMetal;	}
	private int getMetal(){					return this.metal;		}
	private int getColor(){					return this.color;		}
	private String getOrdinary(){			return this.ordinary;	}

}
