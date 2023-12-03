package repositories.adminRepo;

import models.admin.Administrator;

public interface IAdminRepository {
    public Administrator createAdministrator(Administrator administrator);
    public Administrator getAdministrator();

    public void deleteCurrentAdministrator();
}
