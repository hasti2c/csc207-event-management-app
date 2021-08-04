package controllers;

import presenter.InputParser;
import presenter.Presenter;
import usecases.EventManager;
import usecases.TemplateManager;
import usecases.UserManager;

public class TemplateController {
    private final UserManager userManager;
    private final EventManager eventManager;
    private final TemplateManager templateManager;
    private final Presenter presenter;
    private final InputParser inputParser;

    public TemplateController(UserManager userManager, EventManager eventManager, TemplateManager templateManager) {
        this.userManager = userManager;
        this.eventManager = eventManager;
        this.templateManager = templateManager;
        this.presenter = new Presenter();
        this.inputParser = new InputParser();
    }

    public void createNewTemplate(String templateName, String username) {

    }



}
