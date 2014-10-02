package com.hedleyproctor;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class UpdateCustomerSurnameAction implements Runnable {
    private String surname;
    private Semaphore semaphore;
    private SessionFactory sessionFactory;

    public UpdateCustomerSurnameAction(String surname, Semaphore semaphore, SessionFactory sessionFactory) {
        this.surname = surname;
        this.semaphore = semaphore;
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void run() {
        System.out.println("UpdateCustomerSurnameAction");
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        Query query = session.createQuery("from Customer");
        Customer customer = (Customer) query.uniqueResult();
        customer.setSurname(surname);
        semaphore.ready = true;
        tx.commit();
        System.out.println("After surname update, version number is: " + customer.getRev());
    }


}
