package Code.Politics;
import Code.Politics.Minister;
import Code.Human.Human;

public class Constable extends Minister {

  public Constable(Cabinet c, Human p){
    super(c, p);
    this.title = "constable";
	}

  @Override
  public void replace(){
    Minister m = this.getCabinet().findConstable();
    this.getCabinet().setConstable(m);
  }

}
