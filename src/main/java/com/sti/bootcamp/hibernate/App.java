package com.sti.bootcamp.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import com.sti.bootcamp.hibernate.model.AccountEntity;
import com.sti.bootcamp.hibernate.model.CustomerEntity;

/**
 * Hibernate
 *
 */
public class App 
{
    private static SessionFactory factory;
    private static ServiceRegistry serviceRegistry;
    
    public static void main( String[] args )
    {
    	Configuration config = new Configuration();
        config.configure();
        config.addAnnotatedClass(CustomerEntity.class);
        config.addAnnotatedClass(AccountEntity.class);
        config.addResource("query.hbm.xml");
        //config.addResource("Customer.hbm.xml");
        serviceRegistry = new StandardServiceRegistryBuilder().applySettings(config.getProperties()).build();
        factory = config.buildSessionFactory(serviceRegistry);
        
        App app = new App();
        System.out.println("===================================");
        List<CustomerEntity> customers = app.listCustomer();
        for(CustomerEntity u : customers){
            System.out.println(String.format("%s \t%s \t%s \t%s", u.getCustomerNumber(), u.getFirstName(), u.getLastName(), u.getUsername()));

        }
        System.out.println("===================================");

        List<AccountEntity> accounts = app.listAccount();
        for(AccountEntity u : accounts){
            System.out.println(String.format("%s \t%s \t%s \t%s", u.getAccountNumber(), u.getOpenDate(), u.getBalance(), u.getCustomer()));

        }
        System.out.println("===================================");

        List<AccountEntity> accountsByCustomer = app.listAccount(customers.get(0));
        for(AccountEntity u : accountsByCustomer){
            System.out.println(String.format("%s \t%s \t%s \t%s", u.getAccountNumber(), u.getOpenDate(), u.getBalance(), u.getCustomer()));

        }
        System.out.println("===================================");
    }
    
    private List<CustomerEntity> listCustomer()
    {
        Session sesn = factory.openSession();
        List<CustomerEntity> users = new ArrayList<>();
        try{
            Query query = (Query) sesn.createQuery("From Customer");
            query.setMaxResults(10);
            users = query.list();
        } finally {
            sesn.close();
        }
         
        return users;
    }
    
    private List<AccountEntity> listAccount()
    {
        Session sesn = factory.openSession();
        List<AccountEntity> accounts = new ArrayList<>();
        try{
            Query query = (Query) sesn.createQuery("From Account");
            query.setMaxResults(10);
            accounts = query.list();
        } finally {
            sesn.close();
        }
         
        return accounts;
    }

    private List<AccountEntity> listAccount(CustomerEntity customer)
    {
        Session sesn = factory.openSession();
        List<AccountEntity> accounts = new ArrayList<>();
        try{
            Query query = (Query) sesn.getNamedQuery("QUERY_ACCOUNT_BY_CUSTOMER");
            query.setInteger("cif", customer.getCustomerNumber());
            accounts = query.list();
        } finally {
            sesn.close();
        }
         
        return accounts;
    }
}
