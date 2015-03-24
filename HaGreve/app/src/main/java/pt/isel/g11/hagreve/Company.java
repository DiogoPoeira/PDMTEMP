package pt.isel.g11.hagreve;

/**
 * Created by Gomes on 22-Mar-15.
 */
public class Company {

    private String name;
    private boolean isSelected;
    private int id;

    public Company (int id, String name){
        this.id = id;
        this.name = name;
        isSelected = false;
    }
    public int getId(){ return id; }

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
