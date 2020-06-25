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
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
      
    ArrayList<TimeRange> event_time = new ArrayList<TimeRange>();
    Set<String> request_attendees = new HashSet<String>(request.getAttendees());
    for (Event event: events) {
        Set<String> intersection_attendees = new HashSet<String>(request.getAttendees());
        Set<String> event_attendees = new HashSet<String>(event.getAttendees());
        intersection_attendees.retainAll(event_attendees);
        if (!intersection_attendees.isEmpty()) {
            event_time.add(event.getWhen());
        }
    }
    return queryByTime(mergeEventTime(event_time), request.getDuration());
  }

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
