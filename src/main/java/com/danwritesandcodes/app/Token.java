package com.danwritesandcodes.app;

class Token {
    public final String text;
    public final String cleantext;
    private final int index;
    public final boolean isLikelyCommand;
    public final boolean isLikelyOption;

    public Token(String text, String cleantext, int index, boolean isLikelyCommand, boolean isLikelyOption) {
        this.text = text;
        this.cleantext = cleantext;
        this.index = index;
        this.isLikelyCommand = isLikelyCommand;
        this.isLikelyOption = isLikelyOption;
    }

    @Override
    public String toString() {
        return "Token{" +
                "text='" + text + '\'' +
                ", cleantext='" + cleantext + '\'' +
                ", index=" + index +
                ", isLikelyCommand=" + isLikelyCommand +
                ", isLikelyOption=" + isLikelyOption +
                '}';
    }
}
