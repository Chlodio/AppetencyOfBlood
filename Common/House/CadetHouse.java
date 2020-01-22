package House;
import Human.Human;
import Common.Basic;
import java.util.List;
import java.util.ArrayList;

public class CadetHouse extends House {
	protected String parentName;

	public CadetHouse(House parent, Human head, Human founder){
		super(head);
		this.prestige = 			parent.getPrestige();
		this.maleNames = 			new ArrayList<>(parent.getMaleNames());
		this.femaleNames = 			new ArrayList<>(parent.getFemaleNames());
		this.founder = 				founder;
		this.generation =			parent.getGeneration() + 1;
		this.parent =				parent;
		this.ranking =				Basic.max(parent.ranking-1, 1);
		this.parentName =			parent.getName();
		this.setPatriarch(founder);
		parent.branches.add(this);
		this.heads.add(founder);
		this.heads.add(head);
		List<Human> l = new ArrayList<>(founder.getChildren());
		for (Human x: l){
			x.switchHouse(this);
		}
	}

	@Override
	public String getFullName(){		return this.parentName+"-"+this.name; }


	final String[] names = {

	};
}
