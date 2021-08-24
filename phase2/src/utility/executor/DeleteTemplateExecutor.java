package utility.executor;

import controllers.ExitException;
import controllers.TemplateController;
import utility.CommandExecutor;
import utility.UserType;

public class DeleteTemplateExecutor implements CommandExecutor {
    TemplateController templateController;

    @Override
    public String execute(String username, UserType userType) throws ExitException {
        templateController.deleteTemplate();
        return username;
    }

    public void setTemplateController(TemplateController templateController) {
        this.templateController = templateController;
    }
}
