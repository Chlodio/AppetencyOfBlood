package Code.History;
import Code.Politics.Minister;

public class CabinetRegister {
  private Minister[] stewards;
	private Minister[] constables;
	private Minister[] chancellors;
	private Minister[] chamberlains;

  public CabinetRegister(){
    this.stewards = new Minister[0];
    this.constables = new Minister[0];
    this.chancellors = new Minister[0];
    this.chamberlains = new Minister[0];
  }

  public Minister[] recordMinister(Minister m, Minister[] a){
    Minister[] n = new Minister[a.length+1];
		System.arraycopy(a, 0, n, 0, a.length);
		a = n;
		a[a.length-1] = m;
    return a;
  }

  public void recordSteward(Minister m){
    this.stewards = this.recordMinister(m, this.stewards);
  }

  public void recordChamberlain(Minister m){
    this.chamberlains = this.recordMinister(m, this.chamberlains);
  }

  public void recordChancellor(Minister m){
    this.chancellors = this.recordMinister(m, this.chancellors);
  }

  public void recordConstable(Minister m){
    this.constables = this.recordMinister(m, this.constables);
  }

  public String getHTML(){
    String s = "";
    for (Minister x: this.stewards){
      s += x.getPerson().getBirthName()+" "+x.getTenure()+" "+x.getRank()+" "+x.getPerson().getLifespan()+"\n";
    }
    return s;
  }

  public Minister[] getStewards(){
    return this.stewards;
  }

  public Minister[] getConstables(){
    return this.constables;
  }

  public Minister[] getChancellors(){
    return this.chancellors;
  }

  public Minister[] getChamberlains(){
    return this.chamberlains;
  }
}
