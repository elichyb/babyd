package com.babyd.babyd.repositories;

import com.babyd.babyd.exceptions.EtResourceFoundException;
import com.babyd.babyd.exceptions.EtResourceNotFoundException;
import com.babyd.babyd.models.Baby;
import com.babyd.babyd.models.BabyFullInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
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

    private static final String SQL_BABY_WEIGHT_TABLE_CREATE = "create table %s (" +
            "weight_date varchar(50) primary key  not null," +
            "weight double precision not null);";

    private static final String SQL_GET_BABY_WEIGHT = "seleft * from %s";

    private static final String SQL_INSERT_INTO_WEIGHT_BABY_TABLE = "insert into %s (weight_date, weight) values (?, ?)";

    private static final String SQL_CREATE_TABLE_BABY_FULL_INFO = "create table %s (" +
            "date_measure varchar(50) not null," +
            "time_measure varchar(50) not null," +
            "baby_weight double precision not null," +
            "wet_diaper BIT," +
            "dirty_diaper BIT," +
            "feed_amount integer," +
            "breast_side varchar(50)," +
            "breast_feeding_time_length integer," +
            "sleeping_time integer," +
            "feed_type varchar(50)" +
            "CONSTRAINT B_I_KEY primary key (date_measure, time_measure)" +
            ");";

    private static final String SQL_INSERT_BABY_INFO_TABLE = "insert into %s (date_measure, time_measure, baby_weight, wet_diaper," +
            "dirty_diaper, feed_amount, breast_side, breast_feeding_time_length, sleeping_time, feed_type) " +
            "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String SQL_GET_BABY_INFO = "select * " +
            "from %s " +
            "where date_measure=?";

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

            handle_baby_weight_table(baby_id, weight);
            handle_baby_full_info_table(baby_id);

            return baby_id;
        }
        catch ( Exception e ) {
            throw new EtResourceFoundException("Invalid details, failed to create Baby");
        }
    }

    private void handle_baby_full_info_table(UUID baby_id) {
        String table_name = String.format("baby_%s_full_info", String.valueOf(baby_id.getLeastSignificantBits()).substring(1));

        // Create table baby full info
        String create_table_baby_full_info = String.format(SQL_CREATE_TABLE_BABY_FULL_INFO, table_name);
        try {
            jdbcTemplate.execute(create_table_baby_full_info);
        }
        catch (Exception e)
        {
            throw new EtResourceFoundException("Failed to create baby's full info table");
        }
    }

    private void handle_baby_weight_table(UUID uuid, double weight) throws EtResourceFoundException{
        String table_name = String.format("baby_%s_weight", String.valueOf(uuid.getLeastSignificantBits()).substring(1));

        // Create table weight
        String create_table_weight = String.format(SQL_BABY_WEIGHT_TABLE_CREATE, table_name);

        try {
            jdbcTemplate.execute(create_table_weight);
            String today = get_today();

            update_baby_weight(uuid, weight, today);
        }

        catch (Exception e)
        {
            throw new EtResourceFoundException("Failed to create baby's weight table");
        }
    }

    private String get_today() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd ");
        return LocalDateTime.now().toString();
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
    public void update_baby_weight(UUID baby_id, double weight, String measure_date) throws EtResourceNotFoundException {
        String table_name = String.format("baby_%s_weight", String.valueOf(baby_id.getLeastSignificantBits()).substring(1));
        String update_weight_table = String.format(SQL_INSERT_INTO_WEIGHT_BABY_TABLE, table_name);
        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(update_weight_table, PreparedStatement.RETURN_GENERATED_KEYS);
                ps.setString(1, measure_date);
                ps.setDouble(2, weight);
                return ps;
            });
        }
        catch (Exception e ){
            throw new EtResourceNotFoundException("Failed to update baby's weight");
        }
    }

    @Override
    public List<BabyFullInfo> getBabyFullInfoForDate(UUID baby_id, String date) {
        String table_name = String.format("baby_%s_full_info", String.valueOf(baby_id.getLeastSignificantBits()).substring(1));
        String get_baby_full_info = String.format(SQL_GET_BABY_INFO,table_name);
        try {
            return jdbcTemplate.query(get_baby_full_info, new Object[]{date}, babyFullInfoRowMapper);
        }
        catch (Exception e)
        {
            throw new EtResourceNotFoundException("Can't find baby info in the DB");
        }
    }

    @Override
    public void setDiaper(UUID baby_id, String measure_date, String measure_time, Boolean wet_diaper, Boolean dirty_diaper) {
        String table_name = String.format("baby_%s_full_info", String.valueOf(baby_id.getLeastSignificantBits()).substring(1));

        // Insert sql into table baby info
        String setBabyDiqper = String.format(SQL_INSERT_BABY_INFO_TABLE, table_name);
        double baby_weight = get_baby_weight(baby_id, measure_date);
        try{
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(setBabyDiqper, PreparedStatement.RETURN_GENERATED_KEYS);
                ps.setString(1, measure_date);  // measure date
                ps.setString(2, measure_time);  // measure time
                ps.setDouble(3, baby_weight);   // baby weight
                ps.setBoolean(4, wet_diaper);   // wet diaper
                ps.setBoolean(5, dirty_diaper); // dirty diaper
                ps.setObject(6, null);        // feed amount
                ps.setObject(7, null);        // breast_side
                ps.setObject(8, null);        // breast_feeding_time_length
                ps.setObject(9, null);        // sleeping_time
                ps.setObject(10, null);       // feed_type
                return ps;
            });
        }
        catch (Exception e){
            throw new EtResourceNotFoundException("Can't set diaper replacement");
        }
    }

    @Override
    public void setSleepingTime(UUID baby_id, String measure_date, String measure_time, int sleeping_time) {
        String table_name = String.format("baby_%s_full_info", String.valueOf(baby_id.getLeastSignificantBits()).substring(1));

        // Insert sql into table baby info
        String setBabyDiqper = String.format(SQL_INSERT_BABY_INFO_TABLE, table_name);
        double baby_weight = get_baby_weight(baby_id, measure_date);

        try{
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(setBabyDiqper, PreparedStatement.RETURN_GENERATED_KEYS);
                ps.setString(1, measure_date);   // measure date
                ps.setString(2, measure_time);   // measure time
                ps.setDouble(3, baby_weight);    // baby weight
                ps.setObject(4, null);         // wet diaper
                ps.setObject(5, null);         // dirty diaper
                ps.setObject(6, null);         // feed amount
                ps.setObject(7, null);         // breast_side
                ps.setObject(8, null);         // breast_feeding_time_length
                ps.setInt(9, sleeping_time);     // sleeping_time
                ps.setObject(10, null);        // feed_type
                return ps;
            });
        }
        catch (Exception e){
            throw new EtResourceNotFoundException("Can't set dipper replacement");
        }
    }

    private double get_baby_weight(UUID baby_id, String measure_date) {
        String table_name = String.format("baby_%s_weight", String.valueOf(baby_id.getLeastSignificantBits()).substring(1));
        String sql = String.format(SQL_GET_BABY_WEIGHT, table_name);
        List<Weight> babyWeight;
        try{
            babyWeight = jdbcTemplate.query(sql, new Object[]{measure_date}, babyWeightRowMapper);
        }
        catch (Exception e)
        {
            return 0.0;
        }

        double weight = 0;
        long get_measure_date = getDateFromString(measure_date).getTime();
        long min = get_measure_date;

        // Get the last baby weight measure.
        for(Weight w : babyWeight){
            long tmp = getDateFromString(w.getDate()).getTime();
            long tmp_diff = get_measure_date - tmp;
            if (min < tmp_diff && get_measure_date > tmp){
                min = tmp_diff;
                weight = w.getBaby_weight();
            }
        }

        return weight;
    }

    private Date getDateFromString(String measure_date){
        String format = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            return sdf.parse(measure_date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
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

    private RowMapper<BabyFullInfo> babyFullInfoRowMapper = ((rs, rowNum) ->{
       return new BabyFullInfo(
               rs.getString("measure_date"),
               rs.getString("measure_time"),
               rs.getDouble("weight"),
               rs.getBoolean("wet_diaper"),
               rs.getBoolean("dirty_diaper"),
               rs.getInt("feed_amount"),
               rs.getString("breast_side"),
               rs.getInt("breast_feeding_time_length"),
               rs.getInt("sleeping_time"),
               rs.getString("feed_type")
        );
    });

    private RowMapper<Weight> babyWeightRowMapper = ((rs,rowNum)->{
        return new Weight(
                rs.getDouble("weight"),
                rs.getString("weight_date")
        );
    });
}
