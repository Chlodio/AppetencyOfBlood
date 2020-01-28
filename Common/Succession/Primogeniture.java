package Succession;
import Human.*;
import Ancestry.Lineage;
import Ancestry.Claim;
import Politics.Holder;
import java.util.Calendar;
import java.util.List;
import java.util.ArrayList;


public class Primogeniture extends Succession {
	public Primogeniture(Lineage l, SucLaw s){
		super(l, s);
	}

	public int determine(){
		Human h = this.lineage.getIncumbent().getPerson();

		if (this.hasPrimoApparent(h)){
			setLineage();
			setLineal(true);
			return 1;
		} else if (this.hasPrimoPresumptive()){
			setLineage();
			return 2;
		}   else if(this.law.toleratesUltimateHeir() && this.hasSecondaryHeir()){
			this.installSecondaryHeir();
			setLineage();
			return 2;
		} else {
			return 3;
		}
	}

	public boolean hasPrimoApparent(Human h){
		addToLineage(h);
		if (hasPrimoPrimaryHeir(h)){
			this.setPriority(1);
			return true;
		}  else if (this.law.toleratesSecondaryHeir()){
			if (this.hasSecondaryHeir()){
				this.installSecondaryHeir();
				return true;
			}
		}
		removeFromLineage();
		return false;
	}

	public boolean hasTransmitter(){
		return this.law.getTransmitters().size() > 0;
	}

	public boolean hasPrimoTransmitter(){
		List<Human> l = this.law.getTransmitters();
		SucLaw.clearTransmitters();
		for (Human x: l){
			if (hasPrimoPrimaryHeir(x)){
				return true;
			}
		}
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
			for (int x = i; x > 0; x--){
				if (hasPrimoApparent(l[x])){
					this.setPriority(2);
					return true;
				}
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
			List<Human> l = this.law.getHeirGroup(h);
			for(Human x: l){
				addToLineage(x);
				if (!this.law.isNaturallyDead(x) ){
					if (this.law.canInherit(x)){
						if (this.law.shouldInherit(x)){
							this.setHeir(x);
							return true;
						} else if (!this.hasSecondaryHeir()){
							this.lockAndSetHeir(x);
						}
					} else if (this.law.canBeTraced(x)){
						if (this.law.shouldBeTraced(x)){
							if (hasPrimoPrimaryHeir(x)){
								return true;
							}
						} else if (!this.hasSecondaryHeir()) {
							lockSecLineage();
							if (this.findPrimoSecondaryHeir(x)){}
						}
					}
				}
				removeFromLineage();
			}
		}
		return false;
	}

	public boolean findPrimoSecondaryHeir(Human h){
		if (this.law.canInherit(h)){
			return true;
		} else if (this.hasPrimoSecondaryHeir(h) ){
			return true;
		} else {
			List<Human> l;
			l = this.law.getTransmitters();
			SucLaw.clearTransmitters();
			for (Human x: l){
				addToSecLineage(x);
				if ((hasPrimoSecondaryHeir(x))){
					return true;
				}
				removeFromSecLineage();
			}
		}
		return false;
	}

	public boolean hasPrimoSecondaryHeir(Human h){
		if (h.isAdult()){
			List<Human> l = this.law.getHeirGroup(h);
			for(Human x: l){
				addToSecLineage(x);
				if (!this.law.isNaturallyDead(x) ){
					if (this.law.canInherit(x)){
						if (this.law.shouldInherit(x)){
							this.setSecondaryHeir(x);
							return true;
						} else {
/*							if (!x.isChildOf(lineageT[lineageI-2])){
								throw new RuntimeException();
							}*/
							this.law.addTransmitter(x);
						}
					} else if (this.law.canBeTraced(x)){
						if (this.law.shouldBeTraced(x)){
							if (hasPrimoSecondaryHeir(x)){
								return true;
							}
						} else if (!this.hasSecondaryHeir()) {
							/*if (!x.isChildOf(lineageT[lineageI-2])){
								throw new RuntimeException();
							}*/
							this.law.addTransmitter(x);
						}
					}
				}
				removeFromSecLineage();
			}
		}
		return false;
	}


};
