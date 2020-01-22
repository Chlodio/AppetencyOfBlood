package Interest;
import Common.Basic;
import Politics.Office;
import Politics.Realm;
public class InterestTheology extends InterestType{
	public void consider(Office off){			chooseAction(off); }

	public void chooseAction(Office off){
		switch(Basic.randint(7)){
			case 0:
				InterestArchitecture.buildPriory(off);
				break;
			case 1:
				InterestArchitecture.buildAbbey(off);
				break;
			case 2:
				InterestArchitecture.buildTemple(off);
				break;
			case 4:
				InterestArchitecture.buildCathedral(off);
				break;
			case 5:
				giveAlms(off);
				break;
			case 6:
				off.lowerTax();
				off.getRuler().gainRenown(1);
				break;
		}
	}

	public void giveAlms(Office off){
		if (off.getFunds().submitPayment(((Realm) off.getTerritory()).getAlms())){
			((Realm) off.getTerritory()).losePoverty(0.05f);
			off.getRuler().gainRenown(10);
		}
	}

}
