package Succession;
import java.util.List;

public class ProximityOfBlood extends Succession {

	private static int genDebth;
	private static int realDebth;
	private static boolean change;

	public ProximityOfBlood(Lineage l, SucLaw s){
		super(l, s);
	}
/*Proximity of Blood*/

	public int determine(){
		Human h = this.lineage.getIncumbent().getPerson();

		if (this.hasGenerationFirst(h)){
			//this.priority = 1;
			setLineage();
			setLineal(true);
			return 1;
		} else if (this.hasPoBPresumptive()){
			//this.priority = 2;
			setLineage();
			return 2;
		}  else if(this.law.toleratesUltimateHeir() && this.hasSecondaryHeir()){
			this.installSecondaryHeir();
			setLineage();
			return 2;
		} else {
			/*if (House.nextHouse != null && House.nextHouse.getHead().isfromRegnantPaternalLine()){
				System.out.println(Consanguinity.getPaternalRelation(House.nextHouse.getHead(), h));
				System.out.println("*"+House.nextHouse.getFullName());
				Human x = House.nextHouse.getHead();
				while(x.hadFather()){
					System.out.println(x.getFullName()+":"+x.getFather().getSons().contains(x));
					if (x.getFather().isRegnant()){
						System.out.println("z");
						break;
					}
					x = x.getFather();
				}
				System.out.println(x.getFather().getFullName());

				System.exit(0);
			}*/
			//this.priority = 3;
			return 3;
		}

	}

	public boolean hasPoBPresumptive(){
		SucLaw.clearTransmitters();
		clearLineage();
		List<Holder> hl = this.lineage.getList();
		int i = this.lineage.getList().indexOf(this.lineage.getIncumbent())-1;
		for (int x = i; x >= 0; x--){
			if (hasGenerationFirst(hl.get(x).getPerson())){
				return true;
			}
		}
		return false;
	}

	public boolean hasGenerationFirst(Human h){
		addToLineage(h);
		genDebth = -1;
		realDebth = 0;
		int gen = 0;

		do{
			change = false;
			if (this.hasGenerationHeir(h, gen, true)){
				return true;
			}
			gen++;
			if (gen == 15){
				System.exit(0);
			} else if (this.law.toleratesSecondaryHeir()){
				if (this.hasSecondaryHeir()){
					this.installSecondaryHeir();
					return true;
				}
			}
		} while (change);

		removeFromLineage();
		return false;
	}

	public boolean hasGenerationHeir(Human h, int gen, boolean vip){
		realDebth++;
		if(h.isAdult()){
			List<Human> l = this.law.getHeirGroup(h);
			for(Human x: l){
				addToLineage(x);
				if (!this.law.isNaturallyDead(x)){
					if (gen == 0){
						if (this.law.canInherit(x)){
							if (this.law.shouldInherit(x)){
								if (vip){
									this.setHeir(x);
									return true;
								} else if (!this.hasSecondaryHeir()){
									this.lockAndSetHeir(x);
								}
							} else if (!this.hasSecondaryHeir()){
								this.lockAndSetHeir(x);
							}
						}
					} else if (!x.isAlive()){
						if (this.law.canBeTraced(x)){
							if (this.law.shouldInherit(x)){
								if (this.hasGenerationHeir(x, gen-1, vip)){
									return true;
								}
							} else if (!this.hasSecondaryHeir()){
								if (this.hasGenerationHeir(x, gen-1, false)){
									return true;
								}
							}
						}
					}
				}
				removeFromLineage();
			}
		}
		if (realDebth > genDebth){
			genDebth = realDebth;
			change = true;
		}
		realDebth--;
		return false;
	}


}
