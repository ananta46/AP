package se233.chapter2.controller;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import org.json.JSONException;
import se233.chapter2.Launcher;
import se233.chapter2.model.Currency;
import se233.chapter2.model.CurrencyEntity;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public class AllEventHandlers {
    public static void onRefresh() {
        try {
            Launcher.refreshPane();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void onAdd() {
        try {
            //Number 6 : redesign custom dialog to be able to choose baseCurrency of the currency object
            //Pls Check Currency Object in se233.chapter2.model for further update
            //Also check the FetchData.fetch_range for further update

            // Create the custom dialog.
            Dialog<Pair<String, String>> dialog = new Dialog<>();
            dialog.setTitle("Add Currency");
            dialog.setHeaderText(null);
            dialog.setGraphic(null);

            // Set the button types.
            ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(okButtonType);

            // Create the username and password labels and fields.
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));

            // Create the labels and fields.
            TextField baseCurrency = new TextField();
            baseCurrency.setPromptText("Base Currency");
            TextField targetCurrency = new TextField();
            targetCurrency.setPromptText("Target Currency");

            grid.add(new Label("Base Currency:"), 0, 0);
            grid.add(baseCurrency, 1, 0);
            grid.add(new Label("Target Currency:"), 0, 1);
            grid.add(targetCurrency, 1, 1);

            // Set the content of the dialog to the grid
            dialog.getDialogPane().setContent(grid);

            //Button Event
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == okButtonType) {
                    String base = baseCurrency.getText();
                    String target = targetCurrency.getText();
                    return new Pair<>(base, target);
                }
                return null;
            });

            // Show the dialog and wait for the user to close it.
            Optional<Pair<String, String>> result = dialog.showAndWait();
            result.ifPresent(pair -> {
                String base = pair.getKey().toUpperCase();             //Number 4: Add .toUppercase
                String target = pair.getValue().toUpperCase();         //Number 4: Add .toUppercase

                ArrayList<Currency> currency_list = Launcher.getCurrencyList();
                Currency c = new Currency(base,target);
                ArrayList<CurrencyEntity> c_list = FetchData.fetch_range(base,c.getShortCode(), 30); //Number 1
                c.setHistorical(c_list);
                c.setCurrent(c_list.get(c_list.size() - 1));
                currency_list.add(c);
                Launcher.setCurrencyList(currency_list);
            });
            Launcher.refreshPane();

//Old dialog

//            TextInputDialog dialog = new TextInputDialog();
//            dialog.setTitle("Add Currency");
//            dialog.setContentText("Currency code:");
//            dialog.setHeaderText(null);
//            dialog.setGraphic(null);
//            Optional<String> code = dialog.showAndWait();

//Old onClickOk event

//            if (code.isPresent()) {
//                ArrayList<Currency> currency_list = Launcher.getCurrencyList();
//                Currency c = new Currency(code.get().toUpperCase(Locale.ROOT));
//                ArrayList<CurrencyEntity> c_list = FetchData.fetch_range(c.getShortCode(), 30);
//                c.setHistorical(c_list);
//                c.setCurrent(c_list.get(c_list.size() - 1));
//                currency_list.add(c);
//                Launcher.setCurrencyList(currency_list);
//                Launcher.refreshPane();
//            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e){                //Number 5: Use this to catch the incorrect output and redirect it to the onAddAgain
            System.out.println("Incorrect Input");
            onAddAgain();
        }
    }
    public static void onAddAgain() { //Read Number5 above
        try {
            // Create the custom dialog.
            Dialog<Pair<String, String>> dialog = new Dialog<>();
            dialog.setTitle("Not Found Add Again");
            dialog.setHeaderText(null);
            dialog.setGraphic(null);

            // Set the button types.
            ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(okButtonType);

            // Create the username and password labels and fields.
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));

            // Create the labels and fields.
            TextField baseCurrency = new TextField();
            baseCurrency.setPromptText("Base Currency");
            TextField targetCurrency = new TextField();
            targetCurrency.setPromptText("Target Currency");

            grid.add(new Label("Base Currency:"), 0, 0);
            grid.add(baseCurrency, 1, 0);
            grid.add(new Label("Target Currency:"), 0, 1);
            grid.add(targetCurrency, 1, 1);

            // Set the content of the dialog to the grid
            dialog.getDialogPane().setContent(grid);

            //Button Event
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == okButtonType) {
                    String base = baseCurrency.getText();
                    String target = targetCurrency.getText();
                    return new Pair<>(base, target);
                }
                return null;
            });

            // Show the dialog and wait for the user to close it.
            Optional<Pair<String, String>> result = dialog.showAndWait();
            result.ifPresent(pair -> {
                String base = pair.getKey().toUpperCase();
                String target = pair.getValue().toUpperCase();

                ArrayList<Currency> currency_list = Launcher.getCurrencyList();
                Currency c = new Currency(base,target);
                ArrayList<CurrencyEntity> c_list = FetchData.fetch_range(base,c.getShortCode(), 30); //Number 1
                c.setHistorical(c_list);
                c.setCurrent(c_list.get(c_list.size() - 1));
                currency_list.add(c);
                Launcher.setCurrencyList(currency_list);
            });
            Launcher.refreshPane();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e){
            System.out.println("Incorrect Input");
            onAddAgain();
        }
    }

    public static void onDelete(String code) {
        try {
            ArrayList<Currency> currency_list = Launcher.getCurrencyList();
            int index = -1;
            for (int i = 0; i < currency_list.size(); i++) {
                if (currency_list.get(i).getShortCode().equals(code)) {
                    index = i;
                    break;
                }
            }
            if (index != -1) {
                currency_list.remove(index);
                Launcher.setCurrencyList(currency_list);
                Launcher.refreshPane();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public static void onWatch(String code) {
        try {
            ArrayList<Currency> currency_list = Launcher.getCurrencyList();
            int index = -1;
            for (int i = 0; i < currency_list.size(); i++) {
                if (currency_list.get(i).getShortCode().equals(code)) {
                    index = i;
                    break;
                }
            }
            if (index != -1) {
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("Add Watch");
                dialog.setContentText("Rate:");
                dialog.setHeaderText(null);
                dialog.setGraphic(null);
                Optional<String> retrievedRate = dialog.showAndWait();
                if (retrievedRate.isPresent()) {
                    double rate = Double.parseDouble(retrievedRate.get());
                    currency_list.get(index).setWatch(true);
                    currency_list.get(index).setWatchRate(rate);
                    Launcher.setCurrencyList(currency_list);
                    Launcher.refreshPane();
                }
                Launcher.setCurrencyList(currency_list);
                Launcher.refreshPane();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
    public static void onUnwatch(String code) { //Number 3
        try {
            ArrayList<Currency> currency_list = Launcher.getCurrencyList();
            int index = -1;
            for (int i = 0; i < currency_list.size(); i++) {
                if (currency_list.get(i).getShortCode().equals(code)) {
                    index = i;
                    break;
                }
            }
            if (index != -1) {
                currency_list.get(index).setWatch(false);
                currency_list.get(index).setWatchRate(null);
                Launcher.setCurrencyList(currency_list);
                Launcher.refreshPane();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

}