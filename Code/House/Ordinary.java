package Code.House;
import Code.Common.Basic;
import Code.Common.HTML;

  public enum Ordinary{
    ANCHOR("anchor", false),
    BASE("base", false),
    CHIEFDOUBLEARCHED("chief_double-arched", false),
    CHIEFENARCHED("chief_enarched", false),
    CHIEFPALE("chief_pale", false),
    CHIEFPLAIN("chief_plain", false),
    CHIEFTRIANGULAR("chief_triangular", false),
    CROSSANNULETSBRACED("cross_annulets_braced", false),
    CROSSANNULETY("cross_annulety", false),
    CROSSAVELLANE("cross_avellane", false),
    CROSSBOTTONY("cross_bottony", false),
    CROSSBOWEN("cross_bowen", false),
    CROSSCALVATRY("cross_calvatry", false),
    CROSSCANTERBURY("cross_canterbury", false),
    CROSSCLECHY("cross_clechy", false),
    CROSSCOLDHARBOUR("cross_coldharbour", false),
    CROSSCOPTIC("cross_coptic", false),
    CROSSCOUPED("cross_couped", false),
    CROSSCRESCENTY("cross_crescenty", false),
    CROSSDOUBLED("cross_doubled", false),
    CROSSFORMERLY("cross_formerly", false),
    CROSSFORMERLYFITCH("cross_formerly_fitchy", false),
    CROSSFUSILS("cross_fusils", false),
    CROSSJERUSALEM("cross_Jerusalem", false),
    CROSSLATIN("cross_latin", false),
    CROSSLORRAINE("cross_Lorraine", false),
    CROSSLOZENGES("cross_lozenges", false),
    CROSSMASCLES("cross_mascles", false),
    CROSSNORSESUN("cross_norse_sun", false),
    CROSSPARTEDFRETTED("cross_parted_fretted", false),
    CROSSPATONCE("cross_patonce", false),
    CROSSPATRIARCHAL("cross_patriarchal", false),
    CROSSPLAIN("cross_plain", false),
    CROSSPOMELLY("cross_pomelly", false),
    CROSSPOMMELED("cross_pommeled", false),
    CROSSPOTENT("cross_potent", false),
    CROSSQUARTERPIERCED("cross_quarter-pierced", false),
    CROSSSIXANNULETSINTERLACED("cross_six_annulets_interlaced", false),
    CROWN("crown", true),
    DIAMOND("diamond", true),
    FLAUNCHES("flaunches", false),
    FORD("ford", false),
    FOURPILES("four_piles", false),
    TENPILES("ten_piles", false),
    GEMEL("gemel", false),
    HEART("heart", true),
    ORLE("orle", false),
    PALLINVERTED("pall_inverted", false),
    PALLPLAIN("pall_plain", false),
    QUARTER("quarter", false),
    ROUNDEL("roundel", true),
    SALTIRE("saltire", false),
    SALTIRECOUPED("saltire_couped", false),
    SHAKEFORK("shakefork", false),
    SPADE("spade", true),
    TIERCEPLAIN("tierce_plain", false),
    TIERCESINISTER("tierce_sinister", false),
    WALLPLAIN("wall_plain", false),
    WHEEL("wheel", false);

  private static final Ordinary[] ordinaries = {CROSSPLAIN, CROSSANNULETY, CROSSBOTTONY, CROSSCALVATRY, CROSSCOPTIC, CROSSCOUPED, CROSSCRESCENTY, CROSSDOUBLED, CROSSJERUSALEM, CROSSLATIN, CROSSLORRAINE, CROSSNORSESUN, CROSSPARTEDFRETTED, CROSSPATRIARCHAL, CROSSPOMELLY, CROSSPOMMELED, CROSSPOTENT, CROSSQUARTERPIERCED, CROSSAVELLANE, CROSSFORMERLY, CROSSFORMERLYFITCH, CROSSCLECHY, CROSSMASCLES, CROSSSIXANNULETSINTERLACED, CROSSANNULETSBRACED, CROSSCANTERBURY, CROSSCOLDHARBOUR, FOURPILES, TENPILES, ANCHOR, CHIEFPLAIN, CHIEFENARCHED, CHIEFTRIANGULAR, CHIEFDOUBLEARCHED, CROSSFUSILS, CHIEFPALE, BASE, ORLE, FORD, QUARTER, TIERCEPLAIN, TIERCESINISTER, WALLPLAIN, FLAUNCHES, SALTIRECOUPED, SALTIRE, PALLPLAIN, PALLINVERTED, SHAKEFORK, CROSSPATONCE, CROSSBOWEN, HEART, ROUNDEL, CROWN, DIAMOND, SPADE, GEMEL};

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
