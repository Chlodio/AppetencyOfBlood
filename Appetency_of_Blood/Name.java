import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;


public class Name{
	private String name;
	private String nick;
	private String full;
	private boolean special;			//is forename special
	private Human owner;
	private boolean hasRegnal;

	public Name(boolean t, Human h){
		this.name 		= Main.choice(maleNames);
		this.special 	= false;
		this.owner	 	= h;
	}

	public Name(boolean t, Human h, boolean s){
		this.name 		= Main.choice(femaleNames);
		this.special 	= false;
		this.owner	 	= h;
	}

	public Name(String name, boolean sp, Human h){
		this.name 		= name;
		this.special	= sp;
		this.owner 		= h;
	}

	public static void buildForenames(){
		int c 		= 0;
		String l 	= null;
		FileReader reader;
		BufferedReader buffy;
		try {
			reader = new FileReader("Forename_male.txt");
			buffy = new BufferedReader(reader);
			while((l = buffy.readLine()) != null) {
				maleNames[c] = l;
				c++;
            }
			buffy.close();
		}
		catch(IOException e){}
		c = 0;
		try {
			reader = new FileReader("Forename_female.txt");
			buffy = new BufferedReader(reader);
			while((l = buffy.readLine()) != null) {
				femaleNames[c] = l;
				c++;
            }
			buffy.close();
		}
		catch(IOException e){}
	}

	public static void aSon(Human father, Human mother, Human child){

//		if (father.sons.size() <= 10 && father.sons.size() >= Main.randint(20)){
//			child.setName(new Name(orderNames[father.sons.size()-1], true));
//		}

		child.setName(new Name(father.getHouse().getMemberNameM(child), false, child));
		((Man) child).name.setFull(((Man) child).makeName());
	}

	public static void aDaughter(Human father, Human mother, Human child){
/*			if (father.daughters.size() <= 10){
				child.setName(new Name(orderNames[father.daughters.size()-1], true, child));
			}
			else {
				child.setName(new Name(getRandomName(), false, child));
				//Main.choice(names));
			}
	/*		if (father.isRegnant()){
				if (!father.getOldestDaughter().getName().hasNick("Commod")){}
				child.getName().setNick("Commod");
			}*/
			//((Woman) child).name.setFull(((Woman) child).makeName());
			child.setName(new Name(father.getHouse().getMemberNameF(child), false, child));
			((Woman) child).name.setFull(((Woman) child).makeName());
	}

	public void setNick(String n){
		this.nick = n;
		this.setFull(this.getOwner().makeName());
	}

	public boolean hasNick(String s){ 		return this.nick == s; }
	public boolean isSpecial(){ 			return this.special; }
	public Human getOwner(){ 				return this.owner; }
	public String getFull(){ 				return this.full; }
	public String getName(){ 				return this.name; }
	public String getNick(){ 				return this.nick; }
	public boolean hasRegnal(){ 			return this.hasRegnal; }
	public void setRegnal(){				this.hasRegnal = true; }
	public void setFull(String n){ 			this.full = n; }
	public void setName(String n){ 			this.name = n; }
	public void setOwner(Human h){ 			this.owner = h; }


	public static String getRandomMaleName(){ 		return Main.choice(maleNames); }
	public static String getRandomFemaleName(){ 	return Main.choice(femaleNames); }

	static String[] maleNames = 	new String[100];
	static String[] femaleNames = 	new String[100];

	static final String[] orderNames = {"Maxim", "Secund", "Terti", "Quart", "Quant", "Sext", "Septim", "Octavi", "Non", "Decim"};
}
