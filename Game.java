/**
 * This class is the main class of the "World of Zuul" application.
 * "World of Zuul" is a very simple, text based adventure game. Users
 * can walk around some scenery. That's all. It should really be extended
 * to make it more interesting!
 * 
 * To play this game, create an instance of this class and call the "play"
 * method.
 * 
 * This main class creates and initialises all the others: it creates all
 * rooms, creates the parser and starts the game. It also evaluates and
 * executes the commands that the parser returns.
 * 
 * @author Michael Kölling and David J. Barnes
 * @version 2016.02.29
 */

public class Game {
    private Parser parser;
    private Room currentRoom;
    private boolean forceQuit;
    private RoomsHistory roomsHistory;

    /**
     * Create the game and initialise its internal map.
     */
    public Game() {
        createRooms();
        roomsHistory = new RoomsHistory();
        parser = new Parser();
    }

    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms() {
        Room p1, p2, p3, p4, p5, p6, AndarP1;

        // create the rooms
        p1 = new Room("Voce esta na sala 1",
                "A sala 1 está vazia, você precisa descobrir como chegar até a próxima sala.",
                "A direção não é para cima");
        p2 = new Room("Voce esta na sala 2",
                "A sala 2 está vazia, você precisa descobrir como chegar até a próxima sala.",
                "A direção não é para o leste");
        p3 = new Room("Voce esta na sala 3",
                "A sala 3 está vazia, você precisa descobrir como chegar até a próxima sala.",
                "Essa sala não vai a lugar nenhum");
        p4 = new Room("Voce esta na sala 4",
                "A sala 4 está vazia, você precisa descobrir como chegar até a próxima sala.",
                "A direção não é para o oeste");
        p5 = new Room("Voce esta na sala 5",
                "A sala 5 está vazia, você precisa descobrir como chegar até a próxima sala.",
                "A direção não é para o norte");
        p6 = new Room("Parabens! Voce chegou na sala final!!",
                "Descrição longa dizendo que você ganhou o jogo, parabéns!!", "Você chegou!!");
        AndarP1 = new Room("Você está no andar 1 da sala 1",
                "Essa é uma sala vazia que está em cima da sala 1, nada para fazer aqui.", "Não tem nada aqui, desça!");
        // initialise room exit5
        p1.setExit("east", p2);
        p1.setExit("south", p3);
        p1.setExit("up", AndarP1);

        AndarP1.setExit("down", p1);

        p2.setExit("north", p1);
        p2.setExit("east", p1);
        p2.setExit("south", p4);
        p2.setExit("west", p1);

        p3.setExit("north", p1);
        p3.setExit("east", p1);
        p3.setExit("south", p1);
        p3.setExit("west", p1);

        p4.setExit("north", p1);
        p4.setExit("east", p5);
        p4.setExit("south", p1);
        p4.setExit("west", p1);

        p5.setExit("north", p1);
        p5.setExit("east", p6);
        p5.setExit("south", p1);
        p5.setExit("west", p1);

        p1.addItem(new Item("mesa",5.2));
        p2.addItem(new Item("cadeira",1.2));
        p3.addItem(new Item("quadro",0.7));
        p4.addItem(new Item("tesoura",0.2));
        p5.addItem(new Item("caderno",0.2));
        p6.addItem(new Item("caneta",0.2));


        currentRoom = p1; // start game outside
    }

    /**
     * Main play routine. Loops until end of play.
     */
    public void play() {
        printWelcome();

        // Enter the main command loop. Here we repeatedly read commands and
        // execute them until the game is over.

        boolean finished = false;

        while (!finished && !this.forceQuit) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        System.out.println("Thank you for playing.  Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome() {
        System.out.println();
        System.out.println("Welcome to the World of Zuul!");
        System.out.println("World of Zuul is a new, incredibly boring adventure game.");
        System.out.println("Type 'help' if you need help.");
        System.out.println();
        printLocationInfo();
    }

    /**
     * Given a command, process (that is: execute) the command.
     * 
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command) {
        boolean wantToQuit = false;

        if (command.isUnknown()) {
            System.out.println("I don't know what you mean...");
            return false;
        }

        String commandWord = command.getCommandWord();
        if (commandWord.equals("help")) {
            printHelp();
        } else if (commandWord.equals("go")) {
            goRoom(command);
        } else if (commandWord.equals("quit")) {
            wantToQuit = quit(command);
        } else if (commandWord.equals("look")) {
            look();
        } else if (commandWord.equals("dica")) {
            dica();
        } else if (commandWord.equals("back")) {
            back();
        }

        return wantToQuit;
    }

    // implementations of user commands:

    /**
     * Print out some help information.
     * Here we print some stupid, cryptic message and a list of the
     * command words.
     */
    private void printHelp() {
        System.out.println("You are lost. You are alone. You wander");
        System.out.println("around at the university.");
        System.out.println();
        System.out.println("Your command words are:");
        parser.showCommands();
    }

    private void printLocationInfo() {
        System.out.println(currentRoom.getDescription());
        System.out.println(currentRoom.getExitString());
        currentRoom.printItem();
        System.out.println();
    }

    /**
     * Try to go in one direction. If there is an exit, enter
     * the new room, otherwise print an error message.
     */
    private void goRoom(Command command) {
        if (!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }

        if (currentRoom.getDescription().equals("Voce esta na sala 5") && command.getSecondWord().equals("east")) {
            System.out.println("Parabens! Voce chegou na sala final!!");
            this.forceQuit = true;
            return;
        }

        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = null;
        if (direction.equals("north")) {
            nextRoom = currentRoom.getExit("north");
        }
        if (direction.equals("east")) {
            nextRoom = currentRoom.getExit("east");
        }
        if (direction.equals("south")) {
            nextRoom = currentRoom.getExit("south");
        }
        if (direction.equals("west")) {
            nextRoom = currentRoom.getExit("west");
        }
        if (direction.equals("up")) {
            nextRoom = currentRoom.getExit("up");
        }
        if (direction.equals("down")) {
            nextRoom = currentRoom.getExit("down");
        }

        if (nextRoom == null) {
            System.out.println("There is no door!");
        } else {
            roomsHistory.addRoom(currentRoom);
            currentRoom = nextRoom;
            printLocationInfo();
        }
    }

    private void look() {
        System.out.println(currentRoom.getLongDescription());
    }

    private void dica() {
        System.out.println(currentRoom.getDica());
    }

    private void back () {
        if (roomsHistory.getLastRoom() != null) {
            currentRoom = roomsHistory.getLastRoom();
            roomsHistory.removeLastRoom();
            printLocationInfo();
        } else {
            System.out.println("Não há mais salas para voltar");
        }
    }

    /**
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * 
     * @return true, if this command quits the game, false otherwise.
     */
    private boolean quit(Command command) {
        if (command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        } else {
            return true; // signal that we want to quit
        }
    }
}
