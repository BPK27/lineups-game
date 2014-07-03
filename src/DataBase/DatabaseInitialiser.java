package DataBase;

import java.sql.*;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

import FileClasses.LineupData;
import java.util.logging.Level;
import java.util.logging.Logger;


public class DatabaseInitialiser {
	private LineupData data;
	private java.sql.Connection conn;
	private Map<String, Vector<String>> playerData;
	
	public DatabaseInitialiser(LineupData ld, Connection createConn) {
		data = ld;
		conn = createConn.establishConnection();	
		playerData = data.getAllLineups();
	}
	
        //insert the team played against and corresponding details of starting team
        //into database
	public void insertOpposition(){
		String opposition = data.getOpposition();
		String starting11 = data.getMasterLineup();
		String subs = data.getMasterSubs();
		String sql =  "INSERT INTO lineups_game.matches " +
			      "(Team, Starting_11, Subs) VALUES" + 
                              "(?,?,?)";

		PreparedStatement stmt;
		
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, opposition);
			stmt.setString(2, starting11);
			stmt.setString(3, subs);
			stmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Error in insertOpposition" + e);
		}
	}	
	
        // any players without previous entries added to the DB
	public void insertNewPlayers(){
           
             String sql =  "INSERT INTO lineups_game.players " +
                                "(Player_Name) VALUES" + 
                                "(?)";
                
             PreparedStatement stmt;
             try {   
                stmt = conn.prepareStatement(sql);
                
                for(Entry<String, Vector<String>> e : playerData.entrySet()){
                    //if name not found, adds it to the DB
                        if(!playerExists(e.getKey())){
                                stmt.setString(1, e.getKey());
                                stmt.executeUpdate();
                        }
                }
             }catch (SQLException ex) {
                Logger.getLogger(DatabaseInitialiser.class.getName()).log(Level.SEVERE, null, ex);
            }
	}
	
        // insert corresponding player id and match id into the lineups table
	public void insertPlayersToLineups(){
           
            String sql =  "INSERT INTO lineups_game.lineups " +
                          "(matches_matchID, players_playerID) " +
                          "SELECT matches.matchid, players.playerid " +
                          "FROM matches " +
                          "CROSS JOIN players " +
                          "WHERE matches.team=?";
            
            try {    
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, data.getOpposition());
                stmt.executeUpdate();
            } catch (SQLException ex) {
                Logger.getLogger(DatabaseInitialiser.class.getName()).log(Level.SEVERE, null, ex);
            }
			
	}
	
        // check if player is in DB
	private boolean playerExists(String s){
            
             PreparedStatement stmt;
                
             String query = "SELECT player_name FROM lineups_game.players " +
                                                "WHERE player_name = ?";
             ResultSet rs;
             try {   
                stmt = conn.prepareStatement(query);
                stmt.setString(1, s);
                rs = stmt.executeQuery();	
                return rs.first(); //result set returns false if not found
            }catch (SQLException ex) {
                Logger.getLogger(DatabaseInitialiser.class.getName()).log(Level.SEVERE, null, ex);
            }
             
            return true;
             
	}
	
        // inserts the lineups data for the corresponding players for specifc match
	public void insertLineups(){
            PreparedStatement stmt;
        
            String sql = "UPDATE lineups_game.lineups " +
                         "INNER JOIN lineups_game.players " +
                         "ON lineups.players_playerID = players.playerID " +
                         "INNER JOIN lineups_game.matches " +
                         "ON lineups.matches_matchID = matches.matchID " +
                         "SET lineups.starting_11=?, lineups.subs=? " +
                         "WHERE players.Player_Name=? && matches.team =?";
            
            try {    
                stmt = conn.prepareStatement(sql);
                
                for(Entry<String, Vector<String>> player : playerData.entrySet()){
                        Vector<String> lineup = player.getValue();
                        stmt.setString(1, lineup.get(0));
                        stmt.setString(2, lineup.get(1));
                        stmt.setString(3, player.getKey());
                        stmt.setString(4, data.getOpposition());
                        stmt.executeUpdate();
                }
            } catch (SQLException ex) {
                Logger.getLogger(DatabaseInitialiser.class.getName()).log(Level.SEVERE, null, ex);
            }
	}
}
