package com.babyd.babyd.controller;

import com.babyd.babyd.models.Baby;
import com.babyd.babyd.services.BabyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/baby")
public class BabyController {

    @Autowired
    BabyService babyService;

    @GetMapping("/get_my_babies")
    public ResponseEntity<List<Baby>> fetchAllBabies(HttpServletRequest req)
    {
        UUID parent_id = UUID.fromString((String) req.getAttribute("parent_id"));
        List<Baby> babies = babyService.fetchAllBabies(parent_id);
        return new ResponseEntity<>(babies, HttpStatus.OK);
    }

    @DeleteMapping("/remove_baby")
    public ResponseEntity<Map<String, String>> removeBaby(HttpServletRequest req, @RequestBody UUID baby_id)
    {
        UUID parent_id = UUID.fromString((String) req.getAttribute("parent_id"));
        babyService.removeBaby(parent_id, baby_id);
        Map<String,String> map = new HashMap<>();
        map.put("Remove baby:", baby_id.toString());
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @PostMapping("/add_baby")
    public ResponseEntity<Map<String, String>> addBaby(HttpServletRequest request,
                                                       @RequestBody Map<String, Object> babyMap)
    {
        UUID parent_id = UUID.fromString((String) request.getAttribute("parent_id"));
        String first_name = (String) babyMap.get("first_name");
        String last_name = (String) babyMap.get("last_name");
        int food_type = (Integer) babyMap.get("food_type");
        String birth_day = (String) babyMap.get("baby_birth_day");
        double weight = Double.parseDouble((String) babyMap.get("weight"));

        Baby baby = babyService.addBaby(parent_id, first_name, last_name, food_type, birth_day, weight);
        Map<String,String> map = new HashMap<>();
        map.put("successfuly add baby", baby.getId().toString());
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @PostMapping("/get_baby_by_id")
    public ResponseEntity<Baby> findById(HttpServletRequest request,
                                                       @RequestBody Map<String, Object> babyMap)
    {
        UUID parent_id = UUID.fromString((String) request.getAttribute("parent_id"));
        UUID baby_id = UUID.fromString((String) babyMap.get("baby_id"));
        Baby baby = babyService.fetchBabyById(parent_id, baby_id);
        return new ResponseEntity<>(baby, HttpStatus.OK);
    }
}


