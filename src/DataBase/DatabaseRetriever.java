package DataBase;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import GameMechanics.Player;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseRetriever {
	java.sql.Connection conn;
	String team;
	
	public DatabaseRetriever(String s, Connection createConn) {
		team = s;
		conn = createConn.establishConnection();
	}
	
	public Vector<String> getMasterLineup(){
           
                PreparedStatement stmt;
                String query = "SELECT matches.team, matches.starting_11, matches.subs " +
                                                "FROM lineups_game.matches " +
                                                "WHERE matches.team = ?";
                Vector<String> lineup = new Vector<String>();    
            try {             
                stmt = conn.prepareStatement(query);
                stmt.setString(1, team);
                ResultSet rs = stmt.executeQuery();
                rs.next();
                
                lineup.add(rs.getString(1));
                lineup.add(rs.getString(2));
                lineup.add(rs.getString(3));
                System.out.println(lineup);
                
            } catch (SQLException ex) {
                Logger.getLogger(DatabaseRetriever.class.getName()).log(Level.SEVERE, null, ex);
            }
            return lineup;
	}
	
	public Vector<Player> getPlayers(){
            
                PreparedStatement stmt;
                Vector<Player> players = new Vector<Player>() ;
                String query = "SELECT matches.matchid, players.player_name, lineups.starting_11, lineups.subs, lineups.lineup_score " +
                                                "FROM lineups_game.lineups " +
                                                "INNER JOIN lineups_game.matches " +
                                                "ON matches.matchID = lineups.matches_matchID " +
                                                "INNER JOIN lineups_game.players " +
                                                "ON players.playerId = lineups.players_playerID " +
                                                "WHERE matches.team = ?";
             try {   
                stmt = conn.prepareStatement(query);
                stmt.setString(1, team);
                ResultSet rs = stmt.executeQuery();
                
                while(rs.next()){
                        Vector<Object> lineup = new Vector<Object>();
                        int matchID = rs.getInt(1);
                        String player = rs.getString(2);
                        String starting_11 = rs.getString(3);
                        String subs = rs.getString(4);
                        int lineupScore = rs.getInt(5);
                        if(starting_11 == null){
                                lineup = getPreviousLineup(matchID, player, lineupScore);
                                if(lineup.get(1) != null){
                                        players.add(new Player(lineup));
                                }
                        }
                        else{
                                lineup.add(player);
                                lineup.add(starting_11);
                                lineup.add(subs);
                                lineup.add(lineupScore);
                                players.add(new Player(lineup));
                        }
                        
                }
                
            } catch (SQLException ex) {
                Logger.getLogger(DatabaseRetriever.class.getName()).log(Level.SEVERE, null, ex);
            }
            return players;
	}
	
	private Vector<Object> getPreviousLineup(int matchID, String player, int lineupScore){
            
                PreparedStatement stmt;
                Vector<Object>  pastLineup = new Vector<Object>() ;
                String query = "SELECT lineups.starting_11, lineups.subs " +
                                                "FROM lineups_game.lineups " +
                                                "INNER JOIN lineups_game.matches " +
                                                "ON matches.matchID = lineups.matches_matchID " +
                                                "INNER JOIN lineups_game.players " +
                                                "ON players.playerId = lineups.players_playerID " +
                                                "WHERE matches.matchID = ? && players.player_name = ?";
            try {    
                stmt = conn.prepareStatement(query);
                stmt.setInt(1, matchID-1);
                stmt.setString(2, player);
                ResultSet rs = stmt.executeQuery();
                rs.next();
                pastLineup.add(player);
                pastLineup.add(rs.getString(1));
                pastLineup.add(rs.getString(2));
                pastLineup.add(lineupScore);
                
            } catch (SQLException ex) {
                Logger.getLogger(DatabaseRetriever.class.getName()).log(Level.SEVERE, null, ex);
            }
            return pastLineup;
	}
}
