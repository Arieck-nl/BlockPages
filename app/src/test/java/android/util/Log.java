package android.util;

/**
 * Created by Rick van 't Hof on 18/06/2023.
 */
public class Log {
   public static int d(String tag, String msg) {
      System.out.println("DEBUG: " + tag + ": " + msg);
      return 0;
   }

   public static int i(String tag, String msg) {
      System.out.println("INFO: " + tag + ": " + msg);
      return 0;
   }

   public static int w(String tag, String msg) {
      System.out.println("WARN: " + tag + ": " + msg);
      return 0;
   }

   public static int e(String tag, String msg) {
      System.out.println("ERROR: " + tag + ": " + msg);
      return 0;
   }
}
