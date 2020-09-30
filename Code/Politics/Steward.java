package Code.Politics;
import Code.Politics.Minister;
import Code.Human.Human;

public class Steward extends Minister {

  public Steward(Cabinet c, Human p){
    super(c, p);
    this.title = "steward";
	}

  @Override
  public void replace(){
    Minister m = this.getCabinet().findSteward();
    this.getCabinet().setSteward(m);
  }

}
