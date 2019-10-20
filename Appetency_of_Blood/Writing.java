import java.io.FileWriter;
import java.util.ArrayList;
import java.io.IOException;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.List;

public class Writing {

	public static void writeMonarchList(){
		try {
			FileWriter writer = new FileWriter("ListOfMonarchs.html", false);
			writer.write("<html>\n<head><meta charset='UTF-8'>\n");
			writer.write("<link type='text/css' rel='stylesheet' href='standard.css'/>");
			writer.write("</head><body>\n");
			writer.write("</body></html>");
			writer.write("\n<table style='text-align:center;'><r><th>Name</th><th>Birth</th><th>Marriage(s)</th><th>Death</th><th>Claim</th></tr>\n");
			Human q;
			for (Holder r: Realm.getHolderList(0)){
				q = r.getPerson();
				writer.write("<tr><td>"+r.getName()+"<br>"+r.getReign()+"<br><i>"+r.getReignLength()+"</i></td>");
				writer.write("<td>"+q.getBirth()+"</td>");
				writer.write("<td>"+"</td>");
				writer.write("<td>"+q.getPossibleDeath()+"<br>Aged "+q.aged()+"</td>");
				writer.write("<td class='nameCol2'>"+r.getClaim().getClaimHTML()+"</td>");
			}
			writer.write("</table>");
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void writeTable(){
		Human q;
		ArrayList<Human> tempA;
		int tid = -1;
		String s1;
		try {
			FileWriter writer = new FileWriter("MonarchsInfo.html", false);
			writer.write("<html>\n<head><meta charset='UTF-8'>\n");
			writer.write("<link type='text/css' rel='stylesheet' href='standard.css'/>");
			writer.write("</head><body>\n");
			for (Holder r: Realm.getHolderList(0)){
				q = r.getPerson();
				tid++;
				writer.write("<h3>"+r.getName()+" ("+q.getLifespan()+")<hr></h3>\n");
				writer.write("<p>"+r.getReign()+" "+r.getReignLength()+"</p>");
				writer.write("<p>"+r.getClaim().getClaimHTML()+"</p>");
				writer.write("<p>"+r.getNotes()+"</p>");
				writer.write("<p>"+"Inbreeding: "+Consanguinity.countInbreed(q)+"</p>");
				writer.write(r.getRegentsHTML());
				if (q.marriages == null){ continue;}
				for (Marriage y: q.marriages){
					s1 = "";
					s1 = "In "+y.getBeginning()+" "+q.getPronoun()+" married ";
					if (y.isCousinUnion()){
						s1 += q.getPossessive()+" cousin, ";
					}
					s1 += Main.cardinal[y.getAgeAt(y.getSpouse(q))]+"-year-old "+y.getSpouse(q).getFormalName();
					writer.write(s1);
						writer.write("; "+y.getHappinessDesc());
					if (y.getOffspringNum() != 0){
						writer.write(" produced "+y.buildOffspringHTML()+y.buildLivingOffspringHTML()+":<br>");
						writer.write("\n<table><r><th>Name</th><th>Portrait</th><th>Lifespan</th><th>Notes</th></tr>\n");
						for (Human x: y.getOffspring()){
							writer.write("<tr>");
							writer.write("<td class='nameCol'>"+x.getShortName()+"</td>\n<td class='portrait'>");
							writer.write(x.getPortrait());
							writer.write("</td>\n");
							writer.write("<td>"+x.getLifespan()+"<br>"+"Aged "+x.aged()+"</td>\n");
							writer.write("<td>");
							if (x.marriages != null && x.marriages.size() != 0){
								if (x.sex == false){
									if (x.marriages.size() > 1){
										writer.write("Wives:<ol>");
										for(int z = 0; z < x.marriages.size(); z++){
											writer.write("<li>"+x.marriages.get(z).getWife().getFormalName()+" (m. "+x.marriages.get(z).getBeginning()+")</li>\n");
											writer.write(x.marriages.get(z).getOffspringHTML());
										}
										writer.write("</ol>");
									} else{
										writer.write("Married "+x.marriages.get(0).getWife().getFormalName()+" in "+x.marriages.get(0).getBeginning()+"\n");
										writer.write(x.marriages.get(0).getOffspringHTML());
									}
								} else{
									if (x.marriages.size() > 1){
										writer.write("Husbands:<ol>");
										for(int z = 0; z < x.marriages.size(); z++){
											writer.write("<li>"+x.marriages.get(z).getHusband().getFormalName()+" ("+x.marriages.get(z).getBeginning()+")</li>\n");
											writer.write(x.marriages.get(z).getOffspringHTML());
										}
										writer.write("</ol>");
									} else{
										writer.write("Married "+x.marriages.get(0).getHusband().getFormalName()+" in "+x.marriages.get(0).getBeginning()+"\n");
										writer.write(x.marriages.get(0).getOffspringHTML());
									}
								}
							}
							writer.write("</td></tr>");
						}
						writer.write("</table><br>");
					}
					else{
						writer.write(" was childless.<br>");
					}
				}
			}

			//writer.write(Consanguinity.buildFamilyTree(Holder.sovereign));
			writer.write("</body></html>");
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void writeSummary(){
		try {
			FileWriter writer = new FileWriter("Summary.html", false);
			writer.write("<html>\n<head><meta charset='UTF-8'>\n");
			writer.write("<link type='text/css' rel='stylesheet' href='standard.css'/>");
			writer.write("</head><body>\n");
			writer.write("<table>");
			Ruler r;
			writer.write("<tr><th>#</th><th>NAME</th> <th>HOUSE</th> <th>IMP</th> <th>STA</th> <th>THE</th> <th>SCH</th> <th>ECO</th> <th>JUD</th> <th>ARC</th> <th>CHI</th> <th>DIP</th> <th>MAR</th> <th>CHA</<th> <th>FIN</th> <th>STE</th> <th>GUI</th> <th>Eye</th> <th>Hair</th>");
			int numOfRuler = 0;
			int v;
			for (Holder h: Realm.getHolderList(0)){
				r = h.getRuler();
				numOfRuler++;
				writer.write("<tr>");
				writer.write("<td>"+numOfRuler+"</td>");
				writer.write("<td>"+h.getName()+"</<td>");
				writer.write("<td>"+h.getPerson().getHouse().getFullName()+"</<td>");
				if(r != null){
					v = r.getInterest().getImperialism();
					writer.write("<td style='background:"+col[v]+";'>"+ast[v]+"</<td>");
					v = r.getInterest().getStability();
					writer.write("<td style='background:"+col[v]+";'>"+ast[v]+"</<td>");
					v = r.getInterest().getTheology();
					writer.write("<td style='background:"+col[v]+";'>"+ast[v]+"</<td>");
					v = r.getInterest().getScholarship();
					writer.write("<td style='background:"+col[v]+";'>"+ast[v]+"</<td>");
					v = r.getInterest().getEconomy();
					writer.write("<td style='background:"+col[v]+";'>"+ast[v]+"</<td>");
					v = r.getInterest().getJudiacry();
					writer.write("<td style='background:"+col[v]+";'>"+ast[v]+"</<td>");
					v = r.getInterest().getArchitecture();
					writer.write("<td style='background:"+col[v]+";'>"+ast[v]+"</<td>");
					v = r.getInterest().getChivalry();
					writer.write("<td style='background:"+col[v]+";'>"+ast[v]+"</<td>");
					v = r.getInterest().getDiplomacy();
					writer.write("<td style='background:"+col[v]+";'>"+ast[v]+"</<td>");
					v = r.getSkill().getMartial();
					writer.write("<td style='background:"+col[v]+";'>"+Integer.toString(v)+"</<td>");
					v = r.getSkill().getCharisma();
					writer.write("<td style='background:"+col[v]+";'>"+Integer.toString(v)+"</<td>");
					v = r.getSkill().getFinance();
					writer.write("<td style='background:"+col[v]+";'>"+Integer.toString(v)+"</<td>");
					v = r.getSkill().getStewardship();
					writer.write("<td style='background:"+col[v]+";'>"+Integer.toString(v)+"</<td>");
					v = r.getSkill().getGuile();
					writer.write("<td style='background:"+col[v]+";'>"+Integer.toString(v)+"</<td>");
				}
				else{
					writer.write("<td colspan='14'></<td>");
				}
				writer.write("<td>"+Eye.getColor(h.getPerson().getEye())+"</<td>");
				writer.write("<td>"+Hair.getColor(h.getPerson().getHair())+"</<td>");
				writer.write("<tr>");
			}
			writer.write("</table>");
			writer.write("</body></html>");
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public static void writeDemography(){
		int[] lifE = getLifeExpetency();
		int[] eyeC = getEyeColorCensus();
		int[] hairC = getHairColorCensus();
		try {
			FileWriter writer = new FileWriter("Demography.html", false);
			writer.write("<html>\n<head><meta charset='UTF-8'>\n");
			writer.write("<link type='text/css' rel='stylesheet' href='standard.css'/>");
			writer.write("</head><body>\n");
			writer.write("<table><tr><td>Living</td><td>"+Human.living.size()+"</td></tr>");
			writer.write("<tr><td>Life expectancy</td><td>"+lifE[0]+"</td></tr>");
			writer.write("<tr><td>Adult expectancy</td><td>"+lifE[1]+"</td></tr>");
			writer.write("<tr><td>Oldest</td><td>"+lifE[2]+"</td></tr>");
			writer.write("<tr><td>Brown eyed</td><td>"+eyeC[0]+"</td></tr>");
			writer.write("<tr><td>Blue eyed</td><td>"+eyeC[1]+"</td></tr>");
			writer.write("<tr><td>Green eyed</td><td>"+eyeC[2]+"</td></tr>");
			writer.write("<tr><td>Black haired</td><td>"+hairC[0]+"</td></tr>");
			writer.write("<tr><td>Brown haired</td><td>"+hairC[1]+"</td></tr>");
			writer.write("<tr><td>Blond haired</td><td>"+hairC[2]+"</td></tr>");
			writer.write("<tr><td>Straw haired</td><td>"+hairC[3]+"</td></tr>");
			writer.write("<tr><td>Red haired</td><td>"+hairC[4]+"</td></tr>");
			writer.write("</table><b>CENSUS:</b><table>");
			writer.write("<tr><th>YEAR</th><th>MEN</th><th>WOMEN</th></tr>");
			for(String x: Main.census){
				writer.write(x);
			}
			writer.write("</table>");

			writer.write("</body></html>");
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static int[] getLifeExpetency(){
		int[] arr = new int[3];
		int a = 0;
		int oldest = 0;
		int atD0 = 0;
		int NoP0 = 0;
		int agy;
		int atD1 = 0;
		int NoP1 = 0;
		for (Human x: Main.human.values()){
			if (x.isAlive() == false){
				agy = x.aged();
				atD0 += agy; NoP0++;
				if (agy >= 20){
					atD1 += agy; NoP1++;
					if (agy > oldest){
						oldest = agy;
					}
				}
			}
		}
		arr[0] = atD0/NoP0;
		arr[1] = atD1/NoP1;
		arr[2] = oldest;
		return arr;
	}

	public static int[] getEyeColorCensus(){
		int[] c = new int[3];
		for(Human x: Human.living){
			if (x.getEye() == 0){		c[0]++;	}
			else if (x.getEye() == 1){	c[1]++;	}
			else {						c[2]++;	}
		}
		return c;
	}

	public static int[] getHairColorCensus(){
		int[] c = new int[5];
		for(Human x: Human.living){
			if (x.getHair() == 0){		c[0]++;	}
			else if (x.getHair() == 1){	c[1]++;	}
			else if (x.getHair() == 2){	c[2]++;	}
			else if (x.getHair() == 3){	c[3]++;	}
			else {						c[4]++;	}
		}
		return c;
	}



	public static void buildPopulationPyramid(){
		int[] men = findData(Man.men);
		int[] women = findData(Woman.women);
		for(int x = 9; x >= 0; x--){
			System.out.println((x*10)+";"+men[x]+";"+women[x]);
		}
	}

	public static int[] findData(List<Human> l){
		int c = 0;
		int[] a = new int[10];
		int sg 	= (l.get(0).getAge()/10)*10;
		for(Human x: l){
			if (x.getAge() >= sg){
				c++;
			} else{
				a[sg/10] = c;
				c = 0;
				sg = (x.getAge()/10)*10;
			}
		}
		a[sg/10] = c;
		return a;
	}

	private static String[] ast = {"", "*", "**", "***", "****", "*****", "******", "*******", "********", "*********", "**********"};
	private static String[] col = {"#ff0000", "#ff4000", "#ff8000", "#ffbf00", "#ffff00", "#bfff00", "#80ff00", "#40ff00", "#00ff00", "#00ff40", "#00ff80"};
}
