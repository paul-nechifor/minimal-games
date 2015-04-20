package ro.minimul.mg.tetravex;

public enum GuiTheme {
    WHITE_SATURATED(
            "White Saturated",
            0xFFFFFFFF,
            0xFFDDDDDD,
            0xFFFFFFFF,
            0xFF000000,
            new long[] {0xfffb4b2d, 0xffdb6e2c, 0xfffb9928, 0xfff3c71c,
                    0xffa7c71c, 0xff809921, 0xff86c1a1, 0xff7241bc, 0xffc53aa9,
                    0xffff3a90}
    ),
    WHITE_NORMAL(
            "White Normal",
            0xFFEEEEEE,
            0xFFCCCCCC,
            0xFFEEEEEE,
            0xFF222222,
            new long[] {0xffee715b, 0xffd08253, 0xffeea858, 0xffe6c74d,
                    0xffa6bd43, 0xff7f913b, 0xff8db7a0, 0xff7d5ab2, 0xffbb58a7,
                    0xfff265a3}
    ),
    BLACK(
            "Black",
            0xFF000000,
            0xFF222222,
            0xFFAAAAAA,
            0xFFDDDDDD,
            new long[] {0xffaf4d3c, 0xff995c37, 0xffaf7839, 0xffaa9131,
                    0xff798b2b, 0xff5d6b27, 0xff668775, 0xff5a3e83, 0xff893c7a,
                    0xffb24474}
    );
    
    public final String name;
    public final long boardBackground;
    public final long boardForeground;
    public final long pieceBackground;
    public final long textPaint;
    public final long[] colors;
    
    private GuiTheme(String name, long boardBackground, long boardForeground,
            long pieceBackground, long textPaint, long[] colors) {
        this.name = name;
        this.boardBackground = boardBackground;
        this.boardForeground = boardForeground;
        this.pieceBackground = pieceBackground;
        this.textPaint = textPaint;
        this.colors = colors;
    }
}
