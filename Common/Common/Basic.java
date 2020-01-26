package Common;
import Ancestry.Bastard;
import Common.*;
import House.House;
import Human.*;
import Interest.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import Politics.*;
import Relationship.*;



public class Basic {
	public static Calendar date = 									Calendar.getInstance();
	public static List<String> census = 							new ArrayList<>();
	public static Map<Integer, ArrayList<Human>> monthC = 			new HashMap<>();
	public static Map<Integer, House> house = 						new HashMap<>();
	public static String row = 										"%-23s %s–%s\t%s\t%s\t%s\n";
    public static Map<Integer, ArrayList<Integer>> monthE = 		new HashMap<>();
    public static Map<Integer, Human> human = 						new HashMap<>();
    public static Map<Integer, List<Human>> dayC = 					new HashMap<>();
    public static Map<Integer, List<Integer>> dayE = 				new HashMap<>();
    public static Random randomizer = 								new Random();
    public static SimpleDateFormat format1 = 						new SimpleDateFormat("yyyy-MM-dd");

	public static void performSetup(){

		Religion.foundReligion();


 		House.numberNobleHouses();
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
		Basic.date.set(1000,0,1);

		for (int x = 0; x < 100; x++){
			Human.createFamily();
		}

		House.ennobleFirst(10);

		if (Man.singles.size() != 0 || Woman.singles.size() != 0){
			Human w;
			int d;
			int dew = 0;
			List<Human> ms = new ArrayList<>(Man.singles);
			for (Human x: ms){
				dew++;
				if (Marriage.hasWomanSingles(x)){
					w = Marriage.findWife(x);
					d = x.getAgeDifference(w);
					Marriage.marrySpecial(x, w, d);
					for (int y = d; y >= 0; y--){
						if (Basic.randint(4) == 0){
							w.deliverSpecial(x, y);
						}
					}
				} else {
					break;
				}
			}
		}
		Office.create();
		Death.death();

		int dom = 1; 										//day of the month
		int maom = 0;

        for (int century = 0; century < 3; century++){
			for (int decade = 0; decade < 10; decade++){
				census.add("<tr><td>"+Basic.sDate()+"</td><td>"+Man.getAmount()+"</td><td>"+Woman.getAmount()+"</td><td>"+Bastard.getAmount()+"</td></tr>");
				for (int lustrum = 0; lustrum < 2; lustrum++){
					for (int annum = 0; annum < 5; annum++){
						Man.growUp();
						Woman.growUp();
						SexRelation.doAnnual();
						for (Human x: Man.singles){ 							Mating.revaluateM(x);}
						for (Human x: Woman.singles){ 							Mating.revaluateF(x);}
						for (Office x: Office.offices){ 						x.doAccounting();}
						Mating.retire();
						for (int month = 1; month != 13; month++){
						   maom = Basic.date.getActualMaximum(Calendar.DAY_OF_MONTH);
						   for (Office x: Office.offices){
							   if(x.isAtWar()){
								   InterestImperialism.handleSiege(x);
							   }
						   }
						   for (Human x: Human.living){ 		Death.check(x, maom);		}
						   for (Human x: Man.singles){ 			Marriage.propose(x, maom);	}
						   Marriage.doBreeding();
						   Affair.doAdultery();
						   for (Human x: Woman.pregnant){x.ovulate(maom);}
						   while(dom != maom){
							   for (int x0 = 0; x0 != dayC.get(dom).size();x0++){
								   dayC.get(dom).get(x0).saunter(dayE.get(dom).get(x0));
							   }
							   Basic.date.add(Calendar.DATE, 1);
							   dom = Basic.date.get(Calendar.DAY_OF_MONTH);
						   }
						   for (int xc = 1; xc < 32; xc++){
							   dayC.get(xc).clear();
							   dayE.get(xc).clear();
						   }
						   for (int xc = 1; xc != 13; xc++){
							   monthC.get(xc).clear();
							   monthE.get(xc).clear();
				   			}
						   Basic.date.add(Calendar.DATE, 1);
						   dom = Basic.date.get(Calendar.DAY_OF_MONTH);
			            }
						Marriage.flushMonthlyWedding();
					}
				}
			}
        }

		Writing.writeMonarchList();
		Writing.writeTable();
		Writing.writeSummary();
		Writing.writeDemography();
		System.out.println(Marriage.failedMarriageAttemp);
		System.out.println("COMPLETED");

	/*	List<Holder> xt =  Realm.getLineage(1);
		for(Holder x: xt){
			System.out.println(x.getPerson().getFullName());
		}*/

	}

	public static String sDate(){ 					return format1.format(date.getTime()); }

    public static void print(String printable){
		System.out.println(Basic.sDate()+": "+printable+".");
	}

	public static int randint(int n){
		return randomizer.nextInt(n);
	}

	public static byte randite(int n){
		return (byte) randomizer.nextInt(n);
	}

	public static byte randite(int n, int s){
		return (byte) (randomizer.nextInt(n)-s);
	}

	public static String choice(String[] list){
		if (list.length < 1 ){
			throw new RuntimeException();
		}
		return list[randomizer.nextInt(list.length)];
	}

	public static <T> T choice(List<T> list){
		if (list.size() < 1 ){
			throw new RuntimeException();
		}
		return list.get(randomizer.nextInt(list.size()));
	}

	public static int getDaysLived(Calendar c){
		int d = Basic.date.get(Calendar.DATE)-c.get(Calendar.DATE);
		int m = (Basic.date.get(Calendar.MONTH)-c.get(Calendar.MONTH));
		m = (int) (m * 30.416f);
		int y = (Basic.date.get(Calendar.YEAR)-c.get(Calendar.YEAR))*365;
		return (y+m+d);
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

	public static String getOrdial(int i){
		i++;
        if (i > 11 && i < 20){
            return i+"th";
        } else{
            int l = getLastDigit(i);
            if (l > 3){
                return i+"th";
            } else {
                switch(l){
                    case 1: return i+"st";
                    case 2: return i+"nd";
                    case 3: return i+"rd";
                    default: return i+"th";
                }
            }
        }
    }

    public static int getLastDigit(int x){
        return x - ((x / 10) * 10);
    }

	public static String getRoman(int n){ 		return Basic.roman[n-1]; }
	public static String getOrder(int v){		return order[v]; }
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

	public static String getCardinal(int i){
        int v = i/10;
        String s = "";
        switch(v/10){
            case 0:
                break;
            case 1:
                s = "hundred";
                if (i-(v*10) != 0){
                    s += "-";
                } else {
                    return s;
                }
                i -= 100;
                v -= v;

        }

        if (v == 0){
            return s+cardinalDigit_1[i];
        } else {
            if (i < 20){
                return s+cardinalDigit_2[i-10];
            } else {
                if ((i-(v*10)) != 0){
                    return s+cardinalDigit_3[v]+"-"+cardinalDigit_1[i-(v*10)];
                } else {
                    return s+cardinalDigit_3[v];
                }
            }
        }
    }

    final private static String[] cardinalDigit_1 = {"none", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine"};
    final private static String[] cardinalDigit_2 = {"ten", "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen", "seventeen", "eighteen", "nineteen"};
    final private static String[] cardinalDigit_3 = {"", "ten", "twenty", "thirty", "forty", "fifty", "sixty", "seventy", "eighty", "ninety"};


	private final static String[] roman = {"I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X", "XI", "XII", "XIII", "XIV", "XV", "XVI", "XVII", "XVIII", "XIX", "XX", "XXI", "XXII", "XXIII", "XXIV", "XXV", "XXVI", "XXVII", "XXVIII", "XXIX", "XXX"};

	final static String[] order = {"first", "second", "third", "fourth", "fifth", "sixth", "seventh", "eighth", "ninth", "tenth", "eleventh", "twelveth", "thirteenth"};
	final static String[] orderShort = {"1st", "2nd", "3rd", "4th", "5th", "6th", "7th", "8th", "9th", "10th", "11th", "12th", "13th", "14th", "15th", "16th", "17th", "18th", "19th", "20th", "21st", "22nd", "23rd", "24th", "25th", "26th", "27th", "28th", "29th", "30th", "31st", "32nd", "33rd", "34th", "35th", "36th", "37th", "38th", "39th", "40th", "41st", "42nd", "43rd", "44th", "45th", "46th", "47th", "48th", "49th", "50th"};

}
