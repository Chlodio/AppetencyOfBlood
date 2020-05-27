package Code.Human;

import java.util.List;
import java.util.ArrayList;
import java.util.Calendar;
import Code.Common.*;
import Code.Court.Courtier;
import Code.House.House;
import Code.Ancestry.Claim;
import Code.Looks.*;
import Code.Ancestry.Bastard;
import Code.Politics.*;
import Code.Common.Nick;
import Code.Relationship.*;

public class Human {
	protected Calendar death;
	protected Courtier courtier;
	protected House house;  								//Mont of pregnancy
//	protected int cadency;
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
	protected House host;										//Character is the head of this family
	protected Religion religion;
	protected String DNA;
	protected Title title;
  protected boolean sex;
  protected Calendar birth;
  protected Name name;
	protected Personality personality;
	private Minister role;									//Character's career as minister
	private List<Claim> claims;
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

	private byte deathCause;
	/*What was the cause characters death
	0 = natural
	10 = childbirth/maternal death
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
//		Basic.human.put(Human.id, this);
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
//	   Basic.human.put(Human.id, this);
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
//	   Basic.human.put(Human.id, this);
	   this.chaBox = 	new boolean[]{true, false, true};
  }


	public void performPosthumousBirth(Human f, Human m){
		//Basic.annals.recordPosthumousBirth(m.getFullName()+" gave posthumous birth to son of "+f.getFullName());
		this.getName().setNick(Nick.POSTHUMOUS);
	}

	public void kill(byte i){
  	Human.living.remove(this);
		this.death = (Calendar) Basic.date.clone();
		this.bury();
    if (!this.isUnwed()){
			Human s = this.getSpouse();
    	Basic.annals.recordWidowDeath(this, s);
			this.getLatestMarriage().terminate();
			s.becomeWidow();
    } else {
			this.clean();
			Basic.annals.recordSingleDeath(this);
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
		if (this.hadClaims()){
			if (this.claims.size() > 0){
				throw new RuntimeException();
			}
		}
		this.setDeathCause(i);
  }

	public void review(){
		if (this.hasClaims()){
			this.passClaims();
			this.removeAllClaim();
			if (this.claims.size() > 0){
				throw new RuntimeException();
			}
		}
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
			return this.getForename()+" "+this.getHouse().getName();
		} else {
			return this.getForename();
		}
	}

	public String getForename(){
		return ""+this.getName().getName();
	}

	//Get names non-offspring relatives used for naming
	public List<String> getMaleAncestryGroupNames(){
		List<Human> l =  this.getMaleAncestryGroup();
		List<String> ll = new ArrayList<>();
		for(Human x: l){
			ll.add(x.getForename());
		}
		return ll;
	}

	//Get names non-offspring relatives used for naming
	public List<String> getFemaleAncestryGroupNames(){
		List<Human> l =  this.getFemaleAncestryGroup();
		List<String> ll = new ArrayList<>();
		for(Human x: l){
			ll.add(x.getForename());
		}
		return ll;
	}

	public List<String> getLivingSonsNames(){
		List<Human> l =		this.getLegitLivingSons();
		List<String> n = 	new ArrayList<>();
		for(Human x: l){
			n.add(x.getForename());
		}
		return n;
	}

	public List<String> getLivingDaughtersNames(){
		List<Human> l =		this.getLegitLivingDaughters();
		List<String> n = 	new ArrayList<>();
		for(Human x: l){
			n.add(x.getForename());
		}
		return n;
	}

	public boolean hasSonWithTheName(String n){
		List<Human> l = this.getSons();
		for (Human x: l){
			if (x.isRegistered() && x.getForename().equals(n)){
				return true;
			}
		}
		return false;
	}

	public boolean hasDaughterWithTheName(String n){
		List<Human> l = this.getDaughters();
		for (Human x: l){
			if (x.isRegistered() && x.getForename().equals(n)){
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

	public boolean isLivingUnwedAdult(){
		return this.isLivingAdult() && this.isUnwed();
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

	public StringBuffer getPossibleDeath(){
		StringBuffer s = new StringBuffer();
		if (!this.isAlive()){
			return s.append(this.getDeathStr());
		} else{
			return s.append("PRSN");
		}
	}

	public Calendar getDeath(){	return this.death;	}

	//If alive return current date, otherwise dead the death date
	public Calendar getDeathPresent(){
		if (this.isAlive()){
			return Basic.getDate();
		} else {
			return this.getDeath();
		}
	}

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
		p.addRegnalTitle(o.getHolder());
	}

//Family

	public static void createFamily(){
		int ha = 20+Basic.randint(25);				//husband age
		int my = Basic.randint(ha-14);				//marriage age
		int wa = 15+my;								//wife age
		Human f;
		Human m;
//		new Man(ha);
//		new Woman(wa);
		f = new Man(ha); //Basic.human.get(Human.getID()-1);
		m = new Woman(wa); //Basic.human.get(Human.getID());
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
			if (it.isMale() && it.isFirstbornSon()){
				if (f.isOverAgeOf(45)){
					it.getName().setNick(Nick.DESIRED);
				}
			}
		} else {
			it.setFullName(it.makeName());
		}
		Basic.annals.writeLegitBirth(it);
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
		it.getName().setNick(Nick.BASTARD);

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
		StringBuffer s = new StringBuffer("");
		if (age >= 15){
			s.append("<img src='../Input/Portraits/");
			s.append(this.getEyeShort());
			s.append(this.getHairShort());
			if (this.isMale()){
				s.append("_M.png'</img>");
			} else {
				s.append("_F.png'</img>");
			}
			return String.valueOf(s);
		}
		return String.valueOf(s);
	}

	public boolean isFromSameEstate(Human h){
		if (this.hadHouse() && h.hadHouse()){
			return this.getHouse().isNoble() == h.getHouse().isNoble();
		}
		return true;
	}

	public boolean hadHouse(){					return this.house != null;	}

	public String getHouseCoALink(){
		return this.getHouse().getCoALink();
	}


	public boolean isNoble(){
		if (this.getHouse() != null){
			return this.getHouse().isNoble();
		} else {
			return false;
		}
	}

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

	public static List<Integer> getAdultsInt(List<Human> l){
		List<Integer> n = new ArrayList<>(l.size());		//Short for new list
		for(int x = 0; x < l.size(); x++){
			if (l.get(x).isAdult()){
				n.add(x);
			}
		}
		return n;
	}

	//Similar to getAdultsInt, but will get everyone above 14, adult tick is only once a year, thus for a short time there are children who are 14 but not counted as adults
	public static List<Integer> getMajorsInt(List<Human> l){
		List<Integer> n = new ArrayList<>(l.size());		//Short for new list
		for(int x = 0; x < l.size(); x++){
			if (l.get(x).isOverAgeOf(14)){
				n.add(x);
			}
		}
		return n;
	}

	public List<Human> getFromSameEstate(List<Human> l){
		List<Human> n = new ArrayList<>(l.size());		//Short for new list
		for(Human x: l){
			if (x.isFromSameEstate(this)){
				n.add(x);
			}
		}
		return n;
	}

	//Get a list of humans who were alive in certain date
	public static List<Human> getLivingIn(List<Human> l, Calendar c){
		List<Human> n = new ArrayList<>();
		for(Human x: l){
			if (x.wasBornBefore(c) && x.wasAliveIn(c)){
				n.add(x);
			}
		}
		if (n.size() != 0){
			return n;
		} else {
			return null;
		}
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

	public boolean hasSameBirthOrder(){
		return this.getAgnaticOrder() == this.getEnaticOrder();
	}


	//Which order is child born, return -1 if only child, 0 if oldest and -2 if youngest
	public int getBirthOrderRank(List<Human> l){
		String s = "";
		int i = l.indexOf(this);								//Index in the order

		//If there is only one son, just return only son, otherwise [ordinal] son or oldest/youngest
		if (l.size() == 1){
			return -1;
		} else {
			if (i == l.size()-1){
				return -2;									//youngest
			} else {
				return i;
			}
		}
	}

	public String getBirthOrderRank(int i){
		switch(i){
			case 0:
				return "firstborn";
			case -1:
				return "only";
			case -2:
				return "youngest";
			default:
				 return Basic.getOrder(i);
		}
	}

	public boolean wasBornBefore(Human m){
		return this.getBirth().before(m.getBirth());
	}

	public boolean wasBornBefore(Calendar c){
		return this.getBirth().before(c);
	}

	public boolean wasAliveIn(Calendar c){
		if (this.isAlive()){
			return true;
		} else {
				return c.before(this.getDeath());
		}
	}

	public static int getRelativeRank(List<Human>[] l){
		int i = 0;
		if (Basic.isNotNullZero(l[0]) ){
			//Has full relatives
			i += 1;
		}
		if (Basic.isNotNullZero(l[1]) ){
			//Has paternal relatives
			i += 2;
		}
		if (Basic.isNotNullZero(l[2]) ){
			//Has maternal relatives
			i += 4;
		}
		return i;
	}

	public static String getRelativeStatus(String s, List<Human>[] l){
		int i = getRelativeRank(l);
		String p = " At the time he had ";
		switch(i){
			case 0:
				//No relatives
				return "";
			case 1:
				//Only full relatives;
				Human ma = l[0].get(0).getMother();
				for (Human x: l[0]){
					if (x.getMother() != ma){
						throw new RuntimeException();
					}
				}
				return p+getRelativesInfo(s, l[0])+".";
			case 2:
				//Only paternal half-relatives
				return p+getRelativesInfo("paternal half-"+s, l[1])+".";
			case 4:
				//Only maternal half-relatives
				return p+getRelativesInfo("maternal half-"+s, l[2])+".";
			case 3:
				//Full + paterna relatives
				 return p+getFullAndPatRelativesInfo(s, l)+".";
			case 5:
				//Full + maternal relatives
				return p+getFullAndMatRelativesInfo(s, l)+".";
			case 6:
				//Only half-relatives
				return p+getHalfRelativesInfo(s, l)+".";
			default:
				//aka. 7, all type of relatives
				return p+getAllRelativesInfo(s, l)+".";
		}
	}

	public static String getRelativesInfo(String s, List<Human> l){
		if (l.size() == 1){
			return " a "+s+", "+l.get(0).getShortName();
		} else {
			return " "+s+"s: "+Human.getNamesOnList(l);
		}
	}

	public static String getFullAndPatRelativesInfo(String r, List<Human>[] l){
		String s = getRelativesInfo(r, l[0]); 								//Get full relatives
		s += "; "+getRelativesInfo("paternal half-"+r, l[1]); //Get pat. relatives
		return s;
	}

	public static String getFullAndMatRelativesInfo(String r, List<Human>[] l){
		String s = getRelativesInfo(r, l[0]); 									//Get full relatives
		s += "; "+getRelativesInfo("maternal half-"+r, l[2]); 	//Get mat. relatives
		return s;
	}

	public static String getHalfRelativesInfo(String r, List<Human>[] l){
		String s = getRelativesInfo("paternal half-"+r, l[1]); 	//Get pat. relatives
		s += "; "+getRelativesInfo("maternal half-"+r, l[2]); 	//Get mat. relatives
		return s;
	}

	public static String getAllRelativesInfo(String r, List<Human>[] l){
		String s = getRelativesInfo(r, l[0]); 								//Get full relatives
		s += "; "+getRelativesInfo("paternal half-"+r, l[1]); //Get pat. relatives
		s += "; "+getRelativesInfo("maternal half-"+r, l[2]);	//Get mat. relatives
		return s;
	}

	//Get a list of brothers who were alive in certain date
	public List<Human> getLivingPatBrothersIn(Calendar c){
		return this.rela.getLivingPatBrothersIn(c);
	}

	public List<Human>[] getAllBrothersLivingIn(Calendar c){
		return this.rela.getAllBrothersLivingIn(c);
	}

	public List<Human> getPatBrothersLivingIn(Calendar c){
		return this.rela.getPatBrothersLivingIn(c);
	}

	public List<Human> getMatBrothersLivingIn(Calendar c){
		return this.rela.getMatBrothersLivingIn(c);
	}

	public List<Human> getHalfBrothersFrom(List<Human> l1, List<Human> l2){
		return this.rela.getHalfBrothersFrom(l1, l2);
	}

	public Human getFirstbornLegitSon(){
		return this.rela.getLegitSons().get(0);
	}

	public static String getNamesOnList(List<Human> l){
		String s = "";
		switch(l.size()){
			case 1:
				return l.get(0).getShortName();
			case 2:
				return l.get(0).getShortName()+" and "+l.get(1).getShortName();
			default:
				for(int x = 0; x < l.size()-1; x++){
					s += l.get(x).getShortName()+", ";
				}
				s += "and "+l.get(l.size()-1).getShortName();
				return s;
		}
	}


	//Check chara is the head of a house
	public boolean isHouseHead(){
		if (this.getHouse() != null && this.getHouse().getHeads().contains(this) ){
			return true;
		}
		return false;
	}

	public boolean isCurrentHouseHead(){
		return this.getHouse().getHead() == this;
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

	//Intended for women only, see if there is evidence that they could have reproduced
	public boolean wasBarren(){
		return !this.hadChild() && !this.diedInChildbirth();
	}

//Host methods

	public void setHost(House h){
		this.host = h;
	}

	public House getHostHouse(){
		return this.host;
	}

	public boolean isHost(){
		return this.host != null;
	}

	public void resetHost(){
		this.host = null;
	}

	public static Human getRandomPersonForHost(){
		Human h;
		for(int x = 25; x > 0; x--){
			h = Basic.choice(Woman.women);
			if (h.isAdult() && !h.isHost()){
				return h;
			}
		}
		int as = 0;
		for(House x: House.getList()){
			if (x.getHost() != x.getHead()){
				as++;
			}
		}
		int ad = 0;
		int ac = 0;
		for(Human x: Man.getMen()){
			if (x.isAdult()){
				ad++;
				if (!x.isHost()){
					ac++;
				}
			}
		}
		throw new RuntimeException();
	}

	public boolean isPartOfDynasty(){
		return this.getHouse().isDynastic();
	}

//Claim

	public void hasClaimRemove(Office o){
		if (this.hadClaims()){
			List<Claim> l = new ArrayList<>(this.claims);
			for(int x = 0; x < l.size(); x++){
				if (l.get(x).getOffice() == o){
					this.removeClaim(l.get(x));
				}
			}
			if (this.claims.size() > 0){
				throw new RuntimeException();
			}
		}
	}


	public Claim getClaim(int i){
		return this.claims.get(i);
	}

	public void passClaims(){
		List<Claim> l = new ArrayList<>(this.claims);
		this.claims.get(0).pass();
	}

	public boolean hadClaims(){
		return this.claims != null;
	}

	public boolean hasClaims(){
		return this.hadClaims() && this.claims.size() > 0;
	}

	public void addClaim(Claim c){
		if (!this.hadClaims()){
			this.claims = new ArrayList<>(1);
		}
		if (!this.claims.contains(c)){
			this.claims.add(c);
			c.getOffice().addClaimant(c);
		}
	}

	public void removeClaim(Claim c){
		this.claims.remove(c);
		c.getOffice().removeClaimant(c);
	}

	public void removeAllClaim(){
		while(this.claims.size() > 0){
			this.removeClaim(this.claims.get(0));
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
	public boolean hadPatBrother(){				return this.rela.hadPatBrother(); 					}
	public boolean hadFather(){					return this.rela.hadFather();					}
	public boolean hadGrandparents(){			return this.rela.hadGrandparents(); 			}
	public boolean hadGreatGrandparents(){		return this.rela.hadGreatGrandparents(); 		}
	public boolean hadMatGrandpa(){				return this.rela.hadMatGrandpa(); 				}
	public boolean hadMother(){					return this.rela.hadMother();					}
	public boolean hadParents(){				return this.rela.hadParents(); 					}
	public boolean hadPatGrandpa(){				return this.rela.hadPatGrandpa(); 				}
	public boolean hadPatGreatGrandpa(){		return this.rela.hadPatGreatGrandpa(); 			}
	public boolean hasAdultPatBrother(){			return this.rela.hasAdultPatBrother(); 			}
	public boolean hasAffairs(){				return this.rela.hasAffairs();					}
	public boolean hasPatBrother(){				return this.rela.hasPatBrother(); 					}
	public boolean hasChild(){					return this.rela.hasChild(); 					}
	public boolean hadChild(){					return this.rela.hadChild(); 					}
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
	public boolean hasPatSister(){					return this.rela.hasPatSister(); 					}
	public boolean hasSon(){					return this.rela.hasSon(); 						}
	public boolean hasLegitSon(){				return this.rela.hasLegitSon(); 				}
	public boolean hasSons(){					return this.rela.hasSons(); 					}
	public boolean hasUnwedPatBrother(){			return this.rela.hasUnwedPatBrother(); 			}
	public boolean hasUnwedPatSister(){			return this.rela.hasUnwedPatSister(); 				}
	public boolean isActiveAdulterer(){			return this.rela.isActiveAdulterer();			}
	public boolean isBrotherOf(Human h){		return this.rela.isBrotherOf(h);				}
	public boolean isFirstbornSon(){				return this.rela.isFirstbornSon();	}
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
	public Human getOldestLivingPatBrother(){			return this.rela.getOldestLivingPatBrother(); 			}
	public Human getPaternalNephew(){			return this.rela.getPaternalNephew(); 			}
	public Human getPatGreatGrandpa(){			return this.rela.getPatGreatGrandpa(); 			}
	public Human getPatruus(){					return this.rela.getPatruus(); 					}
	public Human getSpouse(){					return this.rela.getSpouse(); 					}
	public Human getUnwedPatBrother(){				return this.rela.getUnwedPatBrother(); 			}
	public Human getUnwedPatSister(){				return this.rela.getUnwedPatSister(); 				}
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
	public List<Human> getPatBrothers(){			return this.rela.getPatBrothers();					}
	public List<Human> getSister(){				return this.rela.getPatSisters();					}
	public List<Human> getChildren(){			return this.rela.getChildren();					}
	public List<Human> getDaughters(){			return this.rela.getDaughters();				}
	public List<Human> getLegitDaughters(){		return this.rela.getLegitDaughters();			}
	public List<Human> getLegitSons(){	return this.rela.getLegitSons();				}
	public List<Human> getLegitSonsLink(){return this.rela.getLegitSonsLink();	}
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
	public int getBirthYear(){	return this.getBirth().get(Calendar.YEAR);		}

	public String getDeathStr(){					return Basic.format1.format(this.death.getTime());	 }
	public Personality getPersonality(){		return this.personality;	 						 }


	public boolean getSex(){						return this.sex;							}
	public boolean isSameSex(Human h){	return this.sex == h.sex; 		}
	public boolean wasOnlyChild(){			return this.getRela().wasOnlyChild();	}

//Micro methods


	public void setFullName(String n){			this.getName().setFull(n);	}
	public void setLegitimacy(boolean b){		this.chaBox[0] = b;		}
	public void setPosthumous(boolean b){		this.chaBox[1] = b;		}
	public void setVirgin(){					this.chaBox[2] = false;	}

	public boolean isLegimate(){				return this.chaBox[0];	}
	public boolean isPosthumous(){				return this.chaBox[1];	}
	public boolean isVirgin(){ 					return this.chaBox[2]; 	}
	public void setDeathCause(byte i){			this.deathCause = i; 			}
	public byte getDeathCause(){				return this.deathCause;			}
	public boolean diedInChildbirth(){			return this.deathCause == 10;	}


	public boolean isFemaleAdult(){				return this.isFemale() && isAdult(); 	}
	public boolean hasTitle(Title t){			return this.title == t;			}
	public boolean hasTitle(){					return this.title != null;		}
	public boolean hasUnwedSameSexSibling(){	return false;					}
	public boolean isAdult(){					return this.fertility != -1;	}
	public boolean isChild(){					return !this.isAdult();			}
	public boolean isAlive(){					return this.death == null;		}
	public boolean isDead(){					return this.death != null;		}
	public boolean isFemale(){					return this.sex == true;		}
	public boolean isGen(){						return this.gen != 0;			}
	public boolean isMale(){					return this.sex == false;		}
	public boolean isManorLord(){				return this.manorLord != null;	}
	public boolean isMarriageable(){ 			return true; 					}
	public boolean isMinor(){					return false;					}	//blank
	public boolean isPolitican(){				return this.polProfile != null; }
	public House getHouse(){					return this.house; 				}
	public int getCadency(){					return 0;									}
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
	public String getAgnaticOrderStr(){		return "";}		//I.e second son
	public String getEnaticOrderStr(){		return "";}
	public int getAgnaticOrder(){					return 0;}
	public int getEnaticOrder(){					return 0;}
	public String getParent(){ 					return "parent";				}
	public String getPibling(){ 				return "pibling";				}
	public String getPossessive(){				return "his"; 					}
	public String getPossessiveRev(){			return "her"; 					}
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
