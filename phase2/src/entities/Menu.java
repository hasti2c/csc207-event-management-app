package entities;

import utility.Command;

import java.util.List;

public class Menu {
    private Command superCommand;
    private Command command;
    private List<Command> subCommands;

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
}
