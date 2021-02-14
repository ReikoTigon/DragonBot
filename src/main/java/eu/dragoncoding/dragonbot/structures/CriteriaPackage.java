package eu.dragoncoding.dragonbot.structures;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class CriteriaPackage<T extends Object & Entity> {
    final CriteriaBuilder criteriaBuilder;
    final CriteriaQuery<T> query;
    final Root<T> root;

    public CriteriaPackage(CriteriaBuilder builder, Class<T> clazz) {
        criteriaBuilder = builder;
        query = builder.createQuery(clazz);
        root = query.from(clazz);
    }

    public CriteriaBuilder getCriteriaBuilder() {
        return criteriaBuilder;
    }
    public CriteriaQuery<T> getQuery() {
        return query;
    }
    public Root<T> getRoot() {
        return root;
    }
}
