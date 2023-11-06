package vn.astec.parkingmanagement.models;

public class VehicleType {
    private int icon;
    private boolean check;
    private String name;

    public VehicleType(int icon, boolean check, String name) {
        this.icon = icon;
        this.check = check;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }
}
