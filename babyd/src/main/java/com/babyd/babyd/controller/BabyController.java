package com.babyd.babyd.controller;

import com.babyd.babyd.models.Baby;
import com.babyd.babyd.models.Parent;
import com.babyd.babyd.repositories.BabyRepositoryImpl;
import com.babyd.babyd.services.BabyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/baby")
public class BabyController {

    @Autowired
    BabyService babyService;

//    @GetMapping("")
//    public String getAllBabiesForParent(HttpServletRequest req)
//    {
//        int id = (int) req.getAttribute("parent_id");
//        return "Auth parent id: " + id;
//    }

    @PostMapping("/add_baby")
    public ResponseEntity<Map<String, String>> addBaby(HttpServletRequest request,
                                                       @RequestBody Map<String, Object> babyMap)
    {
        UUID parent_id = UUID.fromString((String) request.getAttribute("parent_id"));
        String first_name = (String) babyMap.get("first_name");
        String last_name = (String) babyMap.get("last_name");
        int feed_type = (Integer) babyMap.get("feed_type");
        float wight = ((Double)babyMap.get("wight")).floatValue();
        String birth_day = (String) babyMap.get("birth_day");

        Baby baby = babyService.addBaby(parent_id, first_name, last_name, feed_type, wight, birth_day);
        Map<String,String> map = new HashMap<>();
        map.put("successfuly add baby", baby.getId().toString());
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

}
