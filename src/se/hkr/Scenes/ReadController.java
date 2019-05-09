package se.hkr.Scenes;

import se.hkr.Model.Model;

public interface ReadController<T extends Model> {

    boolean filter(T model);

    void search();
}

