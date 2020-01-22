package Looks;
import java.util.Random;
import Common.Basic;

public class Eye {
    private static Random randy = new Random();
    private static String[] colorName = {"brown", "blue", "green"};
	private static int[] brownBrown = {90, 99, 100};
	private static int[] brownBlue = {68, 98, 100};
	private static int[] brownGreen = {68, 90, 100};
	private static int[] blueBlue = {95, 0, 100};
	private static int[] blueGreen = {77, 0, 100};
	private static int[] greenGreen = {0, 0, 100};

	private static int[][][] color = {
		{brownBrown, brownBlue, brownGreen},
		{brownBlue, blueBlue, blueGreen},
		{brownGreen, blueGreen, greenGreen}
	};

    public static String getColor(int x){
		return colorName[x];
	}

	public static String getRandom(){
		int r = 			Basic.randint(100);
		if(r < 78){			return Integer.toString(0);	}
		else if(r < 98){ 	return Integer.toString(1);	}
		else{				return Integer.toString(2);	}
	}

    public static String getGenetic(int father, int mother){
		return Integer.toString(getRandom(color[father][mother]));
    }

    public static int getRandom(int[] list){
        int r = randy.nextInt(100);
        for (int x = 0; x < list.length; x++){
            if (list[x] > r){
                return x;
            }
        }
        return 69;
    }



}
