package Parser.IntermediateCode;

public class ThirdBI extends Third {

    public ThirdBI(){
        typeThird = "BI";
    }

    @Override
    public String generateAssembler() {
        return "\t\tJMP label"+this.value1+"\n\t\tlabel"+this.getLabel()+":\n";
    }
}