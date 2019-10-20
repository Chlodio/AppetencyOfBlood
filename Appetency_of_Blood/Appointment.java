class Appointment extends Succession {
	public static boolean callHeir(Human appointer, Human appointed){
		Succession.doMaintenance();
		Succession.heir = 			appointed;
		Succession.special =		1;
		Succession.inform();
		return true;
	}
}
