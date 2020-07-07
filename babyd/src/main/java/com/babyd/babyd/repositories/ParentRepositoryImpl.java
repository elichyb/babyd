package com.babyd.babyd.repositories;

import com.babyd.babyd.exceptions.EtAuthException;
import com.babyd.babyd.models.Parent;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import java.sql.PreparedStatement;
import java.util.List;


@Repository
public class ParentRepositoryImpl implements ParentRepository {

    private static final String SQL_INSERT_TO_PARENT = "insert into parent (parent_id, first_name, last_name, email, password) " +
            "values (NEXTVAL('parent_seq'), ?, ?, ?, ?)";
    private static final String SQL_GET_ALL_PARENTS = "select *from parent";
    private static final String SQL_COUNT_BY_EMAIL = "select count(*) from parent where email=?";
    private static final String SQL_FIND_BY_PARENT_ID = "select parent_id, first_name, last_name, email, password from parent where parent_id=?";
    private static final String SQL_FIND_BY_PARENT_EMAIL = "select parent_id, first_name, last_name, email, password from parent where email=?";
    private static final String SQL_DELETE_ALL_PARENTS = "delete from parent";


    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public int createParent(String first_name, String last_name, String email, String password) throws EtAuthException {
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(10));
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(SQL_INSERT_TO_PARENT, PreparedStatement.RETURN_GENERATED_KEYS);
                ps.setString(1, first_name);
                ps.setString(2, last_name);
                ps.setString(3, email);
                ps.setString(4, hashedPassword);
                return ps;
            }, keyHolder);
            return (Integer) keyHolder.getKeys().get("parent_id");
        }
        catch ( Exception e ) {
            throw new EtAuthException("Invalid details, failed to create account");
        }
    }

    @Override
    public Parent findByEmailAndPassword(String email, String password) throws EtAuthException {
        try
        {
            Parent parent = jdbcTemplate.queryForObject(SQL_FIND_BY_PARENT_EMAIL, new Object[] {email}, parentRowMapper);
            if (! BCrypt.checkpw(password, parent.getPassword()))
                throw new EtAuthException("invalid email or password");
            return parent;
        }
        catch (EmptyResultDataAccessException e){
            throw new EtAuthException("invalid email or password");
        }
    }

    @Override
    public Integer getCountByEmail(String email) {
        return jdbcTemplate.queryForObject(SQL_COUNT_BY_EMAIL, new Object[] {email}, Integer.class);
    }

    @Override
    public Parent findParentById(int id) {
        return jdbcTemplate.queryForObject(SQL_FIND_BY_PARENT_ID, new Object[] {id}, parentRowMapper);
    }

    @Override
    public List<Parent> getAllParents() {
        return jdbcTemplate.query(
                                SQL_GET_ALL_PARENTS, parentRowMapper
//                                (rs, rowNum) ->
//                                new Parent(
//                                        rs.getInt("parent_id"),
//                                        rs.getString("first_name"),
//                                        rs.getString("last_name"),
//                                        rs.getString("email"),
//                                        rs.getString("password")
//                                )
                            );
    }

    @Override
    public void deleteAllParents() {
        try {
            jdbcTemplate.execute(SQL_DELETE_ALL_PARENTS);
        }
        catch ( Exception e ) {
            throw new EtAuthException("Couldn't delete all parents");
        }
    }

    private RowMapper<Parent> parentRowMapper = ((rs, rowNum) -> {
        return new Parent(
                rs.getInt("parent_id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("email"),
                rs.getString("password")
        );
    });
}
