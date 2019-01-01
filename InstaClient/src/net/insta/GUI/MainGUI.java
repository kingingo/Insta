package net.insta.GUI;

import java.net.URL;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import lombok.Getter;
import net.insta.GUI.user.User;
import net.insta.base.InstaBase;
import net.insta.base.packets.client.instagram.InstagramTestPacket;
import net.insta.base.packets.server.instagram.InstagramAuthDataAnswerPacket;
import net.insta.base.packets.server.instagram.InstagramTestAnswerPacket;
import net.insta.client.Main;

public class MainGUI extends Application {

	private Comment instaWork;

	@Getter
	private Stage stage;
	public TableView<User> tableUserList = new TableView();
	private TableView<Comment> commentList = new TableView();
	private TableView<Schedule> scheduleList = new TableView();
	

	Label progress = new Label("Progress:");

	ProgressBar progressBar = new ProgressBar(0);
	ProgressIndicator progressIndicator = new ProgressIndicator(0);
	int size;
	float percent;

	@Getter
	private final ObservableList<User> userData = FXCollections.observableArrayList();

	private final ObservableList<Comment> commentData = FXCollections.observableArrayList();
	

	@Getter
	private static MainGUI instance;

	private Button start;
	private Button addList;
	private Button addComment;
	private Button deleteComment;
	private Button clearList;

	public static void main(String[] args) {
		launch(args);
	}

	@Getter
	private Text message;

	public void start(Stage stage) {
		progress.setId("white");
		this.stage = stage;
		this.instance = this;
		stage.setTitle("InstaOnFire");
		Group root = new Group();
		Scene sc = new Scene(root, 800, 700);

		TabPane tab = new TabPane();
		BorderPane pane = new BorderPane();
		pane.setId("bg"); // insta color background

		this.message = new Text();

		// Creating Tabs
		Tab instaLogin = new Tab();
		instaLogin.setText("Instagram Login");

		Tab mainGUI = new Tab();
		mainGUI.setText("Like & Comment");

		Tab plan = new Tab();
		plan.setText("Schedule");

		// Adding Elements to the first Tab
		// Label
		Label instaUser = new Label("Username:");
		Label instaPassword = new Label("Password:");
		Label instaLabel = new Label("Instagram Login");

		instaUser.setId("white");
		instaPassword.setId("white");
		instaLabel.setId("white");

		// Textfields
		final TextField tInstaUser = new TextField();
		final PasswordField tInstaPassword = new PasswordField();

		// Buttons
		Button instaLoginBtn = new Button("Login");
		Hyperlink register = new Hyperlink();
		register.setText("Create Acount");

		GridPane gridpane = new GridPane();

		gridpane.add(instaLabel, 0, 0);

		gridpane.add(instaUser, 0, 1);
		gridpane.add(tInstaUser, 1, 1);

		gridpane.add(instaPassword, 0, 2);
		gridpane.add(tInstaPassword, 1, 2);

		gridpane.add(instaLoginBtn, 0, 3);
		gridpane.add(register, 1, 3);
		gridpane.add(message, 1, 4);
		this.message.setId("instaMessage");

		gridpane.setPadding(new Insets(10, 10, 10, 10));
		gridpane.setVgap(5);
		gridpane.setHgap(5);

		gridpane.setAlignment(Pos.CENTER);
		instaLogin.setContent(gridpane);
		tab.getTabs().add(instaLogin);

		// Adding Elements to second Tab
		// Buttons
		GridPane mainGrid = new GridPane();
		this.addList = new Button("Add List");
		this.clearList = new Button("Clear");
		Button deleteEntry = new Button("X");

		this.start = new Button("Start");
		this.start.setTooltip(new Tooltip("Starts liking and commenting from your list"));

		this.addComment = new Button("Add comments");
		this.deleteComment = new Button("Delete comment");
		TextArea tCommentList = new TextArea();
		TextArea tuser = new TextArea();
		TextArea test = new TextArea();
		

		start.setPrefWidth(400);
		addList.setPrefWidth(400);
		clearList.setPrefWidth(400);
		addComment.setPrefWidth(400);
		deleteComment.setPrefWidth(400);

		// Table
		tableUserList.setEditable(true);
		tableUserList.setId("table");
		commentList.setEditable(true);
		

		TableColumn usernameInsta = new TableColumn("Instagram Username:");
		
		usernameInsta.setCellValueFactory(new PropertyValueFactory<>("usernameInsta"));
		usernameInsta.setMinWidth(200);
		
		TableColumn done = new TableColumn("Done");
		done.setCellValueFactory(new PropertyValueFactory<>("done"));
		done.setMinWidth(100);

		TableColumn deleteUserEntry = new TableColumn("X");
		deleteUserEntry.setCellValueFactory(new PropertyValueFactory<>("x"));
		deleteUserEntry.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<User, Boolean>, ObservableValue<Boolean>>() {
					@Override
					public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<User, Boolean> features) {
						return new SimpleBooleanProperty(features.getValue() != null);
					}
				});

		deleteUserEntry.setCellFactory(new Callback<TableColumn<User, Boolean>, TableCell<User, Boolean>>() {
			@Override
			public TableCell<User, Boolean> call(TableColumn<User, Boolean> personBooleanTableColumn) {
				return new DeleteCom(stage, tableUserList);
			}
		});
		// deleteTableEntry.setMinWidth(85);

		tableUserList.setItems(userData);
		tableUserList.getColumns().addAll(usernameInsta, done, deleteUserEntry);
		

		TableColumn comments = new TableColumn("Comments");
		comments.setCellValueFactory(new PropertyValueFactory<>("comments"));
		comments.setMinWidth(300);

		TableColumn deleteComments = new TableColumn("X");
		deleteComments.setCellValueFactory(new PropertyValueFactory<>("x"));
		deleteComments.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Comment, Boolean>, ObservableValue<Boolean>>() {
					@Override
					public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Comment, Boolean> features) {
						return new SimpleBooleanProperty(features.getValue() != null);
					}
				});

		deleteComments.setCellFactory(new Callback<TableColumn<Comment, Boolean>, TableCell<User, Boolean>>() {
			@Override
			public TableCell<User, Boolean> call(TableColumn<Comment, Boolean> personBooleanTableColumn) {
				return new DeleteCom(stage, commentList);
			}
		});

		commentList.setItems(commentData);
		commentList.getColumns().addAll(comments, deleteComments);

		final VBox userBox = new VBox();
		final VBox commentBox = new VBox();
		final VBox tUser = new VBox();
		
		final VBox fTest = new VBox();
		
		VBox box = new VBox(test);
		box.setMaxWidth(400);
		
		VBox userlistBox = new VBox(tuser);
		userlistBox.setMaxWidth(400);
		
		VBox vCommentBox = new VBox(tCommentList);
		vCommentBox.setMaxWidth(400);

		// Spacing from top
		userBox.setSpacing(10);
		commentBox.setSpacing(10);
	
		

		// Progressbar
		final Label statusLabel = new Label();
		statusLabel.setTextFill(Color.BLUE);
		FlowPane pbar = new FlowPane();
		pbar.setPadding(new Insets(10));
		pbar.setHgap(10);
		pbar.setMaxWidth(300);

		pbar.getChildren().addAll(progress, progressBar, progressIndicator);

		final Label instagramLabel = new Label("List of usernames: ");
		userBox.getChildren().addAll(instagramLabel, tableUserList);
		mainGrid.add(userBox, 0, 0);
		mainGrid.add(box, 0, 1);
		mainGrid.add(addList, 0, 2);
		mainGrid.add(clearList, 0, 3);

		final Label commentLabel = new Label("Comments: ");
		commentBox.getChildren().addAll(commentLabel, commentList);
		mainGrid.add(commentBox, 1, 0);
		mainGrid.add(vCommentBox, 1, 1);
		mainGrid.add(addComment, 1, 2);
		mainGrid.add(deleteComment, 1, 3);
	

		mainGrid.setPadding(new Insets(10, 10, 10, 10));
		mainGrid.setVgap(5);
		mainGrid.setHgap(5);

		VBox buttonBar = new VBox();
		buttonBar.setPadding(new Insets(10, 50, 50, 50));
		buttonBar.setSpacing(10);
		ObservableList list = buttonBar.getChildren();
		list.addAll(start, pbar);
		buttonBar.setAlignment(Pos.BOTTOM_CENTER);

		BorderPane bottom = new BorderPane();
		bottom.setCenter(mainGrid);
		bottom.setBottom(buttonBar);
		mainGUI.setContent(bottom);
		tab.getTabs().add(mainGUI);

		pane.setCenter(tab);
		pane.prefHeightProperty().bind(sc.heightProperty());
		pane.prefWidthProperty().bind(sc.widthProperty());

		tab.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
		toogleLikeAndComment();

		// Adding Elements to the third Tab
		// Buttons
		Button upload = new Button("Upload & Plan your posts");
	
		GridPane schedule = new GridPane();
		
		
		// Table
		TableColumn image = new TableColumn("Image");
		image.setMinWidth(200);
		image.setCellValueFactory(new PropertyValueFactory<>("Image"));

		TableColumn description = new TableColumn("Description");
		description.setCellValueFactory(new PropertyValueFactory<>("Description"));
		description.setMinWidth(100);
		
		final VBox scheduleBox = new VBox();
		

		VBox sBox = new VBox(tuser);
		sBox.setMaxWidth(400);
		

		// Spacing from top
		userBox.setSpacing(10);
		commentBox.setSpacing(10);
		
		
		scheduleList.getColumns().addAll(image, description);

		schedule.setPadding(new Insets(10, 10, 10, 10));
		schedule.setVgap(5); 
		schedule.setHgap(5);

		schedule.setAlignment(Pos.CENTER);
		plan.setContent(schedule);
		tab.getTabs().add(plan);
		schedule.add(scheduleBox, 0, 0);
		schedule.add(upload, 0, 1);

		// Button Methods
		instaLoginBtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {

				String username = String.valueOf(tInstaUser.getText());
				String pass = String.valueOf(tInstaPassword.getText());

				InstagramAuthDataAnswerPacket packet = GUIAPI.authData(username, pass);

				if (packet.isSuccess()) {
					instaLoginBtn.setDisable(true);
					getMessage().setFill(Color.GREEN);
					getMessage().setText(packet.getMessage());
					instance.toogleLikeAndComment();

				} else {
					getMessage().setFill(Color.FIREBRICK);
					getMessage().setText(packet.getMessage());

				}
			}
		});

		register.setOnAction((ActionEvent e) -> {
			getHostServices().showDocument("https://instagram.com");

		});

		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent t) {
				Main.exit();
			}
		});

		addList.setOnAction((ActionEvent e) -> {
			
			String usernames = test.getText();
			usernames = usernames.replaceAll(" ", "").replaceAll("\n", "");
			String[] tableUsers = usernames.split("@");

			for (int i = 0; i < tableUsers.length; i++) {

				if (!tableUsers[i].isEmpty() && !contains(tableUsers[i])) {
					userData.add(new User(tableUsers[i], "X"));
				}

			}
			size = userData.size();
			percent = 1.0f / size;
		});

		addComment.setOnAction((ActionEvent e) -> {
			String x = tCommentList.getText();
			String[] commentsList = x.split(";");

			for (int i = 0; i < commentsList.length; i++)
				commentData.add(new Comment(commentsList[i]));
		});

		start.setOnAction((ActionEvent e) -> {
			if (this.userData.isEmpty()) {
				InstaBase.printf("Can't start because the User List is Empty!?");
			} else if (this.commentData.isEmpty()) {
				InstaBase.printf("Can't start because the Comment List is Empty!?");
			} else {
				InstaBase.printf("Bot Started");
				toogleLikeAndComment();

				GUIAPI.sendList(false, cloneUserdata(), cloneComments());
			}
		});

		clearList.setOnAction((ActionEvent e) -> {
			tableUserList.getItems().clear();
		});

		deleteComment.setOnAction((ActionEvent e) -> {
			commentList.getItems().removeAll(commentList.getSelectionModel().getSelectedItems());

		});

		// To-Do --> Create a CSS-File
		URL rc = getClass().getResource("/Login.css");
		String s = rc.toExternalForm();
		sc.getStylesheets().add(s);
		stage.setScene(sc);

		root.getChildren().add(pane);
		stage.setScene(sc);
		stage.show();

	}

	private class DeleteCom extends TableCell<User, Boolean> {

		final Button deleteBtn = new Button("X");

		// centers the button in the cell.
		final StackPane paddedButton = new StackPane();

		DeleteCom(final Stage stage, final TableView table) {
			paddedButton.setPadding(new Insets(3));
			paddedButton.getChildren().add(deleteBtn);

			// TO-DO --> Remove the row properly
			deleteBtn.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent actionEvent) {
					commentList.getItems().removeAll(commentList.getSelectionModel().getSelectedItems());
					tableUserList.getItems().removeAll(tableUserList.getSelectionModel().getSelectedItems());
				}
			});
		}

		/** Only buttons if row !is.Empty(). */
		@Override
		protected void updateItem(Boolean item, boolean empty) {
			super.updateItem(item, empty);
			if (!empty) {
				setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
				setGraphic(paddedButton);
			} else {
				setGraphic(null);
			}
		}
	}

	public ArrayList<String> cloneComments() {
		ArrayList<String> list = new ArrayList<>();
		for (Comment c : this.commentData) {
			list.add(c.getComments());
		}
		return list;
	}

	public ArrayList<String> cloneUserdata() {
		ArrayList<String> list = new ArrayList<>();
		for (User user : this.userData) {
			list.add(user.getInstaUser());
		}
		return list;
	}

	public boolean contains(String username) {
		for (User user : userData) {
			if (user.getInstaUser().equalsIgnoreCase(username))
				return true;
		}
		return false;
	}

	public float getSize() {
		return percent;
	}

	public boolean toogleLikeAndComment() {
		boolean disable = !this.start.isDisable();

		this.start.setDisable(disable);
		this.addComment.setDisable(disable);
		this.deleteComment.setDisable(disable);
		this.addList.setDisable(disable);
		this.clearList.setDisable(disable);

		return disable;
	}

}
