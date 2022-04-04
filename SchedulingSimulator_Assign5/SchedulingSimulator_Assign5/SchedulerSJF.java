import java.util.Comparator;
import java.util.PriorityQueue;

public class SchedulerSJF extends SchedulerBase implements Scheduler {
    private PriorityQueue<Process> readyQueue;
    private Logger logger;

    public SchedulerSJF(Logger logger) {
        readyQueue = new PriorityQueue<>(4, Comparator.comparing(Process::getBurstTime));
        this.logger = logger;
    }

    @Override
    public void notifyNewProcess(Process p) {
        readyQueue.offer(p);
    }

    @Override
    public Process update(Process CPU) {
        if (CPU != null) {
            if (CPU.isBurstComplete()) {
                logger.log(String.format("Process %s burst complete", CPU.getName()));
                ++this.contextSwitches;
                if (CPU.isExecutionComplete()) {
                    logger.log(String.format("Process %s execution complete", CPU.getName()));
                } else {
                    readyQueue.offer(CPU);
                }
            } else {
                return CPU;
            }
        }
        Process nextProcess = readyQueue.poll();
        if (nextProcess != null) {
            logger.log("Scheduled: " + nextProcess.getName());
            ++this.contextSwitches;
        }
        return nextProcess;
    }
}