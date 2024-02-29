import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.imageio.*;
import java.applet.*;
import java.net.*;

/*************************************************************************\
* David Stothers                                                          *
* ICS 4U, Period 4                                                        *
* Final Assignment                                                        *
* This program is a card game similar to Hearthstone, named Super Battles.*
* Due May 24                                                              *
\*************************************************************************/

public class SuperBattles implements MouseListener
{
   Card[] deck;
   JFrame frame = new JFrame ("Super Battles");
   Minion[][] board = new Minion[2][7];
   int boardUse1;
   int boardUse2;
   Player p1 = new Player();
   Player p2 = new Player();
   int deckDraw;
   int screen = 0;
   int backType;
   boolean p1s = false;
   boolean p2s=false;
   Rectangle sur = new Rectangle (0,231,240,231);
   Rectangle et = new Rectangle(0,462,240,231);
   String action = "Current Action";
   Rectangle[][] mhbs = new Rectangle[2][8];
   Rectangle[] p1hhb = new Rectangle [5];
   Rectangle[] p2hhb = new Rectangle [5];
   Rectangle[] phb = new Rectangle[2];
   Minion mS;
   Card cS;
   Drawing draw= new Drawing();
   Drawing d1= new Drawing();
   Drawing d2= new Drawing();
   BufferedImage deckBack;
   JTextField cbn = new JTextField(20);
   boolean mus = true;
   JFrame s2 = new JFrame ("SB Settings");
   JFrame s1 = new JFrame ("Super Battles");
   Rectangle sgb = new Rectangle (1180/6,924/4,100,100);
   Rectangle sb = new Rectangle (5*1180/6,924/4,100,100);
   Rectangle mm = new Rectangle (1180/6,924/4,100,100);
   Rectangle bb = new Rectangle (5*1180/6,924/4,100,100);
   Rectangle egb = new Rectangle(5*1180/6,924/2,100,100);
   SBRun gamex = new SBRun();
   AudioClip music;
   String results = "";
   int stamMax=0;
   
   public SuperBattles ()
   {//This constructor sets up all upon-startup initializations.
   //frame is the title screen, s2 the settings, and s1 the game.
      deckPrep();
      cbn.setText(""+1);
      
      frame.setLayout (new BorderLayout());
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.add(draw);
      draw.addMouseListener(this);
      d1.addMouseListener(this);
      d2.addMouseListener(this);
      frame.setSize(1250,950);
      s1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      s2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      
      s2.setSize(1250,950);
      s1.setSize(1300,950);
      s2.add(d2);
      s1.add(d1);

      backType=Integer.parseInt(cbn.getText().trim());
      try{
      music=Applet.newAudioClip(new URL("https://www.youtube.com/watch?v=gKD-XRSm8YA"));
      music.loop();
      }catch(MalformedURLException mue)
      {System.out.println("Error: The music URL does not work.");
      }
      
      frame.setVisible(true);
      draw.repaint();
   }
   public static void main (String[] args)
   {//Main doesn't do much other than call the constructor.
      new SuperBattles();
   }
   class SBRun extends Thread
   {// SBRun is a Thread object that basically handles starting the game and checking for Minion deaths.
      public void run(){
      if(screen==1)
      {//Game screen
      startGame();

         yourTurn(p1);
         
         for(int i=0;i<7;i++)
         {
            for(int j=0;j<2;j++)
            {
               if(board[j][i]!=null&&board[j][i].def<=0)
               {
                  board[j][i]=null;
               }
            }
         }
         
         d1.repaint();
      }
     }
   }
   public void startGame()
   {//startGame does as the name says. 
    //It executes game-specific startup commands, like player HP, starting hands, and the like.
      screen=1;
      boardUse1=0;
      boardUse2=0;
      p1 = new Player();
      p2 = new Player();
      p1.aB=0;
      p2.aB=1;
      board = new Minion[2][7];
      backType=Integer.parseInt(cbn.getText().trim());
      for (int i=0;i<p1.hand.length;i++)
      {
         p1.drawCard(deck,deckDraw++);
      }
      for (int i=0;i<p2.hand.length;i++)
      {
         p2.drawCard(deck,deckDraw++);
      }
      p1.handClosed=true;
      p2.handClosed=true;
   }
   public void yourTurn(Player p)
   {//yourTurn lets player p know that it is their turn, and handles between-turn events.
      if(deckDraw>=deck.length)
      {deckPrep();}
      p1.turn=false;
      p2.turn=false;
      p1.handClosed=true;
      p2.handClosed=true;
      if(p==p1)
      {stamMax++;
      p1.stamina=stamMax;
      p2.stamina=stamMax+1;}
      for(int i=0;i<7;i++)
      {
         for(int j=0;j<2;j++)
         {
            if(board[j][i]!=null)
            board[j][i].hA=false;
         }
      }
      p.turn=true;
      for (int i=0;i<p1.hand.length;i++)
      {
         while(p1.hand[i]==null)
         p1.drawCard(deck,deckDraw++);
      }
      for (int i=0;i<p2.hand.length;i++)
      {
         while(p2.hand[i]==null)
         p2.drawCard(deck,deckDraw++);
      }
      p.handClosed=false;
   }
   public void deckPrep()
   {//deckPrep initializes and shuffles the deck.
      action="Shuffling Deck";
      s1.repaint();
      
      int deckPosTrack = 0;
      deckDraw=0;
      
      deck = new Card[111];
      
      for (int i = 0; i<5;i++)
      {
         deck[i] = new Card (1,"Centurion","LEGIO XX V V\n",1,"Minion",0.01);
         deck[i].setFXD("Ready Stance");
         deck[i].setReady(true);
         deck[i].setMinionStats (1,1);
      }
      deckPosTrack+=5;
      for (int i = deckPosTrack; i<deckPosTrack+5;i++)
      {
         deck[i] = new Card (2,"Knight","DEVS VVLT",1,"Minion",0.02);
         deck[i].setFXD("Ready Stance");
         deck[i].setReady(true);
         deck[i].setMinionStats (2,1);
      }
      deckPosTrack+=5;
      for (int i = deckPosTrack; i<deckPosTrack+5;i++)
      {
         deck[i] = new Card (3,"Musketeer","All for one, and one for all!\n",2,"Minion",0.03);
         deck[i].setFXD("Ready Stance");
         deck[i].setReady(true);
         deck[i].setMinionStats (3,2);
      }
      deckPosTrack+=5;
      for (int i = deckPosTrack; i<deckPosTrack+5;i++)
      {
         deck[i] = new Card (4,"Archer","Our arrows will \nblot out the Sun.\n",1,"Minion",0.04);
         deck[i].setFXD("Ready Stance");
         deck[i].setReady(true);
         deck[i].setMinionStats (1,1);
      }
      deckPosTrack+=5;
      for (int i = deckPosTrack; i<deckPosTrack+5;i++)
      {
         deck[i] = new Card (5,"Infantry","Rush B!\n",3,"Minion",0.05);
         deck[i].setFXD("Ready Stance. Dying Wish: Summon a 5/1 Machine Gun.");
         deck[i].setReady(true);
         deck[i].setMinionStats (4,3);
         deck[i].setMinionExFX(0,5);
      }
      deckPosTrack+=5;
      for (int i = deckPosTrack; i<deckPosTrack+5;i++)
      {
         deck[i] = new Card (6,"Wizard","aVaDa KeDaVrA\n",2,"Minion",0.06);
         deck[i].setFXD("");
         deck[i].setReady(false);
         deck[i].setMinionStats (3,1);
      }//30
      deckPosTrack+=5;
      for (int i = deckPosTrack; i<deckPosTrack+3;i++)
      {
         deck[i] = new Card (7,"Tank 1918","Mediumunumicus est optimus\n",7,"Minion",0.07);
         deck[i].setFXD("");
         deck[i].setReady(false);
         deck[i].setMinionStats (4,5);
      }
      deckPosTrack+=3;
      for (int i = deckPosTrack; i<deckPosTrack+3;i++)
      {
         deck[i] = new Card (8,"Tank 1945","Your eggs are \nterrible.\n",7,"Minion",0.08);
         deck[i].setFXD("");
         deck[i].setReady(false);
         deck[i].setMinionStats (6,5);
      }
      deckPosTrack+=3;
      for (int i = deckPosTrack; i<deckPosTrack+3;i++)
      {
         deck[i] = new Card (9,"Tank 1975","",7,"Minion",0.09);
         deck[i].setFXD("");
         deck[i].setReady(false);
         deck[i].setMinionStats (6,7);
      }//39
      deckPosTrack+=3;
      deck[deckPosTrack]= new Card(10,"Drone","Watch out for falcons!\n",2,"Minion",0.10);
      deck[deckPosTrack++].setMinionStats (3,2);
      deck[deckPosTrack]= new Card(11,"Apache Heli","\"I identify as an Apache attack helicopter.\"\n",7,"Minion",0.11);
      deck[deckPosTrack].setMinionStats (6,6);
      deck[deckPosTrack].setReady(false);
      deck[deckPosTrack++].setFXD("");
      deck[deckPosTrack]= new Card(12,"Battleship","You sunk my battleship!\n",8,"Minion",0.12);
      deck[deckPosTrack].setMinionStats (8,7);
      deck[deckPosTrack].setReady(false);
      deck[deckPosTrack++].setFXD("Ready Stance");
      deck[deckPosTrack]= new Card(13,"Zeus","Don't make him angry.\n",10,"Minion",0.13);
      deck[deckPosTrack].setMinionStats (14,15);
      deck[deckPosTrack].setMinionExFX(13,13);
      deck[deckPosTrack].setBlessings(false,true);
      deck[deckPosTrack++].setFXD("Deity");
      deck[deckPosTrack]= new Card(14,"Poseidon","Thrice he could not endure the heat.",9,"Minion",0.14);
      deck[deckPosTrack].setMinionStats (14,13);
      deck[deckPosTrack].setBlessings(false,true);
      deck[deckPosTrack++].setFXD("Deity");
      deck[deckPosTrack]= new Card(15,"Hades","It's pronounced \"HAY-deez\", not \"haydz\".\n",10,"Minion",0.15);
      deck[deckPosTrack].setMinionStats (11,11);
      deck[deckPosTrack].setMinionExFX(15,15);
      deck[deckPosTrack].setBlessings(true,true);
      deck[deckPosTrack++].setFXD("Dark, Deity.");
      //45
            
      for (int i=0;i<45;i++)
      {
      Card temp;
      int c1 = (int)(deck.length*Math.random());
      int c2 = (int)(deck.length*Math.random());
      temp=deck[c1];
      deck[c1]=deck[c2];
      deck[c2]=temp;
      }
      action="Deck prepared.";
      s1.repaint();
   }
   
   class Drawing extends JComponent
   {//Drawing
   
   public void paint (Graphics g)
   {//Dimensions: 1180x924
    //Paint handles the graphics of the game. As usual, it takes g as a Graphics object.
    
      if(screen==0)
      {//Main menu
         g.drawString("SUPER BATTLES", 550, 24);
         g.drawRect(1180/6,924/4,100,100);
         g.drawRect(5*1180/6,924/4,100,100);
         g.drawString("Start Game",(1180/6)+5,(924/4)+50);
         g.drawString("Settings",(5*1180/6)+5,(924/4)+50);
         g.drawString(results,1180/2,924/2);
         g.drawString("See the Settings menu for the rules and how to play.",(3*1180/6)-125,(924/4)+50);
         g.drawString("You will need two people to play this game!",(3*1180/6)-125,(924/4)+65);
         g.drawRect(5*1180/6,2*924/4,100,100);
         g.drawString("Exit Game",(5*1180/6)+5,(924/2)+50);
      }
      else if (screen == 1)
      {
         g.drawLine(240,462,1180-240,462);//Mid Line
         g.drawLine(0,231,1250,231);//Field-P2 Line
         g.drawLine(0,693,1250,693);//Field-P1 Line
         for(int i=0;i<2;i++)
         {
            phb[i] = new Rectangle(500,(694*i),231,231);
         }
         for(int i=0;i<8;i++)
            {g.drawLine(240+(100*i),693,240+(100*i),231);//Field Div Lines
            mhbs[0][i] = new Rectangle(240+(100*i),462,100,231);
            mhbs[1][i] = new Rectangle(240+(100*i),231,100,231);}
         for(int i=0;i<5;i++)
            {g.drawLine(830+(100*i),693,830+(100*i),924);//Hand Div Lines
            g.drawLine(100*i,0,100*i,231);}
            
         try{
         deckBack = ImageIO.read(new File("Assets/cardback"+backType+".jpg"));
         g.drawImage(deckBack,1033,347,null);
         }catch(IOException io)
         {
            System.out.println("Error: The specified deck-back image could not be found.");
         }
         
         drawPlayer(g,p2,500,0);
         drawPlayer(g,p1,500,694);
         
         for (int i=0;i<p1.hand.length;i++)
         {
            if(p1.hand[i]!=null)
             drawCard(g,p1.hand[i],(100*i)+731,694);
            p1hhb[i] = new Rectangle(731+(100*i),694,100,231);
         }
         for (int i=0;i<p2.hand.length;i++)
         {  
            if(p2.hand[i]!=null)
             drawCard(g,p2.hand[i],(100*i),0);
            p2hhb[i] = new Rectangle((100*i),0,100,231);
         }
         g.drawString(action,50,900);
         g.drawString("Surrender",5,375);
         g.drawString("End Turn",5,550);
         g.drawRect(0,231,240,231);
         g.drawRect(0,462,240,231);
         for (int i=0;i<7;i++)
         {
            if(board[0][i]!=null)
            {
               drawMinion(g,board[0][i],240+(100*i),462);
            }
            if(board[1][i]!=null)
            {
               drawMinion(g,board[1][i],240+(100*i),231);
            }
         }
         if (p1.handClosed)
           {
              closeHand(g,p1,deckBack);
           }
         else
           {
              openHand(g,p1);
           }
         if(p2.handClosed)
           {
              closeHand(g,p2,deckBack);
           }
         else
           {
              openHand(g,p2);
           }
      }
      if(screen==2)
      {
         g.drawString("Settings", 550, 24);
         mm = new Rectangle (1180/6,924/4,100,100);
         bb = new Rectangle (5*1180/6,924/4,100,100);
         g.drawRect(1180/6,924/4,100,100);
         g.drawRect(5*1180/6,924/4,100,100);
         g.drawString("Toggle Music",(1180/6)+5,(924/4)+50);
         g.drawString("Back to Main Menu",(5*1180/6)+5,(924/4)+50);
         g.drawString("The object of the game is to win by reducing your opponent's HP to 0. This is brought about",50,924/2);
         g.drawString("by playing various soldiers and minions to have them do battle! You both start out with 20 HP",50,924/2+15);
         g.drawString("and 10 Stamina, but be warned, as you take damage, your Stamina cap will decrease as well!",50,924/2+30);
         g.drawString("However,your opponent's field must be clear in order for you to land an attack on them!",50,924/2+45);
         g.drawString("Click on cards to play them onto the battlefield. Click on the Minions that appear and then click on a target",50,924/2+75);
         g.drawString("to have the Minion you first clicked on attack them! A handful of Minions have what's known as Blitzkriegs,",50,924/2+90);
         g.drawString("or Dying Wishes. These activate when a card is played, and when the Minion is killed by another, respectively.",50,924/2+105);
         g.drawString("Use them wisely!",50,924/2+120);
      }
   }
   public void drawCard(Graphics g,Card card,int x,int y)
   {//drawCard, unlike Player's drawCard, provides draw with instructions on visual cards. It takes Graphics,
    //the Card to be drawn, and the coordinates to be drawn at.
      g.drawRect(x,y,100,200);//Outline of card
      g.drawLine(x+80,y,x+80,y+40);//Name-cost seperator
      g.drawLine(x,y+40,x+100,y+40);//Name-pic seperator
      g.drawLine(x,y+120,x+100,y+120);//Pic-effects seperator
      g.drawLine(x,y+180,x+100,y+180);//effects-type seperator
      g.drawLine(x+20,y+180,x+20,y+200);//ATK-type seperator
      g.drawLine(x+80,y+180,x+80,y+200);//DEF-type seperator
      g.drawString(card.name,x+5,y+20);
      g.drawString(""+card.cost,x+80,y+20);
      g.drawString(""+card.type,x+30,y+190);
      g.drawImage(card.pic,x,y+40,null);
      g.drawString(""+card.atk,x+5,y+190);
      g.drawString(""+card.def,x+80,y+190);
      if(card.number==5)//Printing out card-specific Blitzkrieg and Dying Wish effect descriptions
      {//Card effects area is 16 chars per line
         g.drawString("DW:Summon a",x+5,y+135);
         g.drawString("5/1 Machinegun",x+5,y+150);
      }
      else if(card.number==13)
      {
         g.drawString("BK:Deity +1/+1",x+5,y+135);
         g.drawString("DW:Clear Deities",x+5,y+150);
      }
      else if(card.number==15||card.number==32||card.number==38||card.number==66)
      {
         g.drawString("BK:Evil +1/+1",x+5,y+135);
         g.drawString("DW:Clear Board",x+5,y+150);
      }
      else if(card.number==34)
      {
         g.drawString("DW:Regenerate",x+5,y+135);
      }
      else if(card.number==37)
      {
         g.drawString("BK: Deity +1/+1",x+5,y+135);
      }
      else if(card.number==65)
      {
         g.drawString("BK:Evil +1/+1",x+5,y+135);
      }
   }
   public void drawPlayer(Graphics g,Player player,int x,int y)
   {//drawPlayer draw the visual representation of a player on the screen.
   // It takes the same parameters as drawCard, but with a Player, not a Card.
      g.drawRect(x,y,231,231);
      g.drawString (""+player.hp,x+200,y+200);
      g.drawString (""+player.stamina,x+5,y+200);
   }
   public void drawMinion(Graphics g,Minion m,int x, int y)
   {//drawMinion draws Minions. It's just like the last two draw methods.
      g.drawRect(x,y,100,200);
      g.drawLine(x,y+80,x+100,y+80);//Pic-fx seperator
      g.drawLine(x+50,y+80,x+50,y+200);//atk-def seperator
      g.drawString("Can't Attack!",x+5,y+40);
      if(!m.hA||m.rs)
      {
      g.drawImage(m.pic,x,y,null);
      }
      g.drawString(""+m.atk,x+5,y+110);
      g.drawString(""+m.def,x+55,y+110);
   }
   public void closeHand(Graphics g,Player p,BufferedImage db)
   {//closeHand closes p's hand, with card back image db.
      if(p==p1)
      for(int i=0;i<5;i++)
      {
         g.drawImage(db,730+(100*i),693,null);
      }
      else if(p==p2)
      {
      for(int i=0;i<5;i++)
      {
         g.drawImage(db,(100*i),0,null);
      }
      }
   }
   public void openHand(Graphics g,Player p)
   {//openHand removes closed-hand status from player p's hand.
      if(p==p1)
         for (int i=0;i<p1.hand.length;i++)
         {
            if(p1.hand[i]!=null)
               drawCard(g,p1.hand[i],(100*i)+731,694);
            p1hhb[i] = new Rectangle(731+(100*i),694,100,231);
         }
      else if(p==p2)
         for (int i=0;i<p2.hand.length;i++)
         {  
            if(p2.hand[i]!=null)
               drawCard(g,p2.hand[i],(100*i),0);
            p2hhb[i] = new Rectangle((100*i),0,100,231);
         }
   }
   }
   
   public void mousePressed (MouseEvent me)
   {
   }
   public void mouseReleased (MouseEvent me)
   {//mouseReleased handles hitboxes and click events, like attacking, pressing a button, or playing a card.
      int mx = me.getX();
      int my = me.getY();
      String act = action.trim();
      boolean hbf = false;
      
      if(screen==0)
      {
         if(!hbf&&sgb.contains(mx,my))
         {
            hbf=true;
            gamex=new SBRun();            
            gamex.start();
            screen=1;
            frame.setVisible(false);
            s2.setVisible(false);
            s1.setVisible(true);
         }
         else if(!hbf&&sb.contains(mx,my))
         {  
            hbf=true;
            screen=2;
            frame.setVisible(false);
            s2.setVisible(true);
            s1.setVisible(false);
         }
         else if(!hbf&&egb.contains(mx,my))
         {
            hbf=true;
            System.exit(0);
         }
      }
      if (screen==1)
      {//Screen 1
      for(int i = 0; i<2;i++)
      {
      for(int j = 0; j<7;j++)
      {
         if (!hbf && mS==null && mhbs[i][j].contains(mx,my))
         {
            hbf=true;
            action=("Attacking");
            mS = board[i][j];
         }
         else if(!hbf && mS!=null && mhbs[i][j].contains(mx,my) && mS==board[i][j])
         {
            hbf=true;
            action="Deselected";
            mS=null;
         }
         else if (!hbf && mS!= null && mhbs[i][j].contains(mx,my) && (board[i][j]!=mS))
         {
            hbf=true;
            action=("Attacked");
            float dwa = mS.attack(board[i][j]);
            if(dwa==0.13)
            {
               for(int k=0;i<2;i++)
               {
                  for(int l=0;l<7;l++)
                  {
                     board[k][l]=null;
                  }
               }
            }
            d1.repaint();
            mS=null;
         }
      }
      if(!hbf && mS!=null && phb[i].contains(mx,my))
      {
         hbf=true;
         action="Attacked";
         if(phb[1].contains(mx,my))
         {
            boolean fc=true;
            for(int j=0;i<7;i++)
            if(board[p1.aB][i]!=null)
            fc=false;
            if(fc)
            mS.attack(p1);
         }
         else if (phb[0].contains(mx,my))
         {
            boolean fc=true;
            for(int j=0;i<7;i++)
            if(board[p2.aB][i]!=null)
            fc=false;
            if(fc)
            mS.attack(p2);
         }
         mS=null;
      }
      }
      for(int i=0;i<5;i++)
      {
         if (!hbf && p1.turn && p1hhb[i].contains(mx,my))
         {//Board 0 is p1, board 1 is p2
            hbf=true;
            action=("Playing Card");
            for(int k = 0; k<7;k++)
               if(board[0][k]==null)
                  boardUse1=k;
            boolean toDelete=p1.hand[i].play(board[0],boardUse1,p1);
            if(p1.hand[i].bk==13)//Dealing with Blitzkrieg effects.
            {
               for(int l=0;l<7;l++)
               {
                  if(board[0][l]!=null&&board[0][l].holy)
                  {
                     board[0][l].atk++;
                     board[0][l].def++;
                  }
               }
            }
            else if(p1.hand[i].bk==15)
            {
               for(int l=0;l<7;l++)
               {
                  if(board[0][l]!=null&&board[0][l].dark)
                  {
                     board[0][l].atk++;
                     board[0][l].def++;
                  }
               }
            }
            if(toDelete)
            {
            p1.hand[i]=null;
            }
            d1.repaint();
         }
         if (!hbf && p2.turn && p2hhb[i].contains(mx,my))
         {
            hbf=true;
            mS=null;
            action="Playing Card";
            for(int k = 0; k<7;k++)
               if(board[1][k]==null)
                  boardUse2=k;
            boolean toDelete=p2.hand[i].play(board[1],boardUse2,p2);
            if(p2.hand[i].bk==13)//Dealing with Blitzkrieg effects.
            {
               for(int l=0;l<7;l++)
               {
                  if(board[1][l]!=null&&board[1][l].holy)
                  {
                     board[1][l].atk++;
                     board[1][l].def++;
                  }
               }
            }
            else if(p2.hand[i].bk==15)
            {
               for(int l=0;l<7;l++)
               {
                  if(board[1][l]!=null&&board[1][l].dark)
                  {
                     board[1][l].atk++;
                     board[1][l].def++;
                  }
               }
            }
            if(toDelete)
            {
            p2.hand[i]=null;
            }
         }
         }
      if(!hbf&&p1.turn&&sur.contains(mx,my))
      {//Surrender Button
         hbf=true;
         p1.hp=0;
         results="P2 Wins!";
      }
      if(!hbf&&p2.turn&&sur.contains(mx,my))
      {
         hbf=true;
         p2.hp=0;
         results="P1 Wins!";
      }
      if(!hbf&&p1.turn&&et.contains(mx,my))
      {//End Turn Button
         hbf=true;
         yourTurn(p2);
      }
      if(!hbf&&p2.turn&&et.contains(mx,my))
      {
         hbf=true;
         yourTurn(p1);
      }
      for(int i=0;i<7;i++)
         {//Checking for Minion deaths, Dying Wishes and victory conditions
            for(int j=0;j<2;j++)
            {
               if(board[j][i]!=null&&board[j][i].def<=0)
               {
                  if(board[j][i].dw==13)
                  {
                     for(int k=0;k<2;k++)
                        for(int l=0;l<7;l++)
                           if(board[k][l]!=null&&board[k][l].holy)
                              {
                                 board[k][l]=null;
                              }
                  }
                  else if (board[j][i].dw==15)
                     for(int k=0;k<2;k++)
                        for(int l=0;l<7;l++)
                           board[k][l]=null;
                  else if (board[j][i].dw==5)
                  {
                     for(int l=0;l<7;l++)
                     {
                        if(board[j][l]==null)
                        boardUse1=l;
                     }
                     board[j][boardUse1]=new Minion(16,"Machine Gun","Pewpewpew!",5,1);
                  }
                  else if (board[j][i].dw==34)
                  {
                     for(int l=0;l<7;l++)
                     {
                        if(board[j][l]==null)
                        boardUse1=l;
                     }
                     board[j][boardUse1]=new Minion(34,"Vegetal","Did someone said NO VEGETALS?",2,5);
                     board[j][boardUse1].setExFX(34,"Regenerate",0,null);
                  }
                  board[j][i]=null;
               }
            }
         }
         if(p1.hp<=0||p2.hp<=0)
         {
            if(p1.hp<=0)
            {
               results="P2 Wins!";
               screen=0;
            }
            else if(p2.hp<=0)
            {
               results="P1 Wins!";
               screen=0;
            }
            draw.repaint();
            screen=0;
         }
         d1.repaint();
      }
      else if(screen==2)
      {
         if (!hbf&&bb.contains(mx,my))
         {
            hbf=true;
            screen=0;
            frame.setVisible(true);
            s2.setVisible(false);
            s1.setVisible(false);
         }
      }
   }
   public void mouseClicked (MouseEvent me)
   {
   }
   public void mouseEntered (MouseEvent me)
   {
      action=("Game Resumed");
   }
   public void mouseExited (MouseEvent me)
   {
      action=("Mouse Not on Game");
   }
}
