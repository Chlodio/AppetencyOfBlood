import java.util.List;
import java.util.ArrayList;

//Sovereign territory
public class Realm extends Territory{
	private ArrayList<Human> magnates;					//family leaders
	private float poverty;

	private static List<Realm> realms = new ArrayList<>();

	public Realm(Office office){
		super(office);
		this.poverty = 										0.5f;
//		this.magnates =										House.getMagnates();
		realms.add(this);
	}

	public float getPoverty(){ return this.poverty; }

	public void gainPoverty(float a){
		this.poverty = Main.min(Math.round((this.poverty+a)*100.0f)/100.0f, 0.9f);
	}

	public void losePoverty(float a){
		this.poverty = Main.max(Math.round((this.poverty-a)*100.0f)/100.0f, 0.0f);
	}

	public double getAlms(){
		return Math.round(1000.0*(this.area*(1.0f-this.poverty))*100.0)/100.0;
	}

	public static Realm getRealm(int i){	return realms.get(i); }

	public static List<Holder> getHolderList(int i){
		return ((Territory) getRealm(i)).getOffice().getHolderList();
	}

	public static Holder getHolder(int i){
		return ((Territory) getRealm(i)).getOffice().getHolder();
	}

}
