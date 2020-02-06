package Code.House;
import Code.Human.*;
import Code.Common.*;
import Code.Politics.Title;
import Code.Relationship.*;
import Code.Ancestry.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Calendar;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;

public class House {
	private static List<House> list = new ArrayList<>();
	public static House nextHouse;					//Used for temp. tracing
    private static int id = 0;						//Number of created houses
	protected static String spareName;				//Used for temp

	protected String name;							//Main name of the house
	protected boolean legimate;						//false = bastard house
	protected boolean isActive;						//Is house alive
	protected Calendar founding;					//When the house was founded
	protected House parent;							//From were the house orginated from
	protected Human founder;						//Who founded the house
	protected Human head;							//Who is leader of the house de jure primogeniture
	protected Human patriarch;						//Former head whose memory keeps the family together
	protected int generation;						//How ancestors the house has
	protected int nameNum;							//Number for noble name because has to be unique
	protected int coa;								//Coat of arms
	protected int prestige;							//How
	protected int ranking;
	protected boolean isNoble;						//true = noble/false = lowborn
	protected List<House> branches;					//Cadet branches
	protected List<Human> heads;					//List of the heads
	protected List<Human> kinsmen;					//List of male members
	protected List<Human> kinswomen;				//List of female members
	protected List<Human> princes;				//Princes of the house, i.e living sons of the patriarch
	protected List<String> femaleNames;				//Names used for women of the family
	protected List<String> maleNames;				//Names used for men of the family
	protected byte origin;

	/*Origin with following inputs:
		0 = mystic (unknown, should not be used)
		1 = ancient, houses that predate the realm
		2 = bastardy, originates as off shoot fo another house
		3 = posthumous, orginates as of shoot of posthumous house
		4 = morgnatic, become nobilitya via marriagge
		5 = humble, raised from privacy to fill in the ranks
	*/


	protected int naming;							//Naming pattern, with following inputs:
	/*	0		orderly
	 	1		weighted random
		2		equal random
		3		grandfatherly method
		4		respectful (late)*/

    public House(Human head){
		((House) this).handles(head);
		this.legimate = true;
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
		head.setHouse(this);					//House of the house founders need to be assigned here
		this.founding = 						(Calendar) Basic.date.clone();
		this.heads = 							new ArrayList<>();
		this.addHead(head);
		this.nameHouseLowborn();
		this.kinsmen = 							new ArrayList<>();
		this.kinswomen = 						new ArrayList<>();
		this.branches = 						new ArrayList<>();
		this.princes =							new ArrayList<>();
		this.naming	=							Basic.randint(6);
		this.isNoble = false;
		Basic.house.put(House.id, this);
		this.addToPeasants();
	}


	//Create names for the highborn and the lowborn
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
			throw new RuntimeException();
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
			throw new RuntimeException();
		}
	}

	//Get the heir from the agnatic line, should not be used unless there is certainty of heirs existence
	public Human getHeir(){
		for(int x = this.heads.size()-1; x >= 0; x--){
			if (((Man) this.heads.get(x)).hasAgnaticLine()){
				return ((Man) this.heads.get(x)).getAgnaticHeir();
			}
		}
		throw new RuntimeException();
	}

	//Used to see if person is descended from house head (every house member should)
	public boolean descendsFromHead(Human h){
		List<Human> l = getHeads();
		Human t = h;					//Temporary
		for (Human x: l){
			if (l.contains(t) ){
				return true;
			}
			t = t.getFather();
		}
		return false;
	}

	public void succession(Human h){
		/*patriarchy can only be inherited:
			1.	from father to son
			2.	from brother to brother
			3.	from nephew to uncle
			4. 	from uncle to nephew
			5. from cousin to cousin while uncle is alive
		*/

		if (this.hasFittingHeir()){
		} else {
			this.deactivate();
			if (this.isNoble() && !this.isLegimate()){
				this.returnToCirculation();
			}
			this.handleSuccession();
		}
	}

//Check if the late family head has a successor in mind and pass the torch if there is one
	private boolean hasFittingHeir(){
		Human h;
		if (Basic.isNotZero(this.getKinsmenCount())){
			h = this.getHeir();
			if (!h.hadFather()){
				throw new RuntimeException();
			}
			if (!h.isAlive()){
				throw new RuntimeException();
			}
			if (h.getHouse() == this){
				if (h != this.getHead()){
					this.passTheTorchTo(h);
					return true;
				}
			}
		}
		return false;
	}

	private void passTheTorchTo(Human h){
		this.addHead(h);
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
	}

	//The main house died, does it have a cadet branch that could succeed it or it has it gone extinct?
	public void handleSuccession(){

		if (this.findNextHouse()){
			this.succeed(nextHouse);					//nexthouse is static
		} else{
			Basic.print(this.getName()+" went extinct");
		}

	}

	public void succeed(House newHouse){
		Basic.print("The senior line of "+this.getFullName()+" went extinct, but was succeeded by "+nextHouse.getName());
		if (this.hasHigherRanking(newHouse)){
			newHouse.setRanking(this.ranking);
		}

		if (this.isNoble() && !newHouse.isNoble()){
			newHouse.inheritNobleStatus(this);
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
			}
		} catch (NullPointerException e){
			List<Human> l = this.getHeads();
		/*	if (l.get(0) == l.get(1)){
				throw new RuntimeException();
			}*/
			System.out.println("NAME\tIS POSTHUMOUS\tIS FOUNDER\tTENURE");
			for(Human x: l){
				System.out.print(x.getBirthName());
				System.out.print("\t"+x.isPosthumous() );
				System.out.print("\t"+x.getHouse().isFounder(x));
				System.out.println("\t"+x.getLifespan());
			}
			throw new RuntimeException();
		}

		return this.getPatriarch().getLegitNonPosthumousSons().indexOf(h);
	}

	//Cadet branch is formed, if cadet has living sons, his father is dead)
	public void branch(){
		int num = 			this.getSeniorBranchNum();
		List<Human> sons = 	this.getPatriarch().getLegitNonPosthumousSons();
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

		//See if patriarch is in the right postion, if not, do branching again, a progress which will find and appoint a new patriarch
		if (!this.patriarchIsSuited()){
			branch();
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
				throw new RuntimeException();
		}
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
				throw new RuntimeException();
		}
	}

	public String pickRandomNameM(){
		String newN;
		do {
			newN = Name.getRandomMaleName();
		} while(this.isMNameUsed(newN));
		this.addMaleName(newN);
		return newN;
	}

	public String pickRandomNameF(){
		String newN;
		do {
			newN = Name.getRandomFemaleName();
		} while(this.isFNameUsed(newN));
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
		List<String> u = this.getUsableNamesM(h, this.getMaleNames());
		if (Basic.isNotZero(u.size())){
			return Basic.choice(u);
		}
		return this.pickRandomNameM();
	}

	public String doFlatNamingF(Human h){
		String n;
		List<String> u = this.getUsableNamesF(h, this.getFemaleNames());
		if (Basic.isNotZero(u.size())){
			return Basic.choice(u);
		}
		return this.pickRandomNameF();
	}

	public String doWeightNamingM(Human h){
		String n;
		List<String> u = this.getUsableNamesM(h, this.getMaleNames());
		if (Basic.isNotZero(u.size())){
			return Basic.choiceW(u);
		}
		return this.pickRandomNameM();
	}

	public String doWeightNamingF(Human h){
		String n;
		List<String> u = this.getUsableNamesF(h, this.getFemaleNames());
		if (Basic.isNotZero((u.size()))){
			return Basic.choiceW(u);
		}
		return this.pickRandomNameF();
	}

	public String doOrderlyNamingM(Human h){
		String n;
		List<String> u = this.getUsableNamesM(h, this.getMaleNames());
		if (Basic.isNotZero(u.size())){
			return u.get(0);
		}
		return this.pickRandomNameM();
	}

	public String doOrderlyNamingF(Human h){
		String n;
		List<String> u = this.getUsableNamesF(h, this.getFemaleNames());
		if (Basic.isNotZero(u.size())){
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
		u = this.getUsableNamesM(h, this.getMaleNames());


		if (Basic.isNotZero(u.size())){
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
				return n;
			}
		}
		u = this.getUsableNamesF(h, this.getFemaleNames());
		if (Basic.isNotZero(u.size())){
			return Basic.choiceW(u);
		}
		return this.pickRandomNameF();
	}

	public String doAncestralNamingM(Human h){

		List<String> u = this.getUsableNamesM(h, h.getMaleAncestryGroupNames());

		if (Basic.isNotZero(u.size())){
			return Basic.choiceW(u);
		}
		return this.pickRandomNameM();
	}

	public String doAncestralNamingF(Human h){

		List<String> u = this.getUsableNamesF(h, h.getFemaleAncestryGroupNames());

		if (Basic.isNotZero(u.size())){
			return Basic.choiceW(u);
		}
		return this.pickRandomNameF();
	}

//Filter out used names
	public List<String> getUsableNamesM(Human h, List<String> l){

		List<String> ln =		h.getFather().getLivingSonsNames();
		List<String> pot = 		new ArrayList<>(l.size());

		for(String x: l){
			if(!ln.contains(x)){
				pot.add(x);
			}
		}

		return pot;
	}

//Filter out used names
	public List<String> getUsableNamesF(Human h, List<String> l){

		List<String> ln =		h.getFather().getLivingDaughtersNames();
		List<String> pot = 		new ArrayList<>(l.size());

		for(String x: l){
			if(!ln.contains(x)){
				pot.add(x);
			}
		}

		return pot;
	}

	public boolean isMNameUsed(String name){
		return this.maleNames.contains(name);
	}

	public boolean isFNameUsed(String name){
		return this.femaleNames.contains(name);
	}


//Find members method


	public boolean hasAdultKinsman(){
		for(Human x: this.kinsmen){
			if (x.isAdult()){ return true; }
		}
		return false;
	}

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

	public Human getAdultKinsman(Human person){
		for(Human x: this.kinsmen){
			if (x.isAdult() && person != x){ return x; }
		}
		return this.kinsmen.get(0);
	}


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
		return magnates;
	}


	public static void addToHouse(Human joiner, House house){
		joiner.setHouse(house);
		if(joiner.isMale()){
			house.addKinsman(joiner);
			if (joiner.isPosthumous()){
				throw new RuntimeException();
			}

		} else {
			house.addKinswoman(joiner);
		}
	}

//House prince methods

	public void addPrince(Human h){
		if (!h.isAlive()){
			throw new RuntimeException();
		}
		if (((Man) h).isHousePrince()){
			throw new RuntimeException();
		}
		if (this.princes.contains(h)){
			throw new RuntimeException();
		}
		this.princes.add(h);
		((Man) h).makeHousePrince();
		if (!h.isSonOf(this.getPatriarch())){
			throw new RuntimeException();
		}
	}

	/*House prince is removed from the house, if there are no further princes, and the house has head, began house split*/
	public void removePrince(Human h){
		this.princes.remove(h);
		if (this.princes.contains(h)){
			throw new RuntimeException();
		}
		if (!this.hasPrinces() && !this.hasPatriarchHead()){
			if (this.isActive()){
				this.branch();
			}
		}
	}

	public boolean hasLivingPrince(){
		List<Human> l = this.getPrinces();
		return Human.hasLiving(l);
	}

	public List<Human> getPrinces(){
		return new ArrayList<>(this.princes);
	}

	//Naturally when the Patriarch changes so do the princes
	public void setPatriarch(Human p){
		if (p == this.getPatriarch()){
			throw new RuntimeException();
		}
		this.patriarch = p;

		if (p.isAdult()){
			this.makePrinces();
		}

	}

	public void makePrinces(){
		List<Human> l = this.getPatriarch().getLegitNonPosthumousLivingSons();
		for(Human x: l ){
			this.addPrince(x);
		}
	}

	public void resetPrinces(){
		this.princes.clear();
	}

	public static int fetchName(){
		int n = 0;
		try{
			n = Basic.randint(highbornNamesN.size());
		} catch (ArrayIndexOutOfBoundsException e){
			throw new RuntimeException();
		}
		int s = highbornNamesN.get(n);
		highbornNamesN.remove(highbornNamesN.get(n));
		return s;
	}

	public void returnToCirculation(){
		highbornNamesN.add(this.nameNum);
	}

	//Each noble house name must be unique, each number corresponds to a house name, so there is a list integers being used
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
			list.get(x).ennoble(1);					//Argument for origin being 1 for ancient
		}
	}

	//When a noble line goes extinct but their next of kin branch is lowborn they will inherit their status
	public void inheritNobleStatus(House oldHouse){
		this.isNoble = true;
		this.coa = oldHouse.getCoA();
		this.nameNum = oldHouse.getNameNum();
		this.name = oldHouse.getName();
		this.origin = oldHouse.getOrigin();
		this.addToNobles();
		Human.updateNamesOf(this.getMembers());
	}

	//A new family is promoted from privacy to nobility
	//O is the origin
	public void ennoble(int o){
		this.nameHouseHighborn();
		this.isNoble = true;
		this.coa = 1+Basic.randint(100);
		this.setOrigin(o);

		Basic.print("The race of "+this.getHead().getFullName()+" became known as the House of "+this.getName());
		this.addToNobles();
		Human.updateNamesOf(this.getMembers());			//Updates the name of new nobility
		if (!this.getHead().isAlive()){
			throw new RuntimeException();
		}
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

	public static int getNumOfNobles(){				return nobles.size();				}

	private void removeFromNobles(){				nobles.remove(this);				}

	private static void raiseNewNoble(){
		House h = getRandomPeasant();
		h.ennoble(5);
	}

	//Count living noblemen
	public static int getNoblemenCount(){
		int c = 0;							//(c)ount
		for(House x: getNobles()){
			c += x.getKinsmenCount();
		}
		return c;
	}

	//Count living noblewomen
	public static int getNoblewomenCount(){
		int c = 0;							//(c)ount
		for(House x: getNobles()){
			c += x.getKinswomenCount();
		}
		return c;
	}

	//Used every time a branch dies
	public void removeFromCaste(){
		if (this.isNoble()){
			this.removeFromNobles();
			if (this.getNobles().contains(this)){
				throw new RuntimeException();
			}
			//There must always be nobles
			if (!hasPlentyOfNobles()){
				raiseNewNoble();
			}
		} else {
			this.removeFromPeasants();		//Can only belong to one of the group at given time
		}
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
		this.isActive = false;				//Mark as non-active
		list.remove(this);					//Remove from active house list
		this.removeFromCaste();				//Either remove from noble or peasant families
		nextHouse = null;					//Reset the static variable
	}

	public boolean isActive(){				return this.isActive;		}



//Origin

	public void setOrigin(int i){
		this.origin = (byte) i;
	}

	public byte getOrigin(){
		return this.origin;
	}

	public String getOriginString(){
		return originS[this.getOrigin()];
	}

	private final String[] originS = {"Mystical", "Ancient", "Bastardy", "Posthumous", "Morganatic", "Humble"};


//Memberships

		public List<Human> getMembers(){
			List<Human> l = this.getKinsmen();
			l.addAll(this.getKinswomen());
			return l;
		}

//Kinsmen


	public void addKinsman(Human h){
		if (this.kinsmen.contains(h)){
			throw new RuntimeException();
		}

		this.kinsmen.add(h);
		if (h.isPosthumous() && !h.isHouseHead() ){
			throw new RuntimeException();
		}
		if (!h.isLegimate() && !h.isHouseHead() ){
			throw new RuntimeException();
		}
	}

	public void removeKinsman(Human h){
		this.kinsmen.remove(h);
		if (this.kinsmen.contains(h)){
			throw new RuntimeException();
		}
	}

	public List<Human> getKinsmen(){
		return new ArrayList<>(this.kinsmen);
	}


	public int getKinsmenCount(){
		return this.kinsmen.size();
	}


//Kinswomen

	public void addKinswoman(Human h){				this.kinswomen.add(h);		}

	public void removeKinswoman(Human h){			this.kinswomen.remove(h);	}

	public List<Human> getKinswomen(){
		return new ArrayList<>(this.kinswomen);
	}

	public int getKinswomenCount(){
		return this.kinswomen.size();
	}

	//Patriarch should almost always be dead and have living sons, but with some exceptions he is alive
	public boolean patriarchIsSuited(){
		Human p = this.getPatriarch();
		if (p.isAlive() || (p.isAdult() && p.hasLegitNonPosthumousSon()) ) {
			return true;
		} else{
			return false;
		}
	}

	public boolean workingMembers(Human p){
		List<Human> l = this.getKinsmen();
		for(Human x: l){
			if (!x.isPatDescendantOf(p)){
				return false;
			}
		}
		return true;
	}

	public Human getWorkingMember(Human p){
		List<Human> l = this.getMembers();
		for(Human x: l){
			if (!x.isPatDescendantOf(p)){
				return x;
			}
		}
		return null;
	}

//Micro methods

	public boolean hasHead(){						return this.head != null;	}
	public House getParent(){						return this.parent;			}
	public Human getHead(){							return this.head;			}
	public int getGeneration(){						return this.generation;		}
	public int getPrestige(){						return this.prestige;		}
	public int getRanking(){						return this.ranking;		}
	private int getCoA(){							return this.coa;			}
	public String getName(){						return name;				}
	public String getCoALink(){			return "<img src='../Input/CoAs/"+this.getCoA()+".svg'</img>";}
	public List<String> getFemaleNames(){			return this.femaleNames;	}
	public List<String> getMaleNames(){				return this.maleNames;		}
	public static House getHouse(int id){			return Basic.house.get(id);	}
	public static int getId(){						return id;					}
	public static List<House> getList(){			return list;				}
	public static String getSpareName(){			return spareName;			}
	public String getFullName(){					return this.getName();		}
	public String getParentName(){					return this.getParent().getName();		}
	public int getNameNum(){						return this.nameNum;		}

	public void addHead(Human h){
		this.heads.add(h);
		this.head = h;
		if (this != h.getHouse() && (h.getHouse().getParent() == h.getHouse())){
			throw new RuntimeException();
		}
	}



	public void addMaleName(String n){				this.maleNames.add(n);		}
	public void addFemaleName(String n){			this.femaleNames.add(n);	}
	public void addPrestige(int v){					this.prestige += v;			}
	public void gainPrestige(){						this.prestige++;			}
	public void setRanking(int r){					this.ranking = r;			}
	public List<Human> getHeads(){					return new ArrayList<>(this.heads);		}
	public boolean isLegimate(){					return this.legimate;		}
	public boolean isFounder(Human h){				return this.founder == h;	}
	public String getFounding(){		return  Basic.format1.format(this.founding.getTime());	 }


	private static List<Integer> highbornNamesN = 		new ArrayList<>();
	private static String[] highbornNames = 			new String[1111];
	private static String[] lowbornNames = 				new String[144];
	public static List<House> nobles = 					new ArrayList<>(20);
	public static List<House> peasants = 				new ArrayList<>(100);

}
