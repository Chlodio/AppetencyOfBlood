/*Currently unused intended to make economy-system more detailed*/

package Common;
import Human.Human;
import java.util.ArrayList;
import java.util.List;

public class ManorLord {
	private static List<ManorLord> list = new ArrayList<>();
	private int manors;
	private Human owner;

	public ManorLord(Human h, int m){
		this.owner = h;
		this.manors = m;
		list.add(this);
	}

	public ManorLord(Human h, int m, int a){
		this.owner = h;
		this.manors = m;
		this.getOwner().getPaid(a);
		list.add(this);
	}

	public void collectRent(){
		this.owner.getPaid(this.manors);
	}

	public void inherit(int m, int f){
		this.manors = m;
		this.owner.getPaid(f);
	}

	/*Is there is enough inheritance for everyone*/
	public boolean isPlenty(int n){
		return this.manors >= n;
	}

	/*Get number sons that will inherit*/
	public int getHeirs(int n){
		for(int x = n; x >= 0; x++){
			if ((this.manors/x) != 0){
				return x;
			}
		}
		return 0;
	}

	public boolean hasRemainder(int s){
		return (this.manors-((this.manors/s)*s)) != 0;
	}

	public int getRemainderManors(int s){
		return this.manors-((this.manors/s)*s);
	}

	public int getRemainderFund(int s){
		return this.getFund()-((this.getFund()/s)*s);
	}

	public void handleRemainder(Human h, int s){
		h.getManorLord().inherit(this.getRemainderManors(s), this.getRemainderFund(s));
	}

	public int getFund(){
		return this.owner.getFund();
	}

	public Human getOwner(){
		return this.owner;
	}

	public void splitForBrothers(){
		int n;				//num of heirs
		List<Human> l = this.getOwner().getSons();
		if (!isPlenty(l.size())){
			n = this.getHeirs(l.size());
			l.subList(n, l.size()).clear();
		}
		this.splitInheritance(l);
		if (this.hasRemainder(l.size())){
			this.handleRemainder(l.get(0), l.size());
		}
	}

	public void splitInheritance(List <Human> l){
		int m = this.manors/l.size();
		int f = this.getFund()/l.size();
		for (Human x: l){
			this.bequeathTo(x, m, f);
		}
	}

	public void bequeathTo(Human p, int m, int f){
		ManorLord ml = new ManorLord(p, m);
		if (!p.isManorLord()){
			p.makeIntoManorLord(ml);
		} else {
			p.getManorLord().inherit(m, f);
		}
	}

	public void depart(){
		list.remove(this);
		if (this.getOwner().hasSon()){
			if (this.getOwner().hasSons()){
				this.splitForBrothers();
			} else {
				this.bequeathTo(this.getOwner().getLivingSon(), this.manors, this.getFund());
			}
		} else {
			if (this.getOwner().hasBrother()){

			}
		}
	}

}
