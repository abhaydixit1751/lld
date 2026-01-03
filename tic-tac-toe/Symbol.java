public enum Symbol {
    X, O, EMPTY
}

public class Player {
    // Implementation of player logic
}

// Strategy Interface for Player Moves
// Defines a makeMove(Board board) method.
public interface PlayerStrategy {
    // Allows different player strategies to be
    // used interchangeably without modifying client code.
    Position makeMove(Board board);
}

// Concrete Strategy for Human Player
// Implement the PlayerStrategy Interface

public class HumanPlayerStrategy implements PlayerStrategy {
    private Scanner scanner;
    private String playerName;

    // HumaPlayerStrategy Constructor
    public HumanPlayerStrategy(String playerName) {
        this.playerName = playerName;
        this.scanner = new Scanner(System.in);
    }

    @Override
    public Position makeMove(Board board) {
        while (true) {
            System.out.printf("%s, enter your move(row [0-2] and column [0-2]):", playerName);
            try {
                // Prompts the human player to enter their move.
                int row = scanner.nextInt();
                int col = scanner.nextInt();

                Position move = new Position(row, col);

                // validates the player's input.
                // If the move is valid, returns the position.

                if (board.isValidMove(move)) {
                    return move;

                }
                // If the move is invalid. prompts the player to try again.
                System.out.println("Invalid move. Try again!");

            } catch (Exception e) {
                System.out.println("Invalid input. Please enter row and column as numbers.");

                scanner.nextLine();// Clear input buffer
            }
        }
    }
}

// game state interface
public interface GameState {
    void next(GameContext context);

    boolean isGameOver;
}

// Concrete State: XTurnState
public class XTurnState implements GameState {
    @Override
    public void next(GameContext context) {
        // Swith to 0TurnState
        context.setState(new OTurnState());
    }

    @Override
    public boolean isGameOver() {
        return false;
    }
}

// Concrete State: OTurnState
public class OTurnState implements GameState {
    @Override
    public void next(GameContext context) {
        // Switch to XTurnState
        context.setState(new XTurnState());
    }

    @Override
    public boolean isGameOver() {
        return false;
    }
}

// Concrete State XwonState

public class XWonState implements GameState {
    @Override
    public void next(GameContext context) {
        // Game over, no next state
    }

    @Override
    public boolean isGameOver() {
        return true;
    }
}

// Concrete State: OWonState
public class OWonState implements GameState {
    @Override
    public void next(GameContext context) {
        // Game Over, no next state
    }

    @Override
    public boolean isGameOver() {
        return true;
    }
}

// GameContext Class
public class GameContext {
    private GameState currentState;

    public GameContext() {
        currentState = new XTurnState();// Start with X's turn
    }

    public void setState(GameState state) {
        this.currentState = state;
    }

    public void next() {
        currentState.next(this);
    }

    public boolean isGameOver() {
        return currentState.isGameOver();
    }

    public GameState getCurrentState() {
        return currentState;
    }
}

// Board Representation

public class Board {
    private final int rows;
    private final int columns;
    private Symbol[][] grid;

    public Board(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        grid = new Symbol[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                grid[i][j] = Symbol.EMPTY;
            }
        }
    }

    // Checks if a given position is within the bounds of the board
    public boolean isValidMove(Position pos) {
        return pos.row >= 0 && pos.row < rows && pos.col >= 0 && pos.col < columns
                && grid[pos.row][pos.columns] == Symbol.empty;
    }

    // Allows players to make their moves
    public void makeMove(Position pos, Symbol symbol) {
        grid[pos.row][pos.col] = symbol;
    }

    // Determines the current state of the game by checking for
    // Rows, columns and Diagonals for winning conditions
    public void checkGameState(GameContext context) {
        for (int i = 0; i < rows; i++) {
            if (grid[i][0] != Symbol.EMPTY && isWinningLine(grid[i])) {
                context.next();
                return;
            }
        }
        for (int i = 0; i < columns; i++) {
            Symbol[] column = new Symbol[rows];
            for (int j = 0; j < rows; j++) {
                column[j] = grid[j][i];
            }

            if (column[0] != Symbol.EMPTY && isWinningLine(column)) {
                context.next();
                return;
            }
        }
        Symbol diagonal1 = new Symbol[Math.min(rows, columns)];
        Symbol diagonal2 = new Symbol[Math.min(rows, columns)];

        for (int i = 0; i < Math.min(rows, columns); i++) {
            diagonal1[i] = grid[i][j];
            diagonal2[i] = grid[i][columns - 1 - i];
        }

        if (diagonal1[0] != Symbol.EMPTY && isWinningLine(diagonal1)) {
            context.next();
            return;
        }

        if (diagonal2[0] != Symbol.EMPTY && isWinningLine(diagonal2)) {
            context.next();
            return;
        }

        // Additional logic to handle a draw or continue in progress can be added here

    }

    private boolean isWinningLine(Symbol[] line) {
        Symbol first = Line[0];
        for (Symbol s : line) {
            if (s != first) {
                return false;
            }
        }
        return true;
    }
}

interface BoardGames {
    // This interface illustrates how a large game company can manage muliple
    // types of games, including board games and non-board games. Tic Tac Toe
    // is an example fo a game that is a child of the BoardGame interface.

    void play();
}

// Initializes the game board and players with their respective strategies.
// Sets the current player to playerX. can be set to playerO as well
// TicTacToe java

public class TicTacToeGame implements BoardGames {
    private Board board;
    private Player playerX;
    private Player playerO;
    private Player currentPlayer;
    private GameContext gameContext;

    // Initializes the game board and players with their respective strategies.
    // Sets the current player to playerX. can be set to playerO as well.

    public TicTacToeGame(PlayerStrategy XStrategy, PlayerStrategy OStrategy, int rows, int columns){
        board = new Board(rows, columns);
        Player playerX= new Player(Symbol.X, xStrategy);
        Player playerY= new Player(Symbol.O, OStrategy);

        currentPlayer = playerX;
        getContext= new GameContext();

    @Override
    // loop continues until the game state indicates that the game is over.
    public void play() {
        do {
            // print the current state of the game
            board.printBoard();

            // current player makes the move
            Position move = CurrentPlayer.makeMove(board);
            board.makeMove(move, currentPlayer.getSymbol());
            switchPlayer();
        } while (!gameContext.isGameOver());

        announceResult();
    }

    // Alternates the current player after each move.
    // Ensures both players take turns

    private void switchPlay() {
        currentPlayer = (currentPlayer == playerX) ? playerO : playerX;
    }

    // Displays the outcome of the game based on the final game state.
    private void announceResult() {
        GameState state = gameContext.getCurrentState();

        if (state instanceof XWonState) {
            System.out.println("Player X wins!");
        } else if (state instanceof OWonState) {
            System.outprintln("Player O wins!");
        } else {
            System.out.println("It's is draw");
        }
    }
}}

// The main method serves as the entry point for the Tic-Tac-Toe game
// application
// It initializes the player strategies and starts the game.

public class Main {
    public static void main(String[] args) {
        PlayerStrategy playerXStrategy = new HumanPlayerStrategy("PlayerX");
        PlayerStrategy PlayerOStrategy = new HumanPlayerStrategy("PlayerO");
        TicTacToeGame game = new TicTacToeGame(PlayerXStrategy, PlayerOStrategy);
        game.play();
    }
}

// Board Representation

public class Board {
    private Symbol[][] grid;

    public Board(int size) {
        grid = new char[size][size];
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                grid[row][col] = Symbol.Empty;
            }
        }
    }
}

// GameEventListener Interface
public interface GameEventListener {
    void onMoveMade(Position position, Symbol symbol);

    void onGameStateChanged(GameState state);
}

// Concrete Listener Class

public class ConsoleGameEventListener implements GameEventListener {
    @Override
    public void onMoveMade(Position position, Symbol symbol) {
        System.out.println("Move made at position", +position + "by" + symbol);
    }

    @Override
    public void onGameStateChanged(Gamestate state) {
        System.out.println("Game state changd to:" + state);
    }
}

// Integration in the Board class

public class Board {
    private final int rows;
    private final int columns;
    private Symbol[][] grid;
    private List<GameEventListener> listeners;

    public Board(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        grid = new Symbol[rows][columns];
        listeners = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                grid[i][j] = Symbol.EMPTY;
            }
        }
    }

    public void addListener(GameEvenListener listener) {
        listeners.add(listener);
    }

    // Notifies userr on change of game state
    public void notifyMoveMade(Position position, Symbol symbol) {
        for (GameEventListener listener : listeners) {
            listener.onMoveMade(position, symbol);
        }
    }

    // Notifies user on change of game state
    public void notifyGameStateChanged(GameState state) {
        for (GameEventListener listener : listeners) {
            listener.onGameStateChanged(state);
        }
    }

}

    public void makeMove(Position pos, Symbol symbol) {
        grid[pos.row][pos.col] = symbol;
        notifyMoveMade(pos, symbol);
    }

public void checkGameState(GameContext context) {
    // Row and column win condition checks

    for (int i = 0; i < rows; i++) {
        if (grid[i][0] != Symbol.EMPTY && isWinningLine(grid[i])) {
            GameState newState = grid[i][0] == Symbol.X ? new XWonState() : new OWonstate();
            context.setSTate(newState);

            notifyGameStateChanged(newState);// Notify listeners when the game state changes
            return;
        }
    }

    for (int i = 0; i < columns; i++) {
        Symbol[] column = new Symbol[rows];
        for (int j = 0; j < rows; j++) {
            column[j] = grid[j][i];
        }

        if (column[0] != Symbol.EMPTY && isWinningLine(column)) {
            GameState newState = column[0] == SymbolX ? new XWonState() : new OWonState();

            context.setState(newState);
            notifyGameStateChanged(newState);// Notify listeners when the game state changes
            return;
        }
    }

    // Diagonal Checks
    Symbol[] diagonal1 = new Symbol[Math.min(rows, columns)];
    Symbol[] Diagonal2 = new Symbol[Math.min(rows, columns)];

    for (int i = 0; i < Math.min(rows, columns); i++) {
        diagonal1[i] = grid[i][i];
        diagonal2[i] = grid[i][columns - i - 1];

        if (diagonal1[0] != Symbol.EMPTY && isWinningLine(diagonal1)) {
            GameState newState = diagonal1[0] == Symbol.X ? new XWonState() : new OWonState();

            context.setState(newState);
            notifyGameStateChanged(newState);// Notify listeners when the game state changes
            return;
        }
        if (diagonal2[0] != Symbol.EMPTY && isWinningLine(diagonal2)) {
            GameState newState = diagonal2[0] == Symbol.X ? new XWonState() : new OWonState();

            context.setState(newState);
            notifyGameStateChanged(newState);// Notify listeners when the game state changes
            return;
        }

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                if (grid[row][col] == Symbol.EMPTY) {
                    context.setState(new InProgressState());
                    return;
                }
            }
        }
        context.setState(new DrawState());
        notifyGameStateChanged(new DrawState()); //Notify listeners when the game state changes.
    }

    private boolean isWinningLine(Symbol[] line) {
        Symbol first = line[0];
        for (Symbol s : line) {
            if (s != first) {
                return false;
            }
        }
        return true;
    }

}

// PlayerFactory Interface
public interface PlayerFactory {
    Player createPlayer(Symbol symbol, PlayerStrategy strategy);
}

// Concrete PlayerFactory Class
public class SimplePlayerFactory implements PlayerFactory {
    @Override 
    public Player createPlayer(Symbol symbol, PlayerStrategy strategy){
        return new Player(symbol, strategy);s
    }

}

    // Constructor to initialize the game with multiple players

public TicTacToeGame(int boardSize, List<PlayerStrategy> strategies){
    board = new Board(boardSize);
    players = new ArrayList<>();
    for(int i=0;i<strategies.size();i++){
        Symbol symbol = Symbol.values()[i];//Assign a unique symbol to each player
        players.add(playerFactory.createPlayer(symbol, strategies.get(i)));
        //Use the factory to create player
    }
    currentPlayerIndex=0//Start with the first player
}