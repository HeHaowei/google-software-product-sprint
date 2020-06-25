// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps;

import java.util.ArrayList; 
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;   
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class FindMeetingQuery {

  /**
   * query is to query the available TimeRange for a given request and events list
   * Input: Collection of events, MeetingRequest
   * Output: Collection of available TimeRange 
   */

  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {

    ArrayList<TimeRange> event_time = new ArrayList<TimeRange>();
    Set<String> request_attendees = new HashSet<String>(request.getAttendees());
    for (Event event: events) {
        Set<String> intersection_attendees = new HashSet<String>(request.getAttendees());
        Set<String> event_attendees = new HashSet<String>(event.getAttendees());

        // Check whether the event has intersection of attendees with the request
        // If there is intersection, the request must not overlap with the event time
        intersection_attendees.retainAll(event_attendees);
        if (!intersection_attendees.isEmpty()) {
            event_time.add(event.getWhen());
        }
    }
    return queryByTime(mergeEventTime(event_time), request.getDuration());
  }

  /**
   * mergeEventTime is to merge the overlap intervals.
   * Input: the list of event_time
   * Output: merged and sorted list of event_time
   * e.g. [1,3], [2,8], [4,5], [9,10] will be merged into [1,8],[9,10] 
   * [a, b] means [start_time, end_time]
   * The algorithm is: sort the start_time and end_time of the event in ascending order separately
   * Then the above example will become:
   * start_time: (1, 2, 4, 9)
   * end_time: (3, 5, 8, 10)
   * For i from 0 to the size of the eventList, if start_time[i + 1] > end_time[i] or i = size - 1, meaning
   * there generates an interval: [start_time[j], end_time[i]], j is the starting point of the interval
   * and j will be updated to i + 1 after each merge operation 
   */

  private ArrayList<TimeRange> mergeEventTime(ArrayList<TimeRange> event_time) {

      int size = event_time.size();
      if (size < 2) return event_time;
      
      Collections.sort(event_time, TimeRange.ORDER_BY_START);
      ArrayList<TimeRange> event_time_start_order = new ArrayList<TimeRange>(event_time);
      Collections.sort(event_time, TimeRange.ORDER_BY_END);
      ArrayList<TimeRange> event_time_end_order = new ArrayList<TimeRange>(event_time);
      ArrayList<TimeRange> result = new ArrayList<TimeRange>();

      for (int i = 0, j = 0; i < size; i++) {

          if ((i + 1 == size) || (event_time_start_order.get(i + 1).start() > event_time_end_order.get(i).end())) {
              result.add(TimeRange.fromStartEnd(event_time_start_order.get(j).start(), event_time_end_order.get(i).end(), false));
              j = i + 1;
          }
      }
      return result;

  }

  /**
   * queryByTime is to query the availabe TimeRange given the merged and sorted event_time list 
   * and the duration of the request event
   * Input: ArrayList of merged and sorted event_time list and the duration of the request event
   * Output: Collection of available TimeRange 
   * e.g: the event_time is [2,5) [8, 10) [14, 20) [21, 22) and the full range is [0, 24]
   *      and the request duration is 2 
   *      [a, b] or [a, b) is [start_time, end_time] or [start_time, end_time)
   *      Thus, the output result will be [0, 2), [5, 8), [10,14), (22, 24] 
   */
 
    private Collection<TimeRange> queryByTime(ArrayList<TimeRange> event_time, long duration) {

        ArrayList<TimeRange> result = new ArrayList<TimeRange>();
        int current_start = TimeRange.START_OF_DAY;
        for (int i = 0; i < event_time.size(); i++) {
            TimeRange current_time = TimeRange.fromStartEnd(current_start, event_time.get(i).start(), false);
            if (current_time.duration() >= duration) {
                result.add(current_time);
            }
            current_start = event_time.get(i).end();
        }

        TimeRange current_time = TimeRange.fromStartEnd(current_start, TimeRange.END_OF_DAY, true);
        if (current_time.duration() >= duration) {
            result.add(current_time);
        }

        return result;
    } 

  
}
