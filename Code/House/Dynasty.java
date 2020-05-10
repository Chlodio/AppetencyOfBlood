package Code.House;
import Code.House.House;
import Code.Politics.Holder;
import Code.Human.Human;
import Code.Common.Basic;
import Code.Politics.Office;
import Code.Politics.DynasticOffice;
import java.util.Calendar;
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
    System.out.println(h+" "+this.house);
    this.house.resetDynasty();
    this.house = h;
    h.setDynasty(this);
  }


  public void update(int i){
    Holder h = this.dynasts.get(i);

    Dynasty d = new Dynasty(h);
    this.dynasts.remove(h);
    h.getPerson().getHouse().setDynasty(d);
    for(DynasticOffice o: dOffices){
      o.updateIf(h, d);
    }



  }

  public boolean isDynast(Holder h){
    return this.dynasts.contains(h);
  }

  public void addDynast(Holder h, Office o){
    this.dynasts.add(h);
    this.getDynasticOffice(o).addHolder(h);
    this.getDynasticOffice(o).enable();
  }

  public void addOffice(Office o){
    this.offices.add(o);
  }

  public Holder getFounder(){
    return this.dynasts.get(0);
  }

  public DynasticOffice getDynasticOffice(Office o){
    return this.dOffices.get(this.getOfficeNum(o));
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
