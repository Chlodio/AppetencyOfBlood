package Code.Relationship;
import Code.Human.*;
import Code.Common.Basic;
import Code.Politics.Title;
import Code.Politics.Realm;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Marriage extends SexRelation{
	private boolean sated;		//procreational sated
	private byte PRO; 			//procreational optimism 0–20
	private byte type;			//0 = regular, 1 = levirate, 2 = sororate

	protected byte kinType;		//Define how and if the husband and the wife are related
	/*with the following values:
	0 = no relation
	1 = father-daughter (unused)
	2 = mother-son (unused)
	3 = sibling (unused)
	4 = uncle-niece
	5 = aunt-nephew
	6 = cousin
	7 = cousin-once-removed (unused)
	8 = second cousin
	*/


	protected static Human bestMatch;
	protected static int numOfMonthlyWeddings 			= 0;
	protected static List<Float> monthlyWeddingList 	= new ArrayList<>();
	protected static List<Marriage> dList				= new ArrayList<>(); 	//divorce list
	public static int gen 								= 0;
    protected static List<Marriage> list 				= new ArrayList<>();	//active marriages
    protected static Map<Integer, Marriage> marriages 	= new HashMap<>();

/*Used for starters*/
	public Marriage(Human husband, Human wife, int year){
		super(husband, wife);
		this.beginning.add(Calendar.DATE, -365*year);
		this.setMarriage();
    }

/*Used for regulars*/
    public Marriage(Human husband, Human wife){
		super(husband, wife);
		this.anniversary = 	0;
		this.setMarriage();
		this.determineEndForFornication();
		this.consummate();
		this.sated = false;
    }


	public void setMarriage(){
		list.add(this);
		this.stag.addMarriage(this);
		this.doe.addMarriage(this);
		this.setKinType(this.defineKinType());
	}

	public void setKinType(byte i){
		this.kinType = i;
	}

	//Try to to find relation
	public byte defineKinType(){
		Human h = this.getStag(); 	//Get husband
		Human w = this.getDoe();	//Get wife
		if (h.isFirstCousinOf(w)){
			return 6;					//Return cousin
		} else if (h.isSecondCousinOf(w)){
			return 8;
		} else if (h.isUncleOf(w)){
			return 4;					//Return uncle-niece
		} else if (w.isAuntOf(h)){
			return 5;					//Return aunt-nephew
		}

		return 0;

	}

	public String getKinTypeHTML(Human h){
		if (this.hasKinType()){
			switch(this.getKinType()){
				case 6:
					return h.getPossessive()+" cousin, ";
				case 8:
					return h.getPossessive()+" second cousin, ";
				case 4:
					return h.getPossessive()+" "+this.getKinNibling(h)+", ";
				case 5:
					return h.getPossessive()+" "+this.getKinPibling(h)+", ";
				default:
					return "";
			}
		} else {
			return "";
		}
	}

	public String getKinNibling(Human h){
		if (h.isMale()){
			return "niece";
		} else {
			return "nephew";
		}
	}

	public String getKinPibling(Human h){
		if (h.isMale()){
			return "aunt";
		} else {
			return "uncle";
		}
	}

	public void consummate(){

		if (this.stag.isVirgin()){
			this.stag.setVirgin();
		}

		if (this.doe.isVirgin()){
			this.doe.setVirgin();
		} else {
			if (this.doe.getNumOfMarriages() == 1 ){
				throw new RuntimeException();
			}
		}

	}

	public void determineEndForFornication(){
		if (this.getHappiness() > 2){
			this.retireTiredAffairs(this.stag);
			this.retireTiredAffairs(this.doe);
		}
	}

	public void retireTiredAffairs(Human a){
		if (a.isActiveAdulterer()){
			List<Affair> l = a.getAffairs();
			for (Affair x: l){
				if (this.getHappiness() >= x.getHappiness()){
					x.endAffair();
				}
			}
		}
	}

	public static void propose(Human bachelor, int maom){
		if (Basic.randint(bachelor.getMating()) == 0){
			int f = Basic.randint(maom)+1;
			Basic.dayC.get(f).add(bachelor);
			Basic.dayE.get(f).add(1);
			addMonthlyWedding();
		}
	}

	public static void prepare(Human groom){
		if (match(groom)){
			marryFiancee(groom, bestMatch);
		} else if (groom.isNoble()){
			Human w = Woman.findWench();
			if (w != null){
				marryFiancee(groom, w);
			}
		}

		/*	if (groom.getAge() >= 30 && groom.isActiveAdulterer()){
				if (groom.hasUnmarriedMistress()){
					marryMistress(groom);
					ca++;
				}
			} else if (Basic.randint(5) == 0){
				Affair.begin(groom);
			}
		}*/
	}

	public static int ca = 0;

	//When a noble decides to marry a peasant or a mistress who likely is a peasant,
	private static void marryMistress(Human g){
		Human b = g.getRandomUnmarriedMistress();					//Bride who marries the (g)room

		if (b.hadFather()){
			if (b.getHouse().isActive()){
				if (!b.getHouse().isNoble()){						//Just to be safe
					b.getHouse().ennoble(4);						//Origin being morganatic origin
					System.out.println("morganatic ennoblement");
				}
			}
		}
		marryFiancee(g, b);
	}

	public static boolean match(Human b){
		List<Human> brides = new ArrayList<>();
		List<Human> pBrides = Woman.getSingles();
		int bV; Human bC; 	//best value //best character
		for (Human x: pBrides){
			//No old women
			if (!b.isFromSameEstate(x)){	continue;	}
			if (b.isOlderThan(x, 10)|| x.isOverAgeOf(40)){ continue;}
			//No parent-child marriages
			if (x.isMotherOf(b)){ continue; }
			if (b.isFatherOf(x) ){ continue; }
			//No siblin marriages
			if (b.isSiblingOf(x) ){ continue; }
			//
			brides.add(x);
		}
		if (Basic.isNotZero(brides.size())){
				bestMatch = Basic.choice(brides);
				return true;
		}
		return false;
	}

    public static void marryBetrothed(Human husband, Human wife){
		Marriage.marry(husband, wife);
        Basic.print(husband.getFullName()+" gave "+wife.getFullName()+" a green gown.");
	}

	public static void marryFiancee(Human husband, Human wife){
		Marriage.marry(husband, wife);
		Basic.print(husband.getFullName()+" married "+wife.getFullName());
		husband.becomeTaken();
		wife.becomeTaken();
	}

	//Used for pre-simulation setups
	public static Human findWife(Human a){
		List<Human> l = Woman.getSingles();
		for(Human x: l){
			if (a.isOlderThan(x)) {
				if (!a.isSiblingOf(x)){
					if (a.isFromSameEstate(x)){
						return x;
					}
				}
			}
		}
		return null;
	}

	public static boolean hasWomanSingles(Human a){
		for(Human x: Woman.singles){
			if (a.isOlderThan(x) && !a.isSiblingOf(x)){
				return true;
			}
		}
		return false;
	}

//Used for starting characters
	public static void marrySpecial(Human husband, Human wife, int year){
        Marriage temp = new Marriage(husband, wife, year);
		husband.setRelSta(2);
		wife.setRelSta(2);
		if (husband.getTitle() == Title.KING){
			Realm.getHolder(0).setConsort(wife);
		}
		husband.becomeTaken();
		wife.becomeTaken();
    }

    public static void marry(Human husband, Human wife){
        Marriage temp = new Marriage(husband, wife);
		husband.setRelSta(2);
		wife.setRelSta(2);
		if (husband.getTitle() == Title.KING){
			Realm.getHolder(0).setConsort(wife);
		}
    }

	public int getAgeAt(Human spouse){
		return this.beginning.get(Calendar.YEAR)-spouse.getBirthC().get(Calendar.YEAR);
	}

	/*Widow marries her brother-in-laws*/
	public static void doLevirate(Human widow, Human departed){
		marryFiancee(departed.getUnwedBrother(), widow);
		getRecentMarriage().setLevirate();
	}

	/*Widower marries his sister-in-laws*/
	public static void doSororate(Human widower, Human departed){
		marryFiancee(widower, departed.getUnwedSister());
		getRecentMarriage().setSorotate();
	}

	public void terminate(){
		this.ending = (Calendar) Basic.date.clone();
		this.active = false;
		list.remove(this);
	}

	public static void doBreeding(){
		for (Marriage x: list){
			x.breed();
		}
	}

	//Check if the marriage is active, to prevent cases where husband who died ealier in the same would impregnate their widow and where marriage ended in divorce
	public boolean canBreed(){
		if (this.isActive()){
			//Multiple sons are only for noblemen
			return true;
		}
		return false;
	}

	public void breed(){
		Woman w = (Woman) this.doe;

		if (this.canBreed()){
			try{
				if (Basic.randint(100) < this.procreation[this.anniversary] && !w.isPregnant()){
					w.fillUterus(this);
					w.growFetus();
					Woman.pregnant.add(this.doe);
				}
			}
			catch (ArrayIndexOutOfBoundsException e){
				System.out.println("Husband alive: "+this.stag.isAlive()+"\nWife alive: "+this.doe.isAlive());
				System.out.println("Active: "+this.isActive());
				System.out.println("Wife had father:"+this.doe.hadFather());
				System.out.println("Husband died:"+this.stag.getDeath());
				List<Marriage> l = this.stag.getMarriages();
				for(Marriage x: l) {
					System.out.println("Began: "+x.getBeginning());
					System.out.println("Name: "+x.getDoe());
				}
				throw new RuntimeException();
			}
		}
	}

	/*Checks if affair can be created, by calculating marriage happiness, amount of lovers, and the relationship betwen the potential lovers
		m = man
		w = woman
		h = happiness
	*/
	public static boolean canBeLovers(Human m, Human w, int h){
		if (h < 3){
			if (w.canHaveLover(h)){
				if (!m.isIntimateWith(w)){
					return true;
				}
			}
		}
		return false;
	}

	public static boolean hasBadMarriage(Human m){
		List<Marriage> l = Marriage.list;
		for(Marriage x: l){
			if (canBeLovers(m, x.doe, x.getHappiness())){
				return true;
			}
		}
		return false;
	}

	public static int countBadMarriages(){
		int v = 0;
		List<Marriage> l = Marriage.list;
		for(Marriage x: l){
			if (x.getHappiness() < 3){
				v++;
			}
		}
		return v;
	}

	//m is searcher of lover, to make sure the unhappy wive isn't his close relative
	public static List<Human> getUnhappyWives(Human m){
		List<Marriage> ml = Marriage.list;
		List<Human> l = new ArrayList<>((int) (ml.size()*0.4));
		for(Marriage x: ml){
			if (canBeLovers(m, x.doe, x.getHappiness())){
				l.add(x.doe);
			}
		}
		return l;
	}


	/* MIGHT BE USEFUL
		public static Marriage[] getBadMarriages(){
			Marriage[] l = new Marriage[countBadMarriages()];
			int i = 0;
			for(Mariage x: Marriage.list){
				if (x.happiness < 3){
					l[i] = x;
					i++;
				}
			}
		}*/


	public boolean considerDivorce(){
		if(((this.anniversary%10) == 0) && this.stag.isSonless()){
			if (Basic.randint(5) == 0){
				return true;
			}
		}
		return false;
	}

	public void getDivorce(){
		this.terminate();
		this.stag.becomeSingle();
		this.stag.setRelSta(1);
		this.doe.becomeSingle();
		this.doe.setRelSta(1);
	}

	public Human getSpouse(Human party){
		if (party.isMale()){
			return this.getDoe();
		}
		else{
			return this.getStag();
		}
	}


//Writing


	public String getOffspringHTML(){
		if (this.offspring.size() != 0){
			String s = "<ul>";
			for(Human x: this.offspring){
				s += "<li>"+x.getShortName()+" ("+x.getLifespan()+")</li>";
			}
			return s+"</ul>";
		}
		else{
			return "";
		}
	}

	public String getOffspringGroupHTML(){
		boolean males = false;
		boolean females = false;
		for (Human x: this.offspring){
			if (!x.getSex()){
				males = true;
			} else {
				females = true;
			}
		}
		if (males){
			if (females){
				return "children";
			} else {
				return "sons";
			}
		} else{
			return "daughters";
		}
	}

	public String buildOffspringHTML(){
		if (this.offspring.size() > 1){
			return Basic.getCardinal(this.offspring.size())+" "+this.getOffspringGroupHTML();
		}
		else{
			return "a "+this.offspring.get(0).getOffspring();
		}
	}

	public String buildLivingOffspringHTML(){
		int c = 0;
		for(Human x: this.offspring){
			if (x.aged() >= 20){ c++;}
		}
		if (this.offspring.size() != c){
			if (c > 0){
				if (c > (this.offspring.size()/2) ){
					return ", of which "+Basic.getCardinal(c)+" survived";
				}
				else{
					return ", yet only "+Basic.getCardinal(c)+" survived";
				}
			}
			else{
				if (this.offspring.size() != 1){
					return ", but none survived";
				}
				else{
					return ", but "+this.offspring.get(0).getPronoun()+" didn't survive";
				}
			}
		}
		else{
			return "";
		}
	}

	public static void flushMonthlyWedding(){
		if (Basic.date.get(Calendar.YEAR) >= 1100){
			monthlyWeddingList.add(getMonthlyWedding()/(0.0f+Human.living.size()));
		}
		numOfMonthlyWeddings = 0;
	}

	public static float getMonthlyWeddingAverage(){
		float l = 0;
		for (float x: monthlyWeddingList){
			l += x;
		}
		return l/monthlyWeddingList.size();
	}

	public static int getPerOfCousinUnions(){
		List<Marriage> l = getList();
		int n = 0;
		float i;
		for(Marriage x: l){
			if (x.isCousinUnion()){
				n++;
			}
		}

		i = (n+0.0f)/getNum();
		return (int) (i*100);
	}


	public static int getPerOfSecondCousinUnions(){
		List<Marriage> l = getList();
		int n = 0;
		float i;
		for(Marriage x: l){
			if (x.isSecondCousinUnion()){
				n++;
			}
		}

		i = (n+0.0f)/getNum();
		return (int) (i*100);
	}


	public static int getNumOfLevirates(){
		List<Marriage> l = getList();
		int i = 0;
		for (Marriage x: l){
			if (x.isLevirate()){
				i++;
			}
		}
		return i;
	}

	public static int getNumOfSororates(){
		List<Marriage> l = getList();
		int i = 0;
		for (Marriage x: l){
			if (x.isSororate()){
				i++;
			}
		}
		return i;
	}

	public static int getNumOfChildless(){
		List<Marriage> l = getList();
		int i = 0;
		for (Marriage x: l){
			if (x.isChildless()){
				i++;
			}
		}
		return i;
	}

	//total number of children from current marriages
	public static int getNumOfChildren(){
		List<Marriage> l = getList();
		int i = 0;
		for (Marriage x: l){
			i += x.getNumOfLivingOffspring();
		}
		return i;
	}

	public static int getPerOfLevirates(){
		int n = getNumOfLevirates();
		float i;
		i = (n+0.0f)/getNum();
		return (int) (i*100);
	}

	public static int getPerOfSororates(){
		int n = getNumOfSororates();
		float i;
		i = (n+0.0f)/getNum();
		return (int) (i*100);
	}


	public static int getPerOfChidless(){
		int n = getNumOfChildless();
		float i;
		i = (n+0.0f)/getNum();
		return (int) (i*100);
	}

	public static int getAvgNumOfChildren(){
		int n = getNumOfChildren();

		return n/getNum();
	}


//See if this marriage is the same as the most recent marriage of parameter
	public boolean isLastMarriageOf(Human h){
		return this == h.getLatestMarriage();
	}

	public boolean isCousinUnion(){				return this.kinType == 6;				}
	public boolean isSecondCousinUnion(){		return this.kinType == 8;				}
	public boolean hasKinType(){				return this.kinType != 0;				}
	public byte getKinType(){					return this.kinType;					}
	public boolean isLevirate(){				return this.type == 1;					}
	public boolean isSororate(){				return this.type == 2;					}
	public static int getMonthlyWedding(){		return numOfMonthlyWeddings;			}
	public static int getNum(){					return list.size(); 					}
	public static List<Marriage> getList(){		return new ArrayList<>(list); 			}
	public static Marriage getRecentMarriage(){	return list.get(list.size()-1);			}
	public static void addMonthlyWedding(){		numOfMonthlyWeddings++;					}
	public void setLevirate(){					this.type = 1;							}
	public void setSorotate(){					this.type = 2;							}



}
