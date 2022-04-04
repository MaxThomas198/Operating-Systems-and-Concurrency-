import java.util.ArrayDeque;
import java.util.Queue;

public class SchedulerRR extends SchedulerBase implements Scheduler {
    private Queue<Process> readyQueue;
    private Logger logger;
    private int timeQuantum;

    public SchedulerRR(Logger logger, int timeQuantum) {
        readyQueue = new ArrayDeque<>();
        this.logger = logger;
        this.timeQuantum = timeQuantum;

    }

    private boolean isTimeQuantumComplete(Process process) {
        return (process.getElapsedTotal() > 0 && process.getElapsedTotal() % this.timeQuantum == 0);
    }

    @Override
    public void notifyNewProcess(Process process) {
        readyQueue.offer(process);
    }

    @Override
    public Process update(Process CPU) {
        if (CPU != null) {
            if (CPU.isExecutionComplete()) {
                logger.log(String.format("Process %s execution complete", CPU.getName()));
                ++this.contextSwitches;
            } else if (isTimeQuantumComplete(CPU)) {
                logger.log("Time quantum complete for process " + CPU.getName());
                readyQueue.offer(CPU);
                ++this.contextSwitches;
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