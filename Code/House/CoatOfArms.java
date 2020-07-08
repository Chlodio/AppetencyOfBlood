package Code.House;
import Code.House.Charge;
import Code.Common.Basic;
import Code.Common.HTML;
import Code.House.House;

public class CoatOfArms {
  private boolean onMetal;
  private String background;
  private Ordinary ordinary;
  private NonOrdinary nonOrdinary;
  private CoatOfArms parency;
  //Portmanteau of parent cadency, if the owner of coat of arms is a cadet house, quarter their new coat of arms with their parent's CoA

  public CoatOfArms(House h) {
    if (Basic.coinFlip()){
      this.onMetal = true;
      this.background = metals[Basic.randint(2)];
    } else {
      this.onMetal = false;
      this.background = colors[Basic.randint(5)];
    }
    this.pickCharge();
    if (h != null){
      this.parency = h.getCoA();
    }
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
    if (this.parency == null){
      return this.getPlainHTML();
    } else {
      return this.getQuarteredHTML();
    }
	}

  public String getPlainHTML(){
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

  public String getQuarteredHTML(){
    String c;
    String s = "";
    String p = this.parency.parencyQuarterForHTML();
    p += this.cadencyQuarterForHTML();
    p += HTML.getImg("../Input/CoAs/escutcheon.svg");
		s = HTML.getDivClass("CoA", p);
    return s;
	}

  public String parencyQuarterForHTML(){
    String s = "";
    String c = "";

    if (this.ordinary != null){
      c = "../Input/CoAs/Charge/Ordinary/"+this.getOrdinaryHTML();
  	  s = HTML.getImgClass(this.ordinary.getTincture()+"_o", c);
    }
    if (this.nonOrdinary != null){
      c = "../Input/CoAs/Charge/NonOrdinary/"+this.getNonOrdinaryHTML();
      s += HTML.getImgClass(this.nonOrdinary.getTincture()+"_o", c);
    }

    c = HTML.getDivClass("qurTopRight "+this.background+"_e", s);
    c += HTML.getDivClass("qurBottomLeft "+this.background+"_e", s);
    return c;
  }


  public String cadencyQuarterForHTML(){
    String s = "";
    String c = "";

    if (this.ordinary != null){
      c = "../Input/CoAs/Charge/Ordinary/"+this.getOrdinaryHTML();
  	  s = HTML.getImgClass(this.ordinary.getTincture()+"_o", c);
    }
    if (this.nonOrdinary != null){
      c = "../Input/CoAs/Charge/NonOrdinary/"+this.getNonOrdinaryHTML();
      s += HTML.getImgClass(this.nonOrdinary.getTincture()+"_o", c);
    }

    c = HTML.getDivClass("qurTopLeft "+this.background+"_e", s);
    c += HTML.getDivClass("qurBottomRight "+this.background+"_e", s);
    return c;
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
