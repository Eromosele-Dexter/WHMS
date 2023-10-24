package apiContracts.Responses;

import utils.JsonUtils;

public class LoginAdminResponse {

    private String username;

    public LoginAdminResponse(String username){
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return JsonUtils.convertObjectToJson(this);
    }
}
