package Code.House;
import Code.House.House;
import Code.Politics.Holder;
import Code.Human.Human;
import Code.Common.Basic;
import Code.Politics.Office;
import Code.Politics.DynasticOffice;
import Code.calendar.Calendar;
import java.util.List;
import java.util.ArrayList;

public class Dynasty {
  private House house;
  private List<Office> offices;
  private List<Holder> dynasts;
  private List<DynasticOffice> dOffices;
  private Calendar founding;
  private Calendar ending;

  public Dynasty(Holder r){
    House h = r.getPerson().getHouse();
    this.house = h;
    this.offices =  new ArrayList<>();
    this.dOffices = new ArrayList<>();
    this.dynasts =  new ArrayList<>();
    h.setDynasty(this);
    this.addOffice(r.getOffice());
    this.founding = Basic.getDate();
    this.addDynasticOffice(r.getOffice());
    this.addDynast(r, r.getOffice());
  }

  public House getHouse(){
    return this.house;
  }

  public int getOfficeNum(Office h){
    return this.offices.indexOf(h);
  }

  public void addDynasticOffice(Office o){
    this.dOffices.add(new DynasticOffice(o, this));
  }

  public void end(){
    this.ending = Basic.getDate();
  }

  public void switchHouse(House h){
    this.house.resetDynasty();
    this.house = h;
    h.setDynasty(this);
  }


  public void branch(Holder h){
    Dynasty d = new Dynasty(h);
    this.dynasts.remove(h);
    h.getPerson().getHouse().setDynasty(d);
    List<DynasticOffice> l = this.getDynasticOffice();
    for(DynasticOffice x: l){
      x.remove(h);
    }
/*    for(DynasticOffice o: dOffices){
      o.branch(h, d);
    }*/
  }

  //Adjust for obsolute dynasties
  public void adjust(Holder h){
    Dynasty d = h.getDynasty();
    this.dynasts.remove(h);
    for(DynasticOffice x: this.dOffices){
      x.swap(h);
    }
    d.addDynast(h);
  }

  /*public void update(int i){
    Holder h = this.dynasts.get(i);
    Dynasty d = new Dynasty(h);
    this.dynasts.remove(h);
    h.getPerson().getHouse().setDynasty(d);
    for(DynasticOffice o: dOffices){
      o.updateIf(h, d);
    }
  }*/

  public boolean isDynast(Holder h){
    return this.dynasts.contains(h);
  }

  public void addDynast(Holder h, Office o){
    if (this.dynasts.contains(h)){
      throw new RuntimeException();
    }
    this.dynasts.add(h);
    DynasticOffice d = this.getDynasticOffice(o);
    d.addHolder(h);
    if (!d.isActive()){
      this.getDynasticOffice(o).enable();
    }
  }

  public void addDynast(Holder h){
    this.dynasts.add(h);
  }

  public void addOffice(Office o){
    this.offices.add(o);
  }

  //Get holders who lack dynasties as result of newly created cadet dynasties
  public List<Holder> getNonDynastics(){
    List<Holder> l = new ArrayList<>(this.dynasts.size());
    for(Holder x: this.dynasts){
      if (x.getDynasty() == null){
        l.add(x);
      }
    }
    return l;
  }

  //Check if none of the dynasts have correct house
  public boolean isEmpty(){
    for(Holder x: this.dynasts){
      if (x.getDynasty() == this){
        return false;
      }
    }
    return true;
  }

  public Holder getFounder(){
    return this.dynasts.get(0);
  }

  public DynasticOffice getDynasticOffice(Office o){
    return this.dOffices.get(this.getOfficeNum(o));
  }

  public DynasticOffice getDynasticOffice(int i){
    return this.dOffices.get(i);
  }

  public List<DynasticOffice> getDynasticOffice(){
    return new ArrayList<>(this.dOffices);
  }

  public String getName(){
    return this.getHouse().getName()+" dynasty";
  }

  public List<Holder> getDynasts(){
    return new ArrayList<>(this.dynasts);
  }

  public List<Human> getDynastsPerson(){
    List<Human> l = new ArrayList<>(this.dynasts.size());
    for(Holder x: this.dynasts){
      l.add(x.getPerson());
    }
    return l;
  }

  public Calendar getFounding(){   return this.founding;  }
  public Calendar getEnding(){     return this.ending;     }

  public int getTenure(){
    int i = Basic.getDaysBetween(this.getEnding(), this.getFounding());
    return Basic.getYears(i);
  }

}
