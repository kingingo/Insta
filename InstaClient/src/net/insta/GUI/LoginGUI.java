package net.insta.GUI;

import java.net.URL;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import lombok.Getter;
import net.insta.base.event.EventManager;
import net.insta.base.event.events.client.LoginEvent;
import net.insta.base.packets.server.LoginAnswerPacket;
import net.insta.client.Main;

public class LoginGUI extends Application {
	@Getter
	private static LoginGUI instance;

	public static void startServer() {
		Main.init();
		new GUIListener();
	}

	public static void main(String[] args) {
		Application.launch(args);
	}

	class WindowButtons extends HBox {

		public WindowButtons() {
			Button closeBtn = new Button("X");

			closeBtn.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent actionEvent) {
					Platform.exit();
				}
			});

			this.getChildren().add(closeBtn);
		}
	}

	@Getter
	private Button login;
	@Getter
	private Text message;
	@Getter
	private Stage stage;

	@SuppressWarnings("restriction")
	public void start(Stage stage) {
		this.stage = stage;
		this.instance = this;
		// Labels
		Label email = new Label("E-Mail");
		email.setId("white");
		Label password = new Label("Password");
		password.setId("white");
		Hyperlink register = new Hyperlink();
		register.setText("Create Acount");
		this.message = new Text();

		// Textfields
		final TextField tEmail = new TextField();
		final PasswordField tPassword = new PasswordField();

		// Window
		GridPane pane = new GridPane();
		Scene sc = new Scene(pane);
		pane.setId("bg");

		// set css-file
		URL rc = getClass().getResource("/Login.css");
		String s = rc.toExternalForm();
		sc.getStylesheets().add(s);
		stage.setScene(sc);

		// Buttons
		this.login = new Button("Login");
		this.login.setDisable(true);
		pane.setMinSize(400, 200);
		pane.setPadding(new Insets(10, 10, 10, 10));

		pane.setVgap(5);
		pane.setHgap(5);

		pane.setAlignment(Pos.CENTER);

		// Adding Elements to the Gridpane
		pane.add(email, 0, 0);
		pane.add(tEmail, 1, 0);

		pane.add(password, 0, 1);
		pane.add(tPassword, 1, 1);

		pane.add(login, 0, 2);
		pane.add(register, 1, 2);

		pane.add(message, 1, 6);
		this.message.setId("message");

		// Button Methods
		login.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {

				String mail = String.valueOf(tEmail.getText());
				String pass = String.valueOf(tPassword.getText());

				EventManager.callEvent(new LoginEvent(mail, pass));
				login.setDisable(true);

				LoginAnswerPacket packet = GUIAPI.login(mail, pass);

				if (packet.isSuccess()) {
					LoginGUI.getInstance().getMessage().setFill(Color.GREEN);
					LoginGUI.getInstance().getMessage().setText(packet.getMessage());

					stage.hide();
					Stage menuStage = new Stage();
					MainGUI menu = new MainGUI();
					menu.start(menuStage);
					menuStage.show();
				} else {
					LoginGUI.getInstance().getMessage().setFill(Color.FIREBRICK);
					LoginGUI.getInstance().getMessage().setText(packet.getMessage());
					LoginGUI.getInstance().getLogin().setDisable(false);

				}

			}
		});

		register.setOnAction((ActionEvent e) -> {
			getHostServices().showDocument("https://eclipse.org");

		});

		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent t) {
				Main.exit();
			}
		});

		stage.setTitle("Login");

		stage.show();
		startServer();
	}

}
