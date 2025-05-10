# Workout Tracker App (Android)

### 👨‍💻 Authors  
**Terence**, **Feng Ming**, **Wei Hou**, **Huang Ai**

---

A lightweight **workout tracking Android application** built with **Java** and **SQLite** that allows users to track and visualise their fitness activities.

This project also includes a **testing suite** to ensure the app's functionality and reliability, including **unit tests** and **instrumented UI tests** for comprehensive coverage of the core features.

## 📱 Features

- 🚶 Track number of **steps taken**  
- 🔥 Track **calories burned**  
- ⏱️ Track **workout duration**  
- 🗺️ Track and display workout path using **GPS**  
- 📸 Take **photos** during workouts  
- 🖼️ View photos and mapped workout path  
- 📤 Share **workout summary** and photos with others  

## 🧪 Testing

Testing was an essential part of the development process to ensure the quality and functionality of the app. The app uses the following testing strategies:

### Unit Testing
- **JUnit** is used for unit testing to verify the functionality of the app's core components, ensuring individual methods and logic are correct.

### Instrumented Testing
- **Android Instrumentation Tests** were implemented to verify the UI and app interaction, making sure that the app's features work as expected in real user scenarios.

### Test Coverage
- The testing directory contains various test cases for:
  - **Step counting functionality**
  - **Workout data persistence in SQLite**
  - **GPS location tracking**
  - **Camera API and photo-taking functionality**
  - **App UI interaction**

Test cases were written using **JUnit** and **Android Instrumentation** to simulate real user actions and ensure the app behaves as expected across different devices.

### 📁 Test Directory Structure

**Source Code:** [app/src/main/java/com/murdoch/fitnessapp](https://github.com/lee-xin-jin-terence/workout-tracking-android-app/tree/main/app/src/main)  
         
**Unit Tests (JUnit):** [app/src/test/java/com/murdoch/fitnessapp](https://github.com/lee-xin-jin-terence/workout-tracking-android-app/tree/main/app/src/test/java/com/murdoch/fitnessapp)  
   
**Automated Instrumented Tests (Android JUnit Runner):** [app/src/androidTest/java/com/murdoch/fitnessapp](https://github.com/lee-xin-jin-terence/workout-tracking-android-app/tree/main/app/src/androidTest/java/com/murdoch/fitnessapp)

---

## App Screenshots

<img width="158" alt="image" src="https://github.com/user-attachments/assets/1499ea18-7a18-4406-96cc-55ee4e2adbc4" />  
<img width="158" alt="image" src="https://github.com/user-attachments/assets/ad17789c-1be2-417b-a2aa-6a9bc3dc7200" />  
<img width="158" alt="image" src="https://github.com/user-attachments/assets/41015781-3d20-4439-a382-147a21a8d6f3" />  
<img width="158" alt="image" src="https://github.com/user-attachments/assets/9e3dd90b-f91f-445c-89bf-3475f813b7aa" />

## 📄 Project Materials

- 📘 [Project Documentation (PDF)](https://github.com/user-attachments/files/20025691/Android-Workout-App-Documentation.pdf)  
- 📊 [Presentation Slides (PPTX)](https://github.com/user-attachments/files/20025688/Presentation_Slides.pptx)  
- 🎥 [Video Demonstration of Step Count Functionality](https://github.com/user-attachments/assets/372eda37-9b29-4224-b829-93f48907a25c)

## 🛠️ Tech Stack

- **Java** – Primary language  
- **SQLite** – Local database for persisting workout data  
- **Google Maps API** – For tracking workout path  
- **Camera API** – For photo capture  
- **Android Location Services** – For GPS tracking  
- **Android Share Intent** – For sharing workout data  
- **JUnit** – For unit testing  
- **Android Instrumentation** – For UI testing  
