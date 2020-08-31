package Code.Politics;
import Code.Politics.Office;
import Code.Politics.Holder;
import Code.House.Dynasty;
import Code.Common.Basic;
import java.util.ArrayList;
import java.util.List;
import Code.calendar.Calendar;

public class DynasticOffice{
  private Office office;
  private Dynasty dynasty;
  private boolean isActive;
  private List<Holder> holders;

  public DynasticOffice(Office o, Dynasty d){
    this.office = o;
    this.dynasty = d;
    this.holders = new ArrayList<>();
  }

  public void addHolder(Holder h){
    if (this.holders.contains(h)){
      throw new RuntimeException();
    }
    this.holders.add(h);
  }

  public void enable(){
    this.isActive = true;
    this.office.setDynasty(this.dynasty);
  }

  public void disable(){
    this.isActive = false;
  }

  public void branch(Holder h, Dynasty d){
    this.holders.remove(h);
  }

  public void remove(Holder h){
    this.holders.remove(h);
  }

  public void swap(Holder h){
    if (this.holders.contains(h)){
      this.holders.remove(h);
      h.getDynasty().getDynasticOffice(0).addHolder(h);
    }
  }

  public List<Holder> getHolders(){
    return new ArrayList<>(this.holders);
  }

  public Dynasty getDynasty(){
    return this.dynasty;
  }

  //Most recent ruler
  public Holder getRecent(){
    return this.holders.get(this.holders.size()-1);
  }

  public Holder getFirst(){
    return this.holders.get(0);
  }

  public int getOrder(Holder h){
    return this.holders.indexOf(h);
  }

  public int getNum(){
    return this.holders.size();
  }

  public boolean isActive(){
    return this.isActive;
  }

  //Only one holder
  public boolean hadOnlyOne(){
    return this.holders.size() == 1;
  }

  public int getTenureYears(){
    Calendar c = this.getFirst().getStart();
    int i;
    if (!this.isActive()){
      i = Basic.getDaysBetween(c, this.getRecent().getEnd());
    } else {
      i = Basic.getDaysBetween(c, Basic.getDate());
    }
    return Basic.getYears(i);
  }

  public String getPoeticTenure(){
    int i = this.getTenureYears();
    if (i <= 30 || this.getNum() == 1){
      return " briefly-ruled";
    } else if (i <= 60 || this.getNum() < 3){
      return " short-lived";
    } else if (i >= 120){
      return " long-lasting";
    }
    return "";
  }
}
