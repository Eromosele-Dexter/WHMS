package strategies.restock;

import models.Product;

public class RestockByLabour implements IRestockOperationStrategy{
    /**
     * @param product
     */
    @Override
    public Product restock(Product product) {
        // delay by 2 seconds cause its labour
        // TODO: handle restock

        return  null;
    }
}
