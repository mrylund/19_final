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
            this.field.setDescription("Leje af grund        kr. 1.000<br>" +
                    "m/ 1 hus           4.000<br>" +
                    "m/ 2 huse          12.000<br>" +
                    "m/ 3 huse          28.000<br>" +
                    "m/ 4 huse          34.000<br>" +
                    "m/ hotel           40.000<br><br>" +
                    "Hvert hus koster       kr. 4.000<br>" +
                    "Et hotel koster        kr. 4.000 + 4 huse<br>" +
                    "Pantsætningsværdi      kr. 4.000");
        } else if (values[0].equals("GUI_Chance")) {
            this.field = new GUI_Chance();
        } else if (values[0].equals("GUI_Jail")) {
            this.field = new GUI_Jail();
        } else if (values[0].equals("GUI_Shipping")) {
            this.field = new GUI_Shipping();
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
