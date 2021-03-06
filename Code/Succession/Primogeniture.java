package Code.Succession;
import Code.Human.*;
import Code.Ancestry.Lineage;
import Code.Ancestry.Claim;
import Code.Politics.Holder;
import Code.Common.Basic;
import Code.History.Annals;
//import Code.Politics.Office;
import Code.calendar.Calendar;
import java.util.List;
import java.util.ArrayList;


public class Primogeniture extends Succession {
	public Primogeniture(Lineage l){
		super(l);
	}

	public int determine(){
		Human h = this.lineage.getIncumbent().getPerson();
		this.resetPriority();
		this.law.setAsGlobal();

		int heirStatus = this.getHeirStatus();

		if (heirStatus != 3){
			return heirStatus;						//Heir is found the traditional way
		} else if (this.law.hasLastResort()){
			SucLaw.applyLastResort();
			heirStatus = this.getHeirStatus();	//Heir is found the exceptional way
		}
		return heirStatus;
	}

	public int getHeirStatus(){
		Human h = this.lineage.getIncumbent().getPerson();
		if (this.hasPrimoApparent(h)){
			setLineage();
			setLineal(true);
			return this.getPriority();
		} else if (this.hasPrimoPresumptive()){
			setLineage();
			return this.getPriority();
		} else {;
			return 3;
		}
	}

	public boolean hasPrimoApparent(Human h){
		addToLineage(h);
		if (hasPrimoPrimaryHeir(h)){
			this.setPriority(1);
			return true;
		}
		removeFromLineage();
		return false;
	}

	public boolean hasPrimoCheck(Holder h){
		Claim c = h.getClaim();
		Human[] l;
		int i;

		if (h.getClaim().isSpecial()){
			return false;
		} else {
			l = c.getLineage();
		}

		if (l.length > 2){
			i = l.length-1;

			for (int x = 0; x < i; x++){
				addToLineage(l[x]);
			}

			for (int x = i; x > 0; x--){

				if (hasPrimoApparent(l[x])){
					this.setPriority(2);
					return true;
				}
				removeFromLineage();
			}
		}
		return false;
	}


	public boolean hasPrimoPresumptive(){
		clearLineage();
		Human[] l;
		int i;
		List<Holder> hl = this.lineage.getList();

		if (hasPrimoCheck(this.lineage.getIncumbent())){
			return true;
		}


		i = this.lineage.getList().indexOf(this.lineage.getIncumbent())-1;
		for (int x = i; x >= 0; x--){
			if (hasPrimoApparent(hl.get(x).getPerson())){
				this.setPriority(2);
				return true;
			} else if (hasPrimoCheck(hl.get(x))){
				return true;
			}
		}


		return false;
	}


	public Calendar getProminentDate(Human h){
		int i = this.lineage.getList().size()-1;
		List<Holder> l = this.lineage.getList();
		for (int x = i; x >= 0; x--){
			if (l.get(x).getPerson() == h){
				return l.get(x).getStart();
			}
		}
		throw new RuntimeException();
	}

	public boolean hasPrimoPrimaryHeir(Human h){
		if (h.isAdult()){
			List<Human> l = SucLaw.getHeirGroup(h);
			for(Human x: l){
				addToLineage(x);
				if (!SucLaw.isNaturallyDead(x) ){
					if (SucLaw.canInherit(x)){
							this.setHeir(x);
							return true;
					}  else if (SucLaw.canBeTraced(x) && hasPrimoPrimaryHeir(x) ){
								return true;
					} else if  (x.hasUnbornChild()){
						if (x.isMale()){
							((Woman) x.getLatestWife()).getEmbryo().setOffice(this.getLineage().getOffice());
						} else {
								((Woman) x).getEmbryo().setOffice(this.getLineage().getOffice());
						}
						x.getLatestWife();
						this.setPriority(0);
						Basic.annals.recordInterregnumBeginning(x);
						return true;
					}
				}
				removeFromLineage();
			}
		}
		return false;
	}

};
