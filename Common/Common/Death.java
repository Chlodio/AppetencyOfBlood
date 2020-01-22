package Common;

import Human.Human;
public class Death {
	final static Integer[][] chance = {{100, 150, 200, 250, 300, 350, 400, 450, 500, 550 }, {650, 750, 850, 950, 1050, 1150, 1250, 1350, 1450, 1550 }, {1850, 2150, 2450, 2750, 3050, 3350, 3650, 3950, 4250, 4250 }, {4250, 4250, 4250, 4250, 4250, 4250, 4250, 4250, 4250, 4250 }, {3800, 3350, 2900, 2450, 2000, 1550, 1100, 650, 200, 100 }, {95, 90, 85, 80, 75, 70, 65, 60, 55, 50 }, {50, 49, 48, 47, 46, 45, 44, 43, 42, 41 }, {40, 39, 38, 37, 36, 35, 34, 33, 32, 31 }, {30, 29, 28, 27, 26, 25, 24, 23, 22, 21 }, {20, 19, 18, 17, 16, 15, 14, 13, 12, 11 }, {10, 9, 8, 7, 6, 5, 4, 3, 2, 1 }};
	public static void death(){}
	public static void check(Human actor, int maom){
		int a = actor.getAge();
	//	float as 1.0f;
		int v = 0;
		if (a >= 50 && !actor.isMale()){
			v = (int) (Death.chance[a/10][a%10]*1.75);
		} else {
			v = (int) (Death.chance[a/10][a%10]);
		}
		if (Basic.randint(v) == 0){
			int f = Basic.randint(maom)+1;
			Basic.dayC.get(f).add(actor); Basic.dayE.get(f).add(0);
		}
	}
}
