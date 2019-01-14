package Gameboard;

import gui_fields.*;

public class Field {
    private GUI_Field field;
    public Field(String[] values) {
        if(values[0].equals("GUI_Start")){
            this.field = new GUI_Start();
        }else if(values[0].equals("GUI_Street")) {
            System.out.println("This test is working");
            this.field = new GUI_Street();
            this.field.setTitle(values[1]);
            this.field.setSubText("");
            this.field.setDescription("TEST!");
        } else if (values[0].equals("GUI_Chance")) {
            this.field = new GUI_Chance();
        } else if (values[0].equals("GUI_Jail")) {
            this.field = new GUI_Jail();
        }

    }

    public GUI_Field getField() {
        return this.field;
    }

}
