package org.example.model.enums;

public enum ConstraintType {
	PRIMARY_KEY, FOREIGN_KEY, CHECK, UNIQUE;

	public static ConstraintType of(Driver driver, String constraintType) {
		return switch (driver) {
			case POSTGRESQL -> {
				if (constraintType.equalsIgnoreCase("PRIMARY KEY")) yield ConstraintType.PRIMARY_KEY;
				else if (constraintType.equalsIgnoreCase("FOREIGN KEY")) yield ConstraintType.FOREIGN_KEY;
				else if (constraintType.equalsIgnoreCase("CHECK")) yield ConstraintType.CHECK;
				else if (constraintType.equalsIgnoreCase("UNIQUE")) yield ConstraintType.UNIQUE;
				else throw new IllegalArgumentException("constraintType is not supported");
			}
			default -> throw new IllegalArgumentException("driver is not supported");
		};
	}
}
