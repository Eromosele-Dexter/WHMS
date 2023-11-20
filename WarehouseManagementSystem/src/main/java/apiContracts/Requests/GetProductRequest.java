package apiContracts.Requests;

public class GetProductRequest extends Request{

    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
