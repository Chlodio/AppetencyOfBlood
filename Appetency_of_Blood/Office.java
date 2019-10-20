import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Office{
	private boolean atWar;
	private boolean gearingUp;
	private Court court;
	private Debt debt;										//How much is owned
	private float tax;
	private Rule rule;
	private List<Rule> ruleList;
	private List<Holder> holderList;
	private List<Human> consortList;
	//private Holder holder;		 						//Who officially rulers
	//private Ruler ruler;		 							//Who rules in fact
	private Laws laws;
	private List<Project> projects;
	private Military military;
	private Territory territory;
	private Treasury funds;									//Save funds

	public static List<Office> offices =					new ArrayList<>();
//	public static Human firstSovereign;
	public static int id = 0;
	public static List<RegnalName> regnalNames = 			new ArrayList<>();
	public static Map <Integer, Office> office = 			new HashMap();

	public Office(){
		this.court =									new Court(this);
		this.debt = 									new Debt(this);
		this.funds = 									new Treasury(this, this.debt);
		this.holderList = 								new ArrayList<>();
		this.ruleList =									new ArrayList<>();
		this.consortList =								new ArrayList<>();
		this.military = 								new Military(this);
		this.projects = 								new ArrayList<>();
		this.regnalNames = 								new ArrayList<>();
		this.territory = 								new Realm(this);
		this.gearingUp = 								false;
		this.atWar = 									false;
		this.tax = 										0.5f;
		offices.add(this);
	}

	public static Office create(){
		id++;
		office.put(id, new Office());
		Elective.callHeir();
		Human founder = Succession.heir;
		office.get(1).inaugurate(founder);
		//firstSovereign = founder;
		return office.get(id);
	}



	public void inaugurate(Human person){
/*		boolean brotherU = false;
		boolean uncleU = false;
		if (!Claim.getTemp().isSpecial()){
			if (person.hasAdultBrother()){
				if (person.getOldestBrother().isChildOf(Claim.getTemp().getFromLineage(1))){
					brotherU = true;
				}
			}
			if(Claim.getTemp().hasGrandparentClaimant()){
				if (Claim.getTemp().getGrandparentClaimant().hasSon()){
					uncleU = true;
				}
			}
		}*/
		String oldName =								person.getName().getFull();
		if(person.house.getRanking() != 8){				person.house.setRanking(8);}
		Office kingship = 								office.get(1);
		this.rule = new Rule(this);
		this.rule.createSoleRuler(this, person);
		//Holder holder = 								Holder.regnafy(person, this);
		this.addHolder(this.getRule().getSeniorHolder());
	//	person.addRegnalTitle(this);
		//holder.getPerson().title = 						holder.getPerson().getRoyalTitle();
		//person.getName().setFull(holder.getName());
	//	if(!person.isUnwed()){ 							holder.setConsort(person.spouse); }
		System.out.println(oldName+" was inaugurated as "+person.getName().getFull()+" at the age of "+person.getAge()+".");
//		if (brotherU){	holder.addNotes("Sibling civil war");		}
//		if (uncleU){	holder.addNotes("Avunculate civil war");	}
	}

	public void endTenure(){
		this.getRule().handleRegency();
		this.rule.endReign();
//		if (office.holder.getPerson() == office.ruler.getPerson()){
//			Territory territory = office.territory;
//			office.holder.addNotes("Size: "+Integer.toString(territory.getArea())+"|Funds:"+Double.toString(office.getFunds().getFunds())+"|Loan:"+Double.toString(office.getDebt().getAmount())+"|Forts:"+Integer.toString(territory.getForts())+"|Temples:"+Integer.toString(territory.getTemples())+"|Cathedral:"+Integer.toString(territory.getCathedrals())+"|Abbeys:"+Integer.toString(territory.getAbbeys())+"|Priories:"+Integer.toString(territory.getPriories())+"|Scriptoriums:"+Integer.toString(territory.getScriptoriums())+"|Schools:"+Integer.toString(territory.getSchools())+"|Collegas:"+Integer.toString(territory.getCollegas())+"|Monuments:"+Integer.toString(territory.getMonuments())+"|Renown:"+Integer.toString(office.getRuler().getRenown())+"|Poverty: "+Float.toString(((Realm) territory).getPoverty())+"|Tax: "+Float.toString(office.tax));
//		}
		this.manageSuccession();
	}

	public void manageSuccession(){
		if(AgnaticCognaticPrimogeniture.callHeir(this.getKey(), this.getFounder().getPerson())){
			inaugurate(Succession.heir);
		} else{
			Elective.callHeir();
			inaugurate(Succession.heir);
		}
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
	/*	this.getRuler().loseRenown(1);
		if (this.hasDebt()){
			this.debt.handleInterest();
		}
		/*If interest takes more than 33% of the revenue, no actions can be done*/
//		if( this.debt.getInterestPer() < 0.33){
//			if (!this.gearingUp && !this.isAtWar()){
//				this.ruler.getInterest().getRandom().getType().consider(this);
//				this.funds.receivePayment(this.territory.getRevenue());
//			}
//			else{
//				this.military.getFunds().receivePayment(this.territory.getRevenue());
//				if (this.military.getFunds().canAffordCost(5000.0)){
//					this.endGearingUp();
//					InterestImperialism.startConquest(this);
//				}
//			}
//			for(Project x: this.projects){					x.progress(this);			}
//			Project.drain(this);
//		}
//		else{
//			this.funds.receivePayment(this.territory.getRevenue());
//			this.getDebt().pay();
//		}
	}



	public double getNet(){
		return this.territory.getRevenue()-this.getExpenses();
	}

	public double getExpenses(){
		double exp = this.debt.getInterest();
		return exp;
	}

	public void raiseTax(){
		this.tax = Main.min(Math.round((this.tax+0.01f)*100.0f)/100.0f, 1.0f);
	}

	public void lowerTax(){
		this.tax = Main.max(Math.round((this.tax-0.01f)*100.0f)/100.0f, 0.01f);
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
			this.holderList.add(v);
		}
	}

//Simple methods

	public static Office getOffice(int v){				return offices.get(v);		}
	//public Ruler getRuler(){ 							return this.ruler; 			}
	public boolean hasDebt(){ 							return this.debt.getAmount() != 0.0; }
	public boolean hasPositiveNet(){ 					return this.getNet() > 0.0; }
	public boolean isAtWar(){ 							return this.atWar; 			}
	public boolean isGearingUp(){						return this.gearingUp; 		}
	public boolean isHolder(Holder x){					return this.holderList.contains(x); }
	public Debt getDebt(){ 								return this.debt; 			}
	public Human getKey(){								return this.rule.getKey();	}
	public List<Holder> getHolderList(){				return this.holderList;		}
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
	public Holder getFounder(){ 						return this.holderList.get(0); }
	public void setRuler(Ruler r){						this.rule.setSeniorRuler(r); System.exit(0);}
	public void addToConsortList(Human h){				this.consortList.add(h);	}
	public int getConsortRank(Human h){				return this.consortList.indexOf(h);}

}
