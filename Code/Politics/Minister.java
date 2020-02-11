package Code.Politics;
import Code.Politics.Cabinet;
import Code.Human.Human;

/*Ministers or great officers are people employed by the ruler to be govern the realm with their authority. Each ministers is head of their department, and play key role in the development of the realm.*/

public class Minister{
	private Cabinet cabinet;
	private Human person;

	public Minister(Cabinet c, Human p){
		this.setCabinet(c);
		this.setPerson(p);
	}


	public Cabinet getCabinet(){			return this.cabinet;	}
	public void setCabinet(Cabinet c){		this.cabinet = c;		}

	public Human getPerson(){				return this.person;		}
	public void setPerson(Human p){
		this.person = p;
		p.setRole(this);
	}
}
