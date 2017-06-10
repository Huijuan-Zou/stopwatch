package edu.nyu.pqs.stopwatch.impl;

import java.util.ArrayList;
import java.util.List;
import edu.nyu.pqs.stopwatch.api.Stopwatch;

/**
 * The StopwatchFactory is a thread-safe factory class for Stopwatch objects.
 * It maintains references to all created Stopwatch objects and provides a
 * convenient method for getting a list of those objects.
 *
 */
public class StopwatchFactory {
  private volatile static List<Stopwatch> stopwatches = new ArrayList<>();
  private volatile static List<String> idList = new ArrayList<>();
  private final static Object sync = new Object();

  /**
   * Creates and returns a new Stopwatch object
   * @param id The identifier of the new object
   * @return The new Stopwatch object
   * @throws IllegalArgumentException if <code>id</code> is empty, null, or
   *     already taken.
   */
  public static Stopwatch getStopwatch(String id) {
    if (id == null || id.equals("")) {
      throw new IllegalArgumentException("Id can't be empty or null!");
    } else if (idList.contains(id)) {
      throw new IllegalArgumentException("Id is already taken!");
    } 
    final Stopwatch stopwatch;
    synchronized(sync) {
      stopwatch = new StopwatchImpl(id);
      idList.add(id);
      stopwatches.add(stopwatch);
    }
    return stopwatch;
  }

  /**
   * Returns a list of all created stopwatches
   * @return a List of all creates Stopwatch objects.  Returns an empty
   * list if no stopwatches have been created.
   */
  public static List<Stopwatch> getStopwatches() {
    final List<Stopwatch> stopwatchCopy;
    synchronized(sync) {
      stopwatchCopy = stopwatches;
    }
    return stopwatchCopy;
  }
}