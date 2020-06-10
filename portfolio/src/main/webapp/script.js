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
 * This function is to get data from "/data" Servlet 
 */
 
function getDataFromDataServlet() {

    //  fetch('/data').then(response => response.text()).then((helloMessage) => {
    //      document.getElementById('hello-message').innerHTML=helloMessage;
    //  });

    fetch('/data').then(response => response.json()).then((comments) => {
         const commentsElement = document.getElementById('comment-list');
         commentsElement.innerHTML="<h3>Comments:</h3>";
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
  commentElement.innerText = comment.message;
  return commentElement;
}

