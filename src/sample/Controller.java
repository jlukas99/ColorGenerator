package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Controller {

    @FXML
    ProgressBar progressBar;
    @FXML
    TextField hexColorField;
    @FXML
    TextField nameColorField;
    @FXML
    TextField sizeHeightField;
    @FXML
    TextField sizeWidthField;
    @FXML
    Button generateButton;
    @FXML
    Button colorFileButton;
    @FXML
    Button saveLocationButton;
    @FXML
    Label colorFileText;
    @FXML
    Label saveLocationText;
    @FXML
    Label progressText;
    @FXML
    Rectangle hexColorShow;

    private String width;
    private String height;
    private String currentPath;
    private String filePath;
    private String name;

    private int r;
    private int g;
    private int b;

    @FXML
    public void generateImage(){

        width = sizeWidthField.getText();
        height = sizeHeightField.getText();

        if(filePath == null){
            if(currentPath != null){
                name = nameColorField.getText();

                if(!name.isEmpty() && r != 0 && g != 0 && b != 0) {
                    generate();
                } else {
                    progressBar.setStyle("-fx-background-color: #ff5042");
                    progressText.setText("Error: Check color name or hex");
                }
            } else {
                progressBar.setStyle("-fx-background-color: #ff5042");
                progressText.setText("Error: The storage location has not been selected");
            }
        } else {

            generateButton.setDisable(true);

            readFile();
        }
    }

    private void generate() {
        if (isNumeric(width) && isNumeric(height)) {

            int widthInt = Integer.parseInt(width);
            int heightInt = Integer.parseInt(height);

            BufferedImage img = null;

            try {
                img = new BufferedImage(widthInt, heightInt, BufferedImage.TYPE_INT_RGB);
            } catch (RuntimeException e){
                progressBar.setStyle("-fx-background-color: #ff5042");
                progressText.setText("Error: Too big size");
            }

            File f;

            java.awt.Color c = new java.awt.Color(r, g, b); // Color white
            int rgb = c.getRGB();

            for (int i = 0; i < widthInt; i++) {
                for (int j = 0; j < heightInt; j++) {
                    assert img != null;
                    img.setRGB(i, j, rgb);
                }
            }

            try {
                f = new File(currentPath + "\\" + name + ".png");
                System.out.print(currentPath);
                assert img != null;
                ImageIO.write(img, "png", f);
            } catch (IOException e) {
                progressBar.setStyle("-fx-background-color: #ff5042");
                progressText.setText("Error: Select the Save path");
            }

            progressBar.setStyle("-fx-background-color: #6bd500");
            progressText.setText("Image generation completed!");
        } else {
            progressBar.setStyle("-fx-background-color: #FF5042");
            progressText.setText("Error: Check size value");
        }
    }

    @FXML
    public void colorShow(){

        String colorStr;

        switch(hexColorField.getLength()) {
            case 1:

                colorStr = hexColorField.getText() + "FFFFFF";

                color(colorStr);
                break;
            case 2:

                colorStr = hexColorField.getText() + "FFFFF";

                color(colorStr);
                break;
            case 3:

                colorStr = hexColorField.getText() + "FFFF";

                color(colorStr);
                break;
            case 4:

                colorStr = hexColorField.getText() + "FFF";

                color(colorStr);
                break;
            case 5:

                colorStr = hexColorField.getText() + "FF";

                color(colorStr);
                break;
            case 6:

                colorStr = hexColorField.getText() + "F";

                color(colorStr);
                break;
        }
    }

    private void color(String colorStr) {
        try {
            r = Integer.valueOf(colorStr.substring(1, 3), 16);
            g = Integer.valueOf(colorStr.substring(3, 5), 16);
            b = Integer.valueOf(colorStr.substring(5, 7), 16);

            hexColorShow.setFill(Color.rgb(r, g, b));
        } catch (NumberFormatException e) {
            progressBar.setStyle("-fx-background-color: #ff5042");
            progressText.setText("Error: enter the correct color format (#ffffff)");
            System.out.print("Error: " + e);
        }
    }

    private boolean isNumeric(String str)
    {
        try
        {
            int i = Integer.parseInt(str);
            System.out.print(i);
        }
        catch(NumberFormatException nfe)
        {
            return false;
        }

        progressBar.setStyle("-fx-background-color: #bf6400");
        progressText.setText("Generate Image Color, pleas wait...");
        return true;
    }

    public void outputFolder(){
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(new Stage());

        if(selectedDirectory == null){
            progressBar.setStyle("-fx-background-color: #ff5042");
            progressText.setText("Error: The storage location has not been selected");
        }else{
            System.out.println(selectedDirectory.getAbsolutePath());
            currentPath = selectedDirectory.getAbsolutePath();
            saveLocationText.setText(currentPath);
        }
    }

    public void colorFile(){
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if(selectedFile == null){
            colorFileText.setText("");
            hexColorField.setDisable(false);
            nameColorField.setDisable(false);
        }else{
            hexColorField.setDisable(true);
            nameColorField.setDisable(true);
            filePath = selectedFile.getAbsolutePath();
            colorFileText.setText(filePath);
        }
    }

    private void readFile(){

        StringBuilder contentBuilder = new StringBuilder();

        try {
            BufferedReader in = new BufferedReader(new FileReader(filePath));

            String str;

            progressBar.setStyle("-fx-background-color: #bf6400");
            progressText.setText("Generate Image Color, pleas wait...");

            int i = 0;

            while ((str = in.readLine()) != null) {

                contentBuilder.append(str).append("\n");

                try {
                    color(str.substring(0,7));
                    if(!str.substring(7).isEmpty()){
                        name = str.substring(8);
                    } else {
                        i++;
                        name = "color_" + i;
                    }
                } catch (StringIndexOutOfBoundsException e){
                    progressBar.setStyle("-fx-background-color: #ff5042");
                    progressText.setText("Error: " + e);
                    generateButton.setDisable(false);
                }

                generate();

            }
            in.close();
            generateButton.setDisable(false);
        } catch (IOException e) {
            progressBar.setStyle("-fx-background-color: #ff5042");
            progressText.setText("Error: " + e);
            generateButton.setDisable(false);
        }

        String content = contentBuilder.toString();
        System.out.print(content + "\n");
    }
}
