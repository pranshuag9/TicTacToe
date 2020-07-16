package com.techlab.tictactoe;

public class TicTacToe implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private int BOARD_SIZE;
	private Mark[][] board;
	private GameStatus gameStatus;
	private String playerX;
	private String player0;
	private GamePrinter printer = new GamePrinter(this);
	private java.util.Scanner sc = new java.util.Scanner(System.in);

	private class PositionIsFilledException extends Exception {
		private static final long serialVersionUID = 1L;

		private PositionIsFilledException(String message) {
			super(message);
		}
	}

	private class GamePrinter {
		TicTacToe game;

		GamePrinter(TicTacToe game) {
			this.game = game;
		}

		void printTurn(String playerX, String player0) {
			if (game.getGameStatus() == GameStatus.INCOMPLETE)
				System.out.println((game.getXTurn() ? playerX : player0) + "'s Turn");
			else if (game.getGameStatus() != GameStatus.TIE)
				System.out.println((game.getGameStatus() == GameStatus.XWINS ? playerX : player0) + " is the Winner");
			else
				System.out.println("It is a Tie");
		}

		void printBoard() {
			Mark[][] board = game.getBoard();
			for (int i = 0; i < board.length; i++) {
				System.out.print(" ");
				for (int j = 0; j < board[i].length; j++) {
					System.out.print((board[i][j] != Mark.NULL ? board[i][j] : " "));
					System.out.print((j < (board[i].length - 1)) ? (" | ") : (""));
				}
				System.out.println();
				if(i<board.length-1) for (int j = 0; j < board[i].length; j++) System.out.print("----");
				System.out.println();
			}
			System.out.println();
		}

		void makeMove() {
			System.out.println("Enter Row No. and Column No. in range 0 to " + (game.getBoardSize() - 1) + ": ");
		}

		void endGreeting() {
			System.out.println("Game has ended...");
		}

		void takePlayerName(String str) {
			System.out.println("Enter Player " + str + " name: ");
		}

		void startGreeting() {
			System.out.println("Game has started...");
		}

		void takeBoardSize() {
			System.out.println("Enter Board Size: ");
		}
	}

	private Mark[][] createBoard(int size) {
		return new Mark[size][size];
	}

	private boolean XTurn;

	private void initializeBoard(Mark[][] board) {
		for (int i = 0; i < board.length; i++)
			for (int j = 0; j < board[i].length; j++)
				board[i][j] = Mark.NULL;
	}

	public TicTacToe() {
		printer.takePlayerName("X");
		this.playerX = sc.nextLine();
		printer.takePlayerName("0");
		this.player0 = sc.nextLine();
		printer.takeBoardSize();
		int board_size = takeInput();
		this.BOARD_SIZE = board_size > 2 ? board_size : 3;
		this.XTurn = true;
		this.board = createBoard(BOARD_SIZE);
		initializeBoard(board);
		this.gameStatus = GameStatus.INCOMPLETE;
	}

	public int getBoardSize() {
		return this.BOARD_SIZE;
	}

	public String getPlayerX() {
		return this.playerX;
	}

	public String getPlayer0() {
		return this.player0;
	}

	public void startPlay() {
		printer.startGreeting();
		while (this.gameStatus == GameStatus.INCOMPLETE) {
			printer.printTurn(getPlayerX(), getPlayer0());
			printer.makeMove();
			int row = takeInput();
			int col = takeInput();
			while (row < 0 || col < 0 || row > BOARD_SIZE || col > BOARD_SIZE) {
				System.err.println("Row no. or Column no. out of range. Try Again!");
				printer.makeMove();
				row = takeInput();
				col = takeInput();
			}
			boolean flag = makeMove(row, col, true);
			while (flag) {
				printer.makeMove();
				row = takeInput();
				col = takeInput();
				flag = makeMove(row, col, flag);
			}
			printer.printBoard();
		}
		printer.printTurn(getPlayerX(), getPlayer0());
		printer.endGreeting();
		sc.close();
	}

	private int takeInput() {
		while (!sc.hasNextInt())
			sc.next();
		int x = sc.nextInt();
		return x;
	}

	private boolean getXTurn() {
		return this.XTurn;
	}

	private void changeTurn() {
		this.XTurn = !this.XTurn;
	}

	private Mark[][] getBoard() {
		return this.board;
	}

	private GameStatus getGameStatus() {
		Mark m1, m2;
		int row = 0, col = 0;

		// CHECKING HORIZONTAL ROWS
		row = 0;
		while (row < BOARD_SIZE) {
			col = 0;
			while (col < BOARD_SIZE - 1) {
				m1 = board[row][col];
				m2 = board[row][col + 1];
				if (m1 != m2) {
					break;
				}
				col++;
			}
			if (col == BOARD_SIZE - 1) {
				if (board[row][col] == Mark.X) {
					return GameStatus.XWINS;
				} else if (board[row][col] == Mark.O) {
					return GameStatus.OWINS;
				}
			}
			row++;
		}

		// CHECKING VERTICAL COLUMNS
		col = 0;
		while (col < BOARD_SIZE) {
			row = 0;
			while (row < BOARD_SIZE - 1) {
				m1 = board[row][col];
				m2 = board[row + 1][col];
				if (m1 != m2) {
					break;
				}
				row++;
			}
			if (row == BOARD_SIZE - 1) {
				if (board[row][col] == Mark.X) {
					return GameStatus.XWINS;
				} else if (board[row][col] == Mark.O) {
					return GameStatus.OWINS;
				}
			}
			col++;
		}

		// CHECKING BACKWARD DIAGONAL
		row = 0;
		col = 0;
		while (row < BOARD_SIZE - 1 && col < BOARD_SIZE - 1) {
			m1 = board[row][col];
			m2 = board[row + 1][col + 1];
			if (m1 != m2) {
				break;
			}
			row++;
			col++;
		}
		if (row == BOARD_SIZE - 1) {
			if (board[row][col] == Mark.X) {
				return GameStatus.XWINS;
			} else if (board[row][col] == Mark.O) {
				return GameStatus.OWINS;
			}
		}

		// CHECKING FORWARD DIAGONAL
		row = BOARD_SIZE - 1;
		col = 0;
		while (row > 0 && col < BOARD_SIZE - 1) {
			m1 = board[row][col];
			m2 = board[row - 1][col + 1];
			if (m1 != m2) {
				break;
			}
			row--;
			col++;
		}
		if (row == 0) {
			if (board[row][col] == Mark.X) {
				return GameStatus.XWINS;
			} else if (board[row][col] == Mark.O) {
				return GameStatus.OWINS;
			}
		}

		// CHECKING NULL SPACES
		for (row = 0; row < BOARD_SIZE; row++) {
			for (col = 0; col < BOARD_SIZE; col++) {
				Mark m = board[row][col];
				if (m == Mark.NULL) {
					return GameStatus.INCOMPLETE;
				}
			}
		}
		return GameStatus.TIE;
	}

	private void move(int row, int col) throws Exception {
		if (board[row][col] != Mark.NULL)
			throw new PositionIsFilledException("Invalid Move");
		else {
			board[row][col] = getXTurn() ? Mark.X : Mark.O;
			changeTurn();
			this.gameStatus = getGameStatus();
		}
	}

	private boolean makeMove(int row, int col, boolean flag) {
		if (this.gameStatus != GameStatus.INCOMPLETE)
			flag = false;
		try {
			move(row, col);
			return false;
		} catch (Exception e) {
			System.err.println(e + ". Try Again!");
		}
		return true;
	}
}
