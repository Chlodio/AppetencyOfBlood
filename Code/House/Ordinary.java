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
    BARRY("Barry", new String[]{"of_five", "of_eight", "of_ten"}),
    BEND("Bend", new String[]{"dexter", "sinister", "cotised_dexter", "cotised_sinister"}),
    CHEVRON("Chevron", new String[]{"plain", "inverted", "interlaced"}),
    CHIEF("Chief", new String[]{"plain", "triangular", "enarched", "double-arched"}),
    CROSS("Cross", new String[]{"plain", "quarter-pierced", "parted_fretted", "coldharbour", "fusils"}),
    FESS("Fess", new String[]{"plain", "cottised", "dancetty", "doubly_cottised"}),
    FLAUNCHES("Flaunches", new String[]{"plain", "flasque", "voider"}),
    GORE("Gore", new String[]{"dexter", "sinister"}),
    PALE("Pale", new String[]{"plain", "chief", "of_three", "of_four", "of_five"}),
    PALL("Pall", new String[]{"plain", "inverted", "shakefork"}),
    SALTIRE("Saltire", new String[]{"plain", "couped"}),
    TIERCE("Tierce", new String[]{"dexter", "sinister"}),
		FRET("Fret", new String[]{"plain"}),
		BASE("Base", new String[]{"plain", "doventailed", "wavy", "embattled"}),
		FUR("Fur", new String[]{"ermine", "vair"}),
		CHEQUY("Chequy", new String[]{"plain"}),
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
