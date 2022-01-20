import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.event.ActionEvent;

import java.util.ArrayList;

/*
 * TIRA2020 Harkka - Oskari Kansanen
 * 
 * Implementaatiossa tuli _pikkanen_ kiire, niin esim graafi jäi vaiheeseen.
 * Sisällytin suunnittelemani graafi-rakenteen palauttamaani .zip-tiedostoon, mutta en käytä sitä tässä työssä.
 * 
 * Ohjelma muutenkin jättää ERITTÄIN paljon toivomisen varaa, mutta ajan loppuessa kesken tällä mennään.
 * 
 */

public class Harkka2020 extends Application {
   private double mouseX = 0;
   private double mouseY = 0;
   private int intensityDelta = 0;
   private int totalDelta = 0;

   Button button1;
   Button button2;
   Button button3;
   Button button4;
   Image image;
   WritableImage wImage;
   double width;
   double height;
   ImageView imageView1;
   ImageView imageView2;

   @Override
   public void start(Stage stage) throws FileNotFoundException {
      // Creating an image
      image = new Image(new FileInputStream("Testikuva.jpg"));
      width = image.getWidth();
      height = image.getHeight();
      // Setting the image view 1
      imageView1 = new ImageView(image);

      // Setting the position of the image
      // HUOM! Tämä vaikuttaa hiiren koordinaatteihin kuvassa.
      imageView1.setX(50);
      imageView1.setY(25);

      // setting the fit height and width of the image view
      imageView1.setFitHeight(height / 2);
      imageView1.setFitWidth(width / 2);

      // Setting the preserve ratio of the image view
      imageView1.setPreserveRatio(true);

      // Setting the image view 2
      imageView2 = new ImageView(image);

      // Setting the position of the image
      imageView2.setX(width / 2 + 60);
      imageView2.setY(25);

      // setting the fit height and width of the image view
      imageView2.setFitHeight(height / 2);
      imageView2.setFitWidth(width / 2);

      // Setting the preserve ratio of the image view
      imageView2.setPreserveRatio(true);
      int delta = 50;
      Text text1 = new Text("Anna sallittu intensiteettiero");
      text1.setLayoutX(50);
      text1.setLayoutY(height / 2 + delta);
      TextField textField1 = new TextField();
      textField1.setText("5");
      textField1.setLayoutX(50);
      textField1.setLayoutY(height / 2 + 1.3 * delta);

      Text text2 = new Text("Anna sallittu kokonaisero");
      text2.setLayoutX(50);
      text2.setLayoutY(height / 2 + 2.8 * delta);
      TextField textField2 = new TextField();
      textField2.setText("5");
      textField2.setLayoutX(50);
      textField2.setLayoutY(height / 2 + 3.1 * delta);

      button1 = new Button("Hae yksi komponentti syvyyshaulla");
      button1.setLayoutX(50);
      button1.setLayoutY(height / 2 + 4 * delta);

      button2 = new Button("Hae yksi komponentti leveyshaulla");
      button2.setLayoutX(50);
      button2.setLayoutY(height / 2 + 4.8 * delta);

      button3 = new Button("Hae yhden komponentin minimiVPuu");
      button3.setLayoutX(50);
      button3.setLayoutY(height / 2 + 5.5 * delta);

      button4 = new Button("Hae kaikki komponentit");
      button4.setLayoutX(50);
      button4.setLayoutY(height / 2 + 6.2 * delta);

      EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
         public void handle(ActionEvent e) {
            // Luetaan tekstikenttien tiedot.
            String txt1 = textField1.getText();
            intensityDelta = Integer.parseInt(txt1);
            String txt2 = textField2.getText();
            totalDelta = Integer.parseInt(txt2);

            // Valitaan suoritettava tehtävä.
            if (e.getSource() == button1)
               Syvyyshaku();
            if (e.getSource() == button2)
               Leveyshaku();
            if (e.getSource() == button3)
               MinVPuu();
            if (e.getSource() == button4)
               Kaikki();
         }
      };

      button1.setOnAction(event);
      button2.setOnAction(event);
      button3.setOnAction(event);
      button4.setOnAction(event);

      EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() {
         @Override
         public void handle(MouseEvent e) {
            double x = e.getX();
            mouseX = x - 50;
            double y = e.getY();
            mouseY = y - 25;
            System.out.println("Hiiren klikkaus rivilla " + x + " ja sarakkeella " + y);
            System.out.println("ja varmuuden vuoksi: " + mouseX +  " ja " + mouseY);
            // HUOM! Näkyvä kuvan korkeus ja leveys on puolet varsinaisesta kuvasta.
            // Lisäksi näkyvän kuvan vasen yläreuna on kohdassa(50,25).
            // Kuvassa tarvitaan kokonaislukuja.
         }
      };

      imageView1.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);
      // Creating a Group object
      Group root = new Group(imageView1, imageView2, text1, textField1, text2, textField2, button1, button2, button3, button4);

      // Creating a scene object
      Scene scene = new Scene(root, width + 100, height * 0.85);

      // Setting title to the Stage
      stage.setTitle("Harkka 2020");

      // Adding scene to the stage
      stage.setScene(scene);

      // Displaying the contents of the stage
      stage.show();
   }

   // Kuvan piirtämistä varten.
   public int[][] createGreyscale() {
      
      int[][] greyArray = new int[(int) width][(int) height];
      PixelReader pixelReader = image.getPixelReader();
      Color color;
      int r,g,b, grayscale;

      // Calculate grey values for each pixel, and and add them to an array
      for (int y = 0; y < height; y++) {
         for (int x = 0; x < width; x++) {
            // Retrieving the color of the pixel of the loaded image
            color = pixelReader.getColor(x, y);
            // Generating greyscale
            r = (int) Math.round(color.getRed() * 255);
            g = (int) Math.round(color.getGreen() * 255);
            b = (int) Math.round(color.getBlue() * 255);
            grayscale = (int) Math.round(0.3 * r + 0.59 * g + 0.11 * b);
            // Applying greyscale and logging that shit into the array
            greyArray[x][y] = grayscale;
            System.out.println("Harmaasavy arvo kuvapisteelle " + x + " " + y + " on " + grayscale);
         }
      }
      return greyArray;
   }

   public void drawImage(int[][] drawArray, ArrayList<String> eligible) {
      // create the variables required and initialise the pixelWriter
      wImage = new WritableImage((int)width, (int)height);
      PixelWriter writer = wImage.getPixelWriter();
      // colouring book
      Color black = Color.BLACK;
      Color white = Color.WHITE;

      System.out.println("Start drawing");
      // Draw the image from the array
      for (int i = 0; i < eligible.size(); i++) {
         int testX = Integer.parseInt(eligible.get(i).split(",")[0]);
         int testY = Integer.parseInt(eligible.get(i).split(",")[1]);
         writer.setColor(testX, testY, black);
      }

      // draw the image in all of its glory
      imageView2.setImage(wImage);
      System.out.println("End drawing.");
   }

   public ArrayList<String> createMap(int[][] array, String startCoords) {
      
      // Gonna try creating the image using depth first search or something along those lines
      // honestly screw the graph, i am running out of time here
      int h = array.length;
      int l = array[0].length;

      // a variable to keep track of - quite literally - every "pixel"
      boolean[][] visited = new boolean[h][l];
      // this one substitutes the graph
      ArrayList<String> eligible = new ArrayList<String>();

      Stack stack = new Stack();
      stack.push(startCoords);

      // we start
      System.out.println("Start traversal:");
      while (!stack.isEmpty()) {
         String x = (String) stack.pop();
         int row = Integer.parseInt(x.split(",")[0]);
         int col = Integer.parseInt(x.split(",")[1]);
         
         if (row == 0 || col == 0 || row == h-1 || col == l-1 || visited[row][col]) {
            continue;
         }

         visited[row][col] = true;
         // test to the left
         stack.push(row + "," + (col-1));
         if (Math.abs(array[row][col] - array[row][col-1]) <= intensityDelta) {
            eligible.add(row + "," + (col-1));
         }
         // test to the right
         stack.push(row + "," + (col+1));
         if (Math.abs(array[row][col] - array[row][col+1]) <= intensityDelta) {
            eligible.add(row + "," + (col+1));
         }
         // test downwards
         stack.push((row-1) + "," + col);
         if (Math.abs(array[row][col] - array[row-1][col]) <= intensityDelta) {
            eligible.add((row-1) + "," + col);
         }
         // test upwards
         stack.push((row+1) + "," + col);
         if (Math.abs(array[row][col] - array[row+1][col]) <= intensityDelta) {
            eligible.add((row+1) + "," + col);
         }
      }
      // end traversal, return the wannabe graph
      System.out.println("End traversal.");
      return eligible;
   }

   public void Syvyyshaku() {
      // we create the greyscale, this takes time, but I am not aware of a faster way
      int[][] masterGrey = createGreyscale();

      // Stringify the starting coordinates for the depth first search
      String startCoords = (int) mouseX + "," + (int) mouseY;

      // Depth first search
      ArrayList<String> eligible = createMap(masterGrey, startCoords);
      // draw the image
      drawImage(masterGrey, eligible);
   }

   public void Leveyshaku() {
      // Not implemented
      System.out.println("pruut");
   }

   public void MinVPuu() {
      // this is not implemented either
      System.out.println("proot");
   }

   public void Kaikki() {
      // yeah this isn't either
      System.out.println("Don't do that.");
   }

   public static void main(String args[]) {
      launch(args);
   }
}