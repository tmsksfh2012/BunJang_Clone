package com.example.demo.src.sms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class SMSDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public int checkUserStatusByUserId(String userId){
        String checkUserStatusByUserIdIdQuery = "select exists(select userId from User where userId = ? and status = 'normal')";
        String checkUserStatusByUserIdParams = userId;
        return this.jdbcTemplate.queryForObject(checkUserStatusByUserIdIdQuery,
                int.class,
                checkUserStatusByUserIdParams);
    }

    public void postSMS(String userId, String content) {
        String postSMSQuery = "insert into SMS(userId, content) values(?, ?)";
        Object[] postSMSParams = new Object[] {userId, content};

        this.jdbcTemplate.update(postSMSQuery, postSMSParams);
    }

    public int postCheckSMS(String userId, String content) {
        String postCheckSMSQuery = "select(exists(select * from SMS where userId = ? and content = ? and status = 'normal'))";
        Object[] postCheckSMSParams = new Object[] {userId, content};

        return this.jdbcTemplate.queryForObject(postCheckSMSQuery, int.class, postCheckSMSParams);
    }

    public int patchSMS(String userId) {
        String postCheckSMSQuery = "update SMS set status = 'delete' where userId = ?";

        return this.jdbcTemplate.update(postCheckSMSQuery, userId);
    }
}
