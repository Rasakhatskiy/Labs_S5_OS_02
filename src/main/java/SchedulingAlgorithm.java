// Run() is called from Scheduling.main() and is where
// the scheduling algorithm written by the user resides.
// User modification should occur within the Run() function.

import java.util.Vector;
import java.io.*;

public class SchedulingAlgorithm {

    public static int FindNextJob(Vector processVector) {
        return FindNextJob(processVector, -1);
    }

    public static int FindNextJob(Vector processVector, int toAvoid){
        var minProcessId = -1;
        sProcess minProcess;
        var minDiff = Integer.MAX_VALUE;

        for (int i = 0; i < processVector.size(); i++) {
            sProcess tmp = (sProcess) processVector.elementAt(i);
            var tmpDiff = tmp.cputime - tmp.cputimeDone;

            if (tmpDiff <= 0) {
                continue;
            }

            if (i == toAvoid) {
                continue;
            }

            if (tmpDiff < minDiff) {
                minProcess = tmp;
                minProcessId = i;
                minDiff = tmpDiff;
            }
        }
        System.out.println(minProcessId);
        return minProcessId;
    }

    public static Results Run(int runtime, Vector processVector, Results result) {
        int i = 0;
        int comptime = 0;
        int currentProcessId = 0;
        int previousProcess = 0;
        int size = processVector.size();
        int completed = 0;

        // first min
        currentProcessId = FindNextJob(processVector);
        sProcess currentProcess = (sProcess) processVector.elementAt(currentProcessId);

        String resultsFile = "Summary-Processes";
        result.schedulingType = "Batch (Nonpreemptive)";
        result.schedulingName = "Shortest process next";



        try {
            PrintStream out = new PrintStream(new FileOutputStream(resultsFile));
            for (int j = 0; j < processVector.size(); j++) {
                out.println(((sProcess) processVector.elementAt(j)).cputime);
            }
            out.println("Process: " + currentProcessId + " registered... (" + currentProcess.cputime + " " + currentProcess.ioblocking + " " + currentProcess.cputimeDone + " " + currentProcess.cputimeDone + ")");
            while (comptime < runtime) {
                if (currentProcess.cputimeDone >= currentProcess.cputime) {
                    //currentProcess.cputimeDone = comptime;
                    currentProcess.finishedNumber = completed;
                    completed++;
                    out.println("Process: " + currentProcessId + " completed... (" + currentProcess.cputime + " " + currentProcess.ioblocking + " " + currentProcess.cputimeDone + " " + currentProcess.cputimeDone + ")");
                    if (completed == size) {
                        result.compuTime = comptime;
                        out.close();
                        return result;
                    }
                    currentProcessId = FindNextJob(processVector);
                    currentProcess = (sProcess) processVector.elementAt(currentProcessId);
                    out.println("Process: " + currentProcessId + " registered... (" + currentProcess.cputime + " " + currentProcess.ioblocking + " " + currentProcess.cputimeDone + " " + currentProcess.cputimeDone + ")");
                }

                if (currentProcess.ioblocking == currentProcess.ioblockingDone) {
                    out.println("Process: " + currentProcessId + " I/O blocked... (" + currentProcess.cputime + " " + currentProcess.ioblocking + " " + currentProcess.cputimeDone + " " + currentProcess.cputimeDone + ")");
                    currentProcess.numblocked++;
                    currentProcess.ioblockingDone = 0;
                    previousProcess = currentProcessId;
                    currentProcessId = FindNextJob(processVector, previousProcess);
                    if (currentProcessId == -1) {
                        currentProcessId = previousProcess;
                    }
                    currentProcess = (sProcess) processVector.elementAt(currentProcessId);
                    out.println("Process: " + currentProcessId + " registered... (" + currentProcess.cputime + " " + currentProcess.ioblocking + " " + currentProcess.cputimeDone + " " + currentProcess.cputimeDone + ")");
                }


                currentProcess.cputimeDone++;
                if (currentProcess.ioblocking > 0) {
                    currentProcess.ioblockingDone++;
                }
                comptime++;
            }
            out.close();
        } catch (IOException e) {
            System.out.println(e.getStackTrace());
        }
        result.compuTime = comptime;
        return result;
    }
}
