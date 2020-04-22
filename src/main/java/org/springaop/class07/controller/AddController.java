package org.springaop.class07.controller;

import org.springaop.class07.jdbc.StudentHomeworkJdbc;
import org.springaop.class07.model.Student;
import org.springaop.class07.model.TeacherHomework;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


import java.util.Date;

/**
 * 添加学生和作业请求
 * @author asus
 */

@Controller
@RequestMapping(path = "/add")
public class AddController {

    ApplicationContext context = new ClassPathXmlApplicationContext("objetives.xml");
    @RequestMapping(path = "/student")
    public ModelAndView addstudent(String student_id,String student_name){


        ModelAndView modelAndView = new ModelAndView();


        Student s = (Student)context.getBean("objet_student");

        s.setStudentId(Long.parseLong(student_id));
        s.setStudent_name(student_name);
        s.setStudent_password(student_id);
        s.setCreateTime(new Date());

        StudentHomeworkJdbc studentHomeworkJdbc = (StudentHomeworkJdbc)context.getBean("jdbcserve");
        Boolean result = studentHomeworkJdbc.addStudent(s);
        if(result) {
            System.out.println("学生添加成功");
           modelAndView.setViewName("/WEB-INF/pages/successadds");
        }else{
            System.out.println("学生添加失败");
            modelAndView.setViewName("/WEB-INF/pages/failedadds");
        }
        return modelAndView;
    }

    @RequestMapping(path = "/homework")
    public ModelAndView addhomework(String homework_id,String title,String content){
        ModelAndView modelAndView = new ModelAndView();

        //TeacherHomework sh = new TeacherHomework();

        //Spring IoC管理实例
        TeacherHomework th = (TeacherHomework)context.getBean("object_teacherhomework");
        /**
         * 赋值
         */
        th.setHomework_id(Long.parseLong(homework_id));
        th.setHomeworkTitle(title);
        th.setHomeworkContent(content);
        th.setCreateTime(new Date());
        th.setUpdateTime(new Date());
        StudentHomeworkJdbc studentHomeworkJdbc = (StudentHomeworkJdbc)context.getBean("jdbcserve");
        Boolean result = studentHomeworkJdbc.addTeacherHomework(th);
        if(result) {
            System.out.println("作业添加成功");
            modelAndView.setViewName("/WEB-INF/pages/successaddh");
        }else{
            System.out.println("作业添加失败");
            modelAndView.setViewName("/WEB-INF/pages/failedaddh");
        }
        return modelAndView;
    }
}
