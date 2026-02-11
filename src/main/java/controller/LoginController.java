package controller;

import dao.TeacherDao;
import entity.Teacher;
import view.CheckerGUI;

import java.util.Objects;

public class LoginController {
    private CheckerGUI view;
    private TeacherDao teacherDao = new TeacherDao();
    private Teacher loggedInTeacher;

    public LoginController(CheckerGUI view) {
        this.view = view;
    }

    public void tryLogin() throws Exception {
        try {
            System.out.println(view.getLoginEmailValue());
            Teacher foundTeacher = teacherDao.findByEmail(view.getLoginEmailValue());
            System.out.println(foundTeacher);
            if (Objects.equals(foundTeacher.getPassword(), view.getLoginPasswordValue())) {
                loggedInTeacher = foundTeacher;
                view.updateTeacherLabel(loggedInTeacher.getFirstname() + " " + loggedInTeacher.getLastname());
                view.updateTeacherEmailLabel(loggedInTeacher.getEmail());
            } else throw new Exception("Incorrect password");
        } catch (Exception e) {
            throw new Exception("Teacher with given email was not found");
        }
    }

    protected Teacher getLoggedInTeacher() {
        return loggedInTeacher;
    }


}
