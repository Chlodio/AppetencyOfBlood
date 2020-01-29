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
	protected boolean cousinUnion;
	private boolean sated;		//procreational sated
	private byte PRO; 			//procreational optimism 0–20
	private byte type;			//0 = regular, 1 = levirate, 2 = sororate

	protected static Human bestMatch;
	protected static int numOfMonthlyWeddings 			= 0;
	protected static List<Float> monthlyWeddingList 	= new ArrayList<>();
	protected static List<Marriage> dList				= new ArrayList<>(); 	//divorce list
	public static int gen 								= 0;
    protected static List<Marriage> list 				= new ArrayList<>();	//active marriages
    protected static Map<Integer, Marriage> marriages 	= new HashMap<>();
	public static boolean activeCousin 					= false;
	public static int failedMarriageAttemp				= 0;

/*Used for starters*/
	public Marriage(Human husband, Human wife, int year){
		super(husband, wife);
		this.beginning.add(Calendar.DATE, -365*year);
	//	this.anniversary += year;
		this.cousinUnion =  false;
		this.setMarriage();
    }

/*Used for regulars*/
    public Marriage(Human husband, Human wife){
		super(husband, wife);
		this.cousinUnion =  activeCousin;
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
		if (hasCousinMatch(groom)){
			activeCousin = true;
			marryFiancee(groom, bestMatch);
			activeCousin = false;
		}

		if (groom.isUnwed()){
			if (match(groom)){
				marryFiancee(groom, bestMatch);
			} else {
				//	Human w = new Woman(15);
				//	marryFiancee(groom, w);
				if (groom.isNoble()){
					//Morganatic marriage
					if (groom.getAge() >= 30 && groom.isActiveAdulterer()){
						if (groom.hasBornMistress()){
							marryFiancee(groom, groom.getRandomBornMistress());
							groom.getSpouse().getHouse().ennoble();
							System.out.println("morganatic ennoblement");
						}
					} else if (Basic.randint(5) == 0){
						Affair.begin(groom);
					}
					failedMarriageAttemp++;
				}
			}
		}
	}

	public static boolean hasCousinMatch(Human groom){
		List<Human> l = new ArrayList<>();
		int a;
		if (groom.hasFirstCousin()){
			l = groom.getLivingFirstCousins();
			for (Human x: l){
				if (x.isFemaleAdult()){
					a = x.getAge();
					if (x.isUnwed() && a <= (groom.getAge()+5) && a <= 40 ){
						bestMatch = x;
						return true;
					}
				}
			}
		}
		return false;
	}

	public static boolean match(Human bachelor){
		List<Human> brides = new ArrayList<>();
		List<Human> pBrides = new ArrayList<>(Woman.singles);
		Human lass;
		int ba = bachelor.getAge();
		int la = 0;
		int bV; Human bC; 	//best value //best character
		for (int x = 0; x < pBrides.size(); x++){
			lass = pBrides.get(x);
			//No old women
			la = lass.getAge();
			if (!bachelor.isFromSameEstate(lass)){	continue;	}
			if (la > ba-5|| la > 40){ continue;}
			//No parent-child marriages
			if (bachelor.getMother() == lass){ continue; }
			if (bachelor == lass.getFather() ){ continue; }
			//No siblin marriages
			if (bachelor.getFather() != null && (bachelor.getFather() == lass.getFather())){ continue; }
			if (bachelor.getMother() != null && (bachelor.getMother() == lass.getMother())){ continue; }
			//
			brides.add(lass);
		}
		if (brides.size() != 0){
			/*
			List<Human> aBrides = new ArrayList<>();	// accepted brides
			int aP = 1+bachelor.getHouse().getPrestige();
			//Prestige of bride is more than groom's/hypergamy
			for (int y = bachelor.getHouse().getRanking(); y > 0; y--){
				if(aBrides.size() == 0){
					for(Human x: brides){
						aBrides.add(x);
					}
				}
			}

//			for (int y = 2; y < 11; y++){
//				if(aBrides.size() == 0){
//					for(Human x: brides){
//						//&& aP >= x.house.prestige
//						if (x.house.prestige >= aP/y ){
//							aBrides.add(x);
//						}
//					}
//				}
//			}
			if (aBrides.size() != 0){
	//			int aP = (bachelor.house.prestige/4)*3; 		//accepted prestige
	//			while(aBrides.size() == 0){
	//				aP -= bachelor.house.prestige/4;
	//				for(Human x: brides){
	//					if (x.house.prestige >= aP){
	//						aBrides.add(x);
	//					}
	//				}
	//			}
				bV = aBrides.get(0).getMating();
				bC = aBrides.get(0);
				for(Human x: aBrides){
					if (x.getMating() > bV){
						bV = x.getMating();
						bC = x;
					}
				}*/
				bestMatch = Basic.choice(brides); //bC;
				return true;
			//}
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

	public static Human findWife(Human a){
		for(Human x: Woman.singles){
			if (a.getAge() >= x.getAge() && !a.isSiblingOf(x)){
				return x;
			}
		}
		return Woman.singles.get(0);
	}

	public static boolean hasWomanSingles(Human a){
		for(Human x: Woman.singles){
			if (a.getAge() >= x.getAge() && !a.isSiblingOf(x)){
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

//No idea what was the point of this...
//	public List<Human> getOffspring(){
//		List<Human> list = new ArrayList<>();
//		if (this.offspring.size() != 0){
//			for (Human x: this.offspring){
//				list.add(x);
//			}
//		}
//		return list;
//	}



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

	public void breed(){
		Woman w = (Woman) this.doe;

		//multiple sons are only for noblemen
		if (this.stag.isPeasant() && this.stag.hasNumOfLivingSons(4)){
			return;
		}

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

	public boolean isCousinUnion(){				return this.cousinUnion;				}
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
