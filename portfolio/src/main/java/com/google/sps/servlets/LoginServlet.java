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

import com.google.appengine.api.datastore.*;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import com.google.gson.Gson;
import com.google.sps.data.LogObject;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");
    PrintWriter out = response.getWriter();
    UserService userService = UserServiceFactory.getUserService();

    LogObject logObject = new LogObject();

    // If user is not logged in, show a login form (could also redirect to a login page)
    if (!userService.isUserLoggedIn()) {
        String loginUrl = userService.createLoginURL("/");
        logObject.setLoginStatus(false);
        logObject.setLoginUrl(loginUrl); 
        String json = convertToJsonUsingGson(logObject);
        out.println(json);
      return;
    }
    logObject.setLoginStatus(true);

    // If user has not set a nickname, redirect to nickname page
    // String nickname = getUserNickname(userService.getCurrentUser().getUserId());
    // if (nickname == null) {
    //   response.sendRedirect("/nickname");
    //   return;
    // } else {
    //     logObject.setUsername(nackname);
    // }

    // User is logged in and has a nickname, so the request can proceed
    String logoutUrl = userService.createLogoutURL("/");
    logObject.setLogoutUrl(logoutUrl);
    String json = convertToJsonUsingGson(logObject);
    out.println(json);

    // out.println("<p>Hello " + nickname + "!</p>")
    // out.println("<p>Logout <a href=\"" + logoutUrl + "
    // \">here</a>.</p>");
    // out.println("<p>Change your nickname <a href=\"/nickname\">here</a>.</p>");
    // response.getWriter().println(infoMsg);
  }

  /** Returns the nickname of the user with id, or null if the user has not set a nickname. */
  private String getUserNickname(String id) {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Query query =
        new Query("UserInfo")
            .setFilter(new Query.FilterPredicate("id", Query.FilterOperator.EQUAL, id));
    PreparedQuery results = datastore.prepare(query);
    Entity entity = results.asSingleEntity();
    if (entity == null) {
      return null;
    }
    String nickname = (String) entity.getProperty("nickname");
    return nickname;
  }

   /**
   * Converts a ServerStats instance into a JSON string using the Gson library. Note: We first added
   * the Gson library dependency to pom.xml.
   */
  private String convertToJsonUsingGson(LogObject logObject) {
    Gson gson = new Gson();
    String json = gson.toJson(logObject);
    return json;
  }
}