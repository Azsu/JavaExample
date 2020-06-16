package bondedge.com.company;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

public class Main {

    public static class CommonPairs {

        private Map<Integer, Integer> pairs;
        private List<Integer> pairOptions;

        public CommonPairs(List<Integer> pairOptions, Integer sum) {
            Collections.sort(pairOptions);
            this.pairOptions = pairOptions;
            this.pairs = new HashMap<Integer, Integer>();
            this.FindPairs(sum);
        }

        public void FindPairs(Integer sum) {
            for (Integer x1 : this.pairOptions) {
                if (x1 >= sum / 2) {
                    // if we have 2 copies of sum/2 add x1,x1
                    if (x1 + x1 == sum && this.pairOptions.indexOf(x1) != this.pairOptions.lastIndexOf(x1)) {
                        pairs.put(x1, x1);
                    }
                    break;
                }
                else if (this.pairOptions.contains(sum - x1)) {
                    pairs.put(x1, sum - x1);
                }
            }
        }

        public void report() {
            for (Map.Entry<Integer, Integer> entry : pairs.entrySet()) {
                System.out.println(entry.getKey().toString() + "," + entry.getValue().toString());
            }
        }
    }
    public static class LatestCusip {

        private BufferedReader br;
        private Map<String, Float> latestCusips;

        public LatestCusip(String filename) {
            try {
                FileReader fileReader = new FileReader(filename);
                this.latestCusips = new HashMap<String, Float>();
                this.br = new BufferedReader(fileReader);
                processData();

            } catch(Exception e){
                e.printStackTrace();
            }
        }

        // Public API for individual queries
        public Float getCusip(String Cusip) {
            return latestCusips.get(Cusip);
        }

        // Re-entrant
        public void processData()
        {
            String Cusip = new String();
            Float Price = Float.NaN;

            try {
                String line;
                while ((line = br.readLine() ) != null) {
                    try {
                        Price = Float.parseFloat(line);
                    } catch (NumberFormatException e) {
                        if (!Cusip.isEmpty()) {
                            // Found NaN, save last pair
                            latestCusips.put(Cusip, Price);
                        }
                        if (Pattern.matches("[a-zA-Z0-9]{8}", line)) {
                            Cusip = line;
                            Price = Float.NaN;
                        } else {
                            // TODO:: Invalid entry? logger.warn("Possible error at ...");
                            Cusip = new String();
                        }
                    }
                }
                if (!Cusip.isEmpty()) {
                    // Final flush
                    latestCusips.put(Cusip, Price);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void report() {
            for (Map.Entry<String, Float> entry : latestCusips.entrySet()) {
                System.out.println(entry.getKey().toString() + "," + entry.getValue().toString());
            }
        }
    }

    public static void main(String[] args) {
        LatestCusip latestCusip = new LatestCusip("./test_file.txt");
        latestCusip.report();

        Integer SUM = 8;
        List<Integer> pairOptions = Arrays.asList(10,4,2,2,3,4,5,6,7,8);

        CommonPairs commonPairs = new CommonPairs(pairOptions, SUM);
        commonPairs.report();
    }
}
