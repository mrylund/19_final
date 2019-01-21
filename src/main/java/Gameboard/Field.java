package Gameboard;

import gui_fields.*;
import java.awt.*;


public class Field {
    private GUI_Field field;
    private int fieldtype = 0;
    private int owner = -1;
    private int houseCount = 0;
    private boolean hotel = false;


    /**
     * @param values all the values the field should have saved.
     */
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
            fieldtype = 0;

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
            fieldtype = 1;
        } else if (values[0].equals("GUI_Chance")) {
            this.field = new GUI_Chance();
            this.field.setDescription("Her kan de prøve lykken.");
            fieldtype = 2;
        } else if (values[0].equals("GUI_Jail")) {
            this.field = new GUI_Jail();
            this.field.setDescription(values[2]);
            this.field.setSubText(values[2]);
            this.field.setForeGroundColor(new Color(255,255,255));
            this.field.setBackGroundColor(new Color(0,0,0));
            fieldtype = 3;
        } else if (values[0].equals("GUI_Shipping")) {
            this.field = new GUI_Shipping();
            this.field.setTitle(values[2]);
            this.field.setSubText(values[1]);
            this.field.setDescription("Leje af rederi        kr."+ values[4]+"<br>" +
                    "m/ 2 rederier      "+values[5]+"<br>" +
                    "m/ 3 rederier      "+values[6]+"<br>" +
                    "m/ 4 rederier      "+values[7]+"<br>" +
                    "Pantsætningsværdi      kr.");
            this.field.setBackGroundColor(new Color(200,10,20));
            fieldtype = 4;
        } else if (values[0].equals("GUI_Brewery")) {
            this.field = new GUI_Brewery();
            this.field.setSubText(values[1]);
            this.field.setTitle(values[2]);
            this.field.setDescription("leje af grund kr <br> 100 * antal-øjne<br><br>" +
                    "leje af grund med begge bryggerier kr <br>200 * antal-øjne");
            this.field.setForeGroundColor(new Color(0,150,2));
            this.field.setBackGroundColor(new Color(0,0,0));
            fieldtype = 5;
        } else if (values[0].equals("GUI_Tax1")) {
            this.field = new GUI_Tax();
            this.field.setTitle("Betal indkomsskat kr. "+values[4]);
            this.field.setSubText(values[1]);
            this.field.setDescription("Betal indkomsskat kr. "+values[4]);
            fieldtype = 6;
        } else if (values[0].equals("GUI_Tax2")){
            this.field = new GUI_Tax();
            this.field.setTitle("Ekstraordinær statsskat. Betal kr. "+values[3]);
            this.field.setSubText(values[1]);
            this.field.setDescription("Ekstraordinær statsskat. Betal kr. "+values[3]);
            fieldtype = 6;
        } else if (values[0].equals("GUI_Car")) {
            this.field = new GUI_Refuge();
            this.field.setSubText(values[1]);
            this.field.setDescription("Gratis parkering");
            this.field.setTitle("Parkering");
            fieldtype = 7;
        }
    }

    /**
     * @return the GUI_Field tied to the object.
     */
    public GUI_Field getField() {
        return this.field;
    }

    /**
     * @return the type this field has.
     */
    public int getFieldType() {
        return fieldtype;
    }

    /**
     * @return the current owner of the field.
     */
    public int getOwner() {
        return this.owner;
    }

    /**
     * @param player that should own the field.
     * @param color that the field should have.
     */
    public void setOwner(int player, Color color) {
        this.owner = player;
        ((GUI_Ownable)this.field).setBorder(color);
    }

    /**
     * @param amount of houses the field should have on it.
     */
    public void setHouses(int amount) {
        houseCount = amount;
        ((GUI_Street)this.field).setHouses(amount);
    }

    /**
     * @param hotel whether the field should have a hotel or not.
     */
    public void setHotel(boolean hotel) {
        ((GUI_Street)this.field).setHotel(hotel);
        this.hotel = true;
    }

    /**
     * @return whether the field has a hotel on it.
     */
    public boolean hasHotel() {
        return hotel;
    }

    /**
     * @return the amount of houses on the field.
     */
    public int getHouseCount() {
        return this.houseCount;
    }

}
