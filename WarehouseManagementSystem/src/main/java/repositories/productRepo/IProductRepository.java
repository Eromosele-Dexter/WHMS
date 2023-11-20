package repositories.productRepo;

import models.Product;

public interface IProductRepository {

        Product addProduct(Product product);
        Product getProduct(int id);

        Product getProductByName(String name);
        Product updateProduct(Product product);
        void deleteProduct(String name);


}
