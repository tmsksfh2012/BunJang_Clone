package com.example.demo.src.search;

import com.example.demo.src.goods.model.CategoryList;
import com.example.demo.src.search.model.GetSearchPopular;
import com.example.demo.src.search.model.GetSearchRecent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class SearchDao {
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


    //검색어 상태 조회
    public int checkSearchStatusBySearchIdUserId(String searchId, String myId) {
        String checkSearchStatusBySearchIdUserIdQuery = "select exists(select * from SearchLog where slid = ? and userId = ? and status = 'normal')";
        Object[] checkSearchStatusBySearchIdUserIdParams = new Object[]{searchId, myId};
        return this.jdbcTemplate.queryForObject(checkSearchStatusBySearchIdUserIdQuery, int.class, checkSearchStatusBySearchIdUserIdParams);
    }

    public List<GetSearchPopular> getSearchPopular() {
        String getSearchPopularQuery = "select slid as searchId,content from SearchLog group by content order by count(content) DESC limit 10";
        return this.jdbcTemplate.query(getSearchPopularQuery,
            (rs, rowNum) -> new GetSearchPopular(
                    rs.getInt("searchId"),
                    rs.getString("content")));

    }

    public List<GetSearchRecent> getSearchRecent(String myId) {
        String getSearchRecentQuery = "select slid as searchId, content from SearchLog where userId = ? and status = 'normal' order by createdAt desc limit 12";
        return this.jdbcTemplate.query(getSearchRecentQuery,
                (rs, rowNum) -> new GetSearchRecent(
                        rs.getInt("searchId"),
                        rs.getString("content")),
                        myId);

    }

    public void deleteRecent(String searchId) {
        String deleteRecentQuery = "update SearchLog set status = 'hidden' where slId = ?";
        this.jdbcTemplate.update(deleteRecentQuery, searchId);
    }
}
