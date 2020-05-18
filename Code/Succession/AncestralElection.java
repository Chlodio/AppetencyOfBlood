package Code.Succession;
import Code.Politics.Office;
import Code.Human.Human;
import Code.Common.Basic;
import Code.Ancestry.Claim;
import Code.Politics.Office;
import Code.Ancestry.Lineage;
import java.util.List;

public class AncestralElection extends Succession {

  public AncestralElection(Lineage l){
		super(l);
	}

	public int determine(){
    List<Integer> l = Human.getMajorsInt(this.getOffice().getClaimants());
    if (l.size() > 0){
      Claim c = this.getOffice().getClaims().get(Basic.choice(l));
      this.setHeir(c.getHolder());
      this.setLineage(c.getLineage());
      if (c.getLineage().length == 2){
        setLineal(true);
      }
      return 1;
    }
    return 3;
  }


}
