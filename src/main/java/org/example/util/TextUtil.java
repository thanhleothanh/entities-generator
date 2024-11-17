package org.example.util;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.CaseUtils;

public abstract class TextUtil {

	public static final List<String> JAVA_IDENTIFIERS = List.of(
			"abstract", "continue", "for", "new", "switch",
			"assert", "default", "goto", "package", "synchronized",
			"boolean", "do", "if", "private", "this",
			"break", "double", "implements", "protected", "throw",
			"byte", "else", "import", "public", "throws",
			"case", "enum", "instanceof", "return", "transient",
			"catch", "extends", "int", "short", "try",
			"char", "final", "interface", "static", "void",
			"class", "finally", "long", "strictfp", "volatile",
			"const", "float", "native", "super", "while", "null"
	);
	public static final String SPECIAL_CHARACTER_REGEX = "[^a-zA-Z0-9_ ]";
	public static final String START_WITH_NUMBER_REGEX = "^[0-9].*";

	private TextUtil() {}

	public static String toCamelCase(String input, boolean capitalizeFirstCharacter) {
		if (StringUtils.isBlank(input)) {
			return "";
		}
		String result = CaseUtils.toCamelCase(removeSpecialCharacters(input), capitalizeFirstCharacter, '_', ' ');
		if (JAVA_IDENTIFIERS.stream().anyMatch(result::equals)) return result + "_";
		else if (result.matches(START_WITH_NUMBER_REGEX)) return "_" + result;
		else return result;
	}

	public static String removeSpecialCharacters(String input) {
		Pattern pattern = Pattern.compile(SPECIAL_CHARACTER_REGEX);
		Matcher matcher = pattern.matcher(input);
		return matcher.replaceAll("");
	}
}
