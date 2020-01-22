package Interest;
import Common.Basic;

public class Interest{
	private int imperialism;				//Wants to expands the borders of his land
	private int hedonism;					//Wants to enjoy life
	private int stability;					//Wants secure their reign
	private int theology; 					//Wants to help the church
	private int scholarship; 				//Wants to enlighten people
	private	int economy;					//Wants to improve the economy
	private int judiacry;					//Wants to improve the bureaucracy
	private int architecture;				//Wants to construct buildings
	private int chivalry;					//Wants to enchance chivalry
	private int diplomacy;					//wants to pursue diplomacy

	private Interest.Point[] list;
	public Interest(){
		this.list = new Interest.Point[10];
		for (int x = 0; x < 10; x++){
			this.list[x] = Basic.choiceIP(Point.list);
		}
		this.imperialism = 	Point.IMPERIALISM.count(this.list);
		this.hedonism = 	Point.HEDONISM.count(this.list);
		this.stability = 	Point.STABILITY.count(this.list);
		this.theology = 	Point.THEOLOGY.count(this.list);
		this.scholarship = 	Point.SCHOLARSHIP.count(this.list);
		this.economy = 		Point.ECONOMY.count(this.list);
		this.judiacry = 	Point.JUDIACRY.count(this.list);
		this.architecture = Point.ARCHITECTURE.count(this.list);
		this.chivalry = 	Point.CHIVALRY.count(this.list);
		this.diplomacy = 	Point.DIPLOMACY.count(this.list);
	}

	public int getArchitecture(){ 	return this.architecture; }
	public int getChivalry(){ 		return this.chivalry; }
	public int getDiplomacy(){ 		return this.diplomacy; }
	public int getEconomy(){ 		return this.economy; }
	public int getHedonsim(){ 		return this.hedonism; }
	public int getImperialism(){ 	return this.imperialism; }
	public int getJudiacry(){ 		return this.judiacry; }
	public int getScholarship(){ 	return this.scholarship; }
	public int getStability(){ 		return this.stability; }
	public int getTheology(){ 		return this.theology; }
	public Point getRandom(){ 		return Basic.choiceIP(this.list); }

	public void print(){
		System.out.println("Imperialism: "+this.imperialism);
		System.out.println("Hedonism: "+this.hedonism);
		System.out.println("Stability: "+this.stability);
		System.out.println("Theology: "+this.theology);
		System.out.println("Scholarship: "+this.scholarship);
		System.out.println("Economy: "+this.economy);
		System.out.println("Judiacry: "+this.judiacry);
		System.out.println("Architecture: "+this.architecture);
		System.out.println("Chivalry: "+this.chivalry);
		System.out.println("Diplomacy: "+this.diplomacy);
	}

	public enum Point{
		IMPERIALISM(new InterestImperialism()),
		HEDONISM(new InterestHedonism()),
		STABILITY(new InterestStability()),
		THEOLOGY(new InterestTheology()),
		SCHOLARSHIP(new InterestScholarship()),
		ECONOMY(new InterestEconomy()),
		JUDIACRY(new InterestJudiacry()),
		ARCHITECTURE(new InterestArchitecture()),
		CHIVALRY(new InterestChivalry()),
		DIPLOMACY(new InterestDiplomacy());
		public static final Point[] list = {IMPERIALISM, STABILITY, THEOLOGY, SCHOLARSHIP, ECONOMY, JUDIACRY, ARCHITECTURE, CHIVALRY, DIPLOMACY};
		private InterestType type;
		Point(InterestType type){ 			this.type = type; }
		public InterestType getType(){ 		return this.type; }
		public int count(Point[] list){
			int c = 0;
			for (Point x: list){
				if (x == this){	c++; }
			}
			return c;
		}
	}
}
