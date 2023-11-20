package strategies.restock;

import models.Product;

public class RestockByMachine implements IRestockOperationStrategy{
    /**
     * @param product
     */
    @Override
    public Product restock(Product product) {

        Product restockedProduct = null;

        //TODO:            The restocking process brings the product stock back to its max stock quantity as this is specified in the database.
//                    The restocking of a product is based on the restocking schedule of the product as also specified in the database.
//                    For example, if there must be 100 products to be reordered for the product o be restocked to its max quantity,
//                    and the restocking schedule is 30 products, them four restocking operations have to be performed, three
//            operations of 30 products each, and one of 10 products. Once the restocking operation is completed then the
//            server UI is updated with the new product quantities (see Fig. 3).

        int restockSchedule = product.getRestockSchedule();
//        product.setCurrentStockQuantity();

        return restockedProduct;
    }
}
