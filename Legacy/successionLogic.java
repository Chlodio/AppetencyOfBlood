public class successionLogic {
	private static int primary;
	private static int gender;
	public boolean determineSuccession(Human h){
		if (h.hasSibling()){
			if (h.hasChildren()){
				if (h.hasBrother()){
					if (h.hasSon()){
						if (h.hasAdultBrother()){
							if (h.hasAdultSon()){
								int numOfAdBros = h.getNumOfAdultBrothers();
								int numOfAdSons = h.getNumOfAdultSons();
								int numOfHeirs 	= numOfAdBros+numOfAdSons;
								int rng 		= Main.randint(numOfHeirs);
								if (rng > numOfAdBros){
									if (numOfAdBros == 1){

									}
								}
							}
						}
					}
				}
			}
		}
		}
	}

	public void setPrimary(int v){
		primary = v;
	}

}


public boolean proximityOfBlood(){
	Human c;
	if (h.hasLinearOffspring()){
		c = h.getLinearHeir();
		return true;
	}
	if (h.hascollateralDescendant()){

	}
	else if (h.hadSiblings()){

}

public Human getLinearDescendant(){
	if (this.hadChildren()){
		if (this.hasChildren()){
			return this.getOldestChild();
		} else if (this.isAdult()){
			List<Human> l = this.getLegimateChildren();
			List<Human> lg = new ArrayList<>();
			for (Human x: l){
				if (x.isAdult()){
					if (x.hasChild()){
						return x.getOldestChild();
					} else if (x.hadChild()){
						lg.add(x);
					}
				}
			}
		}
	} else{
		return false;
	}
}

public boolean hasLinearDescendant(){
	if (this.hadChildren()){
		if (this.hasChildren()){
			return true;
		} else if (this.isAdult()){
			List<Human> l = this.getLegimateChildren();
			for (Human x: l){
				if (x.hasLinearOffspring()){
					return true;
				}
			}
		}
	} else{
		return false;
	}
}

public boolean hascollateralDescendant(){
	if (this.hadSibling()){
		if (this.hasSibling()){
			return true;
		} else {
			List<Human> l = this.getLegimateSiblings();
			if (x.hasLinearOffspring()){
				return true;
			}
		}
	} else {
		Human h = this;
		while(h.hadFather()){
			h = this.getFather();
			List<Human> l = this.getLegimateSiblings();
			for (Human x:l){
				if (h.hasLinearOffspring()){
					return true;
				}
			}
		}
	}
	return false;
}




/*PROXIMITY OF BLOOD




*/

/* PRIME BRANCHES

	vertical
		geniture
			primogeniture
			ultimogeniture
		proximity
			old proximity
			new proximity
	horizontal
		generational
			seniority
			juniority

*/

/*

	ambilineal
		patrilineal (sons before daughters)
			agnatic
		matrilineal

		bilateral
			cognatic


*/
