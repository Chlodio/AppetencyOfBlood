package Code.Human;

import java.util.List;
import java.util.ArrayList;
import java.util.Calendar;
import Code.Common.*;
import Code.Court.Courtier;
import Code.House.House;
import Code.Looks.*;
import Code.Ancestry.Bastard;
import Code.Politics.*;
import Code.Relationship.*;

public class Human {
	protected Calendar death;
	protected Courtier courtier;
	protected House house;  								//Mont of pregnancy
	protected int cadency;
	protected int fertility;
	protected int fund;
	protected int gen;
	protected int mating;
	protected int relSta; 									//relationship status
	protected Rela rela; 									//relationship status
	protected List<Event> events;
	protected ManorLord manorLord;
	protected Office office;
	protected PolProfile polProfile;
	protected Religion religion;
	protected String DNA;
	protected Title title;
    protected boolean sex;
    protected Calendar birth;
    protected Name name;
	protected Personality personality;
	private Minister role;									//Character's career as minister
    protected static int id = 								0;

    public static List<Human> living = 				new ArrayList<>();
	/*
		0 = single/virgin
		1 = widower/widow
		2 = husband/wife
		3 = celibate
		4 = grieving widow (posthumous pregnancy)
		5 = divorcee
	*/

	protected boolean[] chaBox;
	/*
		0 = legimate? i.e not a bastard
		1 = posthumous?
		2 = virgin?
	*/



//generated
    public Human(int age){
        this.id++;
        this.living.add(this);
        this.birth = 		(Calendar) Basic.date.clone();
		this.birth.add(Calendar.DATE, -365*age);
		this.events =		 new ArrayList<>();
		this.setRelSta(0);
		this.religion = 	Religion.getLatest();
		this.fertility = 	Basic.randint(91)+10;
		this.rela =			new Rela(this);
		this.DNA = 			Eye.getRandom()+Hair.getRandom();
		this.gen =			0;
		this.fund = 		0;
		this.chaBox = 		new boolean[]{true, false, false};
		this.personality =	new Personality();
		Basic.human.put(Human.id, this);
    }

//generated born
	public Human(int y, boolean b){
 	   this.id++;
 	   this.living.add(this);
 	   this.birth = 	(Calendar) Basic.date.clone();
	   this.birth.add(Calendar.DATE, -365*y);
 	   this.events = 	new ArrayList<>();
	   this.setRelSta(0);
 	   this.religion = 	Religion.getLatest();
	   this.fertility = -1;
	   this.rela =		new Rela(this, false);
	   this.fund = 		0;
	   Basic.human.put(Human.id, this);
	   this.chaBox = 	new boolean[]{true, false, true};
    }



//naturally born
	public Human(){
 	   this.id++;
 	   this.living.add(this);
 	   this.birth = 	(Calendar) Basic.date.clone();
 	   this.events = 	new ArrayList<>();
	   this.setRelSta(0);
 	   this.religion = 	Religion.getLatest();
	   this.fertility = -1;
	   this.rela =		new Rela(this, false);
	   this.fund = 		0;
	   Basic.human.put(Human.id, this);
	   this.chaBox = 	new boolean[]{true, false, true};
    }


	public void performPosthumousBirth(Human f, Human m){
		Basic.print(m.getFullName()+" gave posthumous birth to son of "+f.getFullName());

	}

    public void kill(){
        Human.living.remove(this);
		this.death = (Calendar) Basic.date.clone();
		this.bury();
        if (!this.isUnwed()){
			Human s = this.getSpouse();
            Basic.print(String.format("%s deceased, leaving %s a %s",
				this.getFullName(),
				s.getFullName(),
				s.widow()));
			this.getLatestMarriage().terminate();
			s.becomeWidow();
        } else {
			this.clean();
			Basic.print(String.format("%s deceased", this.getFullName()));
		}
		if (this.isActiveAdulterer()){
			this.clearLovers();
		}
		if (!this.isLegimate()){
			Bastard.remove(this);
		}
		this.review();
		if (this.isManorLord()){
			this.getManorLord().depart();
		}
    }

	public void review(){
		if (this.isPolitican()){
			this.getPolProfile().handleDeath();
		}
	}

//Naming methods
	public void rename(Title title){
		if (this.title == null || title.prestige >= this.title.prestige){
			this.title = title;
			this.getName().setFull(this.makeName());
		}
	}

	public String getBirthName(){
		if (this.hadFather()){
			return this.getName().getName()+" "+this.getHouse().getName();
		} else {
			return this.getName().getName();
		}
	}

	//Get names non-offspring relatives used for naming
	public List<String> getMaleAncestryGroupNames(){
		List<Human> l =  this.getMaleAncestryGroup();
		List<String> ll = new ArrayList<>();
		for(Human x: l){
			ll.add(x.getName().getName());
		}
		return ll;
	}

	//Get names non-offspring relatives used for naming
	public List<String> getFemaleAncestryGroupNames(){
		List<Human> l =  this.getFemaleAncestryGroup();
		List<String> ll = new ArrayList<>();
		for(Human x: l){
			ll.add(x.getName().getName());
		}
		return ll;
	}

	public List<String> getLivingSonsNames(){
		List<Human> l =		this.getLegitLivingSons();
		List<String> n = 	new ArrayList<>();
		for(Human x: l){
			n.add(x.getName().getName());
		}
		return n;
	}

	public List<String> getLivingDaughtersNames(){
		List<Human> l =		this.getLegitLivingDaughters();
		List<String> n = 	new ArrayList<>();
		for(Human x: l){
			n.add(x.getName().getName());
		}
		return n;
	}

	public boolean hasSonWithTheName(String n){
		List<Human> l = this.getSons();
		for (Human x: l){
			if (x.isRegistered() && x.getName().getName().equals(n)){
				return true;
			}
		}
		return false;
	}

	public boolean hasDaughterWithTheName(String n){
		List<Human> l = this.getDaughters();
		for (Human x: l){
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


	public boolean isFitForMarriage(){
		if (this.isUnwed() && this.isMarriageable()){
			return true;
		}
		return false;
	}

	//Move the house member into another house
	public void switchHouse(House h){
		if (this.isAlive()){
			this.getHouse().removeKinsman(this);
			h.addKinsman(this);
		}
		if (this.isAdult()){
			//Posthumous daughters are still part of the house...
			List<Human> l = this.getLegitNonPosthumousSons();
			l.addAll(this.getLegitDaughters());
			for (Human x: l){
				x.switchHouse(h);
			}
		}

		if (!this.isPatDescendantOf(h.getPatriarch() ) ){
			throw new RuntimeException();
		}

		this.setHouse(h);
	}


//Age methods

	public int getAge(){
		return Basic.getDaysLived(this.birth)/365;
	}

	public int getAgeDifference(Human a){
		return this.getAge()-a.getAge();
	}

	public int getAged(){
		if (!this.isAlive()){
			return this.death.get(Calendar.YEAR)-this.birth.get(Calendar.YEAR);
		}
		return this.getAge();
	}

	public int getAgeIn(Calendar d){
		return Basic.getDaysBetween(this.birth, d)/365;
	}

	public int getDaysIn(Calendar d){
		return Basic.getDaysBetween(this.birth, d);
	}

	//If age is above
	public boolean isOverAgeOf(int a){
		return this.getAge() >= a;
	}

	//If age is below
	public boolean isUnderAgeOf(int a){
		return this.getAge() < a;
	}

	//Check if age is exact
	public boolean isAtAgeOf(int a){
		return this.getAge() == a;
	}

	public boolean isOlderThan(Human h){
		return this.isOverAgeOf(h.getAge());
	}

	//Check if person is older than (h) plus additional years
	public boolean isOlderThan(Human h, int i){;
		return this.isOverAgeOf(h.getAge()+i);
	}


	public boolean isYoungerThan(Human h, int i){
		return this.isUnderAgeOf(h.getAge()-i);
	}

	public String getPossibleDeath(){
		if (!this.isAlive()){
			return this.getDeath();
		} else{
			return "PRSN";
		}
	}

	public Calendar getDeathDate(){	return this.death;	}

//Title methods

	public boolean isfromRegnantPaternalLine(){
		Human x = House.nextHouse.getHead();
		while(x.hadFather()){
			if (x.getFather().isRegnant()){
				return true;
			}
			x = x.getFather();
		}

		return false;
	}

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

//Family

	public static void createFamily(){
		int ha = 20+Basic.randint(25);				//husband age
		int my = Basic.randint(ha-14);				//marriage age
		int wa = 15+my;								//wife age
		Human f;
		Human m;
		new Man(ha);
		new Woman(wa);
		f = Basic.human.get(Human.getID()-1);
		m = Basic.human.get(Human.getID());
		Marriage.marrySpecial(f, m, my);
		for (int x = my; x >= 0; x--){
			if (Basic.randint(4) == 0){
				m.deliverSpecial(f, x);
			}
		}
	}

	public void deliverSpecial(Human f,int year){
		String g;
		Human it;
		if (Basic.randint(2) == 0){
			it = Man.beBornSpecial(f, this, year);
		} else {
			it = Woman.beBornSpeacial(f, this, year);
		}
		it.gen = f.getGen() + 1;
		it.DNA = Eye.getGenetic(f.getEye(), this.getEye())+""+Hair.getGenetic(f.getHair(), this.getHair());
		this.getLatestMarriage().addOffspring(it);
		if (!it.isMinor()){
			it.reachAdulthood();
		}
		it.addRealChild(f, this);
		it.addChild(f, this);
	/*	if (it.isMale() && !f.getHouse().getPrinces().contains(it)){
			throw new RuntimeException();
		}*/
	}

	public Human deliverBasic(SexRelation union){
		Human father = union.getStag();
		String g;
		Human it;
		if (Basic.randint(2) == 0){
			it = Man.beBorn(father, this);
		} else {
			it = Woman.beBorn(father, this);
		}
		it.gen = father.getGen() + 1;
		it.DNA = Eye.getGenetic(father.getEye(), this.getEye())+""+Hair.getGenetic(father.getHair(), this.getHair());
		union.addOffspring(it);
		return it;
	}


	public Human deliverLegimate(SexRelation sx){
		Human it = this.deliverBasic(sx);
		Human f = sx.getStag();
		it.setLegitimacy(true);
		if (!f.isAlive()){
			it.setPosthumous(true);
		}
		it.handlePostBirth(f, this);

		if (f.isRegnant()){
			it.princify();
		} else {
			it.setFullName(it.makeName());
		}
		Basic.print(this.getFullName()+" gave birth to "+it.getFullName());
		return it;
	}


	public Human deliverIllegimate(SexRelation sx){
		Human it = this.deliverBasic(sx);
		it.setLegitimacy(false);
		if (!sx.getStag().isAlive()){
			it.setPosthumous(true);
		}
		it.handleBastardBirth(sx.getStag(), sx.getDoe());
		Bastard.add(it);
		Basic.print(this.getFullName()+" gave birth to a bastard, "+it.getFullName());
		it.getName().setNick("the Bastard");

		return it;
	}


	public void deliver(SexRelation union){
		Human c;
		if (union instanceof Marriage){
			c = this.deliverLegimate(union);
		} else{
			if (union.getDoe().isMarried()){
				c = this.deliverLegimate(union.getDoe().getLatestMarriage());
				if (!(union.getDoe().getSpouse()).isFatherOf(c) ){
					throw new RuntimeException();

				}
			} else {
				c = this.deliverIllegimate(union);
			}
			c.setGenitor(union.getStag());
		}
		c.addChild(c.getFather(), this);
		c.addRealChild(union.getStag(), this);
		if (c.isMale() && !c.isPosthumous() && c.isLegimate() && !c.getHouse().patriarchIsSuited()){
			throw new RuntimeException();
		}


	}

	public void princifyChildren(){
		List<Human> l = new ArrayList<>(this.getLivingChildren());
		for(Human x: l){
			if(x.isLegimate()){
				x.princify();
			}
		}
	}

	public void setName(Name n){
		this.name = n;
		this.name.setOwner(this);
	}


//Statistics

	public static int calcPerSingles(List<Human> l){
		int num = 0;
		int i = 0;
		for(Human x: l){
			if (x.isAdult()){
				num++;
				if (!x.isMarried()){
					i++;
				}
			}
		}
		return (int) (((i+0.0f)/(num+0.0f))*100);
	}

	public static int calcPerBachelors(List<Human> l){
		int num = 0;
		int i = 0;
		for(Human x: l){
			if (x.isAdult()){
				num++;
				if (!x.wasMarried()){
					i++;
				}
			}
		}
		return (int) (((i+0.0f)/(num+0.0f))*100);
	}

	public static int calcPerWidowed(List<Human> l){
		int num = 0;
		int i = 0;
		for(Human x: l){
			if (x.isAdult()){
				num++;
				if (x.isWidowed()){
					i++;
				}
			}
		}
		return (int) (((i+0.0f)/(num+0.0f))*100);
	}

	public static int calcPerParents(List<Human> l){
		int num = 0;
		int i = 0;
		for(Human x: l){
			if (x.isAdult()){
				num++;
				if (x.hasChild()){
					i++;
				}
			}
		}
		return (int) (((i+0.0f)/(num+0.0f))*100);
	}

	public static int getPerOfSingles(){	return calcPerSingles(getLiving());		}

	public static int getPerOfBachelors(){	return calcPerBachelors(getLiving());	}

	public static int getPerOfWidowed(){	return calcPerWidowed(getLiving()); 	}

	public static int getPerOfParents(){	return calcPerParents(getLiving()); 	}

	public static int getNumOfElderly(List<Human> l){;
		int c = 0;
		for(Human x: l){
			if (x.isOverAgeOf(55)){
				c++;
			}
		}
		return c;
	}

	public static int getNumOfChildren(List<Human> l){;
		int c = 0;
		for(Human x: l){
			if (x.isChild()){
				c++;
			}
		}
		return c;
	}

	public static int getPerOfChildren(){
		float i = (getNumOfChildren(getLiving())+0.0f)/getNumOfLiving();
		return (int) (i*100);
	}

	public static int getPerOfElderly(){
		float i = (getNumOfElderly(getLiving())+0.0f)/getNumOfLiving();
		return (int) (i*100);
	}

	public static final String[] hairShort = {"BHA", "BRH", "BLH", "STH", "REH"};
	public static final String[] eyeShort = {"BRE", "BLE", "GRE"};

	public String getEyeShort(){	return eyeShort[this.getEye()]; 			}
	public String getHairShort(){	return hairShort[this.getHair()]; 			}

	public String getPortrait(){
		int age = this.getAged();
		String s;
		if (age >= 15){
			s = "<img src='../Input/Portraits/";
			s += this.getEyeShort();
			s += this.getHairShort();
			if (this.isMale()){
				s += "_M.png'</img>";
			} else {
				s += "_F.png'</img>";
			}
			return s;
		}
		return "";
	}

	public boolean isFromSameEstate(Human h){
		if (this.hadHouse() && h.hadHouse()){
			return this.getHouse().isNoble() == h.getHouse().isNoble();
		}
		return true;
	}

	public boolean hadHouse(){					return this.house != null;	}

	public String getHouseCoALink(){			return this.getHouse().getCoALink();			}


	public boolean isNoble(){					return this.getHouse().isNoble();				}

	public boolean isPeasant(){
		if (this.hadFather()){
			return !this.getHouse().isNoble();
		}
		return true;
	}

	//Will update all of people in the list
	public static void updateNamesOf(List<Human> l){
		for(Human x: l){
			x.setFullName(x.makeName());				//Make name is different for each sex
		}
	}

	//Check if human is alive and adult
	public boolean isLivingAdult(){
		return this.isAlive() && this.isAdult();
	}

	//Check if the list even has a single living person
	public static boolean hasLiving(List<Human> l){
		for(Human x: l){
			if (x.isAlive()){
				return true;
			}
		}
		return false;
	}

	//Get living from the list
	public static List<Human> getLiving(List<Human> l){
		List<Human> n = new ArrayList<>(l.size());		//Short for new list
		for(Human x: l){
			if (x.isAlive()){
				n.add(x);
			}
		}
		return n;
	}

	//As opposed to filtering out the dead, just get the first living that can be detected
	public static Human getFirstLiving(List<Human> l){
		for(Human x: l){
			if (x.isAlive()){
				return x;
			}
		}
		throw new RuntimeException();
	}

	//Will count the number of living
	public static int countLiving(List<Human> l){
		int c = 0;									//short for count
		for(Human x: l){
			if (x.isAlive()){
				c++;
			}
		}
		return c;
	}

	//Check if the list even has a single person non-posthumous person
	public static boolean hasNonPosthumous(List<Human> l){
		for(Human x: l){
			if (!x.isPosthumous()){
				return true;
			}
		}
		return false;
	}

	//Remove posthumous children from the list
	public static List<Human> getNonPosthumous(List<Human> l){
		List<Human> ll = new ArrayList<>(l.size());
		for(Human x: l){
			if (!x.isPosthumous()){
				ll.add(x);
			}
		}
		return ll;
	}


//Legimate children (i.e children born from marriage)

	public List<Human> getLegitLivingDaughters(){
		return this.rela.getLegitLivingDaughters();
	}

	public List<Human> getLegitLivingSons(){
		return this.rela.getLegitLivingSons();
	}

	public boolean hasMistress(){
		return this.rela.hasMistress();
	}

	public Human getRandomMistress(){
		return this.rela.getRandomMistress();
	}

	public boolean hasUnmarriedMistress(){
		return this.rela.hasUnmarriedMistress();
	}

	public Human getRandomUnmarriedMistress(){
		return this.rela.getRandomUnmarriedMistress();
	}

	//Get the living and the dead
	public List<Human> getLegitNonPosthumousSons(){
		return this.rela.getLegitNonPosthumousSons();
	}

	//Living exclusive version of previous
	public List<Human> getLegitNonPosthumousLivingSons(){
		return this.rela.getLegitNonPosthumousLivingSons();
	}



	public boolean hasLegitNonPosthumousSon(){
		return this.rela.hasLegitNonPosthumousSon();
	}

	//Living version of the previous
	public boolean hadLegitNonPosthumousSon(){
		return this.rela.hadLegitNonPosthumousSon();
	}

	//Check chara is the head of a house
	public boolean isHouseHead(){
		if (this.getHouse() != null && this.getHouse().getHeads().contains(this) ){
			return true;
		}
		return false;
	}

	public Minister getRole(){
		return this.role;
	}

	public void setRole(Minister m){
		this.role = m;
	}

	public boolean hasRole(){
		return this.role != null;
	}

	public void handleRoleDeath(){
		if (this.hasRole()){
			this.getRole().getCabinet().appointMinister();
		}
	}

	//Shortcuts
	public static int getID(){					return id;										}
	public static int getNumOfLiving(){			return living.size();							}
	public static List<Human> getLiving(){		return new ArrayList<>(living);					}

	public boolean areCloselyRelated(Human i){	return this.rela.areCloselyRelated(i); 			}
	public boolean canHaveLover(int h){			return this.rela.canHaveLover(h);				}
	public boolean fatherIsDead(){				return this.rela.fatherIsDead(); 				}
	public boolean had2ndGreatGrandparents(){	return this.rela.had2ndGreatGrandparents(); 	}
	public boolean had3rdGreatGrandparents(){	return this.rela.had3rdGreatGrandparents(); 	}
	public boolean hadBastard(){				return this.rela.hadBastard();					}
	public boolean hadBrother(){				return this.rela.hadBrother(); 					}
	public boolean hadFather(){					return this.rela.hadFather();					}
	public boolean hadGrandparents(){			return this.rela.hadGrandparents(); 			}
	public boolean hadGreatGrandparents(){		return this.rela.hadGreatGrandparents(); 		}
	public boolean hadMatGrandpa(){				return this.rela.hadMatGrandpa(); 				}
	public boolean hadMother(){					return this.rela.hadMother();					}
	public boolean hadParents(){				return this.rela.hadParents(); 					}
	public boolean hadPatGrandpa(){				return this.rela.hadPatGrandpa(); 				}
	public boolean hadPatGreatGrandpa(){		return this.rela.hadPatGreatGrandpa(); 			}
	public boolean hasAdultBrother(){			return this.rela.hasAdultBrother(); 			}
	public boolean hasAffairs(){				return this.rela.hasAffairs();					}
	public boolean hasBrother(){				return this.rela.hasBrother(); 					}
	public boolean hasChild(){					return this.rela.hasChild(); 					}
	public boolean hasFather(){					return this.rela.hasFather();					}
	public boolean hasFatherOrUncle(){			return this.rela.hasFatherOrUncle(); 			}
	public boolean hasFirstCousin(){			return this.rela.hasFirstCousin(); 				}
	public boolean hasGrandpaOrUncle(){			return this.rela.hasGrandpaOrUncle(); 			}
	public boolean hasMaternalPibling(){		return this.rela.hasMaternalPibling(); 			}
	public boolean hasNumOfLivingSons(int v){	return this.rela.hasNumOfLivingSons(v);			}
	public boolean hasPaternalNephew(){			return this.rela.hasPaternalNephew(); 			}
	public boolean hasPaternalPibling(){		return this.rela.hasPaternalPibling(); 			}
	public boolean hasPatruus(){				return this.rela.hasPatruus(); 					}
	public boolean isUncleOf(Human h){			return this.rela.isUncleOf(h); 					}
	public boolean isAuntOf(Human h){			return this.rela.isAuntOf(h); 					}
	public boolean isDaughterOf(Human h){		return this.rela.isDaughterOf(h);				}
	public boolean hasPibling(){				return this.rela.hasPibling(); 					}
	public boolean hasSeniorPaternalRelative(){	return this.rela.hasSeniorPaternalRelative(); 	}
	public boolean hasSister(){					return this.rela.hasSister(); 					}
	public boolean hasSon(){					return this.rela.hasSon(); 						}
	public boolean hasLegitSon(){				return this.rela.hasLegitSon(); 				}
	public boolean hasSons(){					return this.rela.hasSons(); 					}
	public boolean hasUnwedBrother(){			return this.rela.hasUnwedBrother(); 			}
	public boolean hasUnwedSister(){			return this.rela.hasUnwedSister(); 				}
	public boolean isActiveAdulterer(){			return this.rela.isActiveAdulterer();			}
	public boolean isBrotherOf(Human h){		return this.rela.isBrotherOf(h);				}
	public boolean isChildOf(Human h){			return this.rela.isChildOf(h); 					}
	public boolean isFatherOf(Human h){			return this.rela.isFatherOf(h);					}
	public boolean isMotherOf(Human h){			return this.rela.isMotherOf(h);					}
	public boolean isFirstCousinOf(Human h){	return this.rela.isFirstCousinOf(h); 			}
	public boolean isSecondCousinOf(Human h){	return this.rela.isSecondCousinOf(h); 			}
	public boolean isFullSiblingOf(Human i){	return this.rela.isFullSiblingOf(i); 			}
	public boolean isIntimateWith(Human i){		return this.rela.isIntimateWith(i); 			}
	public boolean isLoverOf(Human h){			return this.rela.isLoverOf(h);					}
	public boolean isMarried(){ 				return this.rela.isMarried();					}
	public boolean isPatDescendantOf(Human h){	return this.rela.isPatDescendantOf(h);			}
	public boolean isMarriedTo(Human s){ 		return this.rela.isMarriedTo(s);				}
	public boolean isNephewOf(Human h){			return this.rela.isNephewOf(h); 				}
	public boolean isPaternalNephewOf(Human h){	return this.rela.isPaternalNephewOf(h); 		}
	public boolean isRealBastard(){				return this.rela.isRealBastard();				}
	public boolean isRealChildOf(Human p){		return this.rela.isRealChildOf(p);				}
	public boolean isSiblingOf(Human i){		return this.rela.isSiblingOf(i); 				}
	public boolean isSisterOf(Human h){			return this.rela.isSisterOf(h);					}
	public boolean isSonless(){					return this.rela.isSonless(); 					}
	public boolean isSonOf(Human h){			return this.rela.isSonOf(h); 					}
	public boolean isUnwed(){					return this.rela.isUnwed();						}
	public boolean motherIsDead(){				return this.rela.motherIsDead(); 				}
	public boolean wasAdulterer(){				return this.rela.wasAdulterer();				}
	public boolean wasMarried(){ 				return this.rela.wasMarried();					}
	public Human getFather(){					return this.rela.getFather(); 					}
	public Human getFathersFather(){			return this.rela.getFathersFather(); 			}
	public Human getFathersMother(){			return this.rela.getFathersMother(); 			}
	public Human getGenitor(){					return this.rela.getGenitor(); 					}
	public Human getLatestHusband(){			return this.rela.getLatestHusband(); 			}
	public Human getLatestWife(){				return this.rela.getLatestWife(); 				}
	public Human getLivingSon(){				return this.rela.getLivingSon(); 				}
	public Human getLoverfromAffair(Affair a){	return this.rela.getLoverfromAffair(a);			}
	public Human getMother(){					return this.rela.getMother(); 					}
	public Human getMothersFather(){			return this.rela.getMothersFather(); 			}
	public Human getMothersMother(){			return this.rela.getMothersMother(); 			}
	public Human getOldestBrother(){			return this.rela.getOldestBrother(); 			}
	public Human getPaternalNephew(){			return this.rela.getPaternalNephew(); 			}
	public Human getPatGreatGrandpa(){			return this.rela.getPatGreatGrandpa(); 			}
	public Human getPatruus(){					return this.rela.getPatruus(); 					}
	public Human getSpouse(){					return this.rela.getSpouse(); 					}
	public Human getUnwedBrother(){				return this.rela.getUnwedBrother(); 			}
	public Human getUnwedSister(){				return this.rela.getUnwedSister(); 				}
	public Human[] get2ndGreatGrandparents(){	return this.rela.get2ndGreatGrandparents(); 	}
	public Human[] getFathersParents(){			return this.rela.getFathersParents(); 			}
	public Human[] getGrandparents(){			return this.rela.getGrandparents(); 			}
	public Human[] getGreatGrandparents(){		return this.rela.getGreatGrandparents(); 		}
	public Human[] getMothersParents(){			return this.rela.getMothersParents(); 			}
	public int getAmbition(){ 					return this.getPersonality().getAmbition();		}
	public int getAncestryRating(){				return this.rela.getAncestryRating(); 			}
	public int getChastity(){ 					return this.getPersonality().getChastity();		}
	public int getCunning(){ 					return this.getPersonality().getCunning();		}
	public int getHonour(){ 					return this.getPersonality().getHonour();		}
	public int getLibido(){						return this.getPersonality().getLibido();		}
	public int getNumOfAffairs(){				return this.rela.getNumOfAffairs();				}
	public int getNumOfLivingSiblings(){		return this.rela.getNumOfLivingSiblings(); 		}
	public int getNumOfLivingSons(){			return this.rela.getNumOfLivingSons();			}
	public int getNumOfMarriages(){ 			return this.rela.getNumOfMarriages();			}
	public int getNumOfSons(){					return this.rela.sons.size();					}
	public List<Affair> getAffairs(){			return this.rela.getAffairs();					}
	public List<Affair> getAllAffairs(){		return this.rela.getAllAffairs();				}
	public List<Human> getBastards(){			return this.rela.getBastards();					}
	public List<Human> getMaleAncestryGroup(){  return this.rela.getMaleAncestryGroup();		}
	public List<Human> getFemaleAncestryGroup(){ return this.rela.getFemaleAncestryGroup();		}
	public List<Human> getBrothers(){			return this.rela.getBrothers();					}
	public List<Human> getSister(){				return this.rela.getSisters();					}
	public List<Human> getChildren(){			return this.rela.getChildren();					}
	public List<Human> getDaughters(){			return this.rela.getDaughters();				}
	public List<Human> getLegitDaughters(){		return this.rela.getLegitDaughters();			}
	public List<Human> getLegitSons(){			return this.rela.getLegitSons();				}
	public List<Human> getLivingChildren(){		return this.rela.getLivingChildren();			}
	public List<Human> getLivingDaughters(){	return this.rela.getLivingDaughters(); 			}
	public List<Human> getLivingFirstCousins(){	return this.rela.getLivingFirstCousins(); 		}
	public List<Human> getLivingSons(){			return this.rela.getLivingSons(); 				}
	public List<Human> getMistresses(){			return this.rela.getMistresses();				}
	public List<Human> getPatrui(){				return this.rela.getPatrui(); 					}
	public List<Human> getRealChildren(){		return this.rela.getRealChildren();				}
	public List<Human> getSiblings(){			return this.rela.getSiblings();					}
	public List<Human> getSons(){				return this.rela.getSons();						}
	public List<Marriage> getMarriages(){		return this.rela.getMarriages();				}
	public Marriage getFirstMarriage(){			return this.rela.getFirstMarriage(); 			}
	public Marriage getLatestMarriage(){		return this.rela.getLatestMarriage(); 			}
	public Marriage getMarriage(int i){			return this.rela.getMarriage(i); 				}
	public void addAffair(Affair a){			this.rela.addAffair(a);							}
	public void addChild(Human f, Human m){		this.rela.addChild(f, m);						}
	public void addDaughter(Human f, Human m){	this.rela.addDaughter(f, m);					}
	public void addLegitDaughter(Human h){		this.rela.addLegitDaughter(h);					}
	public void addLegitSon(Human h){			this.rela.addLegitSon(h);						}
	public void addLivingSon(){					this.rela.addLivingSon();						}
	public void addMarriage(Marriage m){		this.rela.addMarriage(m);						}
	public void addRealChild(Human f, Human m){	this.rela.addRealChild(f, m);					}
	public void addSon(Human f, Human m){		this.rela.addSon(f, m);							}
	public void becomeAdulter(Affair a){		this.rela.becomeAdulter(a);						}
	public void clearAffairs(){					this.rela.clearAffairs();						}
	public void endAffair(Affair a){			this.rela.endAffair(a);							}
	public void removeAffair(Affair a){			this.rela.removeAffair(a);						}
	public void removeLivingSon(){				this.rela.removeLivingSon();					}
	public void setAffair(Affair a){			this.rela.setAffair(a);							}
	public void setGenitor(Human h){			this.rela.setGenitor(h); 						}
	public void setParents(Human f, Human m){	this.rela.setParents(f, m);						}
	public void setSpouse(Human h){				this.rela.setSpouse(h);							}
	public void setSpouseNull(){				this.rela.setSpouseNull();						}

	public int getHair(){						return Character.getNumericValue(this.DNA.charAt(1));}
	public int getEye(){						return Character.getNumericValue(this.DNA.charAt(0));}
	public String getBirthF(){					return Basic.format1.format(this.birth.getTime());	 }
	public Calendar getBirth(){					return this.birth; }
	public String getDeath(){					return Basic.format1.format(this.death.getTime());	 }
	public Calendar getBirthC(){				return (Calendar) this.birth.clone();				 }
	public Personality getPersonality(){		return this.personality;	 						 }


//Micro methods


	public void setFullName(String n){			this.getName().setFull(n);	}
	public void setLegitimacy(boolean b){		this.chaBox[0] = b;		}
	public void setPosthumous(boolean b){		this.chaBox[1] = b;		}
	public void setVirgin(){					this.chaBox[2] = false;	}

	public boolean isLegimate(){				return this.chaBox[0];	}
	public boolean isPosthumous(){				return this.chaBox[1];	}
	public boolean isVirgin(){ 					return this.chaBox[2]; 	}
	public boolean isFemaleAdult(){				return this.isFemale() && isAdult(); 	}
	public boolean hasTitle(Title t){			return this.title == t;			}
	public boolean hasTitle(){					return this.title != null;		}
	public boolean hasUnwedSameSexSibling(){	return false;					}
	public boolean isAdult(){					return this.fertility != -1;	}
	public boolean isChild(){					return !this.isAdult();			}
	public boolean isAlive(){					return this.death == null;		}
	public boolean isFemale(){					return this.sex == true;		}
	public boolean isGen(){						return this.gen != 0;			}
	public boolean isMale(){					return this.sex == false;		}
	public boolean isManorLord(){				return this.manorLord != null;	}
	public boolean isMarriageable(){ 			return true; 					}
	public boolean isMinor(){					return false;					}	//blank
	public boolean isPolitican(){				return this.polProfile != null; }
	public House getHouse(){					return this.house; 				}
	public int getCadency(){					return this.cadency;			}
	public int getFund(){						return this.fund;				}
	public int getGen(){						return this.gen;				}
	public ManorLord getManorLord(){			return this.manorLord;			}
	public Name getName(){ 						return this.name; 				}
	public Office getOffice(){					return this.office;				}
	public PolProfile getPolProfile(){			return this.polProfile;			}
	public String child(){						return "";						}
	public String getMaritalBio(){				return "";						}
	public String getFormalName(){				return "?";						}
	public String getFullName(){				return this.getName().getFull();}
	public String getNameS(String title){		return title;					}
	public String getNibling(){ 				return "nibling";				}
	public String getOffspring(){ 				return "son";					}
	public int getSexChildOrder(){				return 0; }		//I.e 2 (of 4 sons)
	public String getSexChildOrderName(){		return "";}		//second son
	public String getParent(){ 					return "parent";				}
	public String getPibling(){ 				return "pibling";				}
	public String getPossessive(){				return "his"; 					}
	public String getPronoun(){					return "he"; 					}
	public String getRelation(int v){ 			return ""; 						}
	public Rela getRela(){ 						return this.rela; 				}
	public String getShortName(){				return "?";						}
	public String getSibling(){					return "sibling";				}
	public String getTitleS(){					return this.title.getName();	}
	public Title getTitle(){					return this.title;				}
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
	public void clearLovers(){};
	public void getPaid(int m){					this.fund+= m;					}
	public void handlePostBirth(Human a, Human b){;}
	public void handleWidowhood(){;}
	public void makeIntoManorLord(ManorLord l){	this.manorLord = l; 			}
	public void nameChild(){;}
	public void ovulate(int x){;};
	public void princify(){;}
	public void reachAdulthood(){; 												}
	public void removeFromElders(){};
	public void setHouse(House n){				this.house = n; 				}
	public void setOffice(Office o){			this.office = o;				}
	public void sRename(Title title){}
    public void saunter(int x){;}
    public void handleBastardBirth(Human a, Human b){;}
	public String getLoverGroup(){ 				return "lovers";				}
	public void setCourtier(Courtier c){		this.courtier = c;				}
	public Courtier getCourtier(){				return this.courtier;			}
	public int getFert(){						return this.fertility;			}
	public int getMating(){						return this.mating;				}
	public void setMating(int i){				this.mating = i;				}
	public int getRelSta(){						return this.relSta;				}
	public void setRelSta(int v){				this.relSta = v;				}
	public boolean isRelSta(int v){				return (this.relSta == v);		}
	public boolean getSex(){					return this.sex;				}
	public void setTitle(Title t){				this.title = t;					}
	public void setPolProfile(PolProfile p){	this.polProfile = p;			}
	public void addEvent(Event e){				this.events.add(e);				}
	public void removeEvent(Event e){			this.events.remove(e);			}
	public boolean isWidowed(){					return this.wasMarried() && this.isUnwed();	}

	public String getCoALink(){
		Human h;
		if (this.isMale()){
			if (((Man) this).isHouseHead()){
				return this.getHouse().getCoALink();
			}
		} if (this.isAdult() && this.wasMarried()){
			h = this.getLatestHusband();
			if (((Man) h).isHouseHead() && h.getHouse().isNoble()){
				return h.getHouse().getCoALink();
			}
		}
		return "";
	}
}
