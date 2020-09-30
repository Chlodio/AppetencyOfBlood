package Code.Common;
import Code.Human.*;
import Code.Politics.*;
import Code.Relationship.*;
import Code.Ancestry.*;
import Code.Looks.*;
import Code.House.House;
import Code.Common.Basic;
import Code.Common.HTML;
import Code.History.Census;
import Code.Politics.Consort;
import Code.Ancestry.Affinity;
import Code.Politics.Cabinet;
import java.io.FileWriter;
import java.util.ArrayList;
import java.io.IOException;
import Code.calendar.Calendar;
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
			td.append(HTML.getTd(x.getFounding().getYearString()));
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
				n = x.getPartnerOf(h).getBirthName();

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
		tr.append(HTML.getTd(q.getBirth().getDateString()));

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

/*
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
	}*/

	public static String getGeneralCensus(){
		StringBuffer s = new StringBuffer();
		s.append(HTML.getCaption("General"));

		//The names of headers
		String[] th = {"#", "⚤", "♂", "♀"};

		String temp = "";
		s.append(HTML.getTr(HTML.createTableHeader(th)));
		temp = HTML.getTd("Living");
		temp += HTML.getTd(Human.living.size()+"");
		temp += HTML.getTd(Man.getAmount()+"");
		temp += HTML.getTd(Woman.getAmount()+"");
		s.append(HTML.getTr(temp));

		temp = HTML.getTd("Children");
		temp += HTML.getTd(Human.getPerOfChildren()+"%");
		temp += HTML.getTd(Man.getPerOfChildren()+"%");
		temp += HTML.getTd(Woman.getPerOfChildren()+"%");
		s.append(HTML.getTr(temp));

		temp = HTML.getTd("Elderly");
		temp += HTML.getTd(Human.getPerOfElderly()+"%");
		temp += HTML.getTd(Man.getPerOfElderly()+"%");
		temp += HTML.getTd(Woman.getPerOfElderly()+"%");
		s.append(HTML.getTr(temp));

		temp = HTML.getTd("Median age");
		temp += HTML.getTd(Human.getMedianAge(Human.getLiving())+"");
		temp += HTML.getTd(Human.getMedianAge(Man.getMen())+"");
		temp += HTML.getTd(Human.getMedianAge(Woman.getWomen())+"");
		s.append(HTML.getTr(temp));

		temp = HTML.getTd("Orphans");
		temp += HTML.getTd(Human.getPerOfOrphans()+"%");
		temp += HTML.getTd(Man.getPerOfOrphans()+"%");
		temp += HTML.getTd(Woman.getPerOfOrphans()+"%");
		s.append(HTML.getTr(temp));

		return HTML.getTable(String.valueOf(s));
	}


	public static String getAdultCensus(){
		StringBuffer s = new StringBuffer();
		int[] lifE = getLifeExpetency();
		s.append(HTML.getCaption("Adult"));

		//The names of headers
		String[] th = {"#", "⚤", "♂", "♀"};

		String temp = "";
		s.append(HTML.getTr(HTML.createTableHeader(th)));

		temp = HTML.getTd("Bachelors");
		temp += HTML.getTd(Human.getPerOfBachelors()+"%");
		temp += HTML.getTd(Man.getPerOfBachelors()+"%");
		temp += HTML.getTd(Woman.getPerOfBachelors()+"%");
		s.append(HTML.getTr(temp));

		temp = HTML.getTd("Singles");
		temp += HTML.getTd(Human.getPerOfSingles()+"%");
		temp += HTML.getTd(Man.getPerOfSingles()+"%");
		temp += HTML.getTd(Woman.getPerOfSingles()+"%");
		s.append(HTML.getTr(temp));

		temp = HTML.getTd("Widowed");
		temp += HTML.getTd(Human.getPerOfWidowed()+"%");
		temp += HTML.getTd(Man.getPerOfWidowed()+"%");
		temp += HTML.getTd(Woman.getPerOfWidowed()+"%");
		s.append(HTML.getTr(temp));

		temp = HTML.getTd("Parents");
		temp += HTML.getTd(Human.getPerOfParents()+"%");
		temp += HTML.getTd(Man.getPerOfParents()+"%");
		temp += HTML.getTd(Woman.getPerOfParents()+"%");
		s.append(HTML.getTr(temp));
		return HTML.getTable(String.valueOf(s));
	}

	public static String getReproductiveWomen(){
		StringBuffer s = new StringBuffer();
		int[] lifE = getLifeExpetency();
		s.append(HTML.getCaption("Reproductive women"));

		//The names of headers
		String[] th = {"Name", "%"};

		String temp = "";
		s.append(HTML.getTr(HTML.createTableHeader(th)));

		temp = HTML.getTd("Married");
		temp += HTML.getTd(Woman.getPerOfMarriedRepWomen()+"%");
		s.append(HTML.getTr(temp));

		temp = HTML.getTd("Pregnant");
		temp += HTML.getTd(Woman.getPerOfPregnantWomen()+"%");
		s.append(HTML.getTr(temp));

		temp = HTML.getTd("Fertility rate");
		temp += HTML.getTd((Woman.getFertilityRate()+"").substring(0,3));
		s.append(HTML.getTr(temp));

		temp = HTML.getTd("Age at first marriage");
		temp += HTML.getTd((Woman.getAgeAtFirstMarriage()+""));
		s.append(HTML.getTr(temp));

		temp = HTML.getTd("Age at first birth");
		temp += HTML.getTd((Woman.getAgeAtFirstChild()+""));
		s.append(HTML.getTr(temp));

		return HTML.getTable(String.valueOf(s));
	}

	public static String getMarriageCensus(){
		StringBuffer s = new StringBuffer();
		s.append(HTML.getCaption("Marriages"));

		//The names of headers
		String[] th = {"Name", "%"};

		String temp = "";
		s.append(HTML.getTr(HTML.createTableHeader(th)));

		temp = HTML.getTd("First cousin marriages");
		temp += HTML.getTd(Marriage.getPerOfCousinUnions()+"%");
		s.append(HTML.getTr(temp));

		temp = HTML.getTd("Second cousin marriages");
		temp += HTML.getTd(Marriage.getPerOfSecondCousinUnions()+"%");
		s.append(HTML.getTr(temp));

		temp = HTML.getTd("Levirate marriages");
		temp += HTML.getTd(Marriage.getPerOfLevirates()+"%");
		s.append(HTML.getTr(temp));

		temp = HTML.getTd("Sororate marriages");
		temp += HTML.getTd(Marriage.getPerOfSororates()+"%");
		s.append(HTML.getTr(temp));

		temp = HTML.getTd("Childless marriages");
		temp += HTML.getTd(Marriage.getPerOfChildless()+"%");
		s.append(HTML.getTr(temp));

		temp = HTML.getTd("Avg. num. of children");
		temp += HTML.getTd((Marriage.getAvgNumOfChildren()+"").substring(0,3));
		s.append(HTML.getTr(temp));

		return HTML.getTable(String.valueOf(s));
	}

	public static String[] getOldestLongevityMonarch(){
		List<Holder> l = Realm.getLineage(0);
		Holder h = l.get(0);
		int a = h.getPerson().getAged();
		for(Holder x: l){
			if (x.getPerson().getAged() > a){
				h = x;
				a = x.getPerson().getAged();
			}
		}

		String[] s = new String[2];
		s[0] = h.getName();
		s[1] = a+"";
		return s;
	}

	public static String[] getYoungestLongevityMonarch(){
		List<Holder> l = Realm.getLineage(0);
		Holder h = l.get(0);
		int a = h.getPerson().getAged();
		for(Holder x: l){
			if (x.getPerson().getAged() < a){
				h = x;
				a = x.getPerson().getAged();
			}
		}

		String[] s = new String[2];
		s[0] = h.getName();
		s[1] = a+"";
		return s;
	}

	public static String[] getLongestReign(){
		List<Holder> l = Realm.getLineage(0);
		Holder h = l.get(0);
		int a = h.getReignLengthExact();
		for(Holder x: l){
			if (x.getReignLengthExact() > a){
				h = x;
				a = x.getReignLengthExact();
			}
		}

		String[] s = new String[2];
		s[0] = h.getName();
		s[1] = h.getReignYearsAndDays();
		return s;
	}

	public static String[] getShortestReign(){
		List<Holder> l = Realm.getLineage(0);
		Holder h = l.get(0);
		int a = h.getReignLengthExact();
		for(Holder x: l){
			if (x.getReignLengthExact() < a){
				h = x;
				a = x.getReignLengthExact();
			}
		}

		String[] s = new String[2];
		s[0] = h.getName();
		s[1] = h.getReignYearsAndDays();
		return s;
	}

	public static String[] getMostChildrenMonarch(){
		List<Holder> l = Realm.getLineage(0);
		Holder h = l.get(0);
		int a = h.getPerson().getChildren().size();
		for(Holder x: l){
			if (x.getPerson().isAdult() && x.getPerson().getChildren().size() > a){
				h = x;
				a = h.getPerson().getChildren().size();
			}
		}

		String[] s = new String[2];
		s[0] = h.getName();
		s[1] = a+"";
		return s;
	}

	public static String[] getFemaleMonarchsPercent(){
		List<Holder> l = Realm.getLineage(0);
		Holder h = l.get(0);
		int a = 0;
		for(Holder x: l){
			if (x.getPerson().isFemale()){
				h = x;
				a++;
			}
		}

		String[] s = new String[2];
		s[0] = "N/A";
		if (a != 0){
			s[1] = (((0.0f+a)/l.size()*100)+"");
			if (s.length >= 4){
				s[1] = s[1].substring(0,4)+"%";
			} else {
				s[1] = s[1].substring(0,3)+"%";
			}
		} else {
			s[1] = "0%";
		}
		return s;
	}

	public static String[] getMostInbred(){
		List<Holder> l = Realm.getLineage(0);
		Holder h = l.get(0);
		int a = Affinity.countInbreed(h.getPerson());
		int t; 	//Temp for inbreeding
		for(Holder x: l){
			t = Affinity.countInbreed(x.getPerson());
			if (t > a){
				h = x;
				a = t;
			}
		}

		String[] s = new String[2];
		s[0] = h.getName();
		s[1] = a+"%";
		return s;
	}


	public static String getMonarchialRecord(){
		StringBuffer s = new StringBuffer();
		s.append(HTML.getCaption("Monarchial record"));

		//The names of headers
		String[] th = {"Name", "Holder", "Record"};
		String[] m;							//Store monarchial record
		String temp = "";
		s.append(HTML.getTr(HTML.createTableHeader(th)));

		temp = HTML.getTd("Oldest");
		m = getOldestLongevityMonarch();
		temp += HTML.getTd(m[0]);
		temp += HTML.getTd(m[1]);
		s.append(HTML.getTr(temp));

		temp = HTML.getTd("Youngest");
		m = getYoungestLongevityMonarch();
		temp += HTML.getTd(m[0]);
		temp += HTML.getTd(m[1]);
		s.append(HTML.getTr(temp));

		temp = HTML.getTd("Longest reign");
		m = getLongestReign();
		temp += HTML.getTd(m[0]);
		temp += HTML.getTd(m[1]);
		s.append(HTML.getTr(temp));

		temp = HTML.getTd("Shortest reign");
		m = getShortestReign();
		temp += HTML.getTd(m[0]);
		temp += HTML.getTd(m[1]);
		s.append(HTML.getTr(temp));

		temp = HTML.getTd("Most children");
		m = getMostChildrenMonarch();
		temp += HTML.getTd(m[0]);
		temp += HTML.getTd(m[1]);
		s.append(HTML.getTr(temp));

		temp = HTML.getTd("Female rulers");
		m = getFemaleMonarchsPercent();
		temp += HTML.getTd(m[0]);
		temp += HTML.getTd(m[1]);
		s.append(HTML.getTr(temp));

		temp = HTML.getTd("Most inbred");
		m = getMostInbred();
		temp += HTML.getTd(m[0]);
		temp += HTML.getTd(m[1]);
		s.append(HTML.getTr(temp));


		return HTML.getTable(String.valueOf(s));

	}

	public static String getCensus(){
		StringBuffer s = new StringBuffer();
		s.append(HTML.getCaption("Census"));

		//The names of headers
		String[] th = {"YEAR", "LIVING", "MEN", "WOMEN", "BIRTHS", "DEATHS"};
		s.append(HTML.getTr(HTML.createTableHeader(th)));
		String[] census = Census.write();
		for(String x: census){
				s.append(x);
		}
		return HTML.getTable(String.valueOf(s));
	}


	public static void writeDemography(){
		//int[] eyeC = getEyeColorCensus();
		//int[] hairC = getHairColorCensus();
		int[][] pop = buildPopulationPyramid();

		StringBuffer s = HTML.getBeginning();
		s.append(getGeneralCensus());
		s.append(getAdultCensus());
		s.append(getReproductiveWomen());
		s.append(getMarriageCensus());
		s.append(getMonarchialRecord());
		s.append(getCensus());


		try {
			FileWriter writer = new FileWriter("Output/Demography.html", false);
			writer.write(String.valueOf(s));

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

	public static void writeConsorts(){
		String t  = String.valueOf(HTML.getBeginning());	//Short for text the written text will be stored here

		t += Consort.getHTML(Office.offices.get(0).getConsorts());

		t += HTML.getEnding();

		try {
			FileWriter writer = new FileWriter("Output/Consorts.html", false);
			writer.write(t);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void writeCabinet(){
		System.out.println(Office.offices.get(0).getCabinet().getRegister().getHTML());
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
