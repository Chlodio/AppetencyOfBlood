package Code.History;
import Code.Common.HTML;
import Code.Common.Basic;
import Code.Human.Human;

public class Census{
  public static int living;
  public static int births;
  public static int deaths;

  public static Census[] booking;
  private int year;
  private int people;   //All living humans at given date
  private float men;   //Percentage of women
  private float women; //Percentage of men
  private float birthRate;
  private float deathRate;


  public Census(int year, int men, int women){
    this.year = year;
    this.people = men+women;
    this.men = (men+0.0f)/this.people;
    this.women = (women+0.0f)/this.people;
    this.assesRating();
  }

  public String getHTML(){
    String s = HTML.getTd(""+this.year);
    s += HTML.getTd(""+this.people);
    s += HTML.getTd(Basic.getPercString(this.men));
    s += HTML.getTd(Basic.getPercString(this.women));
    s += HTML.getTd((""+this.birthRate).substring(0,3));
    s += HTML.getTd((""+this.deathRate).substring(0,3));
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

  public static void addBirth(){
    births++;
  }

  public static void addDeath(){
    deaths++;
  }

  public static void addLiving(){
    living += Human.getLiving().size();
  }

  public void assesRating(){
    this.birthRate = births/(living/1000f)/1000*100;
    this.deathRate = deaths/(living/1000f)/1000*100;
    births = 0;
    deaths = 0;
    living = 0;
  }

}
