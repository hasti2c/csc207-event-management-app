package utility.executor;

import controllers.ExitException;
import controllers.TemplateController;
import utility.CommandExecutor;
import utility.UserType;

public class CreateTemplateExecutor implements CommandExecutor {
    TemplateController templateController;

    @Override
    public String execute(String username, UserType userType) throws ExitException {
        templateController.createNewTemplate();
        return username;
    }

    public void setTemplateController(TemplateController templateController) {
        this.templateController = templateController;
    }
}
