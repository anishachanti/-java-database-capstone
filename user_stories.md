# User Story Template

**Title:**
_As a [user role], I want [feature/goal], so that [reason]._

**Acceptance Criteria:**
1. [Criteria 1]
2. [Criteria 2]
3. [Criteria 3]

**Priority:** [High/Medium/Low]
**Story Points:** [Estimated Effort in Points]
**Notes:**
- [Additional information or edge cases]



## Admin User Stories
1. Admin Login
Title:
As an admin, I want to log into the portal using my username and password, so that I can securely manage the platform.

Acceptance Criteria:

Admin can enter a valid username and password.
The system authenticates the admin credentials.
Invalid credentials display an error message.
Priority: High
Story Points: 3
Notes:

Passwords must be securely stored.
2. Admin Logout
Title:
As an admin, I want to log out of the portal, so that I can protect system access.

Acceptance Criteria:

Admin can log out using the logout option.
The session is terminated successfully.
The admin is redirected to the login page.
Priority: High
Story Points: 2
Notes:

Prevent access to protected pages after logout.
3. Add Doctors
Title:
As an admin, I want to add doctors to the portal, so that they can manage patient appointments.

Acceptance Criteria:

Admin can enter doctor details.
The system validates the required information.
The doctor is successfully added to the database.
Priority: High
Story Points: 5
Notes:

Required details include name, specialization, and contact information.
4. Delete Doctor Profiles
Title:
As an admin, I want to delete a doctor's profile, so that outdated or inactive profiles can be removed.

Acceptance Criteria:

Admin can select a doctor's profile.
The system asks for confirmation before deletion.
The selected profile is removed or deactivated.
Priority: Medium
Story Points: 3
Notes:

Existing appointment records should be preserved.
5. View Monthly Appointment Statistics
Title:
As an admin, I want to run a MySQL stored procedure to retrieve the number of appointments per month, so that I can track platform usage statistics.

Acceptance Criteria:

A stored procedure retrieves appointment counts grouped by month.
The procedure can be executed through MySQL CLI.
The monthly appointment statistics are displayed as query results.
Priority: Medium
Story Points: 5
Notes:

Use appointment dates for monthly grouping.


## Patient User Stories

1. View Doctors
Title:
As a patient, I want to view a list of doctors without logging in, so that I can explore available options before registering.

Acceptance Criteria:

Patients can view the doctor list without authentication.
Doctor names and specializations are displayed.
Patients can browse available doctor profiles.
Priority: High
Story Points: 3
Notes:

Only publicly available doctor information should be displayed.
2. Patient Registration
Title:
As a patient, I want to sign up using my email and password, so that I can book appointments.

Acceptance Criteria:

Patients can register using a valid email and password.
The system validates the registration details.
A patient account is successfully created.
Priority: High
Story Points: 5
Notes:

Email addresses must be unique.
3. Patient Login
Title:
As a patient, I want to log into the portal, so that I can manage my bookings.

Acceptance Criteria:

Patients can enter their registered email and password.
The system verifies the credentials.
Successful login redirects patients to their dashboard.
Priority: High
Story Points: 3
Notes:

Invalid login attempts should display an appropriate error.
4. Patient Logout
Title:
As a patient, I want to log out of the portal, so that I can secure my account.

Acceptance Criteria:

Patients can access the logout option.
The current session is terminated.
Patients are redirected to the login page.
Priority: High
Story Points: 2
Notes:

Protected pages should not be accessible after logout.
5. Book an Appointment
Title:
As a patient, I want to log in and book an hour-long appointment, so that I can consult with a doctor.

Acceptance Criteria:

Patients must log in before booking.
Patients can select a doctor and an available time slot.
The system books an appointment for 60 minutes and confirms the booking.
Priority: High
Story Points: 8
Notes:

Prevent double booking of the same time slot.
6. View Upcoming Appointments
Title:
As a patient, I want to view my upcoming appointments, so that I can prepare accordingly.

Acceptance Criteria:

Patients can view their scheduled appointments.
Appointment details include doctor name, date, and time.
Only the logged-in patient's appointments are displayed.
Priority: High
Story Points: 3
Notes:

Display appointments in chronological order.


## Doctor User Stories

1. Doctor Login
Title:
As a doctor, I want to log into the portal, so that I can manage my appointments.

Acceptance Criteria:

Doctors can enter their registered credentials.
The system authenticates their login details.
Successful login redirects doctors to their dashboard.
Priority: High
Story Points: 3
Notes:

Only authorized doctors should access the dashboard.
2. Doctor Logout
Title:
As a doctor, I want to log out of the portal, so that I can protect my data.

Acceptance Criteria:

Doctors can log out using the logout option.
The session is terminated successfully.
Doctors are redirected to the login page.
Priority: High
Story Points: 2
Notes:

Prevent unauthorized access after logout.
3. View Appointment Calendar
Title:
As a doctor, I want to view my appointment calendar, so that I can stay organized.

Acceptance Criteria:

Doctors can view their scheduled appointments.
Appointments are displayed with dates and times.
Doctors can view upcoming appointments in chronological order.
Priority: High
Story Points: 5
Notes:

Calendar should display relevant appointment details.
4. Mark Unavailability
Title:
As a doctor, I want to mark myself unavailable during specific time slots, so that patients can see only my available slots.

Acceptance Criteria:

Doctors can select dates and time slots as unavailable.
The system saves the unavailability information.
Unavailable slots cannot be booked by patients.
Priority: High
Story Points: 5
Notes:

Existing appointments should not be affected without proper rescheduling.
5. Update Doctor Profile
Title:
As a doctor, I want to update my profile with specialization and contact information, so that patients have up-to-date information.

Acceptance Criteria:

Doctors can edit their profile information.
The system validates the updated details.
Changes are saved and displayed on the doctor profile.
Priority: Medium
Story Points: 3
Notes:

Required fields should not be left empty.
6. View Patient Details
Title:
As a doctor, I want to view patient details for upcoming appointments, so that I can prepare for consultations.

Acceptance Criteria:

Doctors can view patient details associated with their appointments.
Relevant patient information is displayed.
Doctors can access only information they are authorized to view.
Priority: High
Story Points: 5
Notes:

Patient information must remain confidential and secure.
