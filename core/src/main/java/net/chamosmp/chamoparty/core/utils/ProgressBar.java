package net.chamosmp.chamoparty.core.utils;

public class ProgressBar {

	private final int lenght;
	private final char symbol;
	private final String completedColor;
	private final String notCompletedColor;

	/**
	 * @param length
	 * @param symbol
	 * @param completedColor
	 * @param notCompletedColor
	 */
	public ProgressBar(int length, char symbol, String completedColor, String notCompletedColor) {
		super();
		this.lenght = length;
		this.symbol = symbol;
		this.completedColor = completedColor;
		this.notCompletedColor = notCompletedColor;
	}

	/**
	 * @return the length
	 */
	public int getLength() {
		return lenght;
	}

	/**
	 * @return the symbol
	 */
	public char getSymbol() {
		return symbol;
	}

	/**
	 * @return the completedColor
	 */
	public String getCompletedColor() {
		return completedColor;
	}

	/**
	 * @return the notCompletedColor
	 */
	public String getNotCompletedColor() {
		return notCompletedColor;
	}

}
