package GameMechanics;

import java.util.Vector;

//hold player information
public class Player {
	private String name;
	private String team;
	private String subs;
	private int lineupScore;
	
	public Player(Vector<Object> lineup){
            name = (String) lineup.get(0);
            team = (String) lineup.get(1);
            subs = (String) lineup.get(2);
            lineupScore = (int) lineup.get(3);
	}
	
	public String getName(){
            return name;
	}
	
	public String getStarting11(){
            return team;
	}
	
	public String getSubs(){
            return subs;
	}
	
	public int getLineupScore(){
            return lineupScore;
	}
	
}
