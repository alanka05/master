package at.spengergasse.vaadin.core.domain;

import java.io.Serializable;

/**
 * @author der SLM
 */
public interface Entity<Id> extends Serializable {
    Id getId();
    void setId(Id id);
}
