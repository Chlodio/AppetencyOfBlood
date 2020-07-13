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

  public String getPath(){
    return this.type.getType()+"/"+this.type.getSubtype(subtype)+".svg";
  }

	public static final String[] order = new String[]{"ace", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten"};

  public enum Type{
		ANNULET("Annulet", order),
		BENDLET("Bendlet", new String[]{"dexter", "sinister"}),
		BILLET("Billet", order),
		CLOVER("Clover", order),
		CROSSCOUPED("Cross_couped", order),
		CROWN("Crown", order),
		DIAMOND("Diamond", order),
		ESCUTCHEON("Escutcheon", order),
		FEATURE("Feature", new String[]{"ford", "mount", "wall", "sun", "moon"}),
		FLEURDELIS("Fleur-de-lis", new String[]{"ace", "three"}),
		FLOWER("Flower", order),
		HEART("Heart", order),
		ITEM("Item", new String[]{"wheel", "anchor", "chain", "pillar", "ladder", "label", "anvil", "bowen_knot", "carbuncle"}),
		ROD("Rod", order),
		ROUNDEL("Roundel", order),
		SEMY("Semy", new String[]{"annulet", "billet", "clover", "cross_couped", "crown", "diamond", "escutcheon", "fleur-de-lis", "heart", "rod", "roundel", "spade", "star"}),
		SPADE("Spade", order),
		STAR("Star", order),
		WEAPON("Weapon", new String[]{"hammer", "sword", "sword_cross", "scythe", "sickle", "bow_and_arrow", "axe"}),
    CROSS("Cross", new String[]{"annulets_braced", "coptic", "mascles", "annulety", "crescenty", "patonce", "avellane", "doubled", "patriarchal", "bottony", "formerly_fitchy", "pomelly", "bowen", "formerly", "pommeled", "calvatry", "let", "potent", "canterbury", "lorraine", "jerusalem", "latin", "norse_sun"});
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
