import java.util.ArrayList;

class Succession {
 	public static Human heir;
	public static ArrayList<Human> lineage = new ArrayList<>();
	protected static boolean coverture;
	protected static Holder origin;
	protected static Human ancestor;
	protected static int blood;
	protected static int special;
	public static void inform(){
		Claim.temp = new Claim();
		Claim.temp.setBlood(blood);
		Claim.temp.setLineage((ArrayList) lineage.clone());
		Claim.temp.setCoverture(coverture);
		Claim.temp.setSpecial(special);
		Claim.temp.setHolder(heir);
		Claim.temp.setOrigin(origin);
		lineage.clear();
	}

	public static void doMaintenance(){
		Succession.heir = 	null;
		lineage.clear();
		blood = 			0;
		ancestor = 			null;
		Claim.temp = 		null;
		coverture = 		false;
		special = 			0;
		origin =			null;
	}
}
