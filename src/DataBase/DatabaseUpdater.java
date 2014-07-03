package DataBase;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseUpdater {
	
	private java.sql.Connection conn;
	
	public DatabaseUpdater(Connection createConn) {
		conn = createConn.establishConnection();	
	}
	
        // inserts the players score into lineups table
	public void updateLineupScore(String player, String team, int score){
            try {
                String sql = "UPDATE lineups_game.lineups " +
                                "INNER JOIN lineups_game.players " +
                                "ON players.playerID = lineups.players_playerID " +
                                "INNER JOIN lineups_game.matches " +
                                "ON matches.matchID = lineups.matches_matchID " +
                                "SET lineups.lineup_score = ? " +
                                "WHERE players.player_name = ? && matches.team = ?";
        
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, score);
                stmt.setString(2, player);
                stmt.setString(3, team);
                stmt.executeUpdate();
            } catch (SQLException ex) {
                Logger.getLogger(DatabaseUpdater.class.getName()).log(Level.SEVERE, null, ex);
            }
	}
	
        // updates players total score
	public void updateTotals(String player, int score){
            try {
                String sql = "UPDATE lineups_game.players " +
                                        "SET players.total = (? + players.total) " +
                                        "WHERE players.player_name = ?";
                
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, score);
                stmt.setString(2, player);
                stmt.executeUpdate();
            } catch (SQLException ex) {
                Logger.getLogger(DatabaseUpdater.class.getName()).log(Level.SEVERE, null, ex);
            }
	}
}
