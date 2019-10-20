import java.util.ArrayList;

//Orginal houses
public class MainHouse extends House {

	public MainHouse(Human founder){
		super(founder);
		this.prestige = 						0;
		this.maleNames = 						new ArrayList<>();
		this.femaleNames = 						new ArrayList<>();
        this.founder = 							founder;
		this.patriarch =						head;
		this.generation =						0;
		int id = House.getId();
		if (id > 127){							this.ranking = 1;	}
		else if (id > 63){						this.ranking = 2;	}
		else if (id > 31){						this.ranking = 3;	}
		else if (id > 15){						this.ranking = 4;	}
		else if (id > 7){						this.ranking = 5;	}
		else if (id > 3){						this.ranking = 6;	}
		else if (id > 1){						this.ranking = 7;	}
		else {									this.ranking = 8;	}
		this.heads.add(founder);
		this.maleNames.add(founder.getName().getName());
	}

	@Override
	public String getFullName(){				return "House "+this.name;	}

}
