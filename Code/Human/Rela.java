package Code.Human;
import Code.Relationship.*;
import Code.Common.Basic;
import java.util.List;
import java.util.ArrayList;

//Short for relation
public class Rela{
	protected Human o;						//Self owner
    protected Human father;					//Sociological father
	protected Human genitor;				//Biological father
	protected Human mother;					//Sociological/biological mother
	protected Human spouse;
	protected List<Affair> affairs;
	protected List<Affair> allAffairs;
	protected List<Human> daughters;
	protected List<Human> sons;
	protected List<Human> legitSons;		//Will be included as a part of sons
	protected List<Human> legitDaughters;	//Will be included as a part of daughters
	protected List<Human> children;
	protected List<Human> realChildren;		//Sons+daughters are legal children, but not always real
	protected List<Marriage> marriages;
	protected int numOfLivingSons;			//Legimate sons

	//For starters who are adults
	public Rela(Human o){
		this.o = o;
		this.reachAdulthood();
	}

	//For born children
	public Rela(Human o, boolean b){
		this.o = o;
	}



	//Used for names
	public List<Human> getMaleAncestryGroup(){
		List<Human> l = new ArrayList<>();
		boolean pf = false;					//Had paternal grandfather
		boolean mf = false;					//Had maternal grandmother
		if (this.hadFather()){
			if (this.hadPatGrandpa()){
				l.add(this.getFathersFather());
				pf = true;
			}
			if (this.hadMatGrandpa()){
				l.add(this.getMothersFather());
				mf = true;
			}

			if (pf){
				l.addAll(this.getPatrui());
			}
			if (mf){
				l.addAll(this.getAvuncului());
			}

			l.add(this.getFather());
		}
		return l;
	}

	//Used for names
	public List<Human> getFemaleAncestryGroup(){
		List<Human> l = new ArrayList<>();
		boolean pm = false;					//Had paternal grandfather
		boolean mm = false;					//Had maternal grandmother
		if (this.hadFather()){
			if (this.hadPatGrandma()){
				l.add(this.getFathersMother());
				pm = true;

			}
			if (this.hadMatGrandma()){
				l.add(this.getMothersMother());
				mm = true;
			}
			l.add(this.getMother());

			if (pm){
				l.addAll(this.getAmitai());
			}
			if (mm){
				l.addAll(this.getMaterterai());
			}

		}
		return l;
	}


//Children

	public boolean hasChild(){
		return Human.hasLiving(this.getChildren());
	}

	public void addChild(Human f, Human m){
		f.getRela().children.add(this.o);
		m.getRela().children.add(this.o);
	}

	public List<Human> getRealChildren(){
		return this.realChildren;
	}

	public void addRealChild(Human f, Human m){
		f.getRela().realChildren.add(this.o);
		m.getRela().realChildren.add(this.o);
	}

	public boolean isRealChildOf(Human p){
		return p.getRealChildren().contains(this.o);
	}

	public List<Human> getChildren(){
		return new ArrayList<>(this.children);
	}

	public List<Human> getLivingChildren(){
		List<Human> lc = this.o.getChildren();
		return Human.getLiving(lc);
	}

	public boolean isChildOf(Human h){
		return this.father == h || this.mother == h;
	}

//Sons

	public boolean isSonOf(Human h){
		return this.father == h;
	}

	public List<Human> getSons(){				return new ArrayList<>(this.sons);			}

	public List<Human> getLegitNonPosthumousSons(){
		List<Human> l = this.getLegitSons();
		List<Human> l2 = Human.getNonPosthumous(l);
		return l2;
	}

	//Living version of the previous
	public List<Human> getLegitNonPosthumousLivingSons(){
		List<Human> l = this.getLegitLivingSons();
		l = Human.getNonPosthumous(l);
		return l;
	}

	public boolean isSonless(){
		List<Human> l = this.getSons();
		return !Human.hasLiving(l);
	}

	public boolean hasSon(){
		List<Human> l = this.getSons();
		return Human.hasLiving(l);
	}

	public boolean hasLegitSon(){
		List<Human> l = this.getLegitSons();
		return Human.hasLiving(l);
	}

	public boolean hadLegitNonPosthumousSon(){
		List<Human> l = this.getLegitSons();
		return Human.hasNonPosthumous(l);
	}

	//Living version of the previous
	public boolean hasLegitNonPosthumousSon(){
		List<Human> l = this.getLegitSons();
		l = Human.getLiving(l);
		return Human.hasNonPosthumous(l);
	}

	public boolean hasSons(){
		return this.getNumOfLivingSons() > 1;
	}

	public int getNumOfSons(){					return this.sons.size();					}
	public int getNumOfLivingSons(){			return this.numOfLivingSons;				}
	public void addLivingSon(){					this.numOfLivingSons++;						}
	public void removeLivingSon(){				this.numOfLivingSons--;						}
	public boolean hasNumOfLivingSons(int v){	return this.numOfLivingSons >= v;			}

	public List<Human> getLivingSons(){
		List<Human> l = this.getSons();					//Get all sons
		List<Human> ll = Human.getLiving(l);			//Get rid of the dead
		return ll;
	}

	public List<Human> getLegitLivingSons(){
		List<Human> l = this.getLegitSons();			//Get only legimate sons
		List<Human> ll = Human.getLiving(l);			//Get rid of the dead
		return ll;
	}

	public Human getLivingSon(){
		return Human.getFirstLiving(this.getSons());
	}


	public void addSon(Human f, Human m){
		f.getRela().sons.add(this.getOwner());
		m.getRela().sons.add(this.getOwner());
		if (this.getOwner().isLegimate()){
			f.addLivingSon();
			f.getRela().addLegitSon(this.getOwner());
			m.getRela().addLegitSon(this.getOwner());
		}
		m.addLivingSon();					//Mater semper certa est
	}

	public void addLegitSon(Human c){
		this.legitSons.add(c);
	}

	public List<Human> getLegitSons(){
		return new ArrayList<>(this.legitSons);
	}

//Daughters

	public List<Human> getDaughters(){			return new ArrayList<>(this.daughters);	}

	public List<Human> getLivingDaughters(){
		List<Human> l = this.getDaughters();					//Get all daughters
		List<Human> ll = Human.getLiving(l);					//Get rid of the dead
		return ll;
	}

	public List<Human> getLegitLivingDaughters(){
		List<Human> l = this.getLegitDaughters();				//Get only legimate daughters
		List<Human> ll = Human.getLiving(l);					//Get rid of the dead
		return ll;
	}

	public Human getLivingDaughter(){
		return Human.getFirstLiving(this.getDaughters());
	}

	public void addDaughter(Human f, Human m){
		f.getRela().daughters.add(this.getOwner());
		m.getRela().daughters.add(this.getOwner());
		if (this.getOwner().isLegimate()){
			f.getRela().addLegitDaughter(this.getOwner());
			m.getRela().addLegitDaughter(this.getOwner());
		}
	}

	public void addLegitDaughter(Human c){
		this.legitDaughters.add(c);
	}

	public List<Human> getLegitDaughters(){
		return new ArrayList<>(this.legitDaughters);
	}

//Parents

	public void setParents(Human f, Human m){
		this.father = f;
		this.mother = m;
	}


	public boolean fatherIsDead(){				return !this.getFather().isAlive(); 	}
	public Human getGenitor(){					return this.genitor;					}
	public boolean hadFather(){					return this.father != null;				}
	public boolean hadMother(){					return this.mother != null;				}
	public boolean hasFather(){					return this.getFather().isAlive();		}
	public boolean isRealBastard(){				return this.genitor != null;			}
	public boolean motherIsDead(){				return !this.getMother().isAlive(); 	}
	public Human getFather(){					return this.father; 					}
	public Human getMother(){					return this.mother; 					}
	public void setGenitor(Human h){			this.genitor = h; 						}
	public boolean isFatherOf(Human h){			return h.getRela().father == this.o;	}
	public boolean isMotherOf(Human h){			return h.getRela().mother == this.o;	}


	public boolean hasSeniorPaternalRelative(){
		if (this.hasFather()){
			return true;
		} else if (this.hasFatherOrUncle()){
			return true;
		} else{
			if (this.hasGrandpaOrUncle()){
				return true;
			}
		}
		return false;
	}

	public boolean hasFatherOrUncle(){
		if (this.hadPatGrandpa()){
			return this.getFathersFather().hasSon();
		}
		return false;
	}

	public boolean hasGrandpaOrUncle(){
		if (this.hadPatGreatGrandpa()){
			return this.getPatGreatGrandpa().hasSon();
		}
		return false;
	}

	public boolean hadParents(){	return this.hadFather() && this.hadMother();	}


//Siblings

	public boolean isFullSiblingOf(Human h){
		if (this.hadFather() && h.hadFather()){
			if (this.getFather() == h.getFather()){
				return this.getMother() == h.getMother();
			}
		}
		return false;
	}

	public List<Human> getSiblings(){
		List<Human> l = this.getFather().getChildren();
		List<Human> ll = this.getMother().getChildren();
		for(Human x: ll){
			if (!l.contains(x)){
				l.add(x);
			}
		}
		return l;
	}


	//Count living paternal siblings
	public int getNumOfLivingSiblings(){
		int n = 0;

		if (this.hadFather()){
			n += Human.countLiving(this.getBrothers());
			n += Human.countLiving(this.getSisters());
		}

		return n;
	}

	public boolean isSiblingOf(Human i){
		if (this.hadFather() && i.hadFather()){
			return this.getFather() == i.getFather() || this.getMother() == i.getMother();
		}
		return false;
	}

	public boolean areCloselyRelated(Human h){
		return this.isSiblingOf(h) && !h.isChildOf(this.o) && !this.isChildOf(h);
	}

	public boolean isIntimateWith(Human h){
		if (this.areCloselyRelated(h) || this.isLoverOf(h)){
			return true;
		}
		return false;
	}


//Brothers



	public List<Human> getBrothers(){
		List<Human> l = this.father.getSons();
		l.remove(this);								//Remove himself
		return l;
	}

	public boolean hadBrother(){				return this.getFather().getNumOfSons() > 1; }

	public boolean isBrotherOf(Human h){
		return this.father == h.getFather() || this.mother == h.getMother();
	}

	public boolean hasBrother(){
		List<Human> sons = this.getFather().getLivingSons();
		for (Human x: sons){
			if (x != this.o){
				return true;
			}
		}
		return false;
	}

	public Human getOldestBrother(){
		List<Human> sons = this.getFather().getLivingSons();
		for (Human x: sons){
			if (x != this.o){
				return x;
			}
		}
		return null;
	}

	public boolean hasAdultBrother(){
		Human brother;
		if (this.hadFather() && this.hasBrother()){
			brother = this.getOldestBrother();
			if (brother.isAdult()){
				return true;
			}
		}
		return false;
	}

	public boolean hasUnwedBrother(){
		if (this.hadFather()){
			for(Human x: this.getFather().getLivingSons()){
				if (x.isAdult() && x.isFitForMarriage()){ return true;}
			}
		}
		return false;
	}

	public Human getUnwedBrother(){
		List<Human> l = this.getFather().getSons();
		for(Human x: l ){
			if (x.isLivingAdult()){
				if (x.isFitForMarriage()){
					return x;
				}
			}
		}
		return null;
	}


//Sisters

	public List<Human> getSisters(){
		List<Human> l = this.father.getDaughters();
		l.remove(this);									//Don't count yourself
		return l;
	}

	public boolean isSisterOf(Human h){
		return this.father == h.getFather() ||	this.mother == h.getMother();
	}

	public boolean hasSister(){
		List<Human> dau = this.getFather().getLivingDaughters();
		for (Human x: dau){
			if (x != this.o){
				return true;
			}
		}
		return false;
	}

	public boolean hasUnwedSister(){
		if (this.hadFather()){
			for(Human x: this.getFather().getLivingDaughters()){
				if (x.isAdult() && x.isFitForMarriage()){
					return true;
				}
			}
		}
		return false;
	}

	public Human getUnwedSister(){
		List<Human> l = this.getFather().getDaughters();
		for(Human x: l ){
			if (x.isLivingAdult()){
				if (x.isFitForMarriage()){
					return x;
				}
			}
		}
		return null;
	}


//Marital

	public boolean wasMarried(){ 				return this.marriages.size() > 0;}

	public int getNumOfMarriages(){ 			return this.marriages.size();}

	public boolean isMarried(){ 				return this.spouse != null;	}

	public boolean isMarriedTo(Human s){ 		return this.spouse == s;	}

	public boolean isUnwed(){					return this.spouse == null;	}

	public Human getSpouse(){					return this.spouse; 		}

	public Human getLatestHusband(){
		return this.marriages.get(this.marriages.size()-1).getStag();
	}

	public Human getLatestWife(){
		return this.marriages.get(this.marriages.size()-1).getDoe();
	}

	public Marriage getLatestMarriage(){
		return this.marriages.get(this.marriages.size()-1);
	}

	public List<Marriage> getMarriages(){		return new ArrayList<>(this.marriages);	}

	public void setSpouse(Human h){				this.spouse = h;						}

	public void setSpouseNull(){				this.spouse = null;						}

	public void addMarriage(Marriage m){		this.marriages.add(m);					}

	public Marriage getMarriage(int i){			return this.marriages.get(i); 			}

	public Marriage getFirstMarriage(){			return this.marriages.get(0); 			}


//Affairs

	public boolean isLoverOf(Human h){
		if (this.isActiveAdulterer() && h.isActiveAdulterer()){
			if (this.getAffairs().contains(h)){
				return true;
			}
		}
		return false;
	}

	public boolean wasAdulterer(){
		return this.affairs != null;
	}

	public boolean isActiveAdulterer(){
		return this.affairs != null && this.affairs.size() != 0;
	}

	public boolean hasAffairs(){
		return this.affairs.size() != 0;
	}

	public void becomeAdulter(Affair a){
		this.affairs 	= new ArrayList<>();
		this.allAffairs = new ArrayList<>();
		this.addAffair(a);
	}

	public void addAffair(Affair a){
		this.affairs.add(a);
		this.allAffairs.add(a);
	}

	public void removeAffair(Affair a){		this.affairs.remove(a);						}

	public void endAffair(Affair a){		this.affairs.remove(a);						}

	public List<Affair> getAffairs(){		return new ArrayList<>(this.affairs);		}

	public List<Affair> getAllAffairs(){	return new ArrayList<>(this.allAffairs);	}

	public int getNumOfAffairs(){
		if (this.wasAdulterer()){
			return this.affairs.size();
		} else {
			return 0;
		}
	}

	public void setAffair(Affair a){
		if (this.wasAdulterer()){
			this.addAffair(a);
		} else{
			this.becomeAdulter(a);
		}
	}


	//See if there a lover has a father, used for morganatic marriages
	public boolean hasMistress(){
		List<Affair> l = this.getAffairs();
		for(Affair x: l){
			if (x.getBelle().hadFather()){
				return true;
			}
		}
		return false;
	}

	public boolean hasUnmarriedMistress(){
		List<Affair> l = this.getAffairs();
		for(Affair x: l){
			if (!x.getBelle().isMarried()){
				return true;
			}
		}
		return false;
	}

	public Human getRandomUnmarriedMistress(){
		List<Affair> l = this.getAffairs();
		List<Human> ll = new ArrayList<>(l.size());		// will be stored here

		for(Affair x: l){
			if (!x.getBelle().isMarried()){
				ll.add(x.getBelle());
			}
		}
		return Basic.choice(ll);						//Pick random amongst the filtered
	}

	public Human getRandomMistress(){
		List<Affair> l = this.getAffairs();
		List<Human> ll = new ArrayList<>(l.size());		// will be stored here

		for(Affair x: l){
			ll.add(x.getBelle());
		}
		return Basic.choice(ll);						//Pick random amongst the filtered
	}

	//Determined by happiness vs num of affairs
	public boolean canHaveLover(int h){
		return (h+this.getNumOfAffairs()) < 3;
	}

	public Human getLoverfromAffair(Affair h){	return this.o;	}		//overrided

	public List<Human> getMistresses(){
		List<Human> lh = 	new ArrayList<>();
		List<Affair> la = 	this.getAffairs();
		for(Affair x: la){
			lh.add(x.getBelle());
		}
		return lh;
	}

	public boolean hadBastard(){
		List<Human> l = this.getSons();
		for(Human x: l){
			if (!x.isLegimate()){
				return true;
			}
		}
		l = this.getDaughters();
		for(Human x: l){
			if (!x.isLegimate()){
				return true;
			}
		}
		return false;
	}

	public List<Human> getBastards(){
		List<Human> l = new ArrayList<>();
		for(Affair x: this.allAffairs){
			l.addAll(x.getOffspring());
		}
		return l;
	}

	public void clearAffairs(){					this.affairs.clear();						}


//Grandparents

	public boolean hadGrandparents(){
		return this.getFather().hadParents() && this.getMother().hadParents();
	}

	public Human[] getFathersParents(){
		Human[] l = new Human[]{this.getFathersFather(), this.getFathersMother()};
		return l;
	}

	public Human[] getMothersParents(){
		Human[] l = new Human[]{this.getMothersFather(), this.getMothersMother()};
		return l;
	}



//Paternal grandfather


	public boolean hadPatGrandpa(){
		if (this.hadFather()){
			return this.getFather().hadFather();
		}
		return false;
	}

	public Human getFathersFather(){	return this.getFather().getFather();	}

	public Human getFathersMother(){	return this.getFather().getMother();	}

	public Human[] getGrandparents(){
		Human[] l = new Human[4];
		Human[] l1 = this.getFathersParents();
		Human[] l2 = this.getMothersParents();
		for (int x = 0; x < 2; x++){
			l[x] = l1[x];
		}
		for (int x = 2; x < 4; x++){
			l[x] = l2[x-2];
		}
		return l;
	}

	public boolean hadPatGrandma(){
		if (this.hadFather()){
			return this.getFather().hadMother();
		}
		return false;
	}

//Maternal grandfather

public boolean hadMatGrandpa(){
	if (this.hadFather()){
		return this.getMother().hadFather();
	}
	return false;
}

public Human getMothersFather(){			return this.getMother().getFather(); }

public Human getMothersMother(){			return this.getMother().getMother(); }


public boolean hadMatGrandma(){
	if (this.hadFather()){
		return this.getMother().hadMother();
	}
	return false;
}

//Great-grandparents


	public Human[] getGreatGrandparents(){
		Human[] l = new Human[8];
		Human[] l1 = this.getFather().getGrandparents();
		Human[] l2 = this.getMother().getGrandparents();
		for (int x = 0; x < 4; x++){
			l[x] = l1[x];
		}
		for (int x = 4; x < 8; x++){
			l[x] = l2[x-4];
		}
		return l;
	}

	public Human[] get2ndGreatGrandparents(){
		Human[] l = new Human[16];
		Human[] l1 = this.getFather().getGreatGrandparents();
		Human[] l2 = this.getMother().getGreatGrandparents();
		for (int x = 0; x < 8; x++){
			l[x] = l1[x];
		}
		for (int x = 8; x < 16; x++){
			l[x] = l2[x-8];
		}
		return l;
	}

	public boolean hadGreatGrandparents(){
		for(Human x: this.getGrandparents()){
			if (!x.hadParents()){
				return false;
			}
		}
		return true;
	}

	public boolean had2ndGreatGrandparents(){
		for(Human x: this.getGreatGrandparents()){
			if (!x.hadParents()){
				return false;
			}
		}
		return true;
	}

	public boolean had3rdGreatGrandparents(){
		for(Human x: this.get2ndGreatGrandparents()){
			if (!x.hadParents()){
				return false;
			}
		}
		return true;
	}


//Paternal great-grandfather


	public boolean hadPatGreatGrandpa(){
		if (this.hadFather()){
			return this.getFather().hadPatGrandpa();
		}
		return false;
	}

	public Human getPatGreatGrandpa(){
		return this.getFather().getFathersFather();
	}



	public Human getPatruus(){
		Human u = this.getFathersFather();
		for(Human x: u.getLivingSons()){
			if (this.getFather() != x){
				return x;
			}
		}
		return null;
	}


//Cousins

	public boolean isFirstCousinOf(Human h){
		if (this.hadFather() && h.hadFather()){
			if (this.father.isBrotherOf(h.getFather())){
				return true;
			}
			if (this.mother.isSisterOf(h.getFather())){
				return true;
			}
			if (this.father.isSiblingOf(h.getMother())){
				return true;
			}
		}
		return false;
	}

	public boolean hasFirstCousin(){
		List<Human> l1;
		List<Human> l2;
		if (this.hadFather() && this.hadPatGrandpa()){
			l1 = new ArrayList<>(this.getFathersFather().getChildren());
			for (Human x: l1){
				if (x.isAdult() && x != this.getFather()){
					if (Human.hasLiving(x.getChildren())){
						return true;
					}
				}
			}
		} else if (this.hadMother() && this.hadMatGrandpa()){
			l1 = this.getMothersFather().getChildren();
			for (Human x: l1){
				if (x.isAdult() && x != this.getMother()){
					if (Human.hasLiving(x.getChildren())){
						return true;
					}
				}
			}
		}

		return false;
	}

	public List<Human> getLivingFirstCousins(){
		List<Human> l0 = new ArrayList<>();
		List<Human> l1;
		List<Human> l2;
		if (this.hadFather() && this.hadPatGrandpa()){
			l1 = new ArrayList<>(this.getFathersFather().getChildren());
			for (Human x: l1){
				if (x.isAdult() && x != this.getFather()){
					l2 = Human.getLiving(x.getChildren());
				}
			}
		} else if (this.hadMother() && this.hadMatGrandpa()){
			l1 = this.getMothersFather().getChildren();
			for (Human x: l1){
				if (x.isAdult() && x != this.getMother()){
					l2 = Human.getLiving(x.getChildren());
				}
			}
		}
		return l0;
	}


//Piblings


	public boolean hasPaternalPibling(){
		int n = this.getFather().getNumOfLivingSiblings();
		if (n > 1){
			return true;
		} else if (n == 1 && this.fatherIsDead()){
			return true;
		}
		return false;
	}

	public boolean hasMaternalPibling(){
		int n = this.getMother().getNumOfLivingSiblings();
		if (n > 1){
			return true;
		} else if (n == 1 && this.motherIsDead()){
			return true;
		}
		return false;
	}

	public boolean hasPibling(){
		if (this.hasFather() && this.hadPatGrandpa()){
			if (this.hasPaternalPibling() || this.hasMaternalPibling()){
				return true;
			}
		}
		return false;
	}

//Patruus	i.e paternal uncles

	//Does character's father have a living brother?
	public boolean hasPatruus(){
		if (this.hadPatGrandpa()){
			return this.getFather().hasBrother();
		}
		return false;
	}


	public List<Human> getPatrui(){
		List<Human> list = this.getFathersFather().getSons();
		list.remove(this.getFather());
		return list;
	}


//Mother's brothers
	public List<Human> getAvuncului(){
		List<Human> list = this.getMothersFather().getSons();
		list.remove(this.getFather());
		return list;
	}

//Father's sisterss

public List<Human> getAmitai(){
	List<Human> list = this.getFathersFather().getDaughters();
	list.remove(this.getMother());
	return list;
}

//Mother's sisters

public List<Human> getMaterterai(){
	List<Human> list = this.getMothersFather().getDaughters();
	list.remove(this.getMother());
	return list;
}

//Nephew

	public boolean isNephewOf(Human h){
		if (h.hadFather()){
			List<Human> l = h.getSiblings();
			for (Human x: l){
				if (h.isChildOf(x)){
					return true;
				}
			}
		}
		return false;
	}

	public boolean isPaternalNephewOf(Human h){
		List<Human> l = h.getBrothers();
		for (Human x: l){
			if (h.isSonOf(x)){
				return true;
			}
		}
		return false;
	}

	public boolean hasPaternalNephew(){
		List<Human> uncles;
		if (this.hasPatruus()){
			uncles = new ArrayList<>(this.getPatrui());
			for(Human x: uncles){
				if(x.isAdult()){
					if (x.hasSon()){
						return true;
					}
				}
			}
		}
		return false;
	}

	public Human getPaternalNephew(){
		List<Human> uncles = new ArrayList<>(this.getPatrui());
		List<Human> l;
		for(Human x: uncles){
			if (x.isAdult()){
				l = x.getSons();
				for(Human y: l ){
					if(y.isAlive()){
						return y;
					}
				}
			}
		}
		return null;
	}

//Other

	public void reachAdulthood(){
		this.marriages = 			new ArrayList<>();
		this.sons = 				new ArrayList<>();
		this.daughters = 			new ArrayList<>();
		this.legitSons =			new ArrayList<>();
		this.legitDaughters =	new ArrayList<>();
		this.realChildren = 		new ArrayList<>();
		this.children = 			new ArrayList<>();
		this.numOfLivingSons = 		0;						//Legimate livings sons, bastard sons not
	}

	//How many generations of ancestry does the person have
	public int getAncestryRating(){
		if (this.hadParents()){
			if (this.hadGrandparents()){
				if (this.hadGreatGrandparents()){
					if (this.had2ndGreatGrandparents()){
						if (this.had3rdGreatGrandparents()){
							return 5;
						}
						return 4;
					}
					return 3;
				}
				return 2;
			}
			return 1;
		}
		return 0;
	}

	//Check if character descends from the character (pat)ernally
	public boolean isPatDescendantOf(Human h){
		if (this.isChildOf(h)){
			return true;
		} else if (this.hadFather()){
			if (this.getFather().isPatDescendantOf(h)){
				return true;
			}
		}
		return false;
	}

	private Human getOwner(){
		return this.o;
	}

}
