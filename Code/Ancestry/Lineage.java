package Code.Ancestry;

import Code.Succession.*;
import Code.Politics.*;
import Code.Human.Human;
import Code.Succession.SucLaw;
import Code.Politics.Office;

import java.util.ArrayList;
import java.util.List;
import java.util.Calendar;

public class Lineage{

	private List<Holder> list;
	private Human heir;
	private int priority = 3;
	private Office office;
	private SucLaw law;
	private Succession succession;

	public static Human[] lineageZ;

	public Lineage(Office o){
		this.list = 	 		new ArrayList<>();
		this.office = 	  o;
		this.law = 		  	new SucLaw(this);

		this.succession = new Primogeniture(this); //AncestralElection(this);
	}

	public void determineSuccession(){
		this.succession.clearSLineage();
		this.succession.clearLineage();
		this.succession.setLineal(false);
		this.succession.resetSecondaryHeir();
		this.heir = null;
		this.priority = this.succession.determine();
	}


	public void setHeir(Human h){
		this.heir = h;
	}

	public List<Holder> getList(){					return new ArrayList<>(this.list);	}

	public static Human[] getLineage(){			return lineageZ.clone();}

	public static boolean getLineal(){			return Succession.lineal;}

	public boolean isHolder(Holder h){			return this.list.contains(h); 	}

	public void addTo(Holder h){						this.list.add(h); 				}

	public Holder getFounder(){							return this.list.get(0); 		}

	public Holder getIncumbent(){						return this.office.getHolder(); }

	public Human getHeir(){									return this.heir;				}

	public Holder getLineage(int i){				return this.list.get(i);	}

	public Holder getPrev(){
		return this.list.get(this.list.size()-1);
	}

	public void setPriority(int v){					this.priority = v;				}

	public int getPriority(){								return this.priority;			}

	public SucLaw getLaw(){
		return this.law;
	}

	public Office getOffice(){
		return this.office;
	}

	public void declareHeirStatus(){
		switch(this.priority){
			case 1:
				System.out.println("Heir apparent is "+this.heir.getFormalName());
				break;
			case 2:
				System.out.println("Heir presumptive is "+this.heir.getFormalName());
				break;
			default:
				System.out.println("There is no heir");
		}
	}

}
