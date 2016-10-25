package smartsa;


import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
//import java.util.Iterator;
//import java.util.Set;
//import java.util.Vector;

public class SentiWordNet {

//	private static SentiWordNet swn = null;
	private String location = "C:\\SmartSAresources\\SentiWordNet_3.0.0.txt";
	private HashMap<Long, Double[]> dict;

	public SentiWordNet() {

		dict = new HashMap<Long, Double[]>();
		try {
			BufferedReader csv = new BufferedReader(new FileReader(location));
			String line = "";

			while ((line = csv.readLine()) != null) {
				if (line.isEmpty()) {
					continue;
				}
				String[] data = line.split("\t");
                                Long ID = Long.parseLong(data[1]);
                                Double[] scores = new Double[2];
                                scores[0] = Double.parseDouble(data[2]);
                                scores[1] = Double.parseDouble(data[3]);
                                dict.put(ID, scores);
                      }
                  } catch (Exception e) {
			e.printStackTrace();}
            }

        public Double[] getScores(Long ID){

            Double[] scores = new Double[2];
            if (dict.containsKey(ID))
                    scores = dict.get(ID);
            return scores;
            }
}




