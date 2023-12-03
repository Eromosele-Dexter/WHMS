package services;

import apiContracts.Requests.AddProductRequest;
import apiContracts.Requests.GetProductRequest;
import apiContracts.Responses.AddProductResponse;
import apiContracts.Responses.GetProductResponse;
import factories.productFactories.ElectronicFactory;
import factories.productFactories.FurnitureFactory;
import factories.productFactories.GeneralFactory;
import factories.productFactories.ProductFactory;
import models.products.Product;
import repositories.productRepo.IProductRepository;
import statics.factoryKeys;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductService {
    private static IProductRepository productRepository;

    private Map<String, ProductFactory> productsFactoryMap;



    public ProductService(IProductRepository productRepository){
        this.productRepository = productRepository;

        productsFactoryMap = new HashMap<>();

        productsFactoryMap.put(factoryKeys.ELECTRONIC, new ElectronicFactory());

        productsFactoryMap.put(factoryKeys.FURNITURE, new FurnitureFactory());

        productsFactoryMap.put(factoryKeys.GENERAL, new GeneralFactory());

    }

    public AddProductResponse handleCreateProduct(AddProductRequest addProductRequest){

        if(addProductRequest.getProductName() == null){
            return null;
        }

        if(addProductRequest.getProductType() == null){
            addProductRequest.setProductType(factoryKeys.GENERAL);
        }

        String productName = addProductRequest.getProductName();

        double unitPrice = addProductRequest.getUnitPrice();


        int currentStockQuantity = addProductRequest.getCurrentStockQuantity();

        int targetMaxStockQuantity = addProductRequest.getTargetMaxStockQuantity();

        int targetMinStockQuantity = addProductRequest.getTargetMinStockQuantity();

        int restockSchedule = addProductRequest.getRestockSchedule();

        int discountStrategyId = addProductRequest.getDiscountStrategyId();

        String productType  = addProductRequest.getProductType().toLowerCase().trim();

        ProductFactory productFactory = this.productsFactoryMap.get(productType);

        Product product = productFactory.createProduct(productName, unitPrice, currentStockQuantity, targetMaxStockQuantity, targetMinStockQuantity, restockSchedule, discountStrategyId, productType);



        Product createdProduct = this.productRepository.addProduct(product);

        return createdProduct == null ? null : new AddProductResponse(createdProduct.getProductId(), createdProduct.getProductName(),
                createdProduct.getUnitPrice(), createdProduct.getCurrentStockQuantity(), createdProduct.getTargetMaxStockQuantity(),
                createdProduct.getTargetMinStockQuantity(), createdProduct.getRestockSchedule(), createdProduct.getDiscountStrategyId(),
                createdProduct.getProductType());
    }

    public List<GetProductResponse>handleRetrieveAllProducts(){
        List<GetProductResponse> productsList = new ArrayList<>();

        List<Product>products = this.productRepository.getAllProducts();



        for(int i=0; i<products.size(); i++){
            Product product = products.get(i);


            GetProductResponse getProductResponse = new GetProductResponse(product.getProductId(), product.getProductName(), product.getUnitPrice(),
                    product.getCurrentStockQuantity(), product.getTargetMaxStockQuantity(), product.getTargetMinStockQuantity(),
                    product.getRestockSchedule(), product.getDiscountStrategyId(), product.getProductType());


            productsList.add(getProductResponse);
        }

        return productsList;
    }

    public GetProductResponse handleRetrieveProduct(GetProductRequest getProductRequest){
        int id = getProductRequest.getId();

        Product product = this.productRepository.getProduct(id);

        return product == null ? null : new GetProductResponse(product.getProductId(), product.getProductName(), product.getUnitPrice(),
                product.getCurrentStockQuantity(), product.getTargetMaxStockQuantity(), product.getTargetMinStockQuantity(),
                product.getRestockSchedule(), product.getDiscountStrategyId(), product.getProductType());
    }

    public GetProductResponse handleRetrieveProduct(String name){
        Product product = this.productRepository.getProductByName(name);

        return product == null ? null : new GetProductResponse(product.getProductId(), product.getProductName(), product.getUnitPrice(),
                product.getCurrentStockQuantity(), product.getTargetMaxStockQuantity(), product.getTargetMinStockQuantity(),
                product.getRestockSchedule(), product.getDiscountStrategyId(), product.getProductType());
    }

    public Product handleGetProduct(String name){
        return this.productRepository.getProductByName(name);
    }

    public void handleUpdateProduct(Product updatedProduct, int id){
        updatedProduct.setProductId(id);

        this.productRepository.updateProduct(updatedProduct);
    }

    public Map<String, ProductFactory> getProductsFactoryMap() {
        return productsFactoryMap;
    }
}
