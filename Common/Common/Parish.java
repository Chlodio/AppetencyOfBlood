package Common;
import Human.*;
import java.util.ArrayList;

//Members of religious community
public class Parish{

	private ArrayList<Man> men;
	private ArrayList<Woman> women;
	public Parish(){
		this.men = new ArrayList<>();
		this.women = new ArrayList<>();
	}

	public void add(Man person){			this.men.add(person);		}

	public void add(Woman person){			this.women.add(person);		}

	public void remove(Man person){			this.men.remove(person);	}

	public void remove(Woman person){		this.women.remove(person);	}

}
