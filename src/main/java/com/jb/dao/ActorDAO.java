package com.jb.dao;

import com.jb.vo.Actor;

import java.util.Collection;
import java.util.Optional;

public interface ActorDAO<T, I> {

    Optional<Actor> get(int id);

    Collection<T> getAll();

    Optional<I> save(T t);

    void update(T t);

    void delete(T t);
}
