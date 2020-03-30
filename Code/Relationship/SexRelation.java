package Code.Relationship;
import Code.Human.Human;
import Code.Common.Basic;
import java.util.ArrayList;
import java.util.List;
import java.util.Calendar;

public class SexRelation{

	protected boolean active;
	protected float fertility;
	protected Human doe;
	protected Human stag;
	protected int anniversary;
	protected int sexSat;			//sexual satisfaction
	protected int romance;
//	protected int[] procreation;
	protected List<Human> offspring;
    protected static int id 								= 0;
	protected Calendar beginning;
	protected Calendar ending;

	final static double[][] fert = {{0.0, 0.0, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8},
	{0.9, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0},
	{1.0, 0.95, 0.9, 0.85, 0.8, 0.75, 0.7, 0.65, 0.6, 0.55},
	{0.5, 0.45, 0.4, 0.35, 0.3, 0.25, 0.2, 0.15, 0.1, 0.05},};		//female age modifier

	public SexRelation(Human m, Human w, int v){
		this.sexSat =	3+Basic.randint(v);
		this.romance =	3+Basic.randint(v);
		this.setup(m, w);
	}

	public SexRelation(Human m, Human w){
		this.sexSat =	1+Basic.randint(5);
		this.romance =	1+Basic.randint(5);
		if (m.isActiveAdulterer() && w.isActiveAdulterer()){
			List<Affair> l = 	m.getAffairs();
			for (Affair x: l){
				if (x.getBelle() == w){
					m.clearLovers();
				}
			}
		}
		this.setup(m, w);
		stag.setSpouse(w);
		doe.setSpouse(m);
	}

	public void setup(Human m, Human w){
		this.id++;
		this.stag = 		m;
		this.doe = 			w;
		this.beginning = 	(Calendar) Basic.date.clone();
		try{
			this.fertility = 	Math.min(m.getFert(), w.getFert());
		} catch(NullPointerException e){
			System.out.println("Husband fert: "+m.getFert());
			System.out.println(w);
			System.out.println("Wife age: "+w.getAge());
			System.out.println("Adult: "+w.isAdult());
			System.out.println("Wife fert: "+w.getFert());
			throw new RuntimeException();
		}
		this.offspring = 	new ArrayList<>();
		this.active = 		true;
	}


	public int calcProcreation(){
		int ha = 			this.getStag().getAge();
		int wa = 			this.getDoe().getAge();
		int fmy = 			70-ha;
		int ffy = 			50-wa;
		float v = 			(this.getFertility()/10f)*(1.25f);
		int tfy = Math.min(fmy, ffy);
		return (int) (v*(fert[(wa-12)/10][(wa-12)%10]));
	}

		public static void doAnnual(){
			List<Marriage> lm = Marriage.getList();
			for(Marriage x: lm){
				x.anniversary++;
				//if (x.considerDivorce()){ dList.add(x); }
			}
/*			for(Marriage x: dList){
				x.getDivorce();
			}*/
			List<Affair> la = Affair.getList();
			for(Affair x: la){
				x.anniversary++;
			}
//			dList.clear();
		}

		public String getHappinessDesc(){
			return happyDesc[(this.getHappiness()-1)/2]+" "+Basic.choice(unionDesc);
		}

		public boolean isChildless(){
			List<Human> l = this.getOffspring();
			for(Human x: l){
				if (x.isAlive()){
					return true;
				}
			}
			return false;
		}

		public int getNumOfLivingOffspring(){
			List<Human> l = this.getOffspring();
			int i = 0;
			for(Human x: l){
				if (x.isAlive()){
					i++;
				}
			}
			return i;
		}

//Offspring

	public int getNumOfOffspring(){
		return this.offspring.size();
	}

	//If the union produced children
	public boolean getHadChildren(){
		return Basic.isNotZero(this.offspring.size());
	}

	public List<Human> getOffspring(){
		return new ArrayList<>(this.offspring);
	}

	public List<Human> getSons(){
		List<Human> l = new ArrayList<>();
		for(Human x: this.offspring){
			if (x.isMale()){
				l.add(x);
			}
		}
		return l;
	}

	public List<Human> getDaughters(){
		List<Human> l = new ArrayList<>();
		for(Human x: this.offspring){
			if (x.isFemale()){
				l.add(x);
			}
		}
		return l;
	}

	public int getNumOfSons(){
		int i = 0;
		for(Human x: this.offspring){
			if (x.isMale()){
				i++;
			}
		}
		return i;
	}

	public int getNumOfDaughters(){
		int i = 0;
		for(Human x: this.offspring){
			if (x.isFemale()){
				i++;
			}
		}
		return i;
	}

	public void addOffspring(Human o){
		this.offspring.add(o);
	}

	//Offspring that alive stag died
	public List<Human> getSurvivingOffspring(){
		Calendar c = this.getStag().getDeathPresent();
		return Human.getLivingIn(this.getOffspring(), c);

	}

	public List<Human> getSurvivingSons(){
		Calendar c = this.getStag().getDeathPresent();
		return Human.getLivingIn(this.getSons(), c);
	}

	public List<Human> getSurvivingDaughters(){
		Calendar c = this.getStag().getDeathPresent();
		return Human.getLivingIn(this.getDaughters(), c);
	}

	public int getNumOfSurvivingOffspring(){
		return this.getSurvivingOffspring().size();
	}

	public int getNumOfSurvivingSons(){
		return this.getSurvivingSons().size();
	}

	public int getNumOfSurvivingDaughters(){
		return this.getSurvivingDaughters().size();
	}

	public boolean wasChildless(){
		return !Basic.isNotZero(this.offspring.size());
	}

	public Human getFirstborn(){
		return this.offspring.get(0);
	}

	public String getTenure(){
		String s = " (m. "+this.getBeginning();
		if (this.hasEnded()){
			return s+" d. "+this.getEnding()+")";
		} else {
			return s+")";
		}
	}

//Shortened version of GetTenure

	public String getTenureShort(){
		int b = this.getBeginningYear();

//If the relation has not ended there can't be end date...
		if (this.hasEnded()){
			int e = this.getEndingYear();

//If the marriage began and ended during the same year there is no point in typing the same year twice
			if (b != e){
				return b+"–"+e;
			}
		}
		return b+"";

	}

	public int getBeginningYear(){				return this.beginning.get(Calendar.YEAR);}
	public int getEndingYear(){					return this.ending.get(Calendar.YEAR);}

	public Calendar getBeginning(){				return this.beginning;}
	public Calendar getEnding(){				return this.ending;}


	public static String[] happyDesc = 		{"miserable", "joyless", "", "happy", "loving"};
	public static String[] unionDesc = 		{"union", "couple", "wedlock", "marriage", "matrimony"};



	public float getFertility(){ 				return this.fertility;					}
	public int getHappiness(){ 					return this.sexSat+this.romance;		}
	public boolean isActive(){					return this.active;						}
	public Human getStag(){						return this.stag;						}
	public Human getDoe(){						return this.doe;						}
	public boolean hasEnded(){					return this.ending != null;				}




}
