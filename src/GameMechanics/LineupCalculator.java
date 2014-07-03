package GameMechanics;

import java.util.StringTokenizer;
import java.util.Vector;

import DataBase.DatabaseUpdater;

public class LineupCalculator {
	private DatabaseUpdater du;
	private String opponent;
	private String masterLineup;
	private String masterSubs;
	
	public LineupCalculator(Vector<String> matchInfo, DatabaseUpdater updater){
            du = updater;
            opponent = matchInfo.get(0);
            masterLineup = matchInfo.get(1);
            masterSubs = matchInfo.get(2);
	}
	
        // separates the master team into individual players for comaprison
	private Vector<String> separateLineup(){
            Vector<String> lineup = new Vector<String>();
            StringTokenizer st = new StringTokenizer(masterLineup, ",");

            while(st.hasMoreTokens()){
                lineup.add(st.nextToken().toLowerCase());
            }

            return lineup;
		
	}
	
        // separates the master subs into individual players for comaprison
	private Vector<String> separateSubs(){
            Vector<String> subs = new Vector<String>();
            StringTokenizer st = new StringTokenizer(masterSubs, ",");

            while(st.hasMoreTokens()){
                    subs.add(st.nextToken().toLowerCase());
            }

            return subs;
		
	}
	
        // calcualates player score
	public int calculateScore(Player playerInfo){
            int score = 0;
            
            //master details
            Vector<String> lineup = separateLineup();
            Vector<String> subs = separateSubs();
            
            String player = playerInfo.getName();
            
            //for each player match +1 point
            for(String s : lineup){
                    if(playerInfo.getStarting11().toLowerCase().contains(s.trim())){
                            score++;
                    }
            }
            int count = 0; //count number of subs for scoring purposes
            for(String s : subs){
                    if(playerInfo.getSubs().toLowerCase().contains(s.trim())){
                            score++;
                            count++;
                    }
            }
            //less than 3 subs can still score max points if they match subs used
            if(subs.size() < 3){
                    score = score + subCheck(subs, playerInfo, count);
            }

            du.updateLineupScore(player, opponent, score);

            //intended for updating scores if it has to be run again
            if(playerInfo.getLineupScore() != 0){
                    return score - playerInfo.getLineupScore();
            }
            else{
                return score;
            }
		
		
	}

	private int subCheck(Vector<String> subs, Player playerInfo, int score) {
            StringTokenizer st = new StringTokenizer(playerInfo.getSubs(), ",");
            System.out.println(st.countTokens() + " " + score);
            if((subs.size() == st.countTokens()) && (score == st.countTokens())){
                    return 1;
            }
            else 
                    return 0;
		
	}
}
