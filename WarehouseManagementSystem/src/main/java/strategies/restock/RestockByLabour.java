package strategies.restock;

import models.Product;
import services.ProductService;

public class RestockByLabour implements IRestockOperationStrategy{
    /**
     * @param product
     */
    @Override
    public void restock(ProductService productService, Product product) {

        int restockingSchedule = product.getRestockSchedule();

        int maxStockQuantity = product.getTargetMaxStockQuantity();

        int numberOfRestockOperations = ((int)Math.ceil(maxStockQuantity / restockingSchedule));

        int productId = product.getProductId();

        String productType = product.getProductType();

        for(int i=0; i<numberOfRestockOperations;i++) {

            int quantityToBeRestocked = (i < numberOfRestockOperations-1) ? restockingSchedule: (maxStockQuantity % restockingSchedule);

            Product productToBeRestocked = productService.getProductsFactoryMap().get(productType).createProduct(
                    product.getProductName(), product.getUnitPrice(), quantityToBeRestocked,
                    maxStockQuantity, product.getTargetMinStockQuantity(), restockingSchedule, product.getDiscountStrategyId(),
                    productType
            );

            productService.handleUpdateProduct(productToBeRestocked, productId);

            try {
                Thread.sleep(1000);             // delay by 1 seconds cause its labour
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

        }


    }
}
