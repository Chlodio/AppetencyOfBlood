package Code.House;
import Code.Human.Human;
import Code.Common.Basic;
import java.util.List;
import java.util.ArrayList;
import Code.Ancestry.Consanguinity;

public class CadetHouse extends House {
	protected String parentName;

	public CadetHouse(House parent, Human head, Human founder){
		super(founder);
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
		this.addHead(head);
		if (parent.isNoble()){
			this.ennoble(parent.getOrigin());
		}

		//Move the children into the new house

		if (!head.isAlive()){
			throw new RuntimeException();
		}

		List<Human> l = founder.getLegitNonPosthumousSons();
		l.addAll(founder.getLegitDaughters());
		for (Human x: l){
			x.switchHouse(this);
		}

		if (founder.hasLegitSon()){
		} else if ((head.getFather() == founder)){
			if (!this.hasLivingPrince()){
				throw new RuntimeException();
			}
		}
		else {
			if (!this.hasLivingPrince()){
				this.branch();
				if (!this.hasLivingPrince()){
					throw new RuntimeException();
				}
			}
		}

		if (!this.workingMembers(this.getPatriarch())){
			throw new RuntimeException();
		}

		if (!this.patriarchIsSuited()){
			this.branch();
			if (!this.patriarchIsSuited()){
				throw new RuntimeException();
			}
			//throw new RuntimeException();
		}
	}

	@Override
	public String getFullName(){		return this.getParentName()+"-"+this.getName(); }


	final String[] names = {

	};
}
