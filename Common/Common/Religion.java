package Common;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Religion{
	private static int id = 0;
	private static Map<Integer, Religion> religion = new HashMap<>();
	private String name; private Parish parish;
	public Religion(){
		id++;
		this.name = "Orthodox";
		this.parish = new Parish();
	}
	public static Religion foundReligion(){
		religion.put(id+1, new Religion());
		return religion.get(id);
	}
	public static Religion getLatest(){ 	return religion.get(id);}
	public Parish getParish(){ 			return this.parish;		}
}
