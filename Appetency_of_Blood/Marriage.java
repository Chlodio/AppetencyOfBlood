import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Marriage {
	private boolean active;
	private Calendar beginning;
	private Calendar ending;
	private float fertility;
	private Human husband;
	private Human wife;
	private int anniversary;
	private int happiness;
	private int[] procreation;
	private List<Human> offspring;
	private boolean cousinUnion;
	private static Human bestMatch;
	private static int numOfMonthlyWeddings 			= 0;
	private static List<Float> monthlyWeddingList 	= new ArrayList<>();
	private static List<Marriage> dList					= new ArrayList<>(); 	//divorce list
	public static int gen 								= 0;
    private static int id 								= 0;
    private static List<Marriage> list 					= new ArrayList<>();
    private static Map<Integer, Marriage> marriages 	= new HashMap<>();
	public static boolean activeCousin 					= false;
	final static double[][] fert = {{0.0, 0.0, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8},
	{0.9, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0},
	{1.0, 0.95, 0.9, 0.85, 0.8, 0.75, 0.7, 0.65, 0.6, 0.55},
	{0.5, 0.45, 0.4, 0.35, 0.3, 0.25, 0.2, 0.15, 0.1, 0.05},};

    public Marriage(Human husband, Human wife){
        this.id++;
		this.husband = 		husband;
		this.wife = 		wife;
        this.beginning = 	(Calendar) Main.date.clone();
        husband.spouse = 	wife;
		wife.spouse = 		husband;
		this.fertility = 	Math.min(husband.fertility,wife.fertility);
		this.offspring = 	new ArrayList<>();
		this.anniversary = 	0;
		this.active = 		true;
		this.happiness =	1+Main.randint(10);
		this.cousinUnion =  activeCousin;
		int ha = 			husband.getAge();
		int wa = 			wife.getAge();
		int fmy = 			70-ha;
		int ffy = 			50-wa;
		float v = 			(this.fertility/10f)*(0.75f+(this.happiness*0.05f));
	//	System.out.println(v);
	//	Main.pause(1000);
		this.procreation =  new int[100-ha];
		if (fmy > 0 && ffy > 0){
			int tfy = Math.min(fmy, ffy);
			for (int x = 0; x < tfy; x++){
				this.procreation[x] = (int) (v*(fert[(wa-12)/10][(wa-12)%10]));
				wa++;
			}
		}
        Marriage.list.add(this);
    }

	public static void propose(Human bachelor, int maom){
		if (Main.randint(bachelor.mating) == 0){
			int f = Main.randint(maom)+1;
			Main.dayC.get(f).add(bachelor);
			Main.dayE.get(f).add(1);
			addMonthlyWedding();
	//		System.out.println(getMonthlyWedding());
	//		System.exit(0);
		}
	}

	public static void prepare(Human groom){
		if (hasCousinMatch(groom)){
			activeCousin = true;
			marryFiancee(groom, bestMatch);
			activeCousin = false;
		}

		if (groom.isUnwed() && match(groom)){
			marryFiancee(groom, bestMatch);
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
					if (x.isUnwed() && a <= (groom.getAge()+5) && a >= 40 ){
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
			if (la > ba-5|| la > 40){ continue;}
			//No parent-child marriages
			if (bachelor.mother == lass){ continue; }
			if (bachelor == lass.father){ continue; }
			//No siblin marriages
			if (bachelor.father != null && (bachelor.father == lass.father)){ continue; }
			if (bachelor.mother != null && (bachelor.mother == lass.mother)){ continue; }
			//
			brides.add(lass);
		}
		if (brides.size() != 0){
			List<Human> aBrides = new ArrayList<>();	// accepted brides
			int aP = 1+bachelor.house.getPrestige();
			//Prestige of bride is more than groom's/hypergamy
			for (int y = bachelor.house.getRanking(); y > 0; y--){
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
				bV = aBrides.get(0).mating;
				bC = aBrides.get(0);
				for(Human x: aBrides){
					if (x.mating > bV){
						bV = x.mating;
						bC = x;
					}
				}
				bestMatch = bC;
				return true;
			}
		}
		return false;
	}

    public static void marryBetrothed(Human husband, Human wife){
		Marriage.marry(husband, wife);
        Main.print(husband.getName().getFull()+" gave "+wife.getName().getFull()+" a green gown.");
	}

	public static void marryFiancee(Human husband, Human wife){
		Marriage.marry(husband, wife);
		Main.print(husband.getName().getFull()+" married "+wife.getName().getFull());
		husband.becomeTaken();
		wife.becomeTaken();
	}

    public static void marry(Human husband, Human wife){
        Marriage temp = new Marriage(husband, wife);
        Marriage.marriages.put(Marriage.id, temp);
        husband.marriages.add(temp);
		wife.marriages.add(temp);
		husband.relSta = 2;
		wife.relSta = 2;
		if (husband.title == Title.KING){
			Realm.getHolder(0).setConsort(wife);
		}
    }

	public void breed(){
		Woman wife = (Woman) this.wife;
		try{
			if (Main.randint(100) < this.procreation[this.anniversary] && !wife.isPregnant()){
			   	wife.fillUterus(((Man) this.husband));
				wife.growFetus();
				Woman.pregnant.add(this.wife);
			}
		}
		catch (ArrayIndexOutOfBoundsException e){
			System.out.println("Husband alive: "+this.husband.isAlive()+"\nWife alive: "+this.wife.isAlive());
			System.out.println(this.husband.getDeath()+"\n"+this.wife.getDeath());
			System.out.println("Began: "+this.getBeginning());
			System.out.println("Active: "+this.isActive());
			System.exit(0);
		}
	}

	public int getAgeAt(Human spouse){
		return this.beginning.get(Calendar.YEAR)-spouse.birth.get(Calendar.YEAR);
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
	}

	/*Widower marries his sister-in-laws*/
	public static void doSororate(Human widower, Human departed){
		marryFiancee(widower, departed.getUnwedSister());
	}

	public void terminate(){
		this.ending = (Calendar) Main.date.clone();
		this.active = false;
		Marriage.list.remove(this);
	}

	public static void doAnnual(){
		for(Marriage x: list){
			x.anniversary++;
			//if (x.considerDivorce()){ dList.add(x); }
		}
		for(Marriage x: dList){
			x.getDivorce();
		}
		dList.clear();
	}

	public static void doBreeding(){
		for (Marriage x: list){
			x.breed();
		}
	}

	public static int getNum(){											return list.size(); }

	public boolean considerDivorce(){
		if(((this.anniversary%10) == 0) && this.husband.isSonless()){
			if (Main.randint(5) == 0){
				return true;
			}
		}
		return false;
	}

	public void getDivorce(){
		this.terminate();
		this.husband.becomeSingle();
		this.husband.relSta = 1;
		this.wife.becomeSingle();
		this.wife.relSta = 1;
	}

	public Human getSpouse(Human party){
		if (party.isMale()){
			return this.getWife();
		}
		else{
			return this.getHusband();
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
			if (!x.sex){ males = true;}
			else{ females = true; }
		}
		if (males){
			if (females){ return "children"; }
			else { return "sons";}
		}
		else{ return "daughters"; }
	}

	public String buildOffspringHTML(){
		if (this.offspring.size() > 1){
			return Main.cardinal[this.offspring.size()]+" "+this.getOffspringGroupHTML();
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
					return ", of which "+Main.cardinal[c]+" survived";
				}
				else{
					return ", yet only "+Main.cardinal[c]+" survived";
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

	public String getHappinessDesc(){
		return happyDesc[this.happiness-1]+" "+Main.choice(unionDesc);
	}
	public static String[] happyDesc = 			{"loathing", "pugnacious", "miserable",
												"joyless", "cold", "luke-warm", "warm",
												"cordial", "merry", "ardent"};

	public static String[] unionDesc = 			{"union", "couple", "wedlock", "marriage", "matrimony"};

	public static void flushMonthlyWedding(){
		if (Main.date.get(Calendar.YEAR) >= 1100){
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

	public boolean isActive(){					return this.active;						}
	public Human getHusband(){					return this.husband;					}
	public Human getWife(){						return this.wife;						}
	public int getBeginning(){					return this.beginning.get(Calendar.YEAR);}
	public int getOffspringNum(){				return this.offspring.size();			}
	public List<Human> getOffspring(){			return this.offspring;					}
	public static int getMonthlyWedding(){		return numOfMonthlyWeddings;			}
	public static void addMonthlyWedding(){		numOfMonthlyWeddings++;					}
	public void addOffspring(Human o){			this.offspring.add(o);					}
	public boolean isCousinUnion(){				return this.cousinUnion;				}
}
