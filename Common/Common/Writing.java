package Common;
import Human.*;
import Politics.*;
import Relationship.*;
import Ancestry.*;
import Looks.*;
import Common.Basic;
import java.io.FileWriter;
import java.util.ArrayList;
import java.io.IOException;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.List;

public class Writing {

	public static void writeMonarchList(){
		try {
			FileWriter writer = new FileWriter("Output/ListOfMonarchs.html", false);
			writer.write("<html>\n<head><meta charset='UTF-8'>\n");
			writer.write("<link type='text/css' rel='stylesheet' href='standard.css'/>");
			writer.write("</head><body>\n");
			writer.write("</body></html>");
			writer.write("\n<table style='text-align:center;'><r><th>Name</th><th>Birth</th><th>Marriage(s)</th><th>Death</th><th>Claim</th></tr>\n");
			Human q;
			for (Holder r: Realm.getLineage(0)){
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
		List<Human> lo;
		int tid = -1;
		String s1;
		List<Marriage> ml;
		int nOm;		//num of marriages
		try {
			FileWriter writer = new FileWriter("Output/MonarchsInfo.html", false);
			writer.write("<html>\n<head><meta charset='UTF-8'>\n");
			writer.write("<link type='text/css' rel='stylesheet' href='standard.css'/>");
			writer.write("</head><body>\n");
			for (Holder r: Realm.getLineage(0)){
				q = r.getPerson();
				tid++;
				writer.write("<h3>"+r.getName()+" ("+q.getLifespan()+")<hr></h3>\n");
				writer.write("<p>"+r.getReign()+" "+r.getReignLength()+"</p>");
				writer.write("<p>"+r.getClaim().getClaimHTML()+"</p>");
				writer.write("<p>"+r.getNotes()+"</p>");
				writer.write("<p>"+"Inbreeding: "+Consanguinity.countInbreed(q)+"</p>");
				writer.write(r.getRegentsHTML());
				if (q.isChild()){ continue;}
				ml = q.getMarriages();
				for (Marriage y: ml){
					s1 = "";
					s1 = "In "+y.getBeginning()+" "+q.getPronoun()+" married ";
					if (y.isCousinUnion()){
						s1 += q.getPossessive()+" cousin, ";
					}
					s1 += Basic.getCardinal(y.getAgeAt(y.getSpouse(q)))+"-year-old "+y.getSpouse(q).getBirthName();
					writer.write(s1);
						writer.write("; "+y.getHappinessDesc());
					if (y.getOffspringNum() != 0){
						writer.write(" produced "+y.buildOffspringHTML()+y.buildLivingOffspringHTML()+":<br>");
						writer.write("\n<table><r><th>Name</th><th>Prt.</th><th>CoA</th><th>Lifespan</th><th>Notes</th></tr>\n");
						lo = y.getOffspring();
						for (Human x: lo ){
							writer.write("<tr>");
							writer.write("<td class='nameCol'>"+x.getShortName()+"</td>\n<td class='portrait'>");
							writer.write(x.getPortrait());
							writer.write("</td>\n");
							writer.write("<td>");
							writer.write(x.getCoALink());
							writer.write("</td>\n");

							writer.write("<td>"+x.getLifespan()+"<br>"+"Aged "+x.aged()+"</td>\n");
							writer.write("<td>");
							if (x.isAdult() && x.wasMarried()){
								nOm = x.getNumOfMarriages();
								if (x.isMale()){
									if (nOm > 1){
										writer.write("Wives:<ol>");
										for(int z = 0; z < nOm; z++){
											writer.write("<li>"+x.getMarriage(z).getDoe().getBirthName()+x.getMarriage(z).getTenure());
											writer.write(x.getMarriage(z).getOffspringHTML()+"</li>\n");
										}
										writer.write("</ol>");
									} else{
										writer.write("Married "+x.getFirstMarriage().getDoe().getBirthName()+" in "+x.getFirstMarriage().getBeginning()+"\n");
										writer.write(x.getFirstMarriage().getOffspringHTML());
									}
								} else{
									if (nOm > 1){
										writer.write("Husbands:<ol>");
										for(int z = 0; z < nOm; z++){
											writer.write("<li>"+x.getMarriage(z).getStag().getBirthName()+x.getMarriage(z).getTenure());
											writer.write(x.getMarriage(z).getOffspringHTML()+"</li>\n");
										}
										writer.write("</ol>");
									} else{
										writer.write("Married "+x.getFirstMarriage().getStag().getBirthName()+" in "+x.getFirstMarriage().getBeginning()+"\n");
										writer.write(x.getFirstMarriage().getOffspringHTML());
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
				if (q.wasAdulterer()) {
					writer.write(Basic.capitalize(q.getPronoun())+" had following "+q.getLoverGroup()+":<br><ul>");
					List<Affair> la = q.getAllAffairs();
					List<Human> lh;
					for( Affair x: la ) {
						writer.write("<li>"+q.getLoverfromAffair(x).getFormalName());
						lh = x.getOffspring();
						writer.write("<ul>");
						for(Human o: lh){
							writer.write("<li>"+o.getFormalName()+" "+o.getLifespan()+"</li>");
						}
						writer.write("</ul>");
					}
				}

				writer.write("</ul>");
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
			FileWriter writer = new FileWriter("Output/Summary.html", false);
			writer.write("<html>\n<head><meta charset='UTF-8'>\n");
			writer.write("<link type='text/css' rel='stylesheet' href='standard.css'/>");
			writer.write("</head><body>\n");
			writer.write("<table>");
			Ruler r;
			writer.write("<tr><th>#</th><th>NAME</th> <th>HOUSE</th> <th>IMP</th> <th>STA</th> <th>THE</th> <th>SCH</th> <th>ECO</th> <th>JUD</th> <th>ARC</th> <th>CHI</th> <th>DIP</th> <th>MAR</th> <th>CHA</<th> <th>FIN</th> <th>STE</th> <th>GUI</th> <th>Eye</th> <th>Hair</th>");
			int numOfRuler = 0;
			int v;
			for (Holder h: Realm.getLineage(0)){
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
		int[][] pop = buildPopulationPyramid();
		//int[] wom = buildGynaecology();
		try {
			FileWriter writer = new FileWriter("Output/Demography.html", false);
			writer.write("<html>\n<head><meta charset='UTF-8'>\n");
			writer.write("<link type='text/css' rel='stylesheet' href='standard.css'/>");
			writer.write("</head><body>\n");

			writer.write("<b>General</b><table>");
			writer.write("<tr><th>#</th><th>⚤</th><th>♂</th><th>♀</th><tr>\n");

			writer.write("<tr><td>Living</td><td>"+Human.living.size()+"</td>");
			writer.write("<td>"+Man.getAmount()+"</td>");
			writer.write("<td>"+Woman.getAmount()+"</td></tr>");

			writer.write("<tr><td>Children</td><td>"+Human.getPerOfChildren()+"%</td>");
			writer.write("<td>"+Man.getPerOfChildren()+"%</td>");
			writer.write("<td>"+Woman.getPerOfChildren()+"%</td></tr>");

			writer.write("<tr><td>Elderly</td><td>"+Human.getPerOfElderly()+"%</td>");
			writer.write("<td>"+Man.getPerOfElderly()+"%</td>");
			writer.write("<td>"+Woman.getPerOfElderly()+"%</td></tr>");


			writer.write("<tr><td>Life expectancy</td><td>"+lifE[0]+"</td>");
			writer.write("<td>"+lifE[3]+"</td>");
			writer.write("<td>"+lifE[4]+"</td></tr>");


			writer.write("<tr><td>Oldest</td><td>"+lifE[2]+"</td></tr>");
			writer.write("<tr><td>Brown eyed</td><td>"+eyeC[0]+"</td></tr>");
			writer.write("<tr><td>Blue eyed</td><td>"+eyeC[1]+"</td></tr>");
			writer.write("<tr><td>Green eyed</td><td>"+eyeC[2]+"</td></tr>");
			writer.write("<tr><td>Black haired</td><td>"+hairC[0]+"</td></tr>");
			writer.write("<tr><td>Brown haired</td><td>"+hairC[1]+"</td></tr>");
			writer.write("<tr><td>Blond haired</td><td>"+hairC[2]+"</td></tr>");
			writer.write("<tr><td>Straw haired</td><td>"+hairC[3]+"</td></tr>");
			writer.write("<tr><td>Red haired</td><td>"+hairC[4]+"</td></tr>");

			writer.write("</table><b>Adults</b><table>");
			writer.write("<tr><th>#</th><th>⚤</th><th>♂</th><th>♀</th><tr>\n");

			writer.write("<tr><td>Life expectancy</td><td>"+lifE[1]+"</td>");
			writer.write("<td>"+lifE[5]+"</td>");
			writer.write("<td>"+lifE[6]+"</td></tr>");

			writer.write("<tr><td>Bachelors</td>");
			writer.write("<td>"+Human.getPerOfBachelors()+"%</td>");
			writer.write("<td>"+Man.getPerOfBachelors()+"%</td>");
			writer.write("<td>"+Woman.getPerOfBachelors()+"%</td></tr>");

			writer.write("<tr><td>Singles</td>");
			writer.write("<td>"+Human.getPerOfSingles()+"%</td>");
			writer.write("<td>"+Man.getPerOfSingles()+"%</td>");
			writer.write("<td>"+Woman.getPerOfSingles()+"%</td></tr>");

			writer.write("<tr><td>Widowed</td>");
			writer.write("<td>"+Human.getPerOfWidowed()+"%</td>");
			writer.write("<td>"+Man.getPerOfWidowed()+"%</td>");
			writer.write("<td>"+Woman.getPerOfWidowed()+"%</td></tr>");

			writer.write("<tr><td>Parents</td>");
			writer.write("<td>"+Human.getPerOfParents()+"%</td>");
			writer.write("<td>"+Man.getPerOfParents()+"%</td>");
			writer.write("<td>"+Woman.getPerOfParents()+"%</td></tr>");

			writer.write("</table><b>Reproductive age women</b><table>");

			writer.write("<tr><th>#</th><th>%</th><tr>\n");
			writer.write("<tr><td>Married</td><td>"+Woman.getPerOfMarriedRepWomen()+"%</td></tr>");
			writer.write("<tr><td>Pregnant</td><td>"+Woman.getPerOfPregnantWomen()+"%</td></tr>");

			writer.write("</table><b>Marriages</b><table>");

			writer.write("<tr><th>#</th><th>%</th><tr>\n");
			writer.write("<tr><th>Cousin marriages</th><td>"+Marriage.getPerOfCousinUnions()+"%</td><tr>\n");

			writer.write("<tr><th>Levirate marriages</th><td>"+Marriage.getPerOfLevirates()+"%</td><tr>\n");

			writer.write("<tr><th>Sororate marriages</th><td>"+Marriage.getPerOfSororates()+"%</td><tr>\n");

			writer.write("</table><p style='float:right'><b>CENSUS:</b><table>");
			writer.write("<tr><th>YEAR</th><th>MEN</th><th>WOMEN</th><th>Bastards</th></tr>");
			for(String x: Basic.census){
				writer.write(x);
			}
			writer.write("</table></p>");
			writer.write("<svg width='500' height='130' style='left:42%'>");
			writer.write("<rect x='0' y='20' width='500' height='110' style='fill:grey' />");
			for (int[] x: pop){
				writer.write("<rect x='"+(250-x[1])+"' y='"+(100-x[0])+"' width='"+x[1]+"' height='25' style='fill:blue' />");
				writer.write("<rect x='"+(250)+"' y='"+(100-x[0])+"' width='"+x[2]+"' height='25' style='fill:pink' />");
				writer.write("<text x='0' y='"+(123-x[0])+"' fill='black' font-size='8'>"+x[0]+" </text>");
			}
			writer.write("</svg>");

			writer.write("</body></html>");
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

//Gynaecology

/*	public static int[] buildGynaecology(){
		int[] v = new int[2];
		v[0] = getPerOfPregnantWomen();
		v[1] = getPerOfSpinners();
		return v;
	}*/








	public static int[] getLifeExpetency(){
		int[] arr = new int[7];
		int a = 0;
		int oldest = 0;
		int atD0 = 0;	//life exp.
		int NoP0 = 0;	//number of people
		int mLe = 0;	//male life exp.
		int NoM = 0;	//number of men
		int fLe = 0;	//female life exp.
		int NoW = 0; 	//number of women
		int agy;
		int atD1 = 0;	//adult life exp.
		int NoP1 = 0;	//number of adults
		int AMLE = 0; 	//adult male life exp.
		int AFLE = 0; 	//adult female life exp
		int NAM = 0; 	//number of adult men
		int NAW = 0; 	//number of adult women
		for (Human x: Basic.human.values()){
			if (x.isAlive() == false){
				agy = x.aged();
				atD0 += agy;
				NoP0++;
				if (x.isMale()){
					mLe += agy;
					NoM++;
				} else {
					fLe += agy;
					NoW++;
				}
				if (agy >= 20){
					atD1 += agy;
					NoP1++;
					if (x.isMale()){
						AMLE += agy;
						NAM++;
					} else {
						AFLE += agy;
						NAW++;
					}
					if (agy > oldest){
						oldest = agy;
					}
				}
			}
		}
		arr[0] = atD0/NoP0;
		arr[1] = atD1/NoP1;
		arr[2] = oldest;
		arr[3] = mLe/NoM;
		arr[4] = fLe/NoW;
		arr[5] = AMLE/NAM;
		arr[6] = AFLE/NAW;
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

	public static int[][] buildPopulationPyramid(){
		int[] men 	= findData(Man.getMen());
		int[] women = findData(Woman.women);
		int[][] l 	= new int[10][3];
		for(int x = 9; x >= 0; x--){
			l[9-x][0] = x*10;
			l[9-x][1] = men[x];
			l[9-x][2] = women[x];
			//System.out.println((x*10)+";"+men[x]+";"+women[x]);
		}
		return l;
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
