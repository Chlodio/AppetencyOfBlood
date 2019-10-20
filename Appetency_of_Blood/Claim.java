import java.util.ArrayList;
import java.util.List;

class Claim{
	private ArrayList<Human> lineage;
	private boolean coverture;
	private Holder origin;
	private Human holder;
	private int blood; 											//0 = agnatic/1 = cognatic
	private int special;										//0 = regular/1 = elected
	private Office office;
	private Holder predecessor;									//last predecessor
	public final static String[] kinship = {"Agnatic", "Patrilineal", "Cognatic"};
	public static Claim temp;									//stored in succession

	public static Claim getTemp(){
		Claim it = temp;
		return it;
	}

	public String getClaimHTML(){
		String prefix = "";
		int linSiz = 	this.lineage.size();		//lineage size
		String txt;
		if ((linSiz-2) != 0){
			prefix = kinship[this.blood]+" ";
		}

		if (!this.isSpecial()){
			Human heir =	this.holder;
			Human a1;
			Human a2;
			if ((linSiz-2) <= 3){
				if (!this.isCoverture()){
					txt = Main.capitalize(prefix+heir.getRelation(linSiz-2))+" of "+this.lineage.get(linSiz-1).getName().getFull();
			//		return "<ul><li>"+txt+"</<li></ul>";
					if (this.isLastPredecessorParent() || !this.lineage.get(1).isMale() || this.isPredecessorWoman()){
						return "<ul><li>"+txt+"</<li></ul>";
					} else{
						return "<ul><li>"+txt+"</li><li> "+Main.capitalize(Consanguinity.getPaternalRelation( this.getPredecessor().getPerson(),this.getHolder()))+" of "+this.getPredecessor().getPerson().getName().getFull()+"</li></ul>";
					}
				} else{
					return "<ul><li>"+Main.capitalize(heir.getRelation(linSiz-2))+"-in-law of "+this.lineage.get(linSiz-1).getName().getFull()+"</li></ul>";
				}
			} else{
				a1 = this.lineage.get(linSiz-1);
				a2 = this.lineage.get(linSiz-2);
				txt = kinship[this.blood]+" "+(linSiz-2)+"th generation descendant of "+a1.getName().getFull()+" through "+a1.getPossessive()+" "+a2.getOffspring()+", "+a2.getName().getFull();
				if (!this.isCoverture()){
					return "<ul><li>"+txt+"</li></ul>";
				} else{
					txt = Main.toLowerCase(txt);
					return "<ul><li>Spouse of "+txt+"</li></ul>";
				}
			}
		} else{
			return "<ul><li>Elected</li></ul>";
		}
	}

	public boolean hasGrandparentClaimant(){
		if (this.isLineageOfHeight(2) && this.hasOrigin()) {
			if (this.getOrigin().getClaim().isLineageOfHeight(2)){
				return true;
			 }
		 }
		 return false;
	}

	public Human getGrandparentClaimant(){
 		return this.getOrigin().getClaim().getFromLineage(1);
	}

	//Was the last holder incumbent's parent
	public boolean isLastPredecessorParent(){
		if(this.getPredecessor() != null && this.getPredecessor().getPerson().isAdult()){
			return this.getHolder().isChildOf(this.getPredecessor().getPerson());
		}
		return false;
	}

	public boolean isPredecessorWoman(){
		if (this.getPredecessor() != null){
			return this.getPredecessor().getPerson().isFemale();
		}
		return false;
	}

//Micro Methods

	public boolean hasOrigin(){						return this.origin != null;			}
	public boolean isCoverture(){					return this.coverture; 				}
	public boolean isLineageOfHeight(int v){		return this.lineage.size() >= v; 	}
	public boolean isSpecial(){						return this.special != 0; 			}
	public Holder getOrigin(){						return this.origin.getHolder();		}
	public Holder getPredecessor(){					return this.predecessor;			}
	public Human getFromLineage(int v){				return this.lineage.get(v); 		}
	public Human getHolder(){						return this.holder;					}
	public Office getOffice(){						return this.office;					}
	public void setBlood(int b){					this.blood = b;						}
	public void setCoverture(boolean c){			this.coverture = c;					}
	public void setHolder(Human h){					this.holder = h;					}
	public void setLineage(ArrayList<Human> l){		this.lineage = new ArrayList<>(l);	}
	public void setOffice(Office o){				this.office = o;					}
	public void setOrigin(Holder h){				this.origin = h;					}
	public void setPredecessor(Holder h){
		this.predecessor = h;
	}
	public void setSpecial(int s){					this.special = s;					}
}
