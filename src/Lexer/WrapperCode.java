package Lexer;

public class WrapperCode {
    private String code;
    private String buffer;
    private int line = 1;
    private int errors = 0;

    public WrapperCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getBuffer() {
        return buffer;
    }

    public void setBuffer(String buffer) {
        this.buffer = buffer;
    }

    public int getLine() {
        return this.line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public void addLine() {
        this.line++;
    }

    public int getErrors() {
        return errors;
    }

    public void addError() {
        this.errors++;
    }

}
