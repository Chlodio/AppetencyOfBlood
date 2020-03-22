package Code.Human;
import Code.Relationship.*;
import Code.Politics.Office;
import Code.Politics.Title;
import Code.Common.Name;
import Code.Common.Basic;
import Code.House.House;
import java.util.List;
import java.util.ArrayList;
import java.util.Calendar;

public class Woman extends Human {
	protected SexRelation uterus;
	protected int mop;							//month of pregnancy
	protected Office eop;						//events of pregnancy
	protected static List<Human> elders =	  	new ArrayList<>();
	public static List<Human> women = 		new ArrayList<>();
	protected static List<Human> children = 	new ArrayList<>();
	public static List<Human> pregnant = 	new ArrayList<>();
	public static List<Human> singles = 		new ArrayList<>();
	private final static String[] relation = 	{"daughter", "granddaughter", "great-granddaughter", 	"great-great-granddaughter"};

	private static List<Human> childBridePool = new ArrayList<>();
//generated
    public Woman(int age){
		super(age);
		this.name = 		new Name(true, this, true);
		setUp();
		this.name.setFull(this.makeName());
		this.becomeSingle();
	}

//genetically born
	public Woman(int y, boolean b){
 	   super(y, false);
	   setUp();
    }

//naturally born
	public Woman(){
 	   super();
	   setUp();
    }

	public void setUp(){
		this.sex = true;
 	  	this.mop = -1;
 	  	this.religion.getParish().add(this);
 	  	women.add(this);
	}

	@Override
	public String getShortName(){
		return this.getForename();
	}

	@Override
	public String getFormalName(){
		String n = this.getForename();
		if (this.getName().hasRegnal()){
			return this.getFullName();
		} else if (this.title == null){
			if (this.hadFather()){
				return n+" "+this.getHouse().getName();
			} else {
				return n;
			}
		}
		if (this.hasTitle(Title.PRINCESS)){
			return n+", "+Basic.getOrdial(this.getCadency())+" "+this.getTitleS();
		} else if (this.hasTitle(Title.QUEENCONSORT)){
			return n+", "+Basic.getOrdial(this.getOffice().getConsortRank(this))+" "+this.getTitleS();
		} else {
			return n+", "+this.getTitleS();
		}
	}


	@Override
    public String makeName(){
		String s = this.getShortName();
		if (this.getName().hasNick()){
			s +=" "+this.getName().getNickname();
		}
		if (this.house != null){
			if (this.title == null){
				return s;
			} else{
				if (this.hasTitle(Title.PRINCESS)){
					return s+", "+Basic.getOrdial(this.getCadency())+" "+this.getTitleS();
				} else if (this.hasTitle(Title.QUEENCONSORT)){
					return s+", "+Basic.getOrdial(this.getOffice().getConsortRank(this))+" "+this.getTitleS();
				} else {
					return s+", "+this.getTitleS();
				}
			}
		}
		return s;
	}



    @Override
    public void saunter(int x){
        switch(x){
			case 20:
				if (this.isAlive()){ this.childbirth();}
				break;
            case 0:
				if (this.isAlive()){ this.kill((byte) 0);}
				break;
        }
    }

	@Override
	public void clean(){
		if (this.isAdult() && this.relSta <= 1) {
			this.becomeTaken();
			if (this.isRelSta(3)){
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
			int f = Basic.randint(maom)+1;
			Basic.dayC.get(f).add(this); Basic.dayE.get(f).add(20);
		}
	}

	@Override
	public void childbirth(){
		if (Basic.randint(10) != 0){					//Stillbirth? this.relSta != 4
			this.deliver(this.uterus);
			if (Basic.randint(150) == 0){
				this.deliver(this.uterus);			//Twins?
			}
		}
		if (Basic.randint(50) == 0){					//Maternal death?
			this.kill((byte) 10);
		}

		else {
			this.drainUterus();
			//Posthumous
			if (this.isRelSta(4)){
				this.becomeWidow();
			}
		}
	}
	public boolean isPregnant(){ 			return this.uterus != null; 	}
	public void fillUterus(SexRelation sperm){ 		this.uterus = sperm; 	}
	public void growFetus(){				this.mop++; }

	public void drainUterus(){
		this.uterus = null;
		this.mop = -1;
		Woman.pregnant.remove(this);
	}

	public static Human beBornSpeacial(Human f, Human m, int y){
		Human it = new Woman(y, false);
		Woman.children.add(it);
		it.setParents(f, m);
		it.house = f.house;
		it.addToHouse();
		Name.aDaughter(f, m, it);
		it.addDaughter(f, m);
		it.getName().setOwner(it);
		it.cadency = f.getLivingDaughters().size()-1;
		return it;
	}

	public static Human beBorn(Human f, Human m){
		Human it = new Woman();
		Woman.children.add(it);
		return it;
	}

	public void handlePostBirth(Human f, Human m){
		this.setParents(f, m);
		this.nameChild();
		this.addDaughter(f, m);
		this.house = f.house;
		this.addToHouse();
		if (f.isAlive() || this.hasSister()){
			this.cadency = f.getLivingDaughters().size()-1;
		} else{
			this.cadency = 0;
		}
	}

	public void handleBastardBirth(Human f, Human m){
		this.setParents(f, m);
		Name.createFemaleName(this);
		this.addDaughter(f, m);
		//this.house = 		new MainHouse(this);
		this.house = f.house;
		this.addToHouse();
		this.cadency = 0;
	}

	public void nameChild(){;
		Name.aDaughter(this.getFather(), this.getMother(), this);
	}

	@Override
	public void becomeSingle(){
		singles.add(this);
		this.setSpouseNull();
		this.mating = Mating.revaluateF(this);
	}

	@Override
	public void becomeWidow(){
		this.setSpouseNull();
		if (!this.isPregnant()){
			if (this.isMarriageable()){
				this.handleWidowhood();
			} else{
				this.setRelSta(3);
			} //Posthumous pregnancy
		} else{
			this.setRelSta(4);
		}
	}

	@Override
	public void handleWidowhood(){
		Human h = this.getLatestHusband();;
		this.becomeSingle();
		this.setRelSta(1);
		if(h.hasUnwedSameSexSibling()){
		//	Human d = h.getUnwedBrother();
			Marriage.doLevirate(this, h);
		//	System.out.println(this.isMarriedTo(d));
		//	Basic.pause(1000);
		}
	}

	@Override public boolean hasUnwedSameSexSibling(){ return this.hasUnwedSister();}


	@Override
	public void becomeTaken(){				singles.remove(this); }

	@Override
	public void bury(){
		List<Human> h;
		if (this.hadHouse()){
			this.getHouse().removeKinswoman(this);
			if (this.hasSister()){
				h = this.getFather().getLivingDaughters();
				for (int x = 0; x < h.size(); x++){
					h.get(x).cadency = x;
				}
			}
		}

		this.religion.getParish().remove(this);
		women.remove(this);
		if (this.isHost()){
			this.getHostHouse().findHost();
		}

	/*	if (this.title == Title.QUEEN){
			Holder.getLatestSovereign().resetConsort();
		}*/
		//Mother dies while pregnant
		if (this.isAdult()){
			if (this.uterus != null){;
				pregnant.remove(this);
			}
		} else{
			if (this.isOverAgeOf(8)){
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
			x.reachAdulthood();
		}
	}

	@Override
	public void reachAdulthood(){
		Woman.children.remove(this);
		this.fertility = 		Basic.randint(101);
		this.personality =		new Personality();
		this.rela.reachAdulthood();
		if (this.isUnwed()){
			childBridePool.remove(this);
			this.becomeSingle();
		} else {
			Marriage.marryBetrothed(this.getSpouse(), this);
		}

	//	if (x.title == Title.QUEENREGNANT){ Regent.lift(); }
	}

	@Override
	public boolean isMarriageable(){
		if (this.isOverAgeOf(40)){ return false; }
		else{ return true; }
	}

	public void clearLovers(){
		List<Affair> l = this.getAffairs();
		for(Affair x: l){
			x.getBeau().endAffair(x);
			x.remove();
		}
		this.clearAffairs();
	}

	@Override
    public String getSibling(){		return "sister";}

	@Override
    public String getLoverGroup(){	return "beaux";}

	@Override
	public void sRename(Title title){
		this.title = title;
		this.name.setFull(this.makeName());
		//this.getName().setFull(this.getForename());
	}



	public void princify(){
		this.title = Title.PRINCESS;
		this.rename(title.PRINCESS);
	}

	public boolean isPrinceling(){
		return this.title == title.PRINCESS;
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

	public boolean isMinor(){		return this.isUnderAgeOf(12); }

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

	@Override
	public Human getLoverfromAffair(Affair h){	return h.getStag();	}

	@Override
	public int getSexChildOrder(){
		List<Human> l = this.getFather().getDaughters();
		return l.indexOf(this);
	}

	@Override
	public String getAgnaticOrderStr(){
		return this.getBirthOrderRank(this.getAgnaticOrder())+" daughter";
	}

	@Override
	public int getAgnaticOrder(){
		return this.getBirthOrderRank(this.getFather().getLegitDaughters());
	}

	@Override
	public String getEnaticOrderStr(){
		return getBirthOrderRank(this.getEnaticOrder())+" daughter";
	}

	@Override
	public int getEnaticOrder(){
		return this.getBirthOrderRank(this.getMother().getLegitDaughters());
	}

	public void addToElders(){							elders.add(this); 	 	}
	public void removeFromElders(){						elders.remove(this); 	}
	public static int getAmount(){						return women.size();	}
	public static List<Human> getWomen(){				return new ArrayList<>(women);}
	public static int getSingleNum(){					return singles.size();  }
	public static List<Human> getSingles(){				return new ArrayList<>(singles);  }

	//Reproductive women
	public static List<Human> getRepWoman(){
		List<Human> l = getWomen();
		List<Human> ll = new ArrayList<>(l.size());
		for(Human x: l){
			if (x.isAdult() && x.isUnderAgeOf(45)){
				ll.add(x);
			}
		}
		return ll;
	}

//Statistics

	public static int getPerOfSingles(){	return calcPerSingles(getWomen());	}

	public static int getPerOfBachelors(){	return calcPerBachelors(getWomen());}

	public static int getPerOfWidowed(){	return calcPerWidowed(getWomen());	}

	public static int getPerOfParents(){	return calcPerParents(getWomen());  }

	public static int getPerOfMarriedRepWomen(){
		int i = 0;
		List<Human> l = getRepWoman();
		for(Human x: l){
			if (((Woman) x).isMarried()){
				i++;
			}
		}
		return (int) (((i+0.0f)/(l.size()+0.0f))*100);
	}

	public static int getPerOfPregnantWomen(){
		int i = 0;
		List<Human> l = getRepWoman();
		for(Human x: l){
			if (((Woman) x).isPregnant()){
				i++;
			}
		}
		return (int) (((i+0.0f)/(l.size()+0.0f))*100);
	}




	public static int getPerOfChildren(){
		float i = (Human.getNumOfChildren(getWomen())+0.0f)/getAmount();
		return (int) (i*100);
	}

	public static int getPerOfElderly(){
		float i = (Human.getNumOfElderly(getWomen())+0.0f)/getAmount();
		return (int) (i*100);
	}

	//Find a lowborn young woman to a noble, preferably unmarried
	public static Human findWench(){
		List<Human> l = filterWench(getSingles());
		if (Basic.isNotZero(l.size())){
			return Basic.choice(l);
		} else {
			l = filterSuitableWench(getWomen());
			if(Basic.isNotZero(l.size())){
				Human h = Basic.choice(l);
				if (h.isMarried()){
					h.getLatestMarriage().getDivorce();
					return h;
				} else {
					throw new RuntimeException();
				}
				//return Basic.choice(l);
			}
		}
		return null;
	}

	public static List<Human> filterWench(List<Human> l){
		List<Human> ll = new ArrayList<>(l.size());
		for (Human x: l){
			if (x.isAdult() && x.isUnderAgeOf(20)){
				if (x.isPeasant()){
					ll.add(x);
				}
			}
		}
		return ll;
	}

	//Filter out married women who are married to a nobleman
	public static List<Human> filterSuitableWench(List<Human> l){
		l = filterWench(l);
		List<Human> ll = new ArrayList<>(l.size());
		for (Human x: l){
			if (x.getSpouse().isPeasant()){
				ll.add(x);
			}
		}
		return ll;
	}


	@Override
	public String getMaritalBio(){
		String s = "";
		List<Marriage> l;
		if (this.isAdult()){
			l = this.getMarriages();
			for(Marriage x: l){
				s += x.getHTMLPrefixF();
			}
		}
		return s;
	}

}
