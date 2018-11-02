package reversi.server;

import reversi.Reversi;
import reversi.ReversiException;
import reversi.ReversiProtocol;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * Implementation of a server for a reversi game
 *
 * @author Michael Jansen
 */
public class ReversiServer {
    private ServerSocket server;
    private Socket client1;
    private Socket client2;
    private Scanner in1;
    private Scanner in2;
    private PrintWriter out1;
    private PrintWriter out2;
    private Reversi reversi;

    /**
     * Creates a Reversi server and game board
     * @param port the port to run the server on
     * @param dimension the dimensions of the game board
     */
    public ReversiServer(int port, int dimension){
        reversi = new Reversi(dimension);
        try{
            server = new ServerSocket(port);
            // Wait for a connection and create a scanner and writer for the client
            System.out.println("Waiting for player 1...");
            client1 =  server.accept();
            in1 = new Scanner(new InputStreamReader(client1.getInputStream()));
            out1 =  new PrintWriter(client1.getOutputStream(), true);
            System.out.println("Player 1 connected! " + client1);
            // Wait for a second connection and create a scanner and writer for the client
            System.out.println("Waiting for player 2...");
            client2 = server.accept();
            in2 = new Scanner(new InputStreamReader(client2.getInputStream()));
            out2 = new PrintWriter(client2.getOutputStream(), true);
            System.out.println("Player 2 connected!");
        }catch (IOException e){
            e.printStackTrace();
            closeAll();
            System.exit(-1);
        }
    }

    /**
     * Main control method. Sends the connect messages then handles
     * player moves until the game is over
     */
    public void run(){
        System.out.println("Starting game!");

        // Send connect messages to the clients
        sendConnectMessages();

        try {
            // Handle a move while the game is not over
            while (!reversi.gameOver()) {
                handleMoveRound(in1, out1, out2);
                handleMoveRound(in2, out2, out1);
            }
            sendOutcomeMessages();
        }catch (ReversiException e){
            System.err.println("Error: " + e.getMessage());
            sendErrorMessages();
        }finally {
            closeAll();
        }
    }

    /**
     * Handles a round of moving from one player
     * @throws ReversiException if the move made is not valid
     */
    private void handleMoveRound(Scanner moverIn, PrintWriter moverOut, PrintWriter otherOut) throws ReversiException {
        // Tell the client to make a move
        moverOut.println(ReversiProtocol.MAKE_MOVE);
        // Wait for a response from the client
        moverIn.next();
        // Get row and column from the response
        int row = moverIn.nextInt();
        int col = moverIn.nextInt();
        // Make the move on the game board
        reversi.makeMove(row, col);
        // Send the move to the other client
        otherOut.println(ReversiProtocol.MOVE_MADE + " " + row + " " + col);
    }

    /**
     * Sends messages to the client letting them know they have connected and the dimension
     * of the game board
     */
    private void sendConnectMessages(){
        out1.println(ReversiProtocol.CONNECT + " " + reversi.getDimension());
        out2.println(ReversiProtocol.CONNECT + " " + reversi.getDimension());
    }

    /**
     * Sends the outcome of the game to the clients
     */
    private void sendOutcomeMessages(){
        Reversi.Move winner = reversi.getWinner();
        if(winner == Reversi.Move.PLAYER_ONE){
            out1.println(ReversiProtocol.GAME_WON);
            out2.println(ReversiProtocol.GAME_LOST);
        }else if(winner == Reversi.Move.PLAYER_TWO){
            out1.println(ReversiProtocol.GAME_LOST);
            out2.println(ReversiProtocol.GAME_WON);
        }else{
            out1.println(ReversiProtocol.GAME_TIED);
            out2.println(ReversiProtocol.GAME_TIED);
        }
    }

    /**
     * Sends an error message to both clients if an error was made
     */
    private void sendErrorMessages(){
        out1.println(ReversiProtocol.ERROR);
        out2.println(ReversiProtocol.ERROR);
    }

    /**
     * Closes all readers, writers, and sockets
     */
    private void closeAll(){
        try {
            if (out1 != null) out1.close();
            if (out2 != null) out2.close();
            if (in1 != null) in1.close();
            if (in2 != null) in2.close();
            if (client1 != null) client1.close();
            if(client2 != null) client2.close();
            if(server != null) server.close();
        }catch (IOException e){
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Usage: java ReversiServer DIM port");
            System.exit(1);
        }

        int port = Integer.parseInt(args[1]);
        int dimension = Integer.parseInt(args[0]);

        ReversiServer reversiServer = new ReversiServer(port, dimension);
        reversiServer.run();
    }
}
