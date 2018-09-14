import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.control.TableView;
import javafx.scene.media.MediaPlayer;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.TextField;
import javafx.scene.Scene;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.scene.media.Media;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import java.io.File;
import javafx.scene.control.Label;
import javafx.collections.MapChangeListener;
import java.io.IOException;
import java.util.ArrayList;

/**
* A Music Player Application!!!!
*@author eday30
*@version 1.0
*/
public class MusicPlayer extends Application {
    private MediaPlayer player;
    private TableView<Music> table;
    private TableView<Music> searchTable;
    private ObservableList<File> enumList = FXCollections.observableArrayList();
    private ObservableList<Music> playerlist = FXCollections.
        observableArrayList();
    private ObservableList<Music> searchList = FXCollections.
        observableArrayList();

    @Override
    public void start(Stage stage) throws IOException {
        File s = new File(".");
        File[] songFiles = s.listFiles();
        String absPath = "file:" + s.getAbsolutePath();

        ArrayList<Media> myMedia = new ArrayList<>();

        for (int i = 0; i < songFiles.length; i++) {
            if (songFiles[i].toString().contains("mp3")) {
                myMedia.add(new Media(songFiles[i].toURI().toString()));
            }
        }

        ArrayList<Music> myMusic = new ArrayList<>();
        for (int i = 0; i < myMedia.size(); i++) {
            myMusic.add(new Music(myMedia.get(i)));
        }

        for (Music m : myMusic) {
            playerlist.add(m);
        }

        table = new TableView<Music>(playerlist);
        table.setItems(playerlist);
        table.setPrefWidth(900);
        TableColumn<Music, String> firstColumn =
            new TableColumn<Music, String>("File Name");
        firstColumn.setCellValueFactory(
            new PropertyValueFactory<Music, String>("fileName"));
        firstColumn.setPrefWidth(450);
        TableColumn<Music, String> secondColumn =
            new TableColumn<Music, String>("Attributes");
        secondColumn.setCellValueFactory(
            new PropertyValueFactory<>("attributes"));
        secondColumn.setPrefWidth(450);
        TableColumn<Music, String> artistColumn =
            new TableColumn<Music, String>("Artist");
        artistColumn.setCellValueFactory(
            new PropertyValueFactory<>("artist"));
        artistColumn.setPrefWidth(150);
        TableColumn<Music, String> titleColumn =
            new TableColumn<Music, String>("Title");
        titleColumn.setCellValueFactory(
            new PropertyValueFactory<>("title"));
        titleColumn.setPrefWidth(150);
        TableColumn<Music, String> albumColumn =
            new TableColumn<Music, String>("Album");
        albumColumn.setCellValueFactory(new PropertyValueFactory<>("album"));
        albumColumn.setPrefWidth(150);
        secondColumn.getColumns().setAll(artistColumn, titleColumn,
             albumColumn);

        table.getColumns().addAll(firstColumn, secondColumn);
        table.setEditable(true);

        // MediaPlayer player = new MediaPlayer(table.getItems().get(3));
        Button playButton = new Button("Play");
        Button pauseButton = new Button("Pause");
        Button searchButton = new Button("Search Songs");
        Button showButton = new Button("Show all Songs");
        pauseButton.setDisable(true);
        showButton.setDisable(true);
        playButton.setOnAction(e -> {
                if (table.getSelectionModel().getSelectedItem() == null) {
                    player = new MediaPlayer(searchTable.getSelectionModel()
                        .getSelectedItem().getMedia());
                } else {
                    player = new MediaPlayer(table.getSelectionModel()
                        .getSelectedItem().getMedia());
                }
                player.play();
                player.setOnEndOfMedia(new Runnable() {
                    public void run() {
                        playButton.setDisable(false);
                        pauseButton.setDisable(true);
                        playButton.requestFocus();
                    }
                });
                playButton.setDisable(true);
                pauseButton.setDisable(false);
                pauseButton.requestFocus();
            }
        );

        pauseButton.setOnAction(e -> {
                player.pause();
                pauseButton.setDisable(true);
                playButton.setDisable(false);
                playButton.requestFocus();
            }
        );

        HBox buttonBox = new HBox();
        buttonBox.getChildren().setAll(playButton, pauseButton, searchButton,
             showButton);
        VBox full = new VBox(table, buttonBox);

        searchButton.setOnAction(e -> {
                VBox labelBox = new VBox();
                HBox searchBox = new HBox();
                Label newLabel = new Label("Search for songs in Music Player");
                TextField searchLine = new TextField("Search for a song," +
                    " album, artist, or title");
                searchLine.setPrefWidth(250);
                Button search = new Button("Search");
                searchBox.getChildren().setAll(searchLine, search);
                labelBox.getChildren().setAll(newLabel, searchBox);
                searchBox.setPrefWidth(400);
                searchBox.setPrefHeight(200);

                search.setOnAction(a -> {
                        String searchText = searchLine.getText();
                        if (searchList.size() != 0) {
                            searchList.clear();
                        }
                        for (Music m : playerlist) {
                            if (m.getFileName().contains(searchText)) {
                                searchList.add(m);
                            } else if (m.getArtist() != null &&
                                m.getArtist().contains(searchText)) {
                                    searchList.add(m);
                            } else if (m.getAlbum() != null &&
                                m.getAlbum().contains(searchText)) {
                                    searchList.add(m);
                            } else if(m.getTitle() != null &&
                                m.getTitle().contains(searchText)) {
                                    searchList.add(m);
                            }
                        }
                        searchTable = new TableView<>(searchList);
                        searchTable.setItems(searchList);

                        searchTable.setPrefWidth(900);
                        TableColumn<Music, String> searchFirstColumn =
                            new TableColumn<Music, String>("File Name");
                        searchFirstColumn.setCellValueFactory(
                            new PropertyValueFactory<Music, String>("fileName")
                        );
                        searchFirstColumn.setPrefWidth(450);
                        TableColumn<Music, String> searchSecondColumn =
                            new TableColumn<Music, String>("Attributes");
                        searchSecondColumn.setCellValueFactory(
                            new PropertyValueFactory<>("attributes"));
                        searchSecondColumn.setPrefWidth(450);
                        TableColumn<Music, String> searchArtistColumn =
                            new TableColumn<Music, String>("Artist");
                        searchArtistColumn.setCellValueFactory(
                            new PropertyValueFactory<>("artist"));
                        searchArtistColumn.setPrefWidth(150);
                        TableColumn<Music, String> searchTitleColumn =
                            new TableColumn<Music, String>("Title");
                        searchTitleColumn.setCellValueFactory(
                            new PropertyValueFactory<>("title"));
                        searchTitleColumn.setPrefWidth(150);
                        TableColumn<Music, String> searchAlbumColumn =
                            new TableColumn<Music, String>("Album");
                        searchAlbumColumn.setCellValueFactory(
                            new PropertyValueFactory<>("album"));
                        searchAlbumColumn.setPrefWidth(150);
                        searchSecondColumn.getColumns().setAll(
                            searchArtistColumn, searchTitleColumn,
                            searchAlbumColumn);
                        searchTable.getColumns().addAll(searchFirstColumn,
                            searchSecondColumn);
                        searchButton.setDisable(true);
                        showButton.setDisable(false);
                        showButton.requestFocus();

                        VBox searchStuff = new VBox(searchTable, buttonBox);
                        Scene searchScene = new Scene(searchStuff);
                        stage.setScene(searchScene);
                        stage.show();
                    }
                );

                Scene newWindowScene = new Scene(labelBox, 400, 200);
                Stage searchStage = new Stage();
                searchStage.setTitle("Search Songs");
                searchStage.setScene(newWindowScene);
                searchStage.show();
            }
        );

        Scene scene = new Scene(full);
        stage.setScene(scene);

        showButton.setOnAction(b -> {
                VBox showBox = new VBox(table, buttonBox);
                Scene showScene = new Scene(showBox);
                showButton.setDisable(true);
                searchButton.setDisable(false);
                searchButton.requestFocus();
                stage.setScene(showScene);
                stage.show();
            }
        );

        stage.setTitle("Music Player");
        stage.show();
    }

    /**
    * A Music Helper Class
    *@author eday30
    *@version 1.0
    */
    public class Music {
        private String fileName;
        private Media media;
        private String artist;
        private String title;
        private String album;

        /**
        * A Music Constructor. Converts Media Object to Music Object to extract
        * attributes.
        *@param m is the Media Object
        */
        public Music(Media m) {
            ObservableMap<String, Object> mapData = m.getMetadata();
            fileName = m.getSource();
            media = m;
            mapData.addListener(new MapChangeListener<String, Object>() {
                public void onChanged(MapChangeListener.Change<? extends String,
                    ? extends Object> change)  {
                    artist = (String) mapData.get("artist");
                    title = (String) mapData.get("title");
                    album = (String) mapData.get("album");
                    table.refresh();
                }
            });
        }

        /**
        * A getter for the Artist
        *@return artist is the String of the artist name
        */
        public String getArtist() {
            return artist;
        }

        /**
        * A getter for title
        *@return title is the String of the title
        */
        public String getTitle() {
            return title;
        }

        /**
        *A getter for album
        *@return album is the String of the album
        */
        public String getAlbum() {
            return album;
        }

        /**
        * A getter for the File Name
        *@return finish is a String of the corrected File Name
        */
        public String getFileName() {
            String start = fileName;
            int index = start.lastIndexOf('/');
            StringBuilder sb = new StringBuilder(fileName);
            sb.delete(0, index + 1);
            String finish = sb.toString();
            finish = finish.replace("%20", " ");
            return finish;
        }

        /**
        * A getter for the Media Object
        *@return media is the Object that is retained post-Music Transformation
        */
        public Media getMedia() {
            return media;
        }
    }
}