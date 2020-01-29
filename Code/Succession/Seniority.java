/*
package Code.Succession;
import Code.Human.Human;
import Code.Ancestry.Lineage;

import java.util.List;
import java.util.ArrayList;

public class Seniority extends Succession {

	public List<Human> princes;
	//public List<int[]> pClaims;
	public Seniority(Lineage l, SucLaw s){
		super(l, s);
		this.princes = new ArrayList<>();
	}

	public int determine(){
		Human h = this.lineage.getIncumbent().getPerson();
		if (this.hasDirectHeir(h)){ /*
			/*System.out.println("1");*/
	/*		setLineage();
			System.out.println("1");
			return 1;
		} else if (this.hasGeneration(h) && this.hasPrinceHeir(h)){/*
			/*System.out.println("2");
			for(Human x: this.princes){
				System.out.println(x+" "+this.law.canInherit(x));
			}*/
		/*	setLineage();
			return 1;
		} else {
			System.out.println("3");
			return 3;
		}
	}

	public boolean hasDirectHeir(Human h){
		if (this.hasPrinces()){
			if (this.hasPrinceHeir(h)){
				return true;
			} else if (this.hasNextHeir()){
				System.exit(0); //return true;
			}
		}
		return false;
	}

	public boolean hasNextHeir(){
		List<Human> l = 	new ArrayList<>(this.princes);
		List<Human> ll = 	new ArrayList<>();
		this.princes.clear();
		for (Human x: l){
			if (x.isAdult()){
				ll = this.law.getHeirGroup(x);
				for(Human y: ll){
					if (this.law.canInherit(x)){
						addPrince(y);
					}
				}
			}
		}
		for(Human x: this.princes){
			if (this.hasGeneration(x)){
				return true;
			}
		}
		return false;
	}

	public boolean hasPrinceHeir(Human h){
		addToLineage(h);
		for(Human x: this.princes){
			if (this.law.canInherit(x)){
				addToLineage(x);
				this.setHeir(x);
				return true;
			}
		}
		removeFromLineage();
		return false;
	}

	public boolean hasGeneration(Human h){
		if(h.isAdult()){
			List<Human> l = this.law.getHeirGroup(h);
			for(Human x: l){
				if (!this.law.isNaturallyDead(x)){
					if (this.law.canBeTraced(x)){
						this.addPrince(x);
					}
				}
			}
		}
		return this.hasPrinces();
	}

	public boolean hasPrinces(){		return this.princes.size() > 0;	}

	public void addPrince(Human h){

		if (this.princes.contains(h)){
			System.exit(0);
		}
		this.princes.add(h);
	}

}
*/
