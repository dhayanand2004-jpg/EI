import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

interface Direction {
    Direction turnLeft();
    Direction turnRight();
    Position moveForward(Position pos);
    String toString();
}

class North implements Direction {
    public Direction turnLeft() { return new West(); }
    public Direction turnRight() { return new East(); }
    public Position moveForward(Position pos) { return new Position(pos.x, pos.y + 1); }
    public String toString() { return "N"; }
}

class South implements Direction {
    public Direction turnLeft() { return new East(); }
    public Direction turnRight() { return new West(); }
    public Position moveForward(Position pos) { return new Position(pos.x, pos.y - 1); }
    public String toString() { return "S"; }
}

class East implements Direction {
    public Direction turnLeft() { return new North(); }
    public Direction turnRight() { return new South(); }
    public Position moveForward(Position pos) { return new Position(pos.x + 1, pos.y); }
    public String toString() { return "E"; }
}

class West implements Direction {
    public Direction turnLeft() { return new South(); }
    public Direction turnRight() { return new North(); }
    public Position moveForward(Position pos) { return new Position(pos.x - 1, pos.y); }
    public String toString() { return "W"; }
}

// Position class
class Position {
    final int x;
    final int y;

    Position(int x, int y) {
        this.x = x;
        this.y = y;
    }
}

// Composite pattern for grid components
interface GridComponent {
    boolean isObstacleAt(int x, int y);
}

class Obstacle implements GridComponent {
    private final int x, y;

    Obstacle(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean isObstacleAt(int x, int y) {
        return this.x == x && this.y == y;
    }
}

class Grid implements GridComponent {
    private final int width, height;
    private final List<GridComponent> components = new ArrayList<>();

    Grid(int width, int height) {
        this.width = width;
        this.height = height;
    }

    void addComponent(GridComponent component) {
        components.add(component);
    }

    public boolean isObstacleAt(int x, int y) {
        for (GridComponent c : components) {
            if (c.isObstacleAt(x, y)) {
                return true;
            }
        }
        return false;
    }

    boolean isInBounds(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }
}

// Rover class - receiver
class Rover {
    private Position position;
    private Direction direction;
    private final Grid grid;
    private static final Logger logger = Logger.getLogger(Rover.class.getName());

    Rover(int x, int y, Direction direction, Grid grid) {
        this.position = new Position(x, y);
        this.direction = direction;
        this.grid = grid;
    }

    void move() {
        Position nextPos = direction.moveForward(position);
        if (grid.isInBounds(nextPos.x, nextPos.y) && !grid.isObstacleAt(nextPos.x, nextPos.y)) {
            logger.info(String.format("Moving from (%d, %d) to (%d, %d)", position.x, position.y, nextPos.x, nextPos.y));
            position = nextPos;
        } else {
            logger.warning(String.format("Move blocked at (%d, %d) due to obstacle or boundary", nextPos.x, nextPos.y));
        }
    }

    void turnLeft() {
        direction = direction.turnLeft();
        logger.info("Turned left. New direction: " + direction.toString());
    }

    void turnRight() {
        direction = direction.turnRight();
        logger.info("Turned right. New direction: " + direction.toString());
    }

    String getStatusReport() {
        boolean obstacleHere = grid.isObstacleAt(position.x, position.y);
        String obstacleMsg = obstacleHere ? "Obstacle detected." : "No obstacles detected.";
        return String.format("Rover is at (%d, %d) facing %s. %s", position.x, position.y, direction.toString(), obstacleMsg);
    }
}

// Command interface and concrete commands
interface Command {
    void execute();
}

class MoveCommand implements Command {
    private final Rover rover;

    MoveCommand(Rover rover) {
        this.rover = rover;
    }

    public void execute() {
        rover.move();
    }
}

class LeftCommand implements Command {
    private final Rover rover;

    LeftCommand(Rover rover) {
        this.rover = rover;
    }

    public void execute() {
        rover.turnLeft();
    }
}

class RightCommand implements Command {
    private final Rover rover;

    RightCommand(Rover rover) {
        this.rover = rover;
    }

    public void execute() {
        rover.turnRight();
    }
}

// Invoker class
class CommandInvoker {
    private final List<Command> history = new ArrayList<>();

    void executeCommand(Command command) {
        command.execute();
        history.add(command);
    }
}

// Mars Rover Simulator - the client
public class MarsRoverSimulator {
    private final Grid grid;
    private final Rover rover;
    private final CommandInvoker invoker;

    public MarsRoverSimulator(int gridWidth, int gridHeight, int startX, int startY, char startDirection, List<Position> obstacles) {
        grid = new Grid(gridWidth, gridHeight);
        for (Position obs : obstacles) {
            grid.addComponent(new Obstacle(obs.x, obs.y));
        }
        Direction direction = switch (startDirection) {
            case 'N' -> new North();
            case 'S' -> new South();
            case 'E' -> new East();
            case 'W' -> new West();
            default -> throw new IllegalArgumentException("Invalid start direction");
        };
        rover = new Rover(startX, startY, direction, grid);
        invoker = new CommandInvoker();
    }

    public void runCommands(List<Character> commands) {
        for (Character cmd : commands) {
            Command command = switch (cmd) {
                case 'M' -> new MoveCommand(rover);
                case 'L' -> new LeftCommand(rover);
                case 'R' -> new RightCommand(rover);
                default -> null;
            };
            if (command != null) {
                invoker.executeCommand(command);
            }
        }
    }

    public String getStatusReport() {
        return rover.getStatusReport();
    }

    // Main method for demonstration
    public static void main(String[] args) {
        List<Position> obstacles = List.of(new Position(2, 2), new Position(3, 5));
        MarsRoverSimulator simulator = new MarsRoverSimulator(10, 10, 0, 0, 'N', obstacles);
        List<Character> commands = List.of('M', 'M', 'R', 'M', 'L', 'M');
        simulator.runCommands(commands);
        System.out.println(simulator.getStatusReport());
    }
}
