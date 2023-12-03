package services;

import apiContracts.Requests.LoginAdminRequest;
import apiContracts.Requests.RegisterAdminRequest;
import apiContracts.Responses.LoginAdminResponse;
import apiContracts.Responses.RegisterAdminResponse;
import models.admin.Administrator;
import repositories.adminRepo.IAdminRepository;

public class AdminService {

    private static IAdminRepository adminRepository;


    public AdminService(IAdminRepository adminRepository){
        this.adminRepository = adminRepository;
    }

    public RegisterAdminResponse handleAdminSignUp(RegisterAdminRequest registerAdminRequest){

        // TODO: Password and User validation

        String username = registerAdminRequest.getUsername();

        String password = registerAdminRequest.getPassword();

        Administrator newAdministrator = new Administrator(username,password);

        Administrator createdAdmin = this.adminRepository.createAdministrator(newAdministrator);

        return createdAdmin == null ? null: new RegisterAdminResponse(createdAdmin.getUsername());
    }

    public LoginAdminResponse handleAdminLogin(LoginAdminRequest loginAdminRequest){
        String username = loginAdminRequest.getUsername();

        String password = loginAdminRequest.getPassword();

        Administrator currentAdmin = this.adminRepository.getAdministrator();

        boolean isValidUsername = currentAdmin!= null && currentAdmin.getUsername().equals(username);

        boolean isValidPassword = currentAdmin!= null && currentAdmin.getPassword().equals(password);


        if(isValidUsername && isValidPassword ) {

            return new LoginAdminResponse(username, currentAdmin.getId());
        }

        return null;
    }
}
