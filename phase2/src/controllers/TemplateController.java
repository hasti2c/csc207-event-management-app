package controllers;

import presenter.InputParser;
import presenter.Presenter;
import usecases.TemplateManager;

import java.util.*;

public class TemplateController {
    private final TemplateManager templateManager;
    private final Presenter presenter;
    private final InputParser inputParser;

    public TemplateController(TemplateManager templateManager) {
        this.templateManager = templateManager;
        this.presenter = Presenter.getInstance();
        this.inputParser = InputParser.getInstance();
    }

    // == Creating New Template ==
    public void createNewTemplate() {
        String templateName = getNewTemplateName();
        templateManager.createTemplate(templateName);
        addFields(templateName);
    }

    private String getNewTemplateName() {
        presenter.printText("Enter the name of the template:");
        String templateName = inputParser.readLine();

        while (!templateManager.checkNameUniqueness(templateName)){
            presenter.printText("This template name is already taken. Please try a different name:");
            templateName = inputParser.readLine();
        }
        return templateName;
    }

    private void addFields(String templateName) {
        presenter.printText("Would you like to add a field? (Y/N)");
        boolean continueLoop = inputParser.readBoolean();
        if (!continueLoop) {
            presenter.printText("This will be an empty template. Are you sure you don't want to add a field? (Y/N)");
            if (inputParser.readBoolean()) {
                presenter.printText("You have successfully created an empty template!");
                return;
            } else {
                continueLoop = true;
            }
        }

        while (continueLoop) {
            addField(templateName);

            presenter.printText("Would you like to add a field? (Y/N)");
            continueLoop = inputParser.readBoolean();
        }
    }

    private void addField(String templateName) {
        presenter.printText("Enter the field name:");
        String fieldName = inputParser.readLine();

        String dataType = typeMenu();

        presenter.printText("Should this field be required?:");
        String isRequired = inputParser.readLine();

        templateManager.addNewFieldSpecs(templateName, fieldName, dataType, isRequired);
    }

    private String typeMenu(){
        List<String> options = Arrays.asList("string", "boolean", "int", "localdatetime");
        presenter.printMenu("Data Types", options);
        int choice = inputParser.readInt();

        try {
            return options.get(choice - 1);
        } catch (IndexOutOfBoundsException e) {
            presenter.printText("Your input was invalid. Please try again.");
            return typeMenu();
        }
    }
}
