package strategies.restock;

import models.products.Product;
import services.ProductService;

public interface IRestockOperationStrategy {
    public void restock(ProductService productService, Product product);
}
