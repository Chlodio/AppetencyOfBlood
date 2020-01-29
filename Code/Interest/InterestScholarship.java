package Code.Interest;
import Code.Politics.Office;
import Code.Common.Basic;
public class InterestScholarship extends InterestType{

	public void consider(Office off){
		switch(Basic.randint(2)){
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
