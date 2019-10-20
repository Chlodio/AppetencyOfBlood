import java.util.ArrayList;

public class Courtier {
	private Court court;
	private Human person;

	public Courtier(Court c, Human p) {
		this.court = c;
		this.person = p;
	}

	public static void makeCourtier(Court c, Human p){
		p.courtier = new Courtier(c, p);
		c.addCourtier(p.courtier);
	}

	public Human getPerson(){								return this.person;	}

}
