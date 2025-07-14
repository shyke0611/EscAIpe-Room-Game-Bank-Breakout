<!-- Improved compatibility of back to top link: See: https://github.com/othneildrew/Best-README-Template/pull/73 -->
<a name="readme-top"></a>
<!-- PROJECT LOGO -->
<br />
<div align="center">

  <h3 align="center">EscAIpe Room (Bank Breakout: Heist Escape Room)</h3>

  <p align="center">
    Software Engineering Part 2 Group Project for the University of Auckland
  </p>
</div>

<br><br>

<!-- ABOUT THE PROJECT -->
## About The Project
Welcome to the AI Text Box Integrated Escape Room Game! This project is a thrilling and interactive escape room game where players must find clues, solve riddles and puzzles to escape various rooms. The game is developed using Java, JavaFX, CSS, and the OpenAI API.

## Project Demo

https://github.com/shyke0611/EscAIpe-Room-Game-Bank-Breakout/assets/126353075/60250d84-5b6b-4adc-9240-5a6e4906af67


## Introduction

The AI Text Box Integrated Escape Room Game is designed to provide an immersive experience where players interact with an AI-powered text box to find clues and escape from different rooms. The game leverages the OpenAI API to process player inputs and generate responses, enhancing the gameplay with intelligent and context-aware interactions.

## Features

- **Interactive Gameplay**: Players interact with the game through an AI text box, finding clues and solving puzzles to escape rooms.
- **AI-Powered Responses**: The game uses the OpenAI API to generate intelligent and context-aware responses to player inputs.
- **Cognitive Novelty**: Each gameplay session introduces unique and varying elements, keeping the experience fresh and increasing replay value by stimulating player curiosity and engagement.
- **Engaging UI**: The game is developed with JavaFX, providing a rich and engaging user interface.
- **Stylish Design**: Custom CSS is used to enhance the visual appeal of the game.
- **Difficulty Levels**: Choose from Easy, Medium, and Hard levels to match your skill and challenge preference.
- **Timers**: Select from 2, 4, or 6-minute timers to add an extra layer of excitement and urgency to the game.

## Technologies Used

- **Java**: The core programming language used for the game's logic.
- **JavaFX**: Used for creating the user interface.
- **CSS**: Used for styling the game interface.
- **OpenAI API**: Used to process player inputs and generate AI-powered responses.

## To setup OpenAI's API

- add in the root of the project (i.e., the same level where `pom.xml` is located) a file named `apiproxy.config`
- put inside the credentials that you received from no-reply@digitaledu.ac.nz (put the quotes "")

  ```
  email: "upi123@aucklanduni.ac.nz"
  apiKey: "YOUR_KEY"
  ```
  these are your credentials to invoke the OpenAI GPT APIs

## To setup codestyle's API

- add in the root of the project (i.e., the same level where `pom.xml` is located) a file named `codestyle.config`
- put inside the credentials that you received from gradestyle@digitaledu.ac.nz (put the quotes "")

  ```
  email: "upi123@aucklanduni.ac.nz"
  accessToken: "YOUR_KEY"
  ```

 these are your credentials to invoke GradeStyle

## To run the game

`./mvnw clean javafx:run`

## To debug the game

`./mvnw clean javafx:run@debug` then in VS Code "Run & Debug", then run "Debug JavaFX"

## To run codestyle

`./mvnw clean compile exec:java@style`

<!-- REFLECTION -->
## Reflection
[Full report.pdf](https://github.com/user-attachments/files/16173822/SOFTENG206-Final-Report-Team-25.pdf)

#### Context and Motivation

In the development of our AI Text Box Integrated Escape Room Game, our primary challenge was defining a distinct persona for the AI. This involved creating an AI character that would be unique, intuitive, and enhance the gaming experience. Through numerous discussions and iterations, we crafted an AI persona that aligned with the game’s context and resonated with our target audience. Another major challenge was ensuring seamless interaction between the players and the AI, which we addressed by designing a small, unobtrusive user interface and implementing a "quick hint" feature to keep the gameplay engaging.

#### Project Goals

Our goals for this project were to create an immersive escape room game leveraging AI for interactive gameplay. The AI was intended to provide intelligent, context-aware responses to player inputs, enhancing the gaming experience. We aimed to use technologies such as Java, JavaFX, CSS, and the OpenAI API to develop a visually appealing and functionally robust game. Additionally, we sought to incorporate different difficulty levels and timer options to cater to varying player preferences and to ensure a smooth, engaging user experience.

#### Challenges and Learning Experience

We faced several challenges during the development process. One of the largest was managing the intricacies of prompt engineering to ensure the AI provided relevant and consistent responses. This involved extensive testing and tweaking of prompts to balance specificity and flexibility. Another challenge was the technical implementation of AI within the game, including managing response times and minimizing unnecessary API calls to reduce costs. Through these challenges, we learned the importance of concise prompt design, efficient API usage, and the value of iterative testing. These experiences have enhanced our understanding of integrating AI into interactive applications and highlighted the potential for AI to revolutionize user experiences.

### GPT API Diagram
![API Diagram](https://github.com/shyke0611/EscAIpe-Room-Game-Bank-Breakout/assets/126353075/f8345fef-ffee-4cd1-add6-4609a31f9b33)


<!-- MARKDOWN LINKS & IMAGES -->
[contributors-shield]: https://img.shields.io/github/contributors/shyke0611/EscAIpe-Room-Game-Bank-Breakout.svg?style=for-the-badge
[contributors-url]: https://github.com/shyke0611/EscAIpe-Room-Game-Bank-Breakout/graphs/contributors
[forks-shield]: https://img.shields.io/github/forks/shyke0611/EscAIpe-Room-Game-Bank-Breakout.svg?style=for-the-badge
[forks-url]: https://github.com/github_username/repo_name/network/members
[stars-shield]: https://img.shields.io/github/stars/github_username/repo_name.svg?style=for-the-badge
[stars-url]: https://github.com/github_username/repo_name/stargazers
[issues-shield]: https://img.shields.io/github/issues/github_username/repo_name.svg?style=for-the-badge
[issues-url]: https://github.com/github_username/repo_name/issues
[license-shield]: https://img.shields.io/github/license/github_username/repo_name.svg?style=for-the-badge
[license-url]: https://github.com/github_username/repo_name/blob/master/LICENSE.txt
[linkedin-shield]: https://img.shields.io/badge/-LinkedIn-black.svg?style=for-the-badge&logo=linkedin&colorB=555
[linkedin-url]: https://www.linkedin.com/in/andrew-hk-shin
