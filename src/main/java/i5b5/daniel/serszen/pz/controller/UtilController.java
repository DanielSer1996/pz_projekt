package i5b5.daniel.serszen.pz.controller;

import i5b5.daniel.serszen.pz.model.exceptions.LoginException;
import i5b5.daniel.serszen.pz.model.exceptions.ResourceException;
import i5b5.daniel.serszen.pz.model.exceptions.codes.LoginExceptionCodes;
import i5b5.daniel.serszen.pz.model.exceptions.codes.ResourceExceptionCodes;
import i5b5.daniel.serszen.pz.model.services.LoginService;
import org.apache.ibatis.exceptions.IbatisException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

import java.security.GeneralSecurityException;

@Controller
public class UtilController {
    @Autowired
    private LoginService loginService;

    public boolean checkLoginData(String login, String password) throws LoginException, ResourceException {
        try {
            return loginService.checkLoginData(login, password);
        }catch (IllegalStateException e){
            throw new LoginException("Hasło niepoprawnie zapisane, skontaktuj się z administratorem", LoginExceptionCodes.PASSWORD_STORING);
        } catch (IllegalArgumentException e) {
            throw new LoginException("Hasło nie może być puste", LoginExceptionCodes.BLANK_PASSWORD);
        } catch (GeneralSecurityException e) {
            throw new LoginException("Błąd podczas sprawdzania hasła", LoginExceptionCodes.CHECKING_PASSWORD);
        } catch (IbatisException e) {
            throw new ResourceException("Błąd podczas kontaktu z bazą danych", ResourceExceptionCodes.MYBATIS, e);
        } catch (LoginException e){
           throw e;
        } catch (Throwable e){
            throw new ResourceException("Błąd bazy danych",ResourceExceptionCodes.ORACLE,e);
        }
    }

    public void insertAdmin(String login, String password) throws Exception {
        loginService.insertAdmin(login, password);
    }
}
