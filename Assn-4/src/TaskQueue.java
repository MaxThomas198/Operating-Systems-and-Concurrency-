import java.util.*;

public class TaskQueue {
    private List queue;
    public TaskQueue(){
        ArrayList<Integer> digits = new ArrayList<Integer>();
        for(int i = 1; i <= 1000; i++){
            digits.add(new Integer(i));
        }
        java.util.Collections.shuffle(digits);
        this.queue = new LinkedList(digits);
    }

    public synchronized int getSize(){
        return queue.size();
    }

    public synchronized int getTask(){
        int value = (Integer) queue.get(0);
        queue.remove(0);
        return value;
    }
}