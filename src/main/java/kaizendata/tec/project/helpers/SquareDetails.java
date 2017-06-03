/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kaizendata.tec.project.helpers;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 *
 * @author aming
 */
public class SquareDetails {
   
    public SquareDetails() {
     
    }

    public static HBox  squareCreator(Object o) throws IllegalArgumentException, IllegalAccessException {
      HBox square = new HBox();
  
       
        square.setStyle("-fx-border-color :  #5264AE ; -fx-border-radius :  10 10 10 10; -fx-background-radius : 10 10 10 10 ;");

        square.setPadding(new Insets(10, 10, 10, 10));

        square.setPrefSize(333, 63);

        
       
   

        VBox detailsBox = new VBox();
        Field [] fields = o.getClass().getDeclaredFields();
        for (Field field : fields) {

            if (!(field.getType().getSimpleName().equals("List") || field.getType().getSimpleName().equals("ObjectId")) ){
            field.setAccessible(true);
            Label l = new Label(field.get(o).toString());
            detailsBox.getChildren().add(l);  }
        } 


        square.getChildren().add(detailsBox);
       
        return square;


    }
    
    
    
    



}