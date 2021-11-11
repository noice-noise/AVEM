# AVEM CHANGELOG

v.0.10

Created "main" package for "App.java" class launcher of "Main.java" JavaFX Application.
Created "core" package for handling essential Java functions.
Created "fxml" package for complex UI forms and views.
Created "tests" package for automatic testing in JUnit 4.
Created "css" package for themes and style sheets.
Created "resoureces" for icons and other media files.


v.0.20

- Created Equipment Manager to handle tests
- Created comprehensive suite of tests for Equipment Manager.
- Created basic UI for Dashboard.
- Created comprehensive pnlDashboard stack pane for handling side bar navigation buttons.


v.0.22
- Finished Equipment Manager file handing with appending, parsing, and retrieving files upon exit.


v.0.30

- Created "developer" package for Developer testing tools.
- Created DeveloperTools.
- Created AVETheme.
- Updated UI for Dashboard  
- Created CSS style sheets for UI light mode theme.
- Added static class EquipmentView.


v.0.40
- Updated overall UI design.
- UI are now round corners with drop shadows.
- Updated UI icons of sidebars from outlined to filled rounded Material.
- Theme properties of children windows are now responsive from their root/parents.
- Created AVEvent, AVDate, and AVTime.
- AVEventManager, and AVEvent tests.

v.0.41
- Improved Event Reservation page.
- Created Reservation Equipment view that allows multiple selection.
- Implemented fill, clear, and confirmation functions of event templates in reservation page.
- Changed Developer tools to Admin tools.

v.0.5.0
- Created the Calendar grid and allows viewing dates and reservations by week. 
- Implemented the ReservationManager to the grid.
- Improved error handling method in Reservation page.
- Updated Reserved equipment fucntionality which now marks concerned equipment "Reserved" in the Equipment Library and cannot be viewed again for another reservation.

v.0.6.0
- Created the Check in/out page.
- Added a Reservation Scroll Bar, Event, and Equipment pane.
- Merged the Check-in and Check-out page for efficient navigation.
- Implemented the buttons and error handling.
- Updated Check-in and Check-out buttons to mark equipment reservations as "Checked-in" and "Checked-out" instantaneously.
- Improved Admin tools with the ability to initialize improved library of equipment and reset all files including reservations.

v.0.7.0
- Created Dark Mode Theme
- Created Dark Mode CSS style sheets
- Created Appearance button for theme toggles

v.0.80
- Moved Admin tools from the side bar to the Settings
- Implemented Accounts buttons of sidebar
- Added Manage Account functionality including (sign up, edit, and remove)
- Added User Activity view 
- Enabled updates for Announcement
- Enabled editing of currently logged-in profile
- Added simple FAQs area

v.1.0.0
- Fixed time period conflicts.
- Improved handling of reservation conflicts.
- Added Checking of Equipment view during check in and out process.
- Added report Defects functionality during check in and out process.
- Re-align "Sort by" in Equipment side bar
- Restrict buttons for different account accesses
- Change "refresh" in Equipment to "Report Defects"
- Mark/Update reservation's event status: On going, Cancelled, Post-poned etc.
- Enabled Settings for Guest access to enable Appearance and FAQs buttons.
- Updated Sign out button to show signed in account name.
- Updated check in and check out process to allow synchronized equipment location base on event venue.  
- Restricted editing of Name, Type, and Serial Number equipment properties to avoid discrepancies between reservation information.
- Added five theme colors with compatibility for light and dark mode.