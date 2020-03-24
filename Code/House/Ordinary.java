package Code.House;
import Code.Common.Basic;
import Code.Common.HTML;

  public enum Ordinary{
    CROSSPLAIN("cross_plain", false),
    CROSSANNULETY("cross_annulety", false),
    CROSSBOTTONY("cross_bottony", false),
    CROSSCALVATRY("cross_calvatry", false),
    CROSSCOPTIC("cross_coptic", false),
    CROSSCOUPED("cross_couped", false),
    CROSSCRESCENTY("cross_crescenty", false),
    CROSSDOUBLED("cross_doubled", false),
    CROSSJERUSALEM("cross_Jerusalem", false),
    CROSSLATIN("cross_latin", false),
    CROSSLORRAINE("cross_Lorraine", false),
    CROSSNORSESUN("cross_norse_sun", false),
    CROSSPARTEDFRETTED("cross_parted_fretted", false),
    CROSSPATRIARCHAL("cross_patriarchal", false),
    CROSSPOMELLY("cross_pomelly", false),
    CROSSPOMMELED("cross_pommeled", false),
    CROSSPOTENT("cross_potent", false),
    CROSSQUARTERPIERCED("cross_quarter-pierced", false),
    CROSSAVELLANE("cross_avellane", false),
    CROSSFORMERLY("cross_formerly", false),
    CROSSFORMERLYFITCH("cross_formerly_fitchy", false),
    CROSSCLECHY("cross_clechy", false),
    CHIEFPLAIN("chief_plain", false),
    CHIEFENARCHED("chief_enarched", false),
    CHIEFTRIANGULAR("chief_triangular", false),
    CHIEFDOUBLEARCHED("chief_double-arched", false),
    CHIEFPALE("chief_pale", false),
    BASE("base", false),
    ORLE("orle", false),
    FORD("ford", false),
    QUARTER("quarter", false),
    TIERCEPLAIN("tierce_plain", false),
    TIERCESINISTER("tierce_sinister", false),
    WALLPLAIN("wall_plain", false),
    FLAUNCHES("flaunches", false),
    SALTIRECOUPED("saltire_couped", false),
    SALTIRE("saltire", false),
    PALLPLAIN("pall_plain", false),
    PALLINVERTED("pall_inverted", false),
    SHAKEFORK("shakefork", false),
    CROSSPATONCE("cross_patonce", false),
    CROSSBOWEN("cross_bowen", false),
    HEART("heart", true),
    ROUNDEL("roundel", true),
    CROWN("crown", true),
    DIAMOND("diamond", true),
    SPADE("spade", true),
    GEMEL("gemel", false),
    WHEEL("wheel", false);


  private static final Ordinary[] ordinaries = {CROSSPLAIN, CROSSANNULETY, CROSSBOTTONY, CROSSCALVATRY, CROSSCOPTIC, CROSSCOUPED, CROSSCRESCENTY, CROSSDOUBLED, CROSSJERUSALEM, CROSSLATIN, CROSSLORRAINE, CROSSNORSESUN, CROSSPARTEDFRETTED, CROSSPATRIARCHAL, CROSSPOMELLY, CROSSPOMMELED, CROSSPOTENT, CROSSQUARTERPIERCED, CROSSAVELLANE, CROSSFORMERLY, CROSSFORMERLYFITCH, CROSSCLECHY, CHIEFPLAIN, CHIEFENARCHED, CHIEFTRIANGULAR, CHIEFDOUBLEARCHED, CHIEFPALE, BASE, ORLE, FORD, QUARTER, TIERCEPLAIN, TIERCESINISTER, WALLPLAIN, FLAUNCHES, SALTIRECOUPED, SALTIRE, PALLPLAIN, PALLINVERTED, SHAKEFORK, CROSSPATONCE, CROSSBOWEN, HEART, ROUNDEL, CROWN, DIAMOND, SPADE, GEMEL};

  private String[] semis = {"ace", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten"};

  private String type;
  private boolean semi;

  private Ordinary(String s, boolean b){
      this.type = s;
      this.semi = b;
  }

  public String getType(){
    return this.type;
  }

  public boolean isSemi(){
    return this.semi;
  }

  public String getSemi(){
    return semis[1+Basic.randint(9)];
  }

  public static String getOrdinary(){
      Ordinary o = ordinaries[Basic.randint(ordinaries.length)];
      if (o.isSemi()){
        if (Basic.coinFlip()){
          return o.getType()+"_ace";
        }
        else {
          return o.getType()+"_"+o.getSemi();
        }
      } else {
        return o.getType();
      }
  }

}
