package assignment3;
import java.util.*;

public class Game {
	private static final String MENUTITLE = "=====Wumpus====";
	private static final String[] OPTION = {"1. Move player left",
			"2. Move player right",
			"3. Move player up",
			"4. Move player down",
			"5. Quit",
			"6. Reveal map"};
	private GameItem[][] board = new GameItem[4][4];
	private int px,py;
	private int pieceOfGolds = 0;
	private boolean running;
	private boolean reveal;
	Random seed = new Random();
	
	private void setBoard(){
		int wx, wy;
		running = true;
		reveal = false;
		// Initiate a Wumpus
		wx = seed.nextInt(4);
		wy = seed.nextInt(4);
		Wumpus wp = new Wumpus();
		board[wx][wy] = wp;
		System.out.println("Wp: "+wx+","+wy);
		//initiate golds
		pieceOfGolds = seed.nextInt(2);
		for(int i=0;i<=pieceOfGolds;i++){
			int x = seed.nextInt(4);
			int y = seed.nextInt(4);
			if(board[x][y]==null){
				Gold g = new Gold();
				board[x][y] = g;
				System.out.println("Gp: "+x+","+y);
			}
			else{
				i--;
			}
		}
		
		//initiate pits
		for(int i=0;i<3;i++){
			int x = seed.nextInt(4);
			int y = seed.nextInt(4);
			if(board[x][y]==null){
				Pit p = new Pit();
				board[x][y] = p;
				System.out.println("Pp: "+x+","+y);
			}
			else{
				i--;
			}
		}
		
		//initiate player's position
		do{
			px = seed.nextInt(4);
			py = seed.nextInt(4);	
		}while(board[px][py]!=null);
		System.out.println("Player: "+px+","+py);
		
		// initiate ClearGrounds
		for(int i=0;i<4;i++){
			for(int j=0;j<4;j++){
				if(board[i][j]==null){
					ClearGround cg = new ClearGround();
					board[i][j] = cg;
				}
			}
		}
	}
	/* +---+---+---+---+
	 * |   |   |   |   |
	 * +---+---+---+---+
	 * |   |   |   |   |
	 * +---+---+---+---+
	 * |   |   |   |   |
	 * +---+---+---+---+
	 * |   |   |   |   |
	 * +---+---+---+---+
	 */ 
	private void display(){
		System.out.println("+---+---+---+---+");
		for(int i=0;i<4;i++){
			System.out.printf("|");
			for(int j=0;j<4;j++){
				if(i==px&&j==py)
					System.out.printf(" * |");
				else if(reveal)
					System.out.printf(" %c |",board[i][j].show());
				else
					System.out.printf("   |");
			}
			System.out.printf("\n");
			if(i<3) {
				System.out.println("+---+---+---+---+");
			}
		}
		System.out.println("+---+---+---+---+");
	}
	private void senseNearby(){
		boolean senseWumpus = false,
				senseGold = false,
				sensePit = false;
		GameItem[] gi = {board[px][(py+3)%4],board[px][(py+1)%4],board[(px+3)%4][py],board[(px+1)%4][py]};
		for(int i=0;i<4;i++){
			if(gi[i] instanceof Gold){
				senseGold = true;
			}
			else if(gi[i] instanceof Wumpus){
				senseWumpus = true;
			}
			else if(gi[i] instanceof Pit){
				sensePit = true;
			}
		}
		if(senseWumpus){
			System.out.println("A vale smell.");
		}
		if(sensePit){
			System.out.println("A breeze.");
		}
		if(senseGold){
			System.out.println("A faint glitter!!");
		}
		System.out.println();
	}
	private void menu(){
		System.out.println(MENUTITLE);
		for(int i=0;i<OPTION.length;i++){
			System.out.println(OPTION[i]);
		}
	}
	public void runGame(){
		Scanner reader = new Scanner(System.in);
		int option;
		boolean continueInput = true;
		setBoard();
		do{
			display();
			senseNearby();
			menu();
			do {
		        try{
		            option = reader.nextInt();
		            switch(option){
					case 1:
						py = (py+3)%4;
						break;
					case 2:
						py = (py+1)%4;
						break;
					case 3:
						px = (px+3)%4;
						break;
					case 4:
						px = (px+1)%4;
						break;
					case 5:
						running = false;
						break;
					case 6:
						reveal = true;
						break;
					default:
						System.out.println("Please chose a correct option.");
						break;
					}
		            continueInput = false;
		        }
		        catch (InputMismatchException ex) {
		            System.out.println("Try again. (" +
		                    "Incorrect input: a correct option is required)");
		            reader.nextLine();		       
		        }
		    }while (continueInput);
			// Check game status here
			if(board[px][py] instanceof Gold){
				board[px][py] = new ClearGround();
				pieceOfGolds--;
			}
			else if(board[px][py] instanceof Wumpus){
				running = false;
				System.out.println("Failed! You are dead due to deadly attack of a wumpus!");
			}
			else if(board[px][py] instanceof Pit){
				running = false;
				System.out.println("Failed! You are dead in a deep pit!");
			}
			if(pieceOfGolds==-1){
				running = false;
				System.out.println("Congratulations! You've completed this game!");
			}
			continueInput = true;
		}while(running);
	}
}
