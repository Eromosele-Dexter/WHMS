package repositories.adminRepo;

import databaseConnectors.IDatabaseConnector;
import models.admin.Administrator;
import statics.DbConfig;

import java.sql.*;

import static statics.DbQueries.*;

public class AdminRepository implements IAdminRepository{


    private IDatabaseConnector adminDbContext;
    private static Connection adminDb;


    public AdminRepository(IDatabaseConnector adminDbContext){
        this.adminDbContext = adminDbContext;
        this.adminDb = adminDbContext.connect(DbConfig.ADMIN_DB_CONNECTION_STRING);

    }



    /**
     * @param administrator
     * @return administrator if successfully created else returns null
     */

    public Administrator createAdministrator(Administrator administrator) {
        int adminCount = getAdminCount();


        if(adminCount  >= 1)
            return null;


        int rowsInserted = addAdmin(administrator);

        return rowsInserted > 0 ? administrator:null;
    }


    /**
     * @return current administrator else returns null if admin does not exist.
     */

    public Administrator getAdministrator() {
        return getAdmin();
    }

    /**
     * Deletes current administrator
     */
    @Override
    public void deleteCurrentAdministrator() {
        this.deleteCurrentAdmin();
    }

    private int getAdminCount() {
        Statement statement;
        int adminCount = 0;

        try {

            statement = adminDb.createStatement();

            ResultSet resultSet = statement.executeQuery(COUNT_ADMIN_QUERY);

            if (resultSet.next()) {
                adminCount = resultSet.getInt(1); // Get the count from the first column
            }

            adminDbContext.disconnect();

        } catch (SQLException e) {
            adminDbContext.disconnect();
            System.out.println("Error Counting Admin.");
            throw new RuntimeException(e);
        }
        return adminCount;
    }

    private Administrator getAdmin(){
        Administrator administrator = null;
        try {

            String selectSQL = "SELECT * FROM " + ADMIN_TABLE + " ORDER BY id LIMIT 1";

            PreparedStatement preparedStatement = adminDb.prepareStatement(selectSQL);

            ResultSet resultSet = preparedStatement.executeQuery();


            if (resultSet.next()) {
                // Retrieve data from the first entry

                int adminId = resultSet.getInt("id");

                String username = resultSet.getString("username");

                String password = resultSet.getString("password");

                administrator = new Administrator(username,password);

                administrator.setId(adminId);
            }

            preparedStatement.close();

            adminDbContext.disconnect();
        } catch (SQLException e) {
            adminDbContext.disconnect();
            e.printStackTrace();
        }
        return administrator;
    }

    private int addAdmin(Administrator administrator) {
        int rowsInserted = 0;
        try {

            PreparedStatement preparedStatement = adminDb.prepareStatement(ADD_ADMIN_QUERY);

            preparedStatement.setString(1, administrator.getUsername());

            preparedStatement.setString(2, administrator.getPassword());

            rowsInserted  = preparedStatement.executeUpdate();

            if (rowsInserted <= 0) {
                System.out.println("Failed to insert an admin.");
            }

            preparedStatement.close();

            adminDbContext.disconnect();

        } catch (SQLException e) {
            adminDbContext.disconnect();
            throw new RuntimeException(e);
        }
        return rowsInserted;
    }

    private void deleteCurrentAdmin(){
        try {

            String deleteSQL = "DELETE FROM " + ADMIN_TABLE+ " WHERE id = (SELECT MIN(id) FROM " + ADMIN_TABLE + ")";

            PreparedStatement preparedStatement = adminDb.prepareStatement(deleteSQL);

            int rowsDeleted = preparedStatement.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("The first entry has been deleted.");
            } else {
                System.out.println("No entries were deleted (the table may be empty).");
            }

            preparedStatement.close();

            adminDbContext.disconnect();
        } catch (SQLException e) {
            adminDbContext.disconnect();
            e.printStackTrace();
        }
    }
}
