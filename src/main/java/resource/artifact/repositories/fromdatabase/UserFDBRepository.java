package resource.artifact.repositories.fromdatabase;

import resource.artifact.domains.DataBaseConnectInfo;
import resource.artifact.domains.Tuple;
import resource.artifact.domains.User;
import resource.artifact.domains.validators.Validator;
import resource.artifact.repositories.PagingRepository;
import resource.artifact.utils.page.Page;
import resource.artifact.utils.page.Pageable;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("ALL")
public class UserFDBRepository implements PagingRepository<Long, User> {

    Validator<User> validator;
    private Connection dataBaseConnection;

    public UserFDBRepository(Validator<User> validator, DataBaseConnectInfo connectInfo) {
        this.validator = validator;
        connect(connectInfo);
    }

    private void connect(DataBaseConnectInfo connectInfo) {
        try{
            if(dataBaseConnection == null || dataBaseConnection.isClosed()){
                Class.forName("org.postgresql.Driver");
                dataBaseConnection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/" +
                        connectInfo.getDataBaseName(), connectInfo.getHost(),connectInfo.getPassword());
                if(dataBaseConnection != null){
                    System.out.println("Connection established!");
                }
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }


    private Tuple<String, List<Object>> toSql(UserFilterDTO filter) {
        if (filter == null) {
            return new Tuple<>("", Collections.emptyList());
        }
        List<String> conditions = new ArrayList<>();
        List<Object> params = new ArrayList<>();

        filter.getUsers().forEach(user -> {
            conditions.add("id = ?");
            params.add(user);
        });
        String sql = String.join(" or ", conditions);
        sql = "(" + sql + ")";

        if(filter.getFirstName() != "") {
            sql += " and nume like ? ";
            params.add("%" + filter.getFirstName() + "%");
        }
        if(filter.getLastName() != "") {
            sql += " and prenume like ? ";
            params.add("%" + filter.getLastName() + "%");
        }
        return new Tuple<>(sql, params);
    }

    private int count( UserFilterDTO filter) throws SQLException {
        String sql = "select count(*) as count from users";
        Tuple<String, List<Object>> sqlFilter = toSql(filter);
        if (!sqlFilter.first().isEmpty()) {
            sql += " where " + sqlFilter.first();
        }
        try (PreparedStatement statement = dataBaseConnection.prepareStatement(sql)) {
            int paramIndex = 0;
            for (Object param : sqlFilter.last()) {
                statement.setObject(++paramIndex, param);
            }
            try (ResultSet result = statement.executeQuery()) {
                int totalNumberOfUsers = 0;
                if (result.next()) {
                    totalNumberOfUsers = result.getInt("count");
                }
                return totalNumberOfUsers;
            }
        }
    }

    @Override
    public Optional<User> findOne(Long id) {
        try (PreparedStatement statement = dataBaseConnection.prepareStatement("select * from users where id = ?")) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                User user = new User(resultSet.getString("nume"),
                        resultSet.getString("prenume"),
                        resultSet.getString("password"),
                        resultSet.getString("username"));
                user.setId(resultSet.getLong("id"));
                return Optional.of(user);
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<User> findLast() {
        try (PreparedStatement statement = dataBaseConnection.prepareStatement("select * from users order by id desc")) {
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(new User(resultSet.getString("nume"),
                        resultSet.getString("prenume"),
                        resultSet.getString("password"),
                        resultSet.getString("username")));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Iterable<User> findAll() {
        List<User> users = new ArrayList<>();
        try(PreparedStatement statement = dataBaseConnection.prepareStatement("select * from users")){
             ResultSet resultSet = statement.executeQuery();
             while (resultSet.next()) {
                User user = new User(resultSet.getString("nume"),
                        resultSet.getString("prenume"),
                        resultSet.getString("password"),
                        resultSet.getString("username"));
                user.setId(resultSet.getLong("id"));
                users.add(user);
            }
            return users;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<User> save(User entity) {
        entity.setId(1L);
        validator.validate(entity);
        findAll().forEach(user -> {
            if (user.getUsername().equals(entity.getUsername())) {
                throw new RuntimeException("Username already exists");
            }
        });

        String insertSQL = "insert into users (nume, prenume, password, username) values (?, ?, ?, ?)";
        try (PreparedStatement statement = dataBaseConnection.prepareStatement(insertSQL)) {
            statement.setString(1, entity.getFirstName());
            statement.setString(2, entity.getLastName());
            statement.setString(3, entity.getPassword());
            statement.setString(4, entity.getUsername());
            int response = statement.executeUpdate();
            return response == 0 ? Optional.empty() : Optional.of(entity);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<User> delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
        String deleteSQL = "delete from users where id = ?";
        try (PreparedStatement statement = dataBaseConnection.prepareStatement(deleteSQL)) {
            statement.setLong(1, id);
            Optional<User> foundUser = findOne(id);
            int response = 0;
            if (foundUser.isPresent()) {
                response = statement.executeUpdate();
            }
            return response == 0 ? Optional.empty() : foundUser;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<User> update(User entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity cannot be null");
        }
        String updateSQL = "update users set nume = ?, prenume = ?, password = ? , username = ? where id = ?";
        try (PreparedStatement statement = dataBaseConnection.prepareStatement(updateSQL);) {
            statement.setString(1, entity.getFirstName());
            statement.setString(2, entity.getLastName());
            statement.setString(3, entity.getPassword());
            statement.setString(4, entity.getUsername());
            statement.setLong(5, entity.getId());
            int response = statement.executeUpdate();
            return response == 0 ? Optional.empty() : Optional.of(entity);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public Page<User> findAllOnPage(Pageable pageable) {
        return findAllOnPage(pageable, null);
    }

    public Page<User> findAllOnPage(Pageable pageable, UserFilterDTO filter) {
        String sql = "select * from users ";
        Tuple<String, List<Object>> sqlFilter = toSql(filter);
        if (!sqlFilter.first().isEmpty()) {
            sql += "where " + sqlFilter.first();
        }
        sql += " limit ? offset ?";
        try (PreparedStatement statement = dataBaseConnection.prepareStatement(sql)) {
            int paramIndex = 0;
            for (Object param : sqlFilter.last()) {
                statement.setObject(++paramIndex, param);
            }
            statement.setInt(++paramIndex, pageable.getPageSize());
            statement.setInt(++paramIndex, pageable.getPageNumber() * pageable.getPageSize());

            try (ResultSet resultSet = statement.executeQuery()) {
                List<User> users = new ArrayList<>();
                while (resultSet.next()) {
                    User user = new User(resultSet.getString("nume"),
                            resultSet.getString("prenume"),
                            resultSet.getString("password"),
                            resultSet.getString("username"));
                    user.setId(resultSet.getLong("id"));
                    users.add(user);
                }
                return new Page<>(users, count(filter));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
