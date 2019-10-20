public class Treasury {
	private Office owner;
	private double funds;
	private Debt debt;

	Treasury(Office off, Debt debt){
		this.owner = off;
		this.funds = 0.0;
		this.debt = debt;
	}

	public Office getOwner(){ 							return this.owner; 	}

	public double getFunds(){ 							return this.funds; 	}

	public boolean canAffordCost(double amount){
		return this.getFunds() >= amount;
	}

	public void receivePayment(double amount){
		this.funds = Math.round((this.funds+amount)*100.0)/100.0;;
	}

	public boolean submitPayment(double amount){
		if(this.canAffordCost(amount)){
			this.funds = Math.round((this.funds-amount)*100.0)/100.0;
			return true;
		}
		else if (this.owner.getDebt().isBelowDebtLimit() && this.owner.getDebt().canAffordPossibleInterest(amount)){
			this.takeLoan(amount);
			this.submitPayment(amount);
			return true;
		}
		return false;
	}

	public void takeLoan(double amount){
		this.receivePayment(amount);
		this.debt.give(amount);
	}
}
