package i5b5.daniel.serszen.pz.controller;

import i5b5.daniel.serszen.pz.model.services.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class UtilController {
    @Autowired
    private LoginService loginService;

    public boolean checkLoginData(String login, String password) throws Exception {
        return loginService.checkLoginData(login,password);
    }

    public void insertAdmin(String login, String password) throws Exception {
        loginService.insertAdmin(login,password);
    }
}
