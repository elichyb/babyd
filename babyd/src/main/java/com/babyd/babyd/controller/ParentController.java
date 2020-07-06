package com.babyd.babyd.controller;

import com.babyd.babyd.models.Parent;
import com.babyd.babyd.services.ParentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/parent")
public class ParentController {
    @Autowired
    ParentService parentService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerParent(@RequestBody Map<String, Object> ParentMap)
    {
        String first_name = (String) ParentMap.get("first_name");
        String last_name = (String) ParentMap.get("last_name");
        String email = (String) ParentMap.get("email");
        String password = (String) ParentMap.get("password");

        Parent parent = parentService.registerParent(first_name, last_name, email, password);
        Map<String, String> map = new HashMap<>();
        map.put("message", "register successfully");

        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginParnt(@RequestBody Map<String, Object> parentMap)
    {
        String email = (String) parentMap.get("email");
        String password = (String) parentMap.get("password");
        Parent parent = parentService.validateParent(email, password);

        Map<String, String> map = new HashMap<>();
        map.put("message", "login successfully");

        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @GetMapping("/all_parents")
    public List<Parent> getAllParents()
    {
        System.out.println("---> For debugging");
        return parentService.getAllParents();
    }


    @GetMapping("/delete_all_parents")
    public void deleteAllParents()
    {
        System.out.println("---> only for debugging, should be remove when go production");
        parentService.deleteAllParents();
    }
}
