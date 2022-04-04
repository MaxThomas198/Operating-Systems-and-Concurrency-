import java.util.*;

public class ResultTable {
    private  Map piResults;
    public final int PI_MAP_SIZE = 1000;
    public ResultTable() {
        this.piResults = new HashMap<String, Integer>(PI_MAP_SIZE);
    }

    public synchronized void setResult(String key, Integer value){
        piResults.put(key, value);
    }

    public void print(){
        System.out.print("\n3.");

        for(int i = 1; i <= 1000; i++){
            System.out.print(piResults.get(Integer.toString(i)));
        }
    }
}