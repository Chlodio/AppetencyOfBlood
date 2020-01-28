package Common;
import Human.*;
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
		this.name 		= Basic.choice(maleNames);
		this.special 	= false;
		this.owner	 	= h;
	}

	public Name(boolean t, Human h, boolean s){
		this.name 		= Basic.choice(femaleNames);
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
			reader = new FileReader("Input/Forename_male.txt");
			buffy = new BufferedReader(reader);
			while((l = buffy.readLine()) != null) {
				maleNames[c] = l;
				c++;
            }
			buffy.close();
		}
		catch(IOException e){
			System.out.println("f");
			System.exit(0);

		}
		c = 0;
		try {
			reader = new FileReader("Input/Forename_female.txt");
			buffy = new BufferedReader(reader);
			while((l = buffy.readLine()) != null) {
				femaleNames[c] = l;
				c++;
            }
			buffy.close();
		}
		catch(IOException e){}
	}


	public static void createFemaleName(Human c){
		c.setName(new Name(true, c, true));
		((Woman) c).getName().setFull(((Woman) c).makeName());
	}

	public static void aSon(Human father, Human mother, Human child){
		child.setName(new Name(father.getHouse().getMemberNameM(child), false, child));
		((Man) child).getName().setFull(((Man) child).makeName());
	}

	public static void aDaughter(Human father, Human mother, Human child){
			child.setName(new Name(father.getHouse().getMemberNameF(child), false, child));
			((Woman) child).getName().setFull(((Woman) child).makeName());
	}

	public void setNick(String n){
		this.nick = n;
		this.setFull(this.getOwner().makeName());
	}


	public String getPatronymic(){
		String n = this.getOwner().getBirthName();							//I.e Bob Smith

		//Naturally fatherless people don't get a patronymic name
		if (this.getOwner().hadFather()){
			n += ", ";
			if (this.getOwner().isLegimate()){
				n += this.getOwner().getSexChildOrderName()+" of ";			//I.e first son of
			} else {
				n += "natural "+this.getOwner().getOffspring();				//I.e natural son/daughter
			}
			n += this.getOwner().getFather().getFormalName();
		}
		return n;
	}


	public boolean hasNick(String s){ 		return this.nick == s;	}
	public boolean isSpecial(){ 			return this.special; 	}
	public Human getOwner(){ 				return this.owner; 		}
	public String getFull(){ 				return this.full; 		}
	public String getName(){ 				return this.name; 		}
	public String getNick(){ 				return this.nick; 		}
	public boolean hasRegnal(){ 			return this.hasRegnal; 	}
	public void setRegnal(){				this.hasRegnal = true; 	}
	public void setFull(String n){ 			this.full = n; 			}
	public void setName(String n){ 			this.name = n; 			}
	public void setOwner(Human h){ 			this.owner = h; 		}


	public static String getRandomMaleName(){ 		return Basic.choice(maleNames); }
	public static String getRandomFemaleName(){ 	return Basic.choice(femaleNames); }

	static String[] maleNames = 	new String[93];
	static String[] femaleNames = 	new String[48];

	static final String[] orderNames = {"Maxim", "Secund", "Terti", "Quart", "Quant", "Sext", "Septim", "Octavi", "Non", "Decim"};
}
