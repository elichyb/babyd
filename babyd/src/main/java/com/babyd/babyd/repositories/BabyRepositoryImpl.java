package com.babyd.babyd.repositories;

import com.babyd.babyd.exceptions.EtResourceFoundException;
import com.babyd.babyd.exceptions.EtResourceNotFoundException;
import com.babyd.babyd.models.Baby;
import com.babyd.babyd.models.Parent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.List;
import java.util.UUID;

@Repository
public class BabyRepositoryImpl implements BabyRepository{
    // Insert SQL into baby table
    private static final String SQL_INSERT_TO_BABY = "insert into baby (" +
            "baby_id, first_name, last_name, birth_day, food_type)" +
            "values(?, ?, ?, ?, ?)";

    private static final String SQL_INSERT_TO_PARENT_BABY_TABLE = "insert into parent_baby_relation (parent_id, baby_id) " +
            "values(?,?)";

    private static final String SQL_GET_ALL_BABIES_FOR_PARENT_ID = "" +
            "select * from baby where baby_id in (select baby_id from parent_baby_relation where parent_id=?)";

    private static final String SQL_DELETE_BABY_FORM_BABY_TABLE = "delete from baby where baby_id=?;";

    private static final String SQL_DELETE_BABY_FROM_BABY_PARENT_RELATION = "delete from parent_baby_relation where baby_id=?";

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public List<Baby> fetchAllBabies(UUID parent_id) throws EtResourceNotFoundException {
        try {
            return jdbcTemplate.query(SQL_GET_ALL_BABIES_FOR_PARENT_ID, new Object[]{parent_id}, babyRowMapper);
        }
        catch (Exception e){
            throw new EtResourceNotFoundException("Can't find any baby? ");
        }
    }

    @Override
    public Baby findById(UUID parent_id, UUID baby_id) throws EtResourceNotFoundException {
        return null;
    }

    @Override
    public UUID createBaby(UUID parent_id, String first_name, String last_name, int food_type, String birth_day)
            throws EtResourceFoundException
    {
//        Date bd = conver_to_date(birth_day);
        UUID baby_id = UUID.randomUUID();
        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(SQL_INSERT_TO_BABY, PreparedStatement.RETURN_GENERATED_KEYS);
                ps.setObject(1, baby_id);
                ps.setString(2, first_name);
                ps.setString(3, last_name);
                ps.setString(4, birth_day);
                ps.setInt(   5, food_type);
                return ps;
            });

            // Add row into parent_baby table
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(SQL_INSERT_TO_PARENT_BABY_TABLE, PreparedStatement.RETURN_GENERATED_KEYS);
                    ps.setObject(1, parent_id);
                    ps.setObject(2, baby_id);
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
    public void removeBaby(UUID parent_id, UUID baby_id) throws EtResourceNotFoundException {
        try {
            jdbcTemplate.update(SQL_DELETE_BABY_FORM_BABY_TABLE, baby_id);
            jdbcTemplate.update(SQL_DELETE_BABY_FROM_BABY_PARENT_RELATION, baby_id);
        }
        catch (Exception e){
            throw new EtResourceNotFoundException("No baby to remove");
        }
    }

    private RowMapper<Baby> babyRowMapper = ((rs, rowNum) -> {
        return new Baby(
                (UUID) rs.getObject("baby_id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getInt("food_type"),
                rs.getString("birth_day")
        );
    });
}
