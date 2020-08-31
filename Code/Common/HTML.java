
package Code.Common;

public class HTML {

	//Intended for every HTML
	public static StringBuffer getBeginning(){
		StringBuffer s = new StringBuffer("<html>\n<head><meta charset='UTF-8'>\n<link type='text/css' rel='stylesheet' href='standard.css'/>\n</head><body>\n");
		return s;
	}

	public static String getEnding(){
		return "</body></html>";
	}

	//Wrap the text inside of table cell;
	public static String getTd(String s){
		return "<td>"+s+"</td>";
	}

	public static String getTdTitle(String t, String s){
		return "<td title='"+t+"'>"+s+"</td>";
	}


	public static String getTh(String s){
		return "<th>"+s+"</th>";
	}

	public static String getThClass(String s, String n){
		return "<th class='"+s+"'>"+n+"</th>";
	}

	//Get empty row
	public static String getTh(){
		return "<th></th>";
	}

	//Get th with colspan
	public static String getThColspan(int i){
		return "<th colspan='"+i+"'></th>";
	}

	//Get th with colspan with string
	public static String getThCoSpan(String s, int i){
		return "<th colspan='"+i+"'>"+s+"</th>";
	}

	public static String getTdRowspan(String s, int i){
		return "<td rowspan='"+i+"'>"+s+"</td>";
	}

	public static String getTdClassRowspan(String c, String s, int i){
		return "<td class='"+c+"' rowspan='"+i+"'>"+s+"</td>";
	}

	//Get td with a class assignment
	public static String getTdClass(String s, String n){
		return "<td class='"+s+"'>"+n+"</td>";
	}

	//Wrap inside of table row
	public static String getTr(String s){
		return "<tr>"+s+"</tr>\n";
	}

	//Italize
	public static String getI(String s){
		return "<i>"+s+"</i>";
	}

	public static String getBr(){
		return "<br>";
	}

	//Wrap div
	public static String getDivClass(String c, String s){
		return "<div class='"+c+"'>"+s+"</div>";
	}

	public static String getImgClass(String c, String s){
		return "<img class='"+c+"' src='"+s+"'></img>";
	}


	public static String getImg(String s){
		return "<img src='"+s+"'></img>";
	}

	public static String createTableHeader(String[] a){
		String s = "";
		for(String x: a){
			s += getTh(x);
		}
		return s;
	}

	public static String wrapDetails(String s){
		return "<details>"+s+"</details>";
	}

	public static String wrapSummary(String s){
		return "<summary>"+s+"</summary>";
	}

	public static String createTableHeaderClass(String[][] a){
		String s = "";
		for(String[] x: a){
			if (x[1] == ""){
				s += getTh(x[0]);
			} else {
				s += getThClass(x[1], x[0]);
			}
		}
		return s;
	}



	public static String getTable(String t){
		String s = "<table style='text-align:center;'>";	//Start table
		s += t;												//Add paragrap inside of table
		s += "</table>";									//End table
		return s;
	}

	//Adds the text inside of list item;
	public static String getLi(String s){
		return "<li>"+s+"</li>";
	}

	//Adds the text inside of unordered list;
	public static String getUl(String s){
		return "<ul>"+s+"</ul>";
	}

	//Second parameter is for class names
	public static String getUlClass(String s, String c){
		return "<ul class='"+c+"'>"+s+"</ul>";
	}

	//Adds the text inside of list item inside of unordered list;
	public static String getUlLi(String s){
		return getUl(getLi(s));
	}

	//Get underline
	public static String getU(String s){
		return "<u>"+s+"</u>";
	}


	public static String getH3(String s){
		return "<h3>"+s+"</h3>";
	}

	public static String getHr(){
		return "<hr>";
	}

	public static String getP(String s){
		return "<p>"+s+"</p>";
	}

	public static String getPTitle(String a, String b){
		return "<p title='"+a+"'>"+b+"</p>";
	}

}
