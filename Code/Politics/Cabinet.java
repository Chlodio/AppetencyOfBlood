package Code.Politics;
import Code.Politics.Office;
import Code.Politics.Minister;
import Code.Human.Human;
import Code.House.House;
import Code.Common.Basic;
import java.util.List;
import java.util.ArrayList;

public class Cabinet{
	private Office office;
	private Minister minister;

	public Cabinet(Office o){
		this.setOffice(o);
	}

	public Office getOffice(){					return this.office;		}
	public void setOffice(Office c){			this.office = c;			}

	public void setMinister(Minister m){		this.minister = m;		}
	public Minister getMinister(){				return this.minister;	}

	public void appointMinister(){
		Minister m = findMinister(this);
		Basic.print(m.getPerson().getFullName()+" became a great officer");
		this.setMinister(m);
	}

	public Minister findMinister(Cabinet c){
		List<Human> l = 	filterEligible(House.getNobles());
		Minister m = 		new Minister(c, Basic.choice(l));
		return m;
	}

	//Filter out the children, monarch themselves and people who are already have a role
	public List<Human> filterEligible(List<House> l){
		List<Human> ll = new ArrayList<>(l.size());						//temp
		Human r = this.getOffice().getHolder().getPerson();				//The self-ruler
		for(House x: l){
			if (meetsRequirements(x.getHead(), r)){
				ll.add(x.getHead());
			}
		}
		return ll;
	}

	public boolean meetsRequirements(Human h, Human r){
		if (h.isAdult() && !h.hasRole()){
			if (h != r){
				return true;
			}
		}
		return false;
	}

}
