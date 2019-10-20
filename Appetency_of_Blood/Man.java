import java.util.List;
import java.util.ArrayList;
import java.util.Calendar;

public class Man extends Human {
	protected static List<Human> singles = 		new ArrayList<>();
	protected static List<Human> children = 	new ArrayList<>();
	protected static List<Human> men = 			new ArrayList<>();	//inclues all male
	protected static List<Human> elders = 		new ArrayList<>();	//single old people
	private final static String[] relation = 	{"son", "grandson", "great-grandson", "great-great-grandson"};
	private int cadetStatus;					//0 = head, 1 = senior member, 2 = other

//generated
    public Man(int age){
		super(age);
		this.name = 		new Name(true, this);
		this.sex = false;
		this.religion.getParish().add(this);
		this.house = new MainHouse(this);
		this.name.setFull(this.makeName());
		this.cadetStatus = 0;
		this.becomeSingle();
		men.add(this);
	}

//naturally born
	public Man(){
		super();
		this.sex = false;
		this.cadetStatus = 2;
		this.religion.getParish().add(this);
		men.add(this);
	}

	@Override
	public String getShortName(){
		if (this.getName().hasRegnal()){
			return this.getName().getFull();
		}
		return this.getName().getName();
	}

	@Override
	public String getFormalName(){
		String n = this.getName().getName();
		if (this.getName().hasRegnal()){
			return this.getName().getFull();
		} else if (this.title == null){
			return n;
		}
		if (this.hasTitle(Title.PRINCE)){
			return n+", "+Main.getOrderShort(this.getCadency())+" "+this.getTitleS();
		} else {
			return n+", "+this.getTitleS()+" of "+this.getHouse().getName();
		}
	}

	@Override
    public String makeName(){
		String name = this.getName().getName();
		if (this.getName().getNick() != null){
			name +=" "+this.getName().getNick(); }
		if (this.title == null){
			return this.getShortName()+" "+name;
		} else{
			if (this.hasTitle(Title.PRINCE)){
				return name+", "+Main.getOrderShort(this.getCadency())+" "+this.getTitleS();
			} else {
				return name+", "+this.getTitleS()+" of "+this.getHouse().getName();
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
			if (this.relSta == 3){
				this.removeFromElders();
			}
		}
		else {
			Man.children.remove(this);
		}
	}

	public static Human beBorn(Human f, Human m){
		Human it = new Man();
		Man.children.add(it);
		it.father = f;
		it.mother = m;
		f.sons.add(it);
		m.sons.add(it);
		if (f.isAlive() || it.hasBrother()){
			it.cadency = f.getLivingSons().size()-1;
			it.house = f.house;
			it.addToHouse();
			Name.aSon(f, m, it);
			it.getName().setOwner(it);
		} else{								//posthumous birth
			it.cadency = 0;
			((Man) it).setCadetStatus(0);
			it.name = 		new Name(true, it);
			it.house = 		new MainHouse(it);
			it.getName().setNick("Postum");
			it.performPosthumousBirth(f, m);
		}
		if (((Man) f).isPatriarch()){
			f.getHouse().addPrince(it);
		}
		return it;
	}

	@Override
	public void becomeSingle(){

		singles.add(this);
		this.mating = Mating.revaluateM(this);
	}

	@Override
	public void becomeWidow(){
		if (this.isMarriageable()){
			this.handleWidowhood();

		} else{
			this.relSta = 3;
		}
	}


	@Override
	public void handleWidowhood(){
		this.becomeSingle();
		this.relSta = 1;
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
			if(this.hasBrother()){
				h = this.father.getLivingSons();
				for (int x = 0; x < h.size(); x++){
					h.get(x).cadency = x;
				}
			}
			if (this.isHousePrince()){
				this.getHouse().removePrince(this);
			}
/*	 		else if(this.getHouse().isActive() && !this.hasSeniorPaternalRelative()){
				System.out.println("splitting after death of "+this);
				this.getHouse().branch(this);
			}*/
		}
	}

	//Does character have living male descendants?
	public boolean hasAgnaticLine(){
		if (this.isAdult()){
			if (this.hasSon()){
				return true;
			}
			else{
				for (Human x: this.getSons()){
					if (((Man) x).hasAgnaticLine()){
						return true;
					}

				}
			}
		}
		return false;
	}

	public Human getAgnaticHeir(){
		Human heir;
		for(Human x: this.getSons()){
			if(x.isAlive()){
				return x;
			}
			else if (x.isAdult()){
				heir = ((Man) x).getAgnaticHeir();
				if (heir != null){
					return heir;
				}
			}
		}
		return null;
	}

	public static void growUp(){
		ArrayList<Human> rem = new ArrayList<>();
		for (Human x: Man.children){
			if (x.getAge() == 14){
				rem.add(x);
			}
		}
		for (Human x: rem){
			Man.children.remove(x);
			x.fertility = 				Main.randint(101);
			x.marriages = 				new ArrayList<>();
        	x.sons = 					new ArrayList<>();
			x.daughters = 				new ArrayList<>();
			x.becomeSingle();
			if (x.cadency == 0){ 		Marriage.prepare(x);}
//			if (x.title == Title.KING){ Regent.lift(); }
		}
	}

	public String getPortrait(){
		int age = this.aged();
		if (age >= 14){
			if(age < 20){ return "<img src='img/MA/"+Main.choice(Main.MApic)+".jpg'</img>"; }
			else if (age < 45){ return "<img src='img/MY/"+Main.choice(Main.MYpic)+".jpg'</img>"; }
			else if (age < 70){ return "<img src='img/MM/"+Main.choice(Main.MMpic)+".jpg'</img>"; }
			else { return "<img src='img/ME/"+Main.choice(Main.MEpic)+".jpg'</img>"; }
		}
		return "";
	}

	//Determines if man should marry, not if he can
	@Override
	public boolean isMarriageable(){
		if (this.getAge() > 65 || this.getLivingSons().size() >= 3){
			return false;
		}
		return true;
	}

	@Override
	public boolean isChildOf(Human h){
		if (h.isAdult() && h.sons.contains(this)){
			return true;
		}
		return false;
	}

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
    @Override
    public String child(){				return "boy";}
    @Override
    public String getSibling(){			return "brother";}
	@Override
	public String getRelation(int v){	return relation[v];		}
	@Override
	public Title getRoyalTitle(){		return Title.KING;}

	public void princify(){
		this.title = title.PRINCE;
		this.rename(title.PRINCE);
	}

	public void makeHousePrince(){							this.cadetStatus = 1;			}
	public int getCadetStatus(){							return this.cadetStatus;		}
	public boolean isHousePrince(){							return this.cadetStatus == 1; 	}
	public static int getAmount(){							return men.size(); 				}
	public static int getSingleNum(){						return singles.size();  		}
	public void addToElders(){								elders.add(this); 	  			}
	public void removeFromElders(){							elders.remove(this); 			}
	public void setCadetStatus(int i){						this.cadetStatus = i;			}
	public boolean isPatriarch(){							return this.cadetStatus == 0;	}
}
