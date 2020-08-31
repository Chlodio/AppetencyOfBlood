package Code.Politics;
import Code.Common.Nick;
import Code.Common.HTML;

import Code.Common.Event;
import Code.Human.Human;
import Code.Relationship.Marriage;
import Code.Politics.*;
import Code.Ancestry.*;
import Code.Common.*;
import Code.House.Dynasty;
import Code.House.House;
import java.util.ArrayList;
import java.util.List;
import Code.calendar.Calendar;
import java.util.HashMap;
import java.util.Map;
import Code.Politics.Consort;
import Code.House.CadetHouse;

public class Holder{
	private ArrayList<Event> events;
	private ArrayList<Ruler> regents;
	private Calendar end;
	private Calendar start;
	private Claim claim;
	private Consort consort;
	private Human person;
	private Office office;
	private Ruler ruler;
	private String name;
	private String notes;
	//private Dynasty dynasty;

	public Holder(Human person, Office o){
		this.person = 	person;
		this.claim = 	new Claim();
		if (o.getLineage().getPriority() < 3){
			this.claim.combine(this, o);
		} else {
			this.claim.setSpecial(1);
		}

		this.start = 	(Calendar) Basic.date.clone();
		this.notes = 	"";
		this.regents =	new ArrayList<>();
		this.events = 	new ArrayList<>();
		this.office = 	o;
		person.addEvent(Event.E101);

		House h = person.getHouse();
		Dynasty d;
		if (h.isDynastic()){
			d = h.getDynasty();
			if (o.hadDynasty(d)){
			} else {
				//If holder is from estabalished dynasty, but hasn't ruled over office
				o.addDynasty(d);
				d.addOffice(o);
				d.addDynasticOffice(o);
				o.addDynasticOffice(d.getDynasticOffice(o));
			}
			if (!d.isDynast(this)){
				d.addDynast(this, o);
			}
		} else {
			//If holder isn't from dynasty create a new dynasty
		 	d = new Dynasty(this);
			h.setDynasty(d);
			o.addDynasty(d);
			d.addOffice(o);
			d.addDynasticOffice(o);
			o.addDynasticOffice(d.getDynasticOffice(o));
		}
		if (o.getDynasticOffices().size() > 1){
			Dynasty d1 = o.getLineage().getPrev().getDynasty();
			if (d1 != this.getDynasty()){
				d1.getDynasticOffice(o).disable();
			}
		}
	}

	public boolean isFounder(){
    return this == this.getDynasty().getFounder();
  }

	public void setConsort(Human c){
		c.setOffice(this.office);
		c.makeConsortOf(this);
	}

	public static Holder regnafy(Human person, Office office){
		Holder holder = new Holder(person, office);
		Territory territory = holder.getOffice().getTerritory();
		holder.name = RegnalName.regnafy(holder, office);
		return holder;
	}

	public void fixTheFirst(){
		if (this.getPerson().isMale()){
			this.name = (this.getPerson().getForename()+" I");
		} else{
			this.name = (this.getPerson().getForename()+" I");
		}
		this.person.getName().setFull(this.getName());
	}

	public String getReign(){
		String x = this.getStartYearStr();
		if(this.hasEnded()){
			if(this.start.getYear() != this.end.getYear()){
				x += "–";
				x = x+this.end.getYear();
			}
			return x;
		}
		else{
			return x+"–PRSN";
		}
	}

	public String getReignLength(){
		String rl = "";
		if(this.hasEnded()){
			rl += Integer.toString((this.end.getYear()-this.start.getYear()));
		}
		else{
			rl += Integer.toString((Basic.date.getYear()-this.start.getYear()));
		}
		return rl;
	}

	public int getReignLengthExact(){
		if (this.hasEnded()){
		 	return Basic.getDaysBetween(this.getStart(), this.getEnd());
		} else {
			return Basic.getDaysBetween(this.getStart(), Basic.getDate());
		}
	}

	public int getReignLengthYears(){
		return this.getReignLengthExact()/360;
	}

	public String getReignYearsAndDays(){
		int d = this.getReignLengthExact();		//Days
		int y = d/360;
		String s = "";
		d -= y*360;
		s = Basic.getPlural(y, "year");
		if (s != ""){
			s += " and ";
		}
		return s + Basic.getPlural(d, "day");
	}

	public void endReign(){
		this.end = (Calendar) Basic.date.clone();
	}

//Nickname methods

	public void handleNickname(){
		Human h = this.getPerson();
		int a = h.getAged();
		//There will be 33.3% a nick name will be used
		if (this.handleDefinedNickname(h, a)){
		} else if (Basic.drawStraws(2)){
			if (a < 14){
				h.getName().setNick(Nick.CHILD);
			} else if (a < 24){
				h.getName().setNick(Nick.YOUNG);
			} else if (a > 65){
				h.getName().setNick(Nick.OLD);
			} else if (this.getReignLengthYears() >= 20){
				h.getName().setNick(Nick.getRandom());
			}
		}
	}

	public boolean handleDefinedNickname(Human h, int a){
		if (a >= 30){
			if (h.wasOnlyChild()){
				h.getName().setNick(Nick.FORTUNATE);
				return true;
			}
		}
		return false;
	}

//Regent methods

	public void appointRegent(){
		Ruler regent = Regent.make();
		this.getOffice().setRuler(regent);
		this.regents.add(regent);
	}

	public Ruler getRegent(){ return this.regents.get(this.regents.size()-1); }

	public String getRegentsHTML(){
		String e = "";
		if (this.regents.size() != 0){
			e += "Regents:<ul>";
			for (Ruler r: this.regents){
				e += "<li>"+r.getPerson().getFullName()+"</li>";
			}
			e += "</ul><br>";
		}
		return e;
	}

	public String getBiography(){
		String s = this.getEarlyLife();
		s += this.getChildhood()+HTML.getBr();
		s += this.getBiographyMarriage()+HTML.getBr();
		s += this.getBiographyReign()+HTML.getBr();
		return s;
	}

	public String getBiographyMarriage(){
		String s = this.getPerson().getMaritalBio();
		return s;
	}

	public String getChildhood(){
		Human h = this.getPerson();
		List<Human>[] l = h.getAllBrothersLivingIn(h.getBirth());
		if (l != null){
			String s = Human.getRelativeStatus("brother", l);
			return s;
		}
		return "";
	}

	public String getEarlyLife(){
		Human h = this.getPerson();
		StringBuffer s =  new StringBuffer(Basic.capitalize(h.getForename()));
		s.append(" was");
		if (h.hadFather()){
			s.append(" born in ");
			s.append(h.getBirth().getDateLong()).append(" as the ");
			s.append(h.getAgnaticOrderStr()).append(" of ");
			s.append(this.getParentNameAge(h.getFather())).append(" and ");
			if (!h.hasSameBirthOrder()){
				s.append("the ").append(h.getEnaticOrderStr()).append(" of ");
			}
			s.append(this.getParentNameAge(h.getMother()));
			if (h.isPosthumous()){
				int d = Basic.getDaysBetween(h.getFather().getDeath(), h.getBirth());
				String ds = Basic.getMonthsOrDays(d);
				s.append(", ").append(ds).append(" from his father's death");
			}
		} else {
			s.append(" born around ").append(h.getBirthYear());
		}
		return String.valueOf(s.append("."));
	}

	public String getParentNameAge(Human p){
		Human h = this.getPerson();
		String s = Basic.getCardinal(p.getAgeIn(h.getBirth()));
		s += "-year-old ";
		s += p.getName().getPatronymic();
		return s;
	}


	public String getBiographyReign(){
		String s = "In "+this.getStartYearStr();
		s += " "+this.getPerson().getPronoun();
		s += " ascended the throne "+this.getAgeOfAscensionS();
		if (this.hasEnded()){
			s += " and reigned the next ";
		} else {
			s += " and has ruled ";
		}
		s += this.getReignYearsAndDays()+".";
		return s+getDynasticStanding();
	}

	public DynasticOffice getDynasticOffice(){
		Dynasty d = this.getDynasty();
		if (d != null){
			return d.getDynasticOffice(this.getOffice());
		} else{
			System.out.println(this.getPerson().getFathersFather().getName().getPatronymic());
			System.out.println(this.getPerson().getHouse().getParent().getDynasty().getDynasts().contains(this));
			System.out.println(this.getPerson().getHouse()+"->"+this.getPerson().getFather().getHouse()+"->"+this.getPerson().getFathersFather().getHouse());
			throw new RuntimeException();
		}
	}

	public boolean wasLastRulerOfDynasty(){
		return this ==this.getDynasticOffice().getRecent();
	}

	public String getDynasticStanding(){
		StringBuffer s = new StringBuffer(" ");
		s.append(this.getName());
		if (this.hasEnded()){
			s.append(" was ");
		} else {
			s.append(" is ");
		}
		DynasticOffice dy = this.getDynasticOffice();
		if (this.isFounder()){
				if (!dy.hadOnlyOne()){
					s.append("the founder of");
					s.append(dy.getPoeticTenure());		//I.e short-lived
				} else {
					s.append("the only ruler from");
				}
		} else {
			s.append("the ").append(Basic.getOrdial(dy.getOrder(this)));
			if (this.hasEnded()){
				if (this.wasLastRulerOfDynasty()){
					s.append(" and last");
				}
			}
			s.append(" ruler from");
		}
		s.append(" ").append(this.getDynasty().getName());
		return String.valueOf(s)+".";
	}

	public String getAgeOfAscensionS(){
		int a = this.getPerson().getDaysIn(this.getStart());
		if (a > 360){
			return "at the age of "+Basic.getCardinal(a/360);
		} else if (a > 30) {
			return "while only "+Basic.getCardinal(a/30)+"-months-old";
		} else if (a > 0){
			return "while barely "+Basic.getCardinal(a)+"-days-old";
		} else {
			return "after being born only few hours priors";
		}
	}

	public boolean hasEnded(){
		return this.end != null;
	}

	public String getStartYearStr(){
		return Integer.toString(this.getStart().getYear());
	}

//Claim

public String getClaimName(String o){
	if (this.getClaim().getLineage() != null){
		int i = this.getClaim().getLineageLength()-2;
		Human[] l = this.getClaim().getLineage();
		String s = o+", "+this.getPerson().getOffspring()+" of ";
		for(int x = i; x > 0; x--){
			s += l[x].getShortName()+", "+l[x].getOffspring()+" of ";
		}
		s += l[0].getFullName();
		return s;
	} else {
		return o;
	}
}

//Dynastic

	public Dynasty getDynasty(){
		return this.getPerson().getHouse().getDynasty();
	}

	public boolean hasDynasty(){
		return this.getDynasty() != null;
	}

/*	public void setDynasty(Dynasty d){
		this.dynasty = d;
	}*/

//Simple Methods

	public ArrayList<Event> getEvents(){ 	return this.events; }
	public boolean hasConsort(){ 			return this.consort != null; }
	public Claim getClaim(){				return this.claim; }
	public Holder getHolder(){ 				return this; }
	public Consort getConsort(){ 				return this.consort; }
	public Human getPerson(){ 				return this.person; }
	public Office getOffice(){				return this.office; }
	public Ruler getRuler(){ 				return this.ruler; }
	public String getName(){ 				return this.name; }
	public String getNotes(){ 				return this.notes; }
	public void addNotes(String n){ 		this.notes += n+"<br>"; }
	public void resetConsort(){ 			this.consort = null; }
	public void setRuler(Ruler r){ 			this.ruler = r;}
	public Calendar getStart(){				return (Calendar) this.start.clone();	}
	public Calendar getEnd(){				return (Calendar) this.end.clone();	}


}
