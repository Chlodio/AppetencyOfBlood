public class Debt{
	private Office owner; private double amount;

	Debt(Office off){
		this.owner = off;
		this.amount = amount;
	}

	public Office getOwner(){ 									return this.owner; }

	public double getAmount(){ 									return this.amount; }

	public double getInterest(){
		return  Math.round((this.amount*0.1)*100.0)/100.0;
	}

	/*What percentage of revenue goes to paying off interest*/
	public double getInterestPer(){
		return this.getInterest()/this.owner.getTerritory().getRevenue();
	}

	public void handleInterest(){
		this.owner.getFunds().submitPayment(this.getInterest());
	}

	public void give(double amount){
		this.amount = Math.round((this.amount+amount)*100.0)/100;
	}

	/*In order to limit amount of debt, more than ten times of revenue shouldn't be taken*/
	public boolean isBelowDebtLimit(){
		return (this.owner.getNet()*10) > this.amount;
	}

	public void pay(){
		double payoff;
		if (this.owner.getFunds().canAffordCost(this.amount)){
			payoff = this.amount;			//Whole payment
		} else {
			//Partial payment (everything that can)
			payoff = this.owner.getFunds().getFunds();
			if(this.owner.isGearingUp()){
				this.owner.endGearingUp();
				System.out.println("Gearing up was canceled by smart economist.");
			}
		}
		this.amount = Math.round((this.amount-payoff)*100.0)/100.0;
		this.owner.getFunds().submitPayment(payoff);
	}

	//None should take a loans if the new interest would be more than the revenue
	public boolean canAffordPossibleInterest(double loan){
		return this.owner.getTerritory().getRevenue() > ((this.amount+loan)*0.1);
	}

}
