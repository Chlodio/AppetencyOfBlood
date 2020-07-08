package Code.House;
import Code.Common.Basic;
import Code.Common.HTML;
import Code.House.Ordinary;

public class Charge {

  protected int subtype;
	protected String tincture;

	private static final String[] metals = {"argent", "or"};
	private static final String[] colors = {"gules", "sable", "azure", "purpure", "vert"};
  private static final String[] tinctures = {"argent", "or", "gules", "sable", "azure", "purpure", "vert"};


	public Charge(boolean isOnMetal){
		if (isOnMetal){
			this.tincture = pickColor();
		} else {
			this.tincture = pickMetal();
		}
	}

  public Charge(String a, String b){
    this.tincture = getUnusedincture(a, b);
	}


	public static String pickMetal(){
		return metals[Basic.randint(2)];
	}

	public static String pickColor(){
		return colors[Basic.randint(5)];
	}

	public String getTincture(){
		return this.tincture;
	}

  //Pick tincture that isn't neither of parameter
  public static String getUnusedincture(String a, String b){
    String n;
    do {
      n = Basic.choice(colors);
    } while (n == a || n == b);
    return n;
  }

}
