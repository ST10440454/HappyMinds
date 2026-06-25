HappyMinds 📚

A South African primary school learning app for Grades 1–7, built for Android.
HappyMinds gives learners access to structured curriculum notes across four core
subjects — all offline, no internet required.


Table of Contents


About the App
Features
Screenshots Overview
Tech Stack
Project Structure
Getting Started
Admin Login
How the Database Works
Curriculum
User Roles
App Flow
Build & Release
Known Limitations



About the App

HappyMinds is a parent-managed learning companion for South African primary school
learners in Grades 1 to 7. A parent registers an account, waits for admin approval,
then sets up a child profile by selecting the child's name, age and grade. The app
automatically loads the correct curriculum notes for that grade.

The app is fully offline — no internet connection is needed at any point.


Features

For Learners


Structured lesson notes for every topic across 4 subjects
Progress tracking per lesson (0%, in-progress, 100% complete)
Start / Continue / Review button per lesson based on saved progress
Subject filter chips (All, Maths, English, Science, Life Skills)
Search lessons by name
Overall progress dashboard with per-subject breakdown


For Parents


Register an account (pending admin approval)
Set up a child profile (name, age, grade 1–7)
Edit profile at any time
View child's progress and completed lessons
Donate via the Support screen


For Admins


Separate admin login (not visible to regular users on splash)
Approve or reject pending user registrations
View all registered users and their status
View donation records


How the Database Works

HappyMinds uses two local storage mechanisms — no server or cloud database.

User Accounts — SharedPreferences + JSON

All registered user accounts are stored as a JSON array in a private
SharedPreferences file called happyminds_users.


Each User object is serialised with Gson.
Passwords are stored as a hash (String.hashCode()).
The logged-in session is saved in a separate happyminds_session preferences file,
so the user stays logged in after closing the app.


Lesson Progress — Room (per-user)

Each user gets their own isolated Room database file:

user_db_<userId>.db

This means:
After registration, the progress database is completely empty — nothing is
pre-populated.
Progress is only written when a learner opens a lesson and taps "Mark as Complete".
If two children use the same device, their progress is fully separated.


Donations — Room (shared)

Donation records are stored in a shared Room database (user_db_shared.db)
so the admin dashboard can view all donations regardless of which user submitted them.


Curriculum

The curriculum is defined entirely in Curriculum.kt inside Models.kt.
No network calls are made — all content is hardcoded in Kotlin strings.

Grades

Grades 1 through 7 are supported (South African primary school).

Subjects (4 only)

SubjectEmojiMathematics🔢English📖Natural Science🔬Life Skills🌱

Lessons per grade

Each grade has 3 lessons per subject = 12 lessons per grade = 84 lessons total.

Lesson notes format

Every lesson note contains:


Learning objectives — what the learner will achieve
Notes section — clear explanations with examples, tables and diagrams (text)
Practice questions — 4–5 questions per lesson
Answers — provided at the end so learners can self-check


Adding or editing lessons

To change lesson content, open:

app/src/main/java/com/happyminds/app/data/Models.kt

Find the relevant mathsFor(), englishFor(), scienceFor() or lifeSkillsFor()
function. Locate the correct grade block (when (g)) and edit the note string
inside the L(...) call.


User Roles

RoleAccessHow to log inParentRegister + set up child profile, view learning centre and progressRegister → await approval → loginAdminApprove/reject users, view donationsAdmin Login link on login screenVolunteerSubmit a volunteer applicationVolunteer link on login screen

Registration flow

Parent registers → status = PENDING
Admin logs in → approves account → status = APPROVED
Parent logs in → completes child profile → enters learning centre

If an account is PENDING, login shows: "Your account is pending admin approval."
If REJECTED, login shows: "Your account request was declined."
