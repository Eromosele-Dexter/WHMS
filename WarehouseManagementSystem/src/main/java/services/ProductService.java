package services;

import apiContracts.Requests.AddProductRequest;
import apiContracts.Requests.GetProductRequest;
import apiContracts.Responses.AddProductResponse;
import apiContracts.Responses.GetProductResponse;
import factories.productFactories.ElectronicFactory;
import factories.productFactories.FurnitureFactory;
import factories.productFactories.ProductFactory;
import models.Product;
import repositories.productRepo.IProductRepository;

import java.util.HashMap;
import java.util.Map;

public class ProductService {
    private static IProductRepository productRepository;

    private Map<String, ProductFactory> productsFactoryMap;



    public ProductService(IProductRepository productRepository){
        this.productRepository = productRepository;
        productsFactoryMap = new HashMap<>();

        productsFactoryMap.put("electronic", new ElectronicFactory());
        productsFactoryMap.put("furniture", new FurnitureFactory());
        productsFactoryMap.put("general", new FurnitureFactory());

    }

    public AddProductResponse handleCreateProduct(AddProductRequest addProductRequest){

        String productName = addProductRequest.getProductName();

        double unitPrice = addProductRequest.getUnitPrice();

        int currentStockQuantity = addProductRequest.getCurrentStockQuantity();

        int targetMaxStockQuantity = addProductRequest.getTargetMaxStockQuantity();

        int targetMinStockQuantity = addProductRequest.getTargetMinStockQuantity();

        int restockSchedule = addProductRequest.getRestockSchedule();

        int discountStrategyId = addProductRequest.getDiscountStrategyId();

        String productType  = addProductRequest.getProductType();

        ProductFactory productFactory = this.productsFactoryMap.get(productType.toLowerCase());

        Product product = productFactory.createProduct(productName, unitPrice, currentStockQuantity, targetMaxStockQuantity, targetMinStockQuantity, restockSchedule, discountStrategyId, productType);

        Product createdProduct = this.productRepository.addProduct(product);

        return createdProduct == null ? null : new AddProductResponse(createdProduct.getProductId(), createdProduct.getProductName(),
                createdProduct.getUnitPrice(), createdProduct.getCurrentStockQuantity(), createdProduct.getTargetMaxStockQuantity(),
                createdProduct.getTargetMinStockQuantity(), createdProduct.getRestockSchedule(), createdProduct.getDiscountStrategyId(),
                createdProduct.getProductType());
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
}
