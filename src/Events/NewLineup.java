package Events;

import java.io.IOException;
import java.sql.SQLException;

import DataBase.Connection;
import DataBase.DatabaseInitialiser;
import DataBase.DatabaseRetriever;
import DataBase.DatabaseUpdater;
import FileClasses.LineupData;
import GameMechanics.LineupCalculator;
import GameMechanics.Player;
import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import javax.swing.JFileChooser;


public class NewLineup {

  public static void main(String[] args){
      //open file chooser
      JFileChooser jf = new JFileChooser();
      jf.setMultiSelectionEnabled(true);
      jf.showOpenDialog(jf);
      File[] f = jf.getSelectedFiles();
                
      //sort by oldest first for multi files
      Arrays.sort(f, new Comparator<File>(){
          public int compare(File f1, File f2){
              return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified());
           } 
      });
                
      Connection conn = new Connection();
      
      for(File file : f){
          String lineups = file.getAbsolutePath();

          LineupData ld = new LineupData(lineups);
          ld.setup();


          DatabaseInitialiser db = new DatabaseInitialiser(ld, conn);
          db.insertNewPlayers();
          db.insertOpposition();
          db.insertPlayersToLineups();
          db.insertLineups();

          DatabaseUpdater du = new DatabaseUpdater(conn);
          DatabaseRetriever dr = new DatabaseRetriever(ld.getOpposition(), conn);
          LineupCalculator lc = new LineupCalculator(dr.getMasterLineup(), du);
          for(Player v : dr.getPlayers()){
              du.updateTotals(v.getName(), lc.calculateScore(v));
          }                  
      }          
  }
}
