package controllers;

import presenter.InputParser;
import presenter.Presenter;
import usecases.TemplateManager;

import java.util.*;

import static utility.AppConstant.EXIT_TEXT;
import static utility.AppConstant.MENU_EXIT_OPTION;

public class TemplateController {
    private final TemplateManager templateManager;
    private final Presenter presenter;
    private final InputParser inputParser;

    public TemplateController(TemplateManager templateManager) {
        this.templateManager = templateManager;
        this.presenter = Presenter.getInstance();
        this.inputParser = InputParser.getInstance();
    }

    /**
     * Create custom template by prompting current user for event type, name of template, and custom fields.
     */
    // == Creating New Template ==
    public void createNewTemplate() {
        String templateName = getNewTemplateName();
        templateManager.createTemplate(templateName);
        addFields(templateName);
        presenter.printText("Your new template was created.");
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

    public void addNewField() throws ExitException{
        presenter.printText("Which Template would you like to add the new field to?:");
        String templateName = chooseTemplate();
        addField(templateName);
    }
    public void deleteField() throws ExitException{
        presenter.printText("Which Template would you like to delete field from?:");
        String templateName = chooseTemplate();

        presenter.printText("Enter the field name:");
        String fieldName = inputParser.readLine();

        templateManager.deleteFieldSpecs(templateName, fieldName);
    }

    /**
     * Helper function to prompt current user for custom field specifics: name, type.
     */
    private void addField(String templateName) {
        presenter.printText("Enter the field name:");
        String fieldName = inputParser.readLine();

        String dataType = typeMenu();

        presenter.printText("Should this field be required? (Y/N):");
        boolean isRequired = inputParser.readBoolean();

        templateManager.addNewFieldSpecs(templateName, fieldName, dataType, isRequired);
    }

    private String typeMenu(){
        List<String> options = Arrays.asList("string", "boolean", "int", "localdatetime");
        presenter.printMenu("Data Types", options);
        return inputParser.getMenuChoice(options);
    }

    // == Editing Templates ==
    /**
     * Prints a list of templates and returns the user's choice. If the choice is longer than the list of templates,
     * it means the user chose to go back.
     * @return returns the index of the chosen template + 1 (starts at 1 instead of 0)
     */
    public String chooseTemplate() throws ExitException {
        List<String> templateList = templateManager.returnTemplateNames();
        templateList.add(MENU_EXIT_OPTION);
        presenter.printMenu("TemplateList", templateList);
        return inputParser.getMenuChoice(templateList, true);
    }

    public void editTemplate() throws ExitException {
        String templateName = chooseTemplate();
        editTemplateName(templateName);
    }

    private void editTemplateName(String templateName) {
        String newTemplateName = getChangedTemplateName(templateName);
        templateManager.editTemplateName(templateName, newTemplateName);
        presenter.printText("Template name edited successfully.");

    }

    private String getChangedTemplateName(String templateName) {
        presenter.printText("Please enter a new name for the template.");
        String newTemplateName = inputParser.readLine();

        while (!templateManager.checkNameUniqueness(templateName) || templateName.equals(newTemplateName)){
            if (!templateName.equals(newTemplateName))
                presenter.printText("Please enter a different name.");
            else if (templateManager.checkNameUniqueness(newTemplateName))
                presenter.printText("This name is already taken by another template. Please try again.");
            templateName = inputParser.readLine();
        }
        return templateName;
    }

    public void deleteTemplate() throws ExitException{
        presenter.printText("Which Template would you like to delete?:");
        String templateName = chooseTemplate();
        templateManager.deleteTemplate(templateName);
    }



}
