import java.util.List;
import java.util.ArrayList;
import java.util.Calendar;

public class Human {
	protected Calendar death;
	protected Courtier courtier;
	protected House house;  								//Mont of pregnancy
	protected Human mother;
	protected Human spouse;
	protected int cadency;
	protected int fertility;
	protected int gen;
	protected int mating;
	protected int relSta; 									//relationship status
	protected List<Event> events;
	protected List<Human> daughters;
	protected List<Human> sons;
	protected Religion religion;
	protected String DNA;
	protected Title title;
    protected boolean sex;
    protected Calendar birth;
    protected Human father;
    protected List<Marriage> marriages;
    protected Name name;
	protected PolProfile polProfile;
	protected Office office;
	protected ManorLord manorLord;
	protected int fund;
    protected static int id = 								0;
    protected static List<Human> living = 				new ArrayList<>();
	//0 = single/virgin
	//1 = widower/widow
	//2 = husband/wife
	//3 = celibate
	//4 = grieving widow (posthumous pregnancy)


//generated
    public Human(int age){
        this.id++;
        this.living.add(this);
        this.birth = 		(Calendar) Main.date.clone();
		this.birth.add(Calendar.DATE, -365*age);
		this.events =		 new ArrayList<>();
		this.relSta = 		0;
		this.religion = 	Religion.getLatest();
		this.fertility = 	Main.randint(91)+10;
		this.marriages = 	new ArrayList<>();
		this.sons = 		new ArrayList<>();
		this.daughters = 	new ArrayList<>();
		this.DNA = 			Eye.getRandom()+Hair.getRandom();
		this.gen =			0;
		this.fund = 		0;
		Main.human.put(Human.id, this);
    }

//naturally born
	public Human(){
 	   this.id++;
 	   this.living.add(this);
 	   this.birth = 	(Calendar) Main.date.clone();
 	   this.events = 	new ArrayList<>();
 	   this.relSta = 	0;
 	   this.religion = 	Religion.getLatest();
	   this.fund = 		0;
	   Main.human.put(Human.id, this);
    }


	public int aged(){
		if (!this.isAlive()){
			return this.death.get(Calendar.YEAR)-this.birth.get(Calendar.YEAR);
		}
		return this.getAge();
	}

	public List<Human> children(){
		List<Human> children = new ArrayList<>();
		if (this.sons != null){children.addAll(this.sons);}
		if (this.daughters != null){children.addAll(this.daughters);}
		return children;
	}

	public List<Human> livingChildren(){
		List<Human> children = new ArrayList<>();
		if (this.sons != null){
			children.addAll(this.getLivingSons());
			children.addAll(this.getLivingDaughters());
		}
		return children;
	}

	public boolean hasSon(){
		for (Human x: this.sons){
			if (x.isAlive()){
				return true;
			}
		}
		return false;
	}

	public boolean hasSons(){
		int c = 0;
		for (Human x: this.sons){
			if (x.isAlive()){
				c++;
				if (c >= 2){
					return true;
				}
			}
		}
		return false;
	}

	public List<Human> getLivingSons(){
		List<Human> list = new ArrayList<>();
		if (this.isAdult()){
			for (Human x: this.sons){
				if (x.isAlive()){
					list.add(x);
				}
			}
		}
		return list;
	}

	public Human getLivingSon(){
		Human s = this.sons.get(0);
		for (Human x: this.sons){
			if (x.isAlive()){
				s = x;
				break;
			}
		}
		return s;
	}

	public Human getOldestSon(){
		for (Human x: this.sons){
			if (x.isAlive()){
				return x;
			}
		}
		return null;
	}

	public Human getOldestDaughter(){
		for (Human x: this.daughters){
			if (x.isAlive()){
				return x;
			}
		}
		return null;
	}

	public List<Human> getLivingDaughters(){
		List<Human> list = new ArrayList<>();
		for (Human x: this.daughters){
			if (x.isAlive()){
				list.add(x);
			}
		}
		return list;
	}

//Paternal grandfather
	public boolean hadPatGrandpa(){
		if (this.hadFather()){
			return this.getFather().hadFather();
		}
		return false;
	}

//Paternal great-grandfather
	public boolean hadPatGreatGrandpa(){
		if (this.hadFather()){
			return this.getFather().hadPatGrandpa();
		}
		return false;
	}

	public Human getPatGreatGrandpa(){
		return this.getFather().getFathersFather();
	}

//Maternal grandfather
	public boolean hadMatGrandpa(){
		if (this.hadMother()){
			return this.getMother().hadFather();
		}
		return false;
	}

	public Human getPatruus(){
		Human u = this.getFathersFather();
		for(Human x: u.getLivingSons()){
			if (this.getFather() != x){
				return x;
			}
		}
		return null;
	}

	public void deliver(Human father){
		String g;
		Human it;
		if (Main.randint(2) == 0){
			it = Man.beBorn(father, this);
		} else {
			it = Woman.beBorn(father, this);
		}
		it.gen = father.getGen() + 1;
		it.DNA = Eye.getGenetic(father.getEye(), this.getEye())+""+Hair.getGenetic(father.getHair(), this.getHair());
		this.marriages.get(this.marriages.size()-1).addOffspring(it);
		if (father.isRegnant()){
			it.princify();

			//it.getName().setNick("Commod");
		}
		Main.print(this.getName().getFull()+" gave birth to "+it.getName().getFull());
	}

	public void performPosthumousBirth(Human f, Human m){
		Main.print(m.getName().getFull()+" gave posthumous birth to "+f.getName().getFull());

	}

    public void kill(){
        Human.living.remove(this);
		this.death = (Calendar) Main.date.clone();
		this.bury();
        if (!this.isUnwed()){
            Main.print(String.format("%s deceased, leaving %s a %s",
				this.getName().getFull(),
				this.spouse.getName().getFull(),
				this.spouse.widow()));
			this.marriages.get(this.marriages.size()-1).terminate();
			this.spouse.spouse = null;
			this.spouse.becomeWidow();
        } else {
			this.clean();
			Main.print(String.format("%s deceased", this.getName().getFull()));
		}
		this.review();
		if (this.isManorLord()){
			this.getManorLord().depart();
		}
    }

	public void review(){
		if (this.isPolitican()){
			//System.out.println(this.getPolProfile().getRegnal().size());
			this.getPolProfile().handleDeath();

		}

/*		try{
			for(Event x: this.events){
		        switch(x){
			//		case E101:
			//			if (Holder.getLatestSovereign().getEvents().contains(Event.E201)){
			//				Regent.dismiss();					//Holder dies while he has a regent
			//			}
			//			Office.endTenure();
			//			break;
//					case E200:
//						Holder.getLatestSovereign().appointRegent();		//Regent dies, new regent is appointed
//						break;
		        }
			}
		}
		catch (Error e){
			System.out.println("E");
			System.exit(0);
		}*/
    }


//Naming methods
	public void rename(Title title){
		if (this.title == null || title.prestige >= this.title.prestige){
			this.title = title;
			this.getName().setFull(this.makeName());
		}
	}

	public List<String> getLivingSonsNames(){
		List<Human> ls =		this.getLivingSons();
		List<String> names = 	new ArrayList<>();
		List<Human> s = 		new ArrayList<>();
		for(int x = 0; x < ls.size()-1; x++){
			if(!ls.get(x).getName().isSpecial()){
				s.add(ls.get(x));
			}
		}
		for(int x = 0; x < s.size(); x++){
			names.add(s.get(x).getName().getName());
		}
		return names;
	}

	public List<String> getLivingDaughtersNames(){
		List<Human> ld =		this.getLivingDaughters();
		List<String> names = 	new ArrayList<>();
		List<Human> d = 		new ArrayList<>();
		for(int x = 0; x < ld.size()-1; x++){
			if(!ld.get(x).getName().isSpecial()){
				d.add(ld.get(x));
			}
		}
		for(int x = 0; x < d.size(); x++){
			names.add(d.get(x).getName().getName());
		}
		return names;
	}

	public boolean hasSonWithTheName(String n){
		for (Human x: this.sons){
			if (x.isRegistered() && x.getName().getName().equals(n)){
				return true;
			}
		}
		return false;
	}

	public boolean hasDaughterWithTheName(String n){
		for (Human x: this.daughters){
			if (x.isRegistered() && x.getName().getName().equals(n)){
				return true;
			}
		}
		return false;
	}

//Analytics
	public boolean isRegistered(){
		return this.isAlive() && this.isGen();
	}

	public String getLifespan(){
		String x = Integer.toString(this.birth.get(Calendar.YEAR));
		if(!this.isAlive()){
			if(this.birth.get(Calendar.YEAR) != this.death.get(Calendar.YEAR)){
				x += "–";
				if (this.isAlive() == false){
					x = x+this.death.get(Calendar.YEAR);
				}
			}
			return x;
		} else{
			return x+"–PRSN";
		}
	}


	public boolean isSonless(){
		for (Human x: this.sons){
			if (x.isAlive()){
				return true;
			}
		}
		return false;
	}

	public boolean isFitForMarriage(){
		if (this.isUnwed() && this.isMarriageable()){
			return true;
		}
		return false;
	}

	public int getNumOfLivingSiblings(){
		int n = 0;
		if (this.hadFather()){
			for (Human x:this.getFather().sons){
				if (x.isAlive()){
					n++;
				}
			}
			for (Human x:this.getFather().daughters){
				if (x.isAlive()){
					n++;
				}
			}
		}
		return n;
	}

	public boolean hasUnwedBrother(){
		if (this.hadFather()){
			for(Human x: this.getFather().getLivingSons()){
				if (x.isAdult() && x.isFitForMarriage()){ return true;}
			}
		}
		return false;
	}

	public boolean hasUnwedSister(){
		if (this.hadFather()){
			for(Human x: this.getFather().getLivingDaughters()){
				if (x.isAdult() && x.isFitForMarriage()){ return true;}
			}
		}
		return false;
	}

	public Human getUnwedBrother(){
		for(Human x: this.getFather().getSons()){
			if (x.isAlive() && x.isAdult()){
				if (x.isFitForMarriage()){
					return x;
				}
			}
		}
		return null;
	}

	public Human getUnwedSister(){
		for(Human x: this.getFather().getDaughters()){
			if (x.isAlive() && x.isAdult()){
				if (x.isFitForMarriage()){
					return x;
				}
			}
		}
		return null;
	}

	public boolean hasBrother(){
		List<Human> sons = this.getFather().getLivingSons();
		for (Human x: sons){
			if (x != this){
				return true;
			}
		}
		return false;
	}

	public boolean hasSister(){
		List<Human> dau = this.getFather().getLivingDaughters();
		for (Human x: dau){
			if (x != this){
				return true;
			}
		}
		return false;
	}

	public boolean hasPaternalPibling(){
		int n = this.getFather().getNumOfLivingSiblings();
		if (n > 1){
			return true;
		} else if (n == 1 && this.fatherIsDead()){
			return true;
		}
		return false;
	}

	public boolean hasMaternalPibling(){
		int n = this.getMother().getNumOfLivingSiblings();
		if (n > 1){
			return true;
		} else if (n == 1 && this.motherIsDead()){
			return true;
		}
		return false;
	}

	//Is chara parent's siblings are alive
	public boolean hasPibling(){
		if (this.hasFather() && this.hadPatGrandpa()){
			if (this.hasPaternalPibling() || this.hasMaternalPibling()){
				return true;
			}
		}
		return false;
	}


	public Human getOldestBrother(){
		List<Human> sons = this.getFather().getLivingSons();
		for (Human x: sons){
			if (x != this){
				return x;
			}
		}
		return null;
	}

	public boolean hasAdultBrother(){
		Human brother;
		if (this.hadFather() && this.hasBrother()){
			brother = this.getOldestBrother();
			if (brother.isAdult()){
				return true;
			}
		}
		return false;
	}

	//Does character's father have a living brother?
	public boolean hasPatruus(){
		if (this.hadPatGrandpa()){
			return this.getFather().hasBrother();
		}
		return false;
	}

	//paternal uncles
	public List<Human> getPatrui(){
		List<Human> list = new ArrayList<>(this.getFathersFather().getSons());
		list.remove(this.getFather());
		return list;
	}

	public boolean hasPaternalNephew(){
		List<Human> uncles;
		if (this.hasPatruus()){
			uncles = new ArrayList<>(this.getPatrui());
			for(Human x: uncles){
				if(x.isAdult()){
					if (x.hasSon()){
						return true;
					}
				}
			}
		}
		return false;
	}

	//most senior
	public Human getPaternalNephew(){
		List<Human> uncles = new ArrayList<>(this.getPatrui());
		for(Human x: uncles){
			if (x.isAdult()){
				for(Human y: x.getSons()){
					if(y.isAlive()){
						return y;
					}
				}
			}
		}
		return null;
	}


	public boolean hasSeniorPaternalRelative(){
		if (this.hasFather()){
			return true;
		} else if (this.hasFatherOrUncle()){
			return true;
		} else{
			if (this.hasGrandpaOrUncle()){
				return true;
			}
		}
		return false;
	}

	public boolean hasFatherOrUncle(){
		if (this.hadPatGrandpa()){
			return this.getFathersFather().hasSon();
		}
		return false;
	}

	public boolean hasGrandpaOrUncle(){
		if (this.hadPatGreatGrandpa()){
			return this.getPatGreatGrandpa().hasSon();
		}
		return false;
	}

	public boolean hasFirstCousin(){
		if (this.hadFather() && this.hadPatGrandpa()){
			for (Human x: this.getFathersFather().children()){
				if (x.isAdult() && x != this.getFather()){
					for (Human y: x.children()){
						if (y.isAlive()){
							return true;
						}
					}
				}
			}
		} else if (this.hadMother() && this.hadMatGrandpa()){
			for (Human x: this.getMothersFather().children()){
				if (x.isAdult() && x != this.getMother()){
					for (Human y: x.children()){
						if (y.isAlive()){
							return true;
						}
					}
				}
			}
		}

		return false;
	}

	public List<Human> getLivingFirstCousins(){
		List<Human> l = new ArrayList<>();
		if (this.hadFather() && this.hadPatGrandpa()){
			for (Human x: this.getFathersFather().children()){
				if (x.isAdult() && x != this.getFather()){
					for (Human y: x.children()){
						if (y.isAlive()){
							l.add(y);
						}
					}
				}
			}
		}
		if (this.hadMother() && this.hadMatGrandpa()){
			for (Human x: this.getMothersFather().children()){
				if (x.isAdult() && x != this.getMother()){
					for (Human y: x.children()){
						if (y.isAlive()){
							l.add(y);
						}
					}
				}
			}
		}
		return l;
	}



	public void switchHouse(House h){
	//	System.out.println(this+" was moved from "+this.getHouse()+" to "+h);
		if (this.isAlive()){
			this.getHouse().removeKinsman(this);
			h.addKinsman(this);
		}
		for (Human x: this.children()){
			x.switchHouse(h);
		}
		this.house = h;
	}


	public int getAge(){
		return Main.date.get(Calendar.YEAR)-this.birth.get(Calendar.YEAR);
	}

	public Human getLatestHusband(){
		return this.marriages.get(this.marriages.size()-1).getHusband();
	}

	public Human getLatestWife(){
		return this.marriages.get(this.marriages.size()-1).getWife();
	}

	public boolean isSiblingOf(Human i){
		return this.getFather() == i.getFather() && this.hasFather();
	}

	public boolean hadParents(){
		return this.hadFather() && this.hadMother();
	}

	public Human getFathersFather(){
		return this.getFather().getFather();
	}

	public Human getFathersMother(){
		return this.getFather().getMother();
	}

	public Human[] getFathersParents(){
		Human[] l = new Human[]{this.getFathersFather(), this.getFathersMother()};
		return l;
	}

	public Human getMothersFather(){
		return this.getMother().getFather();
	}

	public Human getMothersMother(){
		return this.getMother().getMother();
	}

	public Human[] getMothersParents(){
		Human[] l = new Human[]{this.getMothersFather(), this.getMothersMother()};
		return l;
	}

	public Human[] getGrandparents(){
		Human[] l = new Human[4];
		Human[] l1 = this.getFathersParents();
		Human[] l2 = this.getMothersParents();
		for (int x = 0; x < 2; x++){
			l[x] = l1[x];
		}
		for (int x = 2; x < 4; x++){
			l[x] = l2[x-2];
		}
		return l;
	}

	public Human[] getGreatGrandparents(){
		Human[] l = new Human[8];
		Human[] l1 = this.getFather().getGrandparents();
		Human[] l2 = this.getMother().getGrandparents();
		for (int x = 0; x < 4; x++){
			l[x] = l1[x];
		}
		for (int x = 4; x < 8; x++){
			l[x] = l2[x-4];
		}
		return l;
	}

	public Human[] get2ndGreatGrandparents(){
		Human[] l = new Human[16];
		Human[] l1 = this.getFather().getGreatGrandparents();
		Human[] l2 = this.getMother().getGreatGrandparents();
		for (int x = 0; x < 8; x++){
			l[x] = l1[x];
		}
		for (int x = 8; x < 16; x++){
			l[x] = l2[x-8];
		}
		return l;
	}

	public boolean hadGrandparents(){
		return this.getFather().hadParents() && this.getMother().hadParents();
	}


	public boolean hadGreatGrandparents(){
		for(Human x: this.getGrandparents()){
			if (!x.hadParents()){
				return false;
			}
		}
		return true;
	}

	public boolean had2ndGreatGrandparents(){
		for(Human x: this.getGreatGrandparents()){
			if (!x.hadParents()){
				return false;
			}
		}
		return true;
	}

	public boolean had3rdGreatGrandparents(){
		for(Human x: this.get2ndGreatGrandparents()){
			if (!x.hadParents()){
				return false;
			}
		}
		return true;
	}

	//How many generations of ancestry does the person have
	public int getAncestryRating(){
		if (this.hadParents()){
			if (this.hadGrandparents()){
				if (this.hadGreatGrandparents()){
					if (this.had2ndGreatGrandparents()){
						if (this.had3rdGreatGrandparents()){
							return 5;
						}
						return 4;
					}
					return 3;
				}
				return 2;
			}
			return 1;
		}
		return 0;
	}

	public String getPossibleDeath(){
		if (!this.isAlive()){
			return this.getDeath();
		} else{
			return "PRSN";
		}
	}

//Title methods

	public boolean isRegnant(){
		if (this.isPolitican() && this.getPolProfile().isRegnant()){
			return true;
		}
		return false;
	}

	public void addRegnalTitle(Office o){
		PolProfile p = this.getPolProfile();
		if (!this.isPolitican()){
			p =  new PolProfile(this);
			this.polProfile = p;
			p.makeRegnant();
		} else if (!p.isRegnant()){
			p.makeRegnant();
		}
		p.addRegnalTitle(o);
	}

//Micro methods
	public boolean isFemaleAdult(){				return this.isFemale() && isAdult(); }
	public boolean fatherIsDead(){				return !this.getFather().isAlive(); }
	public boolean hadBrother(){				return this.getFather().getNumOfSons() > 1; }
	public boolean hadFather(){					return this.father != null;		}
	public boolean hadMother(){					return this.mother != null;		}
	public boolean hasFather(){					return this.getFather().isAlive();	}
	public boolean hasUnwedSameSexSibling(){	return false;					}
	public boolean isAdult(){					return this.sons != null;		}
	public boolean isAlive(){					return this.death == null;		}
	public boolean isBrotherOf(Human h){		return this.father == h.father;	}
	public boolean isChildOf(Human h){			return false; 					}
	public boolean isFemale(){					return this.sex == true;		}
	public boolean isGen(){						return this.gen != 0;			}
	public boolean isMale(){					return this.sex == false;		}
	public boolean isMarriageable(){ 			return true; 					}
	public boolean isMarried(){ 				return this.spouse != null;		}
	public boolean isMarriedTo(Human s){ 		return this.spouse == s;		}
	public boolean isPolitican(){				return this.polProfile != null; }
	public boolean isUnwed(){					return this.spouse == null;		}
	public boolean isVirgin(){ 					return false; 					}
	public boolean motherIsDead(){				return !this.getMother().isAlive(); }
	public House getHouse(){					return this.house; 				}
	public Human getFather(){					return this.father; 			}
	public Human getMother(){					return this.mother; 			}
	public Human getSpouse(){					return this.spouse; 			}
	public int getEye(){						return Character.getNumericValue(this.DNA.charAt(0)); }
	public int getGen(){						return this.gen;				}
	public int getHair(){						return Character.getNumericValue(this.DNA.charAt(1));}
	public int getNumOfSons(){					return this.sons.size();		}
	public List<Human> getDaughters(){			return this.daughters;			}
	public List<Human> getSons(){				return this.sons;				}
	public Name getName(){ 						return this.name; 				}
	public PolProfile getPolProfile(){			return this.polProfile;			}
	public static int getID(){					return id;						}
	public static int getNumLiving(){			return living.size();			}
	public String child(){						return "";						}
	public String getBirth(){					return Main.format1.format(this.birth.getTime());}
	public String getDeath(){					return Main.format1.format(this.death.getTime());}
	public String getNameS(String title){		return title;					}
	public String getTitleS(){					return this.title.getName();	}
	public String getNibling(){ 				return "nibling";}
	public String getOffspring(){ 				return "son";					}
	public String getParent(){ 					return "parent";}
	public String getPibling(){ 				return "pibling";}
	public Office getOffice(){					return this.office;				}
	public String getPortrait(){ 				return "";						}
	public String getPronoun(){					return "he"; 					}
	public String getPossessive(){				return "his"; 					}
	public String getRelation(int v){ 			return ""; 						}
	public String getShortName(){				return "?";						}
	public String getFormalName(){				return "?";						}
	public String getSibling(){					return "sibling";				}
	public String makeName(){ 					return "";}
	public String widow(){ 						return "";						}
	public Title getRoyalTitle(){				return Title.KING;				}
	public void addToElders(){};
	public void addToHouse(){					this.house.addKinsman(this);	}
	public void becomeSingle(){};
	public void becomeTaken(){};
	public void becomeWidow(){};
	public void bury(){};
	public void childbirth(){}
	public void clean(){};
	public void handleWidowhood(){;}
	public void getPaid(int m){					this.fund+= m;					}
	public int getFund(){						return this.fund;				}
	public void ovulate(int x){;};
	public void removeFromElders(){};
	public void setHouse(House n){				this.house = n; 				}
	public void setName(Name n){ 				this.name = n; 					}
	public void sRename(Title title){}
    public void saunter(int x){;}
	public int getCadency(){					return this.cadency;			}
	public void princify(){;}
	public boolean hasTitle(Title t){			return this.title == t;			}
	public void setOffice(Office o){			this.office = o;				}
	public ManorLord getManorLord(){			return this.manorLord;			}
	public boolean isManorLord(){				return this.manorLord != null;	}
	public void makeIntoManorLord(ManorLord l){	this.manorLord = l; 			}

}
