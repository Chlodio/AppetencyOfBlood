class AgnaticCognaticPrimogeniture extends Primogeniture {
	public static boolean callHeir(Human forfeiter, Human claimGiver){
		Succession.doMaintenance();
		if (AgnaticPrimogeniture.callHeir(forfeiter, claimGiver) ){ return true; }
		else if (traceHolders(forfeiter)){
			Succession.inform();
			return true;
		}
		return false;
	}

	//Skip the daughters
	public static boolean findHeir(Human deceased){
		if (deceased.isAdult()){
			for(Human x: deceased.sons){
				if (x.isAlive()){
					Succession.heir = x;
					Succession.lineage.add(x);
					return true;
				}
				else if (AgnaticCognaticPrimogeniture.findHeir(x)){
					Succession.lineage.add(x);
					return true;
				}
			}
			for(Human x: deceased.daughters){
				if (x.isAlive()){
					Succession.lineage.add(x);
					if(x.isMarried()){
						Succession.heir 		= x.getSpouse();
						Succession.coverture 	= true;
						//System.exit(0);
					}
					else{
						Succession.heir 		= x;
					}
					return true;
				}
				else if (AgnaticCognaticPrimogeniture.findHeir(x)){
					Succession.lineage.add(x);
					makeCognatic();
					return true;
				}
			}
		}
		return false;
	}

	public static boolean traceHolders(Human forfeiter){
		for (int x = Realm.getHolderList(0).size()-1; x >= 0; x--){
			if (findHeir(Realm.getHolderList(0).get(x).getPerson())){
				Succession.ancestor = Realm.getHolderList(0).get(x).getPerson();
				Succession.lineage.add(Succession.ancestor);
				return true;
			}
		}
		return false;
	}

	public static void makeCognatic(){
		if (Succession.blood == 1){
			Succession.blood = 2;
		}
		else{
			Succession.blood = 1;
		}
	}
}
