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
import server.SessionFactorySingleton;

/**
 * Hello world!
 *
 */
public class App {

    public static void main(String[] args) {

        SessionFactory sessionFactory = SessionFactorySingleton.getInstance().getSessionFactory();

        Session session = null;
        Transaction tx=null;

        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();

     /*       PriceEntity priceEntity = new PriceEntity();
            priceEntity.setItemId(1);
            priceEntity.setPrice(50.0);

            session.save(priceEntity);

            session.getTransaction().commit();*/

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