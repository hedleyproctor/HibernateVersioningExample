package com.hedleyproctor;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.sql.SQLException;

public class VersionTests {
    private SessionFactory sessionFactory;

    @BeforeMethod
    public void setUp() throws SQLException {
        sessionFactory = new Configuration().configure().buildSessionFactory();
    }

    @AfterMethod
    public void afterMethod() {
        sessionFactory.close();
    }

    @Test
    public void optimisticLockFailure() throws InterruptedException {
        System.out.println("Optimistic lock failure demo running");
        Customer customer = new Customer();
        customer.setForename("Charles");
        customer.setSurname("Dickens");

        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        session.save(customer);
        tx.commit();
        System.out.println("After initial save, customer has version number: " + customer.getRev());

        Semaphore semaphore = new Semaphore();

        System.out.println("Have updated surname and forename");

        UpdateCustomerForenameAction updateCustomerForenameAction = new UpdateCustomerForenameAction(customer,"John",semaphore, session);
        UpdateCustomerSurnameAction updateCustomerSurnameAction = new UpdateCustomerSurnameAction("Locke",semaphore,sessionFactory);
        Thread thread1 = new Thread(updateCustomerForenameAction);
        Thread thread2 = new Thread(updateCustomerSurnameAction);
        thread1.start();
        thread2.start();

        int count = 0;
        while ( (!updateCustomerForenameAction.isComplete()|| semaphore.ready) && count < 5 ) {
            System.out.println("Waiting for threads to complete");
            count++;
            Thread.sleep(1000);
        }

    }
}
