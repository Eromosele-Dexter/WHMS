package apiContracts.Responses;

import utils.JsonUtils;

public class LoginAdminResponse {

    private int id;
    private String username;

    public LoginAdminResponse(String username, int id){
        this.username = username;
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getId(){
        return id;
    }

    @Override
    public String toString() {
        return JsonUtils.convertObjectToJson(this);
    }
}
