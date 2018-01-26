package Parser.IntermediateCode;

public class ThirdBF extends Third {

    public ThirdBF(){
        typeThird = "BF";
    }

    @Override
    public String generateAssembler() {
        return " label"+this.value2+"\n";
    }
}