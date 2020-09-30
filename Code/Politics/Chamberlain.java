package Code.Politics;
import Code.Politics.Minister;
import Code.Human.Human;

public class Chamberlain extends Minister {

  public Chamberlain(Cabinet c, Human p){
    super(c, p);
    this.title = "chamberlain";
	}

  @Override
  public void replace(){
    Minister m = this.getCabinet().findChamberlain();
    this.getCabinet().setChamberlain(m);
  }

}
