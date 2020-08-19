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
					return h.getPossessive()+" cousin";
				case 8:
					return h.getPossessive()+" second cousin";
				case 4:
					return h.getPossessive()+" "+this.getKinNibling(h);
				case 5:
					return h.getPossessive()+" "+this.getKinPibling(h);
				default:
					return "";
			}
		} else {
			return "";
		}
	}

	public String getTypeHTML(Human h){
		switch(this.getType()){
			case 1:
				if(h.isMale()){
					return "his brotherꞌs widow";
				} else {
					return "her previous husbandꞌs brother";
				}
			case 2:
				if(h.isMale()){
					return "his previous wifeꞌs sister";
				} else {
					return "her sisterꞌs widower";
				}
			default:
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

	public static void checkProposals(int maom){
		List<Human> w = Woman.getSingles();
		if (w.size() >= 2){
			List<Integer> l = new ArrayList<>(w.size());
			int i = Basic.max(2, (int) (w.size()*0.05) );//(int) (w.size()*0.025);

			int a;
			while(l.size() < i){
					a = Basic.randint(w.size());
					if (!l.contains(a)){
						l.add(a);
					}
			};
			for(int x: l){
				propose(w.get(x), maom);
			}
		}
	}

	public static void propose(Human h, int maom){
		int f = Basic.randint(maom)+1;
		Basic.dayC.get(f).add(h);
		Basic.dayE.get(f).add(1);
		addMonthlyWedding();
	}

	public static int bs = 0;

	public static void prepare(Human b){
		if (match(b)){
			marryFiancee(bestMatch, b);
		}

		/*else if (b.isNoble()){
			if (Basic.getDateYear() > 1100 ){
				bs++;
				//throw new RuntimeException();
			}*/
/*			Human w = Woman.findWench();
			if (w != null){
				marryFiancee(groom, w);
			}
		}*/

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
		Human h;
		if (b.isNoble()){
			h = matchHighborn(b);
		} else {
			h = matchLowborn(b);
		}
		if (h != null){
			bestMatch = h;
			return true;
		} else {
			return false;
		}
	}

	public static Human matchHighborn(Human b){
		List<Human> l = b.getFromSameEstate(Man.getSingles());
		List<Human> m = new ArrayList<>(l.size());

		for(Human x: l){
			if (!x.areCloselyRelated(b)){
				m.add(x);
			}
		}

		List<Human> s = new ArrayList<>(m.size());

		for(Human x: m){
			if (x.isPolitican()){
				s.add(x);
			}
		}

		if (s.size() > 0){
			return Basic.choice(s);
		} else {
			for(Human x: m){
				if (x.isSonless()){
					s.add(x);
				}
			}
			if (s.size() > 0){
				return Basic.choice(s);
			} else if (m.size() > 0){
				return Basic.choice(m);
			} else {
				return null;
			}
		}
	}

	public static Human matchLowborn(Human b){
		List<Human> l = b.getFromSameEstate(Man.getSingles());
		List<Human> m = new ArrayList<>(l.size());

		for(Human x: l){
			if (!x.areCloselyRelated(b)){
				m.add(x);
			}
		}

		if (m.size() > 0){
			return Basic.choice(m);
		}
		return null;
	}


  public static void marryBetrothed(Human husband, Human wife){
		Marriage.marry(husband, wife);
    Basic.print(husband.getFullName()+" gave "+wife.getFullName()+" a green gown.");
	}

	public static void marryFiancee(Human husband, Human wife){
		if (!husband.isMale() || wife.isMale() ){
			throw new RuntimeException();
		}
		Marriage.marry(husband, wife);
		Basic.annals.recordMarriage(husband, wife);
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
		return this.beginning.get(Calendar.YEAR)-spouse.getBirth().get(Calendar.YEAR);
	}

	/*Widow marries her brother-in-laws*/
	public static void doLevirate(Human widow, Human departed){
		marryFiancee(departed.getUnwedPatBrother(), widow);
		getRecentMarriage().setLevirate();
	}

	/*Widower marries his sister-in-laws*/
	public static void doSororate(Human widower, Human departed){
		marryFiancee(widower, departed.getUnwedPatSister());
		getRecentMarriage().setSorotate();
	}

	public void terminate(){
		this.ending = (Calendar) Basic.date.clone();
		this.active = false;
		this.getStag().setSpouseNull();
		this.getDoe().setSpouseNull();
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
			if (this.getStag().isNoble() || this.getStag().getLivingChildren().size() <= 8){
				return true;
			}
		}
		return false;
	}

	public void breed(){
		Woman w = (Woman) this.doe;
		if (this.canBreed() && !w.isPregnant()){
			if (w.isUnderAgeOf(46)){
				if (Basic.randint(100) < this.calcProcreation() ){
					w.fillUterus(this);
					w.growFetus();
					Woman.pregnant.add(this.doe);
				}
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
			if (x.getAged() >= 20){ c++;}
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


	public static int getPerOfChildless(){
		int n = getNumOfChildless();
		float i;
		i = (n+0.0f)/getNum();
		return (int) (i*100);
	}

	public static float getAvgNumOfChildren(){
		float n = getNumOfChildren()+0.0f;

		return n/getNum();
	}

	public String getHTMLInterfix(){
		String s = "";
		if (this.isActive()){
			s =  ", the union has lasted ";
			s += ""+Basic.getYearsAndDays(Basic.getDaysBetween(this.getBeginning(), Basic.getDate()));
		} else {
			s =  ", the union lasted ";
			s += ""+Basic.getYearsAndDays(Basic.getDaysBetween(this.getBeginning(), this.getEnding()));
		}
		return s;
	}

	//Offer explanation why no children were born fro
	public String findReasonForInfertility(){
		if (this.getDoe().wasBarren()){
			if (this.getStag().hadChild()){
				return this.getStag().getForename()+" failed to impregnate his wife";
			} else {
				return this.getStag().getForename()+" was incapable of breeding";
			}
		} else if (this.getStag().hadChild()){
			return this.getDoe().getForename()+" was barren";
		 } else {
			return "both the husband and the wife appeared incapable of reproducing";
		 }
	}

	public String getFirstbornHTML(){
		Human h = this.getFirstborn();
		String s = "child, "+h.getFormalName()+" was born in "+h.getBirthYear();
		if (this.getNumOfOffspring() > 1){
			return "; their first "+s;
		} else {
			return "; their only "+s;
		}
	}

	public String getOffspringInfo(){
	 	int s = this.getNumOfSons();
		int d = this.getNumOfDaughters();
		if (Basic.isNotZero(s)){
			if (Basic.isNotZero(d)){
				return Basic.getPlural(s, "son")+" and "+Basic.getPlural(d, "daughter");
			} else {
				return Basic.getPlural(s, "son");
			}
		} else {
			return Basic.getPlural(d, "daughter");
		}
	}

	public String getSurvivingOffspringInfo(){
			List<Human> l = this.getSurvivingOffspring();
			if (l != null){
				String s = Human.getNamesOnList(this.getSurvivingOffspring());
				if (l.size() > 1){
					return ", the surviving children were: "+s;
				} else if (this.getNumOfOffspring() > 1){
					if(this.getFirstborn() != l.get(0)){
						if (this.getFirstborn().isSameSex(l.get(0))){
							return ", only their other "+l.get(0).getOffspring()+", "+s+" survived";
						} else {
							return ", only their other child, "+s+" survived";
						}
					} else {
						return "";
					}
				} else {
					return "";
				}
			} else if (this.getNumOfOffspring() > 1){
				return ", alas no issue survived";
			} else {
				return ", but "+this.getFirstborn().getPronoun()+" didn't survive";
			}
	}

	public String getChildrenHTML(){
		String s;
		if (!this.wasChildless()){
			s = ", which resulted in "+this.getOffspringInfo();
			s = s+this.getFirstbornHTML();
			return s+""+this.getSurvivingOffspringInfo();
		} else if (!this.isActive()) {
			s = ", but the union proved childless for ";
			s += this.findReasonForInfertility();
			return s;
		}
		return "";
	}

	//Used for men and women
	public String getHTMLCommon(){
		String s = this.getHTMLInterfix();
		s += this.getChildrenHTML();
		return s;
	}

	public String getHTMLPrefixM(){
		String s = " In "+this.getBeginningYear()+", ";
		int a = this.getStag().getAgeIn(this.getBeginning());
		int b = this.getDoe().getAgeIn(this.getBeginning());
		s += "at the age of "+Basic.getCardinal(a);
		s += " "+this.getStag().getPronoun()+" married ";

		if (this.hasKinType()){
			s += this.getKinTypeHTML(this.getStag())+" ";
		}

		if (a != b){
			s += Basic.getCardinal(b)+"-year-old ";
		} else {
			s += "same-aged ";
		}
		s += this.getDoe().getName().getPatronymic();
		s += this.getHTMLCommon();
		return s+".";
	}

	public String getHTMLPrefixF(){
		String s = " In "+this.getBeginningYear()+", ";
		int a = this.getDoe().getAgeIn(this.getBeginning());
		int b = this.getStag().getAgeIn(this.getBeginning());
		s += "at the age of "+Basic.getCardinal(a);
		s += " "+this.getDoe().getPronoun()+" married ";
		if (a != b){
			s += Basic.getCardinal(b)+"-year-old ";
		} else {
			s += "same-aged ";
		}
		s += this.getStag().getName().getPatronymic();
		s += this.getHTMLCommon();
		return s+".";
	}


//See if this marriage is the same as the most recent marriage of parameter
	public boolean isLastMarriageOf(Human h){
		return this == h.getLatestMarriage();
	}

	public boolean isCousinUnion(){					return this.kinType == 6;				}
	public boolean isSecondCousinUnion(){		return this.kinType == 8;				}
	public boolean hasKinType(){						return this.kinType != 0;				}
	public byte getKinType(){								return this.kinType;						}
	public boolean isLevirate(){						return this.type == 1;					}
	public boolean isSororate(){						return this.type == 2;					}
	public boolean isRegular(){							return this.type == 0;					}
	public int getType(){										return this.type;								}
	public static int getMonthlyWedding(){	return numOfMonthlyWeddings;		}
	public static int getNum(){							return list.size(); 						}
	public static List<Marriage> getList(){	return new ArrayList<>(list); 	}
	public static Marriage getRecentMarriage(){
		return list.get(list.size()-1);
	}
	public static void addMonthlyWedding(){		numOfMonthlyWeddings++;				}
	public void setLevirate(){					this.type = 1;							}
	public void setSorotate(){					this.type = 2;							}



}
