package com.babyd.babyd.repositories;

import com.babyd.babyd.exceptions.EtAuthException;
import com.babyd.babyd.exceptions.EtResourceFoundException;
import com.babyd.babyd.exceptions.EtResourceNotFoundException;
import com.babyd.babyd.models.Baby;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class BabyRepositoryImpl implements BabyRepository{
    // Insert SQL into baby table
    private static final String SQL_INSERT_TO_BABY = "insert into baby (baby_id, first_name, last_name, feed_type, wight, birth_day)" +
            "values(NEXTVAL('baby_seq'), ?, ?, ?, convert(date, ?, 103))";

    private static final String SQL_INSERT_TO_PARENT_BABY_TABL = "insert into parent_baby (parent_id, baby_id) " +
            "values(?,?)";

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public List<Baby> findAll(int parent_id) throws EtResourceNotFoundException {
        return null;
    }

    @Override
    public Baby findById(int parent_id, int baby_id) throws EtResourceNotFoundException {
        return null;
    }

    @Override
    public int createBaby(int parent_id, String first_name, String last_name, int feed_type, float wight, String birth_day)
            throws EtResourceFoundException
    {
        // Make insert into baby table
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(SQL_INSERT_TO_BABY, PreparedStatement.RETURN_GENERATED_KEYS);
                ps.setString(1, first_name);
                ps.setString(2, last_name);
                ps.setInt(   3, feed_type);
                ps.setString(4, birth_day);
                return ps;
            }, keyHolder);

            int baby_id = (Integer) keyHolder.getKeys().get("baby_id");

            // Add row into parent_baby table
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(SQL_INSERT_TO_BABY, PreparedStatement.RETURN_GENERATED_KEYS);
                ps.setInt(1, parent_id);
                ps.setInt(2, baby_id);
                return ps;
                });

            //todo Create tables for feed and wight

            return baby_id;
        }
        catch ( Exception e ) {
            throw new EtResourceFoundException("Invalid details, failed to create Baby");
        }
    }


    @Override
    public void removeBaby(int parent_id, int baby_id) throws EtResourceNotFoundException {

    }
}
