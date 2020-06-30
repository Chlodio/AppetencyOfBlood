package Code.Ancestry;
import Code.Human.Human;
import Code.Common.Basic;
import Code.Common.Writing;
import Code.Common.HTML;
import Code.Succession.Succession;
import Code.Politics.Office;
import Code.Politics.Holder;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class Claim  implements Cloneable{
	private Human[] lineage;
	private boolean coverture;							//If held via jure uxoris
	private Holder origin;									//Original holder
	private Human holder;										//Lhe owner of the claim
	private int blood; 											//I.e. 0 = agnatic/2 = cognatic
	private int special;										//I.e. 0 = regular/1 = elected
	private Office office;									//The office claim belongs to
	private Holder predecessor;							//Last predecessor
	private boolean lineal;									//Lineal descendant predecessor

	public final static String[] kinship = {"Agnatic", "Quasi-agnatic", "Cognatic"};
	public static Claim temp;								//Stored in succession

	public Claim(){
	}

	public Claim(Human[] h, Office o){
		this.lineage = h;
		this.office = o;
		this.holder = h[h.length-1];
	}

	public Claim(Human[] h, Claim c){
		this.lineage = h;
		this.office = c.getOffice();
		this.holder = h[h.length-1];
		this.blood = c.getBlood();
	}

	public static Claim getTemp(){
		Claim it = temp;
		return it;
	}

	public static int[] getClaimLineageNum(List<Claim> a){
		int[] i = new int[a.size()];
		for(int x = 0; x < a.size(); x++){
			i[x] = a.get(x).getLineageLength();
		}
		return i;
	}

	public static Human[] makeLineage(Human[] a, Human h){
		Human[] n = Arrays.copyOf(a, a.length+1);
		n[n.length-1] = h;
		return n;
	}



	public void pass(){
		if (this.holder.isAdult()){
			List<Human> l = this.holder.getLegitSons();
			try {
				Claim c = (Claim) this.clone();
				if (this.holder.isFemale()){
					c.diludeBlood();
				}
				for(Human x: l){
					if (x.isAlive()){
						x.addClaim(new Claim(makeLineage(this.getLineage(), x), c));
					} else if (x.isAdult() && x.hasSon()){
						Claim cn = new Claim(makeLineage(this.getLineage(), x), c);
						x.addClaim(cn);
						x.passClaims();
						x.removeClaim(cn);
					}
				}
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
		}
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
			return HTML.getUlLi(this.getPrefix());
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
		String txt = HTML.getLi(this.getPrefix());
		Human p = this.getPredecessor().getPerson();							//predecessor
		String r = Affinity.getPaternalRelation(p, this.getHolder());
		r = Basic.capitalize(r);
		r += " of "+p.getFullName();
		return HTML.getUl(txt+HTML.getLi(r));
	}

	//If the lineage includes women tracing gets bit more complicated so instead of dealing with mess, were are going to simply things just a bit
	private String getAmbilinealType(){
		String txt = HTML.getLi(this.getPrefix());
		Human p = this.getPredecessor().getPerson();					//predecessor
		Human h = this.getHolder();
		String rs = Affinity.getCognaticRelation(p, h);			//daughter/grandaughter, etc

		//If rs is "" don't bother adding anything
		if (Basic.isNotZero(rs.length())){
			txt += HTML.getLi(Basic.capitalize(rs) + " of " + p.getFullName());
		}

		/*Intended result being something like:
			*Son of Alphon		[direct relation]
			*Brother of Bob		[indirect relation to predecessor]
		*/
		return HTML.getUl(txt);
	}

	//Brother- and-son-in-laws of the ruler
	private String getAffinityType(){
		String r = this.getHolder().getRelation(this.getLineageLength());	//son/brother,etc
		Basic.capitalize(r);
		r += "-in-law of "+this.getUltimateAncestor().getFullName();						//e.g. Bob XI
		return HTML.getUlLi(r);
	}

	//As opposed to CloseRelativeType
	private String getDistantRelativeType(){
		int l = this.getLineageLength();
		StringBuffer txt = new StringBuffer(this.getBloodTypeStr());
		txt.append(" ").append(Basic.getOrdial(l-1));
		txt.append(" generation descendant of ").append(getThroughLine(l));
		if (!this.isCoverture()){
			return HTML.getUlLi(String.valueOf(txt));
		} else{
			txt = Basic.toLowerCase(txt);
			return HTML.getUlLi(String.valueOf(txt.insert(0, "Spouse of ")));
		}
	}

	/*As opposed to hereditary type*/
	public String getElectedType(){
		return HTML.getUlLi("Elected");
	}

	//Decrease the qyality of blood
	public void diludeBlood(){
		switch(this.blood){
			case 0:
				this.setBlood(1);
				return;
			case 1:
				this.setBlood(2);
				return;
			default:
				return;
		}
	}

	//Set set blood
	public void setBlood(int b){
		this.blood = b;
	}

	//Return blood type as string
	public String getBloodTypeStr(){
		return kinship[this.blood];
	}

	public int getBlood(){
		return this.blood;
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
			return s += this.getUltimateAncestor().getFullName();		//e.g "Son of Bob"
		}
	}

	/*Used as a suffix and should result in things like 'Walkelin, through his son, Benedict, 1st Prince'
	*/
	private String getThroughLine(int i){
		Human a1 =  this.getUltimateAncestor();
		Human a2 =	this.getPenultimateAncestor();
		StringBuffer s = new StringBuffer(a1.getFullName());
		s.append(", through ");
		s.append(a1.getPossessive()).append(" ").append(a2.getOffspring());
		s.append(", ").append(a2.getFullName());
		return String.valueOf(s);
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

	//Creates a new permanent claim based on data from Lineage and Succession, called from Holder
	public void combine(Holder h, Office o){
		this.setSpecial(0);								//Claim is non-elected
		this.setLineage(Lineage.getLineage());			//Get list of ancestors related to the claim
		this.setLineal(Lineage.getLineal());			//If claim holder is direct descended of pred
		this.setBlood(Succession.getBlood());			//If the claim included some women
		this.setPredecessor(o.getLastHolder());			//The rulers predecessor
		this.setOffice(o);								//What office the claim belongs to
		this.setHolder(h.getPerson());					//What poltical figure does the claim belong to
	}

	public String getLineageString(){
		StringBuffer s = new StringBuffer("");
		for(int x = 0; x < this.lineage.length-1; x++){
			s.append(this.lineage[x].getFormalName()).append(" -> ");
		}
		s.append(this.lineage[this.lineage.length-1].getFormalName());
		return String.valueOf(s);
	}

	public Object clone() throws CloneNotSupportedException {
			return super.clone();
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
	public void setCoverture(boolean c){			this.coverture = c;					}
	public void setHolder(Human h){					this.holder = h;					}
	public void setLineage(Human[] l){				this.lineage = l.clone();			}
	public void setLineal(boolean v){				this.lineal = v;					}
	public boolean isLineal(){						return this.lineal;					}
	public void setOffice(Office o){				this.office = o;					}
	public void setOrigin(Holder h){				this.origin = h;					}
	public void setSpecial(int s){					this.special = s;					}
}
