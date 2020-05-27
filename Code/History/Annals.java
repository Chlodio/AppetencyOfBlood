package Code.History;
import Code.History.Annal;
import Code.House.House;
import Code.Common.Basic;
import Code.Human.Human;
import Code.Politics.Holder;

public class Annals {

  private int firstYear;      //When the recording started
  private Annal[] annals;     //Invidual year and its events

  public Annals(int y, int l){
    this.firstYear = y;
    this.annals = new Annal[l*100];
  }

  public void publishAnnal(int i){
    Annal a = this.annals[i-firstYear];
    if (a != null){
      System.out.println(a.getEntry());
    }
  }

  public void writeLegitBirth(Human h){
    if (h.isNoble()){
      Annal a = this.secureEntry();
      String s = String.format("%s was born for %s and %s", h.getFullName(), h.getFather().getFullName(), h.getMother().getFullName());
      a.add(s);
    }
  }

  public void recordExtinction(House h){
    if (h.isNoble()){
      Annal a = this.secureEntry();
      String s = "house of "+h.getName()+" went extinct";
      a.add(s);
    }
  }

  public void recordExtinctSeniorSuc(House d, House c){
    if (d.isNoble()){
      Annal a = this.secureEntry();
      String s = String.format("senior line of %s went extinct, but was succeeded by %s", d.getFullName(), c.getFullName());
      a.add(s);
    }
  }

  public void recordMarriage(Human h, Human w){
    if (h.isNoble()){
      Annal a = this.secureEntry();
      String s = h.getFullName()+" wed "+w.getName().getPatronymic();
      a.add(s);
    }
  }

  public void recordOfficeEntrace(Human h){
      Annal a = this.secureEntry();
      String s = h.getFullName()+" became a great officer";
      a.add(s);
  }

  public void recordAscension(Holder h, String o){
      Annal a = this.secureEntry();
      String s = String.format("%s took the government at the age of %s as %s", h.getClaimName(o), h.getPerson().getAge(), h.getName());
      a.add(s);
  }


  public void recordWidowDeath(Human h, Human w){
      if (h.isNoble()){
        Annal a = this.secureEntry();
        String s = String.format("%s departed, leaving %s a %s",
        h.getFullName(),w.getFullName(), w.widow());
        a.add(s);
      }
  }

  public void recordSingleDeath(Human h){
      if (h.isNoble()){
        Annal a = this.secureEntry();
        String s = String.format("%s deceased", h.getFullName()) ;
        a.add(s);
      }
  }

  public Annal secureEntry(){
    int y = Basic.getDateYear();
    if (this.annalIsEmptryFor(y)){
      return this.createEntryFor(y);
    } else {
      return this.annals[y-firstYear];
    }
  }

  public Annal createEntryFor(int y){
    return this.annals[y-firstYear] = new Annal(y);
  }

  public boolean annalIsEmptryFor(int y){
    return this.annals[y-firstYear] == null;
  }


}
