import java.util.Random;

public class trainthree {

  public static Random rand = new Random();

  public static int dir; //0 = u, 1 = r, 2 = d, 3 = l
  public static int size = 20;
  public static String[][] board = new String[size][size];

  public static int[] bobpos = new int[2];
  public static int[] foodpos = new int[2];

  public static int movecount = 0;
  public static double[] weights = new double[2]; 
  public static int lastchange = 0;
  public static int direction;

  public static void main(String [] args) throws InterruptedException{
    weights[0] = rand.nextDouble(-0.1, 0.0);
    weights[1] = rand.nextDouble(0.0, 0.1);
    initboard();
    simulate(1);
    System.out.println("1: " + weights[0]);
    System.out.println("2: " + weights[1]);
  }

  public static void move(int input) {
    direction = usenetwork(input);
    int olddist = dist(bobpos, foodpos);
    double epsilon = 0.5;
    if (epsilon >= rand.nextDouble(0, 1)) direction = rand.nextInt(0,1);
    if (direction == 0) {
      int decider = rand.nextInt(0,2);
      if (decider == 0) dir += 1;
      else dir += 3;
      dir %= 4;
    }
    if (dir == 0) bobpos[0] = Math.max(bobpos[0] - 1, 0); //Go up
    if (dir == 1) bobpos[1] = Math.max(bobpos[1] - 1, 0); //Go right
    if (dir == 2) bobpos[0] = Math.min(bobpos[0] + 1, size - 1); //Go down
    if (dir == 3) bobpos[1] = Math.min(bobpos[1] + 1, size - 1); //Go left
    int newdist = dist(bobpos, foodpos);
    if (newdist < olddist) {
      if (direction == 1) weights[1] += 0.01;
      if (direction == 0) weights[0] += 0.01;
    }
    else {
      if (direction == 1) weights[1] -= 0.01;
      if (direction == 0) weights[0] -= 0.01;
    }
  }

  public static void simulate(int print) throws InterruptedException{
    int[] input = new int[2];
    input[1] = dist(bobpos, foodpos);
    input[0] = dist(bobpos, foodpos);
    while (true) { 
        while (dist(bobpos, foodpos) > 0) {
        if (dist(bobpos, foodpos) == 0) initboard();
        move(input[0] - input[1]);
        input[0] = input[1];
        input[1] = dist(bobpos, foodpos);
        movecount++;
        if (print == 1) {
          printboard();
          Thread.sleep(250);
        }
      }
      initboard();
    }
    
  }

  public static int usenetwork(int input) { 
    //Left = 0, stay forwards = 1
    double l = input * weights[0];
    double f = input * weights[1];
    if (f > l) {
      return 1;
    }
    return 0;
  }

  public static void initboard() {
    //Put the food and bob in random places

    //First reset the board
    for (int i = 0; i < board.length; i++) {
      for (int j = 0; j < board[i].length; j++) board[i][j] = "-";
    }

    int bobposx = rand.nextInt(size - 1);
    int bobposy = rand.nextInt(size - 1);
    board[bobposx][bobposy] = "B";
    bobpos[0] = bobposx;
    bobpos[1] = bobposy;

    int foodposx;
    int foodposy;
    dir = rand.nextInt(3);
    while (true) {
      foodposx = rand.nextInt(size - 1);
      foodposy = rand.nextInt(size - 1);
      if (board[foodposx][foodposy] != "B") {
        board[foodposx][foodposy] = "F";
        foodpos[0] = foodposx;
        foodpos[1] = foodposy;
        break;
      }
    }
  }

  public static void printboard() {
    System.out.println("\033[H\033[2J");
    for (int i = 0; i < board.length; i++) {
      for (int j = 0; j < board[i].length; j++) {
        board[i][j] = "-";
        if (i == bobpos[0] && j == bobpos[1]) board[i][j] = "B";
        if (i == foodpos[0] && j == foodpos[1]) board[i][j] = "F";
      }
    }
    for (int i = 0; i < board.length; i++) {
      for (int j = 0; j < board[i].length; j++) System.out.print(board[i][j] + "  ");
      System.out.println();
    }
  }

  public static int dist(int[] pos1, int[] pos2) {
    return Math.abs(pos1[0] - pos2[0]) + Math.abs(pos1[1] - pos2[1]);
  }
}




