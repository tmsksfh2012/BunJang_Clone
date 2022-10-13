package com.example.demo.src.keyword;

import com.example.demo.src.keyword.model.GetKeywordRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class KeywordDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    //유저 상태 조회
    public int checkUserStatusByUserId(String userId) {
        String checkUserStatusByUserIdQuery = "select exists(select * from User where userId = ? and status = 'normal')";
        String checkUserStatusByUserIdParams = userId;
        return this.jdbcTemplate.queryForObject(checkUserStatusByUserIdQuery, int.class, checkUserStatusByUserIdParams);
    }

    //키워드 상태 조회
    public int checkKeywordByUserId(String userId, String keywordId) {
        String checkKeywordByUserIdQuery = "select exists(select * from Keywords where userId = ? and keywordId = ? and status = 'normal')";
        Object[] checkKeywordByUserIdParams = new Object[] {userId, keywordId};
        return this.jdbcTemplate.queryForObject(checkKeywordByUserIdQuery, int.class, checkKeywordByUserIdParams);
    }

    public void postKeyword(String myId, String keyword) {
        String postKeywordQuery = "insert into Keywords(userId, content) values(?, ?)";
        Object[] postKeywordParams = new Object[]{myId, keyword};

        this.jdbcTemplate.update(postKeywordQuery,postKeywordParams);
    }

    public void deleteKeyword(String myId, String keywordId) {
        String postKeywordQuery = "update Keywords set status = 'delete' where userId = ? and keywordId = ?";
        Object[] postKeywordParams = new Object[]{myId, keywordId};

        this.jdbcTemplate.update(postKeywordQuery,postKeywordParams);
    }

    public List<GetKeywordRes> getKeyword(String myId) {
        String getKeywordQuery = "select keywordId, content as keyword from Keywords where userId = ? and status = 'normal'";
        return this.jdbcTemplate.query(getKeywordQuery,
                (rs,rowNum) -> new GetKeywordRes(
                        rs.getInt("keywordId"),
                        rs.getString("keyword")),
                myId);
    }
}
