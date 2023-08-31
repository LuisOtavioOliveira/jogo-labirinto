import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Class Room - a room in an adventure game.
 *
 * This class is part of the "World of Zuul" application.
 * "World of Zuul" is a very simple, text based adventure game.
 *
 * A "Room" represents one location in the scenery of the game. It is
 * connected to other rooms via exits. The exits are labelled north,
 * east, south, west. For each direction, the room stores a reference
 * to the neighboring room, or null if there is no exit in that direction.
 * 
 * @author Michael Kölling and David J. Barnes
 * @version 2016.02.29
 */
public class Room {
    private String description;
    private String longDescription;
    private String dica;
    private HashMap<String, Room> exits;
    public ArrayList<Item> itens;

    /**
     * Create a room described "description". Initially, it has
     * no exits. "description" is something like "a kitchen" or
     * "an open court yard".
     * 
     * @param description The room's description.
     */
    public Room(String description, String longDescription, String dica) {
        this.description = description;
        this.longDescription = longDescription;
        exits = new HashMap<>();
        this.dica = dica;
        itens = new ArrayList<>();
    }

    /**
     * Define the exits of this room. Every direction either leads
     * to another room or is null (no exit there).
     * 
     * @param north The north exit.
     * @param east  The east east.
     * @param south The south exit.
     * @param west  The west exit.
     */
    public void setExit(String direction, Room neighbor) {
        exits.put(direction, neighbor);
    }

    public void addItem(Item objeto){
        itens.add(objeto);
    }
    public void printItem(){
        for (int i=0; i<itens.size(); i++){
            Item nome = itens.get(i);
            System.out.println("Objeto na sala: " + nome.getDescription() +", peso: " + nome.getWeight());
        }
    }
    public String getExitString() {
        String returnString = "Exits: ";
        Set<String> keys = exits.keySet();
        for (String exit : keys) {
            returnString += " " + exit;
        }
        return returnString;

    }

    public Room getExit(String direction) {
        return exits.get(direction);
    }

    public String getLongDescription() {
        return longDescription;
    }

    public String getDescription() {
        return description;
    }

    public String getDica(){
        return dica;
    }
}