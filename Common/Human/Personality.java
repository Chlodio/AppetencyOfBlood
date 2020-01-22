package Human;
import Common.Basic;
public class Personality{
	private byte ambition;
	private byte honour;
	private byte cunning;
	private byte sxd;		//sexual drive
	private byte sxr;		//sexual restrain


	public Personality(){
		this.sxd =		Basic.randite(5,2);		//-2-2 (5)
		this.sxr = 		formChastity(this.sxd);	//-2–2 (5) should not be higher than sdi


		this.ambition = Basic.randite(5,2);		//-2–2 (5)
		this.honour = 	Basic.randite(5,2);		//-2–2 (5)
		this.cunning =	Basic.randite(5,2);		//-2–2 (5)

	}

		public byte getChastity(){	return this.sxr;		}
		public byte getAmbition(){	return this.ambition;	}
		public byte getHonour(){	return this.honour;		}
		public byte getCunning(){	return this.cunning;	}
		public byte getLibido(){	return this.sxd;		}

		public byte formChastity(byte v){
			v = (byte) (v+2);
			if (v != 0){
				return (byte) (2-Basic.randite(v));
			} else {
				return 2;
			}
		}

}
