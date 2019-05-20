package service;

import dao.DAOFactory;
import dao.SequenceDao;
import dao.UserDAO;
import model.entity.User;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class LoginServiceTest {

    private static final String LOGIN = "login";
    private static final String PASSWORD = "password";

    private SequenceDao sequenceDao;
    private DAOFactory daoFactory;
    private UserDAO userDAO;

    private LoginService loginService;

    @Before
    public void setUp() {
        daoFactory = mock(DAOFactory.class);
        userDAO = mock(UserDAO.class);
        sequenceDao = mock(SequenceDao.class);
        loginService = LoginService.getInstance();
        loginService.setFactory(daoFactory);

        when(daoFactory.createUserDAO()).thenReturn(userDAO);
        when(daoFactory.createSequenceDao(any())).thenReturn(sequenceDao);
    }

    @Test
    public void shouldReturnWhenLoginContains() {
        User expectedUser = new User();
        when(userDAO.findByEmail(LOGIN)).thenReturn(expectedUser);

        User actualUser = loginService.isPresentLogin(LOGIN);

        assertEquals(expectedUser, actualUser);
    }

    @Test
    public void shouldReturnNullWhenLoginNotContains() {
        User actualUser = loginService.isPresentLogin(LOGIN);

        assertNull(actualUser);
    }

    @Test
    public void shouldCreateUser() {
        User userWithoutId = new User();
        User expectedUser = new User();

        userWithoutId.setEmail("test.test@gmail.com");
        userWithoutId.setPassword(PASSWORD);

        expectedUser.setPassword(PASSWORD);

        when(userDAO.create(userWithoutId)).thenReturn(expectedUser);
        when(sequenceDao.getNextSequenceId()).thenReturn(null);

        User actualUser = loginService.addUser(userWithoutId);

        assertEquals(expectedUser, actualUser);
    }

    @Test
    public void testSecurePassword() {
        String password = "root";
        User user = new User();
        user.setPassword(password);

        LoginService.getInstance().securePassword(user);

        assertNotEquals(user.getPassword(), password);
    }

}
