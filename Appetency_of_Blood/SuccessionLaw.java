public class SuccessionLaw{
	private Succession succession;
	private int coverture;

	public SuccessionLaw(){
		this.succession = new AgnaticCognaticPrimogeniture();
		this.coverture = 0;
	}
}
