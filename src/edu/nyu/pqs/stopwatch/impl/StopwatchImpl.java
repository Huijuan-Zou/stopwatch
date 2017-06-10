package edu.nyu.pqs.stopwatch.impl;

import java.util.ArrayList;
import java.util.List;
import edu.nyu.pqs.stopwatch.api.Stopwatch;

public class StopwatchImpl implements Stopwatch{
  private final String stopwatchId;
  private long start;
  private boolean stop;
  private List<Long> lapTimes;
  private final Object sync = new Object();

  protected StopwatchImpl(String id) {
    this.stopwatchId = id;
    lapTimes = new ArrayList<>();
    start = -1;
    stop = false;
  }

  @Override
  public String getId() {
    return stopwatchId;
  }

  @Override
  public void start() {
    if (start != -1 && stop == false) {
      throw new IllegalStateException("Already started!");
    }
    synchronized(sync) {
      if (stop == false || start == -1) {
        start = System.currentTimeMillis();
      }
      if (stop == true) {
        start = System.currentTimeMillis() - lapTimes.get(lapTimes.size() - 1);
        lapTimes.remove(lapTimes.size() - 1);
        stop = false;
      }
    }
  }

  @Override
  public void lap() {
    if (stop == true || start == -1) {
      throw new IllegalStateException("Stopwatch doesn't start yet!");
    }
    synchronized(sync) {
      long end = System.currentTimeMillis();
      long curLap = end - start;
      lapTimes.add(curLap);
      start = end;
    }
  }

  @Override
  public void stop() {
    if (start == -1 || stop == true) {
      throw new IllegalStateException("Stopwatch doesn't start yet!");
    }
    synchronized(sync) {
      long end = System.currentTimeMillis();
      long curLap = end - start;
      lapTimes.add(curLap);
      stop = true;
    }
  }

  @Override
  public void reset() {
    synchronized(sync) {
      start = -1;
      stop = false;
      lapTimes.clear();
    }
  }

  @Override
  public List<Long> getLapTimes() {
    List<Long> lapTimesCopy = new ArrayList<>();
    synchronized(sync) {
      lapTimesCopy = lapTimes;
    }
    return lapTimesCopy;
  }

  @Override
  public String toString() {
    return "stopwatchId: " + stopwatchId + ", start: " + start + ", lapTimes: "
        + lapTimes + ".";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + stopwatchId.hashCode();
    return result;
  }

  /* {@InheritDoc}
   * two different stopwatches that happen to have the same internal state are not equal.
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    return false;
  }  
}