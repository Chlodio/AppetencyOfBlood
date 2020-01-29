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
		this.addHead(founder);
		this.addHead(head);
		if (parent.isNoble()){
			this.ennoble();
		}

		//Move the children into the new house
		List<Human> l = founder.getLegitNonPosthumousSons();
		l.addAll(founder.getLegitDaughters());
		for (Human x: l){
			x.switchHouse(this);
		}

		if (this.heads.get(0) == this.heads.get(1)){
			throw new RuntimeException();
		}
	}

	@Override
	public String getFullName(){		return this.getParentName()+"-"+this.getName(); }


	final String[] names = {

	};
}
