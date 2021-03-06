package sample;



import IM.DCT.DCT;
import IM.DCT.DCTInverse;
import IM.Memento.CareTaker;
import IM.Memento.Originator;
import IM.Process.Effects.Grayscale;
import IM.Process.Effects.Negative;
import IM.Utils;
import com.sun.jmx.mbeanserver.Util;
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
    private ImageView originalImg;
    @FXML
    private ImageView applyingDCTImg;
    @FXML
    private ImageView applyingFilterDCT;
    @FXML
    private ImageView IDCT;
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
        File file = fileChooser.showOpenDialog(originalImg.getScene().getWindow());

        if (file != null) {
            this.statusLabel.setText("Opened: " + file.toURI().toString());

            currentImage = SwingFXUtils.fromFXImage(new Image(file.toURI().toString()), null);
            //currentImage = new Grayscale().applyFilter(currentImage, false);

            this.imageWidth = currentImage.getWidth();
            this.imageHeight = currentImage.getHeight();

            System.out.println("Loaded image with size: " + this.imageWidth + " x " + this.imageHeight);

            nValue.setText(String.valueOf(this.imageWidth * this.imageWidth - 1));
            this.setImage(currentImage, originalImg);
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
            this.runDCT(n);
        } catch (NumberFormatException e) {
            System.err.println("Invalid n size, should be between 0 and " + (this.imageHeight * this.imageWidth - 1));
        }
    }

    private void runDCT(int filter) {
        new Thread(() -> {
//            setImage(new BufferedImage(this.imageWidth, this.imageHeight, BufferedImage.TYPE_4BYTE_ABGR), originalImg);
            double[][] redMatrix = new DCT().process(Utils.getMatrix(currentImage, 16));
            double[][] greenMatrix = new DCT().process(Utils.getMatrix(currentImage, 8));
            double[][] blueMatrix = new DCT().process(Utils.getMatrix(currentImage, 0));
            setImage(new BufferedImage(this.imageWidth, this.imageHeight, BufferedImage.TYPE_4BYTE_ABGR), applyingDCTImg);
            setImage(Utils.setImage(redMatrix, greenMatrix, blueMatrix), applyingDCTImg);
            if (filter >= 0) {
                //Utils.printMatrix(matrix);
                System.err.println("\n\nFILTERING\n\n");
                Utils.filter(redMatrix, filter);
                Utils.filter(greenMatrix, filter);
                Utils.filter(blueMatrix, filter);
                setImage(Utils.setImage(redMatrix, greenMatrix, blueMatrix), applyingFilterDCT);
            }
            setImage(Utils.setImage(new DCTInverse().process(redMatrix), new DCTInverse().process(greenMatrix), new DCTInverse().process(blueMatrix)), IDCT);
        }).start();
    }

    private void addToMemento(BufferedImage image) {
        this.originator.setBufferedImage(image);
        this.careTaker.addMemento(this.originator.save());
    }

    private void setImage(BufferedImage image, ImageView imageView) {
        imageView.setImage(SwingFXUtils.toFXImage(image, null));
    }

}
