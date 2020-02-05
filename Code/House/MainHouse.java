package Code.House;
import java.util.ArrayList;
import Code.Human.Human;
//Orginal houses
public class MainHouse extends House {

	public MainHouse(Human founder){
		super(founder);
		handle(founder);
		int id = House.getId();
		if (id > 127){							this.ranking = 1;	}
		else if (id > 63){						this.ranking = 2;	}
		else if (id > 31){						this.ranking = 3;	}
		else if (id > 15){						this.ranking = 4;	}
		else if (id > 7){						this.ranking = 5;	}
		else if (id > 3){						this.ranking = 6;	}
		else if (id > 1){						this.ranking = 7;	}
		else {									this.ranking = 8;	}
	}

	public MainHouse(Human founder, String s){
		super(founder, s);
		handle(founder);
		this.ranking = 8;
	}


	public void handle(Human founder){
		this.prestige = 						0;
		this.maleNames = 						new ArrayList<>();
		this.femaleNames = 						new ArrayList<>();
        this.founder = 							founder;
		this.patriarch =						this.head;
		this.generation =						0;
	//	this.addHead(founder);
		this.maleNames.add(founder.getName().getName());
		this.addKinsman(founder);

	}

	@Override
	public String getFullName(){				return this.name;	}

}
