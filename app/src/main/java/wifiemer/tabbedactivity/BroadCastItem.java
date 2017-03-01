package wifiemer.tabbedactivity;

/**
 * Created by Atul on 2/28/2017.
 */
public class BroadCastItem {

    String ItemName;
    String[] ItemSubCats;

    public BroadCastItem(String itemName, String[] itemSubCats) {
        ItemName = itemName;
        ItemSubCats = itemSubCats;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public String[] getItemSubCats() {
        return ItemSubCats;
    }

    public void setItemSubCats(String[] itemSubCats) {
        ItemSubCats = itemSubCats;
    }
}
