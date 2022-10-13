package com.example.demo.src.regions;

import com.example.demo.src.regions.model.GetLocation;
import com.example.demo.src.regions.model.GetRegionsNameRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Repository
public class RegionsDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){ this.jdbcTemplate = new JdbcTemplate(dataSource);}

    public List<GetRegionsNameRes> getRegionsNameBySearch(String[] search){
        String q0 = "select total1.regionId, total1.provinceName, total1.districtName, total1.regionName\n" +
                "from\n";
        String q1 = "(select RL.regionId ,RL.provinceName, RL.districtName, RL.regionName" +
                "       from RegionList as RL\n" +
                "       left join (select * from UserRegion) UR on RL.regionId = UR.regionId where provinceName like concat('%','";
        String q2 = "','%')\n" +
                "   or districtName like concat('%','";
        String q3 =  "','%')\n" +
                "   or regionName like concat('%','";
        String q4 =  "','%')\n" +
                "group by provinceName, districtName, regionName)";

        String order = "order by provinceName, districtName, regionName\n";

        String getRegionsNameBySearchQuery = q0 +  q1 + search[0] + q2 + search[0] + q3 + search[0] + q4;
        getRegionsNameBySearchQuery += " as total1\n";

        for(int i = 1; i < search.length; i++) {
            getRegionsNameBySearchQuery += "inner join  " + q1 + search[i] + q2 + search[i] + q3 + search[i] + q4
                    + " as total"+(i+1) +" on total"+i+".provinceName = total"+(i+1)+".provinceName and total"+i+".districtName = total"+(i+1)+".districtName and total"+i+".regionName = total"+(i+1)+".regionName\n";
        }
        getRegionsNameBySearchQuery += order;

        return this.jdbcTemplate.query(getRegionsNameBySearchQuery,
                (rs, rowNum) -> new GetRegionsNameRes(
                        rs.getInt("regionId"),
                        rs.getString("provinceName"),
                        rs.getString("districtName"),
                        rs.getString("regionName")
                ));
    }

    public GetRegionsNameRes getRegionsName(int regionId) {
        String getRegionsNamesQuery = "select regionId, provinceName, districtName, regionName from RegionList where regionId = ?";
        return this.jdbcTemplate.queryForObject(getRegionsNamesQuery,
                (rs, rowNum) -> new GetRegionsNameRes(
                        rs.getInt("regionId"),
                        rs.getString("provinceName"),
                        rs.getString("districtName"),
                        rs.getString("regionName")
                ),
                regionId);
    }

    public GetLocation getLocation(int regionId){
        String getLocationQuery = "select regionX, regionY from RegionList where regionId = ? ";
        return this.jdbcTemplate.queryForObject(getLocationQuery,
                (rs, rowNum) -> new GetLocation(
                        rs.getDouble("regionX"),
                        rs.getDouble("regionY")
                ),
                regionId);
    }

    public ArrayList<Integer> getNearRegionsByRange(int range, Double regionX, Double regionY){
        String getNearRegionIdQuery = "SELECT regionId\n" +
                "FROM RegionList\n" +
                "where ((6371 * acos(cos(radians(?)) * cos(radians(regionX)) *\n" +
                "                   cos(radians(regionY) - radians(?)) +\n" +
                "                   sin(radians(?)) * sin(radians(regionX)))) < ?)";
        ArrayList<Integer> list = new ArrayList<>();

        this.jdbcTemplate.query(getNearRegionIdQuery,
                (rs, rowNum) -> list.add(rs.getInt("regionId")), regionX,regionY,regionX, range);
        return list;
    }

    public GetRegionsNameRes getRegionsNameByLocation(GetLocation getLocation) {
        String getRegionsNameByLocationQuery = "SELECT regionId, provinceName, districtName, regionName\n" +
                "FROM RegionList\n" +
                "where (6371 * acos(cos(radians(?)) * cos(radians(regionX)) * cos(radians(regionY) - radians(?)) + sin(radians(?)) * sin(radians(regionX))))\n" +
                "order by (6371 * acos(cos(radians(?)) * cos(radians(regionX)) * cos(radians(regionY) - radians(?)) + sin(radians(?)) * sin(radians(regionX))))\n" +
                "LIMIT 1;";
        Object[] getRegionsNameByLocationParams = new Object[] {getLocation.getRegionX(), getLocation.getRegionY(), getLocation.getRegionX()
                                                                ,getLocation.getRegionX(), getLocation.getRegionY(), getLocation.getRegionX()};
        return this.jdbcTemplate.queryForObject(getRegionsNameByLocationQuery,
                (rs, rowNum) -> new GetRegionsNameRes(
                        rs.getInt("regionId"),
                        rs.getString("provinceName"),
                        rs.getString("districtName"),
                        rs.getString("regionName")
                ),
                getRegionsNameByLocationParams);
    }
}
