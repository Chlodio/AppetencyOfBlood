import java.util.ArrayList;

public class Regent{
	public static Human find(){
		ArrayList<Human> runners = 	new ArrayList<>();
		int p;
		Human w;
		Human holder = 				Realm.getHolder(0).getPerson();
		if (holder.mother != null & holder.mother.isAlive()){
			w = holder.mother;
		}
		else if(holder.house.hasAdultKinsman(holder)){
			w = holder.house.getAdultKinsman(holder);
		}
		else if(holder.house.hasAdultKinswoman(holder)){
			w = holder.house.getAdultKinswoman(holder);
		} else{
			for(House x: House.getList()){
				if (x.getHead().getAge() >= 20){
					runners.add(x.getHead());
				}
			}
			w = runners.get(0);
			p = w.house.getPrestige();
			for(Human x: runners){
				if (x.house.getPrestige() > p){
					w = x; p = x.house.getPrestige();
				}
			}
		}
		return w;
	}

	public static Ruler make(){
		Human person = find();
		Main.print(person.getName().getFull()+" became regent");
		person.events.add(Event.E200);
	/*	if (!person.sex){ person.rename(Title.REGENT); }
		else{ person.sRename(Title.QUEENREGENT); }*/
		return new Ruler(person);
	}

	//Holder with a regent dies, thus the current regent is dismissed
	public static void dismiss(){
		Human regent = Realm.getHolder(0).getRegent().getPerson();
		Main.print(regent.getName().getFull()+" was dismissed as a regent");
		regent.events.remove(Event.E200);
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
		 holder.getPerson().events.remove(Event.E201);
		 r.events.remove(Event.E200);
	/*	 if (!r.sex){
 			r.rename(Title.PROREGENT);
 		}
 		else{
 			r.sRename(Title.PROQUEENREGENT);
 		}*/
		holder.getOffice().setRuler(new Ruler(holder.getPerson()));
		holder.setRuler(holder.getOffice().getRuler());
		Main.print("Regency of "+r.getName().getFull()+" was lifted");
	}
}
