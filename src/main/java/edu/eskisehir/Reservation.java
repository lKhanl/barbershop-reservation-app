package edu.eskisehir;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

public class Reservation {
    private Customer customer;
    private long id;
    private Date date;
    private Time time;
    private int cost;
    private Barber barber;
    private List<Operation> ops;
    private String isDone;

    public Reservation(long id, Date date, Time time, int cost, Barber barber, List<Operation> ops, String isDone) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.cost = cost;
        this.barber = barber;
        this.ops = ops;
        this.isDone = isDone;
    }

    public Reservation(Customer customer, long id, Date date, Time time, int cost, Barber barber, List<Operation> ops, String isDone) {
        this.customer = customer;
        this.id = id;
        this.date = date;
        this.time = time;
        this.cost = cost;
        this.barber = barber;
        this.ops = ops;
        this.isDone = isDone;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public Barber getBarber() {
        return barber;
    }

    public void setBarber(Barber barber) {
        this.barber = barber;
    }

    public List<Operation> getOps() {
        return ops;
    }

    public String getIsDone() {
        return isDone;
    }

    public void setIsDone(String isDone) {
        this.isDone = isDone;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

}
