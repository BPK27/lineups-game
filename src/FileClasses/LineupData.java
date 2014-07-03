package FileClasses;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;


public class LineupData {
	private String gameInfo;
	private String opposition;
	private String masterLineup;
	private String masterSubs;
	private Map<String, Vector<String>> allLineups = new HashMap<String, Vector<String>>();
	private FileCleaner lp;
        
	public LineupData(String s){
            lp = new FileCleaner(s);
	}
        
        public void setup(){
            gameInfo = lp.fileClean();
            setInfo();
        }
	
        //takes the info from file
	private void setInfo(){
            StringTokenizer st = new StringTokenizer(gameInfo, "\n");

            //inital 3 lines are set
            opposition = st.nextToken();
            masterLineup = st.nextToken();
            masterSubs = st.nextToken();

            // for all entries sort the information
            while(st.hasMoreTokens()){
                Vector<String> lineup = new Vector<String>();

                String player = playerCheck(st.nextToken());
                lineup.add(nicknameCheck(st.nextToken()));
                lineup.add(nicknameCheck(subsCheck(st.nextToken())));

                allLineups.put(player, lineup);
            }
	}
	
	public Map<String, Vector<String>> getAllLineups(){
		return allLineups;
	}
	
	public String getOpposition(){
		return opposition;
	}
	
	public String getMasterLineup(){
		return masterLineup;
	}	
	
	public String getMasterSubs(){
		return masterSubs;
	}
	
        //removes uneccesary text from file
	private String playerCheck(String player) {
		String playerClean = player.substring(0, player.indexOf(" said:"));
		return playerClean;
	}
	
        //common nicknames used
	private String nicknameCheck(String team){
		String temp = team.toLowerCase().replaceAll("rio", "Ferdinand");
		String temp1 = temp.toLowerCase().replaceAll("ddg", "de Gea");
		String temp2 = temp1.toLowerCase().replaceAll("rvp", "van Persie");
		return temp2;
	}

        // cleans up formatting
	private String subsCheck(String subs){
		String subsClean = subs.replaceAll("(Subs|SUBS|subs)", "");
		String temp = subsClean.replaceAll("([^a-zA-Z|| |,])", ",");
		return temp;
		 
	}
}
