# 🎮 AptiRush – Java GUI Aptitude Quiz Game

AptiRush is a Java Swing–based aptitude quiz game designed for students preparing for placement exams.  
It includes a GUI interface, multiple difficulty levels, live scoring, a leaderboard, and shuffled questions for every attempt.

---

## 🚀 Features

### 🖥 GUI Interface (Swing)
- Clean and simple layout  
- Buttons for answering  
- No console required  

### 🎯 Multi-Level Quiz
- Levels: **Easy → Medium → Hard**  
- After each level, the user can choose to continue or stop  

### 🔀 Shuffled Questions
- Every time the quiz starts, the question order changes  

### 🧠 Automatic Score Tracking
- Score updates in real time  
- Saves final score to `scores.txt`  

### 🏆 Leaderboard
- Shows **Top 5 scores**  
- Sorted highest to lowest  
- Uses scores stored in `scores.txt`

### 📁 File Handling
- Reads questions from:
  - `easy.txt`
  - `medium.txt`
  - `hard.txt`
- Writes scores to `scores.txt`

---

## 📂 Project 
AptiRush/
│
├── AptiRush.java
├── easy.txt
├── medium.txt
├── hard.txt
└── scores.txt   (auto-generated)
