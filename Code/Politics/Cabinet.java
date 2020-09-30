package Code.Politics;
import Code.Politics.Office;
import Code.Politics.Minister;
import Code.Human.Human;
import Code.House.House;
import Code.Common.Basic;
import Code.Politics.Steward;
import Code.History.CabinetRegister;
import java.util.List;
import java.util.ArrayList;

public class Cabinet{
	private Office office;
	private Minister steward;
	private Minister constable;
	private Minister chancellor;
	private Minister chamberlain;
	private CabinetRegister register;	//Records historical ministers

	//Steward								(governance)
	//Constable							(stratagem)
	//Chancellor 	(clergy)	(legalism)
	//Chamberlain						(governance)
	//Treasurer 	(clergy)	(finances)
	//Marshal								(tactics)

	public Cabinet(Office o){
		this.setOffice(o);
		this.register = new CabinetRegister();
	}

	public void setOffice(Office c){			this.office = c;			}

	public void setupMinisters(){
		Minister m = findSteward();
		this.setSteward(m);

		m = findConstable();
		this.setConstable(m);

		m = findChamberlain();
		this.setChamberlain(m);

		m = findChancellor();
		this.setChancellor(m);
	}

	public Minister findSteward(){
		List<Human> l = 	filterEligible(House.getNobles());
		Minister m = 		new Steward(this, Basic.choice(l));
		return m;
	}

	public Minister findConstable(){
		List<Human> l = 	filterEligible(House.getNobles());
		Minister m = 		new Constable(this, Basic.choice(l));
		return m;
	}

	public Minister findChamberlain(){
		List<Human> l = 	filterEligible(House.getNobles());
		Minister m = 		new Chamberlain(this, Basic.choice(l));
		return m;
	}

	public Minister findChancellor(){
		List<Human> l = 	filterEligible(House.getNobles());
		Minister m = 		new Chancellor(this, Basic.choice(l));
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

	public void performDuties(){
		this.steward.performDuties();
	}

	public void setSteward(Minister m){
		Basic.annals.recordOfficeEntrace(m.getPerson());
		this.register.recordSteward(m);
		this.steward = m;
	}

	public void setConstable(Minister m){
		Basic.annals.recordOfficeEntrace(m.getPerson());
		this.register.recordConstable(m);
		this.constable = m;
	}


	public void setChamberlain(Minister m){
		Basic.annals.recordOfficeEntrace(m.getPerson());
		this.register.recordChamberlain(m);
		this.constable = m;
	}

	public void setChancellor(Minister m){
		Basic.annals.recordOfficeEntrace(m.getPerson());
		this.register.recordConstable(m);
		this.constable = m;
	}

	public CabinetRegister getRegister(){
		return this.register;
	}

	public Minister getSteward(){					return this.steward;	}

	public Office getOffice(){						return this.office;		}


}
