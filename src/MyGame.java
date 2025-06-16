import javafx.animation.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.File;
import java.util.Random;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

//boundingbox intersect


public class MyGame extends Application { // (FULLSCREEN)
    // JavaFX related objects
    Group rootViewContainer;
    Canvas gameCanvas;
    GraphicsContext graphicsContext;
    EventHandler<KeyEvent> keyPressed;
    EventHandler<KeyEvent> keyReleased;
    AnimationTimer timer;


    // Your game objects
    GameObject player;
    ArrayList<GameObject> obstacles;
    GameObject platform;


    // Your game parameters
    final int GAME_WIDTH = 1500;
    final int GAME_HEIGHT = 600;

    int score = 0;
    int gameLevel = 1;
    int levelSpeed = -4;


    boolean jumping = false;
    boolean jumpDown = false; // while jumping if Down pressed.

    boolean gameOver = false;

    double gravitySimulator;


    double velocity = 50;

    double trySeconds = 10;
    double trySeconds2 = 0;

    long tempNow;


    //Image imageBird1;
    //Image imageBird2;

    // Image Files
    Image imageBird1 = new Image("res/bird1.png");
    Image imageBird2 = new Image("res/bird2.png");
    Image imagePlayer = new Image("res/player.png");
    Image imagePlayer2 = new Image("res/player2.png");
    Image imagePlayerDuck = new Image("res/dinoDuck.png");
    Image imagePlayerDuck2 = new Image("res/dinoDuck2.png");


    Image imageCactus = new Image("res/cactus.png");
    Image imageCactusBig = new Image("res/cactus_big.png");

    Image imagePlatform = new Image("res/platform.png");

    // Sound Files
    Media sound1 = new Media(new File("Sounds/Starting_Sound.mp3").toURI().toString());
    Media sound2 = new Media(new File("Sounds/jumpSound.mp3").toURI().toString());
    Media sound3 = new Media(new File("Sounds/AnnoyingMusic.mp3").toURI().toString());
    Media sound4 = new Media(new File("Sounds/coinSound.mp3").toURI().toString());
    Media sound5 = new Media(new File("Sounds/gameOver.mp3").toURI().toString());

    MediaPlayer mediaStart = new MediaPlayer(sound1);
    MediaPlayer mediaJump = new MediaPlayer(sound2);
    MediaPlayer mediaMusic = new MediaPlayer(sound3);  // the music will probably start annoying the player after 1 minute. Can be muted via key M
    MediaPlayer mediaCoin = new MediaPlayer(sound4);

    MediaPlayer mediaGameOver = new MediaPlayer(sound5);

    //  timer.stop();
    //                    Platform.exit();
    Text scoreText = new Text(String.format("Score: %d", score));
    Text levelText = new Text(String.format("Level: %d", gameLevel));


    /**
     * Launch a JavaFX application.
     *
     * @param args
     */
    public static void main(String[] args) {
        launch(args);

    }

    /**
     * Start the game scene with defined GAME_WIDTH and GAME_HEIGHT.
     *
     * @param primaryStage the primary stage for this application, onto which
     *                     the application scene can be set.
     */
    @Override
    public void start(Stage primaryStage) {


        mediaStart.setVolume(0.4);
        mediaStart.play();

        mediaMusic.setVolume(0.2);
        mediaMusic.play();


        rootViewContainer = new Group();
        rootViewContainer.getChildren().removeAll();


        initGame();

        Scene jScene = new Scene(rootViewContainer, GAME_WIDTH, GAME_HEIGHT, Color.LIGHTBLUE);
        primaryStage.setTitle("HUBBM-Dino");
        Image icon = new Image("res/myPreciousDino.png");

        primaryStage.getIcons().add(icon);
        primaryStage.setResizable(false);
        primaryStage.setScene(jScene);
        // primaryStage.setFullScreen(true);

        primaryStage.show();

    }

    /**
     * Init game objects and parameters like key event listeners, timers etc.
     */
    public void initGame() {


        // Generate a game canvas where all your objects, texts etc. will be drawn.
        rootViewContainer.getChildren().clear();
        gameCanvas = new Canvas(GAME_WIDTH, GAME_HEIGHT);
        graphicsContext = gameCanvas.getGraphicsContext2D();
        rootViewContainer.getChildren().add(gameCanvas);


        // Mute the music
        Text mutePropertyText = new Text("Press M to mute/unmute the Music.");
        mutePropertyText.setFont(Font.font("Courier New", 16));
        mutePropertyText.setFill(Color.BLACK);
        mutePropertyText.setX(GAME_WIDTH - 330);
        mutePropertyText.setY(15);
        rootViewContainer.getChildren().add(mutePropertyText);


        // EXIT THE GAME
        Text exitText = new Text("Press esc to exit.");
        exitText.setFont(Font.font("Courier New", 16));
        exitText.setFill(Color.BLACK);
        exitText.setX(GAME_WIDTH - 250);
        exitText.setY(30);


        rootViewContainer.getChildren().add(exitText);


        scoreText.setFont(Font.font("Verdana", 24));
        scoreText.setFill(Color.RED);
        scoreText.setX(0);
        scoreText.setY(24);

        levelText.setFont(Font.font("Verdana", 24));
        levelText.setFill(Color.RED);
        levelText.setX(0);
        levelText.setY(48);

        rootViewContainer.getChildren().addAll(scoreText, levelText);


        // TODO: Init game objects and parameters like key event listeners, timers etc.




        platform = new GameObject(imagePlatform, 0, GAME_HEIGHT - imagePlatform.getHeight() / 4, GAME_WIDTH, imagePlatform.getHeight() / 4);


        player = new GameObject(imagePlayer, 0, (GAME_HEIGHT - imagePlatform.getHeight() / 4) - imagePlayer.getHeight(), imagePlayer.getWidth(), imagePlayer.getHeight());




        double initialYofPlayer = GAME_HEIGHT - imagePlatform.getHeight(); // initial Y position of the player


        obstacles = new ArrayList<>();


        Random rand = new Random();
        //                     rangeMin + (rangeMax - rangeMin)
        double randomDouble = (200) + (GAME_HEIGHT - (platform.getHeight()) - 200) * rand.nextDouble();

        double random = ThreadLocalRandom.current().nextDouble(200, GAME_HEIGHT - ((platform.getHeight() / 4) + 100));


        //double xOfBird1 = Math.random() * GAME_WIDTH;
        double xOfBird = GAME_WIDTH + imageBird2.getWidth();  // initializing the bird outside the game canvas.




        //double yOfBird = randomDouble;
        double yOfBird = random;


        obstacles.add(new Bird(imageBird1, imageBird2, xOfBird, yOfBird, imageBird2.getWidth(), imageBird2.getHeight()));
        obstacles.add(new Cactus(imageCactus, GAME_WIDTH + imageCactus.getWidth() + 250, GAME_HEIGHT - imagePlatform.getHeight() - 25, imageCactus.getWidth(), imageCactus.getHeight()));
        obstacles.add(new Cactus(imageCactusBig, GAME_WIDTH + imageCactusBig.getWidth()+ 550, GAME_HEIGHT - imagePlatform.getHeight() - 25, imageCactusBig.getWidth(), imageCactusBig.getHeight()));


        platform.render(graphicsContext);






        initKeyEventListeners();

        initTimer();
        timer.start();



    }

    /**
     * keyPressed and keyReleased are the two main keyboard event listener objects. You can check which keyboard
     * keys are pressed or released by means of this two objects and make appropriate changes in your game.
     */
    void initKeyEventListeners() {


        keyPressed = event -> {


            switch (event.getCode()) {


                case UP:
                case SPACE: {

                    if (!gameOver) {




                        if (player.getY() == player.getInitialY()) {

                            jumping = true;

                            mediaJump.setVolume(0.5);
                            mediaJump.play();
                            mediaJump.seek(mediaJump.getStartTime());  // this method rewinds the sound back, so that it can be playable again.

                        }



                    }

                    break;
                }


                case ESCAPE:

                    Platform.exit();

                    timer.stop();
                    //timer.start();


                    break;

                case DOWN:

                    if (!gameOver) {
                        // jumping &&
                        if (jumping) {


                            // player.setY(player.getInitialY());
                            jumping = false;
                            jumpDown = true;


                        }


                        graphicsContext.clearRect(player.getX() - 5, player.getY(), player.getWidth() + 10, player.getHeight());

                        player.setImage(imagePlayerDuck);
                        player.setWidth(imagePlayerDuck.getWidth());
                        player.setHeight(imagePlayerDuck.getHeight());

                        player.setY((GAME_HEIGHT - imagePlatform.getHeight() / 4) - imagePlayerDuck.getHeight());


                        break;
                    }
                    break;
                case LEFT:
                    if (!gameOver) {

                        // if the player is in the stage and not jumping
                        if (!(player.getX() <= 0) && (player.getY() == player.getInitialY())) {

                            player.changeApses(-13);

                        }

                        break;
                    }
                    break;

                case RIGHT:

                    if (!gameOver) {
                        // if the player is in the stage and not jumping
                        if (!(player.getX() >= GAME_WIDTH - player.getWidth()) && (player.getY() == player.getInitialY())) {

                            player.changeApses(13);


                        }

                        break;
                    }
                    break;

                case M:

                    // didn't use if !gameOver block intentionally in case the player wants to listen to it.

                    if (mediaMusic.isMute()) {

                        mediaMusic.setMute(false);

                    } else {
//
                        mediaMusic.setMute(true);
                    }
                    break;



                case ENTER:


                    if (gameOver) {
                        timer.stop();
                        initGame();
                        gameOver = false;
                        mediaMusic.play();


                        player.setY(player.getInitialY());
                        player.setX(0);


                        break;
                    }

                    break;

            }

        };

        //  released = true;
        keyReleased = event -> {
            // System.out.println(event.getCode() + " BIRAKTIN");

            switch (event.getCode()) {

                case UP:
                case SPACE: {
                    if (!gameOver) {
                        //player.setOrdinate(+gravitySimulator);
                        //  graphicsContext.clearRect(player.getX() - 3, player.getY(), player.getWidth() + 5, player.getHeight());
//
                        //  //player.setOrdinate(+180);



                        //  player.setOrdinate(+gravity);


                    }

                }
                break;
                case DOWN:

                    if (!gameOver) {


                        player.setImage(imagePlayer);
                        player.setWidth(imagePlayer.getWidth());
                        player.setHeight(imagePlayer.getHeight());
                        player.setY((GAME_HEIGHT - imagePlatform.getHeight() / 4) - imagePlayer.getHeight());

                    }
                    break;

                case LEFT:

                    break;

                case RIGHT:

                    break;
                // Detect the key via its code like LEFT, RIGHT, UP, DOWN, ENTER etc.
            }
        };

        // gameCanvas.setOnKeyPressed(keyPressed);

        gameCanvas.setOnKeyPressed(keyPressed);
        gameCanvas.setOnKeyReleased(keyReleased);

        gameCanvas.setFocusTraversable(true);


    }


    /**
     * This timer object will call the handle() method at every frame. So, in this method's body, you can
     * redraw your objects to make a movement effect, check whether any of your objects collided or not,
     * and update your game score etc. This is the main lifecycle of your game.
     */


    void initTimer() {


        double g = 0.2;


        timer = new AnimationTimer() {


            @Override
            public void handle(long now) {

                tempNow = now;



                scoreText.setText(String.format("Score %d", score));


                int currentLevel = (score / 10) + 1;

                levelText.setText(String.format("Level %d", currentLevel));


                if (currentLevel > gameLevel) {
                    gameLevel++;

                    levelSpeed -= 1;   // Animation speed with respect to level

                    mediaCoin.play();
                    mediaCoin.seek(mediaCoin.getStartTime()); // this method rewinds the sound back, so that it can be playable again.
                }



                // if  (keyPressed.handle(KeyEvent ));




                double seconds = (int) ((now / 1e9) % 5);
                double timeToFlapWings = now / 1e9 % 1.2;


                double timeToPace = now / 1e9 % 0.2;



                if (jumping) {

                    graphicsContext.clearRect(player.getX() - 8, player.getY(), player.getWidth() + 15, player.getHeight());

                    // System.out.println(seconds + " before");
                    // timer.stop();
                    // timer.start();
                    // System.out.println(seconds + " AFTER");


                    // change in X coordinate
                    if (velocity > 0) {


                        double gravitySimulator2 = 0.5 * g * trySeconds * trySeconds;

                        gravitySimulator = 0.8 * g * trySeconds * trySeconds;


                        // gravitySimulator = 12.3 125;
                        //gravitySimulator = 13.3125;


                        velocity -= (g * trySeconds);


                        player.changeOrdinate(-gravitySimulator);


                        trySeconds -= 0.1;


                    } else if (velocity <= 0) {


                        gravitySimulator = 0.8 * g * trySeconds2 * trySeconds2;


                        velocity -= (g * trySeconds);

                        player.changeOrdinate(+gravitySimulator);

                        trySeconds2 += 0.15;


                        if ((((player.getInitialY())) - player.getY() <= 2) && (((player.getInitialY())) - player.getY() >= 0)) {


                            player.setY(player.getInitialY());


                            jumping = false;
                            trySeconds = 10;
                            trySeconds2 = 0;

                            velocity = 50;


                        }


                    }


                } else if (jumpDown) {

                    graphicsContext.clearRect(player.getX() - 8, player.getY(), player.getWidth(), player.getHeight());

                    player.setY(player.getInitialY());


                    if (player.getY() == player.getInitialY()) {

                        graphicsContext.clearRect(player.getX() - 8, player.getY(), player.getWidth(), player.getHeight());

                    }
                    trySeconds = 10;
                    trySeconds2 = 0;
                    gravitySimulator = 0;
                    velocity = 50;
                    jumpDown = false;


                } else {
                    graphicsContext.clearRect(player.getX() - 8, player.getY(), player.getWidth() + 15, player.getHeight());

                }


                if (player.getImage() == imagePlayerDuck) {

                    graphicsContext.clearRect(player.getX() - 8, player.getY(), player.getWidth() + 15, player.getHeight());


                    if (timeToPace < 0.1) {


                        player.setImage(imagePlayerDuck2);
                        player.setWidth(imagePlayerDuck2.getWidth());
                        player.setHeight(imagePlayerDuck2.getHeight());

                        player.setY((GAME_HEIGHT - imagePlatform.getHeight() / 4) - imagePlayerDuck2.getHeight());

                    }


                }


                for (GameObject gameObject : obstacles) {

                    // GAME OVER CONTROL
                    Rectangle2D playerRect = new Rectangle2D(player.getX(),player.getY(),player.getWidth(),player.getHeight());
                    Rectangle2D obstacleRect = new Rectangle2D(gameObject.getX(),gameObject.getY(),gameObject.getWidth(),gameObject.getHeight());

                    if ( playerRect.intersects(obstacleRect)) {
                        Text gameOverText = new Text("GAME OVER!");
                        gameOverText.setFont(Font.font("Impact", 36));
                        gameOverText.setFill(Color.DARKRED);
                        gameOverText.textAlignmentProperty();
                        gameOverText.setX(GAME_WIDTH/2 - 10);
                        gameOverText.setY(100);
                        rootViewContainer.getChildren().add(gameOverText);

                        Text lastScoreText = new Text(String.format("Your Score: %d", score));
                        lastScoreText.setFont(Font.font("Impact", 36));
                        lastScoreText.setFill(Color.DARKRED);
                        lastScoreText.textAlignmentProperty();
                        lastScoreText.setX(GAME_WIDTH/2 - 15);
                        lastScoreText.setY(150);
                        rootViewContainer.getChildren().add(lastScoreText);

                        Text pressEnterText = new Text("Press ENTER to restart!");
                        pressEnterText.setFont(Font.font("Impact", 36));
                        pressEnterText.setFill(Color.DARKRED);
                        pressEnterText.setX(GAME_WIDTH/2 - 60);
                        pressEnterText.setY(250);
                        rootViewContainer.getChildren().add( pressEnterText);

                        mediaGameOver.play();
                        mediaGameOver.seek(mediaGameOver.getStartTime());
                        mediaMusic.stop();



                        jumping = false;
                        trySeconds = 10;
                        trySeconds2 = 0;
                        velocity = 50;
                        levelSpeed = -2;


                        gameLevel = 0;
                        score = 0;

                        gameOver = true;
                        timer.stop();

                    }




                    if (gameObject instanceof Bird) {


                        if (gameObject.getX() < -gameObject.getWidth()) {
                            score++;

                            gameObject.changeApses(GAME_WIDTH + gameObject.getWidth());
                            //obstacles.remove(gameObject);


                        }

                        if (timeToFlapWings < 0.6) {
                            graphicsContext.clearRect(gameObject.getX(), gameObject.getY(), gameObject.getWidth(), gameObject.getHeight());
                            gameObject.changeApses(levelSpeed);
                            ((Bird) gameObject).renderBird(graphicsContext, imageBird1);

                        } else if (timeToFlapWings >= 0.6) {
                            graphicsContext.clearRect(gameObject.getX(), gameObject.getY(), gameObject.getWidth(), gameObject.getHeight());
                            gameObject.changeApses(levelSpeed);
                            ((Bird) gameObject).renderBird(graphicsContext, imageBird2);
                        }

                    } else if (gameObject instanceof Cactus) {


                            if (gameObject.getX() < -gameObject.getWidth()) {
                                score++;
                                gameObject.changeApses(GAME_WIDTH + gameObject.getWidth());

                               //obstacles.remove(gameObject);   // I get multiple errors when I try to remove objects. (ConcurrentModificationException

                               //obstacles.add(new Cactus(imageCactusBig, GAME_WIDTH + imageCactusBig.getWidth(), GAME_HEIGHT - imagePlatform.getHeight() - 25, imageCactusBig.getWidth(), imageCactusBig.getHeight()));
                                //obstacles.add(new Cactus(imageCactus, GAME_WIDTH + imageCactus.getWidth() + 100, GAME_HEIGHT - imagePlatform.getHeight() - 25, imageCactus.getWidth(), imageCactus.getHeight()));



                            }

                            graphicsContext.clearRect(gameObject.getX(), gameObject.getY(), gameObject.getWidth(), gameObject.getHeight());
                            gameObject.changeApses(levelSpeed);
                            gameObject.render(graphicsContext);


                    }

                }


                player.render(graphicsContext);
            }

        };


    }


}




