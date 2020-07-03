package Code.Common;
import Code.Human.*;
import Code.Politics.*;
import Code.Relationship.*;
import Code.Ancestry.*;
import Code.Looks.*;
import Code.House.House;
import Code.Common.Basic;
import Code.Common.HTML;
import java.io.FileWriter;
import java.util.ArrayList;
import java.io.IOException;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.List;

public class Writing {

	public static void writeClaims(){
		String s = writeClaimsInfo().toString();
		try {
			FileWriter writer = new FileWriter("Output/Claims.html", false);
			writer.write(s);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static StringBuffer writeClaimsInfo(){
		StringBuffer s = HTML.getBeginning();
		String[][] th = {{"Name", ""}, {"DoR", ""}, {"Ancestor", ""}, {"Birth", ""}, {"Lineage", ""}};
		StringBuffer l = new StringBuffer(HTML.getTr(HTML.createTableHeaderClass(th)));
		String a;
		int lh;
		Human h;
		Office.offices.get(0).sortClaims();
		for(Claim x:Office.offices.get(0).getClaims()){
			h = x.getHolder();
			lh = x.getLineageLength();
			a = HTML.getTd(h.getFormalName());
			a += (HTML.getTd(""+(lh-1)));
			a += (HTML.getTdTitle((x.getLineageString()), ""+(x.getUltimateAncestor().getFormalName())));
			a += (HTML.getTd(""+h.getBirthYear()));
			a += (HTML.getTd(x.getBloodTypeStr()));
			l.append(HTML.getTr(a));
		}

		s.append(HTML.getTable(String.valueOf(l))).append(HTML.getEnding());
		return s;
	}



	public static void writeNobility(){
		StringBuffer s = HTML.getBeginning();
		//[0] == Name of the th
		//[1] == Name of css class
		String[][] th = {{"Name", ""}, {"CoA", "CoAT"}, {"Founded", ""}, {"Origin", ""}, {"Men", ""}, {"Women", ""}, {"Head", ""}, {"Alliances", ""}};

		StringBuffer t = new StringBuffer(HTML.createTableHeaderClass(th));		//Table
		List<House> l = House.getNobles();
		for(House x: l){
			StringBuffer td = new StringBuffer(HTML.getTd(x.getName()));
			td.append(HTML.getTdClass("CoAT", x.getCoALink()));
			td.append(HTML.getTd(x.getFounding()));
			td.append(HTML.getTd(x.getOriginString()));
			td.append(HTML.getTd(String.valueOf(x.getKinsmenCount())));
			td.append(HTML.getTd(String.valueOf(x.getKinswomenCount())));
			td.append(HTML.getTd(x.getHead().getFormalName()+" ("+x.getHead().getAge()+")" ));
			td.append(HTML.getTd(""+x.getAlliances()));
			t.append(HTML.getTr(String.valueOf(td)));
		}

		//Final row for the total
		StringBuffer td = new StringBuffer(HTML.getTh("TOTAL"));
		td.append(HTML.getThCoSpan("N/A", 3));				//Fill in the empty N&A
		td.append(HTML.getTh(String.valueOf(House.getNoblemenCount())));	//Number of noblemen total
		td.append(HTML.getTh(String.valueOf(House.getNoblewomenCount())));//Number of noblewomen total
		td.append(String.valueOf(HTML.getThColspan(2)));						//Get empty
		t.append(HTML.getTr(String.valueOf(td)));
		s.append(HTML.getTable(String.valueOf(t)));
		s.append(HTML.getEnding());
		try {
			FileWriter writer = new FileWriter("Output/NobleHouses.html", false);
			writer.write(String.valueOf(s));
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void writeMonarchList(){
		String t = recordMonarchList();
		try {
			FileWriter writer = new FileWriter("Output/ListOfMonarchs.html", false);
			writer.write(String.valueOf(t));
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String recordMarriages(Human h){

		StringBuffer lt = new StringBuffer();			//Temporary list
		//If character never reached adulthood, there is no marriages to be recorded
		if (h.isAdult()){
			List<Marriage> m = h.getMarriages();
			String n;
			for(Marriage x: m){
				n = x.getDoe().getBirthName();

				if (x.hasKinType()){
					lt.append(HTML.getPTitle(x.getKinTypeHTML(h), n+" †"));
				} else if (!x.isRegular()) {
					lt.append(HTML.getPTitle(x.getTypeHTML(h), n+" ‡"));
				} else {
					lt.append(HTML.getP(n));
				}

				lt.append(x.getTenureShort());			//When the marriage began and ended
				lt.append(HTML.getBr());

				if (x.getHadChildren()){
					lt.append(x.getNumOfOffspring());
				} else {
					lt.append("No ");
				}

				lt.append(" children");

				//If the marriage is the last, do not bother with hr
				if (!x.isLastMarriageOf(h)){
					lt.append("<hr>");
				}

			//	lt = HTML.getLi(String.valueOf(lt));
			}
			return HTML.getP(String.valueOf(lt));
		} else {
			return String.valueOf(lt);
		}
	}

	//Used to get a row of monarchs for Monarch List
	public static String recordMonarchInvidual(Holder h){

		StringBuffer td = new StringBuffer("");							//Temporary table cell
		StringBuffer tr = new StringBuffer("");							//Temporary table row

		Human q = h.getPerson();				//Every ruler is a person, serves to shorten

	//First cell for name and reign
		td.append(h.getName()).append("<br>");
		if (q.getName().hasNick()){
			td.append(HTML.getI(q.getName().getNickname()));
		}
		td.append(HTML.getBr());
		td.append(h.getReign());
		td.append(HTML.getBr());
		td.append(HTML.getI(h.getReignLength()));			//Italize
		tr.append(HTML.getTd(String.valueOf(td)));											//Add to table

	//Cell for portraits
		tr.append(HTML.getTdClass("portrait", q.getPortrait()));

	//Cell for coat of arms
		tr.append(HTML.getTdClass("CoAT", q.getHouseCoALink()));

	//Cell for birth
		tr.append(HTML.getTd(q.getBirthF()));

	//Cell for marriage
		tr.append(HTML.getTd(recordMarriages(q)));

	//Cell for age
		tr.append(HTML.getTd(q.getPossibleDeath()+"<br>Aged "+(q.getAged())));

	//Cell for claims
		tr.append(HTML.getTdClass("nameCol2", h.getClaim().getClaimHTML()));

	//Cell for dynasty
		tr.append(HTML.getTdClass("nameCol2", q.getHouse().getDynasty().getName()));
		return String.valueOf(tr);
	}


	public static String recordMonarchList(){
		StringBuffer te = new StringBuffer("");							//Temporary table

		StringBuffer t  = HTML.getBeginning();	//Short for text the written text will be stored here

		//The names of headers
		String[][] th = {{"Name", ""}, {"Prt.", "portrait"}, {"CoA", "CoAT"}, {"Birth", ""}, {"Marriage(s)", ""}, {"Death", ""}, {"Claim", ""}, {"Dynasty", ""}};
		te.append(HTML.createTableHeaderClass(th));

		Human q;
		for (Holder r: Realm.getLineage(0)){
			te.append(HTML.getTr(recordMonarchInvidual(r)));
		}

		t.append(HTML.getTable(String.valueOf(te)));				//Table is now added into the body
		t.append(HTML.getEnding());
		return String.valueOf(t);
	}

//End of list of monarchs
//Beginning of  monarchs info

	public static StringBuffer writeMonarchsInfo(){
		StringBuffer s = HTML.getBeginning();
		for (Holder h: Realm.getLineage(0)){
			s.append(writeMonarchHolder(h));
		}
		s.append(HTML.getEnding());
		return s;
	}

	public static String writeMonarchHolder(Holder r){
		Human h = r.getPerson();
		StringBuffer s;
		s = new StringBuffer(HTML.wrapSummary(r.getName()+" ("+h.getLifespan()+")"));
		s.append(HTML.getP(r.getReign()));
		s.append(HTML.getP(r.getClaim().getClaimHTML()));
		s.append(HTML.getP(r.getNotes()));
		s.append(HTML.getP(r.getBiography()));
		return HTML.wrapDetails(String.valueOf(s));
	}


	public static void writeTable(){
		StringBuffer s = writeMonarchsInfo();
		try {
			FileWriter writer = new FileWriter("Output/MonarchsInfo.html", false);
			writer.write(String.valueOf(s));
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

			writer.write("<tr><th>First Cousin marriages</th><td>"+Marriage.getPerOfCousinUnions()+"%</td><tr>\n");

			writer.write("<tr><th>Second Cousin marriages</th><td>"+Marriage.getPerOfSecondCousinUnions()+"%</td><tr>\n");

			writer.write("<tr><th>Levirate marriages</th><td>"+Marriage.getPerOfLevirates()+"%</td><tr>\n");

			writer.write("<tr><th>Sororate marriages</th><td>"+Marriage.getPerOfSororates()+"%</td><tr>\n");

			writer.write("<tr><th>Childless marriages</th><td>"+Marriage.getPerOfChildless()+"%</td><tr>\n");

			writer.write("<tr><th>Avg. num. of children</th><td>"+Marriage.getAvgNumOfChildren()+"</td><tr>\n");

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
		return new int[7];
		/*
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
				agy = x.getAged();
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
		return arr;*/
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
		}
		return l;
	}

	public static int[] findData(List<Human> l){
		int c = 0;
		int[] a = new int[10];
		int sg 	= (l.get(0).getAge()/10)*10;
		for(Human x: l){
			if (x.isOverAgeOf(sg)){
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
