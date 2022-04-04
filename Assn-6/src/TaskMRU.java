class TaskMRU implements Runnable {
    private int[] sequence;
    private int maxMemoryFrames;
    private int maxPageReference;
    private int[] pageFaults;

    public TaskMRU(int[] sequence, int maxMemoryFrames, int maxPageReference, int[] pageFaults) {
        this.maxMemoryFrames = maxMemoryFrames;
        this.sequence = sequence;
        this.maxMemoryFrames = maxMemoryFrames;
        this.maxPageReference = maxPageReference;
        this.pageFaults = pageFaults;
    }

    @Override
    public void run() {
        boolean[] inMemory = new boolean[maxPageReference + 1];
        byte framesInUse = 0;

        int mostRecentlyUsed = -1;
        pageFaults[maxMemoryFrames] = 0;
        for (int pageRef : sequence) {
            if (!inMemory[pageRef]) {
                ++pageFaults[maxMemoryFrames];
                if (framesInUse == maxMemoryFrames) {
                    inMemory[mostRecentlyUsed] = false;
                } else {
                    ++framesInUse;
                }
                inMemory[pageRef] = true;
            }
            mostRecentlyUsed = pageRef;
        }
    }
}