package com.babyd.babyd.repositories;

import com.babyd.babyd.exceptions.EtResourceFoundException;
import com.babyd.babyd.exceptions.EtResourceNotFoundException;
import com.babyd.babyd.models.Baby;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Repository
public class BabyRepositoryImpl implements BabyRepository{

    // Insert SQL into baby table
    private static final String SQL_INSERT_TO_BABY = "insert into baby (" +
            "baby_id, first_name, last_name, birth_day, food_type)" +
            "values(?, ?, ?, ?, ?)";

    private static final String SQL_INSERT_TO_PARENT_BABY_TABLE = "insert into " +
            "parent_baby_relation (parent_id, baby_id) " +
            "values(?,?)";

    private static final String SQL_GET_ALL_BABIES_FOR_PARENT_ID = "" +
            "select * " +
            "from baby " +
            "where baby_id in (select baby_id from parent_baby_relation where parent_id=?)";

    private static final String SQL_DELETE_BABY_FORM_BABY_TABLE = "delete from baby where baby_id=?;";

    private static final String SQL_DELETE_BABY_FROM_BABY_PARENT_RELATION = "delete from parent_baby_relation where baby_id=?";

    private static final String SQL_FIND_BABY_BY_ID = "select * " +
            "from baby " +
            "where baby_id in " +
            "(select baby_id from parent_baby_relation where parent_id=? and baby_id=?)";

    private static final String BABY_WEIGHT_TABLE_CREATE = "create table %s (" +
            "weight_date varchar(50) primary key  not null," +
            "weight double precision not null);";

    private static final String UPDATE_INTO_WEIGHT_BABY_TABLE = "insert into %s (weight_date, weight) values (?, ?)";

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
        try {
            return jdbcTemplate.queryForObject(SQL_FIND_BABY_BY_ID, new Object[]{parent_id, baby_id}, babyRowMapper);
        }
        catch (Exception e){
            throw new EtResourceNotFoundException("Can't find any baby? ");
        }
    }

    @Override
    public UUID createBaby(UUID parent_id, String first_name, String last_name, int food_type, String birth_day, double weight)
            throws EtResourceFoundException
    {
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

            handle_baby_weight(baby_id, weight);

            return baby_id;
        }
        catch ( Exception e ) {
            throw new EtResourceFoundException("Invalid details, failed to create Baby");
        }
    }

    private void handle_baby_weight(UUID uuid, double weight) throws EtResourceFoundException{
        String table_name = String.format("baby_%s_weight", String.valueOf(uuid.getLeastSignificantBits()).substring(1));

        // Create table weight
        String create_table_weight = String.format(BABY_WEIGHT_TABLE_CREATE, table_name);
        String update_weight_table = String.format(UPDATE_INTO_WEIGHT_BABY_TABLE, table_name);

        try {
        jdbcTemplate.execute(create_table_weight);
        update_baby_weight(uuid, weight);
        }

        catch (Exception e)
        {
            throw new EtResourceFoundException("Failed to create baby's weight table");
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

    @Override
    public void update_baby_weight(UUID baby_id, double weight) throws EtResourceNotFoundException {
        String table_name = String.format("baby_%s_weight", String.valueOf(baby_id.getLeastSignificantBits()).substring(1));
        String update_weight_table = String.format(UPDATE_INTO_WEIGHT_BABY_TABLE, table_name);
        try {
            //Get today date
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String today = LocalDateTime.now().toString();

            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(update_weight_table, PreparedStatement.RETURN_GENERATED_KEYS);
                ps.setString(1, today);
                ps.setDouble(2, weight);
                return ps;
            });
        }
        catch (Exception e ){
            throw new EtResourceNotFoundException("Failed to update baby's weight");
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
