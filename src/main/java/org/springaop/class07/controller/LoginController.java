package org.springaop.class07.controller;

import org.springaop.class07.jdbc.StudentHomeworkJdbc;
import org.springaop.class07.model.Student;
import org.springaop.class07.model.StudentHomework;
import org.springaop.class07.model.Teacher;
import org.springaop.class07.model.TeacherHomework;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


import javax.servlet.http.HttpServletResponse;
import javax.swing.*;
import java.util.List;

/**
 * 用户登录请求
 */

@EnableAspectJAutoProxy
@Controller
@RequestMapping(path = "/users")
public class LoginController {

    //获取参数，分别为用户名，用户密码，和用户类型
    @RequestMapping(path = "/login")
    public ModelAndView userlogin(String id, String user_password, String c3, HttpServletResponse response)throws Exception{

        //方法的参数需要和提交表单里面的参数一致
        ModelAndView mv = new ModelAndView();

        System.out.printf("id"+id);
        System.out.printf("密码"+user_password);


        Long user_id;
        //用户名和密码均不能为空
        if(user_password==null || user_password == "" || id == null || id == ""){
            System.out.println("用户id或密码不能为空！");

            JOptionPane.showMessageDialog(null, "账号密码不能为空");

            mv.setViewName("../login");
        }
        else{
            user_id = Long.parseLong(id);

            //用户为学生，进入学生界面
            if(c3.equals("student")){
                System.out.println("是学生");
                //Student s = new Student();

                //Spring Ioc代替静态类
                ApplicationContext context = new ClassPathXmlApplicationContext("objetives.xml");
                Student s = (Student)context.getBean("object_student");

                s.setStudentId(user_id);
                s.setStudent_password(user_password);

                //判断学生id，密码是否正确
                StudentHomeworkJdbc studentHomeworkJdbc = (StudentHomeworkJdbc)context.getBean("jdbcserve");
                Boolean result = studentHomeworkJdbc.loginS(s);
                if(result){
                    System.out.println("该学生登录成功");
                    //进入学生界面
                    List<TeacherHomework> homework_list = studentHomeworkJdbc.selectAll2();

                    mv.addObject("student_id",user_id);
                    mv.addObject("homework_list",homework_list);
                    //传递参数到学生界面
                    mv.setViewName("/WEB-INF/pages/submith");
//                    req.setAttribute("student_id",user_id);
//                    req.setAttribute("homework_list",homework_list);
//                    req.getRequestDispatcher("submith.jsp").forward(req, resp);
                }
                else{
                    //密码错误，重新回到登陆界面
                    System.out.println("密码错误");
                    JOptionPane.showMessageDialog(null, "密码错误");
                    mv.setViewName("../login");

                }
            }
            //用户为老师
            else{
                System.out.println("是老师");
               // Teacher t = new Teacher();

                ApplicationContext context = new ClassPathXmlApplicationContext("objetives.xml");
                Teacher t = (Teacher) context.getBean("object_teacher");
                t.setTeacher_id(user_id);
                t.setTeacher_password(user_password);

                //判断老师id，密码是否正确
                StudentHomeworkJdbc studentHomeworkJdbc = (StudentHomeworkJdbc)context.getBean("jdbcserve");
                Boolean result = studentHomeworkJdbc.loginT(t);
                if(result){
                    System.out.println("该老师登录成功");  //进入学生界面
                    //获取作业列表,并将list传递到就是jsp界面
                    List<StudentHomework> list = StudentHomeworkJdbc.selectAll();
                    mv.addObject("list",list);
                    mv.setViewName("/WEB-INF/pages/choice");

                }
                else{
                    System.out.println("密码错误");
                    JOptionPane.showMessageDialog(null, "密码错误");
                    mv.setViewName("../login");
                }
            }
        }

        return mv;
    }
}
