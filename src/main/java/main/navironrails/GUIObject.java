package main.navironrails;

import java.io.File;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


//import navironrails.GUIObject.ToolBoxType;
import navironrails.civilsystems.railwaysystem.ModelObject;
import navironrails.fxext.RailPane;
import navironrails.reporter.ReporterObject;
import navironrails.surveyor.MapObject;

import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReference;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.Viewpoint;
import com.esri.arcgisruntime.mapping.view.MapView;


public class GUIObject {
    
    /**
     * @enum ToolBoxType
     * 
     * A basic enumeration of the various functions of the buttons on the toolbox.
     * These values are assigned to the userData values of each toggleButton, and the
     * currently selected tool from the box may be fetched directly from the toolbox
     * toggleGroup, in the form of this enumeration.
     */
    public enum ToolBoxType{
        SELECT,
        PAN,
        STATION,
        JUNCTION,
        TERMINAL,
        TRACK,
        RAISED,
        TUNNEL,
        BRIDGE,
        BANKED,
    }
    
    // These objects and containers are often needed by other
    //	widgets for the purposes of binding themselves and
    //	in general for working various layout magicks.
    
    Scene scene;
    BorderPane root;
    ImageView splash;
    StackPane bindPane;
    ScrollPane scrollPane;
    TabPane modelTabPane;
    VBox topBar;

    // These Panes are Important, as they are called most
    //	often during the handling of GUI events.
    ToggleGroup toolBox;
    Pane mapPane;
    RailPane railPane;
    
    // These objects are also important, as the above panes
    //	often call them during their own event handling routines.
    MapObject mapObject;
    ModelObject model;
    ReporterObject reporter;
    
    //ArchGis
    MapView mapView;
    ArcGISMap map;

    private static final double LATITUDE = 29.542;
    private static final double LONGITUDE = -98.5556;
    private static final int LOD = 6;
    
    /**
     * Constructor for the GUIObject.This initializes some basic top level
     * GUI elements that don't require complex initialization.
     */
    public GUIObject(){
        this.bindPane = new StackPane();
        this.scrollPane = new ScrollPane();
        //scrollPane.setPrefWidth(2000);
        //scrollPane.setPrefHeight(2000);
        scrollPane.setHbarPolicy(ScrollBarPolicy.ALWAYS);
        scrollPane.setVbarPolicy(ScrollBarPolicy.ALWAYS);
        
        this.root = new BorderPane();
    }
    
    /**
     * Performs the actual of various complex GUI elements, and returns
     * the fully initialized scene. Since this method only returns a scene,
     * it may be possible to wrap the entire functionality of the application
     * into a sub package, and have it be allowed to loaded as a dialogue in
     * some other application. Experimentation for another time, I suppose.
     * 
     * 
     * The flow of logic of this method can be improved. Instead of initializing
     * various sub-components and attaching them to portions of the root pane
     * in various locations, it would be much cleaner to simply have 3 methods here
     * which configure the 3 components to be attached to the root node and then
     * simply return those components. This way, the root node can be configured
     * in one location, using the return values to attach to the center, top,
     * and left sides of our root borderPane. This would help keep code localized,
     * and improve maintenance. Will definitely be done before the next semester.
     * @return
     */
    public Scene initializeGUI(){
        initTopBar();
        initMap();
        initReports();
        initSplash();

        scene = new Scene(root, 1000, 700);
        return scene;
    }
    
    private void initTopBar(){
        topBar = new VBox();
        topBar.getChildren().addAll(initMenuBar(new MenuBar()), initToolBox());
        topBar.setSpacing(4);
        root.setTop(topBar);
    }
    
    
    /**
     * Fairly straightforward initialization of the MenuBar. Menu items are set,
     * and handlers are assigned to onAction events, (e.g. click events). The handlers
     * on these menu items will call methods on the map, the model, or the reporter.
     * 
     * @param mbr
     * @return An HBox containing all the configured elements.
     */
    private HBox initMenuBar(MenuBar mbr){
		
		// Nothing special going on in here. Each menu item will have a method
		//	attached to it
	
	// File menu
        Menu menuFile = new Menu("File");

		MenuItem fileNewProject = new MenuItem("New Project...");
		fileNewProject.setOnAction(e -> this.newProjectDialogue());

		MenuItem fileSave = new MenuItem("Save");
		fileSave.setOnAction(e -> this.saveAsProcess());

		MenuItem fileSaveAs = new MenuItem("Save As...");
		fileSaveAs.setOnAction(e -> this.saveAsProcess());

		MenuItem fileOpen = new MenuItem("Open");
		fileOpen.setOnAction(e -> this.openFileProcess());

		MenuItem exitOption = new MenuItem("Exit");
		exitOption.setOnAction(e -> this.exitDialogue());

        menuFile.getItems().addAll(fileNewProject, fileSave, fileSaveAs, fileOpen, exitOption);
        
        // Edit menu (used to be Options hence the object names)
        Menu menuOpt = new Menu("Edit");
		
        MenuItem optConfig = new MenuItem("Configure Project Info...");
        optConfig.setOnAction(e -> this.configureProjectInfoDialogue());
        
        MenuItem optReport = new MenuItem("Generate Report...");
        optReport.setOnAction(e -> this.generateReportDialogue());
        
        MenuItem optImportCiv = new MenuItem("Import Civil System...");
        optImportCiv.setOnAction(e -> this.importCivilSystemDialogue());
        
        MenuItem optCivSystem = new MenuItem("Manage Civil Systems....");
        optCivSystem.setOnAction(e -> this.manageCivilSystemsDialogue());
        
        RadioMenuItem optSelectSticky = new RadioMenuItem("Select is Sticky");
        optSelectSticky.setSelected(false);
        
        MenuItem optValidateCurrent = new MenuItem("Validate Current Model...");
        optValidateCurrent.setOnAction(e -> this.validateCurrentModel());
        
        RadioMenuItem optAutoValidate = new RadioMenuItem("AutoValidate");
        optAutoValidate.setSelected(false);
        
        menuOpt.getItems().addAll(optConfig, optReport, optImportCiv,
                optCivSystem, optValidateCurrent,
                optAutoValidate, optSelectSticky);
        
	// Manage menu
        Menu menuMan = new Menu("Manage");
		
        MenuItem manStations = new MenuItem("Stations");
        manStations.setOnAction(e -> this.manageDialogue(manStations.getText()));
		
        MenuItem manComponents = new MenuItem("Components");
        manComponents.setOnAction(e -> this.manageDialogue(manComponents.getText()));
		
        MenuItem manVendors = new MenuItem("Vendors");
        manVendors.setOnAction(e -> this.manageDialogue(manVendors.getText()));
		
        MenuItem manServices = new MenuItem("Services");
        manServices.setOnAction(e -> this.manageDialogue(manServices.getText()));
		
        MenuItem manPolicies = new MenuItem("Policies");
        manPolicies.setOnAction(e -> this.manageDialogue(manPolicies.getText()));
		
        MenuItem manProject = new MenuItem("Project");
        manProject.setOnAction(e -> this.manageDialogue(manProject.getText()));
        
        menuMan.getItems().addAll(manStations, manComponents, manVendors, manServices, manPolicies, manProject);
        
        // Resources menu
        Menu menuRes = new Menu("Resources");
		
        MenuItem resBudget = new MenuItem("Budget");
        resBudget.setOnAction(e -> this.resourceDialogue(resBudget.getText()));
		
        MenuItem resPersonal = new MenuItem("Personnel");
        resPersonal.setOnAction(e -> this.resourceDialogue(resPersonal.getText()));
		
        MenuItem resProperty = new MenuItem("Property");
        resProperty.setOnAction(e -> this.resourceDialogue(resProperty.getText()));
		
        MenuItem resGrants = new MenuItem("Grants");
        resGrants.setOnAction(e -> this.resourceDialogue(resGrants.getText()));
		
        MenuItem resLicense = new MenuItem("License");
        resLicense.setOnAction(e -> this.resourceDialogue(resLicense.getText()));
		
        MenuItem resMisc = new MenuItem("Misc");
        resMisc.setOnAction(e -> this.resourceDialogue(resMisc.getText()));
        
        menuRes.getItems().addAll(resBudget, resPersonal, resProperty, resGrants, resLicense, resMisc);

        // Help menu
        Menu menuHelp = new Menu("Help");
		
        MenuItem about = new MenuItem("About");
        about.setOnAction(e -> this.AboutDialog());
		
        MenuItem userManual = new MenuItem("User Manual");
        userManual.setOnAction(e -> this.userManualProcess());
        
        MenuItem sourceBrowser = new MenuItem("Source Browser");
        sourceBrowser.setOnAction(e -> this.sourceBrowserDialogue(sourceBrowser.getText()));
        
        menuHelp.getItems().addAll(about, userManual, sourceBrowser);

		// Add them all together
        mbr.getMenus().addAll(menuFile, menuOpt, menuMan, menuRes, menuHelp);
        mbr.prefWidthProperty().bind(topBar.widthProperty());
        
        HBox menuBarContainer = new HBox(mbr);
        
        return menuBarContainer;
    }

    /**
     * Another fairly straightforward configuration of the toolbox beneath the
     * toolbar. Having said that, there are some architectural issues. Right now,
     * the RailPane receives the toolbox through constructor injection because
     * the public methods of the RailPane need to collect the value of the
     * currently toggled button for some operations.
     * 
     * This introduces uneccessary copuling between the GUIObject and the RailPane.
     * It should be fixed by changing the public methods of the RailPane such that
     * only those methods that need the toggle value are passed it as a parameter.
     * 
     * @return 
     */
    
    private HBox initToolBox(){
        toolBox = new ToggleGroup();
        
	// So I really like this, declaring everything at the beginning of the method
	//	but there are just too many objects in all the other methods to warrent
	//	doing this for everything so I'm not doing it right now. Maybe in the
	//	future --Ronald
        ToggleButton selectBtn;
        ToggleButton panBtn;
        ToggleButton stationBtn;
        ToggleButton junctionBtn;
        ToggleButton terminalBtn;
        ToggleButton trackBtn;
        ToggleButton raisedBtn;
        ToggleButton tunnelBtn;
        ToggleButton bridgeBtn;
        ToggleButton bankedBtn;
        ToggleButton sliderToggleBtn;
        Slider slider;
        Label timeline;
        
	// Select button
    // This button is selected by default.
        selectBtn = new ToggleButton();
        selectBtn.setGraphic(new ImageView(new Image("resources/icons/selectImg.png")));
        selectBtn.setToggleGroup(toolBox);
        selectBtn.setUserData(ToolBoxType.SELECT);
        selectBtn.setSelected(true);
        selectBtn.setTooltip(new Tooltip("Select Tool"));
        
	// Pan button
        panBtn = new ToggleButton();
        panBtn.setGraphic(new ImageView(new Image("resources/icons/panImg.png")));
        panBtn.setToggleGroup(toolBox);
        panBtn.setUserData(ToolBoxType.PAN);
        panBtn.setTooltip(new Tooltip("Pan Tool"));
        
	// Station button
        stationBtn = new ToggleButton();
        stationBtn.setGraphic(new ImageView(new Image("resources/icons/stationImg.png")));
        stationBtn.setToggleGroup(toolBox);
        stationBtn.setUserData(ToolBoxType.STATION);
        stationBtn.setTooltip(new Tooltip("Station Tool"));
        
	// Junction button
        junctionBtn = new ToggleButton();
        junctionBtn.setGraphic(new ImageView(new Image("resources/icons/junctionImg.png")));
        junctionBtn.setToggleGroup(toolBox);
        junctionBtn.setUserData(ToolBoxType.JUNCTION);
        junctionBtn.setTooltip(new Tooltip("Junction Tool"));
        
	// Terminal button
        terminalBtn = new ToggleButton();
        terminalBtn.setGraphic(new ImageView(new Image("resources/icons/terminalImg.png")));
        terminalBtn.setToggleGroup(toolBox);
        terminalBtn.setUserData(ToolBoxType.TERMINAL);
        terminalBtn.setTooltip(new Tooltip("Terminal Tool"));
        
	// Track button
        trackBtn = new ToggleButton();
        trackBtn.setGraphic(new ImageView(new Image("resources/icons/trackImg.png")));
        trackBtn.setToggleGroup(toolBox);
        trackBtn.setUserData(ToolBoxType.TRACK);
        trackBtn.setTooltip(new Tooltip("Track Tool"));
        
	// Raised track button
        raisedBtn = new ToggleButton();
        raisedBtn.setGraphic(new ImageView(new Image("resources/icons/raisedImg.png")));
        raisedBtn.setToggleGroup(toolBox);
        raisedBtn.setUserData(ToolBoxType.RAISED);
        raisedBtn.setTooltip(new Tooltip("Raised Track Tool"));
        
	// Tunnel track button
        tunnelBtn = new ToggleButton();
        tunnelBtn.setGraphic(new ImageView(new Image("resources/icons/tunnelImg.png")));
        tunnelBtn.setToggleGroup(toolBox);
        tunnelBtn.setUserData(ToolBoxType.TUNNEL);
        tunnelBtn.setTooltip(new Tooltip("Tunnel Button Tool"));
        
	// Bridge track button
        bridgeBtn = new ToggleButton();
        bridgeBtn.setGraphic(new ImageView(new Image("resources/icons/bridgeImg.png")));
        bridgeBtn.setToggleGroup(toolBox);
        bridgeBtn.setUserData(ToolBoxType.BRIDGE);
        bridgeBtn.setTooltip(new Tooltip("Bridge Track Tool"));
    
	// Banked track button
        bankedBtn = new ToggleButton();
        bankedBtn.setGraphic(new ImageView(new Image("resources/icons/bankedImg.png")));
        bankedBtn.setToggleGroup(toolBox);
        bankedBtn.setUserData(ToolBoxType.BANKED);
        bankedBtn.setTooltip(new Tooltip("Banked Track Tool"));
        
	// Slider section
	// Button
        sliderToggleBtn = new ToggleButton("[ ]");	// Couldn't think of an icon so did this
        sliderToggleBtn.setTooltip(new Tooltip("Timeline"));
	// Sllider
        slider = new Slider();
        slider.setMin(0);
        slider.setMax(100);
        slider.setShowTickLabels(false);
        slider.setShowTickMarks(false);
        slider.setBlockIncrement(10);
	// Label
        timeline = new Label("Timeline: ");
    // Container for the slider things
        HBox slideBox = new HBox(timeline, sliderToggleBtn, slider);
        slideBox.setAlignment(Pos.CENTER_LEFT);
        slideBox.setSpacing(4);
        
	// Put everthing together
        HBox toolBoxContainer = new HBox(selectBtn, panBtn, stationBtn,
				junctionBtn, terminalBtn, trackBtn, raisedBtn, tunnelBtn,
				bridgeBtn, bankedBtn, slideBox);
        toolBoxContainer.setPadding(new Insets(8));
        toolBoxContainer.setSpacing(4);
        
        return toolBoxContainer;
    }
    
    /**
     * Initialize the map. Only the barest realization of map functionality
     * has been implemented. Further, the code requires significant cleanup
     * to get rid of stale methods. The reason why more work has not been
     * done on this method and the map object is because map-model integration
     * sucked up all of our time. We STILL have significant bugs in the
     * map-model integration system, though we are making progress.
     */
    //ANTONIO
    private void initMap(){
            //File fd = new File("src/resources/maps.html");
            //mapObject = new MapObject(fd);
            //WebView browser = mapObject.getBrowser();
            
            
            ArcGISMap map = new ArcGISMap(Basemap.createImagery());
            MapView mapView = new MapView();
            map.setInitialViewpoint(new Viewpoint(new Point(-13671170, 5693633, SpatialReference.create(3857)), 57779));

            mapView.setMap(map);

           // mapPane.getChildren().addAll(mapView);
           // mapPane.prefWidthProperty().bind(bindPane.widthProperty());
           // mapPane.prefHeightProperty().bind(bindPane.heightProperty());

            bindPane.getChildren().add(mapView);
            
            initModel();

            root.setCenter(bindPane);

    }
    
    /**
     * This method originally constructed the map and model within a single
     * pane and attached it to the "center" component of our root node. We
     * have since decided to move from doing that to initializing single
     * instances of map-model views as components of individual "projects"
     * created by the end-user, and to load them into tabs within a tab-pane
     * as these projects were created.
     * 
     * Currently, this would require significant revision to the code, and
     * since we have less than 48 hours remaining before our presentation
     * period, we have opted to not aim for perfect optimization quite yet.
     * 
     * This will be done before the beginning of the next semester.
     * 
     */
    
    private void initModel(){
        model = new ModelObject();
        modelTabPane = new TabPane();
        Tab railTab = new Tab();
        Rectangle clip = new Rectangle();

        railPane = new RailPane(model, toolBox, mapObject);
        
        railPane.prefWidthProperty().bind(bindPane.widthProperty());
        railPane.prefHeightProperty().bind(bindPane.heightProperty());
        
        clip.setLayoutX(0);
        clip.setLayoutY(0);
        
        clip.setWidth(railPane.getWidth());
        clip.setHeight(railPane.getHeight());
        
        clip.widthProperty().bind(railPane.prefWidthProperty());
        clip.heightProperty().bind(railPane.heightProperty());
        
        railPane.setClip(clip);
        
        bindPane.getChildren().add(railPane);
        railTab.setContent(bindPane);
        modelTabPane.getTabs().addAll(railTab);
    }
    
    private void initReports(){
        VBox reportColumn = new VBox();
        AnchorPane reportAnchor = new AnchorPane();
        TabPane infoWidget = new TabPane();
        Tab primaryInfoTab = new Tab();
        ScrollPane scrollForLabels = new ScrollPane();
		
        VBox labelOrganizer = new VBox();
        
        Label railStatistics = new Label("Rail Statistics: ");
        Label numStations = new Label("Number of Stations: ");
        Label numJunctions = new Label("Number of Junctions: ");
        Label numTerminals = new Label("Number of Terminals: ");
        Label trackLength = new Label("Track Length(Km): ");
        
        labelOrganizer.getChildren().addAll(railStatistics, numStations, numJunctions, numTerminals, trackLength);
		
        scrollForLabels.setContent(labelOrganizer);
        
		primaryInfoTab.setContent(scrollForLabels);
        
        infoWidget.getTabs().add(primaryInfoTab);
        infoWidget.prefWidthProperty().bind(reportColumn.widthProperty());
        
        TextArea reportBar;
        
        reportBar = new TextArea("Naviron Rails v0.3a");
        reportBar.setPrefColumnCount(20);
        reportBar.prefHeightProperty().set(200);
        reportBar.setScrollLeft(0.0);
        reportBar.setScrollTop(1000);
        reportBar.setWrapText(true);
        
        this.reporter = new ReporterObject(reportBar);
        
        reportAnchor.getChildren().addAll(infoWidget, reportBar);
        AnchorPane.setTopAnchor(infoWidget, 0.0);
        AnchorPane.setBottomAnchor(reportBar, 0.0);
        reportAnchor.prefHeightProperty().bind(reportColumn.heightProperty());
        reportColumn.getChildren().addAll(reportAnchor);
        
        root.setLeft(reportColumn);
    }
    
	// This does nothing right now. No one (Ronald and Jose) can agree on a spash image
    private void initSplash(){
        splash = new ImageView();
    }
    
    private void exitDialogue(){
        Stage prompt = new Stage();
        
        Button yesBtn = new Button("Yes");
        yesBtn.setOnMousePressed(a -> System.exit(1));
        
        Button noBtn = new Button("No");
        noBtn.setOnMousePressed(a -> prompt.close());
        
        HBox btnBox = new HBox(yesBtn, noBtn);
        btnBox.setAlignment(Pos.CENTER);
        btnBox.setPadding(new Insets(5,0,5,0));
        btnBox.setSpacing(10);
        
        VBox box = new VBox(new Text("Do you really want to exit?"), btnBox);
        box.setPadding(new Insets(5,5,5,5));
        
        Scene promptScene = new Scene(box);
        prompt.setScene(promptScene);
		prompt.setAlwaysOnTop(true);
        prompt.show();
    }
    
    private void manageDialogue(String name) {
        Stage manage = new Stage();
		
        Button plus = new Button("", new ImageView("resources/icons/plusSign.png"));
        Button minus = new Button("", new ImageView("resources/icons/minusSign.png"));
        Button up = new Button("", new ImageView("resources/icons/upArrow.png"));
        Button down = new Button("", new ImageView("resources/icons/downArrow.png"));
        Button edit = new Button("", new ImageView("resources/icons/edit.png"));
        Button okay = new Button("Okay");
        
        plus.setPrefSize(40, 40);
        minus.setPrefSize(40, 40);
        up.setPrefSize(40, 40);
        down.setPrefSize(40, 40);
        edit.setPrefSize(40, 40);
        okay.setPrefSize(90, 40);
		okay.setOnMousePressed(a -> manage.close());
        
        TilePane tp = new TilePane(plus, up, minus, down, edit);
        
        tp.setTileAlignment(Pos.CENTER);
        tp.setPrefSize(110, 160);
        tp.setHgap(10);
        tp.setVgap(10);
        tp.setPadding(new Insets(10,0,10,10));
        
        Label l = new Label();
        l.setPrefSize(190, 70);
        ListView lb = new ListView();
        lb.setPrefSize(200, 200);
        
        BorderPane bBox = new BorderPane();
        bBox.setCenter(new VBox(lb));
        //bBox.setCenter(new VBox(l, new Separator(), lb));
        bBox.setRight(new VBox(tp, new StackPane(okay)));
		bBox.setPadding(new Insets(10,0,10,10));
        
        Scene manageScene = new Scene(bBox, 280, 200);
        
        manage.setTitle(name);
        manage.setScene(manageScene);
        manage.setAlwaysOnTop(true);
        manage.setResizable(false);
        manage.show();
    }
    
    private void newProjectDialogue(){
        Stage prompt = new Stage();
        
	// Gridpane hosts the labels and the textfields in this prompt	 
        GridPane textGrid = new GridPane();
        textGrid.setPadding(new Insets(5));
        textGrid.setVgap(8);
        textGrid.setHgap(8);
        
        Label nameLabel = new Label("Project Name: ");
        TextField nameTextField = new TextField();
        Label locationLabel = new Label("Project Location: ");
        TextField locationTextField = new TextField("C:\\Users\\NaviRonRailsProjects\\");
        Button browseButton = new Button("Browse");

	// Can be used if you don't like the default save directory.
		browseButton.setOnAction( e->{
			Stage browsePrompt = new Stage();
			FileChooser fileChooser = new FileChooser();
			fileChooser.showSaveDialog(browsePrompt);
		});
        
	// Alignment of these elements.
        GridPane.setConstraints(nameLabel, 0, 0);
        GridPane.setConstraints(nameTextField, 1, 0);
        GridPane.setConstraints(locationLabel, 0, 1);
        GridPane.setConstraints(locationTextField, 1, 1);
        GridPane.setConstraints(browseButton, 2, 1);
        
        textGrid.getChildren().addAll(nameLabel, nameTextField, locationLabel, locationTextField, browseButton);
        
	// This gridpane is to house the dual columns that is in this prompt.
        GridPane  menuGrid= new GridPane();
        menuGrid.setPadding(new Insets(5));
        menuGrid.setHgap(10);
        menuGrid.setVgap(10);
        
	// The columns are defined.
        menuGrid.setPrefSize(180, 180);
        ColumnConstraints column1 = new ColumnConstraints(100, 100, Double.MAX_VALUE);
        ColumnConstraints column2 = new ColumnConstraints(50);
        ColumnConstraints column3 = new ColumnConstraints(100, 100, Double.MAX_VALUE);
        
	// This is for the two lists, which is going to have data when we have intergrate the backend.
        column1.setHgrow(Priority.ALWAYS);
        column3.setHgrow(Priority.ALWAYS);
        menuGrid.getColumnConstraints().addAll(column1, column2, column3);
        
    // Need to add labels into this.
        Label label1 = new Label("Project Presets");
        GridPane.setHalignment(label1, HPos.CENTER);
        menuGrid.add(label1, 0, 0);
        
        Label label2 = new Label("Presets added.");
        GridPane.setHalignment(label2, HPos.CENTER);
        menuGrid.add(label2, 2, 0);
        
        final ObservableList<String> list1 = FXCollections.observableArrayList("City", "Undeveloped Land",  "Federal Land", "Public Lands", "Private Land", "Foreign Jurisdiction");
        final ListView<String> listView = new ListView<>(list1);
        menuGrid.add(listView, 0, 1);
        
	
	// All you choices will be put into this list. When the buisiness logic behind these presets are finished, they provide lot of relevant information prefilled which can then be changed by the user.
        final ObservableList<String> selected = FXCollections.observableArrayList();
        final ListView<String> heroListView = new ListView<>(selected);
        menuGrid.add(heroListView, 2, 1);
        
        Button sendRightButton = new Button(" > ");
        sendRightButton.setOnAction(e -> {
            String entries = listView.getSelectionModel().getSelectedItem();
            if (entries != null) {
                listView.getSelectionModel().clearSelection();
                list1.remove(entries);
                selected.add(entries);
            }
        });
        
        Button sendLeftButton = new Button(" < ");
        sendLeftButton.setOnAction(e -> {
            String s = heroListView.getSelectionModel().getSelectedItem();
            if (s != null) {
                heroListView.getSelectionModel().clearSelection();
                selected.remove(s);
                list1.add(s);
            }
        });
        
        VBox vbox = new VBox(5);
        vbox.getChildren().addAll(sendRightButton, sendLeftButton);
        vbox.setPrefWidth(50);
        
        menuGrid.add(vbox, 1, 1);
        
        Button applyButton = new Button("Apply");
		applyButton.setOnAction(e -> prompt.close());
	// Why do these two buttons do the same thing? What is their difference?
        Button okButton = new Button("OK");
		okButton.setOnAction(e -> prompt.close());
        
        HBox actionBox = new HBox(5);
        actionBox.setAlignment(Pos.BOTTOM_RIGHT);
        actionBox.setPadding(new Insets(5));
        actionBox.getChildren().addAll(applyButton, okButton);
        
        VBox itemPane = new VBox();
        itemPane.setPadding(new Insets(5));
        itemPane.getChildren().addAll(textGrid, menuGrid, actionBox);

        AnchorPane mainPane = new AnchorPane();
        mainPane.getChildren().addAll(itemPane);
		
        Scene promptScene = new Scene(mainPane);
        prompt.setScene(promptScene);
        prompt.setHeight(325);
        prompt.setResizable(false);
        prompt.setWidth(400);
        prompt.setTitle("New Project");
		prompt.setAlwaysOnTop(true);
        prompt.show();
    }
    
    // Saves user work.
    private void saveAsProcess(){
        Stage prompt = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.showSaveDialog(prompt);
        // prompt.show();
    }
    
	// Open new file
    private void openFileProcess(){
        Stage prompt = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.showOpenDialog(prompt);
    }
    
    // User manual menu item. 
    private void userManualProcess(){
        WebView browser = new WebView();
        WebEngine engine = browser.getEngine();
	// html file that has all the information about the usage of the program.
        File fd = new File("src/resources/usermanual/index.html");
        if(fd.canRead()){
            System.out.println("can read");
        }
        else {
            System.out.println("can't read");
        }
        
        engine.load(fd.toURI().toString());
        Pane pane = new Pane(browser);
        
        Scene promptScene = new Scene(pane);
        Stage prompt = new Stage();
        prompt.setScene(promptScene);
		prompt.setAlwaysOnTop(true);
        prompt.show();
    }
    
    /* The software about page.
     * It will contain software version number and copyright information. 
     * Information about the team members.
     */
    private void AboutDialog() {
        Stage about = new Stage();
		
        VBox pane = new VBox();
        pane.setAlignment(Pos.TOP_CENTER);
        pane.setPadding(new Insets(10,10,10,10));
        pane.setSpacing(10);
        
        ImageView img = new ImageView(new Image("resources/img.png"));	// I picked this --Ronald
        img.setFitHeight(125);
        img.setFitWidth(125);
        
        Label nrr = new Label("Navinron Rails", img);
        nrr.setFont(Font.font("verdana", javafx.scene.text.FontWeight.NORMAL, javafx.scene.text.FontPosture.REGULAR, 40));
        
        Text cp = new Text("Copyright 2018\nNavin Mathews\nRonnie Mullins\nJose Luis Nunez\nAll Rights Reserved");
        cp.setFont(Font.font("verdana", javafx.scene.text.FontWeight.NORMAL, javafx.scene.text.FontPosture.REGULAR, 15));
        
        Button okay = new Button("Okay");
        okay.setOnAction(e -> about.close());
        okay.setPrefSize(125, 50);
        
        pane.getChildren().addAll(nrr, cp, okay);
        
        Scene aboutScene = new Scene(pane, 430, 300);
        
        about.setTitle("About");
        about.setScene(aboutScene);
        about.setAlwaysOnTop(true);
        about.setResizable(false);
        about.show();
    }
    
    private void configureProjectInfoDialogue(){
		Stage info = new Stage();
    // Prepare for the worst naming conventions ever.
	//	There were just too many for me to want to give each a proper name
	//	and they won't be used outside of this method so who cares --Ronald
	
		VBox v = new VBox();
		v.setPadding(new Insets(5,5,5,5));
		
	// Row 1
		Label pn = new Label("Project name:");
		TextField pntf = new TextField();
		
		HBox h1 = new HBox(pn, pntf);
		h1.setAlignment(Pos.CENTER_LEFT);
		h1.setPadding(new Insets(0,0,5,0));
		h1.setSpacing(5);
		
		v.getChildren().add(h1);
		
	// Row 2
		Label tb = new Label("Time begin:");
		TextField tbtf = new TextField();
		tbtf.setPrefWidth(40);
		Label te = new Label("Time end:");
		TextField tetf = new TextField();
		tetf.setPrefWidth(40);
		
		HBox h2 = new HBox(tb, tbtf, te, tetf);
		h2.setAlignment(Pos.CENTER_LEFT);
		h2.setPadding(new Insets(0,0,5,0));
		h2.setSpacing(5);
		
		v.getChildren().add(h2);
		
	// Row 3
		Label tu = new Label("Timeline units:");
		TextField tutf = new TextField();
		tutf.setPrefWidth(40);
		Label tsr = new Label("Timeline step resolutoin:");
		TextField tsrtf = new TextField();
		tsrtf.setPrefWidth(40);
		
		HBox h3 = new HBox(tu, tutf, tsr, tsrtf);
		h3.setAlignment(Pos.CENTER_LEFT);
		h3.setPadding(new Insets(0,0,5,0));
		h3.setSpacing(5);
		
		v.getChildren().add(h3);
		
	// Row 4
		Label ptsd = new Label("Path to survey data:");	// The moment I named this label I broke out laughing.
		TextField ptsdtf = new TextField();				//	I am NOT apologizing or changing it. Fite me --Ronald
		Button b = new Button("Browse");
		b.setPrefWidth(100);
		
		HBox h4 = new HBox(ptsd, ptsdtf, b);
		h4.setAlignment(Pos.CENTER_LEFT);
		h4.setPadding(new Insets(0,0,5,0));
		h4.setSpacing(5);
		
		v.getChildren().add(h4);
		
	// Row 5
		Label oc = new Label("Origin Coordinates:");	// plz dont steal
		TextField octf = new TextField();
		
		HBox h5 = new HBox(oc, octf);
		h5.setAlignment(Pos.CENTER_LEFT);
		h5.setPadding(new Insets(0,0,5,0));
		h5.setSpacing(5);
		
		v.getChildren().add(h5);
		
	// Bottom row
		// Why are these the only ones with actual names?
		//	Because I'm lazy and copy/pasted them from another method
		Button cancel = new Button("Cancel");
		cancel.setPrefWidth(70);
		cancel.setOnAction(e -> info.close());
		
		Button apply = new Button("Apply");
		apply.setPrefWidth(70);
		apply.setOnAction(e -> info.close());
		
		Button okay = new Button("Okay");
		okay.setPrefWidth(70);
		okay.setOnAction(e -> info.close());
		
		HBox h6 = new HBox(cancel, apply, okay);
		h6.setAlignment(Pos.CENTER_RIGHT);
		h6.setSpacing(3);
		
		v.getChildren().add(h6);
		
		Scene resScene = new Scene(v, 400, 175);
		
		info.setTitle("Configure Project Data Things");
		info.setScene(resScene);
		info.setAlwaysOnTop(true);
		info.setResizable(false);
		info.show();  
    }
    
    // The method that generates a text file, with the recommended that are
	//	calculated after all the prototyping is done.
    private void generateReportDialogue(){
        
        Stage report = new Stage();
		
        // Need an achor pane that will house the other two panes.
        AnchorPane mainPane = new AnchorPane();
        Scene reportScene = new Scene(mainPane);

        VBox itemBox = new VBox(7);
        itemBox.setPadding(new Insets(10));
        itemBox.setSpacing(10);
        
        Label nameLabel = new Label("Report Name: ");
        TextField textField = new TextField();
        
        HBox nameContainer = new HBox(7);
        nameContainer.getChildren().addAll(nameLabel, textField);
        
        
        Label typeLabel = new Label("Report Type: ");
        final ComboBox comboBox = new ComboBox();
        comboBox.getItems().addAll(
                "To Be determined",
                "default",
                "default",
                "default",
                "default");
        HBox comboContainer = new HBox(5);
        comboContainer.getChildren().addAll(typeLabel, comboBox);
        
        
        // Checkboxes
        CheckBox cb = new CheckBox("Store in Vault");
	// Will keep track of the changes made.
        CheckBox cb2 = new CheckBox("Mark Corrections? ");
        
        HBox checkBoxContainer = new HBox(5);
        checkBoxContainer.getChildren().addAll(cb, cb2);
        
        Button okButton = new Button("OK");
        okButton.setOnAction(e -> report.close());
        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> report.close());
        
        HBox actionBox = new HBox(5);
        actionBox.setAlignment(Pos.BOTTOM_RIGHT);
        actionBox.getChildren().addAll(okButton, cancelButton);

        itemBox.getChildren().addAll(nameContainer, comboContainer, checkBoxContainer, actionBox);
        mainPane.getChildren().addAll(itemBox);
        
        report.setScene(reportScene);
        report.setTitle("Generate Report");
        report.setHeight(175);
        report.setWidth(300);
		report.setAlwaysOnTop(true);
        report.show();
        
    }
    
    /**
     * A single project can employ multiple different civil systems as part of
     * its operation space. We are ONLY going to implement the railway civil engineering
     * system for the scope of this class and software engineering II. Having said that,
     * the import and manage functionality will be available for proper extension into.
     * 
     * We just will only have one possible importable and manageable civil system, really.
     * 
     */
    private void importCivilSystemDialogue(){
        Stage prompt = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.showOpenDialog(prompt);       
	prompt.setTitle("Import File Systems.");
    }
    
    /**
     * See the documentation for the importCivilSystemDialogue() method.
     */
    
    /**
     * These tabs will have information about the different nodes their railway system has relation to.
     * Most Markers(Railway Station) in out system have ancillary transportation for the last mile.
     */
    private void manageCivilSystemsDialogue(){
        Pane mainPane = new AnchorPane();
        Scene promptScene = new Scene(mainPane);
        
        String[] tabText = new String[]{
            "Railway",
            "Transit",
            "Bus",
            "Air",
            "Subway"
        };
        
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        for (int i = 0; i < tabText.length; i++) {
            Tab tab = new Tab();
            tab.setText(tabText[i]);
            HBox hbox = new HBox();
            hbox.getChildren().add(new Label (tabText[i]));
            hbox.setAlignment(Pos.CENTER);
            tab.setContent(hbox);
            tabPane.getTabs().add(tab);
        }
        
        tabPane.prefWidthProperty().bind(promptScene.widthProperty());
        mainPane.getChildren().add(tabPane);

        Stage prompt = new Stage();
        prompt.setScene(promptScene);
        prompt.setTitle("Manage Civil Systems");
        prompt.setHeight(300);
        prompt.setWidth(350);
		prompt.setAlwaysOnTop(true);
        prompt.show();
        
    }
    
    /**
     * We were completely unable to get to a point where validation of the
     * model was even on our to-do list. Integrating the behavior of the map
     * and the visual representation of the model ended up growing to
     * absorb all of our available development time.
     */
    private void validateCurrentModel(){
        
    }
    
    private void resourceDialogue(String name){
        Stage res = new Stage();
		
        BorderPane pain = new BorderPane();	// Let me have my fun --Ronald
        // Oh, all right. I'll be less brutal about refactoring from this point on,
        //	guys. Your comments will be left in, too. ;^D -- Jose Luis
        BorderPane pane = new BorderPane(pain);
        pane.setPadding(new Insets(5,5,5,5));
        
    // Top in `pane`
        Label l = new Label("Select Civil System");
        ComboBox c = new ComboBox();
        c.setMinWidth(200);
        HBox lc = new HBox(l, c);
        lc.setSpacing(5);
        lc.setAlignment(Pos.CENTER_LEFT);
        
        pane.setTop(lc);
        
    // Top in `pain`
        CheckBox allowMulti = new CheckBox("Allow multi assignment");
        CheckBox hideNon = new CheckBox("Hide non-assigned");
        HBox checks = new HBox(allowMulti, hideNon);
        checks.setAlignment(Pos.BASELINE_CENTER);
        checks.setSpacing(50);
        checks.setPadding(new Insets(5,0,5,0));
        
        pain.setTop(checks);
        
    // Center in `pain`
		// Left list
        ListView leftView = new ListView();
		// Add button
        Button plus = new Button("", new ImageView("resources/icons/plusSign.png"));
        plus.setMinSize(40, 40);
        plus.setTooltip(new Tooltip("Add"));
		
		// Remove button
        Button minus = new Button("", new ImageView("resources/icons/minusSign.png"));
        minus.setMinSize(40, 40);
        minus.setTooltip(new Tooltip("Remove"));
		
		// Move button
        Button arrow = new Button("", new ImageView("resources/icons/leftArrow.png"));
        arrow.setMinSize(40, 40);
        arrow.setTooltip(new Tooltip("Move"));
		
		// Edit button
        Button edit = new Button("", new ImageView("resources/icons/edit.png"));
        edit.setMinSize(40, 40);
        edit.setTooltip(new Tooltip("Edit"));
		
		// Select button
        Button target = new Button("", new ImageView("resources/icons/target.png"));
        target.setMinSize(40, 40);
        target.setTooltip(new Tooltip("Select"));
		
		// Right list
        ListView rightView = new ListView();
        
        VBox v = new VBox(plus, minus, arrow, edit, target);
        v.setSpacing(3);
        
        HBox hv = new HBox(leftView, v, rightView);
        hv.setSpacing(3);
        
        pain.setCenter(hv);
        
    // Bottom in `pane`
        Button cancel = new Button("Cancel");
        cancel.setPrefWidth(70);
        cancel.setOnAction(e -> res.close());
		
        Button apply = new Button("Apply");
        apply.setPrefWidth(70);
        apply.setOnAction(e -> res.close());
		
        Button okay = new Button("Okay");
        okay.setPrefWidth(70);
        okay.setOnAction(e -> res.close());
		
        HBox h = new HBox(cancel, apply, okay);
        h.setAlignment(Pos.CENTER_RIGHT);
        h.setSpacing(3);
        h.setPadding(new Insets(5,0,0,0));
        
        pane.setBottom(h);
        
        Scene resScene = new Scene(pane, 430, 315);
        
        res.setTitle("Manage " + name + " Resources");
        res.setScene(resScene);
        res.setAlwaysOnTop(true);
        res.setResizable(false);
        res.show();
    }
    
    private void sourceBrowserDialogue(String name){
        
    }
    
}
