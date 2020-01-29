package Code.Politics;
import java.util.ArrayList;
import Code.Human.Human;
import Code.House.House;
import Code.Common.Basic;
import Code.Common.Event;
import Code.Politics.*;

public class Regent{
	public static Human find(){
		ArrayList<Human> runners = 	new ArrayList<>();
		int p;
		Human w;
		Human holder = 				Realm.getHolder(0).getPerson();
		if (holder.getMother() != null & holder.getMother().isAlive()){
			w = holder.getMother();
		}
		else if(holder.getHouse().hasAdultKinsman(holder)){
			w = holder.getHouse().getAdultKinsman(holder);
		}
		else if(holder.getHouse().hasAdultKinswoman(holder)){
			w = holder.getHouse().getAdultKinswoman(holder);
		} else{
			for(House x: House.getList()){
				if (x.getHead().getAge() >= 20){
					runners.add(x.getHead());
				}
			}
			w = runners.get(0);
			p = w.getHouse().getPrestige();
			for(Human x: runners){
				if (x.getHouse().getPrestige() > p){
					w = x; p = x.getHouse().getPrestige();
				}
			}
		}
		return w;
	}

	public static Ruler make(){
		Human person = find();
		Basic.print(person.getFullName()+" became regent");
		person.addEvent(Event.E200);
	/*	if (!person.sex){ person.rename(Title.REGENT); }
		else{ person.sRename(Title.QUEENREGENT); }*/
		return new Ruler(person);
	}

	//Holder with a regent dies, thus the current regent is dismissed
	public static void dismiss(){
		Human regent = Realm.getHolder(0).getRegent().getPerson();
		Basic.print(regent.getFullName()+" was dismissed as a regent");
		regent.removeEvent(Event.E200);
	/*	if (!regent.sex){
			regent.rename(Title.PROREGENT);
		}
		else{
			regent.sRename(Title.PROQUEENREGENT);
		}*/
	}

	//Lift regent is lifted upon them becoming an adult
	public static void lift(){
		 Holder holder = Realm.getHolder(0);
		 Human r = holder.getRegent().getPerson();
		 holder.getPerson().removeEvent(Event.E201);
		 r.removeEvent(Event.E200);
	/*	 if (!r.sex){
 			r.rename(Title.PROREGENT);
 		}
 		else{
 			r.sRename(Title.PROQUEENREGENT);
 		}*/
		holder.getOffice().setRuler(new Ruler(holder.getPerson()));
		holder.setRuler(holder.getOffice().getRuler());
		Basic.print("Regency of "+r.getFullName()+" was lifted");
	}
}
