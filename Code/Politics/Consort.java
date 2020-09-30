package Code.Politics;
import Code.Human.Human;
import Code.Common.Basic;
import Code.Politics.Holder;
import Code.Common.HTML;
import Code.calendar.Calendar;
import java.util.List;

public class Consort {
  private Human person;       //Consort's private persona
  private Calendar beginning; //Date of beginning of the tenure
  private Calendar ending;    //Date of ending of the tenure
  private Holder spouse;      //Ruling spouse
  private boolean isActive;     //If active
  /* Keep track if consort's child became a Holder while they were alive*/

  public Consort(Human p, Holder h){
    this.person = p;
    this.beginning = (Calendar) Basic.getDate().clone();
    this.spouse = h;
    this.isActive = true;
  }

  public Human getPerson(){
    return this.person;
  }

  //Check if the consort correspond to specific human profile
  public boolean isTheSameAs(Human h){
    return this.person == h;
  }

  public void setEnding(){
    if (this.isActive){
      this.ending = (Calendar) Basic.getDate().clone();
      this.isActive = false;
    }
  }

  public Calendar getBeginning(){
    return this.beginning;
  }

  public Calendar getEnding(){
    //If the ending hasn't been set, the consort must still in office
    if (!this.isActive){
        return this.ending;
      } else {
        return Basic.getDate();
    }
  }

  public int getConsortRank(){
    List<Consort> l = this.getSpouse().getOffice().getConsorts();
		for(int x = 0; x < l.size(); x++){
			if (l.get(x) == this){
				return x;
			}
		}
		throw new RuntimeException();
	}

  //From what time span was the consort in power
  public String getTenure(){
    Calendar b = this.getBeginning();
    Calendar e = this.getEnding();
    String s = b.getDateLong();
    s += " – "+e.getDateLong();
    s += "<br>("+Basic.getYearsAndDays(b.getDaysBetween(e))+")";
    return s;
  }

  public Holder getSpouse(){
    return this.spouse;
  }

  public boolean isActive(){
    return this.isActive;
  }
/*
  Checks if multiple consorts share the same spouse, and if so, build td with reused rows, while
*/
  public String getSpouseRow(List<Consort> l, int r){
    //r is position of the current consort
    int n = 1;                        //Number of spouse per the ruling holder
    for (int x = r+1; x < l.size(); x++){
      if (l.get(x).getSpouse() == this.getSpouse()){
        n++;
      } else {
        break;
      }
    }

      if (r == 0 || l.get(r-1).getSpouse() != this.getSpouse()){
        if (n != 1){
          return HTML.getTdRowspan(this.getSpouse().getName(), n);
          //Multi-row outcome
        } else {
          return HTML.getTd(this.getSpouse().getName());
          //Regular outcome for consorts whose spouse only had one consort
        }
      } else {
        return "";
        //No need to get a row because it's already handled by multi-row
      }
  }

  /*
    Checks if multiple consorts are siblings, and if so, build td with reused rows, while
  */
  public String getParentRow(List<Consort> l, int r){
    //r is position of the current consort
    int n = 1;                        //Number of spouses with the same parents
    for (int x = r+1; x < l.size(); x++){
      if (l.get(x).getPerson().isFullSiblingOf(this.getPerson())){
        n++;
      } else {
        break;
      }
    }

    if (r == 0 || !l.get(r-1).getPerson().isFullSiblingOf(this.getPerson())) {
      if (n == 1){
        return HTML.getTd(this.getPerson().getParentsString());
      } else
        return HTML.getTdRowspan(this.getPerson().getParentsString(), n);
    } else {
      return "";
    }
  }

  public String getHouseRow(List<Consort> l, int r){
    //r is position of the current consort
    int n = 1;                    //Number of spouses with the same coat of arms

    if (!this.getPerson().hadFather()){
      return "<td>?</td>";
    }
    for (int x = r+1; x < l.size(); x++){
      if (l.get(x).getPerson().isFromSameHouseAs(this.getPerson())){
        n++;
      } else {
        break;
      }
    }

    if (r == 0 || !l.get(r-1).getPerson().isFromSameHouseAs(this.getPerson())) {
      if (n == 1){
        return HTML.getTdClass("CoAT", this.getPerson().getHouseCoALink());
      } else {
        return HTML.getTdClassRowspan("CoAT", this.getPerson().getHouseCoALink(), n);
      }
    } else {
      return "";
    }
  }

  //Determine the reason why they ceased to be a consort
  public String getTermination(){
    if (!this.isActive()){
      Calendar d = this.getSpouse().getPerson().getDeath();

      if (d != null && this.getPerson().wasAliveIn(d)){
        return this.getPerson().getSpouseTitle()+"'s death";
      } else if (this.getPerson().diedInChildbirth()){
        return "maternal death";
      } else {
        return this.getPerson().getPossessive()+" death";
      }
    } else {
      return "";
    }
  }

  //Build a html table from the data
  public static String getHTML(List<Consort> l){
    String td;   //Table cell
    Human p;        //The consort's person
    //[0] == Name of the th
		//[1] == Name of css class
    int r;   //Position of the line of consorts
    int d;   //Duplicate value, if the consort appears consecutively change to 2

		String[][] th = {{"Name", ""}, {"CoA", "CoAT"}, {"Parents", ""}, {"Lifespan", ""}, {"Tenure", ""}, {"Spouse", ""}, {"Termination", ""}};

		StringBuffer thc = new StringBuffer(HTML.createTableHeaderClass(th));

    for(Consort x: l){
      r = x.getConsortRank();
      p = x.getPerson();
      d = 1;
      if (r == 0 || l.get(r-1).getPerson() != x.getPerson()){
        if (r+1 < l.size() && l.get(r+1).getPerson() == x.getPerson()){
          d = 2;    //Used for rowspans
        }
        td = HTML.getTdRowspan(p.getBirthName(), d);
        td += x.getHouseRow(l, r);
        td += x.getParentRow(l, r);
        td += HTML.getTdRowspan(p.getLifespanWithTime(), d);
        td += HTML.getTd(x.getTenure());
      } else {
        td = x.getHouseRow(l, r);
        td += x.getParentRow(l, r);
        td += HTML.getTd(x.getTenure());
      }
      td += x.getSpouseRow(l, r);
      td += HTML.getTd(x.getTermination());

      thc.append(HTML.getTr(String.valueOf(td)));
    }


    return HTML.getTable(String.valueOf(thc));


  }

}
