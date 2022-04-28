import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class App extends Application {
    public static void main(String[] args) throws Exception {
        launch(args);
    }

    File soju; // ไว้เก็บข้อมูลว่าแก้/เปิดไฟล์ไหนอยู่
    FileChooser bartender = new FileChooser(); // หน้าต่างเลือกไฟล์
    TextArea cock = new TextArea(); // พทเขียนอันใหญ่ๆ
    Stage stage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        VBox box = new VBox();
        MenuBar bar = new MenuBar();
        Menu filemenu = new Menu("File");
        MenuItem newitem = new MenuItem("New");
        MenuItem open = new MenuItem("Open");
        MenuItem save = new MenuItem("Save");
        MenuItem saveas = new MenuItem("Save As");
        SeparatorMenuItem sep = new SeparatorMenuItem();
        MenuItem exit = new MenuItem("Exit");

        VBox.setVgrow(cock, Priority.ALWAYS); // set textareaขยายตามจอที่ปรับ
        cock.setPrefSize(800, 400); // ขนาดtext areaที่ต้องการ

        filemenu.getItems().addAll(newitem, open, save, saveas, sep, exit);
        bar.getMenus().add(filemenu);
        box.getChildren().addAll(bar, cock);

        // กด new แล้วทำอะไร
        newitem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                soju = null;
                primaryStage.setTitle("newText.txt - OH");
                cock.clear();
            }
        });

        open.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                soju = bartender.showOpenDialog(primaryStage);
                if (soju == null) {
                    return;
                }
                if (!soju.getName().endsWith(".txt")) { // เช็คว่าเป็นไฟล์textหรือไม่ถ้าไม่จะขึ้นเตือน
                    Alert alert = new Alert(AlertType.WARNING);
                    alert.setTitle("Open");
                    alert.setHeaderText("File Cannot Open");
                    alert.setContentText("choose file.txt");
                    alert.show();
                    return;
                }
                primaryStage.setTitle(soju.getName() + " - OH");
                try { // อ่านทั้งไฟล์
                    String text = new String(Files.readAllBytes(soju.toPath()), StandardCharsets.UTF_8);
                    cock.setText(text);

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        save.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (soju == null) {
                    saveSojuAs();
                } else {
                    saveSoju();
                }

            }
        });

        saveas.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                saveSojuAs(); // เรียกใช้
            }
        });

        exit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Platform.exit();
            }
        });

        Scene scene = new Scene(box);
        primaryStage.setTitle("newText.txt - OH");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    void saveSoju() {
        if (soju.canWrite() || !soju.exists()) {
            try (PrintWriter output = new PrintWriter(soju)) {
                output.print(cock.getText());
            } catch (Exception e) {
                System.out.println(e);
            }

        } else {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Save");
            alert.setHeaderText("File Cannot Edit");
            alert.setContentText("this file read only");
            alert.showAndWait();
            saveSojuAs();
        }

    }

    void saveSojuAs() {
        if (soju == null) {
            bartender.setInitialFileName("newText.txt");
        } else {
            bartender.setInitialFileName(soju.getName());
        }
        File beer = bartender.showSaveDialog(null); // โชว์ไฟล์ออกมา
        if (beer == null) {
            return;
        }
        soju = beer;
        stage.setTitle(soju.getName() + " - OH");

        saveSoju();

    }
}
