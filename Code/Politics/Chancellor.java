package Code.Politics;
import Code.Politics.Minister;
import Code.Human.Human;

public class Chancellor extends Minister {

  public Chancellor(Cabinet c, Human p){
    super(c, p);
    this.title = "chancellor";
	}

  @Override
  public void replace(){
    Minister m = this.getCabinet().findChancellor();
    this.getCabinet().setChancellor(m);
  }

}
