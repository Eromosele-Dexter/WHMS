package strategies.restock;

import models.Product;
import services.ProductService;

public class RestockByMachine implements IRestockOperationStrategy{
    /**
     * @param product
     */
    @Override
    public void restock(ProductService productService, Product product) {

        int restockingSchedule = product.getRestockSchedule();

        int maxStockQuantity = product.getTargetMaxStockQuantity();

        System.out.println("machine");

        int totalQuantityToBeRestocked = maxStockQuantity- product.getCurrentStockQuantity();

        System.out.println("totalQuantityToBeRestocked : " + totalQuantityToBeRestocked );

        double numberOfRestockOperations = (double) totalQuantityToBeRestocked / restockingSchedule;

        System.out.println("ceil: " + Math.ceil(numberOfRestockOperations));

        System.out.println("schedule: "+ restockingSchedule);

        int productId = product.getProductId();

        String productType = product.getProductType();

        System.out.println("number: " + numberOfRestockOperations);

        for(int i=0; i<numberOfRestockOperations;i++) {

            int quantityToBeRestocked = (i < numberOfRestockOperations-1) ? restockingSchedule: (totalQuantityToBeRestocked % restockingSchedule);


            System.out.println("restocked: "+quantityToBeRestocked);

            product.setCurrentStockQuantity(product.getCurrentStockQuantity() + quantityToBeRestocked);

            System.out.println(product.getCurrentStockQuantity());


            Product productToBeRestocked = productService.getProductsFactoryMap().get(productType).createProduct(
                    product.getProductName(), product.getUnitPrice(), product.getCurrentStockQuantity(),
                    maxStockQuantity, product.getTargetMinStockQuantity(), restockingSchedule, product.getDiscountStrategyId(),
                    productType
            );

            productService.handleUpdateProduct(productToBeRestocked, productId);

        }

    }

}
