package com.example;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.time.LocalTime;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

import javafx.stage.FileChooser;
import javafx.stage.Modality;

public class ClockApp extends Application {

	public static AlarmEntry alarmSelect = null;
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd（E）", Locale.JAPAN);
    private double xOffset = 0;
    private double yOffset = 0;
    

    @Override
    public void start(Stage stage) {
        Label clockLabel = new Label();
        clockLabel.setFont(Font.font("Consolas, DejaVu Sans Mono, Monospaced", 48));
        Label dateLabel = new Label();
        dateLabel.setFont(Font.font("Consolas, DejaVu Sans Mono, Monospaced", 24));
        Label commentLabel = new Label();
        commentLabel.setFont(Font.font("Consolas, DejaVu Sans Mono, Monospaced", 24));
        commentLabel.setTextFill(Color.RED);  // ← ここで文字色を赤に

        
        // 背景画像を読み込む
        Image backgroundImage = new Image("file:backImage01.png"); // ローカルファイルを指定（または "your_image.png"）
        ImageView imageView = new ImageView(backgroundImage);
        imageView.setFitWidth(600);
        imageView.setFitHeight(150);
        imageView.setPreserveRatio(true);
        
        updateTime(clockLabel); // 初期表示
        updateDate(dateLabel); // 初期表示
        commentLabel.setText("Symple Clock");

        Timeline timeline = new Timeline(
            new KeyFrame(Duration.seconds(1), event -> {
            	updateTime(clockLabel);
            	updateComment(commentLabel);
            })
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
        
        // 配置
        VBox vbox = new VBox(5, dateLabel, clockLabel, commentLabel);
        vbox.setStyle("-fx-padding: 10px;");
        vbox.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        vbox.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });
        
        StackPane root = new StackPane(imageView, vbox);
        Scene scene = new Scene(root);

        // 🌟 Context Menu（右クリック）を作成
        ContextMenu contextMenu = new ContextMenu();
        MenuItem exitItem = new MenuItem("終了");
        exitItem.setOnAction(e -> Platform.exit());
        MenuItem clearAlarmItem  = new MenuItem("アラーム表示の消去");
        clearAlarmItem .setOnAction(e -> {
            ClockApp.alarmSelect = null;
        });
        MenuItem settingsItem = new MenuItem("設定");
        settingsItem.setOnAction(e -> SetComment(stage));
        contextMenu.getItems().addAll(settingsItem, clearAlarmItem, exitItem);
        // ラベルに右クリックイベント追加
        vbox.setOnContextMenuRequested(event ->
            contextMenu.show(vbox, event.getScreenX(), event.getScreenY())
        );
        
        stage.setScene(scene);
        stage.setTitle("JavaFX Clock");
        stage.setAlwaysOnTop(true); // 常に手前に表示
        stage.setResizable(false);
        stage.initStyle(StageStyle.UTILITY); // 小さくて目立たないウィンドウ枠
        stage.initStyle(StageStyle.UNDECORATED); // タイトルバーなし
        stage.show();
    }

    private void updateTime(Label label) {
        label.setText(LocalTime.now().format(TIME_FORMATTER));
    }
    private void updateDate(Label label) {
        label.setText(LocalDate.now().format(DATE_FORMATTER));
    }
    private void updateComment(Label label) {
    	LocalTime now = LocalTime.now();

        // アラームリストをスキャンして、3分後のアラームを探す
        for (AlarmEntry entry : AlarmManager.alarmList) {
            try {
                LocalTime alarmTime = LocalTime.parse(entry.getTime(), DateTimeFormatter.ofPattern("HH:mm"));

                if (alarmTime.minusMinutes(3).equals(now.truncatedTo(ChronoUnit.MINUTES))) {
                    // 3分後に鳴るアラームが見つかったら選択
                    ClockApp.alarmSelect = entry;
                    break;
                }
            } catch (DateTimeParseException e) {
                System.err.println("アラーム時刻の解析に失敗しました: " + entry.getTime());
            }
        }
    	if( ClockApp.alarmSelect != null ) {
    		label.setText(alarmSelect.getTime() + " " + alarmSelect.getComment());
    	}
    	else {
    		label.setText("--:--");
    	}
    }
    private void SetComment(Stage owner) {
        // 設定画面の表示
        Stage settingsStage = new Stage();
        settingsStage.initModality(Modality.APPLICATION_MODAL);
        settingsStage.initOwner(owner);
        settingsStage.setTitle("設定");

        // テーブルビューの作成
        TableView<AlarmEntry> tableView = new TableView<>(AlarmManager.alarmList);
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                ClockApp.alarmSelect = newSel;
                System.out.println("選択されたアラーム: " + newSel.getTime() + " | " + newSel.getComment());
            }
        });
        TableColumn<AlarmEntry, String> timeCol = new TableColumn<>("時刻");
        timeCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getTime()));
        timeCol.setPrefWidth(80);

        TableColumn<AlarmEntry, String> commentCol = new TableColumn<>("コメント");
        commentCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getComment()));
        commentCol.setPrefWidth(200);

        tableView.getColumns().addAll(timeCol, commentCol);
        tableView.setPrefHeight(200);

        // ファイル読込ボタン
        Button loadButton = new Button("参照");
        loadButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("アラームファイルを選択");
            File file = fileChooser.showOpenDialog(settingsStage);
            if (file != null) {
                loadAlarmsFromFile(file);
            }
        });

        // --- 追加部分：入力フィールドと追加ボタン ---
        TextField timeField = new TextField();
        timeField.setPromptText("hh:mm");
        timeField.setPrefWidth(80);

        TextField commentField = new TextField();
        commentField.setPromptText("コメント");
        commentField.setPrefWidth(180);

        Button addButton = new Button("追加");
        addButton.setOnAction(e -> {
            String time = timeField.getText().trim();
            String comment = commentField.getText().trim();
            if (time.matches("^\\d{2}:\\d{2}$") && !comment.isEmpty()) {
                AlarmManager.alarmList.add(new AlarmEntry(time, comment));
                timeField.clear();
                commentField.clear();
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "時刻（hh:mm）とコメントを正しく入力してください", ButtonType.OK);
                alert.showAndWait();
            }
        });

        HBox inputBox = new HBox(10, timeField, commentField, addButton);
        inputBox.setAlignment(Pos.CENTER_LEFT);

        VBox layout = new VBox(10, new Label("アラームリスト"), tableView, inputBox, loadButton);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER_LEFT);

        Scene scene = new Scene(layout, 420, 360);
        settingsStage.setScene(scene);
        settingsStage.showAndWait();
    	
    }
    private void loadAlarmsFromFile(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            AlarmManager.alarmList.clear();
            String line;
            while ((line = reader.readLine()) != null) {
                // 全角・半角の | に対応
                String[] parts = line.split("[|｜]");
                if (parts.length >= 2) {
                    String time = parts[0].trim();
                    String comment = parts[1].trim();
                    AlarmManager.alarmList.add(new AlarmEntry(time, comment));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "ファイルの読み込みに失敗しました。", ButtonType.OK);
            alert.showAndWait();
        }
    }
    public static void main(String[] args) {
        launch(args);
    }
}
