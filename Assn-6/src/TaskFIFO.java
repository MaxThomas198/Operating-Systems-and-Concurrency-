import java.util.ArrayDeque;
import java.util.Queue;

class TaskFIFO implements Runnable {
    private int[] sequence;
    private int maxMemoryFrames;
    private int maxPageReference;
    private int[] pageFaults;

    public TaskFIFO(int[] sequence, int maxMemoryFrames, int maxPageReference, int[] pageFaults) {
        this.sequence = sequence;
        this.maxMemoryFrames = maxMemoryFrames;
        this.maxPageReference = maxPageReference;
        this.pageFaults = pageFaults;
    }

    @Override
    public void run() {
        boolean[] inMemory = new boolean[maxPageReference + 1];
        byte framesInUse = 0;

        Queue<Integer> replacementQueue = new ArrayDeque<>();

        pageFaults[maxMemoryFrames] = 0;

        for (int pageRef : sequence) {

            if (!inMemory[pageRef]) {
                ++pageFaults[maxMemoryFrames];
                if (framesInUse == maxMemoryFrames) {
                    inMemory[replacementQueue.poll()] = false;
                } else {
                    ++framesInUse;
                }

                inMemory[pageRef] = true;

                replacementQueue.offer(pageRef);
            }
        }
    }
}