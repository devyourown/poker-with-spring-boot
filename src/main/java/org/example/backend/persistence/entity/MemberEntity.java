package org.example.backend.persistence.entity;

import javax.persistence.*;

@Entity
public class MemberEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    private String userId;
    @Column
    private String userPassword;
    @Column
    private int money;

    public MemberEntity() {

    }

    public MemberEntity(long id, String userId, String userPassword, int money) {
        this.id = id;
        this.userId = userId;
        this.userPassword = userPassword;
        this.money = money;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public long getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public int getMoney() {
        return money;
    }
}
