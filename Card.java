import java.awt.*;
import javax.imageio.*;
import java.awt.image.*;
import java.io.*;

public class Card
{
   int number;
   String name;
   String flavtext;
   int cost;
   String type;
   float fx;
   String fxd;
   int atk;
   int def;
   int bk;
   int dw;
   String dwd;
   String bkd;
   boolean rs;
   boolean dark;
   boolean holy;
   BufferedImage pic;
   
   public Card (int n, String na, String f, int c, String t, double xf)
   {//Creates a basic Card given some data.
      number=n;
      name=na;
      flavtext=f;
      cost=c;
      type = t;
      fx=(float)xf;
      bk=n;
      dw=n;
      try{
      pic=ImageIO.read(new File("Assets/"+name.toLowerCase()+".jpg"));
      }catch(IOException io)
      {System.out.println("Error: The requested image could not be found.");
      }
   }
   
   public String toString ()
   {//Prints out a card by number, type, and name.
      return (""+name+": "+type+" #"+number+", "+cost+" Stamina\n"+flavtext);
   }
   
   public boolean ReleasedFirst (Card other)
   {//Compares cards by comparing their numbers.
      return (this.number<other.number);
   }
   public void setFXD (String nd)
   {//Sets the FX description.
      fxd=nd;
   }
   public void setMinionStats (int A, int D)
   {//Sets the basic stats for the Minion the card creates.
      atk=A;
      def=D;
   }
   public void setMinionFX (String dw, String bc, boolean r, boolean d, boolean h)
   {//Sets various extra effects for a Minion.
      dwd=dw;
      bkd=bc;
      rs=r;
      dark=d;
      holy=h;
   }
   public void setReady (boolean r)
   {
      rs=r;
   }
   public void setMinionExFX (int nbk, int ndw)
   {//More extra effects.
      bk=nbk;
      dw=ndw;
   }
   public void setBlessings (boolean d, boolean h)
   {
      holy=h;
      dark=d;
   }
   public boolean play (Minion[] field,int bfloc, Player p)
   {//Basically, play plays the selected Card to slot bfloc in p's associated field array, field, 
    // provided that p has enough stamina to do so.
    //Its boolean return lets the main program know that it has successfully played the card.
      boolean played = false;
      if(p.stamina>=this.cost)
      {
         field[bfloc] = new Minion(this);
         p.stamina-=this.cost;
         bfloc++;
         played=true;
      }
      return played;
   }
}