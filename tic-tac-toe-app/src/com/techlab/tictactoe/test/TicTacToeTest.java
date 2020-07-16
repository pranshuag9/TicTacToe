package com.techlab.tictactoe.test;

import com.techlab.tictactoe.TicTacToe;

public class TicTacToeTest {
	public static void startGame(TicTacToe game) {
		game.startPlay();
	}
	public static void main(String[] args) {
		TicTacToe game1 = new TicTacToe();
		startGame(game1);
	}
}
