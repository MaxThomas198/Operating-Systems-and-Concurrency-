import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

class TaskLRU implements Runnable {
    private int[] sequence;
    private int maxMemoryFrames;
    private int maxPageReference;
    private int[] pageFaults;

    public TaskLRU(int[] sequence, int maxMemoryFrames, int maxPageReference, int[] pageFaults) {
        this.sequence = sequence;
        this.maxMemoryFrames = maxMemoryFrames;
        this.maxPageReference = maxPageReference;
        this.pageFaults = pageFaults;
    }
    @Override
    public void run() {
        Set<Integer> memoryFrames = Collections.newSetFromMap(new LinkedHashMap<>(maxMemoryFrames + 1) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<Integer, Boolean> eldest) {
                return size() > maxMemoryFrames;
            }
        });
        pageFaults[maxMemoryFrames] = 0;
        for (int value : sequence) {
            if (!memoryFrames.contains(value)) {
                ++pageFaults[maxMemoryFrames];
            } else {
                memoryFrames.remove(value);
            }
            memoryFrames.add(value);
        }
    }
}