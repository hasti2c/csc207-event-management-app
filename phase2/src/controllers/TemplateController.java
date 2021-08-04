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

    public void createNewTemplate() {
        presenter.printText("Enter the type of the event:");
        String templateName = inputParser.readLine();
        String newTemplateId = templateManager.createTemplate(templateName);

//        1. user inputs fieldname, datatype, isRequired
//        2. we use setters in FieldSpecs to set these to a new FieldSpecs
//        3. Add these fieldSpecs with addFieldSpecs method


        presenter.printText("Enter the field name:");
        String fieldName = inputParser.readLine();
        presenter.printText("Enter the field data type:");
        String dataType = inputParser.readLine();
        presenter.printText("Do you need this field?:");
        String isRequired = inputParser.readLine();

        templateManager.addFieldSpecs(newTemplateId, templateManager.createNewFieldSpecs(fieldName, dataType, isRequired));

    }



}
