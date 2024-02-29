import java.awt.*;

public class Player
{
   int hp;
   Card[] hand;
   boolean turn;
   int stamina;
   boolean handClosed;
   int aB;
   
   public Player ()
   {
      hp = 20;
      hand = new Card[5];
      stamina=1;
   }
   
   public void drawCard (Card[] deck, int dD)
   {// this drawCard, unlike the drawCard in the main program, copies a Card from deck[dD] and puts it 
   // into an empty slot in the player's hand if there is space.
      boolean full = true;
      int locMark=0;
      for (int i = 0; i<hand.length;i++)
      {
         if(hand[i]==null)
         {
            full=false;
            locMark=i;
         }
      }
      if(!full)
      {
      if(dD>=deck.length||dD<0)
         {
            dD=0;
         }
      hand[locMark]=deck[dD];
      deck[dD++]=null;
      if (hand[locMark]==null)
      {
         if(dD>=deck.length||dD<0)
         {
            dD=0;
         }
         hand[locMark]=deck[dD];
         deck[dD++]=null;
      }
      }
   }
   
   public void surrender ()
   {// surrender is basically a shortcut to ending the game. When the "Surrender" button is pressed, this method 
    // activates for the appropriate player, setting their health to 0 and ending the game.
      hp=0;
   }
}