package Game;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class Shape implements Subject {
	private Board board;
	private Color color;
	private int x = 4, y = 0;

	private int deltaX = 0;
	private boolean checkScore;
	private boolean collision = false;

	private int[][] coords;

	private ArrayList<Observer> observers;

	public Shape() {
		observers = new ArrayList<Observer>();
	}

	public Shape(int[][] coords, Board board, Color color) {
		this.coords = coords;
		this.board = board;
		this.color = color;

	}

	public void update() {

		if (collision) {
			for (int row = 0; row < coords.length; row++) {
				for (int col = 0; col < coords[0].length; col++) {
					if (coords[row][col] != 0) {

						board.getBoard()[y + row][x + col] = color;

					}
				}
			}

			checkLineAndGetScore();
			board.setCurrentShape();
			return;
		}

		boolean moveX = false;
		// check le trai phai
		if (x + deltaX + coords[0].length <= 10 && x + deltaX >= 0) {
			// check block ben canh
			for (int row = 0; row < coords.length; row++) {
				for (int col = 0; col < coords[row].length; col++) {
					if (board.getBoard()[y + row][x + deltaX + col] != null) {
						moveX = true;
					}
				}
			}
			if (!moveX) {
				x += deltaX;

			}
		}
		deltaX = 0;

		if (System.currentTimeMillis() - board.getBeginTime() > board.getDelayTimeForMovement()) {
			// check le duoi
			if (y + 1 + coords.length < Board.BOARD_HEIGHT) {
				// check block duoi
				for (int row = 0; row < coords.length; row++) {
					for (int col = 0; col < coords[0].length; col++) {
						if (coords[row][col] != 0) {
							if (board.getBoard()[y + 1+ row][x + deltaX + col] != null) {
								collision = true;
							}
						}
					}
				}
				if (!collision) {
					y++;
				}
			} else
				collision = true;

			board.setBeginTime(System.currentTimeMillis());
		}
	}

	public void checkLineAndGetScore() {
		// TODO Auto-generated method stub

		int bottomLine = board.getBoard().length - 1;

		for (int topLine = board.getBoard().length - 1; topLine > 0; topLine--) {
			int count = 0;
			for (int col = 0; col < board.getBoard()[0].length; col++) {
				checkScore = false;
				if (board.getBoard()[topLine][col] != null) {
					count++;

				}

				board.getBoard()[bottomLine][col] = board.getBoard()[topLine][col];

				// neu block lap day 1 hang thi check = true
				if (count == 10) {
					checkScore = true;
				}
			}
			// theo tren check bang true tuc la du 1 hang thi score tang them 10
			if (checkScore) {
				board.setScore(board.getScore() + 10);
			}
			if (count < 10) {
				bottomLine--;
			}
		}
	}

	public void render(Graphics g) {
//		draw shape
		g.setColor(color);
		for (int row = 0; row < coords.length; row++) {
			for (int col = 0; col < coords[0].length; col++) {
				if (coords[row][col] != 0) {
					g.fillRect(col * Board.BLOCK_SIZE + x * Board.BLOCK_SIZE,
							row * Board.BLOCK_SIZE + y * Board.BLOCK_SIZE, Board.BLOCK_SIZE, Board.BLOCK_SIZE);
				}
			}
		}

	}

	public void rotateShape() {
		int[][] rotatedShape = transposeMatrix(coords);
		reverseRows(rotatedShape);

		// check le trai phai khi xoay
		if (rotatedShape[0].length + x + deltaX > 10 || y + rotatedShape.length > 19) {
			return;
		}
		for (int row = 0; row < rotatedShape.length; row++) {
			for (int col = 0; col < rotatedShape[0].length; col++) {
				if (rotatedShape[row][col] != 0) {
					if (board.getBoard()[y + row][x + col] != null) {
						return;
					}
				}
			}
		}
		coords = rotatedShape;
	}

	public int[][] transposeMatrix(int[][] matrix) {
		int[][] result = new int[matrix[0].length][matrix.length];
		for (int row = 0; row < matrix.length; row++) {
			for (int col = 0; col < matrix[0].length; col++) {
				result[col][row] = matrix[row][col];
			}
		}
		return result;
	}

	public void reverseRows(int[][] matrix) {
		int middleRows = matrix.length - 1;
		for (int row = 0; row < middleRows; row++) {
			int[] temp = matrix[row];
			matrix[row] = matrix[matrix.length - 1 - row];
			matrix[matrix.length - 1 - row] = temp;
		}

	}

	public void speedUp() {
		board.setDelayTimeForMovement(board.getFast());
	}

	public void speedDown() {
		board.setDelayTimeForMovement(board.getNormal());
	}

	public void moveLeft() {
		deltaX = -1;
	}

	public void moveRight() {
		deltaX = 1;
	}

	@Override
	public void registerObserver(Observer o) {
		// TODO Auto-generated method stub
		observers.add(o);
	}

	@Override
	public void removeObserver(Observer o) {
		// TODO Auto-generated method stub
		observers.remove(o);
	}

	@Override
	public void notifyObserver() {
		// TODO Auto-generated method stub
		for (Observer observer : observers) {
			observer.update();
		}
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void reset() {
		// TODO Auto-generated method stub
		this.x = 4;
		this.y = 0;
		this.collision = false;

	}

	public int[][] getCoords() {
		return coords;
	}

	public Color getColor() {
		return color;
	}

}
