import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class Assign6 {
    static final int MAX_MEMORY_FRAMES = 100;
    static final int MAX_PAGE_REFERENCE = 250;
    static final int SIMULATION_COUNT = 1000;

    public static void main(String[] args) {

        int numberOfProcessors = Runtime.getRuntime().availableProcessors();
        ExecutorService threadPool = Executors.newFixedThreadPool(numberOfProcessors);

        int[][] resultsFIFO = new int[SIMULATION_COUNT][];
        int[][] resultsLRU = new int[SIMULATION_COUNT][];
        int[][] resultsMRU = new int[SIMULATION_COUNT][];

        long startTime = System.currentTimeMillis();
        for (int i = 0; i < SIMULATION_COUNT; ++i) {
            resultsFIFO[i] = new int[MAX_MEMORY_FRAMES + 1];
            resultsLRU[i] = new int[MAX_MEMORY_FRAMES + 1];
            resultsMRU[i] = new int[MAX_MEMORY_FRAMES + 1];
            runSimulation(threadPool, resultsFIFO[i], resultsLRU[i], resultsMRU[i]);
        }

        threadPool.shutdown();
        try {
            threadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            System.out.println(e);
        }
        long endTime = System.currentTimeMillis();

        System.out.printf("Simulation took %d ms\n\n", endTime - startTime);

        int[] minCounts = new int[3];
        getMinCounts(resultsFIFO, resultsLRU, resultsMRU, minCounts);

        System.out.printf("FIFO min PF : %d\n", minCounts[0]);
        System.out.printf("LRU min PF : %d\n", minCounts[1]);
        System.out.printf("MRU min PF : %d\n\n", minCounts[2]);

        reportBeladys(resultsFIFO, "FIFO");
        reportBeladys(resultsLRU, "LRU");
        reportBeladys(resultsMRU, "MRU");
    }

    static void runSimulation(ExecutorService threadPool, int[] pageFaultsFIFO, int[] pageFaultsLRU,
                              int[] pageFaultsMRU) {
        int[] sequence = generateRandomSequence();

        for (int maxFrames = 1; maxFrames <= MAX_MEMORY_FRAMES; ++maxFrames) {
            TaskFIFO task1 = new TaskFIFO(sequence, maxFrames, MAX_PAGE_REFERENCE, pageFaultsFIFO);
            TaskLRU task2 = new TaskLRU(sequence, maxFrames, MAX_PAGE_REFERENCE, pageFaultsLRU);
            TaskMRU task3 = new TaskMRU(sequence, maxFrames, MAX_PAGE_REFERENCE, pageFaultsMRU);

            threadPool.execute(task1);
            threadPool.execute(task2);
            threadPool.execute(task3);
        }
    }

    static void getMinCounts (int[][] resultsFIFO, int[][] resultsLRU,
                              int[][] resultsMRU, int[] minCounts){
        class AlgResult {

            private int id;
            private int[] pageFaults;

            AlgResult(int id, int[] pageFaults) {
                this.id = id;
                this.pageFaults = pageFaults;
            }

            int faultsAt(int index){
                return pageFaults[index];
            }
        }

        for (int j = 0; j < SIMULATION_COUNT; ++j){
            AlgResult[] algResults = new AlgResult[]{
                    new AlgResult(0, resultsFIFO[j]),
                    new AlgResult(1, resultsLRU[j]),
                    new AlgResult(2, resultsMRU[j])
            };
            for (int i = 0; i < MAX_MEMORY_FRAMES; ++i){
                final int index = i;
                Arrays.sort(algResults, Comparator.comparing(alg -> alg.faultsAt(index)));
                ++minCounts[algResults[0].id];
                if (algResults[1].faultsAt(index) == algResults[0].faultsAt(index)) {
                    ++minCounts[algResults[1].id];
                }
                if (algResults[2].faultsAt(index) == algResults[0].faultsAt(index)) {
                    ++minCounts[algResults[2].id];
                }
            }
        }
    }

    static void reportBeladys(int[][] results, String name) {
        int occurences = 0;
        int maxDifference = 0;

        System.out.printf("Belady's Anamoly Report for %s\n", name);
        for (int[] pageFaults : results) {
            for (int i = 2; i < pageFaults.length; ++i) {
                if (pageFaults[i] > pageFaults[i - 1]) {
                    int difference = pageFaults[i] - pageFaults[i - 1];
                    System.out.printf("\tdetected - Previous %d : Current %d (%d)\n", pageFaults[i - 1], pageFaults[i],
                            difference);
                    if (difference > maxDifference) {
                        maxDifference = difference;
                    }
                    ++occurences;
                }
            }
        }
        System.out.printf("\t Anomaly detected %d times with a max difference of %d\n\n", occurences, maxDifference);
    }

    static int[] generateRandomSequence() {
        int[] sequence = new int[1000];
        for (int i = 0; i < sequence.length; ++i) {
            sequence[i] = (int) (1 + Math.random() * MAX_PAGE_REFERENCE);
        }
        return sequence;
    }
}