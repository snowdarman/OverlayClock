package com.example;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.time.LocalTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class ClockApp extends Application {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd（E）", Locale.JAPAN);
    private double xOffset = 0;
    private double yOffset = 0;
    @Override
    public void start(Stage stage) {
        Label clockLabel = new Label();
        clockLabel.setFont(Font.font("Consolas", 48));
        Label dateLabel = new Label();
        dateLabel.setFont(Font.font("Consolas", 24));
        
        // 背景画像を読み込む
        Image backgroundImage = new Image("file:backImage01.png"); // ローカルファイルを指定（または "your_image.png"）
        ImageView imageView = new ImageView(backgroundImage);
        imageView.setFitWidth(600);
        imageView.setFitHeight(150);
        imageView.setPreserveRatio(true);
        
        updateTime(clockLabel); // 初期表示
        updateDate(dateLabel); // 初期表示

        Timeline timeline = new Timeline(
            new KeyFrame(Duration.seconds(1), event -> updateTime(clockLabel))
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
        
        // 配置
        VBox vbox = new VBox(5, dateLabel, clockLabel);
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
        contextMenu.getItems().add(exitItem);
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

    public static void main(String[] args) {
        launch(args);
    }
}
