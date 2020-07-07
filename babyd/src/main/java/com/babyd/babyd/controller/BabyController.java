package com.babyd.babyd.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/baby")
public class BabyController {

    @GetMapping("")
    public String getAllBabiesForParent(HttpServletRequest req)
    {
        int id = (int) req.getAttribute("parent_id");
        return "Auth parent id: " + id;
    }

}
