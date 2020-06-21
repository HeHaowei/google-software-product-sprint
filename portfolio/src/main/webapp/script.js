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
    fetch('/login').then(response => response.json()).then((loginStatus) => {
        console.log(loginStatus);
        const commentForm = document.getElementById('comment-form');
        const loginElement = document.getElementById('login');
        console.log(loginStatus.displayname);
        console.log(typeof loginStatus.displayname);
        loginElement.innerHTML = '<h4>Login Status: '+ loginStatus.loginStatus + '</h4>';
        if (loginStatus.loginStatus){
            if (loginStatus.displayname === "" || typeof loginStatus.displayname === "undefined") {
                console.log("displayname has not been set")
                loginElement.innerHTML = '<h4> Hello, ' + loginStatus.userEmail + '!</h4>'
                loginElement.appendChild(createRedirectButtonElement('/displayname', 'Set Display name'));
               }
               else {
                    loginElement.innerHTML = '<h4> Hello, ' + loginStatus.displayname + '!</h4>'
                    loginElement.appendChild(createRedirectButtonElement('/displayname', 'Change Display name'));
               }
               loginElement.appendChild(createRedirectButtonElement(loginStatus.logoutUrl, 'logout'));
               commentForm.style.display="block";
        }
        else {
            loginElement.innerHTML = '<h4> Log in to leave your comment!</h4>'
            loginElement.appendChild(createRedirectButtonElement(loginStatus.loginUrl, 'login'));
            commentForm.style.display="none";
        }
    })

}

function createRedirectButtonElement(redirectUrl, buttonText) {
    const buttonElement = document.createElement('button');
    buttonElement.innerText = buttonText;
    buttonElement.addEventListener("click", function(){
        window.location.href = redirectUrl;
    });
    return buttonElement;
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

  const userEmail = document.createElement('h4');
  const timestampElement = document.createElement('h4');
  const messageElement = document.createElement('p');
  timestampElement.setAttribute('class', 'time');
  messageElement.setAttribute('class', 'msg');

  if (typeof(comment.user) === "undefined") {
    userEmail.innerText = 'Anonymous User';
  }
  else { 
      userEmail.innerText = 'User: ' + comment.user;
  }
  messageElement.innerText = comment.message;
  timestampElement.innerText = convertTime(comment.timestamp);

  commentElement.appendChild(userEmail);
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

