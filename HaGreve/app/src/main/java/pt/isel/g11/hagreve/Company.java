package pt.isel.g11.hagreve;

/**
 * Created by Gomes on 22-Mar-15.
 */
public class Company {

    private String name;
    private boolean isSelected;

    public Company (String name){
        this.name = name;
        isSelected = false;
    }

    public String getName() {
        return name;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
}
