# List of Features and Design Patterns

## List of features added:

- Ability for users to message other users and admins. They can also view their inbox.
- Ability for admins to send an announcement.
- Admins have a shared inbox where they can see all messages sent to admin
- Adding / Removing Friends.
- Ability for admin to make a user an admin.
- Ability to create new Templates within the system.
- Ability to edit existing Templates within the system.
- Suspend a user permanently or for a given amount of time.
    - When a user gets suspended, their events are automatically turned to private so other users can no longer see them.
    - Once a user is unsuspended, they will be able to manually edit the privacy status of their events to change it to what they desired.
- Unsuspend a suspended user
- A new type of user: Temporary user - gets suspended after a specified time
- Ability to get a new password “emailed” to you.
- Password strength testing.
- Added more event privacy types: Friends only, Public, and Private.
- Ability to change between them.
- Admins can suspend events.
  - A suspended event cannot be seen by other users except for the owner. 
  - Only admins can unsuspend a suspended event.
- 
- Ask for a temporary password if the user forgets their password.
- The user is able to “Go back” at basically any point in the program (using an exit exception rather than simply exiting a loop).
- Admins can grant regular users admin privileges.

## Design Patterns:

### Dependency Injection:

The dependency injection pattern is used by the Manager classes and the Gateway classes. The Gateways implement the interface IGateway and are passed into the Managers’ constructors. This allows us to save and load the program from the Managers via the Gateways which would otherwise not be possible. Additionally, we use Dependency Injection within the System Controller and the sub-controllers. Sometimes we need to access a method from another controller and rather than creating a new object, we pass an already created object to where it is needed. Implementing this design pattern eliminates hard dependencies: if we hypothetically add an additional parameter to the constructor of a dependency, we do not need to change code in the class using the dependency.

### Singleton:

The singleton design pattern is used for Presenter and InputParser. This means that their constructor is private, and they instead have a public static getInstance() method, which returns an instance of the class. This instance is always the same regardless of how many time we call getInstance(). The reason for this was that we want only one instance of each of these classes to exist in the code. This makes sure of that, and also frees classes from having to pass that singular instance around, because any class can get an instance of it directly.

### Iterator:

The iterator pattern is used in the MessageBox class by implementing the Iterable interface in Java. We decided to use an iterator to retrieve a user's messages as it would make changes to the MessageBox extensible in the future. Currently, we store messages in a List, but potentially in the future we may change the data structure. If we didn't have the iterator we would be forced to modify code everywhere we iterated the List in MessageBox, but if we had the iterator, it would only take one change in the MessageBox class. The pattern prevents future shotgun surgeries.

### State/Strategy:

(From Angela Xiang’s exam)
In phase 2 of our project, we realized that as we were refactoring we had a giant (around 23 cases) switch statement that was essentially just a way to choose which "action" (what we call a command in our program) the user chose and to run the methods associated with the command do what the user wants the program to do. For example, one of our commands is `SIGN_UP`. When the user chooses the menu item "Sign Up" the switch statement will go to the correct case and execute the signUp() method.

This design was really clunky and every time we want to add a new command this giant switch statement would need to have a new option. This is not only a code smells but also does not adhere well to the Open/Closed principle.

So, we decided that using the State/Strategy design pattern we could fix this issue. (Unfortunately we were not able to fully implement this design pattern as it was a lot of brute force coding, so we decided not to submit this version as our master copy. We have a branch in our git repository called strategy_state_builder that has the code with this design pattern as well as the builder below.)

The reason why in our program we also included State together with Strategy is that for us, the "family" of algorithms do wildly different things depending on the command that the user chooses. It is quite general but we know that each of the commands must execute something. Since the algorithm that runs depends on the "state" of the user's choice and not on a choice in code that perhaps a client programmer is writing, this could be considered as the State design pattern. So our program is somewhat a mix of both.

In our project, we create an interface called "CommandExecutor" that will be implemented by all of the Executor classes. This interface only includes a method called execute(). Then we create a class for each command and implement the CommandExecutor interface for each. Depending on the class the execute method will do many different things. For example, for the SignUp command, execute() will call a userSignUp() method in the UserController. We continue this for each command. Ultimately we wouldn't have to edit any other existing classes for this, only remove methods from the SystemController.

From https://refactoring.guru/design-patterns/strategy it says "Strategy is a behavioural design pattern that lets you define a family of algorithms, put each of them into a separate class, and make their objects interchangeable." Our program does just that. We have a family of algorithms, this family is one of the Commands that the user at the keyboard might want to execute. Then we put each in a separate class all implementing the same interface so that they are interchangeable.

For us, this gets rid of the very large switch statement and makes the equivalent code in the SystemController just one line: command.execute(); (Command is a stand-in). Also if we were to add new commands we would only have to add a new class, not changing the original code at all. This makes it adhere much better to the SOLID principles.

### Builder:

When implementing the above state/strategy we needed to include a builder in order to build all of the executors. This class imports all of our managers and controllers to build the executors.The builder has a method to build each executor then returns an instance of the executor

## Design Patterns Not Used:

### Observer (for messaging system, with Message being the class observed):

Observer pattern is used for classes, the Observables, to subscribe to state changes of the class being observed. Because we intended for Message being the class observed and MessageBox being the Observable, this would be impractical, since Message doesn’t undergo state changes. It doesn’t change once created.

If we had every MessageBox observe every Message, every MessageBox would know every Message being sent. This could be a privacy issue.