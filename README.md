# SnowPaste Delivery
---
This is a basic Delivery project for CS5500 Fall 2020.
* Group Members: Xinyan Zhang, 
* Website: https://snow-paste-frontend.herokuapp.com
* Video Presentation: <>
* API documentation: 

## Design Document
[Google Docs Link](https://docs.google.com/document/d/1jgMuL628ks2XB1XmzFtcONzXRl6IS-U3eA_Ug2Pe9XQ/edit?usp=sharing)

## UML Diagram
* ##### Class Diagram
![Class Diagram](./asset/class_diagram.png)

* ##### State Diagram
![State Diagram](./asset/state_diagram.png)

## Requirements

A recent Gradle (>= 6.1.1 but < 7.0.0) and JDK 11.

## Building

`./gradlew build`

## Testing

`./gradlew test`

## Running

`./gradlew run`

The server will start on port 5000 by default.

## Deployment Instruction

* #### Backend Deployment
1. Create a new app on Heroku for backend
2. Under the `Settings` tab, click `Reveal Config Vars` in the `Config Vars` section
3. Enter the following variables:
    * KEY: `MONGODB_URI`
    * VALUE: `mongodb+srv://cindy:zxy123@cluster0.6lhhg.mongodb.net/delivery?retryWrites=true&w=majority`
4. Click `Add` to save the variables
5. Go to https://github.com/SnowPaste/delivery and fork the repository to your own github 
6. Create a local copy of the repo with `git clone`
7. To add the `heroku` remote, go to the local folder where you clone the repository and type `heroku git:remote -a <Your Backend Heroku App Name>` in the terminal
8. Download and install the [Heroku CLI](https://devcenter.heroku.com/articles/heroku-cli). Login to your Heroku account by typing `heroku login` in the terminal
9. To deploy the backend application, type `git push heroku main`
10. Wait for a minute or two until the app finish building and deploying

* #### Frontend Deployment
1. Go to https://github.com/SnowPaste/delivery-react-frontend and fork the frontend repository to your own github
2. Create a local copy of the repo with `git clone`
3. Go to the `/src` folder and open the `config.js` file. Change the host to be the domain of your backend Heroku app. You can find the domain in the `Domains` section under the `settings` tab on your Heroku app website.
5. Create another app on Heroku for frontend
6. Make sure you are logged in to Heroku by typing `heroku login` in the terminal
7. To add the `heroku` remote, type `heroku git:remote -a <Your Frontend Heroku App Name>`
8. To deploy the frontend application, type `git push heroku master`
9. After the build and deploy is complete, you should be able to see the app when you access the frontend domain


## Spotless?

Spotless automatically formats code. If it detects errors, run `./gradlew spotlessApply`
to automatically fix them. `./gradlew spotlessCheck` can be used to directly invoke
Spotless.
