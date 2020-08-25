/*
 * ----------------------------------------------------------------------------
 *  (C) Copyright Elichy Barak 2020
 *
 *  The source code for this program is not published or other-
 * wise divested of its trade secrets, irrespective of what has
 * been deposited with the U.S. Copyright Office.
 * ----------------------------------------------------------------------------
 */

package com.babyd.babyd.controller;

import com.babyd.babyd.Constants;
import com.babyd.babyd.emailHandlers.MailService;
import com.babyd.babyd.models.Parent;
import com.babyd.babyd.services.ParentService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/parent")
public class ParentController {

    @Autowired
    ParentService parentService;

    @Autowired
    MailService mailService;

    @PostMapping("/register")
    public ResponseEntity<String> registerParent(HttpServletRequest request, @RequestBody Map<String, Object> ParentMap)
    {
        String first_name = (String) ParentMap.get("first_name");
        String last_name = (String) ParentMap.get("last_name");
        String email = (String) ParentMap.get("email");
        String phone = (String) ParentMap.get("phone");
        String password = (String) ParentMap.get("password");
        parentService.registerParent(first_name, last_name, email, phone, password);
        mailService.sendMail(email, first_name);
        return new ResponseEntity<>("Registration completed", HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginParnt(HttpServletRequest request, @RequestBody Map<String, Object> parentMap)
    {
        String email = (String) parentMap.get("email");
        String password = (String) parentMap.get("password");
        Parent parent = parentService.validateParent(email, password);

        return new ResponseEntity<>(generateJWTToken(parent), HttpStatus.OK);
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

    private Map<String, String> generateJWTToken(Parent parent) {
        long timestamp = System.currentTimeMillis();
        String token = Jwts.builder().signWith(SignatureAlgorithm.HS256, Constants.API_SECRET_KEY)
                .setIssuedAt(new Date(timestamp))
                .claim("parent id", parent.getParent_id())
                .claim("first name", parent.getFirst_name())
                .claim("last name", parent.getLast_name())
                .claim("email", parent.getEmail())
                .compact();
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        return map;
    }
}
