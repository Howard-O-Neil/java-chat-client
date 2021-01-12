package application.Utils;

import java.util.Timer;

public class TimerHelper {
  public static void executeOnceAfter(Runnable instruction, long milis) {
    Timer t = new java.util.Timer();
    t.schedule(new java.util.TimerTask() {
      @Override
      public void run() {
        instruction.run();
        t.cancel();
      }
    }, milis);
  }
}
