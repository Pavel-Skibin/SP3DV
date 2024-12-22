package com.cgvsu;

import com.cgvsu.math.Matrix4f;
import com.cgvsu.math.Vector3f;
import com.cgvsu.model.Model;
import com.cgvsu.model.NormalCalculator;
import com.cgvsu.model.Triangulator;
import com.cgvsu.objreader.ObjReader;
import com.cgvsu.render_engine.*;

import com.cgvsu.scene.Scene;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * Контроллер графического интерфейса пользователя.
 * Управляет загрузкой моделей, управлением камер, освещением и рендерингом.
 */
public class GuiController {

    private static final float TRANSLATION = 0.5F;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Canvas canvas;

    @FXML
    private VBox modelsManager;

    @FXML
    private VBox camerasManager;

    @FXML
    private VBox lightingVectorsManager;

    @FXML
    private VBox lightingModelsManager;

    @FXML
    private Button bindToCameraButton;

    @FXML
    private Button unbindFromCameraButton;

    @FXML
    private TextField textFieldScaleX;

    @FXML
    private TextField textFieldScaleY;

    @FXML
    private TextField textFieldScaleZ;

    @FXML
    private TextField textFieldRotationX;

    @FXML
    private TextField textFieldRotationY;

    @FXML
    private TextField textFieldRotationZ;

    @FXML
    private TextField textFieldTranslationX;

    @FXML
    private TextField textFieldTranslationY;

    @FXML
    private TextField textFieldTranslationZ;

    @FXML
    private TextField textFieldCameraPositionX;

    @FXML
    private TextField textFieldCameraPositionY;

    @FXML
    private TextField textFieldCameraPositionZ;

    @FXML
    private TextField textFieldCameraPointOfDirectionX;

    @FXML
    private TextField textFieldCameraPointOfDirectionY;

    @FXML
    private TextField textFieldCameraPointOfDirectionZ;

    @FXML
    private TextField textFieldLightingModelDirectionX;

    @FXML
    private TextField textFieldLightingModelDirectionY;

    @FXML
    private TextField textFieldLightingModelDirectionZ;

    @FXML
    private CheckBox checkBoxUseFillPolygon;

    @FXML
    private CheckBox checkBoxUseIntrLighting;

    @FXML
    private CheckBox checkBoxUseFlatLighting;

    @FXML
    private CheckBox checkBoxUseTexture;

    @FXML
    private ColorPicker colorPickerGird;

    @FXML
    private ColorPicker colorPickerPolygon;

    @FXML
    private ColorPicker colorPickerLight;

    @FXML
    private CheckBox checkboxUsePolygonalGrid;

    @FXML
    private RadioMenuItem withParallelizationMenuItem;

    @FXML
    private RadioMenuItem withoutParallelizationMenuItem;


    private final CameraManager cameraManager = new CameraManager();
    private final RenderParameters renderParameters = new RenderParameters();


    private int counterModels = 0;
    private int counterLightingModels = 0;

    private Timeline timeline;

    private boolean isLeftButtonPressed = false;
    private double lastMouseX, lastMouseY;
    private boolean isCtrlPressed = false;
    private float distance = 100.0f; // Расстояние от камеры до цели
    private float azimuth = 0.0f;     // Азимут (горизонтальный угол)
    private float elevation = 0.0f;   // Склонение (вертикальный угол)

    // Добавить Scene в поля класса
    private final Scene scene = new Scene();

    /**
     * Инициализация контроллера.
     * Устанавливает начальные параметры камеры, настраивает обработчики событий и запускает рендеринг.
     */
    @FXML
    public void initialize() {


        azimuth = 0.0f;
        elevation = 0.0f;
        distance = 100.0f;

        canvas.setOnScroll(event -> {
            double delta = event.getDeltaY();
            float zoomSensitivity = 0.1f;
            distance -= (float) (delta * zoomSensitivity);
            distance = Math.max(10.0f, distance);
            updateCameraPosition();
            render();
        });

        ToggleGroup toggleGroup = new ToggleGroup();
        withParallelizationMenuItem.setToggleGroup(toggleGroup);
        withoutParallelizationMenuItem.setToggleGroup(toggleGroup);
        withParallelizationMenuItem.setSelected(true);

        bindToCameraButton.setOnAction(event -> bindLightingToCamera());
        unbindFromCameraButton.setOnAction(event -> unbindLightingFromCamera());
        unbindFromCameraButton.setVisible(false);

        canvas.setOnMousePressed(this::handleMousePressed);
        canvas.setOnMouseReleased(this::handleMouseReleased);
        canvas.setOnMouseDragged(this::handleMouseDragged);

        colorPickerGird.setValue(Color.BLUE);
        colorPickerPolygon.setValue(Color.GRAY);

        updateCameraPosition();
        startRendering();
    }

    /**
     * Загружает модель из файла.
     */
    @FXML
    private void loadModelFromFile() {
        FileChooser fileChooser = createFileChooser("Model (*.obj)", "*.obj", "Load Model");
        File file = fileChooser.showOpenDialog(getStage());
        if (file == null) return;

        ObjReader objReader = ObjReader.readLinesFromPath(Path.of(file.getAbsolutePath()));
        Model newModel = objReader.read();


        newModel.setNormals(NormalCalculator.calculateNormals(newModel));
        newModel.setPolygons(Triangulator.triangulate(newModel));

        scene.addModel(newModel);
        addModelControlButtons(newModel);
        startRendering();
    }

    /**
     * Сохраняет модель в файл.
     */
    @FXML
    private void saveModelFromFile() {
        /*ObjWriter objWriter = new ObjWriter();
        for(int i = 0; i < scene.getSelectedModels().size(); i++) {
            String fileName = "Model " + i;
            objWriter.writeModelToObjFile(
                    fileName,
                    scene.getSelectedModels().get(i).getVertices(),
                    scene.getSelectedModels().get(i).getTextureVertices(),
                    scene.getSelectedModels().get(i).getNormals(),
                    scene.getSelectedModels().get(i).getPolygons()
            );
        }*/
    }


    /**
     * Обновляет позицию камеры на основе сферических координат.
     */
    private void updateCameraPosition() {
        double radAzimuth = Math.toRadians(azimuth);
        double radElevation = Math.toRadians(elevation);

        float x = (float) (distance * Math.cos(radElevation) * Math.sin(radAzimuth));
        float y = (float) (distance * Math.sin(radElevation));
        float z = (float) (distance * Math.cos(radElevation) * Math.cos(radAzimuth));

        Vector3f newPosition = new Vector3f(x, y, z).add(cameraManager.getActiveCamera().getTarget());
        cameraManager.getActiveCamera().setPosition(newPosition);
    }

    /**
     * Обрабатывает нажатие мыши.
     *
     * @param event событие мыши
     */
    private void handleMousePressed(MouseEvent event) {
        if (event.isPrimaryButtonDown()) {
            isLeftButtonPressed = true;
            lastMouseX = event.getX();
            lastMouseY = event.getY();
            isCtrlPressed = event.isControlDown();
        }
    }

    /**
     * Обрабатывает отпускание мыши.
     *
     * @param event событие мыши
     */
    private void handleMouseReleased(MouseEvent event) {
        if (!event.isPrimaryButtonDown()) {
            isLeftButtonPressed = false;
        }
    }

    /**
     * Обрабатывает перетаскивание мыши.
     *
     * @param event событие мыши
     */
    private void handleMouseDragged(MouseEvent event) {
        if (isLeftButtonPressed) {
            double deltaX = event.getX() - lastMouseX;
            double deltaY = event.getY() - lastMouseY;

            if (!isCtrlPressed) {
                rotateCamera(deltaX, deltaY);
            } else {
                panCamera(deltaX, deltaY);
            }

            lastMouseX = event.getX();
            lastMouseY = event.getY();
            render();
        }
    }

    /**
     * Вращает камеру на заданные углы.
     *
     * @param deltaX изменение по оси X
     * @param deltaY изменение по оси Y
     */
    private void rotateCamera(double deltaX, double deltaY) {
        float sensitivity = 0.5f;
        azimuth += (float) (deltaX * sensitivity);
        elevation += (float) (deltaY * sensitivity);

        elevation = Math.max(-87, Math.min(87, elevation));
        azimuth = azimuth % 360;
        if (azimuth < 0) azimuth += 360;

        updateCameraPosition();
    }

    /**
     * Перемещает цель камеры на основе изменений мыши.
     *
     * @param deltaX изменение по оси X
     * @param deltaY изменение по оси Y
     */
    private void panCamera(double deltaX, double deltaY) {
        float panSensitivity = 0.05f;
        Vector3f direction = cameraManager.getActiveCamera().getTarget().sub(cameraManager.getActiveCamera().getPosition());
        direction.normalize();

        Vector3f right = direction.cross(new Vector3f(0, 1, 0));
        right.normalize();
        right.multiply((float) deltaX * panSensitivity);


        Vector3f up = new Vector3f(0, 1, 0);
        up.multiply((float) deltaY * panSensitivity);
        cameraManager.getActiveCamera().setTarget(cameraManager.getActiveCamera().getTarget().add(right).add(up));
        updateCameraPosition();
    }

    /**
     * Обрабатывает перемещение камеры вперед.
     *
     * @param actionEvent событие действия
     */
    @FXML
    private void handleCameraForward(ActionEvent actionEvent) {
        moveCamera(new Vector3f(0, 0, -TRANSLATION));
    }

    /**
     * Обрабатывает перемещение камеры назад.
     *
     * @param actionEvent событие действия
     */
    @FXML
    private void handleCameraBackward(ActionEvent actionEvent) {
        moveCamera(new Vector3f(0, 0, TRANSLATION));
    }

    /**
     * Обрабатывает перемещение камеры влево.
     *
     * @param actionEvent событие действия
     */
    @FXML
    private void handleCameraLeft(ActionEvent actionEvent) {
        moveCamera(new Vector3f(TRANSLATION, 0, 0));
    }

    /**
     * Обрабатывает перемещение камеры вправо.
     *
     * @param actionEvent событие действия
     */
    @FXML
    private void handleCameraRight(ActionEvent actionEvent) {
        moveCamera(new Vector3f(-TRANSLATION, 0, 0));
    }

    /**
     * Обрабатывает перемещение камеры вверх.
     *
     * @param actionEvent событие действия
     */
    @FXML
    private void handleCameraUp(ActionEvent actionEvent) {
        moveCamera(new Vector3f(0, TRANSLATION, 0));
    }

    /**
     * Обрабатывает перемещение камеры вниз.
     *
     * @param actionEvent событие действия
     */
    @FXML
    private void handleCameraDown(ActionEvent actionEvent) {
        moveCamera(new Vector3f(0, -TRANSLATION, 0));
    }

    /**
     * Перемещает камеру на заданный вектор.
     *
     * @param delta вектор перемещения
     */
    private void moveCamera(Vector3f delta) {
        Camera activeCamera = cameraManager.getActiveCamera();
        activeCamera.setPosition(activeCamera.getPosition().add(delta));
        render();
    }

    /**
     * Парсит параметры трансформации из текстовых полей.
     *
     * @return параметры трансформации
     * @throws NumberFormatException если данные некорректны
     */
    private TransformationParameters parseParameters() throws NumberFormatException {
        TransformationParameters params = new TransformationParameters();
        params.setAlpha(Double.parseDouble(textFieldRotationX.getText()));
        params.setBeta(Double.parseDouble(textFieldRotationY.getText()));
        params.setGamma(Double.parseDouble(textFieldRotationZ.getText()));
        params.setTranslationX(Double.parseDouble(textFieldTranslationX.getText()));
        params.setTranslationY(Double.parseDouble(textFieldTranslationY.getText()));
        params.setTranslationZ(Double.parseDouble(textFieldTranslationZ.getText()));
        params.setScaleX(Double.parseDouble(textFieldScaleX.getText()));
        params.setScaleY(Double.parseDouble(textFieldScaleY.getText()));
        params.setScaleZ(Double.parseDouble(textFieldScaleZ.getText()));
        return params;
    }

    /**
     * Показывает всплывающее окно с ошибкой.
     *
     * @param title   заголовок окна
     * @param header  заголовок сообщения
     * @param content содержание сообщения
     */
    private void showErrorAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Выполняет перерасчет нормалей модели.
     */
    @FXML
    private void recalculationOfNormals() {
        for (Model selectedModel : scene.getSelectedModels()) {
            TransformationParameters params;
            try {
                params = parseParameters();
            } catch (NumberFormatException e) {
                showErrorAlert("Ошибка ввода", "Некорректные данные",
                        "Пожалуйста, убедитесь, что все поля содержат числовые значения.");
                return;
            }

            Vector3f rotation = new Vector3f((float) params.getAlpha(), (float) params.getBeta(), (float) params.getGamma());
            Vector3f scale = new Vector3f((float) params.getScaleX(), (float) params.getScaleY(), (float) params.getScaleZ());
            Vector3f translation = new Vector3f((float) params.getTranslationX(), (float) params.getTranslationY(), (float) params.getTranslationZ());

            Matrix4f transformationMatrix = GraphicConveyor.scaleRotateTranslate(rotation, scale, translation);
            selectedModel.getVertices().replaceAll(vector -> GraphicConveyor.multiplyMatrix4ByVector3(transformationMatrix, vector));
            selectedModel.setNormals(NormalCalculator.calculateNormals(selectedModel));
        }
        render();
    }



    @FXML
    private void addNewLightingVector() {
        try {
            float x = Float.parseFloat(textFieldLightingModelDirectionX.getText());
            float y = Float.parseFloat(textFieldLightingModelDirectionY.getText());
            float z = Float.parseFloat(textFieldLightingModelDirectionZ.getText());

            Color color = colorPickerLight.getValue();

            Vector3f direction = new Vector3f(x, y, z);
            int lightId = renderParameters.getLightingManager().addLight(direction, color);
            addLightControlButtons(lightId);




        } catch (NumberFormatException e) {
            showErrorAlert("Ошибка ввода", "Некорректные данные",
                    "Пожалуйста, введите корректные данные для источника освещения.");
        }
    }


    private void addLightControlButtons(int lightId) {
        ToggleButton lightButton = new ToggleButton("Light " + lightId);
        lightButton.setOnAction(event -> {
            if (lightButton.isSelected()) {
                renderParameters.getLightingManager().getActiveLightIds().add(lightId);
            } else {
                renderParameters.getLightingManager().getActiveLightIds().remove(lightId);
            }

            // Проверяем, если активных источников света нет, то снимаем привязку от камеры
            if (renderParameters.getLightingManager().getActiveLightIds().isEmpty()) {
                renderParameters.getLightingManager().unbindFromCamera();
            }



            render();
        });

        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(event -> deleteLight(lightId));

        HBox controls = new HBox(5, lightButton, deleteButton);
        controls.setId("light-control-" + lightId);  // Добавляем id
        lightingVectorsManager.getChildren().add(controls);
    }


    private void deleteLight(int lightId) {
        renderParameters.getLightingManager().removeLight(lightId);
        lightingVectorsManager.getChildren().removeIf(node ->
                node.getId() != null && node.getId().equals("light-control-" + lightId)
        );
        render();
    }



    @FXML
    private void bindLightingToCamera() {
        try {
            if (renderParameters.getLightingManager().getActiveLightIds().size() != 1) {
                throw new IllegalStateException("Выбери 1 источник освещения");
            }
            renderParameters.getLightingManager().bindToCamera(cameraManager.getActiveCamera());
            bindToCameraButton.setVisible(false);
            unbindFromCameraButton.setVisible(true);
            render();
        } catch (IllegalStateException e) {
            showErrorAlert("Ошибка привязки", e.getMessage(), "Пожалуйста, выберите только один источник освещения.");
        }
    }

    @FXML
    private void unbindLightingFromCamera() {
        renderParameters.getLightingManager().unbindFromCamera();
        bindToCameraButton.setVisible(true);
        unbindFromCameraButton.setVisible(false);
        render();
    }


    /**
     * Запускает процесс рендеринга.
     */
    private void startRendering() {
        if (timeline == null) {
            timeline = new Timeline(new KeyFrame(Duration.millis(20), event -> render()));
            timeline.setCycleCount(Animation.INDEFINITE);
            timeline.play();
        }
    }

    /**
     * Выполняет рендеринг сцены на канвасе.
     */
    private void render() {
        double width = canvas.getWidth();
        double height = canvas.getHeight();



        canvas.getGraphicsContext2D().clearRect(0, 0, width, height);
        Camera activeCamera = cameraManager.getActiveCamera();
        activeCamera.setAspectRatio((float) (width / height));

        scene.renderScene(canvas.getGraphicsContext2D(), activeCamera, renderParameters, width, height);
    }

    /**
     * Добавляет элементы управления для новой модели.
     */
    private void addModelControlButtons(Model newModel) {
        counterModels++;

        ToggleButton modelButton = new ToggleButton("Model " + counterModels);
        modelButton.setOnAction(event -> {
            if (modelButton.isSelected()) {
                scene.selectModel(newModel);
            } else {
                scene.deselectModel(newModel);
            }
        });

        ToggleButton textureButton = new ToggleButton("Add Texture");
        textureButton.setOnAction(event -> {
            if (textureButton.isSelected()) {
                textureButton.setText("Remove Texture");
                addTexture(newModel);
            } else {
                textureButton.setText("Add Texture");
                removeTexture(newModel);
            }
        });

        Button deleteModelButton = new Button("Delete");
        deleteModelButton.setOnAction(event -> deleteModel(newModel, modelButton));
        HBox modelControls = new HBox(5, modelButton, textureButton, deleteModelButton);
        modelsManager.getChildren().add(modelControls);
    }


    private void deleteModel(Model model, ToggleButton modelButton) {
        scene.removeModel(model);
        modelsManager.getChildren().removeIf(node ->
                node instanceof HBox hBox && hBox.getChildren().contains(modelButton)
        );
        render();
    }

    private void addTexture(Model model) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Texture (*.png)", "*.png"));
        fileChooser.setTitle("Load Texture");

        File initialDirectory = new File("3DModels");
        if (initialDirectory.exists()) {
            fileChooser.setInitialDirectory(initialDirectory);
        }

        File file = fileChooser.showOpenDialog((Stage) canvas.getScene().getWindow());
        if (file == null) {
            return;
        }
        String filePath = file.getAbsolutePath();

        model.loadTexture(filePath);
        render();
    }


    /**
     * Удаляет текстуру из модели.
     *
     * @param model
     */
    private void removeTexture(Model model) {
        model.clearTexture();
        render();
    }

    @FXML
    private void handleGirdColorChange() {
        renderParameters.setPolygonalGridColor(colorPickerGird.getValue());
    }

    @FXML
    private void handlePolygonColorChange() {
        renderParameters.setDefaultFillColor(colorPickerPolygon.getValue());
    }


    /**
     * Обработчик клика по канвасу для фокусировки.
     */
    @FXML
    public void onClickCanvas() {
        canvas.requestFocus();
    }

    /**
     * Возвращает камеру в исходное положение.
     * TODO: Реализовать возврат камеры.
     */
    @FXML
    private void returnToTheStartingPosition() {
        // Реализация возврата камеры
    }

    /**
     * Перемещает камеру к модели.
     * TODO: Реализовать перемещение камеры к модели.
     */
    @FXML
    private void moveToTheModel() {
        // Реализация перемещения камеры к модели
    }



    /**
     * Обрабатывает переключение использования текстур.
     */
    @FXML
    private void handleCheckBoxUseTexture() {
        renderParameters.setEnableTextureMapping(checkBoxUseTexture.isSelected());
        render();
    }

    /**
     * Обрабатывает переключение интерполированного освещения.
     */
    @FXML
    private void handleCheckBoxUseIntrLighting() {
        renderParameters.setEnableInterpolatedLighting(checkBoxUseIntrLighting.isSelected());
        render();
    }

    /**
     * Обрабатывает переключение плоского освещения.
     */
    @FXML
    private void handleCheckBoxUseFlatLighting() {
        renderParameters.setEnableFlatLighting(checkBoxUseFlatLighting.isSelected());
        render();
    }

    /**
     * Обрабатывает переключение заполнения полигонов.
     */
    @FXML
    private void handleCheckBoxFillPolygon() {
        renderParameters.setEnableFillPolygon(checkBoxUseFillPolygon.isSelected());
        render();
    }

    /**
     * Обрабатывает переключение отображения полигональной сетки.
     */
    @FXML
    private void handleCheckBoxDrawPolygonalGrid() {
        renderParameters.setEnablePolygonalGrid(checkboxUsePolygonalGrid.isSelected());
        render();
    }

    /**
     * Переключает режим параллелизации рендеринга.
     */
    @FXML
    private void toggleParallelization() {
        boolean isParallelizationEnabled = withParallelizationMenuItem.isSelected();
        renderParameters.setEnableParallelization(isParallelizationEnabled);
        render();
    }

    /**
     * Добавляет новую камеру, используя координаты из текстовых полей.
     */
    @FXML
    private void addNewCamera() {
        try {

            float posX = Float.parseFloat(textFieldCameraPositionX.getText());
            float posY = Float.parseFloat(textFieldCameraPositionY.getText());
            float posZ = Float.parseFloat(textFieldCameraPositionZ.getText());


            float dirX = Float.parseFloat(textFieldCameraPointOfDirectionX.getText());
            float dirY = Float.parseFloat(textFieldCameraPointOfDirectionY.getText());
            float dirZ = Float.parseFloat(textFieldCameraPointOfDirectionZ.getText());

            Vector3f position = new Vector3f(posX, posY, posZ);
            Vector3f target = new Vector3f(dirX, dirY, dirZ);


            Camera newCamera = new Camera(
                    position,
                    target,
                    0.3F,
                    1,
                    25F,
                    2000
            );


            int cameraId = cameraManager.addCamera(newCamera);
            addCameraControls(cameraId);
            switchToCamera(cameraId);
            render();
        } catch (NumberFormatException e) {
            showErrorAlert("Ошибка ввода", "Некорректные данные",
                    "Пожалуйста, введите числовые значения для координат позиции и направления камеры.");
        }
    }

    /**
     * Добавляет элементы управления для новой камеры.
     *
     * @param cameraId идентификатор камеры
     */
    private void addCameraControls(int cameraId) {
        Button cameraButton = new Button("Camera " + cameraId);
        cameraButton.setOnAction(event -> switchToCamera(cameraId));

        Button deleteCamera = new Button("Delete");
        deleteCamera.setOnAction(event -> deleteCamera(cameraId));

        HBox cameraControls = new HBox(5, cameraButton, deleteCamera);
        camerasManager.getChildren().add(cameraControls);
    }

    /**
     * Переключается на выбранную камеру.
     *
     * @param cameraId идентификатор камеры
     */
    private void switchToCamera(int cameraId) {
        cameraManager.setActiveCamera(cameraId);
        Camera activeCamera = cameraManager.getActiveCamera();

        textFieldCameraPositionX.setText(String.valueOf(activeCamera.getPosition().getX()));
        textFieldCameraPositionY.setText(String.valueOf(activeCamera.getPosition().getY()));
        textFieldCameraPositionZ.setText(String.valueOf(activeCamera.getPosition().getZ()));

        Vector3f target = activeCamera.getTarget();
        textFieldCameraPointOfDirectionX.setText(String.valueOf(target.getX()));
        textFieldCameraPointOfDirectionY.setText(String.valueOf(target.getY()));
        textFieldCameraPointOfDirectionZ.setText(String.valueOf(target.getZ()));
        render();
    }

    /**
     * Удаляет камеру и соответствующие элементы управления.
     *
     * @param cameraId идентификатор камеры
     */
    private void deleteCamera(int cameraId) {
        cameraManager.removeCamera(cameraId);
        camerasManager.getChildren().removeIf(node -> {
            if (node instanceof HBox hBox) {
                return hBox.getChildren().stream()
                        .anyMatch(child -> child instanceof Button button &&
                                button.getText().equals("Camera " + cameraId));
            }
            return false;
        });
        render();
    }

    /**
     * Создает настроенный FileChooser.
     *
     * @param description описание фильтра
     * @param extension   расширение файла
     * @param title       заголовок окна
     * @return настроенный FileChooser
     */
    private FileChooser createFileChooser(String description, String extension, String title) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(description, extension));
        fileChooser.setTitle(title);
        File initialDirectory = new File("3DModels");
        if (initialDirectory.exists()) {
            fileChooser.setInitialDirectory(initialDirectory);
        }
        return fileChooser;
    }

    /**
     * Получает текущий Stage из канваса.
     *
     * @return текущий Stage
     */
    private Stage getStage() {
        return (Stage) canvas.getScene().getWindow();
    }
}