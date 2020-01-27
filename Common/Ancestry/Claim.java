package Ancestry;
import Human.Human;
import Common.Basic;
import Common.Writing;
import Politics.Office;
import Politics.Holder;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class Claim{
	private Human[] lineage;
	private boolean coverture;									//If held via jure uxoris
	private Holder origin;										//Original holder
	private Human holder;										//Lhe owner of the claim
	private int blood; 											//I.e. 0 = agnatic/1 = cognatic
	private int special;										//I.e. 0 = regular/1 = elected
	private Office office;										//The office claim belongs to
	private Holder predecessor;									//Last predecessor
	private boolean lineal;										//Lineal descendant predecessor

	public final static String[] kinship = {"Agnatic", "Patrilineal", "Cognatic"};
	public static Claim temp;									//Stored in succession



	public static Claim getTemp(){
		Claim it = temp;
		return it;
	}


//Here lies HTML stuff used for docomentation

	/*If claim is special it must be elected-type and not hereditary which is more complex claim-type*/
	public String getClaimHTML(){

		if (this.isSpecial()){
			return getElectedType();
		} else{
			return getHereditaryType();
		}
	}

	//If there is less than four ancestors the relation to ruler is considered distant, otherwise it's close relative
	private String getHereditaryType(){
		if (Basic.isLessThan(this.getLineageLength(), 5)){
			return this.getCloseRelativeType();
		} else {
			return this.getDistantRelativeType();
		}
	}

	//To figure out if the ruler gained their claim to the rulership by their blood or by marriage
	private String getCloseRelativeType(){

		if (!this.isCoverture()){
			return this.getBloodType();
		} else{
			return this.getAffinityType();
		}

	}

	//Short for blood-related type in contrast to non-related Affinity type
	private String getBloodType(){

		/*Additional information, secondary claim*/
		if (this.isLineal()){
			return Writing.addUlLi(this.getPrefix());
		} else{
			if (!this.lineageContainsWomen()){
				return getUnilinealType();
			} else {
				return this.getAmbilinealType();
			}
		}
	}

	//As opposed to getAmbilinealType, intended to be more informative
	private String getUnilinealType(){
		String txt = Writing.addLi(this.getPrefix());
		Human p = this.getPredecessor().getPerson();							//predecessor
		String r = Consanguinity.getPaternalRelation(p, this.getHolder());
		r = Basic.capitalize(r);
		r += " of "+p.getFullName();
		r = Writing.addLi(r);
		return Writing.addUl(txt+r);
	}

	//If the lineage includes women tracing gets bit more complicated so instead of dealing with mess, were are going to simply things just a bit
	private String getAmbilinealType(){
		String txt = this.getPrefix();
		Human p = this.getPredecessor().getPerson();					//predecessor
		Human h = this.getHolder();
		String rs = Consanguinity.getCognaticRelation(p, h);			//daughter/grandaughter, etc

		//If rs is "" don't bother adding anything
		if (Basic.isNotZero(rs.length())){
			txt = Writing.addLi(txt);
			txt += Writing.addLi(Basic.capitalize(rs)+" of "+p.getFullName());
		}

		/*Intended result being something like:
			*Son of Alphon		[direct relation]
			*Brother of Bob		[indirect relation to predecessor]
		*/

		return Writing.addUlLi(txt);
	}

	//Brother- and-son-in-laws of the ruler
	private String getAffinityType(){
		String r = this.getHolder().getRelation(this.getLineageLength());	//son/brother,etc
		r = Basic.capitalize(r);
		r += "-in-law of ";
		r += this.getUltimateAncestor().getFullName();						//e.g. Bob XI
		return Writing.addUlLi(r);
	}

	//As opposed to CloseRelativeType
	private String getDistantRelativeType(){
		int l = this.getLineageLength();
		String txt = this.getKinshipTypeStr()+" "+Basic.getOrdial(l-1);
		txt = txt+" generation descendant of "+getThroughLine(l);
		if (!this.isCoverture()){
			return Writing.addUlLi(txt);
		} else{
			txt = Basic.toLowerCase(txt);
			return Writing.addUlLi("Spouse of "+txt);
		}
	}

	/*As opposed to hereditary type*/
	public String getElectedType(){
		return Writing.addUlLi("Elected");
	}

	//return blood type as string
	public String getKinshipTypeStr(){
		return kinship[this.blood];
	}

	//If lineage has certain amount of links, get long form, if not just the name of the ancestor
	private String getPrefix(){
		int ll = this.getLineageLength();		//lineage length
		Human h = this.getHolder();
		String s = h.getRelation(ll-2);
		s = Basic.capitalize(s);
		s += " of ";
		if (Basic.isAtLeast(ll, 3)){
			return s+getThroughLine(ll);	//e.g 'Grandson of Walkelin, through his son, Benedict'
		} else{
			return s+this.getUltimateAncestor().getFullName();		//e.g "Son of Bob"
		}
	}

	/*Used as a suffix and should result in things like 'Walkelin, through his son, Benedict, 1st Prince'
	*/
	private String getThroughLine(int i){
		Human a1 =  this.getUltimateAncestor();
		Human a2 =	this.getPenultimateAncestor();
		String s =	a1.getFullName()+", through ";
		s +=		a1.getPossessive()+" "+a2.getOffspring();
		s +=		", "+a2.getFullName();
		return s;
	}


//Mechanical stuff

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

	//Check if the lineage contains a female
	public boolean lineageContainsWomen(){
		for(Human x: this.lineage){
			if (x.isFemale()){
				return true;
			}
		}
		return false;
	}


	//Setting ruler's predecessor
	public void setPredecessor(Holder h){
		this.predecessor = h;
	}

	//Getting ruler who is the ancestor and basis for the claim
	public Human getUltimateAncestor(){
		return lineage[0];
	}

	//Getting ultimate ancestor's child
	public Human getPenultimateAncestor(){
		return lineage[1];
	}

	public boolean hasOrigin(){						return this.origin != null;			}
	public boolean isCoverture(){					return this.coverture; 				}
	public boolean isLineageOfHeight(int v){		return this.lineage.length >= v; 	}
	public Human[] getLineage(){					return this.lineage; 				}
	public int getLineageLength(){					return this.lineage.length;			}
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
	public void setSpecial(int s){					this.special = s;					}
}
