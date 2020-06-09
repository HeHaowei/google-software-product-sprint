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

import com.google.gson.Gson;
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {
  private ArrayList<String> messages;
  
  public void init(){
      messages = new ArrayList<String>();
    //   messages.add("This is the first message.");
    //   messages.add("This is the second message! ");
    //   messages.add("Third message come in.");
    //   messages.add("Last message!");
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // response.setContentType("text/html;");
    // response.getWriter().println("<h1>Hello HE HAOWEI!</h1>");

    //convert the messages to json format
    String json = convertToJsonUsingGson(messages);

    // Send the JSON as the response
    response.setContentType("application/json;");
    response.getWriter().println(json);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    if (messages.isEmpty()) {
        messages = new ArrayList<String>();
    }  

    String commentMessage = getParameter(request, "comment-area");
    if (!commentMessage.isEmpty()) {
        messages.add(commentMessage);
    }
    // Redirect back to the HTML page.
    response.sendRedirect("/index.html");
  }

 /**
   * Converts a ServerStats instance into a JSON string using the Gson library. Note: We first added
   * the Gson library dependency to pom.xml.
   */
  private String convertToJsonUsingGson(ArrayList<String> message) {
    Gson gson = new Gson();
    String json = gson.toJson(message);
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
