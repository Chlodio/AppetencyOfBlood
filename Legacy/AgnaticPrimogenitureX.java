import java.util.List;

class AgnaticPrimogeniture extends Primogeniture {

	public static boolean findIndirectHeir(Human forfeiter){
		if (findHeir(forfeiter)){ return true;}
		else if (forfeiter.getFather() != null){
			if (findIndirectHeir(forfeiter.getFather()) == true){
				return true;
			}
		}
		return false;
	}
	public static boolean findIndirectHeir(Human forfeiter, Human claimGiver){
		if (findHeir(forfeiter)){ return true;}
		else if (forfeiter.getFather() != null){
			if (forfeiter != claimGiver && findIndirectHeir(forfeiter.getFather())){
				return true;
			}
		}
		return false;
	}
	//For families allows indirect relatives
	public static boolean callHeir(Human forfeiter){
		Succession.doMaintenance();
		if (findHeir(forfeiter)){ return true; }
		else if(forfeiter.getFather() != null){
			if (findIndirectHeir(forfeiter)){ return true; }
		}
		return false;
	}
	//For titles
	public static boolean callHeir(Human forfeiter, Human claimGiver){
		Succession.doMaintenance();
		if (traceHolders(forfeiter)){
			Succession.inform();
			return true;
		}
		return false;
	}


	public static boolean traceHolders(Human forfeiter){
		for (int x = Realm.getHolderList(0).size()-1; x >= 0; x--){
			if (findHeir(Realm.getHolderList(0).get(x).getPerson())){
				Succession.origin = 	Realm.getHolderList(0).get(x);
				Succession.ancestor = 	Realm.getHolderList(0).get(x).getPerson();
				Succession.lineage.add(Succession.ancestor);
				return true;
			}
		}
		return false;
	}

	public static boolean findHeir(Human deceased){
		if (deceased.isAdult()){
			List<Human> l = deceased.getSons();
			for(Human x: l){
				if (x.isLegimate()){
					if (x.isAlive()){
						Succession.heir = x;
						Succession.lineage.add(x);
						return true;
					}
					else if (findHeir(x)){
						Succession.lineage.add(x);
						return true;
					}
				}
			}
		}
		return false;
	}
}
