package Code.Interest;
import Code.Politics.Office;
import Code.Politics.Realm;
import Code.Common.Basic;
public class InterestEconomy extends InterestType{
	public void consider(Office off){
		switch(Basic.randint(2)){
			case 0:
				off.getFunds().receivePayment(off.getTerritory().getRevenue()/2.0);
				off.getRuler().loseRenown(1);
				((Realm) off.getTerritory()).gainPoverty(0.05f);
			case 1:
				off.raiseTax();
				off.getRuler().loseRenown(1);
		}
		if (off.hasDebt() && off.getFunds().canAffordCost(1.0)){
			off.getDebt().pay();
			off.getRuler().gainRenown(1);
		}
	}
}
