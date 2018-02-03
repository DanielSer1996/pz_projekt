package i5b5.daniel.serszen.pz.view.scenes;

import i5b5.daniel.serszen.pz.controller.CarPartController;
import i5b5.daniel.serszen.pz.controller.XmlParser;
import i5b5.daniel.serszen.pz.model.exceptions.InvalidXmlFormatException;
import i5b5.daniel.serszen.pz.model.factories.BeanFactory;
import i5b5.daniel.serszen.pz.model.mybatis.dto.Car;
import i5b5.daniel.serszen.pz.model.mybatis.dto.CarPart;
import i5b5.daniel.serszen.pz.view.delegates.ViewDelegate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.WindowEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.List;

public class AddCarPartScene extends AbstractCustomScene {
    private final Logger logger = LogManager.getLogger(AddCarPartScene.class);

    private GridPane rootPane;
    private VBox descPane;

    private List<CarPart> carPartsList;

    private boolean newCategory;

    private ObservableList<String> carPartCategories = FXCollections.observableArrayList();

    private Label categoryLabel;
    private Label categoryDescriptionLabel;
    private Label nameLabel;
    private Label producerLabel;
    private Label producerCodeNameLabel;
    private Label chosenFile;

    private ComboBox<String> categories;
    private TextField category;
    private TextArea categoryDescription;
    private TextField name;
    private TextField producer;
    private TextField producerCodeName;
    private TextField imgUri;

    private Button chooseImg;
    private Button loadXml;

    private FileChooser fileChooser;

    private Button confirmButton;

    private final CarPartController carPartController;

    private Car addedCar;
    private CarPart addedCarPart;

    public AddCarPartScene() {
        this(new Pane());
    }

    private AddCarPartScene(Parent root) {
        super(root);
        carPartController = BeanFactory.getCarPartController();
        init();
    }

    private void init() {

        categories = new ComboBox<>();
        initTextFields();
        initLabels();
        initFileChooser();
        initConfirmButton();
        initLoadXmlButton();
        initRootPane();
        initDescriptionPane();

        this.setRoot(rootPane);
    }

    @Override
    protected void initRootPane() {
        rootPane = new GridPane();
        rootPane.setPadding(new Insets(10, 10, 10, 10));
        rootPane.setVgap(5);
        rootPane.setHgap(5);
        rootPane.setAlignment(Pos.TOP_LEFT);

        VBox pane = new VBox();
        pane.setAlignment(Pos.TOP_CENTER);
        pane.setSpacing(5);
        pane.getChildren().addAll(categoryLabel,
                category,
                nameLabel,
                name,
                producerLabel,
                producer,
                producerCodeNameLabel,
                producerCodeName,
                chosenFile,
                imgUri,
                chooseImg,
                loadXml,
                confirmButton
        );

        VBox picker = new VBox();
        picker.setAlignment(Pos.TOP_CENTER);
        picker.getChildren().add(categories);
        rootPane.add(picker, 0, 0);
        rootPane.add(pane, 2, 0);
    }

    private void initFileChooser() {
        fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("*.jpg", "*.jpg"),
                new FileChooser.ExtensionFilter("*.png", "*.png"));
        chooseImg = new Button();
        chooseImg.setOnAction(event -> {
            File file = fileChooser.showOpenDialog(this.getWindow());
            if (file != null) {
                imgUri.setText(file.getAbsolutePath());
            }
        });
    }

    private void initLoadXmlButton() {
        loadXml = new Button();

        loadXml.setOnAction(event -> {
            FileChooser xml = new FileChooser();
            xml.getExtensionFilters().add(new FileChooser.ExtensionFilter("*.xml", "*.xml"));

            try {
                File file = xml.showOpenDialog(this.getWindow());
                if (file != null) {
                    CarPart cp = XmlParser.unmarshallCarPartXml(file);
                    category.setText(cp.getCategory());
                    name.setText(cp.getName());
                    producer.setText(cp.getProducer());
                    producerCodeName.setText(cp.getProducerModelCode());
                    imgUri.setText(cp.getImgUri());
                }
            } catch (InvalidXmlFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR,
                        ViewDelegate.getInstance().getMessageForAlert("badXmlFormat"),
                        ButtonType.OK);
                alert.setX(this.getWindow().getX());
                alert.setY(this.getWindow().getY()+this.getWindow().getY()/2);
                alert.showAndWait();
            }
        });
    }

    private void initConfirmButton() {
        confirmButton = new Button();
        confirmButton.setOnAction(event -> {
            Task task = new Task() {
                @Override
                protected Object call() throws Exception {
                    addedCarPart = new CarPart(
                            name.getText(),
                            producer.getText(),
                            producerCodeName.getText(),
                            category.getText(),
                            imgUri.getText());
                    if (newCategory) {
                        carPartController.insertCategory(category.getText(), categoryDescription.getText());
                    }
                    carPartController.insertCarPart(addedCarPart, addedCar);
                    return null;
                }
            };
            task.setOnSucceeded(event1 -> {
                this.getWindow().fireEvent(new WindowEvent(this.getWindow(), WindowEvent.WINDOW_CLOSE_REQUEST));
            });

            task.setOnFailed(event1 -> {
                logger.error(task.getException());
                Alert alert = new Alert(Alert.AlertType.ERROR,
                        ViewDelegate.getInstance().getMessageForAlert("incorrectData"),
                        ButtonType.OK);
                alert.setX(this.getWindow().getX());
                alert.setY(this.getWindow().getY() + this.getWindow().getHeight() / 3);
                alert.showAndWait();
            });

            new Thread(task).start();
        });
    }

    private void initDescriptionPane() {
        descPane = new VBox();
        categoryDescriptionLabel = new Label();
        categoryDescription = new TextArea();
        categoryDescription.setPrefWidth(300);
        categoryDescription.setPrefHeight(200);
        categoryDescription.setWrapText(true);

        descPane.getChildren().addAll(categoryDescriptionLabel, categoryDescription);
    }

    private void initTextFields() {
        category = new TextField();
        category.setEditable(false);
        name = new TextField();
        name.setEditable(true);
        producer = new TextField();
        producerCodeName = new TextField();
        imgUri = new TextField();
        imgUri.setEditable(false);
    }


    private void initLabels() {
        categoryLabel = new Label();
        nameLabel = new Label();
        producerLabel = new Label();
        producerCodeNameLabel = new Label();
        chosenFile = new Label();
    }

    public void initContent() {
        this.getWindow().setHeight(500);
        this.getWindow().setWidth(400);
        categories.setOnAction(event -> {
            String selected = categories.getSelectionModel().getSelectedItem();
            setTextField(category, selected);
        });


        Task<List<CarPart>> task = new Task<List<CarPart>>() {
            @Override
            protected List<CarPart> call() throws Exception {
                carPartsList = carPartController.getAllCarParts();
                return carPartsList;
            }
        };

        task.setOnSucceeded(event -> {
            putContentIntoBrandArray();
        });

        new Thread(task).start();
    }

    private void putContentIntoBrandArray() {
        for (CarPart carPart : carPartsList) {
            if (!carPartCategories.contains(carPart.getCategory())) {
                carPartCategories.add(carPart.getCategory());
            }
        }
        initBrandItems();
    }

    private void setTextField(TextField toChange, String selected) {
        if ("Dodaj nowa".equals(selected) || "Add new".equals(selected)) {
            toChange.clear();
            toChange.setEditable(true);
            this.newCategory = true;
            this.getWindow().setWidth(700);
            rootPane.add(descPane, 3, 0);
        } else {
            toChange.setEditable(false);
            toChange.setText(selected);
            this.newCategory = false;
            this.getWindow().setWidth(400);
            rootPane.getChildren().remove(descPane);
        }
    }

    private void initBrandItems() {
        categories.setItems(carPartCategories);
        categories.getItems().add(ViewDelegate.getInstance().getMessageForAlert("addNew"));
    }

    public CarPart getAddedCarPart() {
        return addedCarPart;
    }

    public void setAddedCarPart(CarPart addedCarPart) {
        this.addedCarPart = addedCarPart;
    }

    public void setAddedCar(Car addedCar) {
        this.addedCar = addedCar;
    }
}
