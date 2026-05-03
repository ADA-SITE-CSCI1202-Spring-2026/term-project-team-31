package gui;


import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Random;

import manager.InventoryManager;
import manager.OrderQueue;
import model.Ingredient;
import persistance.SaveLoadManager;
import processor.DrinkDispenser;
import processor.Fryer;
import processor.Grill;
import processor.IAppliance;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;


public class Dashboard extends BorderPane {

    // Main Engine 
    private final InventoryManager inventoryManager = new InventoryManager();
    private final OrderQueue orderQueue             = new OrderQueue();
    private final List<IAppliance> appliances       = List.of(
            new Grill(), new Fryer(), new DrinkDispenser()
    );
    private double cash = 100.0;

    // Random task generation
    private final Random random = new Random();
    private final List<String> menuTags = List.of("BURGER", "FRIED_CHICKEN", "BEVERAGE");
    private Timeline simulationClock;

    // UI Components 
    private final ObservableList<String> queueDisplay = FXCollections.observableArrayList();
    private final ListView<String> queueListView     = new ListView<>(queueDisplay);
    private final TextArea logArea                   = new TextArea();
    private final Label cashLabel                    = new Label();
    private final GridPane inventoryGrid             = new GridPane();
    private final ComboBox<Ingredient> restockDropdown = new ComboBox<>();

    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm:ss");

    //  Constructor 
    public Dashboard() {
        buildUI();
        refreshInventoryDisplay();
        refreshQueueDisplay();
        startSimulationClock();
        log("🍔 Welcome to The Silicon Spatula! Simulation started.");
    }

    // UI Construction 

    private void buildUI() {
        setStyle("-fx-background-color: #1a1a2e;");
        setPadding(new Insets(12));

        // Title bar
        Label title = new Label("🥄 Silicon Spatula — Restaurant Dashboard");
        title.setFont(Font.font("System", FontWeight.BOLD, 18));
        title.setTextFill(Color.web("#e2b96f"));
        title.setPadding(new Insets(0, 0, 8, 0));
        setTop(title);

        // Four panel layout
        HBox topRow    = new HBox(10, buildQueuePanel(), buildInventoryPanel());
        HBox bottomRow = new HBox(10, buildRestockPanel(), buildLogPanel());
        HBox.setHgrow(topRow.getChildren().get(0), Priority.ALWAYS);
        HBox.setHgrow(topRow.getChildren().get(1), Priority.ALWAYS);
        HBox.setHgrow(bottomRow.getChildren().get(0), Priority.SOMETIMES);
        HBox.setHgrow(bottomRow.getChildren().get(1), Priority.ALWAYS);

        VBox center = new VBox(10, topRow, bottomRow);
        VBox.setVgrow(topRow, Priority.ALWAYS);
        VBox.setVgrow(bottomRow, Priority.ALWAYS);
        setCenter(center);
    }

    // Panel 1 — Order Queue 
    private VBox buildQueuePanel() {
        queueListView.setPrefHeight(200);
        queueListView.setStyle("-fx-background-color: #16213e; -fx-text-fill: white;");
        queueListView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item);
                setStyle(empty ? "" : "-fx-text-fill:rgb(185, 64, 64); -fx-background-color: #16213e;");
            }
        });

        Button cookBtn = styledButton("🍳 Cook Next Order", "#e2b96f", "#1a1a2e");
        cookBtn.setOnAction(e -> cookNextOrder());

        VBox panel = createPanel("📋 Order Queue", queueListView, cookBtn);
        VBox.setVgrow(queueListView, Priority.ALWAYS);
        return panel;
    }

    // Panel 2 — Inventory / Resource State 
    private VBox buildInventoryPanel() {
        cashLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        cashLabel.setTextFill(Color.web("#7dff8a"));

        inventoryGrid.setHgap(12);
        inventoryGrid.setVgap(6);
        inventoryGrid.setPadding(new Insets(6));

        ScrollPane scroll = new ScrollPane(inventoryGrid);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: #0f3460; -fx-background: #0f3460;");

        VBox panel = createPanel("📦 Inventory", cashLabel, scroll);
        VBox.setVgrow(scroll, Priority.ALWAYS);
        return panel;
    }

    // Panel 3 — Restock / Supply Chain */
    private VBox buildRestockPanel() {
        restockDropdown.setItems(FXCollections.observableArrayList(inventoryManager.getAllIngredients()));
        restockDropdown.setPromptText("Select ingredient...");
        restockDropdown.setMaxWidth(Double.MAX_VALUE);
        restockDropdown.setStyle("-fx-background-color: #0f3460; -fx-text-fill: white;");

        Label costLabel = new Label("Cost: $" + InventoryManager.RESTOCK_COST
                + " | Adds: " + InventoryManager.RESTOCK_AMOUNT + " units");
        costLabel.setTextFill(Color.web("#aaaaaa"));
        costLabel.setFont(Font.font(11));

        Button buyBtn = styledButton("🛒 Buy Ingredient", "#5cbfff", "#0d1b2a");
        buyBtn.setOnAction(e -> restockIngredient());

        Button saveBtn = styledButton("💾 Save Game", "#a8ff78", "#0d1b2a");
        saveBtn.setOnAction(e -> log(SaveLoadManager.save(cash, inventoryManager, orderQueue)));

        Button loadBtn = styledButton("📂 Load Game", "#ff8c78", "#0d1b2a");
        loadBtn.setOnAction(e -> loadGame());

        return createPanel("🏪 Restock Supply Chain", restockDropdown, costLabel, buyBtn,
                new Separator(), saveBtn, loadBtn);
    }

    // Panel 4 — System Log 
    private VBox buildLogPanel() {
        logArea.setEditable(false);
        logArea.setWrapText(true);
        logArea.setStyle("-fx-background-color: #0d1b2a; -fx-text-fill: #d4f1b0; "
                + "-fx-font-family: 'Monospaced'; -fx-font-size: 12;");
        VBox.setVgrow(logArea, Priority.ALWAYS);

        Button clearBtn = styledButton("🗑 Clear Log", "#888888", "#1a1a2e");
        clearBtn.setOnAction(e -> logArea.clear());

        VBox panel = createPanel("📟 System Log", logArea, clearBtn);
        VBox.setVgrow(logArea, Priority.ALWAYS);
        return panel;
    }

    // Simulation Engine

    // Starts the internal clock which generates a new random order every 3 seconds
    private void startSimulationClock() {
        simulationClock = new Timeline(
                new KeyFrame(Duration.seconds(3), e -> generateRandomOrder())
        );
        simulationClock.setCycleCount(Timeline.INDEFINITE);
        simulationClock.play();
    }

    //  Creates random order to the queue
    private void generateRandomOrder() {
        String tag  = menuTags.get(random.nextInt(menuTags.size()));
        model.MenuItem item = SaveLoadManager.createFromTag(tag);
        if (item == null) return;

        orderQueue.enqueue(item);
        refreshQueueDisplay();
        log("🆕 New order: " + item.getName() + " (#" + orderQueue.size() + " in queue)");
    }

    // Processes the front order in the queue. Called by the Cook button
    private void cookNextOrder() {
        if (orderQueue.isEmpty()) {
            log("Queue is empty nothing to cook!");
            return;
        }

        model.MenuItem item = orderQueue.dequeue();
        refreshQueueDisplay();

        // 1. Resource check before processing
        if (!inventoryManager.hasSufficientIngredients(item)) {
            Ingredient missing = inventoryManager.findMissingIngredient(item);
            log("ERROR: Cannot cook " + item.getName()
                    + " — Insufficient " + missing + "! Order rejected.");
            return;
        }

        // 2. Find the right appliance via canProcess()
        IAppliance selected = null;
        for (IAppliance appliance : appliances) {
            if (appliance.canProcess(item)) {
                selected = appliance;
                break;
            }
        }

        if (selected == null) {
            log("❌ ERROR: No appliance available to cook " + item.getName() + "!");
            return;
        }

        // 3. Consume resources
        inventoryManager.consumeIngredients(item);

        // 4. Process and earn revenue
        cash += item.getRevenue();
        String processorLog = selected.processTask(item);

        refreshInventoryDisplay();
        log("✅ " + processorLog);
        log("   💰 Earned $" + String.format("%.2f", item.getRevenue())
                + " | Cash: $" + String.format("%.2f", cash));
    }

    // Restocks the selected ingredient if the player has enough cash. */
    private void restockIngredient() {
        Ingredient selected = restockDropdown.getValue();
        if (selected == null) {
            log("⚠  Please select an ingredient to restock.");
            return;
        }
        if (cash < InventoryManager.RESTOCK_COST) {
            log("❌ ERROR: Not enough cash to restock! Need $" + InventoryManager.RESTOCK_COST);
            return;
        }
        cash -= InventoryManager.RESTOCK_COST;
        inventoryManager.restock(selected);
        refreshInventoryDisplay();
        log("🛒 Restocked " + InventoryManager.RESTOCK_AMOUNT + "x " + selected
                + " for $" + InventoryManager.RESTOCK_COST
                + " | Cash: $" + String.format("%.2f", cash));
    }

    // Loads a saved game state. 
    private void loadGame() {
        SaveLoadManager.LoadResult result = SaveLoadManager.load();
        if (!result.success) {
            log(result.message);
            return;
        }
        // Restore state
        cash = result.money;
        if (result.inventoryData != null) {
            inventoryManager.deserialize(result.inventoryData);
        }
        orderQueue.clear();
        if (result.queue != null) {
            for (model.MenuItem item : result.queue) {
                orderQueue.enqueue(item);
            }
        }
        refreshInventoryDisplay();
        refreshQueueDisplay();
        log("📂 " + result.message);
    }

    // ─── UI Refresh ───────────────────────────────────────────────────────────

    /** Refreshes inventory panel — called after every action that changes state. */
    private void refreshInventoryDisplay() {
        cashLabel.setText("💵 Cash: $" + String.format("%.2f", cash));

        inventoryGrid.getChildren().clear();
        inventoryGrid.getColumnConstraints().clear();

        ColumnConstraints col1 = new ColumnConstraints(130);
        ColumnConstraints col2 = new ColumnConstraints(60);
        inventoryGrid.getColumnConstraints().addAll(col1, col2);

        int row = 0;
        for (Map.Entry<Ingredient, Integer> entry : inventoryManager.getSnapshot().entrySet()) {
            Label nameLabel = new Label(formatIngredient(entry.getKey()));
            nameLabel.setTextFill(Color.web("#c8d6e5"));

            int qty = entry.getValue();
            Label qtyLabel = new Label(String.valueOf(qty));
            qtyLabel.setFont(Font.font("System", FontWeight.BOLD, 13));
            qtyLabel.setTextFill(qty <= 2 ? Color.web("#ff6b6b") : Color.web("#7dff8a"));
            qtyLabel.setAlignment(Pos.CENTER_RIGHT);

            inventoryGrid.add(nameLabel, 0, row);
            inventoryGrid.add(qtyLabel,  1, row);
            row++;
        }
    }

    /** Refreshes the order queue list view. */
    private void refreshQueueDisplay() {
        queueDisplay.clear();
        List<model.MenuItem> items = orderQueue.peekAll();
        for (int i = 0; i < items.size(); i++) {
            queueDisplay.add((i + 1) + ". " + items.get(i).getName());
        }
        if (queueDisplay.isEmpty()) {
            queueDisplay.add("— No pending orders —");
        }
    }

    /** Appends a timestamped message to the System Log. */
    private void log(String message) {
        String line = "[" + LocalTime.now().format(TIME_FMT) + "] " + message + "\n";
        Platform.runLater(() -> {
            logArea.appendText(line);
            logArea.setScrollTop(Double.MAX_VALUE);
        });
    }

    // ─── UI Helpers ───────────────────────────────────────────────────────────

    private VBox createPanel(String title, javafx.scene.Node... children) {
        Label header = new Label(title);
        header.setFont(Font.font("System", FontWeight.BOLD, 13));
        header.setTextFill(Color.web("#e2b96f"));
        header.setPadding(new Insets(0, 0, 4, 0));

        VBox box = new VBox(6, header);
        box.getChildren().addAll(children);
        box.setPadding(new Insets(10));
        box.setStyle("-fx-background-color: #16213e; -fx-background-radius: 8;");
        box.setPrefWidth(340);
        VBox.setVgrow(box, Priority.ALWAYS);
        return box;
    }

    private Button styledButton(String text, String bg, String fg) {
        Button btn = new Button(text);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setStyle("-fx-background-color: " + bg + "; -fx-text-fill: " + fg
                + "; -fx-font-weight: bold; -fx-background-radius: 5;");
        btn.setOnMouseEntered(e -> btn.setOpacity(0.85));
        btn.setOnMouseExited(e  -> btn.setOpacity(1.0));
        return btn;
    }

    private String formatIngredient(Ingredient ing) {
        return ing.name().charAt(0) + ing.name().substring(1).toLowerCase().replace("_", " ");
    }

    public void stopClock() {
        if (simulationClock != null) simulationClock.stop();
    }
}
