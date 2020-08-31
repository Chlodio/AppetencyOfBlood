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
		BARRY("barry", new String[]{"of_five", "of_eight", "of_ten", "of_five_wavy", "of_eight_wavy", "of_ten_wavy"}),
		CHEQUY("chequy", new String[]{"plain"});

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
