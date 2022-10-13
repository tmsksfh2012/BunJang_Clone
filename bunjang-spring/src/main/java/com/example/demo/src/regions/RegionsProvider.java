package com.example.demo.src.regions;

import com.example.demo.config.BaseException;
import com.example.demo.src.regions.model.GetLocation;
import com.example.demo.src.regions.model.GetNearRegionsListRes;
import com.example.demo.src.regions.model.GetRegionsNameRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.example.demo.config.BaseResponseStatus.GET_REGIONS_FAIL;

@Service
public class RegionsProvider {
    private final RegionsDao regionsDao;

    @Autowired
    public RegionsProvider(RegionsDao regionsDao) {
        this.regionsDao = regionsDao;
    }

    public List<GetRegionsNameRes> getRegionsNameBySearch(String search) throws BaseException {
        String[] search_list = search.split(" ");
        List<GetRegionsNameRes> getRegionsNameResList;
        try {
            getRegionsNameResList = regionsDao.getRegionsNameBySearch(search_list);
            return getRegionsNameResList;
        } catch (Exception exception) {
            throw new BaseException(GET_REGIONS_FAIL);
        }
    }

    public GetRegionsNameRes getRegionsName(int regionId) throws BaseException {
        try {
            GetRegionsNameRes regionsName = regionsDao.getRegionsName(regionId);
            return regionsName;
        } catch (Exception exception) {
            throw new BaseException(GET_REGIONS_FAIL);
        }
    }

    public GetNearRegionsListRes getNearRegions(int regionId) throws BaseException {
        GetLocation getLocation;
        ArrayList<Integer> range1;
        ArrayList<Integer> range2;
        ArrayList<Integer> range3;
        ArrayList<Integer> range4;

        try {
            getLocation = regionsDao.getLocation(regionId);
            range1 = regionsDao.getNearRegionsByRange(2, getLocation.getRegionX(), getLocation.getRegionY());
            range2 = regionsDao.getNearRegionsByRange(4, getLocation.getRegionX(), getLocation.getRegionY());
            range3 = regionsDao.getNearRegionsByRange(6, getLocation.getRegionX(), getLocation.getRegionY());
            range4 = regionsDao.getNearRegionsByRange(8, getLocation.getRegionX(), getLocation.getRegionY());
        } catch (Exception exception) {
            throw new BaseException(GET_REGIONS_FAIL);
        }
        GetNearRegionsListRes getNearRegionsListRes = new GetNearRegionsListRes(range1, range2, range3, range4);
        return getNearRegionsListRes;
    }

    public GetRegionsNameRes getRegionsNameByLocation(GetLocation getLocation) throws BaseException {
        try {
            GetRegionsNameRes regionsNameByLocation = regionsDao.getRegionsNameByLocation(getLocation);
            return regionsNameByLocation;
        } catch (Exception exception) {
            throw new BaseException(GET_REGIONS_FAIL);
        }
    }
}
