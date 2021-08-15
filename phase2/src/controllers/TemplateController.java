package controllers;

import presenter.InputParser;
import presenter.Presenter;
import usecases.TemplateManager;

import java.util.HashMap;
import java.util.Map;

public class TemplateController {
    private final TemplateManager templateManager;
    private final Presenter presenter;
    private final InputParser inputParser;

    public TemplateController(TemplateManager templateManager) {
        this.templateManager = templateManager;
        this.presenter = Presenter.getInstance();
        this.inputParser = InputParser.getInstance();
    }

    public void createNewTemplate() {
        presenter.printText("Enter the type of the event:");
        String templateName = inputParser.readLine();

        //this checks for duplicate template name
        while (!templateManager.checkNameUniqueness(templateName)){
            presenter.printText("This template name is already taken. Please try a different name:");
            templateName = inputParser.readLine();
        }

        String newTemplateName = templateManager.createTemplate(templateName);

        presenter.printText("Would you like to add a field? (Y/N)");
        String response = inputParser.readLine();

        if (response.equals("N")) {
            presenter.printText("This will be an empty template. Are you sure you don't want to add a field? (Y/N)");
            String innerResponse = inputParser.readLine();
            if (innerResponse.equalsIgnoreCase("Y")) {
                presenter.printText("You have successfully created an empty template!");
            }
            else {
                while (innerResponse.equalsIgnoreCase("N")) {
                    innerResponse = addField(newTemplateName);
                }
            }
        }

        else {
            while (response.equalsIgnoreCase("Y")) {
                response = addField(newTemplateName);
            }
        }
    }

    // return the response of new field
    public String addField(String newTemplateName) {
        presenter.printText("Enter the field name:");
        String fieldName = inputParser.readLine();

        String dataType = typeMenu();

        presenter.printText("Do you need this field?:");
        String isRequired = inputParser.readLine();

        templateManager.addFieldSpecs(newTemplateName, templateManager.createNewFieldSpecs(fieldName,
                dataType, isRequired));

        presenter.printText("Would you like to add a field? (Y/N)");
        String response = inputParser.readLine();

        while (!response.equalsIgnoreCase("Y") || !response.equalsIgnoreCase("N")) {
            presenter.printText("Invalid input. Would you like to add a field? (Y/N)");
            response = inputParser.readLine();
        }
        return response.toUpperCase();
    }

    public String typeMenu(){
        Map<String, String> menuMap = new HashMap<>();
        menuMap.put("1", "string");
        menuMap.put("2", "boolean");
        menuMap.put("3", "int");
        menuMap.put("4", "localdatetime");

        presenter.printEntity(menuMap);

        presenter.printText("Enter the number that corresponds to the data type you want:");
        String response = inputParser.readLine();
        while (!menuMap.containsKey(response)){
            presenter.printText("This is not valid. Please enter again:");
            response = inputParser.readLine();
        }
        return menuMap.get(response);
    }

//    public void createNewTemplate() {
//        presenter.printText("Enter the type of the event:");
//        String templateName = inputParser.readLine();
//        String newTemplateName = templateManager.createTemplate(templateName);
//
////        1. user inputs fieldname, datatype, isRequired
////        2. we use setters in FieldSpecs to set these to a new FieldSpecs
////        3. Add these fieldSpecs with addFieldSpecs method
//
//
//        presenter.printText("Would you like to add a field? (Y/N)");
//        String response = inputParser.readLine();
//
//        if (response.equalsIgnoreCase("N")) {
//            presenter.printText("This will be an empty template. Are you sure? (Y/N)");
//            String innerResponse = inputParser.readLine();
//
//            if (!innerResponse.equalsIgnoreCase("N")) {
//                while (!innerResponse.equalsIgnoreCase("N")) {
//                    presenter.printText("Enter the field name:");
//                    String fieldName = inputParser.readLine();
//
//                    presenter.printText("Enter the field data type:");
//                    String dataType = inputParser.readLine();
//
//                    presenter.printText("Do you need this field?:");
//                    String isRequired = inputParser.readLine();
//
//                    templateManager.addFieldSpecs(newTemplateName, templateManager.createNewFieldSpecs(fieldName, dataType, isRequired));
//
//                    presenter.printText("Would you like to add a field? (Y/N)");
//                    innerResponse = inputParser.readLine();
//                }
//            }
//        }
//        while (!response.equalsIgnoreCase("N")) {
//            presenter.printText("Enter the field name:");
//            String fieldName = inputParser.readLine();
//
//            presenter.printText("Enter the field data type:");
//            String dataType = inputParser.readLine();
//
//            presenter.printText("Do you need this field?:");
//            String isRequired = inputParser.readLine();
//
//            templateManager.addFieldSpecs(newTemplateName, templateManager.createNewFieldSpecs(fieldName, dataType, isRequired));
//
//            presenter.printText("Would you like to add a field? (Y/N)");
//            response = inputParser.readLine();
//        }
//    }
}
