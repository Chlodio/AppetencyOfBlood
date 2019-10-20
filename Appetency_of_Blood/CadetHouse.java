import java.util.ArrayList;

public class CadetHouse extends House {

	public CadetHouse(House parent, Human head, Human founder){
		super(head);
		this.prestige = 			parent.getPrestige();
		this.maleNames = 			new ArrayList<>(parent.getMaleNames());
		this.femaleNames = 			new ArrayList<>(parent.getFemaleNames());
		this.founder = 				founder;
		this.generation =			parent.getGeneration() + 1;
		this.parent =				parent;
		this.ranking =				Main.max(parent.ranking-1, 1);
		this.setPatriarch(founder);
		parent.branches.add(this);
		this.heads.add(founder);
		this.heads.add(head);
		for (Human x: founder.children()){
			x.switchHouse(this);
			//switchHouse(x, oldHouse, newHouse);
		}
	/*	System.out.print(this+" was created with following members("+this.getKinsmen().size()+"):\t");
		for(Human x: this.getKinsmen()){
			System.out.print(x+", ");
		}
		System.out.println();*/
	}

	@Override
	public String getFullName(){		return "Genos "+this.name+"a"; }


	final String[] names = {

	};
}
