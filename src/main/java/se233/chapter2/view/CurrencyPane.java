package se233.chapter2.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import se233.chapter2.controller.AllEventHandlers;
import se233.chapter2.controller.draw.DrawGraphTask;
import se233.chapter2.model.Currency;

import java.util.concurrent.*;


public class CurrencyPane extends BorderPane {
    private  Button watch;
    private  Button unwatch;
    private Button delete;
    private Currency currency;

    //Number 2 are all below
    public CurrencyPane(Currency currency) {
        this.watch = new Button("Watch");
        this.unwatch = new Button("UnWatch");
        this.delete = new Button("Delete");
        this.delete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                AllEventHandlers.onDelete(currency.getShortCode());
            }
        });
        this.watch.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                AllEventHandlers.onWatch(currency.getShortCode());
            }
        });
        this.unwatch.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                AllEventHandlers.onUnwatch(currency.getShortCode());
            }
        });

        this.setPadding(new Insets(0));
        this.setPrefSize(640, 300);
        this.setStyle("-fx-border-color: black");

        try {
            this.refreshPane(currency);
        } catch (ExecutionException e) {
            System.out.println("Encountered an execution exception.");
        } catch (InterruptedException e) {
            System.out.println("Encountered an interrupted exception.");
        }
    }

    public void refreshPane(Currency currency) throws ExecutionException,
            InterruptedException {
        this.currency = currency;

        ExecutorService executor = Executors.newFixedThreadPool(3);

        Future<Pane> infoPaneFuture = executor.submit(new GenInfoPaneTask(currency));
        Future<HBox> topAreaFuture = executor.submit(new GenTopAreaTask(watch,unwatch,delete));
        Future<VBox> drawGraphTaskFuture = executor.submit(new DrawGraphTask(currency));

        //FutureTask futureTask = new FutureTask<VBox>(new DrawGraphTask(currency));
        //ExecutorService executor = Executors.newSingleThreadExecutor();
        //executor.execute(futureTask);

        try {
            // Get the results from the Callable tasks
            Pane currencyInfo = infoPaneFuture.get();
            HBox topArea = topAreaFuture.get();
            VBox currencyGraph = drawGraphTaskFuture.get();

            // Set the results in the CurrencyPane
            this.setTop(topArea);
            this.setLeft(currencyInfo);
            this.setCenter(currencyGraph);

            // Other code...
        } catch (Exception e) {
            e.printStackTrace();
        }

//        VBox currencyGraph = (VBox) futureTask.get();
//        Pane currencyInfo = genInfoPane();
//        Pane topArea = genTopArea();
    }

    private static class GenInfoPaneTask implements Callable<Pane> {
        private Currency currency;
        public GenInfoPaneTask(Currency currency) {
            this.currency = currency;
        }
        @Override
        public Pane call() {
            VBox currencyInfoPane = new VBox(10);
            currencyInfoPane.setPadding(new Insets(5, 25, 5, 25));
            currencyInfoPane.setAlignment(Pos.CENTER);
            Label exchangeString = new Label("");
            Label watchString = new Label("");
            exchangeString.setStyle("-fx-font-size: 20;");
            watchString.setStyle("-fx-font-size: 14;");
            if (this.currency != null) {
                exchangeString.setText(String.format("1 %s\n=\n %.4f "+currency.getBaseFrom(), this.currency.getShortCode(), this.currency.getCurrent().getRate()));
                if (this.currency.getWatch()) {
                    watchString.setText(String.format("(Watch @%.4f)", this.currency.getWatchRate()));
                }
            }
            currencyInfoPane.getChildren().addAll(exchangeString, watchString);
            return currencyInfoPane;
        }

    }

    public class GenTopAreaTask implements Callable<HBox> {
        private Button watch;
        private Button unwatch;
        private Button delete;

        public GenTopAreaTask(Button watch,Button unwatch, Button delete) {
            this.watch = watch;
            this.unwatch = unwatch;
            this.delete = delete;
        }
        public HBox call() {
            HBox topArea = new HBox(10);
            topArea.setPadding(new Insets(5));
            topArea.getChildren().addAll(watch,unwatch,delete);
            topArea.setAlignment(Pos.CENTER_RIGHT);
            return topArea;
        }
    }
}
