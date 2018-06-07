package sample;



import IM.DCT.DCT;
import IM.Memento.CareTaker;
import IM.Memento.Originator;
import IM.Process.Effects.Grayscale;
import IM.Process.Effects.Negative;
import IM.Utils;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.NumberStringConverter;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;


public class Controller implements Initializable {

    // UI
    //--------------------------------------------
    @FXML
    private Label statusLabel;
    @FXML
    private ImageView imageViewSpace;
    @FXML
    private ImageView imageViewFrequency;
    @FXML
    private TextField nValue;
    //--------------------------------------------

    private int imageWidth = -1;
    private int imageHeight = -1;

    // Memento
    //--------------------------------------------
    private final CareTaker careTaker = new CareTaker();
    private final Originator originator = new Originator();
    //--------------------------------------------

    private ToggleGroup group;

    private final KeyCombination keyCombinationCtrlO = new KeyCodeCombination(
            KeyCode.O, KeyCombination.CONTROL_DOWN);

    private final KeyCombination keyCombinationCtrlS = new KeyCodeCombination(
            KeyCode.S, KeyCombination.CONTROL_DOWN);

    private final KeyCombination keyCombinationCtrlZ = new KeyCodeCombination(
            KeyCode.Z, KeyCombination.CONTROL_DOWN);

    private BufferedImage currentImage = null;

    UnaryOperator<TextFormatter.Change> integerFilter = change -> {
        String newText = change.getControlNewText();
        if (newText.matches("-?([0-9][0-9]*)?")) {
            return change;
        }
        return null;
    };

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        imageView.fitWidthProperty().bind(hBox.widthProperty());
//        imageView.fitHeightProperty().bind(hBox.heightProperty());
        nValue.setTextFormatter(new TextFormatter<Integer>(new IntegerStringConverter(), 0, integerFilter));
    }

    @FXML
    public void openButton(ActionEvent event) {
        FileChooser fileChooser = Utils.createFileChooser("Open File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JPEG files (*.jpg)", "*.jpg"));
        File file = fileChooser.showOpenDialog(imageViewSpace.getScene().getWindow());

        if (file != null) {
            this.statusLabel.setText("Opened: " + file.toURI().toString());

            currentImage = SwingFXUtils.fromFXImage(new Image(file.toURI().toString()), null);
            currentImage = new Grayscale().applyFilter(currentImage, false);

            this.imageWidth = currentImage.getWidth();
            this.imageHeight = currentImage.getHeight();

            System.out.println("Loaded image with size: " + this.imageWidth + " x " + this.imageHeight);

            this.setImage(currentImage, imageViewSpace);
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    setImage(new DCT().process(currentImage), imageViewFrequency);
//                }
//            }).start();

            try {
                int[][] matrix = Utils.readFromFile("/home/caiomcg/Workspace/DCT/samples/matrix.txt", 256);
                setImage(Utils.setImage(matrix), imageViewFrequency);
            } catch (FileNotFoundException e) {
                System.err.println("Failed to get DCT");
            }
        }
    }

    @FXML
    private void keyReleased(KeyEvent keyEvent) {
        if (keyCombinationCtrlO.match(keyEvent)) {
            this.openButton(null);
        }
    }

    @FXML
    private void onAdd() {
        try {
            int n = Integer.parseInt(nValue.getText());
            nValue.setText(String.valueOf(++n));
        } catch (NumberFormatException e) {
            nValue.setText("0");
        }
    }

    @FXML
    private void onRemove() {
        try {
            int n = Integer.parseInt(nValue.getText());
            if (--n >= 0) {
                nValue.setText(String.valueOf(n));
            }
        } catch (NumberFormatException e) {
            nValue.setText("0");
        }
    }

    @FXML
    private void onApply() {
        try {
            int n = Integer.parseInt(nValue.getText());
            if (n < 0 || n > (this.imageHeight * this.imageWidth - 1)) {
                throw new NumberFormatException("Imagelid n size");
            }
            System.err.println("All good");
        } catch (NumberFormatException e) {
            System.err.println("Invalid n size, should be between 0 and " + (this.imageHeight * this.imageWidth - 1));
        }
    }

    private void addToMemento(BufferedImage image) {
        this.originator.setBufferedImage(image);
        this.careTaker.addMemento(this.originator.save());
    }

    private void setImage(BufferedImage image, ImageView imageView) {
        imageView.setImage(SwingFXUtils.toFXImage(image, null));
    }

}
