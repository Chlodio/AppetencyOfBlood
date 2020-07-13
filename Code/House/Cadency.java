package Code.House;
import Code.Common.Basic;
import Code.Common.HTML;

public class Cadency extends Charge {

	protected Type type;

	public Cadency(boolean isOnMetal){
      super(isOnMetal);
      this.type = Type.values()[Basic.randint(Type.values().length)];
      this.subtype = this.type.pickSubtype();
  }

  public Cadency(String a, String b){
      super(a, b);
      this.type = Type.values()[Basic.randint(Type.values().length)];
      this.subtype = this.type.pickSubtype();
  }

  public String getPath(){
    return this.type.getType()+"/"+this.type.getSubtype(subtype)+".svg";
  }

  public enum Type{

    BORDURE("Bordure", new String[]{"plain", "engrelee"}),
    CANTON("Canton", new String[]{"dexter", "sinister"}),
		QUARTER("Quarter", new String[]{"dexter", "sinister"}),
		ORLE("Orle", new String[]{"plain", "tressure"});

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
