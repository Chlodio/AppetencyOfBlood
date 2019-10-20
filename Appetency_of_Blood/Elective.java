class Elective extends Succession {
	public static boolean callHeir(){
		Succession.doMaintenance();
		Succession.heir = 			House.getMagnates().get(0);
		Succession.special =		1;
		Succession.inform();
		return true;
	}
}
