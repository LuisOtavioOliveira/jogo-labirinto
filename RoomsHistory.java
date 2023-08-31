import java.util.ArrayList;

public class RoomsHistory {

    private ArrayList<Room> roomsHistory;

    public RoomsHistory() {
        roomsHistory = new ArrayList<Room>();
    }

    public void addRoom(Room room) {
        roomsHistory.add(room);
    }

    public void removeLastRoom() {
        if (roomsHistory.size() > 0) {
            roomsHistory.remove(roomsHistory.size() - 1);
        }
 
    }

    public Room getLastRoom() {
        if (roomsHistory.size() > 0) {
            return roomsHistory.get(roomsHistory.size() - 1);
        } else {
            return null;
        }
    }

}
