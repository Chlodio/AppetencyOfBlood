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

	public static final String[] order = new String[]{"ace", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten", "semy"};

  public enum Type{
    CROSS("Cross", new String[]{
      "annulets_braced", "coptic", "mascles", "annulety", "crescenty", "patonce", "avellane", "doubled", "patriarchal", "bottony", "formerly_fitchy", "pomelly", "bowen", "formerly", "pommeled", "calvatry", "let", "potent", "canterbury", "lorraine", "jerusalem", "latin", "norse_sun"
    }),
		CROWN("Crown", order),
		SPADE("Spade", order),
		BILLET("Billet", order),
		CROSSCOUPED("Cross_couped", order),
		DIAMOND("Diamond", order),
		HEART("Heart", order),
		ROUNDEL("Roundel", order),
		STAR("Star", order),
		ITEM("Item", new String[]{"wheel", "anchor", "chain", "pillar", "ladder", "label"}),
		FEATURE("Feature", new String[]{"ford", "mount", "wall", "sun", "moon"}),
		ESCUTCHEON("Escutcheon", order);

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
