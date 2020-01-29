package Code.Looks;
import Code.Common.Basic;
import java.util.Random;

public class Hair {
	private static Random randy = new Random();
    private static final String[] colorName = {"black", "brown", "blond", "straw.", "red"};
	private static final int[] blackBlack = {100};
	private static final int[] blackBrown = {50, 100};
	private static final int[] blackBlond = {0, 100};
	private static final int[] brownBrown = {25, 74, 86, 97, 100};
	private static final int[] brownBlond = {0, 50, 84, 100};
	private static final int[] brownStraw = {0, 50, 67, 92, 100};
	private static final int[] brownRed = {0, 50, 0, 84, 100};
	private static final int[] blondBlond = {0, 0, 100};
	private static final int[] blondStraw = {0, 0, 50, 100};
	private static final int[] blondRed = {0, 0, 0, 100};
	private static final int[] strawStraw = {0, 0, 25, 75, 100};
	private static final int[] strawRed = {0, 0, 0, 50, 100};
	private static final int[] redRed = {0, 0, 0, 0, 100};
	private static final int[][][] color = {
		{blackBlack, blackBrown, blackBlond, blackBlond, blackBlond},
		{blackBrown, brownBrown, brownBlond, brownStraw, brownRed},
		{blackBlond, brownBlond, blondBlond, blondStraw, blondRed},
		{blackBlond, brownStraw, blondStraw, strawStraw, strawRed},
		{blackBlond, brownStraw, blondStraw, strawRed, strawStraw},
	};
    public static String getColor(int x){
		return colorName[x];
	}

	public static String getRandom(){
		return  			Integer.toString(Basic.randint(5));
//		int r = 			Basic.randint(100);
//		if(r < 75){			return Integer.toString(0);	}
//		else if(r < 86){ 	return Integer.toString(1);	}
//		else if(r < 96){ 	return Integer.toString(2);	}
//		else if(r < 98){ 	return Integer.toString(3);	}
//		else{
//							return Integer.toString(4);
//		}
	}

	public static String getGenetic(int father, int mother){
		return Integer.toString(getRandom(color[father][mother]));
    }

	public static int getRandom(int[] list){
        int r = randy.nextInt(100);;
        for (int x = 0; x < list.length; x++){
            if (list[x] > r){
                return x;
            }
        }
        return 69;
    }


}
