package Code.Politics;
import Code.Politics.Cabinet;
import Code.Human.Human;
import Code.calendar.Calendar;
import Code.Common.Basic;

/*Ministers or great officers are people employed by the ruler to be govern the realm with their authority. Each ministers is head of their department, and play key role in the development of the realm.*/

public class Minister{
	private Cabinet cabinet;
	private Human person;
	protected String title;				//Specific name of the title
	protected Calendar beginning;
	protected Calendar ending;
	private int skill;
	private int exp;
	private int diligence;

	public Minister(Cabinet c, Human p){
		this.setCabinet(c);
		this.setPerson(p);
		this.beginning = Calendar.getDateClone();
		this.skill = 0;
		this.exp = 0;
		this.diligence = 1+Basic.randint(5);
	}

	public void setCabinet(Cabinet c){		this.cabinet = c;		}

	//Always overriden by subclasses
	public void replace(){}

	public void retire(){
		this.ending = Calendar.getDateClone();
	}

	public void performDuties(){
		this.exp += this.diligence;
		if (this.exp >= level[this.skill+1]){
			this.exp -= level[this.skill+1];
			this.skill++;
		}
	}

	public void setPerson(Human p){
		this.person = p;
		p.setRole(this);
	}

	public String getTitle(){
		return this.title;
	}

	public String getRank(){
		return rank[this.skill];
	}

	public Cabinet getCabinet(){			return this.cabinet;	}

	public Human getPerson(){				return this.person;		}

	//The period in which between the character worked as a ministers
	public String getTenure(){
		int b = this.beginning.getYear();
		if (this.ending == null){
			return b+"–PRSN";
		} else {
			return b+"–"+this.ending.getYear();
		}
	}
	private static int[] level = {0, 300, 600, 1200, 2400};
	private static String[] rank = { "novice", "standard", "reputable", "remarkable"};
}
