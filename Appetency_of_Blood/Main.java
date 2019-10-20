import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;


enum Event{
	E101,
	E200,
	E201;
}

class Main {
	public static Map<Integer, ArrayList<Human>> monthC = 			new HashMap<>();
	public static String row = 										"%-23s %s–%s\t%s\t%s\t%s\n";
    public static Calendar date = 									Calendar.getInstance();
    public static Map<Integer, ArrayList<Integer>> monthE = 		new HashMap<>();
    public static Map<Integer, House> house = 						new HashMap<>();
    public static Map<Integer, Human> human = 						new HashMap<>();
    public static Map<Integer, List<Human>> dayC = 					new HashMap<>();
    public static Map<Integer, List<Integer>> dayE = 				new HashMap<>();
    public static Random randomizer = 								new Random();
    public static SimpleDateFormat format1 = 						new SimpleDateFormat("yyyy-MM-dd");
	public static List<String> census = new ArrayList<>();
    public static void main(String[] args) {

		Religion.foundReligion();
 		House.numberHouses();
		Name.buildForenames();
		House.buildNames();
        for (int x = 1; x != 32; x++){
			dayC.put(x, new ArrayList<>());
			dayE.put(x, new ArrayList<>());
		}

        for (int x = 1; x != 13; x++){
			monthC.put(x, new ArrayList<>());
			monthE.put(x, new ArrayList<>());
		}

		date.set(1000,0,1);

		for (int x = 0; x < 90; x++){
			new Man(20);
			new Woman(15, House.getHouse(House.getId()));
			Marriage.marryFiancee(human.get(Human.getID()-1), human.get(Human.getID()));
		}

		Office.create();
		int dom = 1; 										//day of the month
		int maom = 0;
		Death.death();
        for (int century = 0; century < 5; century++){
			for (int decade = 0; decade < 10; decade++){
				census.add("<tr><td>"+sDate()+"</td><td>"+Man.getAmount()+"</td><td>"+Woman.getAmount()+"</td></tr>");
				for (int lustrum = 0; lustrum < 2; lustrum++){
					for (int annum = 0; annum < 5; annum++){
						Man.growUp();
						Woman.growUp();
						Marriage.doAnnual();
						for (Human x: Man.singles){ 							Mating.revaluateM(x);}
						for (Human x: Woman.singles){ 							Mating.revaluateF(x);}
						for (Office x: Office.offices){ 						x.doAccounting();}
						Mating.retire();
						for (int month = 1; month != 13; month++){
						   maom = date.getActualMaximum(Calendar.DAY_OF_MONTH);
						   for (Office x: Office.offices){
							   if(x.isAtWar()){
								   InterestImperialism.handleSiege(x);
							   }
						   }
						   for (Human x: Human.living){ 		Death.check(x, maom);		}
						   for (Human x: Man.singles){ 			Marriage.propose(x, maom);	}
						   Marriage.doBreeding();
						   for (Human x: Woman.pregnant){x.ovulate(maom);}
						   while(dom != maom){
							   for (int x0 = 0; x0 != dayC.get(dom).size();x0++){
								   dayC.get(dom).get(x0).saunter(dayE.get(dom).get(x0));
							   }
							   date.add(Calendar.DATE, 1);
							   dom = date.get(Calendar.DAY_OF_MONTH);
						   }
						   for (int xc = 1; xc < 32; xc++){
							   dayC.get(xc).clear();
							   dayE.get(xc).clear();
						   }
						   for (int xc = 1; xc != 13; xc++){
							   monthC.get(xc).clear();
							   monthE.get(xc).clear();
				   			}
						   date.add(Calendar.DATE, 1);
						   dom = date.get(Calendar.DAY_OF_MONTH);
			            }
						Marriage.flushMonthlyWedding();
					}
				}
			}
        }

//		System.out.println((0.0f+Marriage.gen)/Human.getID());
//		System.out.println("#NAME\t\t\t#LIFE\t\t#SPOU.\t#CHIL.\t#SCHI");
		Writing.writeMonarchList();
		Writing.writeTable();
		Writing.writeSummary();
		Writing.writeDemography();
		//Writing.buildPopulationPyramid();



	///	System.out.println("Weddings per populations:\t"+Marriage.getMonthlyWeddingAverage());
		int amoc = 0;
		int cwsth = 0;
		//Set<Human> keys = human.keySet();
		for (Human x: human.values()){
			if (x.sex == false && x.aged() >= 20 && x.children().size() >= 1){
				for(Human y: x.children()){
					if (!y.isAlive()){
						amoc++;
						if (y.aged() >= 20){
							cwsth++;
						}
					}
				}
			}
		}
		System.out.println("COMPLETED");
		//System.out.println(((0.0+cwsth)/amoc)+"<br>"+cwsth+"<br>"+amoc);
//		System.out.println("Virgins: "+(Math.round((0.0+Mating.getVirgings())/Human.id*100.0)/100.0));
//		System.out.println("Inbreeding: "+Consanguinity.countInbreed(Holder.sovereign));



    }
	public static void geno(Human father){
		String x;
		if (father.death != null){ x = Integer.toString(father.death.get(Calendar.YEAR)); }
		else { x = "PRSNT"; }
		String y = "0";
		String z = "0";
		List<Human> children = father.children();
		int a = 0;
		if (father.marriages != null){
			y = Integer.toString(father.marriages.size());
			if (father.sons != null){
				z = Integer.toString(children.size());
			}
			for (Human u: children){
				if (u.aged() >= 20){ a++;}
			}
		}
		System.out.printf(row, father.getName().getFull(), father.birth.get(Calendar.YEAR), x, y, z, a);
	}
    public static void jump(int y, String t) {
        System.out.println(y); System.out.println(t);
    }
    public static int randint(int n){
        return randomizer.nextInt(n);
    }
	public static String choice(String[] list){
		return list[randomizer.nextInt(list.length)];
    }
	public static <T> T choice(List<T> list){
		return list.get(randomizer.nextInt(list.size()));
    }


	public static int[] zipf(int n){
        int[] ar = new int[n];
        ar[0] = 100;
        for(int x = 1; x < n; x++){
            ar[x] = ar[x-1]+(ar[0]/(x+1));
        }
        return ar;
    }

	public static String choiceW(List<String> list){
		int[] weight = zipf(list.size());
		int r = randomizer.nextInt(weight[weight.length-1]);
		for(int x = 0; x < weight.length; x++){
			if(r < weight[x]){
				return list.get(x);
			}
		}
		return "?";
	}

	public static Interest.Point choiceIP(Interest.Point[] list){
		return list[randomizer.nextInt(list.length)];
	}
    public static String sDate(){ 				return format1.format(date.getTime()); }
    public static void print(String printable){ System.out.println(Main.sDate()+": "+printable+"."); }
	public static String getRoman(int n){ 		return Main.roman[n-1]; }
	public static String getOrder(int v){		return order[v]; }
	public static String getOrderShort(int v){	return orderShort[v]; }
	public static double max(double n1, double n2){
		if (n1 >=  n2){ return n1; }
		else{ return n2; }
	}
	public static double min(double n1, double n2){
		if (n1 <=  n2){ return n1; }
		else{ return n2; }
	}
	public static float max(float n1, float n2){
		if (n1 >=  n2){ return n1; }
		else{ return n2; }
	}
	public static float min(float n1, float n2){
		if (n1 <=  n2){ return n1; }
		else{ return n2; }
	}
	public static int max(int n1, int n2){
		if (n1 >=  n2){ return n1; }
		else{ return n2; }
	}
	public static int min(int n1, int n2){
		if (n1 <=  n2){ return n1; }
		else{ return n2; }
	}

	public static String capitalize(String it){
		return Character.toUpperCase(it.charAt(0))+it.substring(1);
	}

	public static String toLowerCase(String it){
		return Character.toLowerCase(it.charAt(0))+it.substring(1);
	}

	public static void pause(int ms){
		try{
			Thread.sleep(ms);
		}
		catch(InterruptedException e){

		}
	}

	private final static String[] roman = {"I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X", "XI", "XII", "XIII", "XIV", "XV", "XVI", "XVII", "XVIII", "XIX", "XX", "XXI", "XXII", "XXIII", "XXIV", "XXV", "XXVI", "XXVII", "XXVIII", "XXIX", "XXX"};
	final static String[] cardinal = {"none", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten", "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen", "seventeen", "eighteen", "nineteen", "twenty", "twenty-one", "twenty-two", "twenty-three", "twenty-four", "twenty-fife", "twenty-six", "twenty-seven", "twenty-eight", "twenty-nine", "thirty", "thirty-one", "thirty-two", "thirty-three", "thirty-four", "thirty-fife", "thirty-six", "thirty-seven", "thirty-eight", "thirty-nine", "forty", "forty-one", "forty-two", "forty-three", "forty-four", "forty-fife", "forty-six", "forty-seven", "forty-eight", "forty-nine", "fifty", "fifty-one", "fifty-two", "fifty-three", "fifty-four", "fifty-fife", "fifty-six", "fifty-seven", "fifty-eight", "fifty-nine", "sixty", "sixty-one", "sixty-two", "sixty-three", "sixty-four", "sixty-fife", "sixty-six", "sixty-seven", "sixty-eight", "sixty-nine", "seventy", "seventy-one", "seventy-two", "seventy-three", "seventy-four", "seventy-fife", "seventy-six", "seventy-seven", "seventy-eight", "seventy-nine", "eighty", "eighty-one", "eighty-two", "eighty-three", "eighty-four", "eighty-fife", "eighty-six", "eighty-seven", "eighty-eight", "eighty-nine", "ninety", "ninety-one", "ninety-two", "ninety-three", "ninety-four", "ninety-fife", "ninety-six", "ninety-seven", "ninety-eight", "ninety-nine", "hundred", "hundred-one", "hundred-two", "hundred-three", "hundred-four", "hundred-fife", "hundred-six", "hundred-seven", "hundred-eight", "hundred-nine"};
	final static String[] order = {"first", "second", "third", "fourth", "fifth", "sixth", "seventh", "eighth", "ninth", "tenth", "eleventh", "twelveth", "thirteenth"};
	final static String[] orderShort = {"1st", "2nd", "3rd", "4th", "5th", "6th", "7th", "8th", "9th", "10th", "11th", "12th", "13th", "14th", "15th", "16th", "17th", "18th", "19th", "20th", "21st", "22nd", "23rd", "24th", "25th", "26th", "27th", "28th", "29th", "30th", "31st", "32nd", "33rd"};
	//Female adoslence
	public static final String[] FApic = {"001", "002", "003", "004", "005", "006", "007", "008", "009", "010", "011", "012", "013", "014", "015", "016", "017", "018", "019",  "020", "021", "022", "023", "024", "025", "026", "027", "028", "029", "030", "031", "032", "033"};
	//Female young adult
	public static final String[] FYpic = {"001", "002", "003", "004", "005", "006", "007", "008", "009", "010", "011", "012", "013", "014", "015", "016", "017", "018", "019",  "020", "021", "022", "023", "024", "025", "026", "027", "028", "029", "030", "031", "032", "033"};
	//Male adoslence
	public static final String[] MApic = {"001", "002", "003", "004", "005", "006", "007", "008", "009", "010", "011", "012", "013"};
	 //Male young adult
	public static final String[] MYpic = {"001", "002", "003", "004", "005", "006", "007", "008", "009", "010", "011", "012"};
	//Male middle-aged
	public static final String[] MMpic = {"001", "002", "003", "004", "005", "006", "007", "008", "009", "010"};
	//Male elder
	public static final String[] MEpic = {"001", "002"};
}


//==="{{"&E1&", "&E2&", "&E3&", "&E4&", "&E5&", "&E6&", "&E7&", "&E8&", "&E9&", "&E10&" "&"}, {"&E11&", "&E12&", "&E13&", "&E14&", "&E15&", "&E16&", "&E17&", "&E18&", "&E19&", "&E20&" "&"}, {"&E21&", "&E22&", "&E23&", "&E24&", "&E25&", "&E26&", "&E27&", "&E28&", "&E29&", "&E30&" "&"}, {"&E31&", "&E32&", "&E33&", "&E34&", "&E35&", "&E36&", "&E37&", "&E38&", "&E39&", "&E40&" "&"}, {"&E41&", "&E42&", "&E43&", "&E44&", "&E45&", "&E46&", "&E47&", "&E48&", "&E49&", "&E50&" "&"}, {"&E51&", "&E52&", "&E53&", "&E54&", "&E55&", "&E56&", "&E57&", "&E58&", "&E59&", "&E60&" "&"}, {"&E61&", "&E62&", "&E63&", "&E64&", "&E65&", "&E66&", "&E67&", "&E68&", "&E69&", "&E70&" "&"}, {"&E71&", "&E72&", "&E73&", "&E74&", "&E75&", "&E76&", "&E77&", "&E78&", "&E79&", "&E80&" "&"}, {"&E81&", "&E82&", "&E83&", "&E84&", "&E85&", "&E86&", "&E87&", "&E88&", "&E89&", "&E90&" "&"}, {"&E91&", "&E92&", "&E93&", "&E94&", "&E95&", "&E96&", "&E97&", "&E98&", "&E99&", "&E100&" "&"}, {"&E101&", "&E102&", "&E103&", "&E104&", "&E105&", "&E106&", "&E107&", "&E108&", "&E109&", "&E110&", "&"}}"
