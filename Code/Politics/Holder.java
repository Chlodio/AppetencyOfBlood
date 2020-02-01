package Code.Politics;

import Code.Common.Event;
import Code.Human.Human;
import Code.Politics.*;
import Code.Ancestry.*;
import Code.Common.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

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

	public Holder(Human person, Office office){
		this.person = 	person;
		this.claim = 	new Claim();
		if (office.getLineage().getPriority() < 3){
			this.claim.combine(this, office);
		} else {
			this.claim.setSpecial(1);
		}

		this.start = 	(Calendar) Basic.date.clone();
		this.notes = 	"";
		this.regents =	new ArrayList<>();
		this.events = 	new ArrayList<>();
		this.office = 	office;
		person.addEvent(Event.E101);
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
		System.out.println(holder.name);
		return holder;
	}

	public void fixTheFirst(){
		if (this.getPerson().isMale()){
			this.name = (this.getPerson().getName().getName()+" I");
		} else{
			this.name = (this.getPerson().getName().getName()+" I");
		}
		this.person.getName().setFull(this.getName());
	}

	public String getReign(){
		String x = Integer.toString(this.start.get(Calendar.YEAR));
		if(this.end != null){
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
		String rl = "(";
		if(this.end != null){
			rl += Integer.toString((this.end.get(Calendar.YEAR)-this.start.get(Calendar.YEAR)));
		}
		else{
			rl += Integer.toString((Basic.date.get(Calendar.YEAR)-this.start.get(Calendar.YEAR)));
		}
		return rl+")";
	}

	public void endReign(){
		this.end = (Calendar) Basic.date.clone();
	}


//Regent Methods

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


}
