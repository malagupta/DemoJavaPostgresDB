package com.jb.dao;

import com.jb.vo.Actor;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ActorDAOImpl implements ActorDAO<Actor, Integer> {

    private static final Logger LOGGER = Logger.getLogger(ActorDAOImpl.class.getName());
    private final Optional<Connection> connection;

    public ActorDAOImpl() {
        this.connection = JDBCConnection.getConnection();
    }

    @Override
    public Optional<Actor> get(int id) {
        return connection.flatMap(conn -> {
            Optional<Actor> actor = Optional.empty();
            String sql = "SELECT * FROM actor WHERE actor_id = " + id;

            try (Statement statement = conn.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {

                if (resultSet.next()) {
                    String firstName = resultSet.getString("first_name");
                    String lastName = resultSet.getString("last_name");

                    actor = Optional.of(new Actor(id, firstName, lastName));

                    LOGGER.log(Level.INFO, "Found {0} in database", actor.get());
                }
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }

            return actor;
        });
    }

    @Override
    public Collection<Actor> getAll() {
        Collection<Actor> actors = new ArrayList<>();
        String sql = "SELECT * FROM actor";

        connection.ifPresent(conn -> {
            try (Statement statement = conn.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {

                while (resultSet.next()) {
                    int id = resultSet.getInt("actor_id");
                    String firstName = resultSet.getString("first_name");
                    String lastName = resultSet.getString("last_name");

                    Actor actor = new Actor(id, firstName, lastName);

                    actors.add(actor);

                    LOGGER.log(Level.INFO, "Found {0} in database", actor);
                }

            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        });

        return actors;
    }

    @Override
    public Optional<Integer> save(Actor person) {
        String message = "The Actor to be added should not be null";
        Actor nonNullPerson = Objects.requireNonNull(person, message);
        String sql = "INSERT INTO "
                + "actor(first_name, last_name) "
                + "VALUES(?, ?)";

        return connection.flatMap(conn -> {
            Optional<Integer> generatedId = Optional.empty();

            try (PreparedStatement statement = conn.prepareStatement(sql,
                                                                     Statement.RETURN_GENERATED_KEYS)) {

                statement.setString(1, nonNullPerson.getFirstName());
                statement.setString(2, nonNullPerson.getLastName());

                int numberOfInsertedRows = statement.executeUpdate();

                //Retrieve the auto-generated id
                if (numberOfInsertedRows > 0) {
                    try (ResultSet resultSet = statement.getGeneratedKeys()) {
                        if (resultSet.next()) {
                            generatedId = Optional.of(resultSet.getInt(1));
                        }
                    }
                }

                LOGGER.log(Level.INFO, "{0} created successfully? {1}",
                           new Object[]{nonNullPerson, numberOfInsertedRows > 0});
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }

            return generatedId;
        });
    }

    @Override
    public void update(Actor actor) {
        String message = "The actor to be updated should not be null";
        Actor nonNullPerson = Objects.requireNonNull(actor, message);
        String sql = "UPDATE actor "
                + "SET "
                + "first_name = ?, "
                + "last_name = ? "
                + "WHERE "
                + "actor_id = ?";

        connection.ifPresent(conn -> {
            try (PreparedStatement statement = conn.prepareStatement(sql)) {

                statement.setString(1, nonNullPerson.getFirstName());
                statement.setString(2, nonNullPerson.getLastName());
                statement.setInt(4, nonNullPerson.getId());

                int numberOfUpdatedRows = statement.executeUpdate();

                LOGGER.log(Level.INFO, "Was the actor updated successfully? {0}",
                           numberOfUpdatedRows > 0);

            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        });
    }

    @Override
    public void delete(Actor person) {
        String message = "The person to be deleted should not be null";
        Actor nonNullPerson = Objects.requireNonNull(person, message);
        String sql = "DELETE FROM actor WHERE actor_id = ?";

        connection.ifPresent(conn -> {
            try (PreparedStatement statement = conn.prepareStatement(sql)) {

                statement.setInt(1, nonNullPerson.getId());

                int numberOfDeletedRows = statement.executeUpdate();

                LOGGER.log(Level.INFO, "Was the actor deleted successfully? {0}",
                           numberOfDeletedRows > 0);

            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        });
    }

}
