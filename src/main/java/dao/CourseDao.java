package dao;

import entity.Teacher;
import jakarta.persistence.EntityManager;

import java.util.List;

import entity.Course;

import java.util.*;

    public class CourseDao {
        public void persist(Course course) {
            EntityManager em = datasource.MariaDBJpaConnection.getInstance();
            em.getTransaction().begin();
            em.persist(course);
            em.getTransaction().commit();
        }

        public Course find(int id) {
            try {
                EntityManager em = datasource.MariaDBJpaConnection.getInstance();
                return em.find(Course.class, id);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Data not found.");
                return null;
            }
        }


        public List<Course> findByTeacher(Teacher teacher){
            try {
                EntityManager em = datasource.MariaDBJpaConnection.getInstance();
                List<Course> courses = em.createQuery("select c from Course c WHERE c.teacher = :cTeach",
                                Course.class)
                        .setParameter("cTeach", teacher)
                        .getResultList();
                return courses;
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Data not found.");
                return null;
            }
        }

        public void update(Course course) {
            EntityManager em = datasource.MariaDBJpaConnection.getInstance();
            em.getTransaction().begin();
            em.merge(course);
            em.getTransaction().commit();
        }

        public void delete(Course course) {
            EntityManager em = datasource.MariaDBJpaConnection.getInstance();
            em.getTransaction().begin();
            em.remove(course);
            em.getTransaction().commit();
        }
    }
