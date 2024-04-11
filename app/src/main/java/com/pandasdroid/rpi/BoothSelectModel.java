package com.pandasdroid.rpi;

public class BoothSelectModel {
    public String booth;
    public boolean is_selected;

    public BoothSelectModel(String booth, boolean is_selected) {
        this.booth = booth;
        this.is_selected = is_selected;
    }

    public String getBooth() {
        return booth;
    }

    public void setBooth(String booth) {
        this.booth = booth;
    }

    public boolean isIs_selected() {
        return is_selected;
    }

    public void setIs_selected(boolean is_selected) {
        this.is_selected = is_selected;
    }
}
