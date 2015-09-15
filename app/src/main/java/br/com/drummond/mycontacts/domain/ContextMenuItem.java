package br.com.drummond.mycontacts.domain;

/**
 * Created by Fabiano de Lima Abre on 07/09/2015.
 */
public class ContextMenuItem {
    private int icon;
    private String label;

    public ContextMenuItem(int i,String l){
        icon=i;
        label = l;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}

