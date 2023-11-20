package strategies.restock;

import models.Product;

public interface IRestockOperationStrategy {
    public Product restock(Product product);
}
