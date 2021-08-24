package entities;

import utility.Command;
import utility.Savable;

import java.util.List;

public class Menu implements Savable {
    private Command superCommand;
    private Command command;
    private List<Command> subCommands;

    /**
     * Creates a new Menu.
     * @param superCommand Super command of this menu.
     * @param command Root command of this menu.
     * @param subCommands List of subcommands of this menu.
     */
    public Menu(Command superCommand, Command command, List<Command> subCommands) {
        this.superCommand = superCommand;
        this.command = command;
        this.subCommands = subCommands;
    }

    /**
     * Creates a new Menu.
     */
    public Menu(){}

    /**
     * @return Root command of this menu.
     */
    public Command getCommand() {
        return command;
    }

    /**
     * @return Subcommands of this menu.
     */
    public List<Command> getSubCommands() {
        return subCommands;
    }

    @Override
    public String getID() {
        return command.getName();
    }
}
