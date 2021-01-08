/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.engine;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.decor.Decor;
import fr.ubx.poo.model.go.Bomb;
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

public final class GameEngine {

    private static AnimationTimer gameLoop;
    private final String windowTitle;
    private final Game game;
    private final Player player;
    public List<Bomb> bombList = new ArrayList<>();
    private final List<Sprite> sprites = new ArrayList<>();
    private StatusBar statusBar;
    private Pane layer;
    private Input input;
    private Stage stage;
    private Sprite spritePlayer;
    private boolean explosionCreated = false;

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
                Bomb bomb = new Bomb(game, player.getPosition(), now);
                bombList.add(bomb);
                player.bombNumDec();
            }
            else {
                System.out.println("Plus de bombe");
            }
        }
        input.clear();
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

    private void update(long now){
        player.update(now);

        if(!bombList.isEmpty()) {
            for (int i = 0; i < bombList.size(); i++) {
                bombList.get(i).update(now);
                if(bombList.get(i).destroyed[0]){
                    Position bombPos = bombList.get(i).getPosition();

                    for(int t=0; t<bombList.get(i).spriteRange[0]+1; t++){
                        Position iterPosT = new Position(bombPos.x, bombPos.y-t);
                        Decor decorPosT = game.getWorld().get(iterPosT);
                        if(game.getWorld().isInside(iterPosT)) {
                            if (game.getWorld().isEmpty(iterPosT) || decorPosT.isBreakable()) {
                                Sprite explosionT = SpriteFactory.createExplosion(layer, iterPosT);
                                sprites.add(explosionT);
                            }
                        }
                    }

                    for(int b=0; b<bombList.get(i).spriteRange[1]+1; b++){
                        Position iterPosB = new Position(bombPos.x, bombPos.y+b);
                        Decor decorPosB = game.getWorld().get(iterPosB);
                        if(game.getWorld().isInside(iterPosB)) {
                            if (game.getWorld().isEmpty(iterPosB) || decorPosB.isBreakable()) {
                                Sprite explosionB = SpriteFactory.createExplosion(layer, iterPosB);
                                sprites.add(explosionB);
                            }
                        }
                    }

                    for(int l=0; l<bombList.get(i).spriteRange[2]+1; l++){
                        Position iterPosL = new Position(bombPos.x-l, bombPos.y);
                        Decor decorPosL = game.getWorld().get(iterPosL);
                        if(game.getWorld().isInside(iterPosL)) {
                            if (game.getWorld().isEmpty(iterPosL) || decorPosL.isBreakable()) {
                                Sprite explosionL = SpriteFactory.createExplosion(layer, iterPosL);
                                sprites.add(explosionL);
                            }
                        }

                    }

                    for(int r=0; r<bombList.get(i).spriteRange[3]+1; r++){
                        Position iterPosR = new Position(bombPos.x+r, bombPos.y);
                        Decor decorPosR = game.getWorld().get(iterPosR);
                        if(game.getWorld().isInside(iterPosR)) {
                            if (game.getWorld().isEmpty(iterPosR) || decorPosR.isBreakable()) {
                                Sprite explosionR = SpriteFactory.createExplosion(layer, iterPosR);
                                sprites.add(explosionR);
                            }
                        }
                    }

                }

                refreshBombSprites();

                if(bombList.get(i).destroyed[1]) {
                    game.getWorld().setChange(true);
                    bombList.remove(bombList.get(i));
                }
            }
        }

        if(game.getWorld().hasChanged()){
            System.out.println("Rafraichissement des sprites de décor");
            refreshWorldSprites();
            game.getWorld().setChange(false);
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
        if(!bombList.isEmpty()){
            for(int i=0; i<bombList.size(); i++){
                int finalI = i;
                sprites.remove(bombList.get(finalI));
                sprites.add(SpriteFactory.createBomb(layer, bombList.get(finalI)));
            }
        }
        //game.bombSpriteSetChanged(false);
    }
}
