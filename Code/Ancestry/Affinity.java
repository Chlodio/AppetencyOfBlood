package Code.Ancestry;
import Code.Common.Basic;
import Code.Human.Human;
import java.util.ArrayList;
import java.util.List;

public class Affinity{
	private static List<Human> list = new ArrayList<>();

	public static String countInbreed(Human h){
		int a = h.getAncestryRating();
		list.clear();
		addParents(h, a);
		if (list.size() != 0){
			list.remove(h);
			return getUniqueEntries(list)+"%, "+a;
		}
		return "0%";
	}

	public static int getUniqueEntries(List<Human> l){
		List<Human> u = new ArrayList<>();
		int i = 0;
		for (Human x: l){
			if (!u.contains(x)){
				u.add(x);
			}
		}
		i = l.size()-u.size();
		return Math.round((((i+0.0f)/l.size())*10.0f)*10);
	}

	public static void addParents(Human h, int n){
		if (n != 0){
			list.add(h);
			addParents(h.getFather(), n-1);
			addParents(h.getMother(), n-1);
		}
	}

	public static int countDuplicates(){
		int c = 0;
		int d = 0;
		for(Human x: list){
			c = 0;
			for(Human y: list){
				if(x == y){
					c++;
					if (c > 1){ 			d++; }
				}
			}
		}
		return d;
	}

	public static String buildFamilyTree(Human h){
		StringBuffer s = new StringBuffer("<svg height='500' width='1000'>");
		int y = 460;
		s.append("<text x='50%' y='").append(y);
		s.append("' text-anchor='middle'>");
		s.append(h.getFullName()).append("</text>");
		s.append(buildLineage(h.getFather(), y-15, 2, 33));
		s.append(buildLineage(h.getMother(), y-15, 0, 66));
		s.append(buildLineage(h.getFathersMother(), y-65, 0, 66));
		s.append(buildLineage(h.getFathersFather().getMother(), y-115, 0, 66));
		s.append("<line x1='45%' y1='").append(y-45).append("'x2='55%' y2='");
		s.append(y-45);
		s.append("' style='stroke:rgb(0,0,0);stroke-width:2'/>");
		s.append("</svg>");
		return String.valueOf(s);
	}

	public static String buildLineage(Human h, int y, int n, int x){
		String s = "<line x1='50%' y1='"+(y)+"'x2='50%' y2='"+(y-20)+"' style='stroke:rgb(0,0,0);stroke-width:2'/>";
		s += 	"<text x='"+x+"%' y='"+(y-30)+"' text-anchor='middle'>"+h.getFullName()+"</text>";
		if (n > 0){			return s+buildLineage(h.getFather(), y-50, n-1, x);
		} else {			return s; }
	}

	public static int getGenDif(Human from, Human to){	return from.getGen() - to.getGen();}


	public static String getCognaticRelation(Human from, Human to){
		if(from.isSiblingOf(to)){
			return to.getSibling();
		} else if (from.isNephewOf(to)){
			return to.getNibling();
		} else if (from.isNephewOf(from)){
			return to.getPibling();
		} else if (from.isFirstCousinOf(to)){
			return "first cousin";
		}
		return "";
	}



	public static String getPaternalRelation(Human from, Human to){
		String r;
		int v = getGenDif(from, to);
		if (from == to){			return "same"; }
		switch(v){
			case 0:
				return getPatrilinearCollaretal(from, to);
			case 1:
				return getPaternalAvunculate(from, to);
			case -1:
			 	return getPaternalSubrinate(from, to);
			case 2:
			 	return getPaternalGrandparent(getGenPaternalAncestor(2, from), to);
			case-2:
				return getPaternalGrandchild(from, getGenPaternalAncestor(2, to));
			case 3:
			 	return getPatGreatgrandparent(getGenPaternalAncestor(3, from), to);
			case -3:
			 	return getPatGreatgrandchild(from, getGenPaternalAncestor(3, to));
		}
		return "???";
	}

	public static int getCommonAncestorDegree(Human from, Human to){
		int v = from.getGen();
		for(int x = 0; x < v; x++){
			if (from.isBrotherOf(to)){
				return x;
			}
			else{
				from =	from.getFather();
				to =	to.getFather();
			}
		}
		return 0;
	}

	public static Human getGenPaternalAncestor(int gen, Human h){
		for(int x = 0; x < gen; x++){
			h = h.getFather();
		}
		return h;
	}

	public static String getPatrilinearCollaretal(Human from, Human to){
		int v = getCommonAncestorDegree(from, to);
		if (v == 0){
			if (from.isFullSiblingOf(to)){
				return to.getSibling();
			} else {
				return "half-"+to.getSibling();
			}
		}
		else{
			return Basic.getOrder(v-1)+" cousin";
		}
	}


	public static String getPaternalAvunculate(Human from, Human to){
		int v;
		if (to.isChildOf(from)){
			return to.getParent();
		}
	 	v = getCommonAncestorDegree(getGenPaternalAncestor(1, from), to);
		switch(v){
			case 0: return to.getPibling();
			default:
				return Basic.getOrder(v-1)+" cousin once removed";
		}
	}

	public static String getPaternalSubrinate(Human from, Human to){
		int v;
		if (from.isChildOf(to)){
			return to.getOffspring();
		}
	 	v = getCommonAncestorDegree(from, getGenPaternalAncestor(1, to));
		switch(v){
			case 0: return to.getNibling();
			default:
				return Basic.getOrder(v-1)+" cousin once removed";
		}
	}

	public static String getPaternalGrandparent(Human from, Human to){
		int v = getCommonAncestorDegree(from, to);
		switch(v){
			case 0:
				return "grand"+to.getParent();
			case 1: return to.getPibling();
			default:
				return Basic.getOrder(v-1)+" cousin twice removed";
		}
	}

	public static String getPaternalGrandchild(Human from, Human to){
		int v = getCommonAncestorDegree(from, to);
		switch(v){
			case 0:	return "grand"+to.getOffspring();
			case 1: return "great "+to.getNibling();
			default:
				return Basic.getOrder(v-1)+" cousin twice removed";
		}
	}

	public static String getPatGreatgrandparent(Human from, Human to){
		int v = getCommonAncestorDegree(from, to);
		switch(v){
			case 0:	return "great grand"+to.getParent();
			case 1: return "great-grand "+to.getPibling();
			default:
				return Basic.getOrder(v-1)+" cousin thrice removed";
		}
	}

	public static String getPatGreatgrandchild(Human from, Human to){
		int v = getCommonAncestorDegree(from, to);
		switch(v){
			case 0:	return "great grand"+to.getOffspring();
			case 1: return "great-grand "+to.getNibling();
			default:
				return Basic.getOrder(v-1)+" cousin thrice removed";
		}
	}


	public static void printDes(Human h, int i){
		for(int x = 0; x < i; x++){
			System.out.print("\t");
		}
		if (h.isAdult()){
			List<Human> l = h.getSons();
			for(Human x: l){
				printDes(x, i+1);
			}
		}
	}


}
