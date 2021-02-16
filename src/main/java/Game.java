import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;

public class Game extends Application {

    private static Game gameInstance;

    private PaneOrganizer _paneOrganizer = new PaneOrganizer();
    private boolean soundOff = false;
    private HashMap<KeyCode,Boolean> keys = new HashMap<>();

    public static int score = 0;



    private void update(){
        playerControll();
        playerScore();
        checkSide();
        gameOver(false);
    }

    public void gameOver(boolean hitHole){
        if (_paneOrganizer.player.ifFalls()||hitHole){
            _paneOrganizer.gameOverText();
            _paneOrganizer.gameoverText.setText("Game over! press 'Space' to restart");
            _paneOrganizer.scoreText.setTranslateY(300);
            _paneOrganizer.scoreText.setTranslateX(200);
            if(!soundOff){
                new Sound ("/sounds/fall.wav");
                soundOff=true;
            }
            if(isPressed(KeyCode.SPACE)){
                restart();
            }
        }
    }



    private void playerScore() {
        PaneOrganizer.gameRoot.getChildren().remove(_paneOrganizer.scoreText);

        if(_paneOrganizer.checkPlayerPos){
                score+=1;
        }
        _paneOrganizer.scoreText.setText("Score: "+ score);
        _paneOrganizer.scoreText.setTranslateY(30);
        _paneOrganizer.scoreText.setTranslateX(50);
        _paneOrganizer.scoreText.setScaleX(2);
        _paneOrganizer.scoreText.setScaleY(2);
        PaneOrganizer.gameRoot.getChildren().add(_paneOrganizer.scoreText);
    }

    private void checkSide() {
        if(_paneOrganizer.player.getTranslateX()<-59){
            _paneOrganizer.player.setTranslateX(520);
        }else if(_paneOrganizer.player.getTranslateX()>450 && _paneOrganizer.player.getTranslateX()>519){
            _paneOrganizer.player.setTranslateX(-59);
        }
    }

    private void playerControll() {
        if(_paneOrganizer.player.getTranslateY()>=5){
            _paneOrganizer.player.jumpPlayer();
            _paneOrganizer.player.setCanJump(false);
        }
        if(isPressed(KeyCode.LEFT)){
            _paneOrganizer.player.setScaleX(-1);
            _paneOrganizer.player.moveX(-7);
        }
        if(isPressed(KeyCode.RIGHT)){
            _paneOrganizer.player.setScaleX(1);
            _paneOrganizer.player.moveX(7);
        }
        if(_paneOrganizer.player.playerVelocity.getY()<10){
            _paneOrganizer.player.playerVelocity = _paneOrganizer.player.playerVelocity.add(0,1);
        } else _paneOrganizer.player.setCanJump(false);
        _paneOrganizer.player.moveY((int)_paneOrganizer.player.playerVelocity.getY());
    }

    public void restart(){
        PaneOrganizer.platforms.clear();
        PaneOrganizer.holes.clear();
        soundOff=false;
        keys.clear();
        Game.score = 0;
        PaneOrganizer.gameRoot.getChildren().remove(_paneOrganizer.gameoverText);
        PaneOrganizer.gameRoot.getChildren().clear();
        PaneOrganizer.appRoot.getChildren().removeAll(PaneOrganizer.gameRoot);
        PaneOrganizer.gameRoot.getChildren().clear();

        _paneOrganizer.initContent();
    }

    private boolean isPressed(KeyCode key){
        return keys.getOrDefault(key,false);
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setResizable(false);
        Scene scene = new Scene(PaneOrganizer.appRoot,450,700);

        scene.setOnKeyPressed(event-> keys.put(event.getCode(), true));
        scene.setOnKeyReleased(event -> {
            keys.put(event.getCode(), false);
        });
        primaryStage.setTitle("Doodle Jump");
        primaryStage.setScene(scene);
        primaryStage.show();
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
            }
        };
        timer.start();
    }
    public static void main(String[] args) {
        launch(args);
    }


    public static Game getGameInstance() {
        if (gameInstance == null) {
            gameInstance = new Game();
        }
        return gameInstance;
    }
}