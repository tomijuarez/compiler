package Parser.IntermediateCode;

public class StackReference {

    private String ref;

    public StackReference(String ref) {
        this.ref = ref;
    }

    public StackReference(StackReference ref) {
        this.ref = ref.getReference();
    }

    public String getReference() {
        return this.ref;
    }

    @Override
    public String toString() {
        return '['+this.ref+']';
    }

    public int getRef() {
        return Integer.valueOf(this.ref);
    }

}
