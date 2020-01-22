package Politics;

public enum Title{
	LORD("Lord", 20),
	LADY("Lady", 10),
	INTENDANT("Praetor", 50),
	PROREGENT("Regent", 50),
	REGENT("Regent", 50),
	KING("King", 100),
	PRINCE("Prince", 50),
	PRINCESS("Princess", 50),
	QUEENREGNANT("Queen", 90),
	QUEENCONSORT("Queen-consort", 50),
	PRINCECONSORT("Prince-consort", 55);
	public String name;
	public int prestige;
	Title(String n, int p){
		this.name = n;
		this.prestige = p;
	}
	public String getName(){			return this.name;	}
	public int getPrestige(){			return this.prestige;	}

}

//QUEEN_DOWAGER("Queen", 30),								//aka queen dowager
//QUEEN_MOTHER("Queen", 60),
//PROQUEENMOTHER("Queen", 60),
//QUEENREGENT("Queen", 70),
//PROQUEENREGENT("Queen", 70);
