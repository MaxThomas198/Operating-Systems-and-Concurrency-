public class Assn4 {
    public static void main(String[] args){
        int processors = Runtime.getRuntime().availableProcessors();
        TaskQueue piTaskQueue = new TaskQueue();
        ResultTable piResultTable = new ResultTable();
        Thread[] threads = new Thread[processors];
        long start = System.currentTimeMillis();
        System.out.print("\nDetermining Pi");

        try{
            for (int i = 0; i < processors; i++){
                threads[i] = new Thread(new workerThread(piTaskQueue, piResultTable));
                threads[i].start();
            }

            for (int i = 0; i < processors; i++){
                threads[i].join();
            }
        }
        catch(Exception e){
            System.out.println("Threading Error: \n" + e);
        }

        long totalTime = System.currentTimeMillis() - start;
        piResultTable.print();
        System.out.println("\nTotal Number Of Processors " + processors);
        System.out.println("Pi computation took " + totalTime + " ms");
    }
}
