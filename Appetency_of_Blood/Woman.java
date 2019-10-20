import java.util.List;
import java.util.ArrayList;
import java.util.Calendar;

public class Woman extends Human {
	protected Human uterus;
	protected int mop;
	protected static List<Human> elders =	  	new ArrayList<>();
	protected static List<Human> women = 		new ArrayList<>();
	protected static List<Human> children = 	new ArrayList<>();
	protected static List<Human> pregnant = 	new ArrayList<>();
	protected static List<Human> singles = 		new ArrayList<>();
	private final static String[] relation = 	{"daughter", "granddaughter", "great-granddaughter", 	"great-great-granddaughter"};

	private static List<Human> childBridePool = new ArrayList<>();
//generated
    public Woman(int age, House house){
		super(age);
		this.name = 		new Name(true, this, true);
		this.sex = true;
		this.mop = -1;
		this.religion.getParish().add(this);
		//House.addToHouse(this, house);
		this.name.setFull(this.makeName());
		this.becomeSingle();
		women.add(this);
	}

//naturally born
	public Woman(){
 	   super();
 	   this.sex = true;
 	   this.mop = -1;
	   this.religion.getParish().add(this);
	   women.add(this);
    }

	@Override
	public String getShortName(){
		return this.getName().getName();
	}

	@Override
	public String getFormalName(){
		String n = this.getName().getName();
		if (this.getName().hasRegnal()){
			return this.getName().getFull();
		} else if (this.title == null){
			return n;
		}
		if (this.hasTitle(Title.PRINCESS)){
			return n+", "+Main.getOrderShort(this.getCadency())+" "+this.getTitleS();
		} else if (this.hasTitle(Title.QUEENCONSORT)){
			return n+", "+Main.getOrderShort(this.getOffice().getConsortRank(this))+" "+this.getTitleS();
		} else {
			return n+", "+this.getTitleS();
		}
	}


	@Override
    public String makeName(){
		String s = this.getShortName();
		if (this.getName().getNick() != null){
				s +=" "+this.getName().getNick();
		}
		if (this.house != null){
			if (this.title == null){
				return s+" "+this.house.getName();
			} else{
				if (this.hasTitle(Title.PRINCESS)){
					return s+", "+Main.getOrderShort(this.getCadency())+" "+this.getTitleS();
				} else if (this.hasTitle(Title.QUEENCONSORT)){
					return s+", "+Main.getOrderShort(this.getOffice().getConsortRank(this))+" "+this.getTitleS();
				} else {
					return s+", "+this.getTitleS();
				}
			}
			//return this.title.name+" "+this.house.getName()+"e"+s;
		}
		return s;
	}



    @Override
    public void saunter(int x){
        switch(x){
			case 20:
				if (this.isAlive() == true){ this.childbirth();}
				break;
            case 0:
				if (this.isAlive() == true){ this.kill();}
				break;
        }
    }

	@Override
	public void clean(){
		if (this.isAdult() && this.relSta <= 1) {
			this.becomeTaken();
			if (this.relSta == 3){
				this.removeFromElders();
			}
		}
		else {
			Woman.children.remove(this);
		}
	}

	@Override
	public void ovulate(int maom){
		this.growFetus();
		if (this.mop == 10){
			int f = Main.randint(maom)+1;
			Main.dayC.get(f).add(this); Main.dayE.get(f).add(20);
		}
	}

	@Override
	public void childbirth(){
		if (Main.randint(10) != 0){					//Stillbirth? this.relSta != 4
			this.deliver(this.uterus);
			if (Main.randint(150) == 0){
				this.deliver(this.uterus);			//Twins?
			}
		}
		if (Main.randint(50) == 0){					//Maternal death?
			this.kill();
		}

		else {
			this.drainUterus();
			//Posthumous
			if (this.relSta == 4){ this.becomeWidow(); }
		}
	}
	public boolean isPregnant(){ 			return this.uterus != null; 	}
	public void fillUterus(Man sperm){ 		this.uterus = sperm; 			}
	public void growFetus(){				this.mop++; }

	public void drainUterus(){
		this.uterus = null;
		this.mop = -1;
		Woman.pregnant.remove(this);
	}
	public static Human beBorn(Human f, Human m){
		Human it = new Woman();
		Woman.children.add(it);
		it.father = f;
		it.mother = m;
		f.daughters.add(it);
		m.daughters.add(it);
		it.house = f.house;
		it.addToHouse();
		Name.aDaughter(f, m, it);
		it.getName().setOwner(it);
		if (f.isAlive() || it.hasSister()){
			it.cadency = f.getLivingDaughters().size()-1;
		} else{
			it.cadency = 0;
		}

		//it.getName().setNick("Postum");



		return it;
	}

	@Override
	public void becomeSingle(){
		singles.add(this);
		this.mating = Mating.revaluateF(this);
	}

	@Override
	public void becomeWidow(){
		if (!this.isPregnant()){
			if (this.isMarriageable()){
				this.handleWidowhood();
			} else{
				this.relSta = 3;
			} //Posthumous pregnancy
		} else{
			this.relSta = 4;
		}
	}

	@Override
	public void handleWidowhood(){
		Human h = this.getLatestHusband();;
		this.becomeSingle();
		this.relSta = 1;
		if(h.hasUnwedSameSexSibling()){
		//	Human d = h.getUnwedBrother();
			Marriage.doLevirate(this, h);
		//	System.out.println(this.isMarriedTo(d));
		//	Main.pause(1000);
		}
	}

	@Override public boolean hasUnwedSameSexSibling(){ return this.hasUnwedSister();}


	@Override
	public void becomeTaken(){				singles.remove(this); }

	@Override
	public void bury(){
		List<Human> h;
		if (this.house != null){
			this.house.removeKinswoman(this);
			if (this.hasSister()){
				h = this.father.getLivingDaughters();
				for (int x = 0; x < h.size(); x++){
					h.get(x).cadency = x;
				}
			}
		}
		this.religion.getParish().remove(this);
		women.remove(this);
	/*	if (this.title == Title.QUEEN){
			Holder.getLatestSovereign().resetConsort();
		}*/
		//Mother dies while pregnant
		if (this.isAdult()){
			if (this.uterus != null){;
				pregnant.remove(this);
			}
		} else{
			if (this.getAge() >= 8){
				if (!this.isMarried()){
					childBridePool.remove(this);
				}
			}
		}

	};

	public static void growUp(){
		List<Human> rem = new ArrayList<>();
		for (Human x: Woman.children){
			switch(x.getAge()){
				case 8:
					childBridePool.add(x);
					break;
				case 12:
					rem.add(x);
					break;
			}
		}
		for (Human x: rem){
			Woman.children.remove(x);
			x.fertility = 		Main.randint(101);
			x.marriages = 		new ArrayList<>();
        	x.sons = 			new ArrayList<>();
			x.daughters = 		new ArrayList<>();
			if (x.isUnwed()){
				childBridePool.remove(x);
				x.becomeSingle();
			} else {
				Marriage.marryBetrothed(x.getSpouse(), x);
			}

		//	if (x.title == Title.QUEENREGNANT){ Regent.lift(); }
		}
	}

	@Override
	public boolean isVirgin(){
		if (this.marriages == null || this.marriages.size() != 0){
			return true;
		}
		else{
			return false;
		}
	};

	public String getPortrait(){
		int age = this.aged();
		if (age >= 12){
			if(age < 20){ return "<img src='img/FA/"+Main.choice(Main.FApic)+".jpg'</img>"; }
			else{ return "<img src='img/FY/"+Main.choice(Main.FYpic)+".jpg'</img>"; }
		}
		return "";
	}

	@Override
	public boolean isMarriageable(){
		if (this.getAge() >= 40){ return false; }
		else{ return true; }
	}

	@Override
    public String getSibling(){		return "sister";}

	@Override
	public void sRename(Title title){
		this.title = title;
		this.name.setFull(this.makeName());
		//this.getName().setFull(this.getName().getName());
	}

	public void princify(){
		this.title = Title.PRINCESS;
		this.rename(title.PRINCESS);
	}

	@Override
	public void switchHouse(House h){
		if (this.isAlive()){
			this.getHouse().removeKinswoman(this);
			h.addKinswoman(this);
		}
		this.house = h;
	}

	@Override
	public void addToHouse(){
		this.house.addKinswoman(this);
	}

	@Override
	public String getRelation(int v){					return relation[v];		}

	@Override
	public Title getRoyalTitle(){						return Title.QUEENREGNANT;}

	@Override
	public boolean isChildOf(Human h){
		if (h.isAdult() && h.daughters.contains(this)){
			return true;
		}
		return false;
	}

	@Override
    public String widow(){ 			return "widow";}

	@Override
	public String getPronoun(){ 	return "she";}

	@Override
	public String getPossessive(){ 	return "her";}

	@Override
    public String child(){ 			return "girl";}

	@Override
    public String getOffspring(){ 	return "daughter";}

	@Override
	public String getParent(){ 		return "mother";}

	@Override
	public String getPibling(){ 	return "aunt";}

	@Override
	public String getNibling(){ 	return "niece";}

	public static int getAmount(){						return women.size();	}
	public static int getSingleNum(){					return singles.size();  }
	public void addToElders(){							elders.add(this); 	 	}
	public void removeFromElders(){						elders.remove(this); 	}

}
