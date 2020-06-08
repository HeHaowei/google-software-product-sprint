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

    fetch('/data').then(response => response.json()).then((messages) => {
         console.log(messages);
         const messageElement = document.getElementById('hello-message');
         messageElement.innerHTML="<h3>Comments:</h3>";
         const messageListElement = document.createElement('ul');
         for (msg of messages) {
             messageListElement.appendChild(createListElement(msg));
         }
         messageElement.appendChild(messageListElement);
     });
}

function createListElement(text) {
  const liElement = document.createElement('li');
  liElement.innerText = text;
  return liElement;
}

