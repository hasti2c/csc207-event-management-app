package entities;

import utility.Command;
import utility.Savable;

import java.util.List;

public class Menu implements Savable {
    private Command superCommand;
    private Command command;
    private List<Command> subCommands;

    public Menu(Command superCommand, Command command) {
        this.superCommand = superCommand;
        this.command = command;
    }

    public Menu(){

    }

    public Command getSuperCommand() {
        return superCommand;
    }

    public void setSuperCommand(Command superCommand) {
        this.superCommand = superCommand;
    }

    public Command getCommand() {
        return command;
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    public List<Command> getSubCommands() {
        return subCommands;
    }

    public void setSubCommands(List<Command> subCommands) {
        this.subCommands = subCommands;
    }

    @Override
    public String getID() {
        return command.getName();
    }
}
