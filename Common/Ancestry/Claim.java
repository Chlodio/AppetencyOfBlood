package Ancestry;
import Human.Human;
import Common.Basic;
import Politics.Office;
import Politics.Holder;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class Claim{
	private Human[] lineage;
	private boolean coverture;
	private Holder origin;
	private Human holder;
	private int blood; 											//0 = agnatic/1 = cognatic
	private int special;										//0 = regular/1 = elected
	private Office office;
	private Holder predecessor;									//last predecessor
	private boolean lineal;										//Lineal descendant predecessor

	public final static String[] kinship = {"Agnatic", "Patrilineal", "Cognatic"};
	public static Claim temp;									//stored in succession



	public static Claim getTemp(){
		Claim it = temp;
		return it;
	}

	public String getThroughLine(int i){
		Human a1 =	lineage[0];
		Human a2 =	lineage[1];
		String s =	a1.getFullName()+", through ";
		s +=		a1.getPossessive()+" "+a2.getOffspring();
		s +=		", "+a2.getFullName();
		return s;
	}

	public String getClaimHTML(){
		String prefix = "";
		int linSiz;
		if (lineage != null){
			linSiz = lineage.length;
			for(Human x: this.lineage){
				System.out.print(x.getFormalName()+" ("+x+") -> ");
			}
			System.out.println("\n");
		} else {
			linSiz = 0;
		}
		String txt =	"";
		if (linSiz > 2){
			prefix = kinship[this.blood]+" ";
		}
;
		if (!this.isSpecial()){
			Human heir =	this.holder;
			Human pr   =	this.getPredecessor().getPerson();	//predecessor
			if (linSiz <= 4){
				if (!this.isCoverture()){
					String c1 = Basic.capitalize(heir.getRelation(linSiz-2))+" of ";

					if ((linSiz) > 2){
						txt += c1+getThroughLine(linSiz);
					} else{
						String c2 = this.lineage[0].getFullName();
						txt = c1+c2;
					}
					/*Additional information, secondary claim*/


					if (this.isLineal()){
						return "<ul><li>"+txt+"</<li></ul>";
					} else{
						if (!this.lineageContainsWomen()){
							String r1 = "<ul><li>"+txt+"</li><li>";
							String r2 = Consanguinity.getPaternalRelation(pr, heir);
							r2 = Basic.capitalize(r2);
							String r3 = " of "+pr.getFullName()+"</li></ul>";
							return r1+r2+r3;
						} else {
							String rs = Consanguinity.getCognaticRelation(pr, heir);
							if (rs.length() > 0){
								return "<ul><li>"+txt+"</li><li>"+Basic.capitalize(rs)+" of "+pr.getFullName()+"</li></ul>";
							} else {
								return "<ul><li>"+txt+"</<li></ul>";
							}
						}
					}
				} else{
					return "<ul><li>"+Basic.capitalize(heir.getRelation(linSiz))+"-in-law of "+this.lineage[0].getFullName()+"</li></ul>";
				}
			} else{
				txt = kinship[this.blood]+" "+Basic.getOrdial(linSiz-1);
				txt = txt+" generation descendant of "+getThroughLine(linSiz);
				if (!this.isCoverture()){
					return "<ul><li>"+txt+"</li></ul>";
				} else{
					txt = Basic.toLowerCase(txt);
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

	//Was the last holder incumbent's parent?
	public boolean isLastPredecessorParent(){
		return this.getHolder().isChildOf(this.getPredecessor().getPerson());
	}

	public boolean isPredecessorWoman(){
		if (this.getPredecessor() != null){
			return this.getPredecessor().getPerson().isFemale();
		}
		return false;
	}

//Micro Methods

	public boolean lineageContainsWomen(){
		for(Human x: this.lineage){
			if (x.isFemale()){
				return true;
			}
		}
		return false;
	}

	public boolean hasOrigin(){						return this.origin != null;			}
	public boolean isCoverture(){					return this.coverture; 				}
	public boolean isLineageOfHeight(int v){		return this.lineage.length >= v; 	}
	public Human[] getLineage(){					return this.lineage; 				}
	public boolean isSpecial(){						return this.special != 0; 			}
	public Holder getOrigin(){						return this.origin.getHolder();		}
	public Holder getPredecessor(){					return this.predecessor;			}
	public Human getFromLineage(int v){				return this.lineage[v]; 			}
	public Human getHolder(){						return this.holder;					}
	public Office getOffice(){						return this.office;					}
	public void setBlood(int b){					this.blood = b;						}
	public void setCoverture(boolean c){			this.coverture = c;					}
	public void setHolder(Human h){					this.holder = h;					}
	public void setLineage(Human[] l){				this.lineage = l.clone();			}
	public void setLineal(boolean v){				this.lineal = v;					}
	public boolean isLineal(){						return this.lineal;					}
	public void setOffice(Office o){				this.office = o;					}
	public void setOrigin(Holder h){				this.origin = h;					}
	public void setPredecessor(Holder h){
		this.predecessor = h;
	}
	public void setSpecial(int s){					this.special = s;					}
}
