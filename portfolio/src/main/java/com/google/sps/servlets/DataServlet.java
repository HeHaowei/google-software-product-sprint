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

package com.google.sps.servlets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import com.google.gson.Gson;
import com.google.sps.data.Comment;
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {
  
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    
    ArrayList<Comment>  comments = new ArrayList<Comment>();
    Query query = new Query("Comment").addSort("timestamp", SortDirection.DESCENDING);
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);
    for (Entity entity : results.asIterable()) {
      long id = entity.getKey().getId();
      String message = (String) entity.getProperty("message");
      long timestamp = (long) entity.getProperty("timestamp");
      String userEmail = (String) entity.getProperty("userEmail");

      Comment comment = new Comment(id, message, timestamp);
      comments.add(comment);
    }

    //convert the comments to json format
    String json = convertToJsonUsingGson(comments);

    // Send the JSON as the response
    response.setContentType("application/json;");
    response.getWriter().println(json);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

    UserService userService = UserServiceFactory.getUserService();
    String commentMessage = getParameter(request, "comment-area");
    Entity messageEntity = new Entity("Comment");
    String userEmail = userService.getCurrentUser().getEmail();
    if (!commentMessage.isEmpty()) {
        long timestamp = System.currentTimeMillis();
        messageEntity.setProperty("message", commentMessage);
        messageEntity.setProperty("timestamp", timestamp);
        messageEntity.setProperty("userEmail", userEmail);
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        datastore.put(messageEntity);
    }
    // Redirect back to the HTML page.
    // 
    response.sendRedirect("/index.html");
  }

 /**
   * Converts a ServerStats instance into a JSON string using the Gson library. Note: We first added
   * the Gson library dependency to pom.xml.
   */
  private String convertToJsonUsingGson(ArrayList<Comment> comments) {
    Gson gson = new Gson();
    String json = gson.toJson(comments);
    return json;
  }

/**
  * Get parameter from user input.
  */
  private String getParameter(HttpServletRequest request, String name) {      
      String parameter = request.getParameter(name);
      return parameter;
  }

}
