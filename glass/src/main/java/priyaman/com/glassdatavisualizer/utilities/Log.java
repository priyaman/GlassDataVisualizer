package priyaman.com.glassdatavisualizer.utilities;

import android.os.SystemClock;


public final class Log
{
    private static final String TAG = "GlassDataVirtualizer";
    public static int LOGLEVEL = android.util.Log.VERBOSE;
    public static final boolean V = LOGLEVEL <= android.util.Log.VERBOSE;
    public static final boolean D = LOGLEVEL <= android.util.Log.DEBUG;
    public static final boolean I = LOGLEVEL <= android.util.Log.INFO;
    public static final boolean W = LOGLEVEL <= android.util.Log.WARN;
    public static final boolean E = LOGLEVEL <= android.util.Log.ERROR;

    public static void v(String logMe)
    {
        android.util.Log.v(TAG, SystemClock.uptimeMillis() + " " + logMe);
    }
    public static void v(String logMe, Throwable ex)
    {
        android.util.Log.v(TAG, SystemClock.uptimeMillis() + " " + logMe, ex);
    }

    public static void d(String logMe)
    {
        android.util.Log.d(TAG, SystemClock.uptimeMillis() + " " + logMe);
    }
    public static void d(String logMe, Throwable ex)
    {
        android.util.Log.d(TAG, SystemClock.uptimeMillis() + " " + logMe, ex);
    }

    public static void i(String logMe)
    {
        android.util.Log.i(TAG, logMe);
    }
    public static void i(String logMe, Throwable ex)
    {
        android.util.Log.i(TAG, logMe, ex);
    }

    public static void w(String logMe)
    {
        android.util.Log.w(TAG, logMe);
    }
    public static void w(String logMe, Throwable ex)
    {
        android.util.Log.w(TAG, logMe, ex);
    }

    public static void e(String logMe)
    {
        android.util.Log.e(TAG, logMe);
    }
    public static void e(String logMe, Exception ex)
    {
        android.util.Log.e(TAG, logMe, ex);
    }
}
