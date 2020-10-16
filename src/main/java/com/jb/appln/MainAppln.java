package com.jb.appln;

import com.jb.core.NonExistentEntityException;
import com.jb.dao.ActorDAO;
import com.jb.dao.ActorDAOImpl;
import com.jb.vo.Actor;

import java.util.Collection;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainAppln {

    private static final Logger LOGGER = Logger.getLogger(MainAppln.class.getName());
    private static final ActorDAO<Actor, Integer> ACTOR_DAO = new ActorDAOImpl();

    public static void main(String[] args) {
        Actor actor = new Actor();
        try {
            actor = getActor(1);
        } catch (NonExistentEntityException ex) {
            LOGGER.log(Level.WARNING, ex.getMessage());
        }

        addActorsToDB();

        actor.setFirstName("Franklin");
        actor.setLastName("Hudson");
        updateActor(actor);

        getAllActors().forEach(System.out::println);

        deleteActor(actor);
    }

    private static void addActorsToDB() {
        //Test whether a customer can be added to the database
        Actor firstActor = new Actor(301, "Java", "Ora");
        Actor secondActor = new Actor(301, "Shreya", "Gupta");
        Actor thirdActor = new Actor(302, "Pavni", "Gupta");

        addActor(firstActor).ifPresent(firstActor::setId);
        addActor(secondActor).ifPresent(secondActor::setId);
        addActor(thirdActor).ifPresent(thirdActor::setId);
    }

    public static Actor getActor(int id) throws NonExistentEntityException {
        Optional<Actor> actor = ACTOR_DAO.get(id);
        return actor.orElseThrow(NonExistentEntityException::new);
    }

    public static Collection<Actor> getAllActors() {
        return ACTOR_DAO.getAll();
    }

    public static void updateActor(Actor actor) {
        ACTOR_DAO.update(actor);
    }

    public static Optional<Integer> addActor(Actor actor) {
        return ACTOR_DAO.save(actor);
    }

    public static void deleteActor(Actor actor) {
        ACTOR_DAO.delete(actor);
    }
}

