import java.util.ArrayList;

class RegnalName {
	private String name;
	private int count; 						//how many times does the name appear
	private ArrayList<Holder> users;		//holders who use this regnal name

	public RegnalName(String name){
		this.name = name;
		this.count = 1;
		this.users = new ArrayList<>();
	}

	public static String regnafy(Holder holder, Office office){
		String name = holder.getPerson().getName().getName();
		holder.getPerson().getName().setRegnal();
		if (isListed(name, office)){
			int o = getListed(name, office).increase(holder);
			if (holder.getPerson().isMale()){
				return name+" "+Main.getRoman(o);
			} else{
				return name+" "+Main.getRoman(o);
			}
		} else{
			RegnalName regnal = office.addRegnalNames(new RegnalName(name));
			regnal.users.add(holder);
			if (holder.getPerson().isMale()){
				return name;
			} else{
				return name;
			}
		}
	}

	public static boolean isListed(String name, Office office){
		for(RegnalName x: office.getRegnalNames()){
			if (x.name == name){
				return true;
			}
		}
		return false;
	}

	public static RegnalName getListed(String name, Office office){
		for(RegnalName x: office.getRegnalNames()){
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
