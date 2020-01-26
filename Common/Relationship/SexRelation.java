package Relationship;
import Human.Human;
import Common.Basic;
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
	protected int[] procreation;
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
			System.exit(0);
		}
		this.offspring = 	new ArrayList<>();
		this.active = 		true;
		this.calcProcreation(m, w);
	}

	/*Calculate fertility array*/
		public void calcProcreation(Human m, Human w){
			int ha = 			m.getAge();
			int wa = 			w.getAge();
			int fmy = 			70-ha;
			int ffy = 			50-wa;
			float v = 			(this.getFertility()/10f)*(0.75f+(this.getHappiness()*0.1f));
			this.procreation =  new int[100-ha];
			if (fmy > 0 && ffy > 0){
				int tfy = Math.min(fmy, ffy);
				for (int x = 0; x < tfy; x++){
					this.procreation[x] = (int) (v*(fert[(wa-12)/10][(wa-12)%10]));
					wa++;
				}
			}
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


		public static String[] happyDesc = 		{"miserable", "joyless", "", "happy", "loving"};
		public static String[] unionDesc = 		{"union", "couple", "wedlock", "marriage", "matrimony"};

		public float getFertility(){ 				return this.fertility;					}
		public int getHappiness(){ 					return this.sexSat+this.romance;		}
		public boolean isActive(){					return this.active;						}
		public Human getStag(){						return this.stag;						}
		public Human getDoe(){						return this.doe;						}
		public int getBeginning(){					return this.beginning.get(Calendar.YEAR);}
		public int getEnding(){						return this.ending.get(Calendar.YEAR);}
		public boolean hasEnded(){					return this.ending != null;				}
		public int getOffspringNum(){				return this.offspring.size();			}
		public List<Human> getOffspring(){			return new ArrayList<>(this.offspring);}
		public void addOffspring(Human o){			this.offspring.add(o);					}


}
