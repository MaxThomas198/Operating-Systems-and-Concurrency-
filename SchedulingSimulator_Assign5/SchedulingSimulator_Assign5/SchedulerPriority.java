import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

public class SchedulerPriority extends SchedulerBase implements Scheduler {
    private Queue<Process> readyQueue;
    private Logger logger;

    public SchedulerPriority(Logger logger) {
        readyQueue = new PriorityQueue<>(5, Comparator.comparing(Process::getPriority));
        this.logger = logger;
    }

    @Override
    public void notifyNewProcess(Process process) {
        readyQueue.offer(process);
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