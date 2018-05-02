package server.database;

import java.util.List;
import java.util.Properties;

import entities.BuffetEntity;
import entities.OrdersEntity;
import entities.PriceEntity;
import org.hibernate.*;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

/**
 * Hello world!
 *
 */
public class App {
    private static SessionFactory sessionFactory = null;
    private static ServiceRegistry serviceRegistry = null;

    private static SessionFactory configureSessionFactory() throws HibernateException
    {
        Configuration configuration = new Configuration();
        configuration.configure();

        Properties properties = configuration.getProperties();

        serviceRegistry = new ServiceRegistryBuilder().applySettings(properties).buildServiceRegistry();
        sessionFactory = configuration.buildSessionFactory(serviceRegistry);

        return sessionFactory;
    }

    public static void main(String[] args) {
        // Configure the session factory
        configureSessionFactory();

        Session session = null;
        Transaction tx=null;

        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();

            OrdersEntity ordersEntity = new OrdersEntity();
            ordersEntity.setOrderId(1);
            ordersEntity.setItemId(2);

            session.save(ordersEntity);

            session.getTransaction().commit();

            // Fetching saved data
            System.out.println("Get data from table...");
            Query query = session.createQuery("from BuffetEntity");
            List list = query.list();
            for(Object buffetEntity: list)
            {
                System.out.println(((BuffetEntity) buffetEntity).getLocation());
            }


        }
        catch (Exception ex)
        {
            ex.printStackTrace();

            // Rolling back the changes to make the data consistent in case of any failure 
            // in between multiple server.database write operations.
            tx.rollback();
        } finally
        {
            if(session != null)
            {
                session.close();
            }
        }
    }
}