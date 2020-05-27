package Code.Common;
import Code.Common.Basic;

public enum Nick {
  ABLE("the Able", 2),
  ACCURSED("the Accursed", 3),
  BAD("the Bad", 4),
  BALD("the Bald", 3),
  BLACK("the Black", 6),
  BOLD("the Bold", 4),
  BRAVE("the Brave", 5),
  CHASTE("the Chaste", 4),
  CONQUEROR("the Conqueror", 7),
  CRUEL("the Cruel", 5),
  DESIRED("the Desired", 1),
  DEVIL("the Devil", 4),
  DRUNKARD("the Drunkard", 3),
  FAIR("the Fair", 7),
  FAT("the Fat", 7),
  FORTUNATE("the Fortunate", 3),
  GENEROUS("the Generous", 3),
  GENTLE("the Gentle", 3),
  GLORIOUS("the Glorious", 3),
  GOOD("the Good", 10),
  HAMMER("the Hammer", 3),
  HANDSOME("the Handsome", 5),
  HOLY("the Holy", 3),
  HUNTER("the Hunter", 3),
  ILLUSTRIOUS("the Illustrious", 4),
  IRON("the Iron", 6),
  JUST("the Just", 5),
  KIND("the Kind", 2),
  LAME("the Lame", 3),
  LIBERAL("the Liberal", 3),
  LION("the Lion", 7),
  MAD("the Mad", 4),
  MAGNANIMOUS("the Magnanimous", 10),
  MAGNIFICENT("the Magnificent", 5),
  PEACEFUL("the Peaceful", 5),
  PIOUS("the Pious", 10),
  PRUDENT("the Prudent", 2),
  QUARRELLER("the Quarreller", 2),
  RED("the Red", 8),
  RIGHTEOUS("the Righteous", 4),
  SILENT("the Silent", 2),
  SIMPLE("the Simple", 4),
  SOLDIER("the Soldier", 3),
  STRICT("the Strict", 3),
  STRONG("the Strong", 4),
  TALL("the Tall", 4),
  SHORT("the Short", 4),
  TERRIBLE("the Terrible", 4),
  THUNDERBOLT("the Thunderbolt", 2),
  TROUBADOUR("the Troubadour", 2),
  UNLUCKY("the Unlucky", 2),
  VALIANT("the Valiant", 4),
  VICTORIOUS("the Victorious", 5),
  WARLIKE("the Warlike", 7),
  WELLBELOVED("the Well-Beloved", 2),
  WHITE("the White", 2),
  WICKED("the Whicked", 2),
  WISE("the Wise", 10),
  YELLOW("the Yellow", 2),
  POSTHUMOUS("the Posthumous", 3),
  BASTARD("the Bastard", 5),
  CHILD("the Child", 5),
  YOUNG("the Young", 5),
  OLD("the Old", 10),
  ELDER("the Elder", 4);
  public String name;
  public byte rarity;
  Nick(String s, int r){
    this.name = s;
    this.rarity = (byte) r;
  }
    //FORTUNATE
    public static Nick[] list = {ABLE, ACCURSED, BAD, BLACK, BOLD, BRAVE, CHASTE, CONQUEROR, CRUEL, DEVIL, DRUNKARD, FAIR, FAT, GENEROUS, GENTLE, GLORIOUS, GOOD, HAMMER, HANDSOME, HOLY, HUNTER, ILLUSTRIOUS, IRON, JUST, KIND, LAME, LIBERAL, LION, MAD, MAGNANIMOUS, MAGNIFICENT, PEACEFUL, PIOUS, PRUDENT, QUARRELLER, RED, RIGHTEOUS, SILENT, SIMPLE, SOLDIER, STRICT, STRONG, TALL, SHORT, TERRIBLE, THUNDERBOLT, TROUBADOUR, UNLUCKY, VALIANT, VICTORIOUS, WARLIKE, WELLBELOVED, WHITE, WICKED, WISE, YELLOW};

    public String getName(){
      return this.name;
    }

    public int getRarity(){
      return this.rarity;
    }

    public boolean isRarerThan(Nick n){
      return this.getRarity() < n.getRarity();
    }

    public static Nick getRandom(){
      return Basic.choice(list);
    }

}
