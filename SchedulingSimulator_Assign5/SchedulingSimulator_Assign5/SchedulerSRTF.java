import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

public class SchedulerSRTF extends SchedulerBase implements Scheduler {
    private Queue<Process> readyQueue;
    private Logger logger;

    public SchedulerSRTF(Logger logger) {
        readyQueue = new PriorityQueue<>(4, Comparator.comparing(this::timeRemaining));
        this.logger = logger;
    }

    private int timeRemaining(Process process) {
        if (process != null) {
            return process.getTotalTime() - process.getElapsedTotal();
        } else {
            return -1;
        }
    }

    @Override
    public void notifyNewProcess(Process p) {
        readyQueue.offer(p);
    }

    @Override
    public Process update(Process CPU) {
        if (CPU != null) {
            int timeOfNext = timeRemaining(readyQueue.peek());
            if (0 < timeOfNext && timeOfNext < timeRemaining(CPU)) {
                readyQueue.offer(CPU);
                logger.log("Preemptively removed: " + CPU.getName());
                ++contextSwitches;
            } else {
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
        }
        Process nextProcess = readyQueue.poll();
        if (nextProcess != null) {
            logger.log("Scheduled: " + nextProcess.getName());
            ++this.contextSwitches;
        }
        return nextProcess;
    }
}
