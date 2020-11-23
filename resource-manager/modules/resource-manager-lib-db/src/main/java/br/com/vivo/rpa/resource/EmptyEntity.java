package br.com.vivo.rpa.resource;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

// quarkus needs a declared entity to boot the datasource

@Entity
public class EmptyEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        return this == o || getClass().isInstance(o) && Objects.equals(getId(), ((EmptyEntity) o).getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
