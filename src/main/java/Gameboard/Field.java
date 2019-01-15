package Gameboard;

import gui_fields.*;

public class Field {
    private GUI_Field field;
    public Field(String[] values) {
        if(values[0].equals("GUI_Start")){
            this.field = new GUI_Start();
        }else if(values[0].equals("GUI_Street")) {
            this.field = new GUI_Street();
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
        } else if (values[0].equals("GUI_Shipping")) {
            this.field = new GUI_Shipping();
            this.field.setTitle(values[2]);
            this.field.setSubText(values[1]);
        } else if (values[0].equals("GUI_Brewery")) {
            this.field = new GUI_Brewery();
        } else if (values[0].equals("GUI_Tax")) {
            this.field = new GUI_Tax();
        } else if (values[0].equals("GUI_Car")) {
            this.field = new GUI_Refuge();
        }

    }

    public GUI_Field getField() {
        return this.field;
    }

}
