package Code.Human;
import Code.Common.Name;
import Code.House.*;
import Code.Common.Basic;
import Code.Relationship.*;
import Code.Politics.Title;
import java.util.List;
import java.util.ArrayList;
import java.util.Calendar;

public class Man extends Human {
	public static List<Human> singles = 		new ArrayList<>();
	protected static List<Human> children = 	new ArrayList<>();
	protected static List<Human> men = 			new ArrayList<>();	//inclues all male
	protected static List<Human> elders = 		new ArrayList<>();	//single old people
	private final static String[] relation = 	{"son", "grandson", "great-grandson", "great-great-grandson"};
	private int cadetStatus;					//0 = head, 1 = senior member, 2 = other

//generated
    public Man(int age){
		super(age);
		setUp();
		this.name = 		new Name(true, this);
		new MainHouse(this);
		this.name.setFull(this.makeName());
		this.cadetStatus = 0;
		this.becomeSingle();

	}

//children of generated
	public Man(int y, boolean b){
		super(y, b);
		setUp();
	}

//naturally born
	public Man(){
		super();
		setUp();
	}

	public void setUp(){
		this.sex = false;
		this.cadetStatus = 2;
		this.religion.getParish().add(this);
		men.add(this);
	}

	@Override
	public String getShortName(){
		if (this.getName().hasRegnal()){
			return this.getFullName();
		}
		return this.getName().getName();
	}

	@Override
	public String getFormalName(){
		String n = this.getName().getName();
		if (this.getName().hasRegnal()){
			return this.getFullName();
		} else if (this.title == null){
			return n+" "+this.getHouse().getName();
		}
		if (this.hasTitle(Title.PRINCE)){
			return n+", "+Basic.getOrdial(this.getCadency())+" "+this.getTitleS();
		} else {
			return n+", "+this.getTitleS()+" "+this.getHouse().getName();
		}
	}

	@Override
    public String makeName(){
		String name = this.getName().getName();
		String hn =	  this.getHouse().getName();
		if (this.getName().getNick() != null){
			name +=" "+this.getName().getNick();
		}
		if (this.title == null){
			if (this.isLegimate()){
				return name+" "+hn;
			} else{
				return name;
			}
		} else{
			if (this.hasTitle(Title.PRINCE)){
				return name+", "+Basic.getOrdial(this.getCadency())+" "+this.getTitleS();
			} else {
				return name+", "+this.getTitleS()+" "+hn;
			}
		}
	}


    @Override
    public void saunter(int x){
        switch(x){
			case 1:
				if (this.isAlive()){ Marriage.prepare(this); }
                break;
            case 0:
                this.kill();
                break;
        }
    }

	@Override
	public void clean(){
		if (this.isAdult() &&  this.relSta <= 1) {
			this.becomeTaken();
			if (this.isRelSta(3)){
				this.removeFromElders();
			}
		}
		else {
			Man.children.remove(this);
		}
	}

	public static Human beBornSpecial(Human f, Human m, int y){
		Human it = new Man(y, false);
		Man.children.add(it);
		it.setParents(f, m);
		it.cadency = f.getNumOfLivingSons();
		it.setHouse(f.house);
		it.addToHouse();
		Name.aSon(f, m, it);
		it.addSon(f, m);
		if (!f.hadFather()){
			f.getHouse().addPrince(it);
		}
		if (!f.getHouse().getKinsmen().contains(it)){
			throw new RuntimeException();
		}
		return it;
	}

	public static Human beBorn(Human f, Human m){
		Human it = new Man();
		Man.children.add(it);
		return it;
	}

	//Part of regulart birth chain, not to be confused posthumous birth that includes them
	public void handlePostBirth(Human f, Human m){
		this.setParents(f, m);
		if (!this.isPosthumous()){
			this.setHouse(f.house);
			this.addToHouse();
			this.nameChild();
			this.addSon(f, m);
			this.cadency = f.getNumOfLivingSons();

			if (((Man) f).isPatriarch()){
				f.getHouse().addPrince(this);
			}

		} else{								//Posthumous birth
			this.cadency = 0;
			((Man) this).setCadetStatus(0);
			this.name = 		new Name(true, this);
			new MainHouse(this);
			this.getHouse().setOrigin(3);			//Set posthumous origin
			this.addSon(f, m);
			if (this.getHouse() == f.getHouse()){
				throw new RuntimeException();
			}
			//Ennoble the house if its a noble origin
			if (f.getHouse().isNoble()){
				this.getHouse().ennoble(3);
			}

			if (this.getHouse().getHead() != this && !this.getHouse().getKinsmen().contains(this)){
				throw new RuntimeException();
			}
			this.performPosthumousBirth(f, m);
		}
	}

	public void handleBastardBirth(Human f, Human m){
		this.setParents(f, m);
		this.cadency = 0;
		((Man) this).setCadetStatus(0);
		this.setName(new Name(true, this));
		new MainHouse(this, f.getName().getName());
		this.getHouse().setOrigin(2);							//Set bastardy
		((Man) this).name.setFull(((Man) this).makeName());
		this.addSon(f, m);
	}

	public void nameChild(){;
		Name.aSon(this.getFather(), this.getMother(), this);
	}

	@Override
	public void becomeSingle(){
		singles.add(this);
		this.setSpouseNull();
		this.mating = Mating.revaluateM(this);
	}

	@Override
	public void becomeWidow(){
		this.setSpouseNull();
		if (this.isMarriageable()){
			this.handleWidowhood();

		} else{
			this.setRelSta(3);
		}
	}


	@Override
	public void handleWidowhood(){
		this.becomeSingle();
		this.setRelSta(1);
		Human w = this.getLatestWife();
		if(w.hasUnwedSameSexSibling()){
			Marriage.doSororate(this, w);
		}
		//if(this.isSonless()){ Marriage.prepare(this); }
	}

	@Override
	public boolean hasUnwedSameSexSibling(){ return this.hasUnwedBrother(); }

	@Override
	public void bury(){
		List<Human> h;
		this.house.removeKinsman(this);
		this.religion.getParish().remove(this);
		men.remove(this);
		if (this == this.getHouse().getHead()){
			this.house.succession(this);
		}

		if (this.hadFather()){
			if (this.isLegimate()){
				this.getFather().removeLivingSon();
				if(this.hasBrother()){
					h = this.getFather().getLivingSons();
					for (int x = 0; x < h.size(); x++){
						h.get(x).cadency = x;
					}
				}
			}
			if (this.isHousePrince()){
				this.getHouse().removePrince(this);
			}

			this.getMother().removeLivingSon();
			if (this.getHouse().isActive() && !this.getHouse().patriarchIsSuited()){
				throw new RuntimeException();
			}
		}
		this.handleRoleDeath();
	}



	//Does character have living male descendants?
	public boolean hasAgnaticLine(){
		List<Human> l;
		if (this.isAdult()){
			if (this.hasLegitNonPosthumousSon()){
				return true;
			}
			else{
				l = this.getLegitNonPosthumousSons();
				for (Human x: l ){
					if (((Man) x).hasAgnaticLine()){
						return true;
					}

				}
			}
		}
		return false;
	}


	//Get their who is of legimate and not of posthumous descendent
	public Human getAgnaticHeir(){
		Human heir;
		List<Human> l = this.getLegitNonPosthumousSons();
		for(Human x: l ){
			if(x.isAlive()){
				return x;
			}
			else if (x.isAdult()){
				if (((Man) x).hasAgnaticLine()){
					return ((Man) x).getAgnaticHeir();
				}
			}
		}
		throw new RuntimeException();
	}

	public static void growUp(){
		ArrayList<Human> rem = new ArrayList<>();
		for (Human x: Man.children){
			if (x.getAge() == 14){
				rem.add(x);
			}
		}
		for (Human x: rem){
			x.reachAdulthood();
		}
	}

	@Override
	public void reachAdulthood(){
		Man.children.remove(this);
		this.fertility = 			Basic.randint(101);
		this.personality =			new Personality();
		this.rela.reachAdulthood();
		this.becomeSingle();
		if (this.cadency == 0){
			Marriage.prepare(this);
		}
//			if (x.title == Title.KING){ Regent.lift(); }
	}


	//Determines if man should marry, not if he can
	@Override
	public boolean isMarriageable(){
		if (this.getAge() > 65 || this.hasNumOfLivingSons(3)){
			return false;
		}
		return true;
	}

	public void clearLovers(){
		List<Affair> l = this.getAffairs();
		for(Affair x: l){
			x.getBelle().endAffair(x);
			x.remove();
		}
		this.clearAffairs();
	}

	public boolean isMinor(){			return this.getAge() < 14; }

	@Override
    public String widow(){ 				return "widower";}

    @Override
    public String getParent(){ 			return "father";}

    @Override
    public String getPibling(){ 		return "uncle";}

    @Override
    public String getNibling(){ 		return "nephew";}

    @Override
    public void becomeTaken(){			singles.remove(this);}

	public static List<Human> getSingles(){ return singles;}

    @Override
    public String child(){				return "boy";}

    @Override
    public String getSibling(){			return "brother";}

	@Override
    public String getLoverGroup(){		return "mistresses";}

	@Override
	public String getRelation(int v){	return relation[v];		}

	@Override
	public Human getLoverfromAffair(Affair h){	return h.getDoe();	}


	//I.e 2 (of 4 sons)
	@Override
	public int getSexChildOrder(){
		List<Human> l = this.getFather().getSons();
		return l.indexOf(this);
	}

	//I.e second son or only son
	@Override
	public String getSexChildOrderName(){

		List<Human> l = this.getFather().getLegitSons();
		String s = "";
		int i = l.indexOf(this);								//Index in the order

		//If there is only one son, just return only son, otherwise [ordinal] son or oldest/youngest
		if (l.size() == 1){
			s = "only";
		} else {
			if (i == 0){
				s = "oldest";
			} else if (i == l.size()-1){
				s = "youngest";
			} else {
				s = Basic.getOrder(i);
			}
		}

		return s+" son";
	}

	@Override
	public Title getRoyalTitle(){		return Title.KING;}

	public void princify(){
		this.title = title.PRINCE;
		this.rename(title.PRINCE);
	}

	public boolean isPrinceling(){
		return this.title == Title.PRINCE;
	}

	public void makeHousePrince(){							this.cadetStatus = 1;			}
	public int getCadetStatus(){							return this.cadetStatus;		}
	public boolean isHousePrince(){
		return this.getHouse().getPrinces().contains(this);
		//return this.cadetStatus == 1;
	}

	public void addToElders(){								elders.add(this); 	  			}
	public void removeFromElders(){							elders.remove(this); 			}
	public void setCadetStatus(int i){						this.cadetStatus = i;			}
	public boolean isPatriarch(){							return this.cadetStatus == 0;	}
	public static List<Human> getMen(){						return new ArrayList<>(men);	}
	public static int getAmount(){							return men.size(); 				}
	public static int getSingleNum(){						return singles.size();  		}


//Statistics

	public static int getPerOfSingles(){	return calcPerSingles(getMen());	}

	public static int getPerOfBachelors(){	return calcPerBachelors(getMen());	}

	public static int getPerOfWidowed(){	return calcPerWidowed(getMen());  	}

	public static int getPerOfParents(){	return calcPerParents(getMen());  	}

	public static int getPerOfChildren(){
		float i = (Human.getNumOfChildren(getMen())+0.0f)/getAmount();
		return (int) (i*100);
	}

	public static int getPerOfElderly(){
		float i = (Human.getNumOfElderly(getMen())+0.0f)/getAmount();
		return (int) (i*100);
	}


}
