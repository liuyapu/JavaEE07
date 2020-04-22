package org.springaop.class07.controller;

import org.springaop.class07.jdbc.StudentHomeworkJdbc;
import org.springaop.class07.model.StudentHomework;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 学生更新和提交作业请求
 */

@Controller
@RequestMapping(path = "/submit")
public class SubmitHomeworkController {

    @RequestMapping(path = "/homework")
    public ModelAndView submithomework(String studentid,String homeworkid,
                                       String homeworktitle,String homeworkcontent,String homeworktime)throws Exception{
        ModelAndView modelAndView = new ModelAndView();

        //新建一个StudentHomework对象,用于存储待提交的作业
       // StudentHomework sh = new StudentHomework();
        ApplicationContext context = new ClassPathXmlApplicationContext("objetives.xml");
        StudentHomework sh = (StudentHomework)context.getBean("object_studenthomework");

        sh.setStudentId(Long.parseLong(studentid));
        sh.setHomeworkId(Long.parseLong(homeworkid));
        sh.setHomeworkTitle(homeworktitle);
        sh.setHomeworkContent(homeworkcontent);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(homeworktime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        sh.setCreateTime(date);
        sh.setUpdateTime(new Date());

        System.out.println("homeid"+sh.getHomeworkId());
        System.out.printf("studentid"+sh.getStudentId());
        //对作业数据进行更新
        StudentHomeworkJdbc studentHomeworkJdbc = (StudentHomeworkJdbc)context.getBean("jdbcserve");
        Boolean bl = studentHomeworkJdbc.submithomework(sh);
        if(bl){
            System.out.println("数据更新成功");
            modelAndView.setViewName("/WEB-INF/pages/success");
        }
        else{
            //数据更新失败，即之前并未提交过，将数据插入表内
            System.out.println("数据更新失败");
            sh.setCreateTime(new Date());
            sh.setUpdateTime(new Date());
            if(studentHomeworkJdbc.insertstudenthomework(sh)){
                //JOptionPane.showMessageDialog(null, "作业提交成功");
                modelAndView.setViewName("/WEB-INF/pages/success");
                System.out.println("数据插入成功");
            }
            else{
                System.out.println("数据插入失败");
                modelAndView.setViewName("/WEB-INF/pages/failed");
            }

        }
        modelAndView.setViewName("/WEB-INF/pages/success");
        return modelAndView;
    }
}
