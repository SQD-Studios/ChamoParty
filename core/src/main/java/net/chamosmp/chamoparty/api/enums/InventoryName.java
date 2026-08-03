package net.chamosmp.chamoparty.api.enums;

public enum InventoryName {

	VOTE("vote"),
	
	;

	private final String name;

	/**
	 * @param name
	 */
    InventoryName(String name) {
		this.name = name;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

}
