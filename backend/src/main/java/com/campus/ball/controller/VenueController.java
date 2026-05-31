package com.campus.ball.controller;

import com.campus.ball.common.Result;
import com.campus.ball.entity.Venue;
import com.campus.ball.repository.VenueRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/venue")
@Api(tags = "场地管理接口")
public class VenueController {
    @Autowired
    private VenueRepository venueRepository;
    
    @GetMapping("/list")
    @ApiOperation("查询所有场地")
    public Result<List<Venue>> listVenues(@RequestParam(required = false) String sportType) {
        List<Venue> venues;
        if (sportType != null && !sportType.isEmpty()) {
            venues = venueRepository.findBySportType(sportType);
        } else {
            venues = venueRepository.findAll();
        }
        return Result.success(venues);
    }
    
    @GetMapping("/detail/{id}")
    @ApiOperation("获取场地详情")
    public Result<Venue> getVenueDetail(@PathVariable Long id) {
        return venueRepository.findById(id)
                .map(Result::success)
                .orElse(Result.error("场地不存在"));
    }

    @PutMapping("/coordinates/{id}")
    @ApiOperation("更新场地坐标")
    public Result<Venue> updateCoordinates(@PathVariable Long id, @RequestBody Map<String, Integer> body) {
        return venueRepository.findById(id).map(venue -> {
            if (body.containsKey("mapX")) venue.setMapX(body.get("mapX"));
            if (body.containsKey("mapY")) venue.setMapY(body.get("mapY"));
            venueRepository.save(venue);
            return Result.success(venue);
        }).orElse(Result.error("场地不存在"));
    }

    @PostMapping("/batch-coordinates")
    @ApiOperation("批量导入或更新场地坐标（按名称匹配）")
    public Result<List<Venue>> batchCoordinates(@RequestBody List<Map<String, Object>> list) {
        List<Venue> result = new ArrayList<>();
        for (Map<String, Object> item : list) {
            String name = (String) item.get("name");
            String sportType = (String) item.get("sportType");
            Integer mapX = item.get("mapX") != null ? ((Number) item.get("mapX")).intValue() : null;
            Integer mapY = item.get("mapY") != null ? ((Number) item.get("mapY")).intValue() : null;
            if (name == null || name.isEmpty()) continue;

            Venue venue = venueRepository.findByName(name).orElse(null);
            if (venue == null) {
                venue = new Venue();
                venue.setName(name);
                venue.setSportType(sportType);
                venue.setMapX(mapX);
                venue.setMapY(mapY);
            } else {
                if (mapX != null) venue.setMapX(mapX);
                if (mapY != null) venue.setMapY(mapY);
                if (sportType != null) venue.setSportType(sportType);
            }
            result.add(venueRepository.save(venue));
        }
        return Result.success(result);
    }
}
