package de.mcdorians.codebase.locbano;

import java.util.UUID;

public class TokenGenerator {

	public static String generateToken() {
		return UUID.randomUUID().toString();
	}

}
