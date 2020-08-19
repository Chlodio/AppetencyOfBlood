package Code.House;
import Code.Common.Basic;
import Code.Common.HTML;

public class NonOrdinary extends Charge {

	protected Type type;

	public NonOrdinary(boolean isOnMetal){
      super(isOnMetal);
      this.type = Type.values()[Basic.randint(Type.values().length)];
      this.subtype = this.type.pickSubtype();
  }

  public NonOrdinary(String a, String b){
      super(a, b);
      this.type = Type.values()[Basic.randint(Type.values().length)];
      this.subtype = this.type.pickSubtype();
  }

  public String getPath(String s){
    return this.type.getType()+"/"+this.type.getSubtype(subtype)+s+".svg";
  }

	public static final String[] order = new String[]{"ace", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten"};

  public enum Type{
		ANNULET("annulet", order),
		BILLET("billet", order),
		CLOVER("clover", order),
		CROSSCOUPED("cross_couped", order),
		CROWN("crown", order),
		DIAMOND("diamond", order),
		ESCUTCHEON("escutcheon", order),
		FEATURE("nature", new String[]{"ford", "mount", "sun", "crescent", "thunderbolt", "cloud"}),
		FLEURDELIS("fleur-de-lis", order),
		FLOWER("flower", order),
		HEART("heart", order),
		ITEM("item", new String[]{"wheel", "anchor", "chain", "bowen_knot", "carbuncle"}),
		ROD("rod", order),
		ROUNDEL("roundel", order),
		SEMY("semy", new String[]{"annulet", "billet", "clover", "cross_couped", "crown", "diamond", "escutcheon", "fleur-de-lis", "heart", "rod", "roundel", "spade", "star"}),
		SPADE("spade", order),
		STAR("star", order),
		WEAPON("weapon", new String[]{"sword", "sword_cross", "bow_and_arrow", "axe", "trident", "morning_star"}),
		STRUCTURE("structure", new String[]{"pillar", "wall", "castle", "tower", "mill"}),
		TOOL("tool", new String[]{"anvil", "ladder", "label", "scythe", "sickle", "hammer",}),
    CROSS("cross", new String[]{"annulets_braced", "coptic", "mascles", "annulety", "crescenty", "patonce", "avellane", "doubled", "patriarchal", "bottony", "formerly_fitchy", "pomelly", "bowen", "formerly", "pommeled", "calvatry", "let", "potent", "canterbury", "lorraine", "jerusalem", "latin", "norse_sun"});
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
