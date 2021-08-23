import controllers.SystemController;
import utility.Command;

public class PhaseTwoMain {
    public static void main(String[] args) {
        SystemController controller = new SystemController();
        Command.initializeAllExecutor(controller.getExecutorBuilder());
        controller.run();
    }
}
