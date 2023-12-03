package repositories.productRepo;

import models.products.Product;

import java.util.List;

public interface IProductRepository {

        Product addProduct(Product product);

        public List<Product> getAllProducts();

        Product getProduct(int id);

        Product getProductByName(String name);

        Product updateProduct(Product product);
        void deleteProduct(String name);


}
