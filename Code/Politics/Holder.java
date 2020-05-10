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
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import Code.House.CadetHouse;

public class Holder{
	private ArrayList<Event> events;
	private ArrayList<Ruler> regents;
	private Calendar end;
	private Calendar start;
	private Claim claim;
	private Human consort;
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
		if (!h.isDynastic()){
			throw new RuntimeException();
		}
	//	this.setDynasty(d);
	}


	public boolean isFounder(){
    return this == this.getDynasty().getFounder();
  }

	public void setConsort(Human c){
		c.setOffice(this.office);
		this.consort = c;
		this.office.addToConsortList(c);
		if (c.isFemale()){
			c.setTitle(Title.QUEENCONSORT);
			c.sRename(Title.QUEENCONSORT);
		}
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
			if(this.start.get(Calendar.YEAR) != this.end.get(Calendar.YEAR)){
				x += "–";
				x = x+this.end.get(Calendar.YEAR);
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
			rl += Integer.toString((this.end.get(Calendar.YEAR)-this.start.get(Calendar.YEAR)));
		}
		else{
			rl += Integer.toString((Basic.date.get(Calendar.YEAR)-this.start.get(Calendar.YEAR)));
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
		return this.getReignLengthExact()/365;
	}

	public String getReignYearsAndDays(){
		int d = this.getReignLengthExact();		//Days
		int y = d/365;
		String s = "";
		d -= y*365;
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
		if (Basic.drawStraws(2)){
			if (a < 14){
				h.getName().setNick(Nick.CHILD);
			} else if (a < 24){
				h.getName().setNick(Nick.YOUNG);
			} else if (a > 60){
				h.getName().setNick(Nick.OLD);
			} else if (this.getReignLengthYears() >= 20){
				h.getName().setNick(Nick.getRandom());
			}
		}
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
		String s = "";
		s += this.getEarlyLife();
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
		String s = Basic.capitalize(h.getForename())+" was";
		if (h.hadFather()){
			s += " born in ";
			s += Basic.sDateLong(h.getBirth())+" as the ";
			s += h.getAgnaticOrderStr()+" of ";
			s += this.getParentNameAge(h.getFather());
			s += " and ";
			if (!h.hasSameBirthOrder()){
				s += "the "+h.getEnaticOrderStr()+" of ";
			}
			s += this.getParentNameAge(h.getMother());
			if (h.isPosthumous()){
				int d = Basic.getDaysBetween(h.getFather().getDeath(), h.getBirth());
				String ds = Basic.getMonthsOrDays(d);
				s += ", "+ds+" from his father's death";
			}
		} else {
			s += " born around ";
			s += h.getBirthYear();
		}
		return s+".";
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
		String s = " "+this.getName();
		if (this.hasEnded()){
			s += " was ";
		} else {
			s += " is ";
		}
		DynasticOffice dy = this.getDynasticOffice();
		if (this.isFounder()){
				if (!dy.hadOnlyOne()){
					s += "the founder of";
					s += dy.getPoeticTenure();		//I.e short-lived
				} else {
					s += "the only ruler from";
				}
		} else {
			s += "the "+Basic.getOrdial(dy.getOrder(this));
			if (this.hasEnded()){
				if (this.wasLastRulerOfDynasty()){
					s += " and last";
				}
			}
			s += " ruler from";
		}
		return s+" "+this.getDynasty().getName()+".";
	}

	public String getAgeOfAscensionS(){
		int a = this.getPerson().getDaysIn(this.getStart());
		if (a > 365){
			return "at the age of "+Basic.getCardinal(a/365);
		} else if (a > 31) {
			return "while only "+Basic.getCardinal(a/31)+"-months-old";
		} else {
			return "while merely "+Basic.getCardinal(a)+"-days-old";
		}
	}

	public boolean hasEnded(){
		return this.end != null;
	}

	public String getStartYearStr(){
		return Integer.toString(this.getStart().get(Calendar.YEAR));
	}

//Dynastic

	public Dynasty getDynasty(){
		return this.getPerson().getHouse().getDynasty();
	}

/*	public void setDynasty(Dynasty d){
		this.dynasty = d;
	}*/

//Simple Methods

	public ArrayList<Event> getEvents(){ 	return this.events; }
	public boolean hasConsort(){ 			return this.consort != null; }
	public Claim getClaim(){				return this.claim; }
	public Holder getHolder(){ 				return this; }
	public Human getConsort(){ 				return this.consort; }
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
