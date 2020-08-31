package Code.Human;
import Code.Relationship.SexRelation;
import Code.Human.Human;
import Code.Common.Basic;
import Code.Politics.Office;
import Code.History.Annals;
import java.text.SimpleDateFormat;
import java.util.*;


public class Embryo {
  private SexRelation origin;     //Used figure out the embryo's parentage
  private byte age;               //Months from conception
  private Office office;          //Only used for the posthumous

  public Embryo(SexRelation s){
    this.origin = s;
    this.age = 0;
  }

  public void growEmbryo(){
    this.age++;
    if (this.age == 10){
			int f = Basic.randint(30)+1;
			Basic.dayC.get(f).add(this.getMother());
			Basic.dayE.get(f).add(20);

		}
  }

  public void handlePosthumous(){
    if (this.office != null){
      this.office.manageSuccession();
      Basic.annals.recordInterregnumEnding();
    }
  }

  public void setOffice(Office o){
    this.office = o;
  }


  public SexRelation getOrigin(){ return this.origin; }

  public Human getFather(){       return this.origin.getStag(); }

  public Human getMother(){       return this.origin.getDoe(); }

  public Office getOffice(){      return this.office; }

  public boolean hasOffice(){      return this.office != null; }


}
