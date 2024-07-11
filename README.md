<!-- Improved compatibility of back to top link: See: https://github.com/othneildrew/Best-README-Template/pull/73 -->
<a name="readme-top"></a>

<!-- PROJECT SHIELDS -->
[![Email](https://img.shields.io/badge/Email-D14836?style=for-the-badge&logo=gmail&logoColor=white)](mailto:hyungkenine2003@gmail.com)
[![LinkedIn][linkedin-shield]][linkedin-url]
[![Website](https://img.shields.io/badge/Website-Visit-blue?style=for-the-badge)](https://andrewshinportfolio.netlify.app)
[![Contributors][contributors-shield]][contributors-url]


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


## Introduction

The AI Text Box Integrated Escape Room Game is designed to provide an immersive experience where players interact with an AI-powered text box to find clues and escape from different rooms. The game leverages the OpenAI API to process player inputs and generate responses, enhancing the gameplay with intelligent and context-aware interactions.

## Features

- **Interactive Gameplay**: Players interact with the game through an AI text box, finding clues and solving puzzles to escape rooms.
- **AI-Powered Responses**: The game uses the OpenAI API to generate intelligent and context-aware responses to player inputs.
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

#### Context and Motivation


#### Project Goals


#### Challenges and Learning Experience



<!-- CONTACT -->
## Contact

Email - hyungkenine2003@gmail.com

Project Link - [https://github.com/shyke0611/Car_Rental_Project.git](https://github.com/shyke0611/Car_Rental_Project.git)


<!-- MARKDOWN LINKS & IMAGES -->
[contributors-shield]: https://img.shields.io/github/contributors/github_username/repo_name.svg?style=for-the-badge
[contributors-url]: https://github.com/github_username/repo_name/graphs/contributors
[forks-shield]: https://img.shields.io/github/forks/github_username/repo_name.svg?style=for-the-badge
[forks-url]: https://github.com/github_username/repo_name/network/members
[stars-shield]: https://img.shields.io/github/stars/github_username/repo_name.svg?style=for-the-badge
[stars-url]: https://github.com/github_username/repo_name/stargazers
[issues-shield]: https://img.shields.io/github/issues/github_username/repo_name.svg?style=for-the-badge
[issues-url]: https://github.com/github_username/repo_name/issues
[license-shield]: https://img.shields.io/github/license/github_username/repo_name.svg?style=for-the-badge
[license-url]: https://github.com/github_username/repo_name/blob/master/LICENSE.txt
[linkedin-shield]: https://img.shields.io/badge/-LinkedIn-black.svg?style=for-the-badge&logo=linkedin&colorB=555
[linkedin-url]: https://www.linkedin.com/in/andrew-hk-shin
