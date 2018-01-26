package Parser.IntermediateCode;

public class ThirdOut extends Third {

    private int stPos = 0;

    public ThirdOut(String str, int stPos) {
        this.typeThird = "OUT";
        this.value1 = str;
        this.stPos = stPos;
    }

    private String getAddress() {
        return "ST_STRING_ID_"+ this.stPos;
    }

    @Override
    public String generateAssembler() {
        String assembler = "\t\tinvoke MessageBox, NULL, addr "+this.getAddress() + ", addr "+this.getAddress()+", MB_OK\n";
        return (this.isLabeled()) ? assembler+"\t\tlabel"+this.getLabel()+":\n" : assembler;
    }
}
