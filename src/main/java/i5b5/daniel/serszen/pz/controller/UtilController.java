package i5b5.daniel.serszen.pz.controller;

import i5b5.daniel.serszen.pz.model.exceptions.BlankFieldException;
import i5b5.daniel.serszen.pz.model.exceptions.LoginException;
import i5b5.daniel.serszen.pz.model.exceptions.ResourceException;
import i5b5.daniel.serszen.pz.model.exceptions.codes.LoginExceptionCodes;
import i5b5.daniel.serszen.pz.model.exceptions.codes.ResourceExceptionCodes;
import i5b5.daniel.serszen.pz.model.services.LoginService;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.exceptions.IbatisException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.security.GeneralSecurityException;

@Controller
public class UtilController {
    @Autowired
    private LoginService loginService;
    private final Logger logger = LogManager.getLogger(UtilController.class);


    public UtilController() {
    }

    public boolean checkLoginData(String login, String password) throws LoginException, ResourceException, BlankFieldException {
        try {
            validateInput(login, password);
            return loginService.checkLoginData(login, password);
        } catch (IllegalStateException e) {
            logger.error("Password storing error",e);
            throw new LoginException("Hasło niepoprawnie zapisane, skontaktuj się z administratorem", LoginExceptionCodes.PASSWORD_STORING);
        } catch (GeneralSecurityException e) {
            logger.error("Password verifying error",e);
            throw new LoginException("Błąd podczas sprawdzania hasła", LoginExceptionCodes.CHECKING_PASSWORD);
        } catch (IbatisException e) {
            logger.error("Mapper error", e);
            throw new ResourceException("Błąd podczas kontaktu z bazą danych", ResourceExceptionCodes.MYBATIS, e);
        } catch (LoginException e) {
            logger.error("Login error", e);
            throw e;
        }
    }

    private void validateInput(String login, String password) throws BlankFieldException {
        if(StringUtils.isBlank(login) || StringUtils.isBlank(password)){
            throw new BlankFieldException("Login i hasło nie mogą być puste");
        }
    }
}
