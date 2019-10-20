public class Rule {
	private Holder seniorHolder;	//occupied outside of interregnum
	private Holder juniorHolder;
	private Ruler seniorRuler;		//ALWAYS occupied
	private Ruler juniorRuler;
	private Human key;				//person whose existence justifies the reign
	private int type;				//0 = interregnum, 1 = sole rule, 2 = coregency
	private boolean regency;
	private Office office;

	//sole ruler
	public Rule(Office o){
		this.office = o;
		o.addRule(this);
	}


	public void createSoleRuler(Office o, Human s){
		this.seniorHolder = 				Holder.regnafy(s, o);
		this.key =							s;
		this.type =							1;
		s.title = 							s.getRoyalTitle();
		s.getName().setFull(this.seniorHolder.getName());
		if(!s.isUnwed()){
			this.seniorHolder.setConsort(s.getSpouse()) ;
		}
		if (!s.isPolitican()){				s.polProfile = new PolProfile(s);	}
		s.getPolProfile().addRegnalTitle(o);
	}

	public void handleRegency(){
		if(this.hasRegency()){
			this.endRegency();
		}
	}

	public void endRegency(){
		this.regency = false;
	}

	public void endCoregency(){
		this.seniorHolder.endReign();
		this.juniorHolder.endReign();
	}

	public void endReign(){
		switch(this.getType()){
			case 1:
				this.seniorHolder.endReign();
				break;
			case 2:
				this.endCoregency();
				break;
		}
	}


//Micro methods

	public boolean hasRegency(){				return this.regency;					}
	public Holder getSeniorHolder(){			return this.seniorHolder;				}
	public Human getKey(){						return this.key;						}
	public int getType(){						return this.type;						}
	public Ruler getSeniorRuler(){				return this.seniorRuler;				}
	public void setSeniorRuler(Ruler r){		this.seniorRuler = r;					}
//	public void setType(int i){					this.type = i;							}

}
