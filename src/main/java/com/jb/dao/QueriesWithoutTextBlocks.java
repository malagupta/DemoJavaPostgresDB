package com.jb.dao;

import com.jb.vo.Actor;

public class QueriesWithoutTextBlocks {
    String updateActorUsingParameter(Actor actor) {
        String query = String.format("UPDATE actor " +
                                     "SET first_name = '%s' " +
                                     "AND last_name = '%s' " +
                                     "WHERE actor_id = %d ",
                                     actor.getFirstName(),
                                     actor.getLastName(),
                                     actor.getId());
        return query;
    }

    String updateActorNoParameters() {
        String query = "UPDATE actor" +
                       " SET first_name = 'Joe', last_name = 'Annam' " +
                       "WHERE actor_id = 145";
        return query;
    }

    String simpleSelect() {
        String query = "SELECT first_name, last_name" +
                       "FROM actor " +
                       "WHERE first_name LIKE 'A%'";
        return query;
    }

    String simpleSelectQuery() {
        String query = "SELECT first_name, " +
                       "last_name " +
                       "FROM actor " +
                       "WHERE first_name LIKE '%A'";
        return query;
    }
}
