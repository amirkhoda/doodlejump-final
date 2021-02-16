import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.util.ArrayList;

public class PaneOrganizer {

    public static ArrayList<Platform> platforms = new ArrayList<>();
    public static ArrayList<Hole> holes = new ArrayList<>();

    Image backgroundImg = new Image(getClass().getResourceAsStream("images/background2.png"));

    public static final int BLOCK_SIZE = 68;

    public static Pane appRoot = new Pane();
    public static Pane gameRoot = new Pane();
    public Character player;
    Text scoreText = new Text();
    Text gameoverText = new Text();
    public boolean checkPlayerPos;

    public PaneOrganizer(){
        initContent();
    }

    public void initContent(){
        ImageView background = new ImageView(backgroundImg);
        background.setFitHeight(700);
        background.setFitWidth(450);
        int shift = 650;
        int min = 130;
        int max = 160;
        for (int i = 0; i < 8; i++) { //Зеленые платформы
            shift-=min+(int)(Math.random()*((max-min)+1));
            platforms.add(new Platform(1,(int)(Math.random()*5*BLOCK_SIZE),shift));
        }
        for (int i = 0; i < 4; i++) { //Коричневые платформы
            holes.add(new Hole(1,(int)(Math.random()*5*BLOCK_SIZE),shift));
            shift-=min+(int)(Math.random()*((max-min)+1));
            platforms.add(new Platform(2,(int)(Math.random()*5*BLOCK_SIZE),shift));
        }
        addCharacters(background);
    }

    public void addCharacters(ImageView background) {
        player = new Character(0);
        player.setTranslateX(185);
        player.setTranslateY(650);
        player.translateYProperty().addListener((obs,old,newValue)->{
            checkPlayerPos = false;
            if(player.getTranslateY()<300){
                checkPlayerPos = true;
                for(Platform platform : platforms){
                    platform.setTranslateY(platform.getTranslateY()+0.5);
                    if(platform.getTranslateY()==701){
                        if (platform.id == 2){
                            platform.brownPlatform();
                            platform.setDestroyOnce(false);
                        }
                        platform.setTranslateY(1);
                        platform.setTranslateX(Math.random()*6*BLOCK_SIZE);
                    }
                }
                for(Hole hole : holes){
                    hole.setTranslateY(hole.getTranslateY()+0.5);
                    if(hole.getTranslateY()==701){
                        if (hole.id == 2){
                            hole.brownPlatform();
                            hole.setDestroyOnce(false);
                        }
                        hole.setTranslateY(1);
                        hole.setTranslateX(Math.random()*6*BLOCK_SIZE);
                    }
                }
            }
        });

        gameRoot.getChildren().add(player);
        appRoot.getChildren().remove(gameRoot);
        appRoot.getChildren().addAll(background, gameRoot);
    }

    public void gameOverText(){

        gameRoot.getChildren().remove(gameoverText);
        gameoverText.setText(null);
        gameoverText.setTranslateX(150);
        gameoverText.setTranslateY(330);

        gameoverText.setScaleX(2);
        gameoverText.setScaleY(2);
        gameRoot.getChildren().add(gameoverText);
    }


}
