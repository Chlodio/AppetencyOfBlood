class Elective {
	public static boolean callHeir(){
		Succession.doMaintenance();
		Succession.heir = 			Basic.choice(House.getMagnates());
		Succession.special =		1;
		Succession.inform();
		return true;
	}
}
