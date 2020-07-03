package Code.House;
import Code.House.Charge;
import Code.Common.Basic;
import Code.Common.HTML;

public class CoatOfArms {
  private boolean onMetal;
  private String background;
  private Ordinary ordinary;
  private NonOrdinary nonOrdinary;
  public CoatOfArms() {
    if (Basic.coinFlip()){
      this.onMetal = true;
      this.background = metals[Basic.randint(2)];
    } else {
      this.onMetal = false;
      this.background = colors[Basic.randint(5)];
    }
    this.pickCharge();
  }

  public void pickCharge(){
    switch(Basic.randint(3)){
      case 0:
        this.ordinary = new Ordinary(this.onMetal);
        break;
      case 1:
        this.nonOrdinary = new NonOrdinary(this.onMetal);
        break;
      default:
        this.ordinary = new Ordinary(this.onMetal);
        this.nonOrdinary = new NonOrdinary(this.background, this.ordinary.getTincture());
    }
  }

	public String getHTML(){
		String c;
    String s = "";

    if (this.ordinary != null){
      c = "../Input/CoAs/Charge/Ordinary/"+this.getOrdinaryHTML();
  	  s = HTML.getImgClass(this.ordinary.getTincture()+"_o", c);
    }
    if (this.nonOrdinary != null){
      c = "../Input/CoAs/Charge/NonOrdinary/"+this.getNonOrdinaryHTML();
      s += HTML.getImgClass(this.nonOrdinary.getTincture()+"_o", c);
    }

    s += HTML.getImg("../Input/CoAs/escutcheon.svg");
		s = HTML.getDivClass("CoA "+this.background+"_e", s);
		return s;
	}


  public String getOrdinaryHTML(){
    return this.ordinary.getPath();
  }

  public String getNonOrdinaryHTML(){
    return this.nonOrdinary.getPath();
  }


  //Background is on metal, not color
  public boolean isOnMetal(){
    return this.onMetal;
  }

  private static final String[] metals = {"argent", "or"};
	private static final String[] colors = {"gules", "sable", "azure", "purpure", "vert"};

}
