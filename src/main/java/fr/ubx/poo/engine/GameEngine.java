/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.engine;

import fr.ubx.poo.game.BombSteps;
import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.go.Bomb;
import fr.ubx.poo.view.image.ImageFactory;
import fr.ubx.poo.view.sprite.Sprite;
import fr.ubx.poo.view.sprite.SpriteFactory;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.model.go.character.Player;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static fr.ubx.poo.game.BombSteps.BombA;
import static fr.ubx.poo.game.BombSteps.BombB;
import static fr.ubx.poo.game.BombSteps.BombC;
import static fr.ubx.poo.game.BombSteps.BombD;

public final class GameEngine {

    private static AnimationTimer gameLoop;
    private final String windowTitle;
    private final Game game;
    private final Player player;
    private final List<Sprite> sprites = new ArrayList<>();
    private StatusBar statusBar;
    private Pane layer;
    private Input input;
    private Stage stage;
    private Sprite spritePlayer;
    private Sprite spriteBomb;
    //private Sprite spriteMonster;

    public GameEngine(final String windowTitle, Game game, final Stage stage) {
        this.windowTitle = windowTitle;
        this.game = game;
        this.player = game.getPlayer();

        initialize(stage, game);
        buildAndSetGameLoop();
    }

    private void initialize(Stage stage, Game game) {
        this.stage = stage;
        Group root = new Group();
        layer = new Pane();

        int height = game.getWorld().dimension.height;
        int width = game.getWorld().dimension.width;
        int sceneWidth = width * Sprite.size;
        int sceneHeight = height * Sprite.size;
        Scene scene = new Scene(root, sceneWidth, sceneHeight + StatusBar.height);
        scene.getStylesheets().add(getClass().getResource("/css/application.css").toExternalForm());

        stage.setTitle(windowTitle);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        input = new Input(scene);
        root.getChildren().add(layer);
        statusBar = new StatusBar(root, sceneWidth, sceneHeight, game);
        // Create decor sprites
        game.getWorld().forEach( (pos,d) -> sprites.add(SpriteFactory.createDecor(layer, pos, d)));
        spritePlayer = SpriteFactory.createPlayer(layer, player);

        game.getWorld().setChange(false);
    }

    protected final void buildAndSetGameLoop() {
        gameLoop = new AnimationTimer() {
            public void handle(long now) {
                // Check keyboard actions
                processInput(now);

                // Do actions
                update(now);

                // Graphic update
                render();

                statusBar.update(game);
            }
        };
    }

    private void processInput(long now) {
        if (input.isExit()) {
            gameLoop.stop();
            Platform.exit();
            System.exit(0);
        }
        if (input.isMoveDown()) {
            player.requestMove(Direction.S);
        }
        if (input.isMoveLeft()) {
            player.requestMove(Direction.W);
        }
        if (input.isMoveRight()) {
            player.requestMove(Direction.E);
        }
        if (input.isMoveUp()) {
            player.requestMove(Direction.N);
        }
        if (input.isBomb()) {
            if(player.getNumBomb() > 0) {
                Bomb bomb = new Bomb(game, player.getPosition());
                game.bombList.add(bomb);
                DetonateBomb(bomb);
                game.bombSpriteSetChanged(true);
                player.bombNumDec();

                System.out.println("BOMB : plus que " + player.getNumBomb() + " bombes et vous avez posé " + game.bombList.size());
            }
            else {
                System.out.println("Plus de bombe");
            }
            //layer.requestMove(Direction.N);
        }
        input.clear();
    }

    private void DetonateBomb(Bomb bomb){
        TimerTask updateToStepB = new TimerTask() {
            public void run() {
                bomb.setBombStep(BombB);
                game.bombSpriteSetChanged(true);
            }
        };

        TimerTask updateToStepC = new TimerTask() {
            public void run() {
                bomb.setBombStep(BombC);
                game.bombSpriteSetChanged(true);
            }
        };

        TimerTask updateToStepD = new TimerTask() {
            public void run() {
                bomb.setBombStep(BombD);
                game.bombSpriteSetChanged(true);
            }
        };

        TimerTask Explode = new TimerTask() {
            public void run() {
                bomb.Explode();
                game.bombList.remove(game.bombList.size()-1);
                game.bombSpriteSetChanged(true);
            }
        };

        Timer timer1 = new Timer("Bomb timer 1");
        timer1.schedule(updateToStepB, 1000L);

        Timer timer2 = new Timer("Bomb timer 2");
        timer2.schedule(updateToStepC, 2000L);

        Timer timer3 = new Timer("Bomb timer 3");
        timer3.schedule(updateToStepD, 3000L);

        Timer timerOut = new Timer("Bomb timer final");
        timerOut.schedule(Explode, 4000L);

    }

    private void showMessage(String msg, Color color) {
        Text waitingForKey = new Text(msg);
        waitingForKey.setTextAlignment(TextAlignment.CENTER);
        waitingForKey.setFont(new Font(60));
        waitingForKey.setFill(color);
        StackPane root = new StackPane();
        root.getChildren().add(waitingForKey);
        Scene scene = new Scene(root, 400, 200, Color.WHITE);
        stage.setTitle(windowTitle);
        stage.setScene(scene);
        input = new Input(scene);
        stage.show();
        new AnimationTimer() {
            public void handle(long now) {
                processInput(now);
            }
        }.start();

    }

    private void update(long now) {
        player.update(now);

        if(game.getWorld().hasChanged()){
            System.out.println("Rafraichissement des sprites de décor");
            refreshWorldSprites();
        }

        if(game.bombSpriteHasChanged()){
            System.out.println("Rafraichissement des sprites de bombe");
            refreshBombSprites();
        }

        if (player.isAlive() == false) {
            gameLoop.stop();
            showMessage("Perdu!", Color.RED);
        }
        if (player.isWinner()) {
            gameLoop.stop();
            showMessage("Gagné!", Color.BLUE);
        }
    }

    private void render() {
        sprites.forEach(Sprite::render);
        // last rendering to have player in the foreground
        spritePlayer.render();
    }

    public void start() {
        gameLoop.start();
    }

    private void refreshWorldSprites(){
        sprites.forEach(Sprite::remove);
        sprites.clear();
        game.getWorld().forEach( (pos,d) -> sprites.add(SpriteFactory.createDecor(layer, pos, d)));
        game.getWorld().setChange(false);
        refreshBombSprites();
    }

    private void refreshBombSprites(){
        sprites.forEach(Sprite::remove);
        sprites.clear();
        game.getWorld().forEach( (pos,d) -> sprites.add(SpriteFactory.createDecor(layer, pos, d)));
        if(!game.bombList.isEmpty()){
            for(int i=0; i<game.bombList.size(); i++){
                int finalI = i;
                game.getWorld().forEach( (pos, d) -> sprites.add(SpriteFactory.createBomb(layer, game.bombList.get(finalI))));
            }
        }
        game.bombSpriteSetChanged(false);
    }
}
