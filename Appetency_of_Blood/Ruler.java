class Ruler{

	private Human person;

	private int renown;

	public Ruler(Human person){
		this.person 	= 	person;
		this.renown 	= 	0;
	}

	public void gainRenown(int amount){
		this.renown = Main.min(this.renown+amount, 100);
	}

	public void loseRenown(int amount){
		this.renown = Main.max(this.renown-amount, -100);
	}

//Simple Methods

	public Human getPerson(){  						return this.person; }
	public int getRenown(){ 						return this.renown; }
	public Interest getInterest(){ 					return this.person.getPolProfile().getInterest();}
	public Skill getSkill(){ 						return this.person.getPolProfile().getSkill();}
}
