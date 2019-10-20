public class InterestImperialism extends InterestType{
	public void consider(Office off){
		chooseAction(off);
	}

	public void chooseAction(Office off){
		switch(Main.randint(2)){
			case 0:
				InterestArchitecture.buildFort(off);
				break;
			case 1:
				prepareForWar(off);
				break;
		}
	}

	public static void prepareForWar(Office off){
		if (!off.isGearingUp()){
			if (off.getFunds().submitPayment(5000.0)){
				startConquest(off);
			}
			else{
				off.beginGearingUp();
			}
		}
	}

	public static void startConquest(Office off){
		off.goToWar();
	}

	public static void handleSiege(Office off){
		off.getMilitary().getFunds().submitPayment(5000.0);
		off.getRuler().loseRenown(1);
		if (Main.randint(3) == 0){
			winConquest(off);
		}
		else if (off.getFunds().canAffordCost(5000.0) || Main.randint(3) == 0){
			loseConquest(off);							//give up
		}
	}

	public static void winConquest(Office off){
		off.getRuler().gainRenown(10);
		off.getTerritory().expand();
		off.endWar();
	}

	public static void loseConquest(Office off){
		off.getRuler().gainRenown(-20);
		off.endWar();
	}

}
