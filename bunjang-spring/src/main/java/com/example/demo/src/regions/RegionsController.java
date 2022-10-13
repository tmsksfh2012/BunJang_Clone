package com.example.demo.src.regions;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.regions.model.GetLocation;
import com.example.demo.src.regions.model.GetNearRegionsListRes;
import com.example.demo.src.regions.model.GetRegionsNameRes;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.isRegexLatitude;
import static com.example.demo.utils.ValidationRegex.isRegexLongitude;

@RestController
@RequestMapping("/regions")
public class RegionsController {

    private final RegionsProvider regionsProvider;
    private final RegionsService regionsService;
    private final JwtService jwtService;

    @Autowired
    public RegionsController(RegionsProvider regionsProvider, RegionsService regionsService, JwtService jwtService) {
        this.regionsProvider = regionsProvider;
        this.regionsService = regionsService;
        this.jwtService = jwtService;
    }

    /**
     * 검색을 통해 동네 리스트 조회
     * [GET] /regions?search={search}
     * @return BaseResponse<List<GetRegionsRes>>
     */
    @ResponseBody
    @GetMapping
    public BaseResponse<List<GetRegionsNameRes>> getRegionsNameBySearch(@RequestParam("search") String search) {
        try {
            int userIdByJwt = jwtService.getUserId();

            List<GetRegionsNameRes> getRegionsNameRes = regionsProvider.getRegionsNameBySearch(search);
            return new BaseResponse<>(getRegionsNameRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 입력받은 regionId를 동네 이름으로 반환하기
     * [GET] /regions/:regionId
     * @return BaseResponse<GetRegionsNameRes>
     */
    @ResponseBody
    @GetMapping("/{regionId}")
    public BaseResponse<GetRegionsNameRes> getRegionsName(@PathVariable("regionId") int regionId) throws BaseException {

        if(regionId < 1 || regionId > 22080){
            return new BaseResponse<>(GET_REGION_EXIST_ERROR);
        }
        try {

            GetRegionsNameRes getTownNameRes = regionsProvider.getRegionsName(regionId);
            return new BaseResponse<>(getTownNameRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 특정 동네의 range 별 근처 동네 리스트 반환
     * [GET] /regions/near?regionId={regionId}
     * @return BaseResponse<GetNearRegionsListRes>
     */
    @ResponseBody
    @GetMapping("/near")
    public BaseResponse<GetNearRegionsListRes> getNearRegions(@RequestParam("regionId") int regionId) throws BaseException {

        if(regionId < 1 || regionId > 22080){
            return new BaseResponse<>(GET_REGION_EXIST_ERROR);
        }

        try{
            GetNearRegionsListRes getNearTownListRes  = regionsProvider.getNearRegions(regionId);
            return new BaseResponse<>(getNearTownListRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 입력받은 위치 좌표를 통해 현재 위치한 동네 이름 반환받기
     * [GET] /regions/location
     * @return BaseResponse<GetRegionsNameRes>
     */
    @ResponseBody
    @GetMapping("/location")
    public BaseResponse<GetRegionsNameRes> getRegionsNameByLocation(@RequestBody GetLocation getLocation) throws BaseException {
        if (!(isRegexLatitude(getLocation.getRegionX()))) {
            return new BaseResponse<>(GET_REGION_LATITUDE_ERROR);
        }

        if (!(isRegexLongitude(getLocation.getRegionY()))) {
            return new BaseResponse<>(GET_REGION_LONGITUDE_ERROR);
        }

        try{
            GetRegionsNameRes getRegionsNameRes  = regionsProvider.getRegionsNameByLocation(getLocation);
            return new BaseResponse<>(getRegionsNameRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
