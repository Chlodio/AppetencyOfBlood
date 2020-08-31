package Code.House;
import Code.Common.Basic;
import Code.Common.HTML;

public class Ordinary extends Charge {

	protected Type type;

  public Ordinary(boolean isOnMetal){
      super(isOnMetal);
      this.type = Type.values()[Basic.randint(Type.values().length)];
      this.subtype = this.type.pickSubtype();
  }

  public String getPath(){
    return this.type.getType()+"/"+this.type.getSubtype(subtype)+".svg";
  }

  public enum Type{
		BAR("bar", new String[]{"two", "three", "of_two_wavy", "of_three_wavy", "gemel_two"}),
    BEND("Bend", new String[]{"dexter", "sinister", "cotised_dexter", "cotised_sinister", "bendlet_dexter", "bendlet_sinister", "riband_dexter", "riband_sinister", "scarp", "fret"}),
    CHIEF("Chief", new String[]{"plain", "triangular", "enarched", "double-arched"}),
    CROSS("Cross", new String[]{"plain", "dancetty", "quarter-pierced", "parted_fretted", "coldharbour", "fusils"}),
    FESS("Fess", new String[]{"plain", "cottised", "dancetty", "doubly_cottised"}),
    FLAUNCHES("Flaunches", new String[]{"plain", "flasque", "voider"}),
    GORE("Gore", new String[]{"dexter", "sinister"}),
    PALE("Pale", new String[]{"plain", "chief", "dancetty", "dancetty_chief", "of_three", "of_four", "of_five"}),
    PALL("Pall", new String[]{"plain", "inverted", "shakefork"}),
    SALTIRE("Saltire", new String[]{"plain", "couped"}),
    TIERCE("Tierce", new String[]{"dexter", "sinister", "dancetty_dexter", "dancetty_sinister"}),
		BASE("base", new String[]{"plain", "dovetailed", "wavy", "embattled", "engrailed", "indented"}),
		FUR("Fur", new String[]{"ermine", "vair"}),
		GYRON("Gyron", new String[]{"dexter", "sinister"}),
		GYRONNY("Gyronny", new String[]{"of_eight", "of_sixteen"}),
		PILES("Piles", new String[]{"of_four", "of_ten"});

  private String type;
  private String[] subtype;

  private Type(String s, String[] b){
      this.type = s;
      this.subtype = b;
  }

  public String getType(){
    return this.type;
  }

  public String getSubtype(int i){
    return this.subtype[i];
  }

  public int pickSubtype(){
    return Basic.randint(this.subtype.length);
  }

  }
}
