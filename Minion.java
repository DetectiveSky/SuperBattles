import java.awt.*;
import javax.imageio.*;
import java.awt.image.*;
import java.io.*;

public class Minion
{
   int number;
   String name;
   String flavText;
   int atk;
   int def;
   int dw;
   String dwd;
   int bk;
   String bkd;
   boolean rs;
   boolean dark;
   boolean holy;
   boolean hA = true;
   BufferedImage pic;
   //boolean ranged;
   
   public Minion (int n, String na, String f, int a, int d)
   {// This constructor makes a Minion given some data about it.
      name=na;
      number=n;
      flavText=f;
      atk=a;
      def=d;
      rs=false;
      dw=0;
      bk=0;
      dark=false;
      holy=false;
      try{
      pic=ImageIO.read(new File("Photos/"+name.toLowerCase()+".jpg"));
      }catch(IOException io)
      {System.out.println("Error, 404: The requested image could not be found.");
      }
   }
   public Minion (Card c)
   {// This constructor is better for use with Card objects. 
    // It extrapolates various data from the Card's fields and makes a Minion out of it.
      number = c.number;
      name = c.name;
      flavText=c.flavtext;
      atk = c.atk;
      def=c.def;
      dw=c.dw;
      bk=c.bk;
      dwd=c.dwd;
      bkd=c.bkd;
      rs=c.rs;
      dark=c.dark;
      holy=c.holy;
      pic=c.pic;
   }
   
   public String toString ()
   {// This is where a Minion is printed out, with some basic data plus extra effects.
      String result = ("#"+number+": "+name+", "+atk+"/"+def+". \n"+flavText+"\n");
      if (dark && holy)
         result+=("Dark, Holy");
      else if (dark)
      {
         result+="Dark";
      }
      else if (holy)
      {
         result+="Holy";
      }
      if (rs)
      {
         result+="\nReady Stance";
      }
      if (dw!=0)
      {
         result+=("\n"+dwd);
      }
      return result;
   }
   
   public void setReadyStance (boolean b)
   {// This method sets the Ready Stance of a Minion to b.
   rs = b;
   }
   
   public void setExFX (int ndw, String ndwd, int nbk, String nbkd)
   {// Setting the Dying Wish and Blitzkrieg, and their respective descriptions.
      dw=ndw;
      bk=nbk;
      dwd=ndwd;
      bkd=nbkd;
   }
   
   public float attack (Minion target)
   {//When a Minion attacks another Minion, each Minion's Attack is subtracted from the other's Defense.
   if(!hA || rs)
   {
      target.def-=this.atk;
      this.def-=target.atk;
      hA=true;
      rs=false;
      if(target.dw!=0)
      {
         return target.dw;
      }
      else return 0;
   }
   else return 0;
   }
   public void attack (Player p)
   {//... When a Minion attacks a Player, though, the Player cannot retaliate on its own.
   if(!hA || rs)
   {
      p.hp-=this.atk;
      hA=true;
      rs=false;
   }
   }
   //In both of the Attack methods, the Minion's hasAttacked(or hA) lets the game know whether the Minion 
   //has attacked yet. This is initially true to enforce a 1-turn warmup period, but it can be overridden
   //by a Ready Stance. Ready Stance is then set to false so that a Minion can't attack more than once per
   //turn by abusing this.
   
   public void setBlessings (boolean d, boolean h)
   {//This sets the Dark and Holy stati of a Minion.
      dark=d;
      holy=h;
   }
   
   /*public void setRanged (boolean r)
   {
      ranged = r;
   }*/
}