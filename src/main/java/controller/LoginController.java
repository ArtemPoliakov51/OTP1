package controller;

import dao.TeacherDao;
import entity.Teacher;
import view.LoginView;

import java.util.Objects;

public class LoginController {

    private static LoginController instance;

    private LoginView view;
    private TeacherDao teacherDao = new TeacherDao();
    private Teacher loggedInTeacher;

    private LoginController() {}

    public static LoginController getInstance() {
        if (instance == null) {
            instance = new LoginController();
        }
        return instance;
    }

    public void tryLogin() throws Exception {
        try {
            System.out.println(view.getLoginEmailValue());
            Teacher foundTeacher = teacherDao.findByEmail(view.getLoginEmailValue());
            System.out.println(foundTeacher);
            if (Objects.equals(foundTeacher.getPassword(), view.getLoginPasswordValue())) {
                loggedInTeacher = foundTeacher;
            } else throw new Exception("Incorrect password");
        } catch (Exception e) {
            throw new Exception("Teacher with given email was not found");
        }
    }

    public void setLoginView(LoginView loginView) {
        this.view = loginView;
    }

    public Teacher getLoggedInTeacher() {
        return loggedInTeacher;
    }

}
