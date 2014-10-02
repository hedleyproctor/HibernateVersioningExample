package com.hedleyproctor;

import org.hibernate.Session;
import org.hibernate.Transaction;

public class UpdateCustomerForenameAction implements Runnable {

    private Customer customer;
    private String forename;
    private Semaphore semaphore;
    private Session session;
    private boolean isComplete = false;

    public UpdateCustomerForenameAction(Customer customer, String forename, Semaphore semaphore, Session session) {
        this.customer = customer;
        this.forename = forename;
        this.semaphore = semaphore;
        this.session = session;
        System.out.println("On creation of UpdateCustomerForenameAction, customer has rev number: " + customer.getRev());
    }

    @Override
    public void run() {
        while (!semaphore.ready) {
            System.out.println("UpdateCustomerForenameAction. Not ready yet.");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("UpdateCustomerForenameAction attempting to update");
        Transaction tx = session.beginTransaction();
        customer.setForename(forename);
        tx.commit();
        isComplete = true;
    }

    public boolean isComplete() {
        return isComplete;
    }
}
