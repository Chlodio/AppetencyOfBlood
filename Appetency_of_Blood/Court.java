import java.util.ArrayList;

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
		for(Human x: House.getMagnates()){
			Courtier.makeCourtier(this, x);
		}
	}

	public void addCourtier(Courtier c){
		this.courtiers.add(c);
	}

}
