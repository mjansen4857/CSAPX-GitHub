package reversi.client;

import reversi.Reversi;
import reversi.ReversiException;
import reversi.ReversiProtocol;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * Implementation of a client for a reversi game
 *
 * @author Michael Jansen
 */
public class ReversiClient {
    private Socket server;
    private Scanner in;
    private PrintWriter out;
    private Reversi reversi;
    private boolean running;

    /**
     * Creates a client connection with the server and instantiates the game board
     * @param hostname The hostname of the server
     * @param port The port of the server
     */
    public ReversiClient(String hostname, int port){
        running = false;
        try{
            // Connect to the server and create a scanner and a writer
            server = new Socket(hostname, port);
            in = new Scanner(new InputStreamReader(server.getInputStream()));
            out = new PrintWriter(server.getOutputStream(), true);
            // Wait for a connect message from the server
            in.next();
            // Get the board dimension from the message and create the game board
            int dimension = in.nextInt();
            reversi = new Reversi(dimension);
        }catch (IOException e){
            e.printStackTrace();
            closeAll();
            System.exit(-1);
        }
    }

    /**
     * Main control method of the client. Handles commands from the server
     * while the game is still going
     */
    public void run(){
        running = true;
        System.out.println(reversi);

        while(running){
            while(!in.hasNext());
            String command = in.next();
            handleCommand(command);
        }
    }

    /**
     * Handles a command from the server
     * @param command The command from the server
     */
    private void handleCommand(String command){
        switch (command){
            case ReversiProtocol.MAKE_MOVE:
                makeMove();
                System.out.println(reversi);
                break;
            case ReversiProtocol.MOVE_MADE:
                try {
                    moveMade(in.nextInt(), in.nextInt());
                    System.out.println(reversi);
                }catch (ReversiException e){
                    System.err.println("Server sent an invalid move");
                }
                break;
            case ReversiProtocol.GAME_LOST:
                System.out.println("You lost! Boo!");
                running = false;
                break;
            case ReversiProtocol.GAME_TIED:
                System.out.println("You ties! Meh!");
                running = false;
                break;
            case ReversiProtocol.GAME_WON:
                System.out.println("You won! Yay!");
                running = false;
                break;
            case ReversiProtocol.ERROR:
                System.out.println("The server reported an error");
                closeAll();
                System.exit(-1);
                break;
            default:
                System.err.println("The server sent an invalid command");
                closeAll();
                System.exit(-1);
        }
    }

    /**
     * Prompts the user to make a move then sends that move to the server
     * if it is valid
     */
    private void makeMove(){
        System.out.print("Your turn! Enter row column: ");
        // Read a row and column from input
        Scanner reader = new Scanner(new InputStreamReader(System.in));
        int row = reader.nextInt();
        int col = reader.nextInt();
        // Attempt to make a move and report it to the server.
        // If the move is invalid it will prompt the user again
        try{
            moveMade(row, col);
            out.println(ReversiProtocol.MOVE + " " + row + " " + col);
        }catch (ReversiException e){
            System.out.println("Invalid move!");
            makeMove();
        }
    }

    /**
     * Displays a move to the console
     * @param row the row of the move
     * @param col the column of the move
     * @throws ReversiException if the move is invalid
     */
    private void moveMade(int row, int col) throws ReversiException{
        reversi.makeMove(row, col);
        System.out.println("A move has been made in row " + row + " column " + col);
    }

    /**
     * Closes the reader, writer, and socket
     */
    private void closeAll(){
        try {
            if (out != null) out.close();
            if (in != null) in.close();
            if (server != null) server.close();
        }catch (IOException e){
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Usage: java ReversiClient <hostname> <port number>");
            System.exit(1);
        }

        String hostname = args[0];
        int port = Integer.parseInt(args[1]);

        ReversiClient client = new ReversiClient(hostname, port);
        client.run();
    }
}
