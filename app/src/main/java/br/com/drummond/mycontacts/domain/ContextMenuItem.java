package br.com.drummond.mycontacts.domain;

/**
 * Created by Fabiano de Lima Abre on 07/09/2015.
 */
public class ContextMenuItem {
    private String label;

    public ContextMenuItem(String l){
        label = l;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}

