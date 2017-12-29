package i5b5.daniel.serszen.pz.model.services;

public interface LoginService {
    boolean checkLoginData(String login, String password) throws Exception;
    void insertAdmin(String login, String password) throws Exception;
}
