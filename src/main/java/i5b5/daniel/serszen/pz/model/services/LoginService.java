package i5b5.daniel.serszen.pz.model.services;

import i5b5.daniel.serszen.pz.model.exceptions.LoginException;

import java.security.GeneralSecurityException;

public interface LoginService {
    boolean checkLoginData(String login, String password) throws GeneralSecurityException, LoginException;
}
