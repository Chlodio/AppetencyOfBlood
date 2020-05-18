package Code.History;
import Code.House.House;
import Code.Common.Basic;
import Code.History.Annals;

public class Annal{
  private String entry;
  private Annals work;
  private int year;
  private int count;      //Number of events

  public Annal(int y){
    this.year = y;
  }

  public void add(String s){
    if (this.entry == null){
      this.entry = "This year ";
    } else {
      this.entry += " This same year ";
    }
    this.entry += s+".";
  }

  public String getEntry(){
    return this.year+"\t"+this.entry;
  }

}
