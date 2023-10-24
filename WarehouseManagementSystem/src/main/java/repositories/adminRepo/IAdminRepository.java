package repositories.adminRepo;

import models.Administrator;

public interface IAdminRepository {
    public Administrator createAdministrator(Administrator administrator);
    public Administrator getAdministrator();

    public void deleteCurrentAdministrator();
}
