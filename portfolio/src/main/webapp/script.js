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

/**
 * Adds a random greeting to the page.
 */
function addRandomGreeting() {
  const greetings =
      ['boy', 'Chinese', 'future programmer', 'basketball lover'];

  // Pick a random greeting.
  const greeting = greetings[Math.floor(Math.random() * greetings.length)];

  // Add it to the page.
  const greetingContainer = document.getElementById('greeting-container');
  greetingContainer.innerText = greeting;
}

/**
 * This function is to get the login status of the user
 */
function getLoginStatus() {
    fetch('/login').then(response => response.json()).then((logObject) => {
        console.log(logObject);
        const loginElement = document.getElementById('login');
        loginElement.innerHTML = '<h4>Login Status: '+ logObject.loginStatus + '<h4>';
    })

}


/**
 * This function is to get data from "/data" Servlet 
 */
 
function getDataFromDataServlet() {

    fetch('/data').then(response => response.json()).then((comments) => {
         const commentsElement = document.getElementById('comment-list');
         commentsElement.innerHTML = '';
         const commentListElement = document.createElement('ul');
         for (comment of comments) {
             commentListElement.appendChild(createListElement(comment));
         }
         commentsElement.appendChild(commentListElement);
     });
}

function createListElement(comment) {
  const commentElement = document.createElement('li');
  commentElement.className = 'comment';
  
  const timestampElement = document.createElement('h4');
  const messageElement = document.createElement('p');
  timestampElement.setAttribute('class', 'time');
  messageElement.setAttribute('class', 'msg');
  messageElement.innerText = comment.message;
  timestampElement.innerText = convertTime(comment.timestamp);

  commentElement.appendChild(messageElement);
  commentElement.appendChild(timestampElement);

  return commentElement;
}

function convertTime(timestamp) {
    var date = new Date(timestamp);
    // Hours part from the timestamp
    var hours = date.getHours();
    // Minutes part from the timestamp
    var minutes = "0" + date.getMinutes();
    // Seconds part from the timestamp
    var seconds = "0" + date.getSeconds();

    var year = date.getFullYear();
    var months = ['Jan','Feb','Mar','Apr','May','Jun','Jul','Aug','Sep','Oct','Nov','Dec'];

    var month = months[date.getMonth()];
    var day = date.getDate();

    // Will display time in 10:30:23 format
    var formattedTime = month + ' ' + day + ' ' + year + ' ' + hours + ':' + minutes.substr(-2) + ':' + seconds.substr(-2);

    return formattedTime;
}

window.setInterval(getDataFromDataServlet, 3000);

