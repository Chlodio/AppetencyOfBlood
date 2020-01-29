package Code.Court;
import Code.Human.Human;
import java.util.ArrayList;

public class Courtier {
	private Court court;
	private Human person;

	public Courtier(Court c, Human p) {
		this.court = c;
		this.person = p;
	}

	public static void makeCourtier(Court c, Human p){
		p.setCourtier(new Courtier(c, p));
		c.addCourtier(p.getCourtier());
	}

	public Human getPerson(){								return this.person;	}

}
