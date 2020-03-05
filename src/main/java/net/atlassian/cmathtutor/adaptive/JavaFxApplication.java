package net.atlassian.cmathtutor.adaptive;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.atlassian.cmathtutor.adaptive.fxcontroller.MainSceneController;
import net.rgielen.fxweaver.core.FxWeaver;

public class JavaFxApplication extends Application {

    private ConfigurableApplicationContext applicationContext;

    @Override
    public void init() throws Exception {
	String[] args = getParameters().getRaw().toArray(new String[0]);
	this.applicationContext = new SpringApplicationBuilder()
		.sources(AdaptiveTestingApplication.class)
		.run(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
	FxWeaver fxWeaver = applicationContext.getBean(FxWeaver.class);
	Parent root = fxWeaver.loadView(MainSceneController.class);
	Scene scene = new Scene(root);
	primaryStage.setScene(scene);
	primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
	this.applicationContext.close();
	Platform.exit();
    }
}
