package strategies.restock;

import models.Product;
import services.ProductService;

public interface IRestockOperationStrategy {
    public void restock(ProductService productService, Product product);
}
