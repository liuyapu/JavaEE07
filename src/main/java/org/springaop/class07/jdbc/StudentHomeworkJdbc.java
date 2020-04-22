package org.springaop.class07.jdbc;



import org.springaop.class07.model.Student;
import org.springaop.class07.model.StudentHomework;
import org.springaop.class07.model.Teacher;
import org.springaop.class07.model.TeacherHomework;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@EnableAspectJAutoProxy
@Service
public class StudentHomeworkJdbc {
    public static void main(String[] args) {
        List<StudentHomework> list = selectAll();

        for (StudentHomework sh : list){
            System.out.println("作业是："+sh.getHomeworkContent());
        }
    }



    public static List<StudentHomework> selectAll(){


        String sqlString = "SELECT * FROM s_student_homework";


        Connection connection = null;

        List<StudentHomework> list = new ArrayList<>();
        try {

            connection =  DatabasePool.getHikariDataSource().getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sqlString);
            connection.setAutoCommit(false);
            // 获取执行结果
            while (resultSet.next()){
                StudentHomework sh = new StudentHomework();

                sh.setStudentId(resultSet.getLong("student_id"));
                sh.setHomeworkId(resultSet.getLong("homework_id"));
                sh.setHomeworkTitle(resultSet.getString("homework_title"));
                sh.setHomeworkContent(resultSet.getString("homework_content"));
                sh.setCreateTime(resultSet.getTimestamp("create_time"));
                list.add(sh);
            }
            connection.commit();

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if(connection!=null){
                    connection.rollback();
                }
            } catch (SQLException e1) {
                System.out.println(e1);
            }
        }finally{
            try {
                if(connection!=null){
                    connection.rollback();
                }
            } catch (SQLException e2) {
                System.out.println(e2);
            }
        }

        return list;
    }
    public static List<TeacherHomework> selectAll2(){

        String sqlString = "SELECT * FROM s_homework";

        Connection connection = null;

        List<TeacherHomework> list = new ArrayList<>();
        try{

            connection =  DatabasePool.getHikariDataSource().getConnection();
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(sqlString);
            connection.setAutoCommit(false);
            // 获取执行结果
            while (resultSet.next()){
                TeacherHomework sh1 = new TeacherHomework();

                sh1.setHomework_id(resultSet.getLong("homework_id"));
                sh1.setHomeworkTitle(resultSet.getString("title"));
                sh1.setHomeworkContent(resultSet.getString("content"));
                sh1.setCreateTime(resultSet.getTimestamp("create_time"));
                list.add(sh1);
            }
            connection.commit();

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if(connection!=null){
                    connection.rollback();
                }
            } catch (SQLException e1) {
                System.out.println(e1);
            }
        }finally{
            try {
                if(connection!=null){
                    connection.rollback();
                }
            } catch (SQLException e2) {
                System.out.println(e2);
            }
        }

        return list;
    }
    public static Boolean addStudent(Student s){

        Boolean a=false;

        Connection connection = null;

        String sqlString="insert into s_student(student_id,student_name,student_password) values(?,?,?)";
        try {


            connection =  DatabasePool.getHikariDataSource().getConnection();
            PreparedStatement ps = connection.prepareStatement(sqlString);
            connection.setAutoCommit(false);
            ps.setLong(1,s.getStudentId());
            ps.setString(2,s.getStudent_name());
            ps.setString(3,s.getStudent_password());
            int row = ps.executeUpdate();
            if(row > 0){
                System.out.println("成功添加了 " + row + "个学生！ ");
                a=true;
            }
            connection.commit();


        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if(connection!=null){
                    connection.rollback();
                }
            } catch (SQLException e1) {
                System.out.println(e1);
            }
        }finally{
            try {
                if(connection!=null){
                    connection.rollback();
                }
            } catch (SQLException e2) {
                System.out.println(e2);
            }
        }

        return a;
    }
    public static Boolean addTeacherHomework(TeacherHomework s){

        Boolean a=false;
        String sqlString="insert into s_homework(homework_id,title,content) values(?,?,?)";

//        List<StudentHomework> list = new ArrayList<>();

        Connection connection = null;
        try {

            connection =  DatabasePool.getHikariDataSource().getConnection();
            PreparedStatement ps = connection.prepareStatement(sqlString);
            connection.setAutoCommit(false);
            ps.setLong(1,s.getHomework_id());
            ps.setString(2,s.getHomeworkTitle());
            ps.setString(3,s.getHomeworkContent());

            int row = ps.executeUpdate();
            if(row > 0){
                System.out.println("成功添加了 " + row + "条数据！ ");
                a=true;
            }
            connection.commit();

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if(connection!=null){
                    connection.rollback();
                }
            } catch (SQLException e1) {
                System.out.println(e1);
            }
        }finally{
            try {
                if(connection!=null){
                    connection.rollback();
                }
            } catch (SQLException e2) {
                System.out.println(e2);
            }
        }

        return a;
    }
    public static Boolean loginS(Student s){
        Boolean a=false;                //判断账号密码是否正确
        Boolean x = false;              //控制循环
        String sqlString="select *from s_student";

        Connection connection = null;
        try {

            connection =  DatabasePool.getHikariDataSource().getConnection();
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(sqlString);
            connection.setAutoCommit(false);
            while(resultSet.next() && !x){

                if(s.getStudentId() == resultSet.getLong("student_id")){
                    if(s.getStudent_password().equals(resultSet.getString("student_password"))){
                        x = true;
                        a = true;
                        break;
                    }
                }
            }
            connection.commit();

        }catch (SQLException e) {
            e.printStackTrace();
            try {
                if(connection!=null){
                    connection.rollback();
                }
            } catch (SQLException e1) {
                System.out.println(e1);
            }
        }finally{
            try {
                if(connection!=null){
                    connection.rollback();
                }
            } catch (SQLException e2) {
                System.out.println(e2);
            }
        }
        return a;
    }
    public static Boolean loginT(Teacher t){

        Boolean a=false;                //判断账号密码是否正确
        Boolean x = false;              //控制循环
        String sqlString="select *from s_teacher";

        Connection connection = null;
        try{

            connection =  DatabasePool.getHikariDataSource().getConnection();
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(sqlString);
            connection.setAutoCommit(false);
            while(resultSet.next() && !x){

                if(t.getTeacher_id() == resultSet.getLong("teacher_id")){
                    if(t.getTeacher_password().equals(resultSet.getString("teacher_password"))){
                        x = true;
                        a = true;
                        break;
                    }
                }
            }
            connection.commit();

        }catch (SQLException e) {
            e.printStackTrace();
            try {
                if(connection!=null){
                    connection.rollback();
                }
            } catch (SQLException e1) {
                System.out.println(e1);
            }
        }finally{
            try {
                if(connection!=null){
                    connection.rollback();
                }
            } catch (SQLException e2) {
                System.out.println(e2);
            }
        }
        return a;
    }
    public static Boolean submithomework(StudentHomework sh){

        Boolean a=false;                //判断作业是否提交过,提交过则更更新

        String sqlString="UPDATE s_student_homework SET homework_content=?,update_time=? WHERE homework_id=? and student_id=?";

        Connection connection = null;
        try {

            connection =  DatabasePool.getHikariDataSource().getConnection();

            PreparedStatement ps = connection.prepareStatement(sqlString);
            connection.setAutoCommit(false);
            ps.setString(1,sh.getHomeworkContent());
            ps.setDate(2,new Date(sh.getUpdateTime().getTime()));
            ps.setLong(3,sh.getHomeworkId());
            ps.setLong(4,sh.getStudentId());

            //执行sql语句，返回影响行数
            int res=ps.executeUpdate();
            if(res>0){
                System.out.println("更新数据成功");
                a = true;
            }
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if(connection!=null){
                    connection.rollback();
                }
            } catch (SQLException e1) {
                System.out.println(e1);
            }
        }finally{
            try {
                if(connection!=null){
                    connection.rollback();
                }
            } catch (SQLException e2) {
                System.out.println(e2);
            }
        }
        return a;
    }
    public static Boolean insertstudenthomework(StudentHomework sh){

        Boolean a=false;
        String sqlString="insert into s_student_homework(student_id,homework_id,homework_title,homework_content,update_time,create_time) values(?,?,?,?,?,?)";

        Connection connection = null;
        try {

            connection =  DatabasePool.getHikariDataSource().getConnection();

            PreparedStatement ps = connection.prepareStatement(sqlString);
            connection.setAutoCommit(false);
                ps.setLong(2,sh.getHomeworkId());
                ps.setString(3,sh.getHomeworkTitle());
                ps.setLong(1,sh.getStudentId());
                ps.setString(4,sh.getHomeworkContent());
                ps.setDate(5,new Date(sh.getUpdateTime().getTime()));
                ps.setDate(6,new Date(sh.getCreateTime().getTime()));

                int row = ps.executeUpdate();
                if(row > 0){
                    System.out.println("成功添加了 " + row + "条数据！ ");
                    a=true;
                }
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if(connection!=null){
                    connection.rollback();
                }
            } catch (SQLException e1) {
                System.out.println(e1);
            }
        }finally{
            try {
                if(connection!=null){
                    connection.rollback();
                }
            } catch (SQLException e2) {
                System.out.println(e2);
            }
        }

        return a;
    }
}
