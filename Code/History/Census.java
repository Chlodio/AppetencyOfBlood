package Code.History;
import Code.Common.HTML;
import Code.Common.Basic;

public class Census{
  public static Census[] booking;
  private int year;
  private int living;   //All living humans at given date
  private float men;   //Percentage of women
  private float women; //Percentage of men

  public Census(int year, int men, int women){
    this.year = year;
    this.living = men+women;
    this.men = (men+0.0f)/this.living;
    this.women = (women+0.0f)/this.living;
  }

  public String getHTML(){
    String s = HTML.getTd(""+this.year);
    s += HTML.getTd(""+this.living);
    s += HTML.getTd(Basic.getPercString(this.men));
    s += HTML.getTd(Basic.getPercString(this.women));
    return HTML.getTr(s);
  }

  //Compiles complete census for writing purposes
  public static String[] write(){
    String[] s = new String[booking.length];
    for(int x = 0; x < booking.length; x++){
      s[x] = booking[x].getHTML();
    }
    return s;
  }

}
