import java.util.ArrayList;

public class Project{
	private static ArrayList<Project> removable = new ArrayList<>();
	private int id;
	private int time;
	private double cost;

	public Project(int i, int t, double c){
		this.id = i;
		this.time = t;
		this.cost = c;
	}

	public void progress(Office off){
		if(this.coverCost(off)){
			this.time--;
			if (this.time == 0){			this.finishTask(off); 		}
		} else{								removable.add(this); 		}
	}

	private boolean coverCost(Office off){
		return off.getFunds().submitPayment(this.cost);
	}

	private void finishTask(Office off){
		switch(this.id){
			case 1:
				off.getTerritory().buildFort();
				break;
			case 2:
				off.getTerritory().buildScriptorium();
				break;
			case 3:
				off.getTerritory().buildSchool();
				break;
			case 4:
				off.getTerritory().buildCollega();
				break;
			case 5:
				off.getTerritory().buildPriory();
				break;
			case 6:
				off.getTerritory().buildAbbey();
				break;
			case 7:
				off.getTerritory().buildTemple();
				break;
			case 8:
				off.getTerritory().buildCathedral();
				break;
			case 9:
				off.getTerritory().buildMonument();
				break;
		}
		removable.add(this);
	}

	public static void drain(Office off){
		for(Project x: removable){
			off.getProjects().remove(x);
		}
		removable.clear();
	}

	public enum Type{
		FORT();
	}
}
