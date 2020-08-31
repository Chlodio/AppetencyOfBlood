package Code.calendar;

//This is 360-days calendar


public class Calendar {
  private int year;
  private int month;
  private int monthDay;
  private int yearDay;

  public Calendar(int y, int m, int d){
    this.year       = y;
    this.month      = m;
    this.monthDay = d;
    this.yearDay  = (y*360)+(m*30)+d;
  }

  public Calendar(Calendar c){
    this.year     = c.year;
    this.month    = c.month;
    this.monthDay = c.monthDay;
    this.yearDay  = c.yearDay;
  }

  public Calendar clone(){
    return new Calendar(this);
  }

  public void addDay(){
     this.addDays(1);
   }

   public void addDays(int i){
     this.yearDay += i;
     int y = i/360;
     i -= y*360;
     int m = i/30;
     i -= m*30;
     if ((this.monthDay + i) > 30){
         this.monthDay = (this.monthDay+i)%30;
         m++;
     } else {
         this.monthDay +=  i;
     }
     if ((this.month + m) > 12){
         this.month = (this.month + m)-12;
         y++;
     } else {
         this.month +=  m;
     }
     this.year += y;
   }

   public void takeDay(){
     this.takeDays(1);
   }

   public void takeDays(int i){

     this.yearDay -= i;
     int y = i/360;
     i -= y*360;
     int m = i/30;
     i -= m*30;

     if ((this.monthDay - i) < 1){
         this.monthDay = 30 + (this.monthDay - i);
         m++;
     } else {
         this.monthDay -=  i;
     }

     if ((this.month - m) < 1){
         this.month = 12 + (this.month - m);
         y++;
     } else {
         this.month -=  m;
     }
     this.year -= y;

   }

  public String getDateString(){
    String y = String.format("%02d", this.year);
    String m = String.format("%02d", this.month);
    String d = String.format("%02d", this.monthDay);
    return y+"."+m+"."+d;
  }

  public String getDateLong(){
    String y = String.format("%02d", this.year);
    String m = monthName[this.month].substring(0, 3);
    String d = String.format("%02d", this.monthDay);
    return y+" "+m+". "+d;
  }


  public int updateMonth(int d){
      int i = d%30;
      this.month += (d/30);
      this.yearDay += this.month*30;
      if (this.month > 12){
          this.year  += 1;
          this.month -= 12;
      }
      return i;
  }

  public int updateMonthDown(int d){
      int i = d%30;
      this.month     -= (d/30);
      this.yearDay -= this.month*30;
      if (this.month < 1){
          this.year  -= 1;
          this.month = 12+this.month;
      }
      return i;
  }

  //Updates year, month, and day of month based on yearDay, after reduction
  public void updateDown(){
      if (this.month > 1){
        this.month--;
      } else {
        this.year        = this.yearDay/360;
        this.month       = 12;
      }
      this.monthDay    = 30+this.monthDay;
  }

  //Updates year, month, and day of month based on yearDay, after increasement
  public void update(){
    this.year        = this.yearDay/360;
    int i            = this.yearDay-(this.year*360);
    this.month       = i/30;
    i                = i-(this.month*30);
    this.monthDay  = (this.yearDay)-((this.year*360)+(this.month*30));
  }

  //If object came before argument
  public boolean before(Calendar c){
    if (c.year < this.year){
      return false;
    } else if (this.year == c.year){
      return c.yearDay < this.yearDay;
    }
    return true;
  }

  public int getMonthDay(){
    return this.monthDay;
  }

  public int getMonth(){
    return this.year;
  }

  public int getYear(){
    return this.year;
  }

  public String getYearString(){
    return ""+this.year;
  }

  private static final String[] monthName = {"?", "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

}
