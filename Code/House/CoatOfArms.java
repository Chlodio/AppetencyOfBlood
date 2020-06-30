package Code.House;
import Code.House.Charge;
import Code.Common.Basic;
import Code.Common.HTML;

public class CoatOfArms {
  private boolean onMetal;
  private String background;
  private Ordinary ordinary;

  public CoatOfArms() {
    if (Basic.coinFlip()){
      this.onMetal = true;
      this.background = metals[Basic.randint(2)];
    } else {
      this.onMetal = false;
      this.background = colors[Basic.randint(5)];
    }
    this.ordinary = new Ordinary(this.onMetal);
  }


	public String getHTML(){
		String c = "../Input/CoAs/Charge/Ordinary/"+this.getOrdinaryHTML();
		String s = HTML.getImgClass(this.ordinary.getTincture()+"_o", c);
		s = HTML.getDivClass("escu "+this.background+"_e", s);
		s = HTML.getDivClass("CoA", s);
		return s;
	}


  public String getOrdinaryHTML(){
    return this.ordinary.getPath();
  }

  //Background is on metal, not color
  public boolean isOnMetal(){
    return this.onMetal;
  }

  private static final String[] metals = {"argent", "or"};
	private static final String[] colors = {"gules", "sable", "azure", "purpure", "vert"};

}
