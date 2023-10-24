package apiContracts.Responses;

import utils.JsonUtils;

public class RegisterAdminResponse {
    private String username;

    public RegisterAdminResponse(String username){
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
