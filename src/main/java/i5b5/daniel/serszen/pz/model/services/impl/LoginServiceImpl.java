package i5b5.daniel.serszen.pz.model.services.impl;

import i5b5.daniel.serszen.pz.model.exceptions.UserNotFoundException;
import i5b5.daniel.serszen.pz.model.mybatis.mappers.LoginMapper;
import i5b5.daniel.serszen.pz.model.services.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;
import org.apache.commons.codec.binary.Base64;

@Service
public class LoginServiceImpl implements LoginService {

    private static final int iterations = 20*1000;
    private static final int saltLen = 32;
    private static final int desiredKeyLen = 256;

    @Autowired
    private LoginMapper loginMapper;

    @Override
    public boolean checkLoginData(String login, String password) throws Exception {
        if(login == null || password == null){
            throw new IllegalArgumentException("Login or password is null");
        }
        String storedPass = loginMapper.getEncryptedPassByLogin(login);
        if(storedPass == null){
            throw new UserNotFoundException("User doesn't exists");
        }
        return check(password,storedPass);
    }

    @Override
    public void insertAdmin(String login, String password) throws Exception {
        String encPassword = getSaltedHash(password);
        loginMapper.insertAdminUser(login,encPassword);
    }

    private String getSaltedHash(String password) throws Exception {
        byte[] salt = SecureRandom.getInstance("SHA1PRNG").generateSeed(saltLen);
        return Base64.encodeBase64String(salt) + "$" + hash(password, salt);
    }

    private String hash(String password, byte[] salt) throws Exception {
        if (password == null || password.length() == 0)
            throw new IllegalArgumentException("Empty passwords are not supported.");
        SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        SecretKey key = f.generateSecret(new PBEKeySpec(
                password.toCharArray(), salt, iterations, desiredKeyLen)
        );
        return Base64.encodeBase64String(key.getEncoded());
    }

    private boolean check(String password, String stored) throws Exception{
        String[] saltAndPass = stored.split("\\$");
        if (saltAndPass.length != 2) {
            throw new IllegalStateException(
                    "The stored password have the form 'salt$hash'");
        }
        String hashOfInput = hash(password, Base64.decodeBase64(saltAndPass[0]));
        return hashOfInput.equals(saltAndPass[1]);
    }
}
