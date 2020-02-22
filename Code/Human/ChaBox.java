package Code.Human;
public class ChaBox{
	private boolean legimate;
	private boolean posthumous;


	public ChaBox(boolean b){
		this.legimate 	= b;
		this.posthumous = false;
	}

	public ChaBox(){
		this.posthumous = false;
	}

	public void setLegitimacy(boolean b){	this.legimate = b;				}
	public boolean isLegimate(){			return this.legimate;			}
	public void setPosthumous(boolean b){	this.posthumous = b;			}
	public boolean isPosthumous(){			return this.posthumous;			}
}
