package Code.Politics;
import Code.Common.Basic;
import java.util.ArrayList;
import java.util.List;

public class RegnalName {
	private String name;
	private int count; 						//how many times does the name appear
	private ArrayList<Holder> users;		//holders who use this regnal name

	public RegnalName(String name){
		this.name = name;
		this.count = 1;
		this.users = new ArrayList<>();
	}

	public static String regnafy(Holder holder, Office office){
		String name = holder.getPerson().getForeName();
		holder.getPerson().getName().setRegnal();
		if (isListed(name, office)){
			int o = getListed(name, office).increase(holder);
			return name+" "+Basic.getRoman(o);
		} else{
			RegnalName regnal = office.addRegnalNames(new RegnalName(name));
			regnal.users.add(holder);
			return name;
		}
	}

	public static boolean isListed(String name, Office office){
		List<RegnalName> l = office.getRegnalNames();
		for(RegnalName x: l){
			if (x.name == name){
				return true;
			}
		}
		return false;
	}

	public static RegnalName getListed(String name, Office office){
		List<RegnalName> l = office.getRegnalNames();
		for(RegnalName x: l){
			if (x.name == name){
				return x;
			}
		}
		return null;
	}

	public int increase(Holder holder){
		if (this.count == 1){
			this.users.get(0).fixTheFirst();
		}
		this.count++;
		this.users.add(holder);
		return this.count;
	}

	public String getName(){
		return this.name;
	}
}
