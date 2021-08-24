package utility.executor;

import controllers.ExitException;
import controllers.TemplateController;
import utility.CommandExecutor;
import utility.UserType;

public class ChangeTemplateNameExecutor implements CommandExecutor {
    TemplateController templateController;

    public void setTemplateController(TemplateController templateController) {
        this.templateController = templateController;
    }

    @Override
    public String execute(String username, UserType userType) throws ExitException {
        templateController.editTemplateName();
        return username;
    }
}
