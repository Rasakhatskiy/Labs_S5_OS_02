public class sProcess {
  public int cputime;
  public int ioblocking;
  public int cputimeDone;
  public int ioblockingDone;
  public int numblocked;
  public int finishedNumber;

  public sProcess (int cputime, int ioblocking, int cpudone, int ionext, int numblocked, int finishedNumber) {
    this.cputime = cputime;
    this.ioblocking = ioblocking;
    this.cputimeDone = cpudone;
    this.ioblockingDone = ionext;
    this.numblocked = numblocked;
    this.finishedNumber = finishedNumber;
  } 	
}
