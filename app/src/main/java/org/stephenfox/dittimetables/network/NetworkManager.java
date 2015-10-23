package org.stephenfox.dittimetables.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


/**
 * A class for querying basic network inforamtion
 */
public class NetworkManager {

  /**
   * Determines whether the device has internet connection or not.
   *
   * @param context A context.
   * @return boolean True: Device has connection to internet.
   *                 False: Device does not have connection to internet.
   *
   * Reference:
   * {@link
   * 'http://developer.android.com/training/monitoring-device-state/connectivity-monitoring.html'}
   */
  public static boolean hasInternetConnection(Context context) {
    ConnectivityManager connManager = (ConnectivityManager)
        context.getSystemService(Context.CONNECTIVITY_SERVICE);

    NetworkInfo activeNetwork = connManager.getActiveNetworkInfo();
    return activeNetwork != null && activeNetwork.isConnected();
  }
}
