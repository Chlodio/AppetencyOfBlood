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
    BAR("Bar", new String[]{"two", "three", "wavy_two", "wavy_three", "gemel_two"}),
    BARRY("Barry", new String[]{"five", "eight", "ten"}),
    BEND("Bend", new String[]{"dexter", "sinister", "cotised_dexter", "cotised_sinister"}),
    BENDLET("Bendlet", new String[]{"dexter", "sinister"}),
    CANTON("Canton", new String[]{"dexter", "sinister"}),
    CHEVRON("Chevron", new String[]{"plain", "inverted", "interlaced"}),
    CHIEF("Chief", new String[]{"plain", "triangular", "enarched", "double-arched"}),
    CROSS("Cross", new String[]{"plain", "quarter-pierced", "parted_fretted", "coldharbour", "fusils"}),
    FESS("Fess", new String[]{"plain", "cottised", "dancetty", "doubly_cottised"}),
    FLAUNCHES("Flaunches", new String[]{"plain"}),
    GYRON("Gyron", new String[]{"dexter", "sinister"}),
    ORLE("Orle", new String[]{"plain", "bordure"}),
    PALE("Pale", new String[]{"plain", "chief"}),
    PALL("Pall", new String[]{"plain", "inverted", "shakefork"}),
    QUARTER("Quarter", new String[]{"dexter", "sinister"}),
    SALTIRE("Saltire", new String[]{"plain", "couped"}),
    TIERCE("Tierce", new String[]{"dexter", "sinister"}),
		PILES("Piles", new String[]{"four", "ten"});

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
