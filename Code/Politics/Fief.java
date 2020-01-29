package Code.Politics;

//Non-sovereign territory
public class Fief extends Territory{
	private Realm realm; 			//part of a realm
	public Fief(Office office, Realm realm){
		super(office);
		this.realm = realm;
	}
}
