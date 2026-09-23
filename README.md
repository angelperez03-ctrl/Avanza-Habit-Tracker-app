# Avanza-Habit-Tracker-app outline




COM 437 – Project Outline

Angel Perez

Saint Leo University

COM 437: Mobile Application Development

Professor Marwan Omar

August 31, 2026



1. **Project Description:**
   
The following project will consist of the development of a Habit Tracker mobile application called Avanza. This application will be developed as an Android mobile application using Java. This app is designed to help users create, monitor, and maintain positive habits. Users will have the ability to create personal habits, set how frequent they want to complete them, record daily progress, and review their performance over time. This application will provide notifications which will remind users to complete their habit. The app will also have streak tracking and statistics that reflect user progress. This will enable users to stay consistent and motivated when completing a habit.

2. **Problem Addressing:**
   
Avanza aims to address multiple problems that users face. For example, many people struggle to maintain consistent routines as they might forget their goals or lose motivation. Also, there may not be a way to monitor their progress. Manual writing of routines does not provide reminders or tracking of habit progress. The Habit Tracker will address these issues by providing users with a centralized platform to track, organize, and manage their habits. Notifications will be used to remind users to complete their habit. A way to mark a habit complete and track their progress will also be implemented within this application. This will ensure that users are reminded of their goals and promote consistency.

3. **Platform:**
   
The platform used to develop the habit tracker application will be Android. The development platforms that will be used are Android Studio and Java. Java will be the main programming language for this application. GitHub will be used to store, track, and manage code. Android is best for this application as this can be downloaded across multiple brands of devices. Making this application as a mobile application is easier for users as they are more likely to carry their phones and can quickly check or update their habits. To address security concerns, this application will store user information locally on their devices.

4. **Front/Back end support:**
   
Java will be programming language for the frontend of the mobile application. XML will also be used to design the layout of each screen. This will create a user interface which will allow users to interaction with the application. Users can interact with the application through screens, buttons, forms, lists, navigation menus, and progress displays. The interface will be user-friendly and not too complex, which can provide users with easy navigation and access to different features of the application. Java will create the functionality of the application which ensures each button, form, list, menu, and display works as intended. The backend for the Avanza application will use SQLite database. The database will contain user information to be stored locally. The database will also contain other information such as habit names, descriptions, schedules, completion dates, reminder times, streak information, and habit completion frequency. The logic for this application will consist of calculation of statistics and current habit streaks. Completion rate percentage, current habit progress, and the total number of habits to do will also be calculated in the backend.

5. **Functionality:**
   
The main functions of Avanza will consist of:

•	Create account (sign up functionality)

•	Forgot password

•	User authentication (sign in functionality)

•	Create a new habit

•	Edit existing habits

•	Delete habits

•	Add a name and description for each habit

•	Select how frequently a habit should be completed

•	Mark a habit as completed for the current day

•	View complete and incomplete habits

•	Track current habit streaks

•	Track the longest streak for each habit

•	View habit completion history

•	Set reminder notifications for habits

•	Show total weekly check-ins

•	Show habit completion progress

•	Display the user’s current weekly habit completion progress as a percentage

•	Display weekly habit completion rate

•	Settings

•	Account Settings

These will be the basic functionalities for the habit tracker application to be implemented within an 8-week timeframe. Optional features include a dark mode theme, achievements, and motivational messages if there is enough time. 

6. **Design(wireframes)**

The application will use a simple mobile design for the habit tracker application. The main screens will include the login, signup, forgot password, home/dashboard screen, add habit screen, habit details and progress screen, and settings screen. Figma will be used to create these designs.

<img width="381" height="778" alt="Screenshot 2026-08-29 212053" src="https://github.com/user-attachments/assets/dfbba863-7ee6-4d3d-9883-1ebbde542777" />

This first design is the log in screen of the Avanza habit tracker application. This user will enter their credentials once an account has been created. Several options for a user include the ability to select “Create an account”. This will redirect them to the sign up screen, where users can create their Avanza account. If a user forgets their password, they can select the forgot password, in which Avanza will redirect the user to the forgot password display to change their password. To log into their account, users will need to enter their email address and password they used when creating their account. After credentials have been provided, users can select sign in which will take them to their Avanza habit dashboard.

<img width="412" height="840" alt="Screenshot 2026-08-29 221335" src="https://github.com/user-attachments/assets/dc1aa0fa-b7b4-4472-87fc-30b9a6563889" />

The following explains the account creation display for Avanza. This will allow users to create their account to access their dashboard screen. This screen prompts the user to enter their full name, email address, and password. Users after entering their information, they will select the sign up button to create their account. Afterwards, the application will automatically redirect the user to their Avanza home/dashboard display.

<img width="408" height="835" alt="Screenshot 2026-08-29 222026" src="https://github.com/user-attachments/assets/de593e9d-b763-4e3c-85bd-64ac7a0372bb" />
 
The next Avanza display will be used to allow the user to change their password. If users forget their password to their Avanza account, this can select the forgot password option and the application will redirect the user to the change password display. The following information is required from users to change their password. For instance, the user’s email address is required to ensure that the password is changed for the associated account. The application will then require the user to enter their new password and confirm the new password to make the change. After the user changes their password, they will be sent back to the log in screen where they will sign in with the updated credentials. 

<img width="398" height="815" alt="Screenshot 2026-08-30 173600" src="https://github.com/user-attachments/assets/8528db07-ba98-4b09-9e08-c6f652262ab3" />
 
The Avanza home/dashboard screen design includes several different features for this application, such as creating a habit, tracking weekly progress, tracking habit completion progress, and current habit streaks. Each name and description of the habit will be displayed. The bottom menu includes different options for the user to navigate throughout the app. These options include a way to access the home dashboard, habit statistics for the user, and settings. The user’s name and date are displayed at the top of the dashboard. The buttons next to the habit names and descriptions are the edit button and mark complete (check-in) button. Habit completion frequency is displayed next to the streaks of each app, determining whether the user wants to complete the habit everyday or another specific time of the week. Weekly progress is shown at the top to let users know how many out of the total habits are completed for this week. The days of the week for each habit are highlighted as the user completes their habit each day.

<img width="399" height="816" alt="Screenshot 2026-08-30 183525" src="https://github.com/user-attachments/assets/db951b9f-1e91-47cb-b459-ce0fa0120d7b" />
 
Habit statistics will be incorporated within Avanza. The statistics display shows best streak, indicating the longest streak in a habit. For instance, if a user has a streak of 13 days for one habit, this information will be displayed on the statistics screen. Other information includes the user’s completion rate, total habits currently being tracked, number of completed check-ins for the week, and weekly completion. The completion rate is calculated by finding the average weekly completion percentage across all habits. For each habit, the number of days completed during the week is divided by the total number of days in the week. For example, if the Morning Run habit was completed 6 out of 7 days, its completion rate would be calculated as 6 ÷ 7, which equals approximately 0.857, or 85.7%. The same calculation is performed for each habit. Once all individual habit completion rates have been calculated, they are added together and divided by the total number of habits. The resulting value represents the user's average weekly completion rate and is converted into a percentage for display. The total number of habits currently tracked is shown and the total number of check-ins for the week is also displayed to show the current number of days the user has completed a habit. The weekly completion information is shown to allow users to see their progress for each habit.

<img width="836" height="798" alt="Screenshot 2026-08-30 211506" src="https://github.com/user-attachments/assets/b86f86f6-8c2b-4c5d-b749-a2bf5604ca67" />
 
Settings and account settings are added. The settings screen displays the user’s name, an edit button, the option to enable notifications and reminder time. An option for signing out is provided. The edit button redirects the user to the account settings where the user can change their account details. User’s in account settings can change their name and email. Once the changes have been made, the user can select update profile. Notifications can be enabled or disabled, and a reminder time can be manually set by the user to remind them when the application should let them know to complete a habit.

<img width="845" height="800" alt="Screenshot 2026-08-30 211540" src="https://github.com/user-attachments/assets/c84dc45c-f5ae-4dba-bcfd-057dd30ca385" />
 
Finally, the habit creation and habit editing features enables a user to add and edit habits. Habit deletion is also displayed within the edit habit feature. The new habit feature prompts the user to enter habit name, a description of the habit, and habit frequency. The habit editing feature allows the user to change habit details such as name, description, and frequency. For adding a new habit, the user simply selects add habit. To update a habit’s information, the user can select save or cancel if they do not want to change the habit’s information. If a user decides to delete a habit, they can select the delete button which will display a warning message. The warning message will alert the user if they want to delete the habit. The users select the delete button to remove a habit.


Project (M5) Updates:

Several changes have been added to the Avanza Habit Tracker application in the past few weeks. During weeks 2-3, changes include the creation of the initial Avanza Habit Tracker design, wireframes, screen layouts, navigation, and basic application structure. During week 4, the SQLite database functionality was implemented. Other changes include overall user authentication, user account creation, login, password hashing/verification, password reset/change functionality, and habit editing features. Improvements to the dashboard, settings, and statics screens have also been implemented to further enhance the application. The project has been pushed to GitHub. Current changes for this week include habit creation, storing habits within the database, and connecting habit information to dashboard and statistics screens. Future changes include implementing habit completion/progress tracking, settings/account settings, notification/reminder time functionality, testing and fixing bugs. The final tested version will be reflected in GitHub and in the README file. Any changes or modifications to the application will also be explained in the README file in future updates.

Version Changelog 1.1

Previous Updates (Weeks 2-4): 
•	Created the initial Avanza Habit Tracker application design
•	Developed application wireframes for Avanza
•	Created screen layouts
•	Implemented navigation between application displays
•	Established basic Android application structure
•	Created dashboard, settings, statics, and account screens such as login, account creation, change password
•	Implemented SQLite database functionality
•	Added user authentication
•	Added user account creation
•	Implemented user login functionality
•	Added password hashing and password verification
•	Added password reset and change-password functionality
•	Added habit editing functionality
•	Improved dashboard screen
•	Improved settings and account settings screens
•	Improved the statistics screen
•	Push Avanza Habit Tracker project to GitHub

Current Updates (Week 5):
•	Implement habit creation functionality
•	Store newly created habits in the SQLite database
•	Connect stored habit information to the Avanza dashboard
•	Connect habit data to the Statistics screen

Future Updates (Weeks 6-8):
•	Implement habit completion and progress tracking.
•	Continue improvements to Settings and Account Settings functionality.
•	Implement notification and reminder-time functionality.
•	Continue updating and pushing application changes to GitHub.
•	Test all major application features.
•	Identify and fix application bugs.
•	Verify that database functionality operates correctly.
•	Verify habit creation, editing, completion, and progress tracking.
•	Test user account and authentication functionality.
•	Test notification and reminder functionality.
•	Complete final application improvements.
•	Update the GitHub repository with the final tested version.
•	Update the README file to reflect the final application features and changes.
•	Document any additional modifications made to the application before final submission.


**References:**

Figma. (2025). Figma: The collaborative interface design tool. Figma. https://www.figma.com/
