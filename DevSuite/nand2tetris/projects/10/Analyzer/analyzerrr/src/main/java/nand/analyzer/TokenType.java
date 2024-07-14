package nand.analyzer;
public enum TokenType {
    KEYWORD,
    SYMBOL,
    IDENTIFIER,
    INTEGER_CONSTANT,
    STRING_CONST, 
    COMPOUND;
	
	@Override
	public String toString() {
		if(this.name().contains("_")) {
			String[] splitName = this.name().split("\\_");
			String firstCharacter = ("" + splitName[1].charAt(0)).toUpperCase();
			String name = splitName[0].toLowerCase() + firstCharacter + splitName[1].substring(1).toLowerCase();
			return name;
		} else {
			return super.toString().toLowerCase();
		}
	}
}
