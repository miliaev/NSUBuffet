package database;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

import java.util.Properties;

public class SessionFactorySingleton
{
    private static volatile SessionFactorySingleton s;
    private SessionFactory sessionFactory;

    private SessionFactorySingleton()
    {
        Configuration configuration = new Configuration();
        configuration.configure();

        Properties properties = configuration.getProperties();

        ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().applySettings(properties).buildServiceRegistry();
        sessionFactory = configuration.buildSessionFactory(serviceRegistry);
    }

    public org.hibernate.SessionFactory getSessionFactory()
    {
        return sessionFactory;
    }

    public static SessionFactorySingleton getInstance()
    {
        if (s != null ) return s;
        synchronized(SessionFactorySingleton.class)
        {
            if (s == null )
            {
                s = new SessionFactorySingleton();
            }
        }

        return s;
    }
} 
