public class workerThread implements Runnable{
    private TaskQueue tasks;
    private ResultTable piResults;

    public workerThread(TaskQueue taskQueue, ResultTable resultTable){
        this.tasks = taskQueue;
        this.piResults = resultTable;
    }

    public void run(){
        Integer digit;
        Integer result;
        Bpp bpp = new Bpp();
        int tasksSize = tasks.getSize();

        while(tasksSize != 0){
            digit = tasks.getTask();
            result = bpp.getDecimal(digit);
            piResults.setResult(digit.toString(), result);
            tasksSize = tasks.getSize();

            if(tasksSize % 10 == 0){
                System.out.print(".");
                System.out.flush();
            }
        }
    }
}