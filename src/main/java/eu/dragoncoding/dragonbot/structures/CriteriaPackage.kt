package eu.dragoncoding.dragonbot.structures

import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Root

class CriteriaPackage<T>(val criteriaBuilder: CriteriaBuilder, clazz: Class<T>) where T : Any, T : Entity {
    val query: CriteriaQuery<T> = criteriaBuilder.createQuery(clazz)
    val root: Root<T> = query.from(clazz)
}