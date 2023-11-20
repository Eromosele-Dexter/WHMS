package repositories.productRepo;

import databaseConnectors.IDatabaseConnector;
import models.Product;
import statics.DbConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static statics.DbQueries.*;

public  class ProductRepository implements IProductRepository{
    private IDatabaseConnector productDbContext;
    private static Connection productDb;

    public ProductRepository(IDatabaseConnector productDbContext){
        this.productDbContext = productDbContext;
        this.productDb = productDbContext.connect(DbConfig.PRODUCTS_DB_CONNECTION_STRING);

    }

    /**
     * @param product
     * @returns product
     */
    @Override
    public Product addProduct(Product product) {

        // product with name already exists in db
        if(getProductByName(product.getProductName()) != null) {
            productDbContext.disconnect();
            return null;
        }

        int rowsInserted = 0;
        try {

            PreparedStatement preparedStatement = productDb.prepareStatement(ADD_PRODUCT_QUERY);

            preparedStatement.setString(1, product.getProductName());

            preparedStatement.setInt(2, product.getCurrentStockQuantity());

            preparedStatement.setDouble(3, product.getUnitPrice());

            preparedStatement.setInt(4, product.getTargetMaxStockQuantity());

            preparedStatement.setInt(5, product.getTargetMinStockQuantity());

            preparedStatement.setInt(6, product.getRestockSchedule());

            preparedStatement.setInt(7, product.getDiscountStrategyId());

            rowsInserted  = preparedStatement.executeUpdate();

            if (rowsInserted <= 0) {
                System.out.println("Failed to insert product.");
            }

            preparedStatement.close();

            productDbContext.disconnect();

        } catch (SQLException e) {
            productDbContext.disconnect();
            throw new RuntimeException(e);
        }
        return product;
    }

    /**
     * @param id
     * @returns product
     */
    @Override
    public Product getProduct(int id) {
        Product product = null;
        try {

            String selectSQL = "SELECT * FROM " + PRODUCTS_TABLE + " WHERE id = " + id;

            PreparedStatement preparedStatement = productDb.prepareStatement(selectSQL);

            ResultSet resultSet = preparedStatement.executeQuery();


            if (resultSet.next()) {
                // Retrieve data from the first entry

                int productId = resultSet.getInt("id");

                String productName = resultSet.getString("name");

                int currentStockQuantity = resultSet.getInt("current_stock_quantity");

                double unitPrice = resultSet.getDouble("unit_price");

                int targetMaxQuantity = resultSet.getInt("target_max_quantity");

                int targetMinQuantity = resultSet.getInt("target_min_quantity");

                int restockSchedule = resultSet.getInt("restock_schedule");

                int discountStrategyId = resultSet.getInt("discount_strategy_id")  ;

                String productType = resultSet.getString("product_type");

                product = new Product(productName, unitPrice, currentStockQuantity, targetMaxQuantity, targetMinQuantity, restockSchedule, discountStrategyId, productType) {

                };

                product.setProductId(productId);

            }

            preparedStatement.close();

            productDbContext.disconnect();
        } catch (SQLException e) {
            productDbContext.disconnect();
            e.printStackTrace();
        }
        return product;
    }


    /**
     * @param name
     * @returns product
     */
    public Product getProductByName(String name) {
        Product product = null;
        try {

            String selectSQL = "SELECT * FROM " + PRODUCTS_TABLE + " WHERE name = " + "'" + name + "'";

            PreparedStatement preparedStatement = productDb.prepareStatement(selectSQL);

            ResultSet resultSet = preparedStatement.executeQuery();


            if (resultSet.next()) {
                // Retrieve data from the first entry

                int productId = resultSet.getInt("id");

                String productName = resultSet.getString("name");

                int currentStockQuantity = resultSet.getInt("current_stock_quantity");

                double unitPrice = resultSet.getDouble("unit_price");

                int targetMaxQuantity = resultSet.getInt("target_max_quantity");

                int targetMinQuantity = resultSet.getInt("target_min_quantity");

                int restockSchedule = resultSet.getInt("restock_schedule");

                int discountStrategyId = resultSet.getInt("discount_strategy_id");

                String productType = resultSet.getString("product_type");

                product = new Product(productName, unitPrice, currentStockQuantity, targetMaxQuantity, targetMinQuantity, restockSchedule, discountStrategyId, productType) {

                };

                product.setProductId(productId);

            }

            preparedStatement.close();

            productDbContext.disconnect();
        } catch (SQLException e) {
            productDbContext.disconnect();
            e.printStackTrace();
        }
        return product;
    }

    /**
     * @param product
     * @returns product
     */
    @Override
    public Product updateProduct(Product product) {

        try {

            PreparedStatement preparedStatement = productDb.prepareStatement(ADD_PRODUCT_QUERY);

            preparedStatement.setString(1, product.getProductName());

            preparedStatement.setInt(2, product.getCurrentStockQuantity());

            preparedStatement.setDouble(3, product.getUnitPrice());

            preparedStatement.setInt(4, product.getTargetMaxStockQuantity());

            preparedStatement.setInt(5, product.getTargetMinStockQuantity());

            preparedStatement.setInt(6, product.getRestockSchedule());

            preparedStatement.setInt(7, product.getDiscountStrategyId());

            preparedStatement.setInt(8, getProductByName(product.getProductName()).getProductId());

            preparedStatement.executeUpdate();


            preparedStatement.close();

            productDbContext.disconnect();

        } catch (SQLException e) {
            productDbContext.disconnect();
            throw new RuntimeException(e);
        }
        return product;
    }

    /**
     * @param name
     */
    @Override
    public void deleteProduct(String name) {

    }
}
