package Code.Politics;
import Code.Economy.*;
import Code.Court.Court;
import Code.Politics.Rule;
import Code.Human.Human;
import Code.Ancestry.*;
import Code.Politics.*;
import Code.House.House;
import Code.Politics.DynasticOffice;
import Code.Politics.Cabinet;
import Code.Common.*;
import Code.House.Dynasty;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Office{
	private boolean atWar;
	private boolean gearingUp;
	private Court court;
	private Debt debt;											//How much is owned
	private float tax;
	private Rule rule;
	private String name;
	private List<Rule> ruleList;
	private Lineage lineage;
	private List<Human> consortList;
	private List<Project> projects;
	private Military military;
	private Territory territory;
	private Treasury funds;									//Save funds
	private List<Human> claimants;					//people with claims
	private List<Dynasty> dynasties;
	private List<DynasticOffice> dOffices;
	private List<RegnalName> regnalNames = 					new ArrayList<>();

	private Cabinet cabinet;								//Collection of ministers

	public static List<Office> offices =					new ArrayList<>();;
	public static int id = 0;

	public static Map<Integer, Office> office = 			new HashMap<>();


	public Office(){
		this.court =									new Court(this);
		this.debt = 									new Debt(this);
		this.funds = 									new Treasury(this, this.debt);
		this.lineage = 								new Lineage(this);
		this.ruleList =								new ArrayList<>();
		this.consortList =						new ArrayList<>();
		this.military = 							new Military(this);
		this.projects = 							new ArrayList<>();
		this.dynasties =							new ArrayList<>();
		this.regnalNames = 						new ArrayList<>();
		this.dOffices =								new ArrayList<>();
		this.claimants = 							new ArrayList<>();
		this.territory = 							new Realm(this);
		this.gearingUp = 							false;
		this.atWar = 									false;
		this.tax = 										0.5f;
		offices.add(this);
		this.name =										"#"+offices.size();
	}

	public static Office create(){
		id++;
		office.put(id, new Office());
		Human founder = Basic.choice(House.getMagnates());
		if (founder.isRegnant()){
			throw new RuntimeException();		//The same
		}
		office.get(offices.size()).inaugurate(founder);
		office.get(offices.size()).handleCabinet();
		return office.get(id);
	}



	public void inaugurate(Human person){
		String oldName =								person.getFullName();
		if(person.getHouse().getRanking() != 8){		person.getHouse().setRanking(8);}
		Office kingship = 								office.get(1);
		this.rule = new Rule(this);
		this.rule.createSoleRuler(this, person);
		this.addHolder(this.getRule().getSeniorHolder());
		person.hasClaimRemove(this);
		System.out.println(oldName+" was inaugurated as "+person.getFullName()+" at the age of "+person.getAge()+".");
	}

	public void endTenure(){
		this.getRule().handleRegency();
		this.rule.endReign();
		this.spreadClaims();
		this.manageSuccession();
	}

	public void spreadClaims(){
		Human h = this.getHolder().getPerson();
		if (h.isAlive()){
			Claim c;
			List<Human> l = h.getLegitSons();
			for (Human x: l){
				if (x.isAlive()){
					x.addClaim( new Claim( new Human[]{h, x} ) );
				}
			}
		}
	}

	public void manageSuccession(){

		this.getLineage().determineSuccession();
		Human s;
		if (this.getLineage().getPriority() < 3){
			if (this.getLineage().getHeir() == null){
				System.out.println(this.getLineage().getPriority());
				throw new RuntimeException();
			}
			s = this.getLineage().getHeir();
		} else {
			try {
				s = Basic.choice(House.getMagnates());		//Elect
			} catch (RuntimeException e){
				throw new RuntimeException();
			}
		}

		inaugurate(s);
	}


	public RegnalName addRegnalNames(RegnalName rn){
		this.regnalNames.add(rn);
		return rn;
	}

	public void doAccounting(){
		double payments = this.debt.getInterest(); Realm ter = (Realm) this.territory;
		if (ter.getPoverty() != this.tax){
			if (ter.getPoverty() <= this.tax){ ter.gainPoverty(0.01f); }
			else{ ter.losePoverty(0.01f); }
		}
	}



	public double getNet(){
		return this.territory.getRevenue()-this.getExpenses();
	}

	public double getExpenses(){
		double exp = this.debt.getInterest();
		return exp;
	}

	public void raiseTax(){
		this.tax = Basic.min(Math.round((this.tax+0.01f)*100.0f)/100.0f, 1.0f);
	}

	public void lowerTax(){
		this.tax = Basic.max(Math.round((this.tax-0.01f)*100.0f)/100.0f, 0.01f);
	}


	public Holder getLastHolder(){
		int v = this.ruleList.size()-2;
		if (v >= 0){
			return this.ruleList.get(v).getSeniorHolder();
		} else{
			return null;
		}
	}

	public void addHolder(Holder v){
		if (!this.isHolder(v)){
			this.addToLineage(v);
		}
	}

//Dynasties
	//is part of estabalished dynasty
	public boolean hadDynasty(Dynasty d){
		return this.getDynasties().contains(d);
	}

	public List<Dynasty> getDynasties(){
		return new ArrayList<>(this.dynasties);
	}

	public DynasticOffice getRecentDynasticOffice(){
		return this.dOffices.get(this.dOffices.size()-1);
	}

	public void addDynasty(Dynasty d){
		this.dynasties.add(d);
	}

	public void addDynasticOffice(DynasticOffice d){
		if (this.dOffices.size() != 0){
			this.getRecentDynasticOffice().disable();
		}
		this.dOffices.add(d);
	}

	public List<DynasticOffice> getDynasticOffices(){
		return this.dOffices;
	}

//Claimants

	public List<Human> getClaimants(){
		return this.claimants;
	}

	public void addClaimant(Human h){
		this.claimants.add(h);
	}

	public void removeClaimant(Human h){
		this.claimants.remove(h);
	}

//shortcuts
	public boolean isHolder(Holder h){					return this.lineage.isHolder(h); 	}
	public void addToLineage(Holder h){					this.lineage.addTo(h);				}
	public Holder getFounder(){ 						return this.lineage.getFounder(); 	}


//Simple methods

	public Lineage getLineage(){						return this.lineage;		}
	public static Office getOffice(int v){				return offices.get(v);		}
	//public Ruler getRuler(){ 							return this.ruler; 			}
	public boolean hasDebt(){ 							return this.debt.getAmount() != 0.0; }
	public boolean hasPositiveNet(){ 					return this.getNet() > 0.0; }
	public boolean isAtWar(){ 							return this.atWar; 			}
	public boolean isGearingUp(){						return this.gearingUp; 		}
	public Debt getDebt(){ 								return this.debt; 			}
	public Human getKey(){								return this.rule.getKey();	}

	public List<Project> getProjects(){ 				return this.projects; 		}
	public List<RegnalName> getRegnalNames(){ 			return this.regnalNames; 	}
	public List<Rule> getRuleList(){					return this.ruleList;		}
	public Military getMilitary(){ 						return this.military; 		}
	public Rule getRule(){								return this.rule;			}
	public Ruler getRuler(){							return this.rule.getSeniorRuler();}
	public Holder getHolder(){							return this.rule.getSeniorHolder();}
	public Territory getTerritory(){ 					return this.territory; 		}
	public Treasury getFunds(){ 						return this.funds; 			}
	public void addProject(Project n){					this.projects.add(n);		}
	public void addRule(Rule rule){						this.ruleList.add(rule);	}
	public void beginGearingUp(){ 						this.gearingUp = true; 		}
	public void endGearingUp(){ 						this.gearingUp = false; 	}
	public void endWar(){ 								this.atWar = false; 		}
	public void goToWar(){ 								this.atWar = true; 			}
	public void setRuler(Ruler r){						this.rule.setSeniorRuler(r);}
	public void addToConsortList(Human h){				this.consortList.add(h);	}
	public int getConsortRank(Human h){					return this.consortList.indexOf(h);}

	public Cabinet getCabinet(){						return this.cabinet;		}

	public void handleCabinet(){
		this.cabinet =				new Cabinet(this);	//Create cabinet
		this.getCabinet().appointMinister();			//Appoint minister into the cabinet
	}

}
