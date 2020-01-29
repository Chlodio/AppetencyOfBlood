package Relationship;
import Common.*;
import Human.*;
import java.util.List;
import java.util.ArrayList;

public class Affair extends SexRelation {
	public static List<Affair> list = new ArrayList<>();
	//private Human beau;			//male lover
	//private Human belle;		//female lover

	public Affair(Human m, Human w){
		super(m, w, 3);
		m.setAffair(this);
		w.setAffair(this);
		list.add(this);
	}

	public static Human findMistress(Human m){
		List<Human> l = Marriage.getUnhappyWives(m);
		return Basic.choice(l);
	}

	public static void begin(Human m){
		if (Marriage.hasBadMarriage(m)){
			Human w = findMistress(m);
			if (w.isChildOf(m) || w.isSiblingOf(m)){
				System.out.println("Daughter/sister seduction");
				throw new RuntimeException();
			}
			new Affair(m, w);
			Basic.print(m.getShortName()+" ("+m.getAge()+") began affair with "+w.getShortName()+" ("+w.getAge()+")");
		}
	}

	public static void doAdultery(){
		for (Affair x: list){
			x.engageAdultery();
		}
	}

	public void engageAdultery(){
		Woman w = (Woman) this.doe;
		if (Basic.randint(100) < this.procreation[this.anniversary] && !w.isPregnant()){
			w.fillUterus(this);
			w.growFetus();
			Woman.pregnant.add(this.doe);
			Basic.print(w.getShortName()+" became pregnant with his lover, "+this.stag.getShortName()+"'s child");
		}
	}

	public void endAffair(){
		this.remove();
		this.stag.endAffair(this);
		this.doe.endAffair(this);
		Basic.print("Affair of "+this.stag.getFullName()+" and "+this.doe.getFullName()+" ended");
	}


	public Human getBeau(){		return this.stag;	}
	public Human getBelle(){	return this.doe;	}
	public void remove(){		list.remove(this);	}
	public static List<Affair> getList(){
		return new ArrayList<>(list);
	}

}
