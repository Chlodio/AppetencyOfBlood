public class InterestScholarship extends InterestType{

	public void consider(Office off){
		switch(Main.randint(2)){
			case 0:
				InterestArchitecture.buildSchool(off);
				break;
			case 1:
				InterestArchitecture.buildScriptorium(off);
				break;
			case 2:
				InterestArchitecture.buildCollega(off);
				break;
		}
	}

}
