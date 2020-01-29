package Code.Court;

import Code.House.House;
import Code.Human.Human;
import Code.Politics.Office;
import java.util.ArrayList;
import java.util.List;

public class Court {
	private Office office;
	private ArrayList<Courtier> courtiers;
//	private Constable constable;
//  private Chancellor chancellor;
//	private Steward steward;
//  private Chamberlain chamberlain;
//  private Treasurer treasurer;
//  private Almoner almoner;


	public Court(Office off){
		this.office = off;
		this.courtiers = new ArrayList<>();
		List<Human> l = House.getMagnates();
		for(Human x: l){
			Courtier.makeCourtier(this, x);
		}
	}

	public void addCourtier(Courtier c){
		this.courtiers.add(c);
	}

}
