package Politics;

public class Territory{
	private Office office; protected int area;
	private int forts; private int abbeys; private int temples; private int schools;
	private int scriptoriums; private int collegas; private int monuments;
	private int cathedrals; private int priories;
	public Territory(Office office){
		this.office = office; this.area = 1;
		this.forts = 0;
		this.schools = 0; this.collegas = 0; this.scriptoriums = 0;
		this.abbeys = 0; this.priories = 0;
		this.cathedrals = 0; this.temples = 0;
		this.monuments = 0;
	}
	public int getArea(){ 				return this.area; }
	public int getForts(){ 				return this.forts; }
	public int getAbbeys(){ 			return this.abbeys; }
	public int getSchools(){ 			return this.schools; }
	public int getTemples(){ 			return this.temples; }
	public int getCollegas(){ 			return this.collegas; }
	public int getMonuments(){ 			return this.monuments; }
	public int getCathedrals(){ 		return this.cathedrals; }
	public int getPriories(){ 			return this.priories; }
	public int getScriptoriums(){ 		return this.scriptoriums; }
	public Office getOffice(){			return this.office; }
	public void buildFort(){
										this.forts++;
										this.office.getRuler().gainRenown(1);
	}

	public void buildPriory(){
										this.priories++;
										this.office.getRuler().gainRenown(1);
	}

	public void buildAbbey(){
										this.abbeys++;
										this.office.getRuler().gainRenown(10);
	}

	public void buildScriptorium(){
										this.scriptoriums++;
										this.office.getRuler().gainRenown(1);
	}

	public void buildSchool(){
										this.schools++;
										this.office.getRuler().gainRenown(5);
	}

	public void buildCollega(){
										this.collegas++;
										this.office.getRuler().gainRenown(5);
	}

	public void buildTemple(){
										this.temples++;
										this.office.getRuler().gainRenown(1);
	}

	public void buildCathedral(){
										this.cathedrals++;
										this.office.getRuler().gainRenown(10);
	}

	public void buildMonument(){
										this.monuments++;
										this.office.getRuler().gainRenown(10);
	}

	public void expand(){
		this.area++;
		((Realm) this).gainPoverty(0.05f);
	}

	//Collect tax
	public double getRevenue(){
		return Math.round((this.area*(1.0-((Realm) this).getPoverty())*500.0)*100.0)/100.0;
	}

}
