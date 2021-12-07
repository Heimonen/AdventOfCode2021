package com.heimonen;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Board {
	private static final int BOARD_WIDTH = 5;
	private static final int BOARD_HEIGHT = 5;

	private final Entry[][] board;
	private int currentLine;
	private int score;

	public Board(String firstLine) {
		this.board = new Entry[BOARD_HEIGHT][BOARD_WIDTH];
		this.currentLine = 0;
		this.addLine(firstLine);
	}

	public void addLine(String line) {
		String[] entries = line.trim().split("\\s+");
		for (int i = 0; i < entries.length; i++) {
			this.board[currentLine][i] = new Entry(Integer.parseInt(entries[i]));
		}
		this.currentLine++;
	}

	/**
	 * Checks whether or not all entries have been entered into the board.
	 * @return Returns true if the board is complete, false otherwise
	 */
	public boolean hasAllEntries() {
		return this.currentLine == BOARD_HEIGHT;
	}

	/**
	 * Checks whether the board already has bingo
	 * @return true if it has, false otherwise
	 */
	public boolean hasWon() {
		return this.score != 0;
	}

	/**
	 * Marks the entry and checks whether that board won
	 * @param value the value of the entry to mark
	 * @return true if a win, false otherwise
	 */
	public boolean mark(int value) {
		// Inefficient
		for (int y = 0; y < BOARD_HEIGHT; y++) {
			for (int x = 0; x < BOARD_WIDTH; x++) {
				Entry entry = getEntry(y, x);
				if (entry.getValue() == value) {
					entry.mark();
				}
			}
		}
		return evaluateBoard();
	}

	public void print() {
		StringBuilder sb = new StringBuilder();
		for (int y = 0; y < BOARD_HEIGHT; y++) {
			for (int x = 0; x < BOARD_WIDTH; x++) {
				Entry entry = getEntry(y, x);
				if (entry.isMarked()) {
					sb.append("[" + getEntry(y, x).getValue() + "] ");
				} else {
					sb.append(getEntry(y, x).getValue() + " ");
				}
			}
			sb.append("\n");
		}
		System.out.println(sb);
	}

	public int getScore() {
		return this.score;
	}

	public void calculateScore(int lastNumber) {
		int sumOfUnmarked = Arrays.stream(this.board)
				.flatMap(Arrays::stream)
				.collect(Collectors.toList()).stream().filter(it -> !it.isMarked()).mapToInt(Entry::getValue).sum();
		this.score = sumOfUnmarked * lastNumber;

	}

	/**
	 * Returns true if there is a bingo
	 * @return true if bingo, false otherwise
	 */
	public boolean evaluateBoard() {
		for (int i = 0; i < BOARD_HEIGHT; i++) {
			boolean bingoHorizontal = true;
			boolean bingoVertical = true;
			for (int j = 0; j < BOARD_WIDTH; j++) {
				if (!this.getEntry(i, j).isMarked()) {
					bingoHorizontal = false;
				}
				if (!this.getEntry(j, i).isMarked()) {
					bingoVertical = false;
				}
				if (!bingoHorizontal && !bingoVertical) {
					break;
				}
			}
			if (bingoHorizontal || bingoVertical) {
				return true;
			}
		}
		return false;
	}

	private Entry getEntry(int y, int x) {
		return this.board[y][x];
	}

}
