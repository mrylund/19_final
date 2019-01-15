package Gameboard;

import gui_fields.*;
import java.awt.*;

public class Field {
    private GUI_Field field;
    public Field(String[] values) {
        if(values[0].equals("GUI_Start")){
            this.field = new GUI_Start();
            String[] lineColor = values[4].split(",");
            int[] colorOfField = new int[lineColor.length];
            for(int i = 0; i < colorOfField.length; i++){
                colorOfField[i] = Integer.parseInt(lineColor[i]);
            }
            this.field.setBackGroundColor(new Color(colorOfField[0],colorOfField[1],colorOfField[2]));
            this.field.setSubText(values[1]);
            this.field.setDescription("Når de passerer start indkasserer de 4000 kr.");
            this.field.setTitle("Start");

        }else if(values[0].equals("GUI_Street")) {
            this.field = new GUI_Street();
            String[] lineColor = values[11].split(",");
            int[] colorOfField = new int[lineColor.length];
            for(int i = 0; i < colorOfField.length; i++){
                colorOfField[i] = Integer.parseInt(lineColor[i]);
            }
            this.field.setBackGroundColor(new Color(colorOfField[0],colorOfField[1],colorOfField[2]));
            this.field.setTitle(values[2]);
            this.field.setSubText(values[1]);
            this.field.setDescription("Leje af grund        kr."+ values[4]+"<br>" +
                    "m/ 1 hus           "+values[5]+"<br>" +
                    "m/ 2 huse          "+values[6]+"<br>" +
                    "m/ 3 huse          "+values[7]+"<br>" +
                    "m/ 4 huse          "+values[8]+"<br>" +
                    "m/ hotel           "+values[9]+"<br><br>" +
                    "Hvert hus og hotel koster       kr. "+values[10]+"<br>" +
                    "Pantsætningsværdi      kr.");
        } else if (values[0].equals("GUI_Chance")) {
            this.field = new GUI_Chance();
            this.field.setDescription("Her kan de prøve lykken.");
        } else if (values[0].equals("GUI_Jail")) {
            this.field = new GUI_Jail();
            this.field.setDescription(values[2]);
            this.field.setSubText(values[2]);
        } else if (values[0].equals("GUI_Shipping")) {
            this.field = new GUI_Shipping();
            this.field.setTitle(values[2]);
            this.field.setSubText(values[1]);
            this.field.setDescription("Leje af rederi        kr."+ values[4]+"<br>" +
                    "m/ 2 rederier      "+values[5]+"<br>" +
                    "m/ 3 rederier      "+values[6]+"<br>" +
                    "m/ 4 rederier      "+values[7]+"<br>" +

                    "Pantsætningsværdi      kr.");
        } else if (values[0].equals("GUI_Brewery")) {
            this.field = new GUI_Brewery();
            this.field.setSubText(values[1]);
            this.field.setTitle(values[2]);
            this.field.setDescription("leje af grund kr <br> 100 * antal-øjne<br><br>" +
                    "leje af grund med begge bryggerier kr <br>200 * antal-øjne");
        } else if (values[0].equals("GUI_Tax1")) {
            this.field = new GUI_Tax();
            this.field.setTitle("Betal indkomsskat kr. "+values[4]);
            this.field.setSubText(values[1]);
            this.field.setDescription("Betal indkomsskat kr. "+values[4]);
        } else if (values[0].equals("GUI_Tax2")){
            this.field = new GUI_Tax();
            this.field.setTitle("Ekstraordinær statsskat. Betal kr. "+values[3]);
            this.field.setSubText(values[1]);
            this.field.setDescription("Ekstraordinær statsskat. Betal kr. "+values[3]);
        } else if (values[0].equals("GUI_Car")) {
            this.field = new GUI_Refuge();
            this.field.setSubText(values[1]);
            this.field.setDescription("Gratis parkering");
            this.field.setTitle("Parkering");
        }
    }

    public GUI_Field getField() {
        return this.field;
    }

}
