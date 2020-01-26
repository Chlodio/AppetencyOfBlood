package House;
import Human.*;
import Common.*;
import Politics.Title;
import Relationship.*;
import Ancestry.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Calendar;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;

public class House {
	private static List<House> list = new ArrayList<>();
	public static House nextHouse;
    private static int id = 0;

	protected boolean isActive;
	protected Calendar founding;
	protected House parent;
	protected Human founder;
	protected Human head;
	protected Human patriarch;						//former head whose memory keeps the family together
	protected int generation;
	protected int naming;
	protected int nameNum;
	protected int coa;								//coat of arms
	protected int prestige;
	protected int ranking;
	protected boolean isNoble;						//true = noble/false = lowborn
	protected List<House> branches;
	protected List<Human> heads;
	protected List<Human> kinsmen;
	protected List<Human> kinswomen;
	protected List<Human> princes;					//living sons of the patriarch
	protected List<String> femaleNames;
	protected List<String> maleNames;
	protected static String spareName;								//used for temp
	protected String name;
	protected boolean legimate;						//false = bastard house

	/*	0		orderly
	 	1		weighted random
		2		equal random
		3		grandfatherly method
		4		respectful (late)*/

    public House(Human head){
		((House) this).handles(head);
		this.legimate = true;
		this.isNoble = false;
		this.nameHouseLowborn();
    }

	public House(Human head, String s){
		((House) this).handles(head);
		this.legimate = false;
		this.isNoble = false;
		this.name = 							"Fitz"+s;
	}

	public void handles(Human head){
        id++;
		this.activate();
		this.founding = 						(Calendar) Basic.date.clone();
		this.head = 							head;
		this.heads = 							new ArrayList<>();
		this.heads.add(head);
		this.kinsmen = 							new ArrayList<>();
		this.kinswomen = 						new ArrayList<>();
		this.branches = 						new ArrayList<>();
		this.princes =							new ArrayList<>();
		this.naming	=							Basic.randint(6);
		this.coa =								Basic.randint(100)+1;
		Basic.house.put(House.id, this);
		this.addToPeasants();
	}


	public static void buildNames(){
		int c 		= 0;
		String l 	= null;
		FileReader reader;
		BufferedReader buffy;
		try {
			reader = new FileReader("Input/Surname_lowborn.txt");
			buffy = new BufferedReader(reader);
			while((l = buffy.readLine()) != null) {
				lowbornNames[c] = l;
				c++;
            }
			buffy.close(
			);
		}
		catch(IOException e){
			System.out.println(e);
			System.exit(1);
		}
		c = 0;
		try {
			reader = new FileReader("Input/Surname_highborn.txt");
			buffy = new BufferedReader(reader);
			while((l = buffy.readLine()) != null) {
				highbornNames[c] = l;
				c++;
            }
			buffy.close();
		}
		catch(IOException e){
			System.out.println(e);
			System.exit(1);
		}
	}

	public Human getHeir(){
		for(int x = this.heads.size()-1; x >= 0; x--){
			if (((Man) this.heads.get(x)).hasAgnaticLine()){
				return ((Man) this.heads.get(x)).getAgnaticHeir();
			}
		}
		return null;
	}

	public void succession(Human h){
		/*patriarchy can only be inherited:
			1.	from father to son
			2.	from brother to brother
			3.	from nephew to uncle
			4. 	from uncle to nephew
			5. from cousin to cousin while uncle is alive
		*/
		Human heir = h;
		if (this.getKinsmen().size() > 0){
			heir = getHeir();
		}
		if (heir != h){
			if (heir.getHouse() == this){
				this.head = heir;
				this.heads.add(this.head);
				if (this.head.isAdult()){
					if (this.head.isSonless() && this.head.isUnwed()){
						Marriage.prepare(this.head);
					}
				}
				if (this.head.hasTitle()){
					this.head.rename(Title.LORD);
					if (this.head.isMarried()){
						Human s = this.head.getSpouse();
						if (!s.hasTitle() || Title.LORD.getPrestige() >= s.getTitle().getPrestige()){
							s.rename(Title.LADY);
						}
					}
				}
				return;
			}
		}
		this.deactivate();
		nextHouse = null;
		if (this.isNoble() && !this.isLegimate()){
			this.returnToCirculation();
		}
		if (this.findNextHouse()){
			this.handleRanking(nextHouse);
			Basic.print("The senior line of "+this.getFullName()+" went extinct, but was succeeded by "+nextHouse.getName());
		} else{
			Basic.print(this.getName()+" went extinct");
		}

		if (list.contains(this)){
			System.out.println("ERROR dead house is alive");
			throw new RuntimeException();
		}
	}

	public void handleRanking(House newHouse){
		if (this.hasHigherRanking(newHouse)){
			newHouse.setRanking(this.ranking);
		}
	}

	public boolean hasHigherRanking(House newHouse){
		return this.ranking > newHouse.ranking;
	}

	//Current head's ancestor who is the son of the branch leader
	/*Lets search for common ancestor between */
	public int getSeniorBranchNum(){
		Human p = this.getPatriarch();
		Human h = this.getHead();
		try{
			while(h.getFather() != p){
				h = h.getFather();
				if (h.isPosthumous()){
					System.out.println("posthum");
				}
			}
		} catch (NullPointerException e){
			h = this.getHead();
			System.out.println(h.getHouse()==p.getHouse());
			System.out.println("One of the heads is posthumous:"+this.hadPosthumousHead());
			System.out.println("Senior branch not found");
			System.out.println(this+" "+this.getHeads().get(this.getHeads().size()-1));
			System.out.println("Living male members: "+this.getKinsmen().size());
			System.out.println(this.getHead()+" "+p);
			if (this.getHead().getFather().hadPatGrandpa()){
				Consanguinity.printDes(p.getFather().getFathersFather(), 0);
			} else{
				Consanguinity.printDes(p.getFathersFather(), 0);
			}
/*			System.out.print(o+" died");
			System.out.println(", the current "+this.getHead()+" is his "+Consanguinity.getPaternalRelation(o, this.getHead()));
			System.out.println(o.getBirth()+" "+o.isAlive());
			System.out.println(this.getHead().getBirth()+" "+this.getHead().isAlive());
			System.out.println(this.getHead().getFather().getBirth()+" "+this.getHead().getFather().isAlive());
			System.out.println("Head has patruus: "+this.getHead().hasPatruus());
			if (this.getHead().getFather().hasPatruus()){
				System.out.println("Head has patruus");
				System.out.println(this.getHead().getFather().getPatruus());
				System.exit(0);
			}
			if(o.getFather().hadPatGrandpa()){
				Consanguinity.printDes(o.getFather().getPatGrandpa(), 0);
			} else{
				Consanguinity.printDes(o.getPatGrandpa(), 0);
			}
			System.out.println("O-man's father is"+h+"\t"+Consanguinity.getPaternalRelation( this.getHead(), h)+" "+(this.getHead().getPatGrandpa()==h));
			System.out.println(h.getPatGrandpa().isSiblingOf(this.getHead().getPatGrandpa()));
			System.out.println( "O-man's grandfather is"+h.getFather()+"\t"+Consanguinity.getPaternalRelation( this.getHead(), h.getFather()));

			for(Human it: this.getHeads()){
				System.out.println(it+"\t"+it.getLifespan()+"\t"+Consanguinity.getPaternalRelation( this.getHead(), it));
			}
			son = this.getHead();
			while(son.getFather() != h){
				System.out.println(son);
				son = son.getFather();
			}	*/
			System.exit(0);
		}

		return this.getPatriarch().getSons().indexOf(h);
	}

	//Cadet branch is formed, if cadet has living sons, his father is dead)
	public void branch(){
		int num = 			this.getSeniorBranchNum();
		List<Human> sons = 	this.getPatriarch().getSons();
		int sonN =			sons.size();
		List<Human> list =	new ArrayList<>();
		CadetHouse nHouse;
		Human head;
		this.setPatriarch(sons.get(num));
		for(int x = num+1; x < sonN; x++){
			if ( ((Man) sons.get(x)).hasAgnaticLine()){
				list.add(sons.get(x));
			}
		}

		for(Human x: list){
			head = ((Man) x).getAgnaticHeir();
			nHouse = new CadetHouse(this, head, x);
		}

	}


	public boolean findNextHouse(){
		if (this.findNextHouseDirect()){ 			return 	true; 	}
		else if (this.findNextHouseIndirect()){ 	return true; 	}
		return false;
	}

	public boolean findNextHouseDirect(){
		for (int x = this.branches.size()-1; x >= 0; x--){
			if (this.branches.get(x).kinsmen.size() != 0){
				nextHouse = this.branches.get(x); return true;
			}
			else {
				if (this.branches.get(x).findNextHouseDirect() == true){ return true;}
			}
		}
		return false;
	}

	public boolean findNextHouseIndirect(){
		if(this.parent != null){
			if (this.parent.findNextHouseDirect()){ 		return true; }
			else if (this.parent.findNextHouseIndirect()){ 	return true; }
		}
		return false;
	}

//Name methods


	public void nameHouseLowborn(){
		this.name = lowbornNames[Basic.randint(lowbornNames.length)];
	}

	public void nameHouseHighborn(){
		int n = 								fetchName();
		this.name = 							highbornNames[n];
		this.nameNum =							n;
	}

	public String getMemberNameM(Human c){
		switch(this.naming){
			case 0:
				return this.doOrderlyNamingM(c);			//orderly
			case 1:
				return this.doWeightNamingM(c);				//weighted random naming
			case 2:
				return this.doFlatNamingM(c);				//stochastic
			case 3:
				return this.doAncestralNamingM(c);			//ancestral naming
			case 4:
				return this.doParentalNamingM(c);			//parentral naming
			case 5:
				return this.doAlteringNamingM(c);			//altering naming
			default:
				System.exit(0);
		}
		return "?";
	}

	public String getMemberNameF(Human c){
		switch(this.naming){
			case 0:
				return this.doOrderlyNamingF(c);			//orderly
			case 1:
				return this.doWeightNamingF(c);				//weighted random naming
			case 2:
				return this.doFlatNamingF(c);				//stochastic
			case 3:
				return this.doAncestralNamingF(c);			//ancestral naming
			case 4:
				return this.doParentalNamingF(c);			//parentral naming
			case 5:
				return this.doAlteringNamingF(c);			//altering naming
			default:
				System.exit(0);
		}
		return "?";
	}

	public String pickRandomNameM(){
		String newN;
		do {
			newN = Name.getRandomMaleName();
		} while(this.isMNameUsed(newN) && this.getKinsmen().size() < 100);
		this.addMaleName(newN);
		return newN;
	}

	public String pickRandomNameF(){
		String newN;
		do {
			newN = Name.getRandomFemaleName();
		} while(this.isFNameUsed(newN) && this.getKinswomen().size() < 100);
		this.addFemaleName(newN);
		return newN;
	}

	public String doAlteringNamingM(Human h){
		this.naming = Basic.randint(5);
		String n = this.getMemberNameM(h);
		this.naming = 5;					//reset
		return n;
	}

	public String doAlteringNamingF(Human h){
		this.naming = Basic.randint(5);
		String n = this.getMemberNameF(h);
		this.naming = 5;					//reset
		return n;
	}

	public String doFlatNamingM(Human h){
		String n;
		List<String> u = this.getUsableNamesM(h.getFather());
		if (u.size() > 0){
			return Basic.choice(u);
		}
		return this.pickRandomNameM();
	}

	public String doFlatNamingF(Human h){
		String n;
		List<String> u = this.getUsableNamesF(h.getFather());
		if (u.size() > 0){
			return Basic.choice(u);
		}
		return this.pickRandomNameF();
	}

	public String doWeightNamingM(Human h){
		String n;
		List<String> u = this.getUsableNamesM(h.getFather());
		if (u.size() > 0){
			return Basic.choiceW(u);
		}
		return this.pickRandomNameM();
	}

	public String doWeightNamingF(Human h){
		String n;
		List<String> u = this.getUsableNamesF(h.getFather());
		if (u.size() > 0){
			return Basic.choiceW(u);
		}
		return this.pickRandomNameF();
	}

	public String doOrderlyNamingM(Human h){
		String n;
		List<String> u = this.getUsableNamesM(h.getFather());
		if (u.size() > 0){
			return u.get(0);
		}
		return this.pickRandomNameM();
	}

	public String doOrderlyNamingF(Human h){
		String n;
		List<String> u = this.getUsableNamesF(h.getFather());
		if (u.size() > 0){
			return u.get(0);
		}
		return this.pickRandomNameF();
	}

	public String doParentalNamingM(Human h){
		String n;
		List<String> u;
		if (h.hasFather()){
			n = h.getFather().getName().getName();
			if (!h.getFather().hasSonWithTheName(n)){
				return n;
			}
		}
		u = this.getUsableNamesM(h.getFather());
		if (u.size() > 0){
			return Basic.choiceW(u);
		}
		return this.pickRandomNameM();
	}

	public String doParentalNamingF(Human h){
		String n;
		List<String> u;
		if (h.hasFather()){
			n = h.getMother().getName().getName();
			if (!h.getFather().hasDaughterWithTheName(n)){
				/*if (n.charAt(n.length()-1) != 'a'){
					System.out.println(h.getMother().getFullName());
					System.out.println(h.getMother().isFemale());
					System.out.println(n);
					String o = names[500];
					System.exit(0);
				}*/
				return n;
			}
		}
		u = this.getUsableNamesF(h.getFather());
		if (u.size() > 0){
			return Basic.choiceW(u);
		}
		return this.pickRandomNameF();
	}

	public String doAncestralNamingM(Human h){
		String gfn;
		List<String> u;
		if (h.hadPatGrandpa()){
			gfn = h.getFathersFather().getName().getName();
			if (!h.getFather().hasSonWithTheName(gfn)){
				return gfn;
			}
			else if (h.hadMatGrandpa()){
				gfn = h.getMothersFather().getName().getName();
				if (!h.getFather().hasSonWithTheName(gfn)){
					if (!this.isMNameUsed(gfn)){
						this.addMaleName(gfn);
					}
					return gfn;
				} else {
					u = this.getUsableNamesM(h.getFather());
					u.remove(h.getFathersFather().getName().getName());
				}
			} else {
				u = this.getUsableNamesM(h.getFather());
				u.remove(gfn);
			}
		} else {
			u = this.getUsableNamesM(h.getFather());
		}

		u.remove(h.getFather().getName().getName());
		if (u.size() > 0){
			return Basic.choiceW(u);
		}
		return this.pickRandomNameM();
	}

	public String doAncestralNamingF(Human h){
		String gfn;
		List<String> u;
		if (h.hadPatGrandpa()){
			gfn = h.getFathersMother().getName().getName();
			if (!h.getFather().hasDaughterWithTheName(gfn)){
				return gfn;
			}
			else if (h.hadMatGrandpa()){
				gfn = h.getMothersMother().getName().getName();
				if (!h.getFather().hasDaughterWithTheName(gfn)){
					if (!this.isFNameUsed(gfn)){
						this.addFemaleName(gfn);
					}
					return gfn;
				} else {
					u = this.getUsableNamesF(h.getFather());
					u.remove(h.getFathersMother().getName().getName());
				}
			} else {
				u = this.getUsableNamesF(h.getFather());
				u.remove(gfn);
			}
		} else {
			u = this.getUsableNamesF(h.getFather());
		}

		u.remove(h.getFather().getName().getName());
		if (u.size() > 0){
			return Basic.choiceW(u);
		}
		return this.pickRandomNameF();
	}

	public List<String> getUsableNamesM(Human h){
		List<String> houN = 	new ArrayList<>(this.maleNames);
		List<String> l = 		new ArrayList<>(h.getLivingSonsNames());
		List<String> pot = 		new ArrayList<>();
		for(String x: houN){
			if(!l.contains(x)){
				pot.add(x);
			}
		}


		return pot;
	}

	public List<String> getUsableNamesF(Human h){
		List<String> houN = 	new ArrayList<>(this.femaleNames);
		List<String> l = 		new ArrayList<>(h.getLivingDaughtersNames());
		List<String> pot = 		new ArrayList<>();
		for(String x: houN){
			if(!l.contains(x)){
				pot.add(x);
			}
		}
		/*if(h.isRegnant()){
			System.out.println("Houn:");
			for(String x: houN){
				System.out.println(x);
			}
			System.out.println("Pot:");
			for(String x: pot){
				System.out.println(x);
			}
			System.out.println("l:");
			for(String x: l){
				System.out.println(x);
			}
			//Basic.pause(500);
			//System.exit(0);
		}*/
		return pot;
	}

	public boolean isSpareName(Human father){
		spareName = null;
		List<Human> temp = new ArrayList<>(father.getLivingSons());
		List<String> houN = new ArrayList<>(this.maleNames);
		List<Human> sons = new ArrayList<>();
		List<String> pot = new ArrayList<>();
		List<String> names = new ArrayList<>();
		int comp = 0;

		for(int x = 0; x < temp.size()-1; x++){
			if(!temp.get(x).getName().isSpecial()){
				sons.add(temp.get(x));
			}
		}
		for(int x = 0; x < sons.size(); x++){
			names.add(sons.get(x).getName().getName());
		}

		if (houN.size() > sons.size()){

			for(String x: houN){
				if(!names.contains(x)){
					pot.add(x);
				}
			}
			spareName = Basic.choiceW(pot);

			return true;
		}
		return false;
	}

	public boolean isMNameUsed(String name){
		return this.maleNames.contains(name);
	}

	public boolean isFNameUsed(String name){
		return this.femaleNames.contains(name);
	}
//Find members

	public boolean hasAdultKinsman(){
		for(Human x: this.kinsmen){
			if (x.isAdult()){ return true; }
		}
		return false;
	}

	//anyone but this
	public boolean hasAdultKinsman(Human person){
		for(Human x: this.kinsmen){
			if (x.isAdult() && x != person){ return true; }
		}
		return false;
	}

	public Human getAdultKinsman(){
		for(Human x: this.kinsmen){
			if (x.isAdult()){ return x; }
		}
		return this.kinsmen.get(0);
	}


	//anyone but this
	public Human getAdultKinsman(Human person){
		for(Human x: this.kinsmen){
			if (x.isAdult() && person != x){ return x; }
		}
		return this.kinsmen.get(0);
	}


	//anyone but this
	public boolean hasAdultKinswoman(Human person){
		for(Human x: this.kinswomen){
			if (x.isAdult() && x != person){ return true; }
		}
		return false;
	}

	public boolean hasAdultKinswoman(){
		for(Human x: this.kinswomen){
			if (x.isAdult()){ return true; }
		}
		return false;
	}

	//anyone but this
	public Human getAdultKinswoman(Human person){
		for(Human x: this.kinswomen){
			if (x.isAdult() && person != x){ return x; }
		}
		return this.kinswomen.get(0);
	}

	public Human getAdultKinswoman(){
		for(Human x: this.kinswomen){
			if (x.isAdult()){ return x; }
		}
		return this.kinswomen.get(0);
	}

	public static List<Human> getMagnates(){
		List<Human> magnates = new ArrayList<>();
		List<House> nobles = getNobles();
		for (House x: nobles){
			magnates.add(x.getHead());
		}
		/*
		for (House x: list){
			if (x.getRanking() >= 6){
				magnates.add(x.head);
			}
		}

		if (magnates.size() == 0){
			for (House x: list){
				if (x.getRanking() >= 5){
					magnates.add(x.head);
				}
			}
		}
		if (magnates.size() == 0){
			System.out.println("No living men.");
			System.exit(0);
		}*/
		return magnates;
	}

	public static void addToHouse(Human joiner, House house){
		joiner.setHouse(house);
		if(joiner.isMale()){
			house.addKinsman(joiner);
		} else {
			house.addKinswoman(joiner);
		}
	}

//House prince methods

	public void addPrince(Human h){
		this.princes.add(h);
		((Man) h).makeHousePrince();
	}

	/*House prince is removed from the house, if there are no further princes, andthe house has head, began house split*/
	public void removePrince(Human h){
		this.princes.remove(h);
		if (!this.hasPrinces() && !this.hasPatriarchHead()){
			if (this.isActive()){
				//if (Consanguinity.getPaternalRelation(h, this.getPatriarchHead()));
				/*Human p = this.getPatriarch();
				Human he = this.getHead();*/
				this.branch();
			}
		}
	}

	//Naturally when the Patriarch changes so do the princes
	public void setPatriarch(Human p){
		this.patriarch = p;
		if (p.isAdult()){
			this.makePrinces();
		}
	}

	public void makePrinces(){
		List<Human> l = this.getPatriarch().getLivingSons();
		for(Human x: l ){
			this.addPrince(x);
		}
	}

	public static int fetchName(){
		int n = 0;
		try{
			n = Basic.randint(highbornNamesN.size());
		} catch (ArrayIndexOutOfBoundsException e){
			System.out.println(highbornNamesN.size());
			System.exit(0);
		}
		int s = highbornNamesN.get(n);
		highbornNamesN.remove(highbornNamesN.get(n));
		return s;
	}

	public void returnToCirculation(){
		highbornNamesN.add(this.nameNum);
	}

	//each noble house name must be unique, each number corresponds to a house name, so there is a list integers being used
	public static void numberNobleHouses(){
		for(int x = 0; x < highbornNames.length; x++){
			highbornNamesN.add(x);
		}
	}

	public boolean hadPosthumousHead(){
		List<Human> l = this.getHeads();
		for(Human x: l){
			if (x.isPosthumous()){
				return true;
			}
		}
		return false;
	}


	public static void ennobleFirst(int i){
		for(int x = 0; x < i; x++){
			list.get(x).ennoble();
		}
	}

	public void ennoble(){
		this.nameHouseHighborn();
		this.isNoble = true;
		Basic.print("The race of "+this.getHead().getFullName()+" became known as the House of "+this.getName());
		this.addToNobles();
	}

	public void addToNobles(){
		peasants.remove(this);
		nobles.add(this);
	}

	public boolean hasPlentyOfNobles(){
		return getNumOfNobles() >= 10;
	}

	public boolean isNoble(){						return this.isNoble;		}
	public static List<House> getNobles(){			return new ArrayList<>(nobles);		}
	public int getNumOfNobles(){					return nobles.size();				}
	private void removeFromNobles(){				nobles.remove(this);				}

	private static void raiseNewNoble(){
		House h = getRandomPeasant();
		h.ennoble();
	}

	public void addToPeasants(){					peasants.add(this);					}
	private void removeFromPeasants(){				peasants.remove(this);				}



	public static House getRandomPeasant(){
		House h = Basic.choice(getPeasants());
		if (!h.isActive()){
			throw new RuntimeException();
		}
		return h;
	}

	public static List<House> getPeasants(){		return new ArrayList<>(peasants);	}

	public boolean hasPatriarchHead(){				return this.patriarch == this.head; }
	public Human getPatriarch(){					return this.patriarch;				}
	public boolean hasPrinces(){					return this.princes.size() != 0; 	}

//Activation	-	if the house is alive or dead

	public void activate(){
		this.isActive = true;
		list.add(this);
	}

	public void deactivate(){
		this.isActive = false;
		list.remove(this);

		if (this.isNoble()){
			this.removeFromNobles();
			//There must always be nobles
			if (!hasPlentyOfNobles()){
				raiseNewNoble();
			}
		} else {
			this.removeFromPeasants();
		}

	}

	public boolean isActive(){						return this.isActive;		}


//Micro methods

	public boolean hasHead(){						return this.head != null;	}
	public House getParent(){						return this.parent;			}
	public Human getHead(){							return this.head;			}
	public int getGeneration(){						return this.generation;		}
	public int getPrestige(){						return this.prestige;		}
	public int getRanking(){						return this.ranking;		}
	public int getCoA(){							return this.coa; 			}
	public String getCoALink(){			return "<img src='../Input/CoAs/"+this.getCoA()+".svg'</img>";}
	public List<Human> getKinsmen(){				return this.kinsmen;		}
	public List<Human> getKinswomen(){				return this.kinsmen;		}
	public List<String> getFemaleNames(){			return this.femaleNames;	}
	public List<String> getMaleNames(){				return this.maleNames;		}
	public static House getHouse(int id){			return Basic.house.get(id);	}
	public static int getId(){						return id;					}
	public static List<House> getList(){			return list;				}
	public static String getSpareName(){			return spareName;			}
	public String getFullName(){					return null;				}
	public String getName(){						return name;				}
	public void addKinsman(Human h){				this.kinsmen.add(h);		}
	public void addKinswoman(Human h){				this.kinswomen.add(h);		}
	public void addMaleName(String n){				this.maleNames.add(n);		}
	public void addFemaleName(String n){			this.femaleNames.add(n);	}
	public void addPrestige(int v){					this.prestige += v;			}
	public void gainPrestige(){						this.prestige++;			}
	public void removeKinsman(Human h){				this.kinsmen.remove(h);		}
	public void removeKinswoman(Human h){			this.kinswomen.remove(h);	}
	public void setRanking(int r){					this.ranking = r;			}
	public List<Human> getHeads(){					return new ArrayList<>(this.heads);		}
	public boolean isLegimate(){					return this.legimate;		}



	private static List<Integer> highbornNamesN = 		new ArrayList<>();
	private static String[] highbornNames = 			new String[1111];
	private static String[] lowbornNames = 				new String[145];
	public static List<House> nobles = 					new ArrayList<>(20);
	public static List<House> peasants = 				new ArrayList<>(100);

}
