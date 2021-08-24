README
# CSC207 Project Phase 1 README

## Running Instructions

Pull from [https://markus.teach.cs.toronto.edu/git/csc207-2021-05/group_0014](https://markus.teach.cs.toronto.edu/csc207-2021-05/assignments/1#) and open the project in Intellij. To run program first install the GSON library: in Intellij go to Project structure and install GSON library from Maven (com.google.code.gson:gson:2.8.5) as well as the apache library (org.apache.commons:commons-text:1.9) also from maven.

To run the program, go to PhaseTwoMain.java and run. Make sure to open the project to group_0014 as the top level folder to ensure the file paths are correct.

UML diagram is included as a PNG and as a UML file.

## Overview of Functionality

The program is an event creation system.

### Users can:

- Trial the system
    - Create events but do not save
    - View other's events but cannot interact with them
- Create an account
    - Choose to be admin or regular user
- Log in to their account
- Log out of their account
- Create an event based on existing templates
    - Publish and unpublish their events
    - Delete their events
- Attend events created and published by other users (and their own)
- Unattend events that they've attended
- View their own events
- View other user's published events
    - Separated into ones they're attending and ones they are not
- Edit Account info
    - Change their username
    - Change their password
    - Change their email
    - Delete their account
    - Change from regular user to admin
- At any point, type "back" to return to a previous menu or if they are on a menu, they can choose the go back option on the menu.
- Save everything/all data

### Admin Users Can
- edit templates
- create new templates
- suspend users
- suspend events
- send announcements to all users
- 

### The System

- All information is saved into files that is human-readable
    - Files can be edited externally
- All read into the program automatically

## Design Decisions and Notes

- Admin users have all the same permissions as a regular user plus they can edit template names
    - They do not have to go back to a different "regular" account to do things
- Trial users can only create events and view events, but they cannot attend or save their event
- Events have custom fields that are determined by the template used to create it
- Templates are abstract and new templates can be added from files (without changing the code)
- Templates are a collection of fields. Each field can be specified independently and the number of fields is very flexible.
- Events are created based on a template but once the event is created, it no longer depends on the template
- Note: the design of templates in the program makes it very easily extendable

## Future Improvements

- Much of the textUI (dialogue) is currently hard-coded and can be changed to be more separated from the code (eg read from files)
- Use cases could extend an interface (dependency injection). In general include more interfaces in the program.
- The responsibilities can be better separated between the controllers
- Some controller methods can be split into smaller pieces to be made more concise and modular
