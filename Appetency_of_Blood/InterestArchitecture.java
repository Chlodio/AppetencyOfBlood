public class InterestArchitecture extends InterestType{
	public void consider(Office off){
		chooseAction(off);
	}

	public void chooseAction(Office off){
		switch(Main.randint(9)){

			case 0:
				buildPriory(off);
				break;
			case 1:
				buildAbbey(off);
				break;
			case 2:
				buildScriptorium(off);
				break;
			case 3:
				buildTemple(off);
				break;
			case 4:
				buildCathedral(off);
				break;
			case 5:
				buildSchool(off);
				break;
			case 6:
				buildCollega(off);
				break;
			case 7:
				buildFort(off);
				break;
			case 8:
				buildMonument(off);
				break;
		}
	}


//Educational buildings

	public static boolean buildSchool(Office off){
		off.addProject(new Project(3, 5, 100));
		return true;
	}

	public static boolean buildCollega(Office off){
		Territory t = off.getTerritory();
		if (t.getSchools()/(t.getCollegas()+1) >= 5){
			off.addProject(new Project(4, 10, 100));
			return true;
		}
		else if(buildSchool(off)){						return true;	}
		return false;
	}

//Religious buildings

	public static boolean buildPriory(Office off){
		off.addProject(new Project(5, 5, 50));
		return true;
	}

	public static boolean buildAbbey(Office off){
		Territory t = off.getTerritory();
		if (t.getPriories()/(t.getAbbeys()+1) >= 10){
			off.addProject(new Project(6, 10, 75));
			return true;
		}
		else if(buildPriory(off)){
			return true;
		}
		return false;
	}

	public static boolean buildTemple(Office off){
		off.addProject(new Project(7, 2, 125));
		return true;
	}

	public static boolean buildCathedral(Office off){
		Territory t = off.getTerritory();
		if (t.getTemples()/(t.getCathedrals()+1) >= 10){
			off.addProject(new Project(8, 25, 100));
			return true;
		}
		else if(buildTemple(off)){			return true; }
		return false;
	}

	public static boolean buildScriptorium(Office off){
		Territory t = off.getTerritory();
		if (t.getPriories() > t.getScriptoriums()){
			off.addProject(new Project(2, 1, 125));
			return true;
		}
		return false;
	}

	public static boolean buildFort(Office off){
		off.addProject(new Project(1, 10, 1000));
		return true;
	}

	public static boolean buildMonument(Office off){
		off.addProject(new Project(9, 50, 500));
		return false;
	}
}
